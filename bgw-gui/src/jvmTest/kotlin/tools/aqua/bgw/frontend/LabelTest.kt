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

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
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
  lateinit var tester: BGWTester

  init {
    app.showNonBlocking()
  }

  @BeforeTest
  fun setup() {
    tester = BGWTester()
  }

  @AfterTest
  fun cleanup() {
    tester.cleanup()
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

    tester.load(port = app.headlessEnvironment, width = 1000, height = 1000)
    val labelWeb = tester.getBGWComp(label.id)

    println("Before: ${labelWeb.location}")

    val animation =
        MovementAnimation(componentView = label, byX = 180.0, duration = 1000).apply {
          onFinished = {
            println("Finish: ${labelWeb.location}")
            label.posX += 60.0
          }
        }
    //    assertAnimationFinished(scene, animation)
    assertAnimated(
        scene = scene,
        animation = animation,
        element = labelWeb,
        property = BGWComp::location,
        expectedFinishedValue = BGWLocation(label.posX + 180.0, label.posY),
        expectedResetValue = BGWLocation(label.posX + 50.0, label.posY),
    )

    println("After: ${labelWeb.location}")

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
    val content = tester.load(port = app.headlessEnvironment, width = 1000, height = 1000)
    tester.getBGWScene()

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
}
