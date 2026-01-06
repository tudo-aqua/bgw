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

@file:Suppress("unused")

package tools.aqua.bgw.core

import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.AnimationPropertyCache
import tools.aqua.bgw.animation.AnimationType
import tools.aqua.bgw.animation.ComponentAnimation
import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.animation.FlipAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.animation.RotationAnimation
import tools.aqua.bgw.animation.ScaleAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.animation.SteppedComponentAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.RootComponent
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Logger
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
   * before the [Animation] actually starts playing.
   *
   * @param animation [Animation] to play.
   * @return Boolean whether the animation successfully started playing.
   */
  fun playAnimation(animation: Animation): Boolean {
    // Check if this is a top-level singular ComponentAnimation trying to animate an already
    // animating component
    if (animation is ComponentAnimation<*> && animation.componentView.componentAnimating) {
      Logger.warning(
          "The ComponentView ${animation.componentView} is already animating. " +
              "Please wait until the animation is over or chain animations together using ParallelAnimations or SequentialAnimations.")
      return false
    }

    // For Sequential/Parallel animations, collect all components and check if any are already
    // animating
    if (animation is SequentialAnimation || animation is ParallelAnimation) {
      val allComponents = collectComponentsFromAnimation(animation)
      val alreadyAnimating = allComponents.filter { it.componentAnimating }

      if (alreadyAnimating.isNotEmpty()) {
        Logger.warning(
            "One or more components in the animation are already animating: ${alreadyAnimating.joinToString(", ")}. " +
                "Please wait until the animations are over or chain animations together using ParallelAnimations or SequentialAnimations.")
        return false
      }
    }

    // Cache initial state for ALL components in this animation tree BEFORE starting any animations
    val allComponents = collectComponentsFromAnimation(animation)
    for (component in allComponents) {
      // Only cache if not already cached (first animation on this component)
      if (!Frontend.animationCache.containsKey(component.id)) {
        cacheInitialComponentState(component)
      }
    }

    addAnimationRecursively(animation)
    Frontend.sendAnimation(animation)
    return true
  }

  /**
   * Recursively collects all ComponentViews from an animation tree. This is used to check if any
   * components are already animating before starting a new animation.
   */
  private fun collectComponentsFromAnimation(animation: Animation): Set<ComponentView> {
    val components = mutableSetOf<ComponentView>()

    when (animation) {
      is ComponentAnimation<*> -> components.add(animation.componentView)
      is SequentialAnimation -> {
        animation.animations.forEach { components.addAll(collectComponentsFromAnimation(it)) }
      }
      is ParallelAnimation -> {
        animation.animations.forEach { components.addAll(collectComponentsFromAnimation(it)) }
      }
    }

    return components
  }

  /**
   * Caches the initial state of a component when animations start. This ensures all animations
   * (Sequential/Parallel) use the same initial state.
   */
  private fun cacheInitialComponentState(componentView: ComponentView) {
    Frontend.animationCache[componentView.id] =
        AnimationPropertyCache(
            posX = componentView.posX.toInt(),
            posY = componentView.posY.toInt(),
            scaleX = componentView.scaleX,
            scaleY = componentView.scaleY,
            rotation = componentView.rotation,
            opacity = componentView.opacity,
            visual = componentView.visual)
  }

  private fun addAnimationRecursively(animation: Animation, parent: Animation? = null) {
    animation.parentAnimation = parent

    if (animation is ComponentAnimation<*>) {
      val animationType =
          when (animation) {
            is MovementAnimation<*> -> AnimationType.MOVEMENT
            is ScaleAnimation<*> -> AnimationType.SCALE
            is RotationAnimation<*> -> AnimationType.ROTATION
            is FadeAnimation<*> -> AnimationType.FADE
            is FlipAnimation<*> -> AnimationType.FLIP
            is SteppedComponentAnimation<*> -> AnimationType.STEPPED
          }

      animation.componentView.animationTypes.add(animationType)
      animation.componentView.componentAnimating = true
    }

    animation.isRunning = true

    when (animation) {
      is SequentialAnimation -> {
        animation.animations.forEach { addAnimationRecursively(it, animation) }
        animations.add(animation)
      }
      is ParallelAnimation -> {
        animation.animations.forEach { addAnimationRecursively(it, animation) }
        animations.add(animation)
      }
      else -> animations.add(animation)
    }
  }

  /**
   * Stops all currently playing [Animation]s. This will NOT revert persisted animations that
   * already finished. It will however cancel so that onFinished callbacks do not get invoked.
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
