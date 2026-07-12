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

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.layoutviews.Pane
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual

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

  /* @Test
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
  } */

  /*@Test
  fun testBasicLabel() {
    val scene = BoardGameScene(width = 1000, height = 1000)
    val label =
        Label(
            posX = 50,
            posY = 50,
            width = 100,
            text = "Hello, BGW!",
            font = Font(size = 20),
            visual =
                CompoundVisual(
                    ColorVisual.RED,
                    ImageVisual("icon.png"),
                    TextVisual("Icon Text", font = Font(size = 15))))

    scene.addComponents(label)
    app.showGameScene(scene)

    println("Label added to scene. Loading HTML content from port: ${app.headlessEnvironment}")

    tester.load(port = app.headlessEnvironment, width = 1000, height = 1000)
    val labelWeb = tester.getBGWComp(label.id)

    println("Label Web Component: $labelWeb")
    assertVisualsEqual(label.visual, labelWeb)
  } */

    @Test
    fun testPane() {
        val scene = BoardGameScene(width = 1000, height = 1000)
        val pane = HexagonGrid<HexagonView>(
            width = 500,
            height = 500,
            posX = 100,
            posY = 100,
            visual = ColorVisual.LIGHT_GRAY
        )
        val label =
            HexagonView(
                posX = 50,
                posY = 50,
                size = 100,
                visual =
                    CompoundVisual(
                        ColorVisual.RED,
                        ImageVisual("icon.png"),
                        TextVisual("Icon Text", font = Font(size = 15))
                    )
            )
        pane[0, 0] = label

        scene.addComponents(pane)
        app.showGameScene(scene)

        println("Label added to pane. Pane added to scene. Loading HTML content from port: ${app.headlessEnvironment}")

        tester.load(port = app.headlessEnvironment, width = 1000, height = 1000)
        val paneWeb = tester.getBGWComp(pane.id)

        println("Pane Web Component: $paneWeb")
        assertComponentsEqual(pane.components, paneWeb.components)
    }
}
