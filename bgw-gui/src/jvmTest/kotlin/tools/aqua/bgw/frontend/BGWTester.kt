/*
 * Copyright 2025-2026 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.frontend

import io.github.bonigarcia.wdm.WebDriverManager
import java.time.Duration
import java.time.Instant
import java.util.Date
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.min
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.NoSuchElementException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.ComponentAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.SingleLayerVisual
import tools.aqua.bgw.visual.TextVisual
import tools.aqua.bgw.visual.Visual

class BGWTester(
    private val headless: Boolean = System.getProperty("bgw.test.headless", "true").toBoolean(),
    private val defaultTimeout: Duration = Duration.ofSeconds(15),
) : AutoCloseable {
  private var webDriver: WebDriver? = null
  private var htmlContent: String? = null
  private var sizeMult = 1.0
  private var sceneXOffset = 0.0
  private var sceneYOffset = 0.0

  init {
    if (System.getProperty("webdriver.chrome.driver").isNullOrBlank()) {
      WebDriverManager.chromedriver().setup()
    }
  }

  /**
   * Load HTML with access to WebDriver for interactions Returns pair of (HTML content, WebDriver) -
   * remember to call cleanup() after use
   */
  fun load(
      port: Int,
      timeoutSeconds: Long = defaultTimeout.seconds,
      width: Number,
      height: Number
  ): String {
    val url = "http://localhost:$port"

    try {
      val options =
          ChromeOptions().apply {
            if (headless) addArguments("--headless=new")
            addArguments("--no-sandbox")
            addArguments("--disable-extensions")
            addArguments("--disable-dev-shm-usage")
            addArguments("--disable-gpu")
            addArguments("--window-size=${width.toInt()},${height.toInt()}")
            addArguments("--disable-logging")
            addArguments("--disable-dev-shm-usage")
            addArguments("--log-level=3") // Suppress INFO, WARNING, and ERROR
            setExperimentalOption("useAutomationExtension", false)
            setExperimentalOption("excludeSwitches", listOf("enable-automation"))
          }

      webDriver = ChromeDriver(options)
      val driver = webDriver!!

      driver.get(url)
      val wait = WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
      wait.until {
        (driver as JavascriptExecutor).executeScript("return document.readyState") == "complete"
      }
      wait.until(ExpectedConditions.presenceOfElementLocated(By.className("bgw-root")))
      wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("bgw_game_scene")))

      checkSizes(width, height)

      htmlContent = driver.pageSource
      return driver.pageSource ?: ""
    } catch (e: Exception) {
      webDriver?.quit()
      webDriver = null
      throw RuntimeException("Failed to load HTML with driver from $url: ${e.message}", e)
    }
  }

  fun cleanup() {
    webDriver?.quit()
    webDriver = null
  }

  override fun close() = cleanup()

  /** Waits until [condition] becomes true and reports the last DOM on timeout. */
  fun await(
      message: String,
      timeout: Duration = defaultTimeout,
      condition: BGWTester.() -> Boolean,
  ) {
    val driver = requireDriver()
    try {
      WebDriverWait(driver, timeout).until { condition() }
    } catch (exception: Exception) {
      throw AssertionError("Timed out waiting for $message. DOM: ${driver.pageSource}", exception)
    }
  }

  /** Returns a component when it exists, or `null` while React is still reconciling. */
  fun findBGWComp(id: String): BGWComp? =
      try {
        getBGWComp(id)
      } catch (_: NoSuchElementException) {
        null
      }

  fun awaitBGWComp(id: String, timeout: Duration = defaultTimeout): BGWComp {
    await("BGW component '$id'", timeout) { findBGWComp(id) != null }
    return getBGWComp(id)
  }

  fun awaitMissing(id: String, timeout: Duration = defaultTimeout) {
    await("BGW component '$id' to disappear", timeout) { findBGWComp(id) == null }
  }

  internal fun requireDriver(): WebDriver =
      requireNotNull(webDriver) { "Call load() before interacting with BGWTester." }

  fun checkSizes(originalWidth: Number, originalHeight: Number) {
    val driver = webDriver
    requireNotNull(driver) { "WebDriver should not be null after loading HTML" }

    val rootSize = driver.findElement(By.ByClassName("bgw-root")).size
    val sceneSize = driver.findElement(By.ByTagName("bgw_game_scene")).size
    val sceneLocation = driver.findElement(By.ByTagName("bgw_game_scene")).location

    check(sceneSize.width <= rootSize.width && sceneSize.height <= rootSize.height) {
      "Scene exceeds root bounds: root=$rootSize, scene=$sceneSize"
    }

    val widthScale = sceneSize.width.toDouble() / originalWidth.toDouble()
    val heightScale = sceneSize.height.toDouble() / originalHeight.toDouble()
    check(abs(widthScale - heightScale) < 0.02) {
      "Scene is scaled non-uniformly: widthScale=$widthScale, heightScale=$heightScale"
    }
    sizeMult = min(widthScale, heightScale)
    sceneXOffset = sceneLocation.x.toDouble()
    sceneYOffset = sceneLocation.y.toDouble()
  }

  fun getBGWScene(): WebElement {
    val driver = webDriver
    requireNotNull(driver) { "WebDriver should not be null after loading HTML" }

    return driver.findElements(By.cssSelector("bgw_game_scene")).first()
  }

  fun getBGWComp(id: String): BGWComp {
    val driver = requireDriver()
    val element = driver.findElement(By.id(id))
    requireNotNull(element) { "Element with id '$id' not found in the loaded HTML" }

    val bgwElement = BGWComp(id, element, sizeMult, sceneXOffset, sceneYOffset)

    return bgwElement
  }
}

data class BGWScale(val width: Double, val height: Double)

data class BGWLocation(val x: Double, val y: Double)

class BGWComp(
    val id: String,
    private val webElement: WebElement,
    private var sizeMult: Double,
    private var sceneXOffset: Double,
    private var sceneYOffset: Double
) {

  val tagName: String
    get() = webElement.tagName

  val text: String
    get() = webElement.text

  val isDisplayed: Boolean
    get() = webElement.isDisplayed

  val isEnabled: Boolean
    get() = webElement.isEnabled

  fun click() = webElement.click()

  fun css(name: String): String = webElement.getCssValue(name)

  fun attribute(name: String): String? = webElement.getAttribute(name)

  val size: BGWScale
    get() = webElement.size.let { BGWScale(it.width / sizeMult, it.height / sizeMult) }

  val location: BGWLocation
    get() =
        webElement.location.let {
          BGWLocation((it.x - sceneXOffset) / sizeMult, (it.y - sceneYOffset) / sizeMult)
        }

  /** Untransformed BGW coordinates, useful when scale or rotation changes the rendered bounds. */
  val logicalLocation: BGWLocation
    get() = BGWLocation(cssPixels("left") / sizeMult, cssPixels("top") / sizeMult)

  /** Untransformed BGW size, useful when scale or rotation changes the rendered bounds. */
  val logicalSize: BGWScale
    get() = BGWScale(cssPixels("width") / sizeMult, cssPixels("height") / sizeMult)

  val bounds: Pair<BGWLocation, BGWScale>
    get() =
        BGWLocation(webElement.rect.point.x.toDouble(), webElement.rect.point.y.toDouble()) to
            BGWScale(
                webElement.rect.dimension.width.toDouble() / sizeMult,
                webElement.rect.dimension.height.toDouble() / sizeMult)

  val scale: Pair<Double, Double>
    get() =
        webElement.getCssValue("scale").let {
          val parts = it.split(" ")
          when (parts.size) {
            2 -> {
              Pair(parts[0].toDouble(), parts[1].toDouble())
            }
            1 -> {
              Pair(parts[0].toDouble(), parts[0].toDouble())
            }
            else -> {
              Pair(1.0, 1.0) // Default scale if not specified
            }
          }
        }

  val opacity: Double
    get() = webElement.getCssValue("opacity").toDoubleOrNull() ?: 1.0

  val rotation: Double
    get() =
        webElement
            .getCssValue("rotate")
            .takeUnless { it == "none" }
            ?.let {
              val parts = it.split(" ").map { part -> part.removeSuffix("deg") }
              when (parts.size) {
                1 -> parts[0].toDoubleOrNull() ?: 0.0
                2 -> parts[0].toDoubleOrNull() ?: 0.0 // Assuming second part is the rotation
                else -> 0.0 // Default rotation if not specified
              }
            } ?: rotationFromTransform()

  private fun rotationFromTransform(): Double {
    val transform = webElement.getCssValue("transform")
    if (!transform.startsWith("matrix(")) return 0.0
    val values =
        transform.substringAfter('(').substringBefore(')').split(',').map { it.trim().toDouble() }
    return atan2(values[1], values[0]) * 180.0 / PI
  }

  private fun cssPixels(property: String): Double =
      webElement.getCssValue(property).removeSuffix("px").toDoubleOrNull() ?: 0.0

  val visuals: List<SingleLayerVisual>
    get() {
      val parentElement = webElement.findElements(By.ByTagName("bgw_visuals")).firstOrNull()

      if (parentElement == null) {
        return emptyList()
      }

      val children: List<WebElement> = parentElement.findElements(By.cssSelector(":scope > *"))

      return children.map { visual ->
        when (visual.tagName) {
          "bgw_color_visual" -> {
            val color = visual.getCssValue("background-color")
            val rgba = color.substringAfter('(').substringBeforeLast(')').split(",")
            val r = rgba[0].trim().toInt()
            val g = rgba[1].trim().toInt()
            val b = rgba[2].trim().toInt()
            val a = rgba.getOrNull(3)?.trim()?.toDoubleOrNull() ?: 1.0
            ColorVisual(r, g, b, a)
                .apply { id = visual.getAttribute("id") ?: "" }
                .also { println("Color: rgba($r, $g, $b, $a)") }
          }
          "bgw_image_visual" -> {
            val src =
                visual
                    .getCssValue("background-image")
                    .removePrefix("url(\"")
                    .removeSuffix("\")")
                    .replace(Regex(".+/static/"), "")
            ImageVisual(src)
                .apply { id = visual.getAttribute("id") ?: "" }
                .also { println("Image source: $src") }
          }
          "bgw_text_visual" -> {
            val text = visual.text
            TextVisual(text)
                .apply { id = visual.getAttribute("id") ?: "" }
                .also { println("Text: $text") }
          }
          else -> {
            throw IllegalArgumentException("Unknown visual type: ${visual.tagName}")
          }
        }
      }
    }

  val components: List<BGWComp>
    get() {
      var parentElement = webElement.findElements(By.ByTagName("bgw_contents")).firstOrNull()
      if (parentElement == null) {
        return emptyList()
      }

      val isHexagon = parentElement.findElements(By.ByTagName("bgw_hexagon_content")).firstOrNull()
      var selector = By.cssSelector(":scope > *")
      if (isHexagon != null) {
        selector = By.cssSelector(":scope > bgw_hexagon_content > *")
      }

      val children: List<WebElement> = parentElement.findElements(selector)
      return children.map { child ->
        BGWComp(child.getAttribute("id") ?: "", child, sizeMult, sceneXOffset, sceneYOffset)
      }
    }

  private val animationDuration: Long
    get() =
        webElement.getCssValue("transition-duration").let {
          val parts = it.split(" ")
          parts.find { it.contains("s") }?.removeSuffix("s")?.toLongOrNull() ?: 0L
        }
}

fun assertComponentsEqual(
    expected: List<ComponentView>,
    actual: List<BGWComp>,
    message: String = "Components do not match"
) {
  assertEquals(expected.size, actual.size, "$message: Size mismatch")
  assertContentEquals(expected.map { it.id }, actual.map { it.id }, "$message: ID mismatch")
}

fun assertVisualsEqual(
    visualProperty: Visual,
    element: BGWComp,
    message: String = "Visuals do not match"
) {
  if (visualProperty is CompoundVisual) {
    assertVisualsEqual(visualProperty.children, element.visuals, message)
  } else if (visualProperty is SingleLayerVisual) {
    assertVisualsEqual(listOf(visualProperty), element.visuals, message)
  } else {
    throw IllegalArgumentException("Unsupported visual type: ${visualProperty::class}")
  }
}

fun assertVisualsEqual(
    expected: List<SingleLayerVisual>,
    actual: List<SingleLayerVisual>,
    message: String = "Visuals do not match"
) {
  assertEquals(expected.size, actual.size, "$message: Size mismatch")
  assertContentEquals(expected.map { it.id }, actual.map { it.id }, "$message: Visual-ID mismatch")
  for (i in expected.indices) {
    val expectedVisual = expected[i]
    val actualVisual = actual[i]
    assertEquals(
        expectedVisual::class, actualVisual::class, "$message: Visual type mismatch at index $i")
    when (expectedVisual) {
      is ColorVisual -> {
        assertEquals(
            expectedVisual.color,
            (actualVisual as ColorVisual).color,
            "$message: Color mismatch at index $i")
      }
      is ImageVisual -> {
        assertEquals(
            expectedVisual.path,
            (actualVisual as ImageVisual).path,
            "$message: Image path mismatch at index $i")
      }
      is TextVisual -> {
        assertEquals(
            expectedVisual.text,
            (actualVisual as TextVisual).text,
            "$message: Text mismatch at index $i")
      }
    }
  }
}

fun assertAnimated(
    scene: BoardGameScene,
    animation: ComponentAnimation<*>,
    element: BGWComp,
    property: BGWComp.() -> Any,
    expectedFinishedValue: Any,
    expectedResetValue: Any? = null
) {
  var expectedResetValue: Any? = expectedResetValue
  val oldFinishBlock = animation.onFinished
  if (expectedResetValue == null) {
    expectedResetValue = property(element)
  }

  var finishValue: Any? = null

  scene.playAnimation(
      animation.apply {
        onFinished = { event ->
          finishValue = property(element)
          oldFinishBlock?.invoke(event)
        }
      })

  Thread.sleep((animation.duration + 500).toLong())

  assertRenderedEquals(
      expectedFinishedValue,
      finishValue,
      "Expected value $expectedFinishedValue but got $finishValue after animation finished")

  val afterValue = property(element)
  assertRenderedEquals(
      expectedResetValue,
      afterValue,
      "Expected value $expectedResetValue but got $afterValue after animation reset")
}

/** Compares browser measurements with tolerance for integer CSS pixel rounding. */
fun assertRenderedEquals(expected: Any?, actual: Any?, message: String = "Rendered values differ") {
  when {
    expected is BGWLocation && actual is BGWLocation -> {
      assertAround(expected.x, actual.x, 1.0, "$message (x)")
      assertAround(expected.y, actual.y, 1.0, "$message (y)")
    }
    expected is BGWScale && actual is BGWScale -> {
      assertAround(expected.width, actual.width, 1.0, "$message (width)")
      assertAround(expected.height, actual.height, 1.0, "$message (height)")
    }
    expected is Double && actual is Double -> assertAround(expected, actual, 0.02, message)
    else -> assertEquals(expected, actual, message)
  }
}

fun assertAnimationFinished(scene: BoardGameScene, animation: Animation, gracePeriod: Int = 500) {
  var animationFinished = false
  val animationDuration = animation.duration
  val timestampStart = Date.from(Instant.now())
  var timestampEnd = Date.from(Instant.now())
  val oldFinishBlock = animation.onFinished

  scene.playAnimation(
      animation.apply {
        onFinished = { event ->
          animationFinished = true
          timestampEnd = Date.from(Instant.now())
          oldFinishBlock?.invoke(event)
        }
      })

  Thread.sleep((animation.duration + gracePeriod).toLong())

  assertTrue(animationFinished, "Animation did not call onFinished-Callback")
  assertAround(
      animationDuration.toDouble(),
      (timestampEnd.time - timestampStart.time).toDouble(),
      gracePeriod / 2.0,
      "Animation with duration of ${animationDuration}ms took longer than the expected (${(timestampEnd.time - timestampStart.time).toDouble()}ms)")
}

fun assertAround(
    expectedValue: Double,
    actualValue: Double,
    range: Double,
    message: String =
        "Value $actualValue is not in range [${expectedValue - range}, ${expectedValue + range}]"
) {
  assertInRange(actualValue, expectedValue - range, expectedValue + range, message)
}

fun assertInRange(
    actualValue: Double,
    min: Double,
    max: Double,
    message: String = "Value $actualValue is not in range [$min, $max]"
) {
  assertTrue(actualValue in min..max, message)
}

fun assertLocationEquals(
    expectedValue: BGWLocation,
    actualValue: BGWLocation,
) {
  assertInRange(
      actualValue.x,
      expectedValue.x - 1.0,
      expectedValue.x + 1.0,
      "Expected position $expectedValue but got $actualValue")
  assertInRange(
      actualValue.y,
      expectedValue.y - 1.0,
      expectedValue.y + 1.0,
      "Expected position $expectedValue but got $actualValue")
}
