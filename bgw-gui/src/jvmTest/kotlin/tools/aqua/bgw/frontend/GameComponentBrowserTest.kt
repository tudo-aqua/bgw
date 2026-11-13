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
import org.openqa.selenium.JavascriptExecutor
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.HexOrientation
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual

class GameComponentBrowserTest : BrowserTestBase() {

  @Test
  fun croppedImageVisualRedrawsWhenOnlyItsOffsetChanges() {
    val token =
        TokenView(
            width = 32,
            height = 32,
            visual = ImageVisual("icon.png", width = 32, height = 32, offsetX = 0, offsetY = 0))
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(token) })
    tester.awaitBGWComp(token.id)
    tester.await("crop at the origin") { centerPixel(token) == listOf(32L, 28L, 48L) }

    // The image is cached by the browser, so only the offset of the crop changes.
    token.visual = ImageVisual("icon.png", width = 32, height = 32, offsetX = 64, offsetY = 64)
    tester.await("crop at the new offset") { centerPixel(token) == listOf(228L, 89L, 118L) }
  }

  /** Returns the RGB value of the center pixel of the canvas that draws the crop of [token]. */
  @Suppress("UNCHECKED_CAST")
  private fun centerPixel(token: TokenView): List<Long>? =
      (tester.requireDriver() as JavascriptExecutor).executeScript(
          """
          const canvas = document.getElementById(arguments[0])?.querySelector("canvas");
          if (!canvas) return null;
          const pixel = canvas.getContext("2d").getImageData(16, 16, 1, 1).data;
          return [pixel[0], pixel[1], pixel[2]];
          """
              .trimIndent(),
          token.id) as List<Long>?

  @Test
  fun tokenRendersAndUpdatesItsVisualAndTransformProperties() {
    val token = TokenView(posX = 50, posY = 70, width = 120, height = 90, visual = ColorVisual.RED)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(token) })
    assertVisualsEqual(token.visual, tester.awaitBGWComp(token.id))

    token.visual = CompoundVisual(ColorVisual.BLUE, TextVisual("token"))
    token.scaleX = 1.5
    token.scaleY = 0.75
    tester.await("token visual and scale update") {
      val rendered = getBGWComp(token.id)
      rendered.visuals.size == 2 && rendered.css("transform").startsWith("matrix")
    }
    assertVisualsEqual(token.visual, tester.getBGWComp(token.id))
  }

  @Test
  fun cardSwitchesBetweenFrontAndBackVisuals() {
    val card =
        CardView(
            width = 140,
            height = 210,
            front = CompoundVisual(ColorVisual.WHITE, TextVisual("front")),
            back = CompoundVisual(ColorVisual.BLUE, TextVisual("back")))
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(card) })
    assertVisualsEqual(card.backVisual, tester.awaitBGWComp(card.id))

    card.showFront()
    tester.await("card front visual") {
      getBGWComp(card.id).visuals.any { it is TextVisual && it.text == "front" }
    }
    card.flip()
    tester.await("card flipped back") {
      getBGWComp(card.id).visuals.any { it is TextVisual && it.text == "back" }
    }
  }

  @Test
  fun diceSwitchesSidesAndReplacesItsVisualSet() {
    val die =
        DiceView(
            width = 130,
            height = 130,
            visuals = (1..3).map { CompoundVisual(ColorVisual.WHITE, TextVisual(it.toString())) })
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(die) })
    tester.await("initial die side") {
      getBGWComp(die.id).visuals.any { it is TextVisual && it.text == "1" }
    }

    die.currentSide = 2
    tester.await("third die side") {
      getBGWComp(die.id).visuals.any { it is TextVisual && it.text == "3" }
    }

    die.setVisuals(listOf(ColorVisual.RED, ColorVisual.GREEN, ColorVisual.BLUE))
    tester.await("replaced die visual set") {
      val color = getBGWComp(die.id).visuals.singleOrNull() as? ColorVisual
      color?.color == ColorVisual.BLUE.color
    }
  }

  @Test
  fun hexagonUpdatesOrientationAndSize() {
    val hex = HexagonView(size = 80, visual = ColorVisual.ORANGE)
    show(BoardGameScene(width = 800, height = 600).apply { addComponents(hex) })
    val rendered = tester.awaitBGWComp(hex.id)
    assertEquals("hex-pointy_top", rendered.attribute("aria-details"))

    hex.orientation = HexOrientation.FLAT_TOP
    hex.size = 110.0
    tester.await("hex orientation and size update") {
      val current = getBGWComp(hex.id)
      current.attribute("aria-details") == "hex-flat_top" &&
          current.logicalSize.width in 219.0..221.0 &&
          current.logicalSize.height in 189.0..192.0
    }
  }
}
