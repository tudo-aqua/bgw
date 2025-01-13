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

package tools.aqua.bgw.mapper

import AnimationData
import data.animation.*
import tools.aqua.bgw.animation.*

internal object AnimationMapper {
  private fun AnimationData.fillData(animation: Animation): AnimationData {
    return this.apply {
      id = animation.id
      duration = animation.duration
    }
  }

  private fun ComponentAnimationData.fillData(
      componentAnimation: ComponentAnimation<*>
  ): ComponentAnimationData {
    return this.apply {
      id = componentAnimation.id
      componentView = ComponentMapper.map(componentAnimation.componentView)
      duration = componentAnimation.duration
    }
  }

  private fun mapSpecific(animation: Animation): AnimationData {
    return when (animation) {
      is ComponentAnimation<*> -> {
        when (animation) {
          is FadeAnimation<*> -> FadeAnimationData().fillData(animation)
          is MovementAnimation<*> -> MovementAnimationData().fillData(animation)
          is RotationAnimation<*> -> RotationAnimationData().fillData(animation)
          is ScaleAnimation<*> -> ScaleAnimationData().fillData(animation)
          is FlipAnimation<*> -> FlipAnimationData().fillData(animation)
          is SteppedComponentAnimation<*> -> {
            when (animation) {
              is DiceAnimation<*> ->
                  DiceAnimationData().fillData(animation) as SteppedComponentAnimationData
              is RandomizeAnimation<*> ->
                  RandomizeAnimationData().fillData(animation) as SteppedComponentAnimationData
            }
          }
        }
      }
      is DelayAnimation -> DelayAnimationData().fillData(animation)
      is ParallelAnimation -> ParallelAnimationData().fillData(animation)
      is SequentialAnimation -> SequentialAnimationData().fillData(animation)
      else ->
          throw IllegalArgumentException("Unknown animation type: ${animation::class.simpleName}")
    }
  }

  fun map(animation: Animation): AnimationData {
    return when (animation) {
      is FadeAnimation<*> ->
          (mapSpecific(animation) as FadeAnimationData).apply {
            fromOpacity = animation.fromOpacity
            toOpacity = animation.toOpacity
            animationType = "fade"
          }
      is MovementAnimation<*> ->
          (mapSpecific(animation) as MovementAnimationData).apply {
            byX = (animation.toX - animation.fromX).toInt()
            byY = (animation.toY - animation.fromY).toInt()
              animationType = "move"
          }
      is RotationAnimation<*> ->
          (mapSpecific(animation) as RotationAnimationData).apply {
            byAngle = animation.toAngle - animation.fromAngle
            animationType = "rotate"
          }
      is ScaleAnimation<*> ->
          (mapSpecific(animation) as ScaleAnimationData).apply {
            fromScaleX = animation.fromScaleX
            fromScaleY = animation.fromScaleY
            toScaleX = animation.toScaleX
            toScaleY = animation.toScaleY
            animationType = "scale"
          }
      is FlipAnimation<*> ->
          (mapSpecific(animation) as FlipAnimationData).apply {
            fromVisual = VisualMapper.map(animation.fromVisual)
            toVisual = VisualMapper.map(animation.toVisual)
            animationType = "flip"
          }
      is SequentialAnimation ->
          (mapSpecific(animation) as SequentialAnimationData).apply {
            animations = animation.animations.map { map(it) }
          }
      is ParallelAnimation ->
          (mapSpecific(animation) as ParallelAnimationData).apply {
            animations = animation.animations.map { map(it) }
          }
      is RandomizeAnimation<*> ->
          (mapSpecific(animation) as RandomizeAnimationData).apply {
            visuals = animation.visuals.map { VisualMapper.map(it) }
            toVisual = VisualMapper.map(animation.toVisual)
            speed = animation.speed
              animationType = "random"
          }
      is DiceAnimation<*> ->
          (mapSpecific(animation) as DiceAnimationData).apply {
            toSide = animation.toSide
            speed = animation.speed
              animationType = "dice"
          }
      is DelayAnimation ->
          (mapSpecific(animation) as DelayAnimationData).apply { duration = animation.duration }
      else ->
          throw IllegalArgumentException("Unknown animation type: ${animation::class.simpleName}")
    }
  }
}
