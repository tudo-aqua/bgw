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
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.DiceAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.main.examples.ExampleAnimationScene as AnimationScene
import tools.aqua.bgw.mapper.AnimationMapper
import tools.aqua.bgw.visual.ColorVisual

/** Metadata: [AnimationScene] */
internal class ExampleAnimationScene : BoardGameScene(width = 622, height = 300) {
  val map = mutableMapOf<String, String>()

  val greenToken =
      TokenView(
          posX = 100, posY = 100, width = 100, height = 100, visual = ColorVisual(Color(0xc6ff6e)))

  val blueToken =
      TokenView(
          posX = 100, posY = 100, width = 100, height = 100, visual = ColorVisual(Color(0x6dbeff)))

  /** Creates a movement animation that moves the token from x = 100 to x = 200 in 1 second. */
  val movementAnimationTo =
      MovementAnimation(componentView = greenToken, fromX = 100.0, toX = 200.0, duration = 1000)

  /** Creates a movement animation that moves the token by x = 200 and y = -50 in 0.5 seconds. */
  val movementAnimationBy =
      MovementAnimation(componentView = blueToken, byX = 200.0, byY = -50.0, duration = 500)

  val diceCustomSide =
      DiceView(
          posX = 261,
          posY = 100,
          width = 100,
          height = 100,
          visuals =
              listOf(
                  ColorVisual(Color(0xc6ff6e)),
                  ColorVisual(Color(0xffc656)),
                  ColorVisual(Color(0xfa6c56)),
                  ColorVisual(Color(0xef4444)),
                  ColorVisual(Color(0xbb6dff)),
                  ColorVisual(Color(0x6dbeff))))

  /**
   * Creates a dice animation that rolls the dice to side 3 (red) in 1 second. It shuffles the
   * visual 20 times in that duration until landing on the desired side.
   */
  val diceAnimation = DiceAnimation(dice = diceCustomSide, toSide = 3, speed = 20, duration = 1000)

  init {
    setComponentAndSerialize(::greenToken)
    setComponentAndSerialize(::blueToken)
    setComponentAndSerialize(::diceCustomSide)
    playAnimationAndSerialize(::movementAnimationTo, ::greenToken)
    playAnimationAndSerialize(::movementAnimationBy, ::blueToken)
    playAnimationAndSerialize(::diceAnimation, ::diceCustomSide)
  }

  fun playMovementAnimationTo() {
    playAnimation(movementAnimationTo)
  }

  fun playMovementAnimationBy() {
    playAnimation(movementAnimationBy)
  }

  fun playDiceAnimation() {
    playAnimation(diceAnimation)
  }

  private fun <T : ComponentView> playAnimationAndSerialize(
      anim: KProperty0<Animation>,
      prop: KProperty0<T>
  ) {
    val comp = prop.get()
    val animation = anim.call()

    val fullyQualifiedName = "${this::class.qualifiedName}.${anim.name}"

    clearComponents()
    addComponents(comp)

    val animationData = AnimationMapper.map(animation)
    val json = jsonMapper.encodeToString(PropData(animationData))
    map[fullyQualifiedName] = json
  }

  internal fun <T : ComponentView> setComponentAndSerialize(prop: KProperty0<T>) {
    val comp = prop.get()

    clearComponents()
    addComponents(comp)

    val fullyQualifiedName = "${this::class.qualifiedName}.${prop.name}"

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    val json = jsonMapper.encodeToString(PropData(appData))
    map[fullyQualifiedName] = json

    clearComponents()
  }
}
