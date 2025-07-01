/*
 * Copyright 2025 The BoardGameWork Authors
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
import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.support.ui.WebDriverWait

object BGWTester {
  private var webDriver: WebDriver? = null

  init {
    // Set up Chrome driver automatically
    WebDriverManager.chromedriver().setup()
  }

  /**
   * Simple synchronous method to load HTML content from localhost Returns the rendered HTML content
   * after JavaScript execution
   */
  fun loadHTML(port: Int, waitForElement: String? = null, timeoutSeconds: Long = 15): String {
    val url = "http://localhost:$port"

    try {
      // Configure Chrome options for headless testing
      val options =
          ChromeOptions().apply {
            addArguments("--headless") // Run in headless mode
            addArguments("--no-sandbox")
            addArguments("--disable-extensions")
            addArguments("--disable-dev-shm-usage")
            addArguments("--disable-gpu")
            addArguments("--window-size=1000,1000")
            addArguments("--disable-logging")
            addArguments("--disable-dev-shm-usage")
            addArguments("--log-level=3") // Suppress INFO, WARNING, and ERROR
            setExperimentalOption("useAutomationExtension", false)
            setExperimentalOption("excludeSwitches", listOf("enable-automation"))
          }

      webDriver = ChromeDriver(options)
      val driver = webDriver!!

      // Navigate to the page
      driver.get(url)
      driver.manage().window().fullscreen()

      // Wait for page to load and JavaScript to execute
      val wait = WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))

      if (waitForElement != null) {
        // Wait for a specific element to be present (useful for React apps)
        wait.until {
          driver.findElements(org.openqa.selenium.By.cssSelector(waitForElement)).isNotEmpty()
        }
      } else {
        // Wait for document ready state
        wait.until {
          (driver as JavascriptExecutor).executeScript("return document.readyState") == "complete"
        }
        // Additional wait for React components to render
        Thread.sleep(2000)
      }

      // Get the fully rendered HTML
      return driver.pageSource
    } catch (e: Exception) {
      throw RuntimeException("Failed to load HTML from $url: ${e.message}", e)
    } finally {
      // Clean up
      webDriver?.quit()
      webDriver = null
    }
  }

  /**
   * Load HTML with access to WebDriver for interactions Returns pair of (HTML content, WebDriver) -
   * remember to call cleanup() after use
   */
  fun loadHTMLWithDriver(port: Int, timeoutSeconds: Long = 15): Pair<String?, WebDriver> {
    val url = "http://localhost:$port"

    try {
      val options =
          ChromeOptions().apply {
            addArguments("--headless")
            addArguments("--no-sandbox")
            addArguments("--disable-extensions")
            addArguments("--disable-dev-shm-usage")
            addArguments("--disable-gpu")
            addArguments("--window-size=1000,1000")
            addArguments("--disable-logging")
            addArguments("--disable-dev-shm-usage")
            addArguments("--log-level=3") // Suppress INFO, WARNING, and ERROR
            setExperimentalOption("useAutomationExtension", false)
            setExperimentalOption("excludeSwitches", listOf("enable-automation"))
          }

      webDriver = ChromeDriver(options)
      val driver = webDriver!!

      driver.get(url)
      driver.manage().window().fullscreen()

      val wait = WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
      wait.until {
        (driver as JavascriptExecutor).executeScript("return document.readyState") == "complete"
      }
      Thread.sleep(2000) // Wait for React components

      return Pair(driver.pageSource, driver)
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

  fun checkSizes(driver: ChromeDriver) {
    val size = driver.manage().window().size.height
    val rootSize = driver.findElement(By.ByClassName("bgw-root")).size.height
    val sceneSize = driver.findElement(By.ByTagName("bgw_game_scene")).size.height

    if (size != rootSize || size != sceneSize) {
      throw RuntimeException("Scene size mismatch: window=$size, root=$rootSize, scene=$sceneSize")
    }
  }

  fun getWebComponents(port: Int, timeoutSeconds: Long = 15): WebElement {
    val (html, driver) = loadHTMLWithDriver(port, timeoutSeconds)
    requireNotNull(driver) { "WebDriver should not be null after loading HTML" }
    requireNotNull(html) { "HTML content should not be null after loading" }

    checkSizes(driver as ChromeDriver)

    return driver.findElements(By.cssSelector("bgw_game_scene")).first()
  }

  fun getWebComponent(port: Int, timeoutSeconds: Long = 15, id: String): WebElement {
    val (html, driver) = loadHTMLWithDriver(port, timeoutSeconds)
    requireNotNull(driver) { "WebDriver should not be null after loading HTML" }
    requireNotNull(html) { "HTML content should not be null after loading" }

    checkSizes(driver as ChromeDriver)

    return driver.findElement(By.id(id))
  }
}
