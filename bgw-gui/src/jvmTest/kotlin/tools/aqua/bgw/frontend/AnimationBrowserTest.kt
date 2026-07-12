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
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.DiceAnimation
import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.animation.RandomizeAnimation
import tools.aqua.bgw.animation.RotationAnimation
import tools.aqua.bgw.animation.ScaleAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.TextVisual

class AnimationBrowserTest : BrowserTestBase() {

  @Test
  fun persistentMovementUpdatesJvmStateAndRenderedLogicalPosition() {
    val (scene, token) = tokenScene()
    show(scene)
    playAndAwait(
        scene, MovementAnimation(token, byX = 180.0, byY = 90.0, duration = 250, persist = true))

    assertEquals(230.0, token.posX)
    assertEquals(140.0, token.posY)
    tester.await("persisted movement in DOM") {
      val location = getBGWComp(token.id).logicalLocation
      location.x in 229.0..231.0 && location.y in 139.0..141.0
    }
  }

  @Test
  fun nonPersistentMovementResetsRenderedAndJvmPosition() {
    val (scene, token) = tokenScene()
    show(scene)
    playAndAwait(scene, MovementAnimation(token, byX = 200.0, duration = 250, persist = false))

    assertEquals(50.0, token.posX)
    tester.await("movement reset in DOM") { getBGWComp(token.id).logicalLocation.x in 49.0..51.0 }
  }

  @Test
  fun persistentRotationUpdatesJvmAndRenderedTransform() {
    val (scene, token) = tokenScene()
    show(scene)
    playAndAwait(scene, RotationAnimation(token, byAngle = 135.0, duration = 250, persist = true))

    assertEquals(135.0, token.rotation)
    tester.await("persisted rotation in DOM") { getBGWComp(token.id).rotation in 134.9..135.1 }
  }

  @Test
  fun scaleAnimationPersistsBothAxes() {
    val (scene, token) = tokenScene()
    show(scene)
    playAndAwait(
        scene,
        ScaleAnimation(
            componentView = token, toScaleX = 1.75, toScaleY = 0.6, duration = 250, persist = true))

    assertEquals(1.75, token.scaleX)
    assertEquals(0.6, token.scaleY)
    tester.await("persisted scale in DOM") {
      val scale = getBGWComp(token.id).scale
      scale.first in 1.73..1.77 && scale.second in 0.58..0.62
    }
  }

  @Test
  fun fadeAnimationPersistsOpacity() {
    val (scene, token) = tokenScene()
    show(scene)
    playAndAwait(scene, FadeAnimation(token, toOpacity = 0.25, duration = 250, persist = true))

    assertEquals(0.25, token.opacity)
    tester.await("persisted fade in DOM") { getBGWComp(token.id).opacity in 0.24..0.26 }
  }

  @Test
  fun parallelAnimationAppliesMovementRotationAndFadeTogether() {
    val (scene, token) = tokenScene()
    show(scene)
    val animation =
        ParallelAnimation(
            MovementAnimation(token, byX = 120.0, duration = 300, persist = true),
            RotationAnimation(token, byAngle = 90.0, duration = 300, persist = true),
            FadeAnimation(token, toOpacity = 0.5, duration = 300, persist = true))
    playAndAwait(scene, animation)

    tester.await("parallel animation final DOM state") {
      val rendered = getBGWComp(token.id)
      rendered.logicalLocation.x in 169.0..171.0 &&
          rendered.rotation in 89.9..90.1 &&
          rendered.opacity in 0.49..0.51
    }
  }

  @Test
  fun sequentialAnimationAppliesEachPersistedStepInOrder() {
    val (scene, token) = tokenScene()
    show(scene)
    val animation =
        SequentialAnimation(
            MovementAnimation(token, byX = 100.0, duration = 180, persist = true),
            MovementAnimation(
                token,
                fromX = 150,
                toX = 150,
                fromY = 50,
                toY = 190,
                duration = 180,
                persist = true),
            RotationAnimation(token, byAngle = 45.0, duration = 180, persist = true))
    playAndAwait(scene, animation)

    assertEquals(150.0, token.posX)
    assertEquals(190.0, token.posY)
    assertEquals(45.0, token.rotation)
    tester.await("sequential animation final DOM state") {
      val rendered = getBGWComp(token.id)
      rendered.logicalLocation.x in 149.0..151.0 &&
          rendered.logicalLocation.y in 189.0..191.0 &&
          rendered.rotation in 44.9..45.1
    }
  }

  @Test
  fun diceAnimationEndsOnRequestedSideAndVisual() {
    val die =
        DiceView(width = 120, height = 120, visuals = (1..4).map { TextVisual(it.toString()) })
    val scene = BoardGameScene(width = 800, height = 600).apply { addComponents(die) }
    show(scene)
    playAndAwait(scene, DiceAnimation(die, toSide = 3, duration = 350, speed = 8, persist = true))

    assertEquals(3, die.currentSide)
    tester.await("dice animation final visual") {
      getBGWComp(die.id).visuals.singleOrNull().let { it is TextVisual && it.text == "4" }
    }
  }

  @Test
  fun flipAndRandomizeAnimationsPersistTheirFinalVisuals() {
    val token = TokenView(width = 120, height = 120, visual = ColorVisual.RED)
    val scene = BoardGameScene(width = 800, height = 600).apply { addComponents(token) }
    show(scene)

    playAndAwait(
        scene,
        FlipAnimation(
            token, fromVisual = ColorVisual.RED, toVisual = ColorVisual.BLUE, duration = 250))
    tester.await("flip final visual") { renderedColor(token)?.color == ColorVisual.BLUE.color }

    playAndAwait(
        scene,
        RandomizeAnimation(
            token,
            visuals = listOf(ColorVisual.RED, ColorVisual.GREEN, ColorVisual.BLUE),
            toVisual = ColorVisual.ORANGE,
            duration = 300,
            speed = 8,
            persist = true))
    tester.await("randomize final visual") {
      renderedColor(token)?.color == ColorVisual.ORANGE.color
    }
  }

  private fun tokenScene(): Pair<BoardGameScene, TokenView> {
    val token = TokenView(posX = 50, posY = 50, width = 100, height = 100, visual = ColorVisual.RED)
    return BoardGameScene(width = 800, height = 600).apply { addComponents(token) } to token
  }

  private fun playAndAwait(scene: BoardGameScene, animation: Animation) {
    var finished = false
    val previous = animation.onFinished
    animation.onFinished = {
      finished = true
      previous?.invoke(it)
    }
    scene.playAnimation(animation)
    tester.await("${animation::class.simpleName} completion") { finished }
  }

  private fun renderedColor(token: TokenView): ColorVisual? =
      tester.getBGWComp(token.id).visuals.singleOrNull() as? ColorVisual
}
