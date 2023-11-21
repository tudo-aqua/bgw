/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.examples.maumau.main

import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.container.HexagonGrid
import tools.aqua.bgw.components.gamecomponentviews.HexagonView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.examples.maumau.view.MauMauViewController
import tools.aqua.bgw.style.BackgroundRadius
import tools.aqua.bgw.style.BorderColor
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.style.BorderWidth
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.TextVisual
import kotlin.reflect.KMutableProperty1

/** Entry point. */
fun main() {
    Application.show()
    //MauMauViewController()
}

object Application : BoardGameApplication() {
    val menuScene = object : MenuScene() {
        val label = Label(0, 0, 100, 100, "Hello", visual = ColorVisual.RED).apply {
            visual.apply {
                backgroundRadius = BackgroundRadius(10)
                borderRadius = BorderRadius(10)
            }
            onMouseClicked = {
                playAnimation(
                        MovementAnimation(
                                componentView = this,
                                byX = 100,
                                byY = 100,
                                duration = 1000
                        )
                )
            }
        }

        init {
            addComponents(label)
        }
    }

    val gameScene = object : BoardGameScene() {
        val map = BidirectionalMap<HexagonView, Coordinate>()

        val hexagonGrid = HexagonGrid<HexagonView>()

        val player: Player = Player("Player1")


        val label1 = Label(0, 0, 100, 100, "asdfasdfasdf", visual = ColorVisual.RED).apply {
            font = Font(
                    size = 20.0,
                    family = "Emotional Baggage"
            )
            visual.apply {
                backgroundRadius = BackgroundRadius(10)
                borderRadius = BorderRadius(10)

            }
        }
        val label2 = Label(100, 100, 100, 100, "asdfasdfasdf", visual = ColorVisual.RED)

        init {
            addComponents(label1, label2)
            initializeHexagonGrid()
            addComponents(hexagonGrid)
        }

        fun initializeHexagonGrid() {
            for (row in 0..4) {
                for (col in 0..4) {
                    val hexagon = HexagonView(visual = ColorVisual.RED).apply {
                        visual.apply {
                            borderColor = BorderColor.BLACK
                            //borderRadius = BorderRadius(40)
                            borderWidth = BorderWidth(15)
                        }

                        onMouseClicked = {
                            this.rotation += 60
                        }
                    }
                    hexagonGrid[col, row] = hexagon
                    val cords = Coordinate(col, row)
                    map.add(hexagon to cords)
                }
            }
        }
    }

    init {
        //showMenuScene(menuScene)
        showGameScene(gameScene)
    }
}


class Player(var name: String)
/*
fun test() {
  val player = Player("Player1")

  val label = Label().apply {
    text = player.name
    textProperty.bind(player::name)
  }

  val button = Button().apply {
    onMouseClicked = {
      showMenuScene()
    }
  }
}*/

















