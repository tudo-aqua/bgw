/*
 * Copyright 2021-2026 The BoardGameWork Authors
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

package examples.concepts.loading

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color

fun main() {
  LoadingScreenExample()
}

/**
 * Example demonstrating the BGW loading screen with image pre-caching.
 *
 * This shows how to:
 * 1. Display an animated "made with BGW" loading screen
 * 2. Pre-cache images before showing the main game scene
 * 3. Show a progress bar during caching
 * 4. Automatically hide the loading screen when done
 */
class LoadingScreenExample : BoardGameApplication("Loading Screen Example", 1920, 1080) {

    private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.GRAY)

    private val imageFront: ImageVisual =
        ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 260, offsetY = 200)
    private val imageBack: ImageVisual =
        ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 260, offsetY = 800)
    private val dieVisuals: MutableList<Visual> =
        mutableListOf(
            ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 0, offsetY = 0),
            ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 200, offsetY = 0),
            ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 400, offsetY = 0),
            ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 600, offsetY = 0),
            ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 800, offsetY = 0),
            ImageVisual(path = "D6.png", width = 200, height = 200, offsetX = 1000, offsetY = 0),
        )
    private val randomCardFaces: MutableList<Visual> =
        mutableListOf(
            ImageVisual(path = "card_deck.png", width = 130, height = 200, offsetX = 0, offsetY = 0),
            ImageVisual(
                path = "card_deck.png", width = 130, height = 200, offsetX = 130, offsetY = 0),
            ImageVisual(
                path = "card_deck.png", width = 130, height = 200, offsetX = 260, offsetY = 0),
            ImageVisual(
                path = "card_deck.png", width = 130, height = 200, offsetX = 390, offsetY = 0),
            ImageVisual(
                path = "card_deck.png", width = 130, height = 200, offsetX = 520, offsetY = 0),
            ImageVisual(
                path = "card_deck.png", width = 130, height = 200, offsetX = 650, offsetY = 0),
        )

    private val buttonDelay: Button =
        Button(posX = 900, posY = 800, text = "Delay").apply { visual = ColorVisual.WHITE }
    private val buttonMovement: Button =
        Button(posX = 500, posY = 700, text = "Move").apply { visual = ColorVisual.WHITE }
    private val buttonRotation: Button =
        Button(posX = 650, posY = 700, text = "Rotate").apply { visual = ColorVisual.WHITE }
    private val buttonOpacity: Button =
        Button(posX = 800, posY = 700, text = "Fade").apply { visual = ColorVisual.WHITE }
    private val buttonStretch: Button =
        Button(posX = 950, posY = 700, text = "Scale").apply { visual = ColorVisual.WHITE }
    private val buttonFlip: Button =
        Button(posX = 1100, posY = 700, text = "Flip").apply { visual = ColorVisual.WHITE }
    private val buttonRandomize: Button =
        Button(posX = 1250, posY = 700, text = "Randomize").apply { visual = ColorVisual.WHITE }
    private val buttonDie: Button =
        Button(posX = 1400, posY = 700, text = "Roll").apply { visual = ColorVisual.WHITE }

    private val cardMovement: CardView =
        CardView(posX = 500, posY = 450, front = imageFront, back = imageBack)
    private val cardRotation: CardView =
        CardView(posX = 650, posY = 450, front = imageFront, back = imageBack)
    private val cardOpacity: CardView =
        CardView(posX = 800, posY = 450, front = imageFront, back = imageBack)
    private val cardStretch: CardView =
        CardView(posX = 950, posY = 450, front = imageFront, back = imageBack)
    private val cardFlip: CardView =
        CardView(posX = 1100, posY = 450, front = imageFront, back = imageBack)
    private val cardRandomize: CardView =
        CardView(posX = 1250, posY = 450, front = imageFront, back = imageBack)
    private val die: DiceView = DiceView(posX = 1400, posY = 500, visuals = dieVisuals)

  init {
    // List of images to pre-cache
    val imagesToCache = listOf(
        "card_deck.png",
        "D6.png",
        // Add more image paths here
    )

    // Show loading screen with pre-caching
    // The loading screen will automatically hide when:
    // 1. All images are cached AND
    // 2. The logo animation finishes AND
    // 3. The minimum display time (1000ms) has elapsed
    preCacheImages(
        imagePaths = imagesToCache,
        // logoPath defaults to empty string, which shows the animated BGW logo
        // To use a custom logo, specify: logoPath = "my_custom_logo.gif"
        minimumDisplayTime = 1000, // Show for at least 1 second after logo finishes
        showProgressBar = true, // Show progress bar
        onComplete = {
          // Optional: Called when loading is complete
          println("Pre-caching complete!")
        }
    )
      gameScene.addComponents(
          buttonDelay,
          buttonMovement,
          buttonRotation,
          buttonOpacity,
          buttonStretch,
          buttonFlip,
          buttonRandomize,
          buttonDie,
          cardMovement,
          cardRotation,
          cardOpacity,
          cardStretch,
          cardFlip,
          cardRandomize,
          die)
    // Show the game scene
    showGameScene(gameScene)

    // Start the application
    show()
  }
}

