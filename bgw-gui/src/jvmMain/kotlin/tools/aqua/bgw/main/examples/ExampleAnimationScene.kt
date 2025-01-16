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
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.main.examples.ExampleUIScene as AnimationScene
import tools.aqua.bgw.mapper.AnimationMapper
import tools.aqua.bgw.visual.ColorVisual

/** Metadata: [AnimationScene] */
internal class ExampleAnimationScene : BoardGameScene(width = 622, height = 300) {
  val map = mutableMapOf<String, String>()

  val button =
      Button(
          posX = 100,
          posY = 50,
          width = 200,
          height = 100,
          text = "I am a Button.",
          visual = ColorVisual.LIGHT_GRAY)

  val button2 =
      Button(
          posX = 0,
          posY = 0,
          width = 200,
          height = 200,
          text = "Also a Button.",
          visual = ColorVisual.RED)

  val movementAnimation =
      MovementAnimation(componentView = button, fromX = 100.0, toX = 200.0, duration = 1000)

  val movementAnimationBy =
      MovementAnimation(componentView = button, byX = 100.0, byY = 100.0, duration = 1000)

  init {
    playAnimationAndSerialize(::movementAnimation, ::button)
  }

  fun playMovementAnimation() {
    playAnimation(movementAnimation)
  }

  private fun <T : ComponentView> setComponentAndSerialize(prop: KProperty0<T>) {
    val comp = prop.get()

    clearComponents()
    addComponents(comp)

    val fullyQualifiedName = "${this::class.qualifiedName}.${prop.name}"

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    val json = jsonMapper.encodeToString(PropData(appData))
    map[fullyQualifiedName] = json

    clearComponents()
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

    println(map[fullyQualifiedName])
  }
}
