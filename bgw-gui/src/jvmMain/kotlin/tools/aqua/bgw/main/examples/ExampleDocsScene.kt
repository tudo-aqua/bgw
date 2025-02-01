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

package tools.aqua.bgw.main.examples

import PropData
import jsonMapper
import kotlin.reflect.KProperty0
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.main.examples.ExampleDocsScene as Scene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/** Metadata: [Scene] */
internal class ExampleDocsScene : BoardGameScene(width = 445, height = 300) {
  val map = mutableMapOf<String, String>()

    val tokenRect =
        TokenView(
            width = 100,
            height = 100,
            visual = ColorVisual(Color(0xc6ff6e)))

    val tokenCircle =
        TokenView(
            width = 100,
            height = 100,
            visual = ColorVisual(Color(0xffc656)).apply {
                style.borderRadius = BorderRadius.FULL
            })

    val cardImage = CardView(
        width = 120,
        height = 200,
        front = ImageVisual("assets/elements/fortify.png")
    )

    val cardBack = CardView(
        width = 120,
        height = 200,
        front = ImageVisual("assets/elements/fortify.png"),
        back = ImageVisual("assets/elements/back.png"),
    ).apply {
        showBack()
    }

    val diceDefaultSide = DiceView(
        width = 100,
        height = 100,
        visuals = listOf(ColorVisual(Color(0xef4444)))
    )

    val diceCustomSide = DiceView(
        width = 100,
        height = 100,
        visuals = listOf(
            ColorVisual(Color(0xc6ff6e)),
            ColorVisual(Color(0xffc656)),
            ColorVisual(Color(0xfa6c56)), // Orange
            ColorVisual(Color(0xef4444)),
            ColorVisual(Color(0xbb6dff)),
            ColorVisual(Color(0x6dbeff))
        )
    ).apply {
        currentSide = 2
    }

  val hexagonPointy = HexagonView(size = 60, visual = ColorVisual(Color(0xbb6dff)))

  val hexagonFlat =
      HexagonView(
          size = 60, visual = ColorVisual(Color(0x6dbeff)), orientation = HexOrientation.FLAT_TOP)

  val linearLayout =
      LinearLayout<GameComponentView>(
          posX = 0,
          posY = 0,
          width = this.width,
          height = 300,
          orientation = Orientation.HORIZONTAL,
          alignment = Alignment.CENTER,
          spacing = 50)

  init {
      setComponentsAndSerialize("tokenExample", ::tokenRect, ::tokenCircle)
      setComponentsAndSerialize("cardExample", ::cardImage, ::cardBack)
    setComponentsAndSerialize("hexagonExample", ::hexagonPointy, ::hexagonFlat)
    setComponentsAndSerialize("diceExample", ::diceDefaultSide, ::diceCustomSide)
  }

  internal fun <T : ComponentView> setComponentsAndSerialize(
      name: String,
      vararg props: KProperty0<T>
  ) {
    clearComponents()
    linearLayout.clear()

    props.forEach { prop ->
      val comp = prop.get()
      linearLayout.add(comp as GameComponentView)
    }

    addComponents(linearLayout)

    val fullyQualifiedName = "${this::class.qualifiedName}.$name"

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    val json = jsonMapper.encodeToString(PropData(appData))
    map[fullyQualifiedName] = json

    clearComponents()
  }
}
