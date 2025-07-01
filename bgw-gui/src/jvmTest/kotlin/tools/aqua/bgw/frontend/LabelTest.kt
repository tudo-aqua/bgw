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

import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertTrue
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

class TestApplication :
    BoardGameApplication("Test Application", width = 1000, height = 1000, headless = true) {
  init {
    println("Test Application initialized.")
  }
}

val app = TestApplication()

class LabelTest {
  init {
    app.showNonBlocking()
  }

  @Test
  fun testLabelMovement() {
    val scene = BoardGameScene(width = 1000, height = 1000)
    val label =
        Label(
            posX = 50,
            posY = 50,
            width = 100,
            text = "Hello, BGW!",
            font = Font(size = 20),
            visual = ColorVisual.LIGHT_GRAY)

    scene.addComponents(label)
    app.showGameScene(scene)

    println("Label added to scene. Loading HTML content from port: ${app.headlessEnvironment}")

    var animationFinished = false

    val labelWeb = BGWTester.getWebComponent(port = app.headlessEnvironment, id = label.id)

    println(labelWeb.location)

    scene.playAnimation(
        MovementAnimation(componentView = label, byX = 200, duration = 1000).apply {
          onFinished = {
            animationFinished = true
            println(labelWeb.location)
            println("Animation finished successfully.")
          }
        })

    Thread.sleep(1500) // Wait for animation to complete
    println(labelWeb.location)

    println("Checking after animation... ${animationFinished}")

    assertTrue(animationFinished, "Animation should have finished successfully")

    println("✓ All assertions passed for animation label test")
  }

  @Test
  fun testBasicLabel() {
    val scene = BoardGameScene(width = 1000, height = 1000)
    val label =
        Label(
            posX = 50,
            posY = 50,
            width = 100,
            text = "Hello, BGW!",
            font = Font(size = 20),
            visual = ColorVisual.LIGHT_GRAY)

    scene.addComponents(label)
    app.showGameScene(scene)

    println("Label added to scene. Loading HTML content from port: ${app.headlessEnvironment}")

    // Load HTML content synchronously
    val content = BGWTester.loadHTML(port = app.headlessEnvironment)
    BGWTester.getWebComponents(port = app.headlessEnvironment)

    // Now we can use direct assertions!
    println("HTML content loaded successfully. Length: ${content.length} characters")

    // Assert the label text is present
    assertContains(content, "Hello, BGW!", message = "Label text should be present in HTML")

    // Assert minimum content length (indicates JavaScript executed)
    assertTrue(
        content.length > 1000, "Content should be substantial, indicating JavaScript execution")

    // Assert no error indicators
    assertTrue(
        !content.contains("error", ignoreCase = true), "HTML should not contain error messages")

    println("✓ All assertions passed for basic label test")
  }

  @Test
  fun testLabelWithDifferentProperties() {
    val scene = BoardGameScene(width = 300, height = 300)
    val label =
        Label(
            posX = 100,
            posY = 100,
            width = 200,
            text = "Custom Test Label",
            font = Font(size = 16),
            visual = ColorVisual.BLUE)

    scene.addComponents(label)
    app.showGameScene(scene)

    val content = BGWTester.loadHTML(port = app.headlessEnvironment)

    // Test specific content
    assertContains(content, "Custom Test Label")
    assertTrue(content.contains("width", ignoreCase = true), "Width styling should be present")

    println("✓ Custom label test passed")
  }

  @Test
  fun testMultipleLabels() {
    val scene = BoardGameScene(width = 400, height = 400)

    val label1 =
        Label(posX = 10, posY = 10, width = 100, text = "First Label", font = Font(size = 14))
    val label2 =
        Label(posX = 10, posY = 50, width = 100, text = "Second Label", font = Font(size = 14))

    scene.addComponents(label1, label2)
    app.showGameScene(scene)

    val content = BGWTester.loadHTML(port = app.headlessEnvironment)

    // Assert both labels are present
    assertContains(content, "First Label")
    assertContains(content, "Second Label")

    // Count occurrences
    val firstCount = content.split("First Label").size - 1
    val secondCount = content.split("Second Label").size - 1

    assertTrue(firstCount >= 1, "First Label should appear at least once")
    assertTrue(secondCount >= 1, "Second Label should appear at least once")

    println("✓ Multiple labels test passed")
  }
}
