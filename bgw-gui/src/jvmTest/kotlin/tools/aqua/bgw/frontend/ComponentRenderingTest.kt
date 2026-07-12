/*
 * Copyright 2026 The BoardGameWork Authors
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
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

/** Browser-level regressions for the JVM -> websocket -> Kotlin/JS -> React rendering pipeline. */
class ComponentRenderingTest {
  private lateinit var tester: BGWTester

  init {
    app.showNonBlocking()
  }

  @BeforeTest
  fun setUp() {
    tester = BGWTester()
  }

  @AfterTest
  fun tearDown() {
    tester.close()
  }

  @Test
  fun componentPropertiesUpdateWithoutReloadingThePage() {
    val scene = BoardGameScene(width = 1000, height = 1000)
    val label =
        Label(
            posX = 40,
            posY = 60,
            width = 180,
            height = 70,
            text = "before",
            visual = ColorVisual.RED)
    scene.addComponents(label)
    app.showGameScene(scene)

    tester.load(app.headlessEnvironment, width = scene.width, height = scene.height)
    val rendered = tester.awaitBGWComp(label.id)
    assertEquals("bgw_label", rendered.tagName)
    assertTrue(rendered.text.contains("before"))
    assertRenderedEquals(BGWLocation(40.0, 60.0), rendered.location)

    label.text = "after"
    label.posX = 230.0
    label.posY = 170.0
    label.width = 260.0
    label.height = 110.0
    label.opacity = 0.4
    label.rotation = 35.0
    label.visual = ColorVisual.BLUE

    tester.await("changed label text and logical bounds") {
      val current = getBGWComp(label.id)
      current.text.contains("after") &&
          current.logicalLocation.x in 229.0..231.0 &&
          current.logicalLocation.y in 169.0..171.0 &&
          current.logicalSize.width in 259.0..261.0 &&
          current.logicalSize.height in 109.0..111.0
    }
    tester.await("changed opacity") { getBGWComp(label.id).opacity == 0.4 }
    tester.await("changed rotation") { getBGWComp(label.id).rotation in 34.9..35.1 }
    tester.await("changed visual") { getBGWComp(label.id).visuals.singleOrNull() is ColorVisual }
    assertVisualsEqual(label.visual, tester.getBGWComp(label.id))
  }

  @Test
  fun sceneAndNestedPaneHierarchyTrackAddRemoveAndReorder() {
    val scene = BoardGameScene(width = 1000, height = 1000)
    val pane = Pane<ComponentView>(posX = 100, posY = 100, width = 500, height = 400)
    val first = Label(posX = 20, posY = 20, width = 120, height = 60, text = "first")
    val second = Label(posX = 180, posY = 20, width = 120, height = 60, text = "second")
    pane.add(first)
    scene.addComponents(pane)
    app.showGameScene(scene)

    tester.load(app.headlessEnvironment, width = scene.width, height = scene.height)
    val renderedPane = tester.awaitBGWComp(pane.id)
    tester.await("initial pane hierarchy") {
      getBGWComp(pane.id).components.map(BGWComp::id) == listOf(first.id)
    }

    pane.add(second)
    tester.await("nested component addition") {
      getBGWComp(pane.id).components.map(BGWComp::id) == listOf(first.id, second.id)
    }

    pane.toFront(first)
    tester.await("nested z-order change") {
      getBGWComp(pane.id).components.map(BGWComp::id) == listOf(second.id, first.id)
    }

    pane.remove(second)
    tester.awaitMissing(second.id)
    assertComponentsEqual(pane.components, renderedPane.components)

    scene.removeComponents(pane)
    tester.awaitMissing(pane.id)
    scene.addComponents(pane)
    tester.awaitBGWComp(pane.id)
  }

  @Test
  fun browserInputTravelsBackToJvmAndRendersTheResult() {
    val scene = BoardGameScene(width = 1000, height = 1000)
    val output = Label(posX = 100, posY = 220, width = 300, height = 70, text = "clicks: 0")
    var clicks = 0
    val button =
        Button(posX = 100, posY = 100, width = 220, height = 80, text = "Click me").apply {
          onMouseClicked = {
            clicks++
            output.text = "clicks: $clicks"
          }
        }
    scene.addComponents(button, output)
    app.showGameScene(scene)

    tester.load(app.headlessEnvironment, width = scene.width, height = scene.height)
    tester.awaitBGWComp(button.id).click()
    tester.await("mouse event round-trip") { getBGWComp(output.id).text.contains("clicks: 1") }
    assertEquals(1, clicks)
  }
}
