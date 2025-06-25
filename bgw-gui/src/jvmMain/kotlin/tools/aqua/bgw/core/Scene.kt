/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.core

import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.RootComponent
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for BGW scenes.
 *
 * @param T Generic [ComponentView].
 * @param width [Scene] width in virtual coordinates.
 * @param height [Scene] height in virtual coordinates.
 * @param background [Scene] [background] [Visual].
 * @see BoardGameScene
 * @see MenuScene
 * @since 0.1
 */
sealed class Scene<T : ComponentView>(width: Number, height: Number, background: Visual) {

  internal val id = IDGenerator.generateSceneID()

  /**
   * [MutableList] containing all [ComponentView]s currently below mouse position while performing a
   * drag gesture.
   */
  internal val dragTargetsBelowMouse: MutableSet<ComponentView> = mutableSetOf()

  internal var isVisible: Boolean = false

  /**
   * The root node of this [Scene].
   *
   * Use it to compare the parent [Property] of any [ComponentView] to find out whether it was
   * directly added to the [Scene].
   */
  @Suppress("LeakingThis") val rootNode: RootComponent<T> = RootComponent(this)

  /** The drag gesture temporary root node. */
  @Suppress("LeakingThis") internal val dragGestureRootNode: RootComponent<T> = RootComponent(this)

  /** The width of this [Scene] in virtual coordinates. */
  // TODO: Change to property for updating during runtime
  val width: Double = width.toDouble()

  /** The height of this [Scene] in virtual coordinates. */
  // TODO: Change to property for updating during runtime
  val height: Double = height.toDouble()

  /** All [ComponentView]s on the root node. */
  internal val rootComponents: ObservableList<T> = ObservableArrayList()

  /**
   * [Property] for the [background] [Visual] of this [Scene].
   *
   * @see background
   */
  internal val backgroundProperty: Property<Visual> = Property(background)

  /** The background [Visual] of this [Scene]. */
  var background: Visual
    get() = backgroundProperty.value
    set(value) {
      backgroundProperty.value = value
    }

  /** Returns all root components that are currently contained in this [Scene]. */
  val components: List<T>
    get() = rootComponents.toList()

  /**
   * [Property] for the [opacity] of the [background] of this [Scene].
   *
   * @see opacity
   */
  internal val opacityProperty = DoubleProperty(1.0)

  /** Opacity of the [background] of this [Scene]. */
  var opacity: Double
    get() = opacityProperty.value
    set(value) {
      require(value in 0.0..1.0) { "Value must be between 0 and 1 inclusive." }

      opacityProperty.value = value
    }

  /** All [Animation]s currently playing. */
  internal val animations: ObservableList<Animation> = ObservableArrayList()

  /**
   * Gets invoked with a [KeyEvent] whenever a key is pressed.
   *
   * @see KeyEvent
   */
  var onKeyPressed: ((KeyEvent) -> Unit)? = null

  /**
   * Gets invoked with a [KeyEvent] whenever a key is released.
   *
   * @see KeyEvent
   */
  var onKeyReleased: ((KeyEvent) -> Unit)? = null

  /**
   * Gets invoked with a [KeyEvent] whenever a Character is typed. Gets invoked after [onKeyPressed]
   * .
   *
   * @see KeyEvent
   * @see onKeyPressed
   */
  @Deprecated(
      "The onKeyTyped event is defined in this specification for reference and completeness.")
  var onKeyTyped: ((KeyEvent) -> Unit)? = null

  /** Gets invoked with no event whenever a scene gets shown. */
  var onSceneShown: (() -> Unit)? = null

  /** Gets invoked with no event whenever a scene gets hidden. */
  var onSceneHidden: (() -> Unit)? = null

  /**
   * Gets invoked whenever the scene is rescaled because the window was resized.
   *
   * @since 0.10
   */
  var onSceneRescaled: (() -> Unit)? = null

  /**
   * Adds all given [ComponentView]s to the root node and [rootComponents] list.
   *
   * @param components Components to add.
   */
  fun addComponents(vararg components: T) {
    rootComponents.addAll(
        components.toList().onEach {
          check(it.parent == null) { "Component $it is already contained in another container." }
          it.parent = rootNode
        })
  }

  /**
   * Removes all given [ComponentView]s from the root node and [rootComponents] list.
   *
   * @param components Components to remove.
   */
  fun removeComponents(vararg components: T) {
    rootComponents.removeAll(components.toList().onEach { it.parent = null })
  }

  /** Removes all [ComponentView]s from the root node and [rootComponents] list. */
  fun clearComponents() {
    rootComponents.forEach { it.parent = null }
    rootComponents.clear()
  }

  /**
   * Plays given [Animation] non-blocking. This means that any subsequent updates may be executed
   * before the [Animation] actually starts playing. [ComponentView]s that are part of the
   * [Animation] will reset their state immediately to the state before the [Animation] was played,
   * once the [Animation] is finished.
   *
   * The [Animation] only animates the [ComponentView] visually and does not update its state. The
   * Component will reset its visual state on next refresh if the state is not set in onFinished,
   * which is the suggested way of usage.
   *
   * @param animation [Animation] to play.
   */
  fun playAnimation(animation: Animation) {
    addAnimationRecursively(animation)
    Frontend.sendAnimation(animation)
  }

  private fun addAnimationRecursively(animation: Animation) {
    when (animation) {
      is SequentialAnimation -> {
        animation.animations.forEach { addAnimationRecursively(it) }
        animations.add(animation)
      }
      is ParallelAnimation -> {
        animation.animations.forEach { addAnimationRecursively(it) }
        animations.add(animation)
      }
      else -> animations.add(animation)
    }
  }

  /**
   * Stops all currently playing [Animation]s and resets visual state of all [ComponentView]s that
   * were currently animating immediately.
   *
   * @see Animation
   */
  fun stopAllAnimations() {
    Frontend.stopAnimations()
  }

  /**
   * Searches [node] recursively through the visual tree and logs path where the [node] appears as
   * first component and the [rootNode] as last.
   *
   * @param node Child to find.
   * @return Path to child.
   * @throws IllegalStateException If child was not contained in this [Scene].
   */
  fun findPathToChild(node: ComponentView): List<ComponentView> {
    if (node is RootComponent<*>) {
      check(node == rootNode) { "Child is contained in another scene" }
      return listOf(rootNode)
    }

    val parent =
        checkNotNull(node.parent) {
          "Encountered component $node that is not contained in a scene."
        }

    return mutableListOf(node) + findPathToChild(parent)
  }
}
