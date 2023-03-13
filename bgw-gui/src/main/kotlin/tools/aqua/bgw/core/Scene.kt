/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.builder.DragDataObject
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.RootComponent
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.observable.lists.ObservableArrayList
import tools.aqua.bgw.observable.lists.ObservableList
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.CoordinatePlain
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for BGW scenes.
 *
 * @param T Generic [ComponentView].
 * @param width [Scene] width in virtual coordinates.
 * @param height [Scene] height in virtual coordinates.
 * @param background [Scene] [background] [Visual].
 *
 * @see BoardGameScene
 * @see MenuScene
 */
sealed class Scene<T : ComponentView>(width: Number, height: Number, background: Visual) {

  /**
   * [MutableList] containing all [ComponentView]s currently below mouse position while performing a
   * drag gesture.
   */
  internal val dragTargetsBelowMouse: MutableSet<ComponentView> = mutableSetOf()

  /**
   * [Property] for the currently dragged [DynamicComponentView] or `null` if no
   * [DynamicComponentView] is currently dragged.
   */
  internal val draggedComponentProperty: Property<DragDataObject?> = Property(null)

  /**
   * Currently dragged [ComponentView] encapsulated in a [DragDataObject] or `null` if no
   * [DynamicComponentView] is currently dragged.
   */
  val draggedComponent: DynamicComponentView?
    get() = draggedComponentProperty.value?.draggedComponent

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
  val width: Double = width.toDouble()

  /** The height of this [Scene] in virtual coordinates. */
  val height: Double = height.toDouble()

  /** All [ComponentView]s on the root node. */
  internal val rootComponents: ObservableList<T> = ObservableArrayList()

  /**
   * [Property] for the [background] [Visual] of this [Scene].
   *
   * @see background
   */
  internal val backgroundProperty: Property<Visual> = Property(background)

  /**
   * The background [Visual] of this [Scene].
   *
   * @see backgroundProperty
   */
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

  /**
   * Opacity of the [background] of this [Scene].
   *
   * @see opacityProperty
   */
  var opacity: Double
    get() = opacityProperty.value
    set(value) {
      require(value in 0.0..1.0) { "Value must be between 0 and 1 inclusive." }

      opacityProperty.value = value
    }

  /**
   * [Property] for the currently displayed zoom detail of this [Scene].
   *
   * @see zoomDetail
   */
  internal val zoomDetailProperty: Property<CoordinatePlain> =
      Property(CoordinatePlain(0, 0, width, height))

  /**
   * The currently displayed zoom detail of this [Scene].
   *
   * @see zoomDetailProperty
   */
  internal var zoomDetail
    get() = zoomDetailProperty.value
    set(value) {
      zoomDetailProperty.value = value
    }

  /** [Map] for all [ComponentView]s to their [StackPane]s. */
  internal val componentsMap: MutableMap<ComponentView, StackPane> = HashMap()

  /** Cache for the background of a [Scene]. */
  internal var backgroundCache: Region? = null

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
   * Gets invoked with a [KeyEvent] whenever a Character is typed. Gets invoked after [onKeyPressed].
   *
   * @see KeyEvent
   * @see onKeyPressed
   */
  var onKeyTyped: ((KeyEvent) -> Unit)? = null

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
   * Plays given [Animation].
   *
   * @param animation [Animation] to play.
   */
  fun playAnimation(animation: Animation) {
    animations.add(animation)
  }

  //	/**
  //	 * Zooms [Scene] to given bounds.
  //	 *
  //	 * @param fromX left bound.
  //	 * @param fromY top bound.
  //	 * @param toX right bound.
  //	 * @param toY bottom bound.
  //	 */
  //	fun zoomTo(fromX: Number, fromY: Number, toX: Number, toY: Number): Unit =
  //		zoomTo(fromX.toDouble(), fromY.toDouble(), toX.toDouble(), toY.toDouble())

  //	/**
  //	 * Zooms [Scene] to given bounds.
  //	 *
  //	 * @param from top left [Coordinate].
  //	 * @param to bottom right [Coordinate].
  //	 */
  //	fun zoomTo(from: Coordinate, to: Coordinate): Unit =
  //		zoomTo(from.xCoord, from.yCoord, to.xCoord, to.yCoord)

  //	/**
  //	 * Zooms [Scene] to given bounds.
  //	 *
  //	 * @param to layout bounds.
  //	 */
  //	fun zoomTo(to: CoordinatePlain): Unit =
  //		zoomTo(to.topLeft, to.bottomRight)

  //	/**
  //	 * Zooms scene out to max bounds.
  //	 */
  //	fun zoomOut() {
  //		zoomDetail = CoordinatePlain(0, 0, width, height)
  //	}

  //	/**
  //	 * Sets [zoomDetailProperty] to given bounds.
  //	 * Checks for targets out of layout bounds.
  //	 */
  //	private fun zoomTo(fromX: Double, fromY: Double, toX: Double, toY: Double) {
  //		when {
  //			fromX < 0 || fromY < 0 || toX < 0 || toY < 0 ->
  //				throw IllegalArgumentException("All bounds must be positive.")
  //			fromX >= toX ->
  //				throw IllegalArgumentException("Right X bound is not greater than left X bound.")
  //			fromY >= toY ->
  //				throw IllegalArgumentException("Bottom Y bound is not greater than top Y bound.")
  //			toX > width ->
  //				throw IllegalArgumentException("Right X bound is greater than scene width.")
  //			toY > height ->
  //				throw IllegalArgumentException("Bottom Y bound is greater than scene height.")
  //		}
  //
  //		zoomDetail = CoordinatePlain(fromX, fromY, toX, toY)
  //	}

  /**
   * Searches [node] recursively through the visual tree and logs path where the [node] appears as
   * first component and the [rootNode] as last.
   *
   * @param node Child to find.
   *
   * @return Path to child.
   *
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
