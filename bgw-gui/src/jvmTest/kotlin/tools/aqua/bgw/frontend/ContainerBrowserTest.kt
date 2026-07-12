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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.HexOrientation
import tools.aqua.bgw.visual.ColorVisual

class ContainerBrowserTest : BrowserTestBase() {

  @Test
  fun paneSupportsBulkAddRemoveClearAndReinsert() {
    val pane = Pane<ComponentView>(width = 500, height = 350)
    val children = (0..2).map { Label(posX = it * 100, width = 80, height = 50, text = "$it") }
    pane.addAll(children)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(pane) })
    tester.await("all pane children") {
      getBGWComp(pane.id).components.map(BGWComp::id) == children.map { it.id }
    }

    pane.removeAll(listOf(children[0], children[2]))
    tester.await("bulk pane removal") {
      getBGWComp(pane.id).components.map(BGWComp::id) == listOf(children[1].id)
    }

    pane.clear()
    tester.await("cleared pane") { getBGWComp(pane.id).components.isEmpty() }
    pane.add(children[2])
    tester.awaitBGWComp(children[2].id)
  }

  @Test
  fun gridPaneTracksSparseCellsReplacementAndGeometryChanges() {
    val grid = GridPane<ComponentView>(columns = 3, rows = 2, spacing = 5)
    grid.setColumnWidths(100)
    grid.setRowHeights(70)
    val first = Label(width = 80, height = 50, text = "first")
    val replacement = Label(width = 90, height = 55, text = "replacement")
    grid[0, 0] = first
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(grid) })
    tester.awaitBGWComp(first.id)

    grid[0, 0] = replacement
    tester.awaitMissing(first.id)
    tester.awaitBGWComp(replacement.id)

    grid.setColumnWidth(0, 160)
    grid.setRowHeight(0, 110)
    tester.await("grid geometry update") {
      val current = getBGWComp(grid.id)
      current.logicalSize.width > 350 && current.logicalSize.height > 180
    }

    grid.clear()
    tester.awaitMissing(replacement.id)
  }

  @Test
  fun areaMaintainsFreePositionedChildHierarchy() {
    val area = Area<TokenView>(width = 450, height = 300)
    val first = TokenView(posX = 20, posY = 30, width = 70, height = 70, visual = ColorVisual.RED)
    val second =
        TokenView(posX = 160, posY = 90, width = 80, height = 80, visual = ColorVisual.BLUE)
    area.addAll(first, second)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(area) })
    tester.await("area hierarchy") {
      getBGWComp(area.id).components.map(BGWComp::id) == listOf(first.id, second.id)
    }
    assertRenderedEquals(BGWLocation(160.0, 90.0), tester.getBGWComp(second.id).logicalLocation)

    area.toBack(second)
    tester.await("area reorder") { getBGWComp(area.id).components.first().id == second.id }
  }

  @Test
  fun cardStackPushPopAndAlignmentReachRenderedHierarchy() {
    val stack = CardStack<CardView>(width = 180, height = 250)
    val first = CardView(width = 120, height = 180, front = ColorVisual.RED)
    val second = CardView(width = 100, height = 160, front = ColorVisual.BLUE)
    stack.push(first)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(stack) })
    tester.awaitBGWComp(first.id)

    stack.push(second)
    tester.await("card pushed") {
      getBGWComp(stack.id).components.map(BGWComp::id) == listOf(first.id, second.id)
    }
    assertRenderedEquals(
        BGWLocation(second.posX, second.posY), tester.getBGWComp(second.id).logicalLocation)

    assertEquals(second, stack.pop())
    tester.awaitMissing(second.id)
  }

  @Test
  fun linearLayoutUpdatesOrderingSpacingAndOrientation() {
    val layout =
        LinearLayout<TokenView>(
            width = 450, height = 300, spacing = 15, orientation = Orientation.HORIZONTAL)
    val tokens = (0..2).map { TokenView(width = 60, height = 60, visual = ColorVisual.RED) }
    layout.addAll(tokens)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(layout) })
    val contents = tester.awaitBGWComp(layout.id).descendant("bgw_contents")
    assertEquals("row", contents.getCssValue("flex-direction"))

    layout.orientation = Orientation.VERTICAL
    layout.spacing = 35.0
    tester.await("vertical linear layout") {
      getBGWComp(layout.id).descendant("bgw_contents").getCssValue("flex-direction") == "column"
    }
    assertTrue(
        tester.getBGWComp(tokens[1].id).location.y > tester.getBGWComp(tokens[0].id).location.y)
  }

  @Test
  fun satchelHidesContainedComponentsAndRestoresRemovedOne() {
    val satchel = Satchel<TokenView>(width = 180, height = 160, visual = ColorVisual.ORANGE)
    val token = TokenView(posX = 30, posY = 40, width = 70, height = 80, visual = ColorVisual.GREEN)
    satchel.add(token)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(satchel) })
    tester.await("satchel child hidden") { getBGWComp(token.id).opacity == 0.0 }

    satchel.remove(token)
    tester.awaitMissing(token.id)
    assertEquals(30.0, token.posX)
    assertEquals(40.0, token.posY)
    assertEquals(70.0, token.width)
    assertEquals(80.0, token.height)
  }

  @Test
  fun hexagonGridAddsRemovesAndReorientsCells() {
    val grid = HexagonGrid<HexagonView>()
    val center = HexagonView(size = 45, visual = ColorVisual.RED)
    val neighbor = HexagonView(size = 45, visual = ColorVisual.BLUE)
    grid[0, 0] = center
    grid[1, 0] = neighbor
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(grid) })
    tester.await("hex grid children") { getBGWComp(grid.id).components.size == 2 }

    grid.orientation = HexOrientation.FLAT_TOP
    tester.await("hex grid orientation update") {
      getBGWComp(center.id).attribute("aria-details") == "hex-flat_top" &&
          getBGWComp(neighbor.id).attribute("aria-details") == "hex-flat_top"
    }
    grid.remove(0, 0)
    tester.awaitMissing(center.id)
  }

  @Test
  fun cameraPaneRendersTargetAndAppliesZoomAndInteractionState() {
    val target = Pane<ComponentView>(width = 500, height = 400)
    val token = TokenView(posX = 180, posY = 130, width = 90, height = 90, visual = ColorVisual.RED)
    target.add(token)
    val camera = CameraPane(width = 350, height = 260, target = target)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(camera) })
    val rendered = tester.awaitBGWComp(camera.id)
    assertTrue(rendered.descendants("bgw_camera_target").isNotEmpty())
    tester.awaitBGWComp(token.id)

    camera.zoom = 1.75
    camera.interactive = true
    tester.await("camera zoom transform") {
      getBGWComp(camera.id)
          .descendant(".react-transform-component")
          .getCssValue("transform")
          .contains("1.75")
    }
    assertTrue(camera.interactive)
  }

  @Test
  fun switchingGameScenesReplacesTheEntireRenderedHierarchy() {
    val oldLabel = Label(width = 180, height = 60, text = "old")
    val newLabel = Label(width = 180, height = 60, text = "new")
    val oldScene = BoardGameScene(width = 800, height = 600).apply { addComponents(oldLabel) }
    val newScene = BoardGameScene(width = 800, height = 600).apply { addComponents(newLabel) }
    show(oldScene)
    tester.awaitBGWComp(oldLabel.id)

    app.showGameScene(newScene)
    tester.awaitMissing(oldLabel.id)
    tester.awaitBGWComp(newLabel.id)
  }
}
