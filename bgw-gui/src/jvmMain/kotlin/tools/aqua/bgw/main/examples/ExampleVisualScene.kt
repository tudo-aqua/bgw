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

package tools.aqua.bgw.main.examples

import PropData
import jsonMapper
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.main.examples.ExampleVisualScene as Scene
import tools.aqua.bgw.style.BlurFilter
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.style.Cursor
import tools.aqua.bgw.style.Flip
import tools.aqua.bgw.style.SaturationFilter
import tools.aqua.bgw.style.SepiaFilter
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.TextVisual
import tools.aqua.bgw.visual.Visual

/** Metadata: [Scene] */
internal class ExampleVisualScene : BoardGameScene(width = 445, height = 300) {
  val map = mutableMapOf<String, String>()

  private val dark = Color(0x0F141F)

  private fun token(visual: Visual, width: Int = 90, height: Int = 90) =
      TokenView(width = width, height = height, visual = visual)

  private fun token70(visual: Visual) = token(visual, width = 70, height = 70)

  // region ColorVisual
  val colorSolid = token(ColorVisual(Color(0xBB6DFF)))

  val colorAlpha = token(ColorVisual(Color(187, 109, 255, 0.5)))

  val colorTransparency = token(ColorVisual(Color(0xBB6DFF)).apply { transparency = 0.25 })

  val colorPreset = token(ColorVisual.RED)
  // endregion

  // region TextVisual
  val textDefault = token(CompoundVisual(ColorVisual(Color(0xBB6DFF)), TextVisual("Token")))

  val textStyled =
      token(
          CompoundVisual(
              ColorVisual(Color(0x6DBEFF)),
              TextVisual(
                  text = "Token",
                  font =
                      Font(
                          size = 20,
                          color = dark,
                          fontWeight = Font.FontWeight.BOLD,
                          fontStyle = Font.FontStyle.ITALIC))))

  val textTopLeft =
      token(
          CompoundVisual(
              ColorVisual(Color(0xFFC656)),
              TextVisual(
                  text = "Top left",
                  font = Font(size = 16, color = dark),
                  alignment = Alignment.TOP_LEFT)))

  val textOffset =
      token(
          CompoundVisual(
              ColorVisual(Color(0xC6FF6E)),
              TextVisual(
                  text = "Offset",
                  font = Font(size = 16, color = dark),
                  offsetX = 10,
                  offsetY = -20,
                  rotation = -20)))
  // endregion

  // region ImageVisual
  val imageFull = token(ImageVisual("assets/elements/fortify.png"), width = 70, height = 113)

  val imageLeftHalf =
      token(
          ImageVisual("assets/elements/fortify.png", width = 107, height = 345),
          width = 35,
          height = 113)

  val imageRightHalf =
      token(
          ImageVisual("assets/elements/fortify.png", width = 107, height = 345, offsetX = 107),
          width = 35,
          height = 113)

  val imageTopHalf =
      token(
          ImageVisual("assets/elements/fortify.png", width = 214, height = 172),
          width = 70,
          height = 56)

  val imageRotated =
      token(ImageVisual("assets/elements/fortify.png", rotation = 90), width = 113, height = 113)
  // endregion

  // region CompoundVisual
  val compoundImageAndColor =
      token(
          CompoundVisual(
              ImageVisual("assets/elements/fortify.png"),
              ColorVisual(Color(0xC6FF6E)).apply { transparency = 0.35 }),
          width = 70,
          height = 113)

  val compoundImageAndText =
      token(
          CompoundVisual(
              ImageVisual("assets/elements/fortify.png"),
              TextVisual(
                  text = "Fortify",
                  font = Font(size = 14, color = Color.WHITE, fontWeight = Font.FontWeight.BOLD),
                  alignment = Alignment.BOTTOM_CENTER)),
          width = 70,
          height = 113)

  val compoundBadge =
      token(
          CompoundVisual(
              ColorVisual(Color(0x161D29)).apply { style.borderRadius = BorderRadius.LARGE },
              ColorVisual(Color(0xEF4444)).apply {
                style.borderRadius = BorderRadius.FULL
                transparency = 0.8
              },
              TextVisual("7", font = Font(size = 40, color = Color.WHITE))),
          width = 100,
          height = 100)
  // endregion

  // region Style
  val radiusNone =
      token70(ColorVisual(Color(0xFA6C56)).apply { style.borderRadius = BorderRadius.NONE })

  val radiusMedium =
      token70(ColorVisual(Color(0xFA6C56)).apply { style.borderRadius = BorderRadius.MEDIUM })

  val radiusXL =
      token70(ColorVisual(Color(0xFA6C56)).apply { style.borderRadius = BorderRadius.XL })

  val radiusFull =
      token70(ColorVisual(Color(0xFA6C56)).apply { style.borderRadius = BorderRadius.FULL })

  val radiusCustom =
      token70(
          ColorVisual(Color(0xFA6C56)).apply {
            style.borderRadius = BorderRadius(24, 0, 24, 0)
            style.cursor = Cursor.POINTER
          })
  // endregion

  // region Filters
  val filterNone = token(ImageVisual("assets/elements/fortify.png"), width = 70, height = 113)

  val filterBlur =
      token(
          ImageVisual("assets/elements/fortify.png").apply { filters.blur = BlurFilter.SMALL },
          width = 70,
          height = 113)

  val filterGreyscale =
      token(
          ImageVisual("assets/elements/fortify.png").apply {
            filters.saturation = SaturationFilter.GREYSCALE
          },
          width = 70,
          height = 113)

  val filterSepia =
      token(
          ImageVisual("assets/elements/fortify.png").apply { filters.sepia = SepiaFilter.SEPIA },
          width = 70,
          height = 113)
  // endregion

  // region Flip
  val flipNone = token(ImageVisual("assets/elements/fortify.png"), width = 70, height = 113)

  val flipHorizontal =
      token(
          ImageVisual("assets/elements/fortify.png").apply { flipped = Flip.HORIZONTAL },
          width = 70,
          height = 113)

  val flipVertical =
      token(
          ImageVisual("assets/elements/fortify.png").apply { flipped = Flip.VERTICAL },
          width = 70,
          height = 113)

  val flipBoth =
      token(
          ImageVisual("assets/elements/fortify.png").apply { flipped = Flip.BOTH },
          width = 70,
          height = 113)
  // endregion

  private val layout =
      LinearLayout<GameComponentView>(
          posX = 0,
          posY = 0,
          width = 445,
          height = 300,
          orientation = Orientation.HORIZONTAL,
          alignment = Alignment.CENTER,
          spacing = 12)

  init {
    serialize("colorExample", colorSolid, colorAlpha, colorTransparency, colorPreset)
    serialize("textExample", textDefault, textStyled, textTopLeft, textOffset)
    serialize("imageExample", imageFull, imageLeftHalf, imageRightHalf, imageTopHalf, imageRotated)
    serialize("compoundExample", compoundImageAndColor, compoundImageAndText, compoundBadge)
    serialize("styleExample", radiusNone, radiusMedium, radiusXL, radiusFull, radiusCustom)
    serialize("filterExample", filterNone, filterBlur, filterGreyscale, filterSepia)
    serialize("flipExample", flipNone, flipHorizontal, flipVertical, flipBoth)
  }

  private fun serialize(name: String, vararg components: GameComponentView) {
    clearComponents()
    layout.clear()
    components.forEach { layout.add(it) }
    addComponents(layout)

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    map["${this::class.qualifiedName}.$name"] = jsonMapper.encodeToString(PropData(appData))

    layout.clear()
    clearComponents()
  }
}
