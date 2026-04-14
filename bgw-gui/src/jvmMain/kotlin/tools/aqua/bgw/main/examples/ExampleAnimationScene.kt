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
import kotlin.reflect.KProperty0
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.DiceAnimation
import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.animation.RotationAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.AnimationInterpolation
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Color
import tools.aqua.bgw.main.examples.ExampleAnimationScene as AnimationScene
import tools.aqua.bgw.mapper.AnimationMapper
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

/** Metadata: [AnimationScene] */
internal class ExampleAnimationScene : BoardGameScene(width = 445, height = 300) {
  val map = mutableMapOf<String, String>()

  val greenToken =
      TokenView(
          posX = 100, posY = 100, width = 100, height = 100, visual = ColorVisual(Color(0xc6ff6e)))

  val blueToken =
      TokenView(
          posX = 172.5,
          posY = 100,
          width = 100,
          height = 100,
          visual = ColorVisual(Color(0x6dbeff)))

  val purpleToken =
      TokenView(
          posX = 172.5,
          posY = 100,
          width = 100,
          height = 100,
          visual = ColorVisual(Color(0xbb6dff)))

  val card =
      CardView(
              posX = 175,
              posY = 71,
              width = 95,
              height = 158,
              front = ImageVisual("assets/elements/fortify.png"),
              back = ImageVisual("assets/elements/back.png"),
          )
          .apply { showBack() }

  /** Creates a movement animation that moves the token from x = 100 to x = 200 in 1 second. */
  val movementAnimationTo =
      MovementAnimation(componentView = greenToken, fromX = 100.0, toX = 245.0, duration = 1000)

  /** Creates a movement animation that moves the token by x = 200 and y = -50 in 0.5 seconds. */
  val rotateAnimationBy =
      RotationAnimation(componentView = blueToken, byAngle = 225.0, duration = 1000)

  val flipAnimation =
      FlipAnimation(gameComponentView = card, toVisual = card.frontVisual, duration = 1000)

  val fadeAnimation = FadeAnimation(componentView = purpleToken, toOpacity = 0.25, duration = 1000)

  val t1 =
      TokenView(
          posX = 47.5, posY = 125, width = 50, height = 50, visual = ColorVisual(Color(0xc6ff6e)))
  val t2 =
      TokenView(
          posX = 147.5, posY = 125, width = 50, height = 50, visual = ColorVisual(Color(0x6dbeff)))
  val t3 =
      TokenView(
          posX = 247.5, posY = 125, width = 50, height = 50, visual = ColorVisual(Color(0xbb6dff)))
  val t4 =
      TokenView(
          posX = 347.5, posY = 125, width = 50, height = 50, visual = ColorVisual(Color(0xfa6c56)))

  val area =
      Area<TokenView>(
              width = 445,
              height = 300,
          )
          .apply {
            add(t1)
            add(t2)
            add(t3)
            add(t4)
          }

  val parallelInterpolationAnimation =
      ParallelAnimation(
          RotationAnimation(
              componentView = t1,
              byAngle = 225.0,
              duration = 1000,
              interpolation = AnimationInterpolation.LINEAR),
          RotationAnimation(
              componentView = t2,
              byAngle = 225.0,
              duration = 1000,
              interpolation = AnimationInterpolation.SMOOTH),
          RotationAnimation(
              componentView = t3,
              byAngle = 225.0,
              duration = 1000,
              interpolation = AnimationInterpolation.SPRING),
          RotationAnimation(
              componentView = t4,
              byAngle = 225.0,
              duration = 1000,
              interpolation = AnimationInterpolation.STEPS),
      )

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
    setComponentAndSerialize(::purpleToken)
    setComponentAndSerialize(::card)
    setComponentAndSerialize(::diceCustomSide)

    playAnimationAndSerialize(::movementAnimationTo, ::greenToken)
    playAnimationAndSerialize(::flipAnimation, ::card)
    playAnimationAndSerialize(::rotateAnimationBy, ::blueToken)
    playAnimationAndSerialize(::fadeAnimation, ::purpleToken)
    playAnimationAndSerialize(::diceAnimation, ::diceCustomSide)

    setComponentAndSerialize(::area)
    playAnimationAndSerialize(::parallelInterpolationAnimation, ::area)
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
