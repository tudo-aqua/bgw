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

@file:Suppress("unused", "MemberVisibilityCanBePrivate", "TooManyFunctions", "GrazieInspection")

package tools.aqua.bgw.components

import IDGenerator
import kotlin.math.floor
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.event.*
import tools.aqua.bgw.observable.properties.*
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.CoordinatePlain
import tools.aqua.bgw.visual.Visual

/**
 * [ComponentView] is the abstract baseclass of all framework components.
 *
 * It defines important fields and functions that are necessary to visualize inheriting components.
 *
 * @constructor Creates a [ComponentView].
 *
 * @param posX the X coordinate for this [ComponentView] relative to its container.
 * @param posY the Y coordinate for this [ComponentView] relative to its container.
 * @param width width for this [ComponentView].
 * @param height height for this [ComponentView].
 * @param visual visual for this [ComponentView].
 *
 * @throws tools.aqua.bgw.exception.IllegalInheritanceException inheriting from this [Class] is not
 * advised, because it cannot be rendered and trying to do so will result in an
 * [tools.aqua.bgw.exception.IllegalInheritanceException].
 *
 * @see tools.aqua.bgw.exception.IllegalInheritanceException
 */
abstract class ComponentView
internal constructor(posX: Number, posY: Number, width: Number, height: Number, visual: Visual) {

  internal val id = IDGenerator.generateID()

  /**
   * Property for the order of [ComponentView] inside of [parent].
   *
   * @see zIndex
   */
  internal val zIndexProperty: IntegerProperty = IntegerProperty(0)

  /** Order of [ComponentView] inside of [parent]. */
  var zIndex: Int
    get() = zIndexProperty.value
    set(value) {
      if (parent is LayeredContainer<*>) {
        try {
          @Suppress("UNCHECKED_CAST")
          (parent as LayeredContainer<ComponentView>).setZIndex(this, value)
        } catch (_: ClassCastException) {
          error("$parent is not a compatible container type")
        }
      } else {
        zIndexProperty.value = value
      }
    }

  /**
   * The parent of this [ComponentView].
   *
   * `null` if this [ComponentView] is not contained in a [GameComponentContainer], [LayoutView], or
   * a [Scene].
   *
   * If the component has been added directly to a [Scene], [parent] is equal to the scene's
   * [RootComponent].
   *
   * If the component is contained within a container, parent is equal to that container.
   * @see Scene
   * @see GameComponentContainer
   */
  var parent: ComponentView? = null
    internal set

  /**
   * Field that indicates whether posX and posY denote the center or top left of this
   * [ComponentView].
   */
  var isLayoutFromCenter: Boolean = false
    internal set

  /** Name field only for debugging purposes. Has no effect on rendering. */
  val name: String = this::class.simpleName + "@" + this.hashCode().toHexString()
  private fun Int.toHexString() = "0x" + this.toString(16).padStart(8, '0')

  /**
   * [Property] for the horizontal position of this [ComponentView].
   *
   * @see posX
   * @see actualPosX
   */
  internal val posXProperty: DoubleProperty = DoubleProperty(posX.toDouble())

  /**
   * Horizontal position of this [ComponentView].
   *
   * @see actualPosX
   */
  var posX: Double
    get() = posXProperty.value
    set(value) {
      posXProperty.value = value
    }

  /**
   * Horizontal position of this [ComponentView] considering scale.
   *
   * @see posX
   */
  var actualPosX: Double
    get() {
      val parent = parent
      val offset = if (parent != null && parent.isLayoutFromCenter) parent.actualWidth / 2 else 0.0

      return posX + (width - actualWidth) / 2 - offset
    }
    private set(_) {}

  /**
   * [Property] for the vertical position of this [ComponentView].
   *
   * @see posY
   * @see actualPosY
   */
  internal val posYProperty: DoubleProperty = DoubleProperty(posY.toDouble())

  /**
   * Vertical position of this [ComponentView].
   *
   * @see actualPosY
   */
  var posY: Double
    get() = posYProperty.value
    set(value) {
      posYProperty.value = value
    }

  /**
   * Vertical position of this [ComponentView] considering scale.
   *
   * @see posY
   */
  var actualPosY: Double
    get() {
      val parent = parent
      val offset = if (parent != null && parent.isLayoutFromCenter) parent.actualHeight / 2 else 0.0

      return posY + (height - actualHeight) / 2 - offset
    }
    private set(_) {}

  /**
   * [Property] for the [width] of this [ComponentView].
   *
   * @see width
   * @see actualWidth
   */
  internal val widthProperty: DoubleProperty = DoubleProperty(width.toDouble())

  /**
   * The [width] for this [ComponentView].
   *
   * @see actualWidth
   */
  var width: Double
    get() = widthProperty.value
    set(value) {
      widthProperty.value = value
    }

  /**
   * The actual [width] for this [ComponentView] considering scale.
   *
   * @see width
   */
  var actualWidth: Double
    get() = width * scaleX
    private set(_) {}

  /**
   * [Property] for the [height] of this [ComponentView].
   *
   * @see height
   * @see actualHeight
   */
  internal val heightProperty: DoubleProperty = DoubleProperty(height.toDouble())

  /**
   * The [height] for this [ComponentView].
   *
   * @see actualHeight
   */
  var height: Double
    get() = heightProperty.value
    set(value) {
      heightProperty.value = value
    }

  /**
   * The actual [height] for this [ComponentView] considering scale.
   *
   * @see height
   */
  var actualHeight: Double
    get() = height * scaleY
    private set(_) {}

  /**
   * [Property] for the horizontal scale of this [ComponentView].
   *
   * @see scaleX
   */
  internal val scaleXProperty: DoubleProperty = DoubleProperty(1.0)

  /** Horizontal scale of this [ComponentView]. */
  var scaleX: Double
    get() = scaleXProperty.value
    set(value) {
      scaleXProperty.value = value
    }

  /**
   * [Property] for the vertical scale of this [ComponentView].
   *
   * @see scaleY
   */
  internal val scaleYProperty: DoubleProperty = DoubleProperty(1.0)

  /** Vertical scale of this [ComponentView]. */
  var scaleY: Double
    get() = scaleYProperty.value
    set(value) {
      scaleYProperty.value = value
    }

  /**
   * Scale of this [ComponentView].
   *
   * @throws IllegalStateException When invoking getter if scaleX and scaleY differ. Use scaleX and
   * scaleY getters instead. Safe to use if scale is equal in both directions.
   *
   * @see scaleX
   * @see scaleY
   */
  var scale: Double
    get() {
      require(scaleX == scaleY) {
        "Cannot return single scale because scaleX ($scaleX) and scaleY ($scaleY) are different."
      }
      return scaleX
    }
    set(value) {
      scaleX = value
      scaleY = value
    }

  /**
   * Returns a [CoordinatePlain] containing the component's corner [Coordinate]s and its layout
   * bounds.
   */
  var layoutBounds: CoordinatePlain
    get() =
        CoordinatePlain(
                topLeftX = actualPosX,
                topLeftY = actualPosY,
                bottomRightX = actualPosX + actualWidth,
                bottomRightY = actualPosY + actualHeight)
            .rotated(
                rotation,
                Coordinate(
                    xCoord = actualPosX + actualWidth / 2, yCoord = actualPosY + actualHeight / 2))
    private set(_) {}

  /**
   * [Property] for the rotation of this [ComponentView] in degrees.
   *
   * Values not in [0,360) get mapped to values in [0,360) by modulo operation with 360.
   *
   * example conversions: -10 -> 350 -370 -> 350 370 -> 10 730 -> 10
   *
   * @see rotation
   */
  internal val rotationProperty: DoubleProperty = DoubleProperty(0.0)

  /**
   * Rotation of this [ComponentView] in degrees.
   *
   * Values not in [0,360) get mapped to values in [0,360) by modulo operation with 360.
   *
   * example conversions: -10 -> 350 -370 -> 350 370 -> 10 730 -> 10
   */
  var rotation: Double
    get() = rotationProperty.value
    set(value) {
      rotationProperty.value = value - floor(value / 360.0) * 360.0
    }

  /**
   * [Property] for the current [Visual] of this [ComponentView].
   *
   * @see visual
   */
  internal val visualProperty: Property<Visual> = Property(visual)

  /** Current [Visual]. */
  open var visual: Visual
    get() = visualProperty.value
    /** Sets a copy of the given [Visual] [value] to this field and refreshes GUI. */
    set(value) {
      visualProperty.value = value
    }

  /**
   * [Property] for the [opacity] of this [ComponentView].
   *
   * Must be in range 0.0 to 1.0.
   *
   * 0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.
   *
   * Note that invisible objects (opacity == 0.0) still remain interactive.
   *
   * @see opacity
   */
  internal val opacityProperty: LimitedDoubleProperty =
      LimitedDoubleProperty(
          lowerBoundInclusive = 0.0, upperBoundInclusive = 1.0, initialValue = 1.0)

  /**
   * Opacity of this [ComponentView].
   *
   * Must be in range 0.0 to 1.0.
   *
   * 0.0 corresponds to 0% opacity, where 1.0 corresponds to 100% opacity.
   *
   * Note that invisible objects (opacity == 0.0) still remain interactive.
   */
  var opacity: Double
    get() = opacityProperty.value
    set(value) {
      require(value in 0.0..1.0) { "Value must be between 0 and 1 inclusive." }

      opacityProperty.value = value
    }

  /**
   * [Property] for the visibility of this [ComponentView].
   *
   * Invisible [ComponentView]s are disabled. An object marked as visible may still be opaque due to
   * opacity.
   */
  internal val isVisibleProperty: BooleanProperty = BooleanProperty(true)

  /**
   * Visibility of this [ComponentView].
   *
   * Invisible [ComponentView]s are disabled. An object marked as visible may still be opaque due to
   * opacity.
   *
   * @see isDisabled
   * @see opacity
   */
  var isVisible: Boolean
    get() = isVisibleProperty.value
    set(value) {
      isVisibleProperty.value = value
    }

  /**
   * [Property] that controls if user input events cause input functions of this [ComponentView] to
   * get invoked.
   *
   * `true` means no invocation, where `false` means invocation.
   *
   * For a list of affected functions refer to the `See Also` section.
   *
   * @see onMouseEntered
   * @see onMouseExited
   * @see dropAcceptor
   * @see onDragDropped
   * @see onKeyPressed
   * @see onKeyReleased
   * @see onKeyTyped
   * @see onMousePressed
   * @see onMouseReleased
   * @see onMouseClicked
   *
   * @see isDisabled
   */
  internal val isDisabledProperty: BooleanProperty = BooleanProperty(false)

  /**
   * Controls if user input events cause input functions of this [ComponentView] to get invoked.
   *
   * `true` means no invocation, where `false` means invocation.
   *
   * For a list of affected functions refer to the `See Also` section.
   *
   * @see onMouseEntered
   * @see onMouseExited
   * @see dropAcceptor
   * @see onDragDropped
   * @see onKeyPressed
   * @see onKeyReleased
   * @see onKeyTyped
   * @see onMousePressed
   * @see onMouseReleased
   * @see onMouseClicked
   */
  var isDisabled: Boolean
    get() = isDisabledProperty.value
    set(value) {
      isDisabledProperty.value = value
    }

  /**
   * [Property] that controls whether this [ComponentView] is focusable or not.
   *
   * @see isFocusable
   */
  internal val isFocusableProperty: BooleanProperty = BooleanProperty(true)

  /** Controls whether this [ComponentView] is focusable or not. */
  var isFocusable: Boolean
    get() = isFocusableProperty.value
    set(value) {
      isFocusableProperty.value = value
    }

  /**
   * Gets invoked with a [MouseEvent] whenever the mouse enters this [ComponentView].
   *
   * @see Event
   * @see isDisabled
   */
  var onMouseEntered: ((MouseEvent) -> Unit)? = null

  /**
   * Gets invoked with a [MouseEvent] whenever the mouse leaves this [ComponentView].
   *
   * @see Event
   * @see isDisabled
   */
  var onMouseExited: ((MouseEvent) -> Unit)? = null

  /**
   * Gets invoked with a [MouseEvent] whenever the mouse is pressed inside this [ComponentView].
   *
   * @see MouseEvent
   * @see isDisabled
   */
  var onMousePressed: ((MouseEvent) -> Unit)? = null

  /**
   * Gets invoked with a [MouseEvent] whenever the mouse is released inside this [ComponentView].
   *
   * @see MouseEvent
   * @see isDisabled
   */
  var onMouseReleased: ((MouseEvent) -> Unit)? = null

  /**
   * Gets invoked with a [MouseEvent] whenever the mouse is clicked inside this [ComponentView].
   * Gets invoked after [onMousePressed] and [onMouseReleased].
   *
   * @see MouseEvent
   * @see onMousePressed
   * @see onMouseReleased
   * @see isDisabled
   */
  var onMouseClicked: ((MouseEvent) -> Unit)? = null

  /**
   * Gets invoked with a [WheelEvent] whenever the mouse wheel is turned while the mouse is inside
   * this [ComponentView].
   *
   * @see WheelEvent
   * @see isDisabled
   */
  var onWheel: ((WheelEvent) -> Unit)? = null

  /**
   * Gets invoked with a [KeyEvent] whenever a key is pressed while this [ComponentView] has focus.
   *
   * @see KeyEvent
   * @see isDisabled
   * @see isFocusable
   */
  var onKeyPressed: ((KeyEvent) -> Unit)? = null

  /**
   * Gets invoked with a [KeyEvent] whenever a key is released while this [ComponentView] has focus.
   *
   * @see KeyEvent
   * @see isDisabled
   * @see isFocusable
   */
  var onKeyReleased: ((KeyEvent) -> Unit)? = null

  /**
   * Gets invoked with a [KeyEvent] whenever a Character is typed while this [ComponentView] has
   * focus. Gets invoked after [onKeyPressed].
   *
   * @see KeyEvent
   * @see onKeyPressed
   * @see isDisabled
   * @see isFocusable
   */
  @Deprecated(
      "The onKeyTyped event is defined in this specification for reference and completeness and will be removed in a future version.")
  var onKeyTyped: ((KeyEvent) -> Unit)? = null

  /**
   * Returns whether this [ComponentView] is a valid drop target for the dragged component in the
   * given [DragEvent] or not.
   *
   * Implement this function in such a way that it returns `true` if this [ComponentView] accepts
   * the drop of the given [DropEvent.draggedComponent] or `false` if a drop is not valid. The
   * [DropEvent.draggedComponent] will snap back if all available drop targets return `false`.
   *
   * It is advised not to modify the [Scene] or its children in this function. A better suited
   * function to modify the [Scene] or its children after a drag and drop gesture is [onDragDropped]
   * .
   *
   * Note: [onDragDropped] only gets invoked if the dropAcceptor returns `true` for the given
   * [DropEvent].
   *
   * @see onDragDropped
   * @see DropEvent
   * @see isDisabled
   */
  var dropAcceptor: ((DragEvent) -> Boolean)? = null

  /**
   * Gets invoked with a [DragEvent] whenever the mouse enters this [ComponentView] while performing
   * a drag gesture.
   *
   * @see DragEvent
   */
  var onDragGestureEntered: ((DragEvent) -> Unit)? = null

  /**
   * Gets invoked with a [DragEvent] whenever the mouse leaves this [ComponentView] while performing
   * a drag gesture.
   *
   * @see DragEvent
   */
  var onDragGestureExited: ((DragEvent) -> Unit)? = null

  /**
   * Gets invoked with a [DragEvent] whenever a drag and drop gesture finishes over this
   * [ComponentView] and the [dropAcceptor] returns `true` for the given [DragEvent].
   *
   * @see dropAcceptor
   * @see DropEvent
   * @see isDisabled
   */
  var onDragDropped: ((DragEvent) -> Unit)? = null

  /**
   * Repositions this [ComponentView] to the specified coordinates.
   *
   * @param posX New X coordinate.
   * @param posY New Y coordinate.
   */
  fun reposition(posX: Number, posY: Number) {
    this.posX = posX.toDouble()
    this.posY = posY.toDouble()
  }

  /**
   * Adds an offset to this [ComponentView]'s Position.
   *
   * @param offsetX Offset for the X coordinate.
   * @param offsetY Offset for the Y coordinate.
   */
  fun offset(offsetX: Number, offsetY: Number) {
    this.posX += offsetX.toDouble()
    this.posY += offsetY.toDouble()
  }

  /**
   * Resizes this [ComponentView] to the specified [width] and [height].
   *
   * @param width New width.
   * @param height New height.
   */
  fun resize(width: Number, height: Number) {
    this.width = width.toDouble()
    this.height = height.toDouble()
  }

  /**
   * Scales this [ComponentView] by the given [scalar].
   *
   * @param scalar New scale.
   *
   * @throws IllegalArgumentException If the given [scalar] is negative.
   */
  fun scale(scalar: Number) {
    this.scale = checkScalarPositive(scalar)
  }

  /**
   *
   * Scales this [ComponentView]'s width by the given [scalar].
   *
   * @param scalar New x scale.
   *
   * @throws IllegalArgumentException If the given [scalar] is negative.
   */
  fun scaleX(scalar: Number) {
    this.scaleX = checkScalarPositive(scalar)
  }

  /**
   * Scales this [ComponentView]'s height by the given [scalar].
   *
   * @param scalar New y scale.
   *
   * @throws IllegalArgumentException If the given [scalar] is negative.
   */
  fun scaleY(scalar: Number) {
    this.scaleY = checkScalarPositive(scalar)
  }

  /**
   * Checks given [scalar] for being positive.
   *
   * @param scalar scalar to be checked.
   *
   * @throws IllegalArgumentException If the given [scalar] is negative.
   */
  private fun checkScalarPositive(scalar: Number): Double {
    val scalarDoubleValue = scalar.toDouble()
    require(scalarDoubleValue >= 0) {
      "Only non-negative scalars are allowed. Provided scalar was $scalarDoubleValue."
    }
    return scalarDoubleValue
  }

  /**
   * Rotates this [ComponentView] by the given number of [degrees].
   *
   * @param degrees Degrees to add to current rotation. May be negative.
   */
  fun rotate(degrees: Number) {
    this.rotation += degrees.toDouble()
  }

  /**
   * Removes this component from its parent.
   *
   * @return Returns the former parent.
   *
   * @throws IllegalStateException If this component is not contained in any container. Use parent
   * field to check.
   *
   * @see parent
   */
  fun removeFromParent(): ComponentView {
    val par = parent

    checkNotNull(par) { "This child has no parent." }

    return par.also { it.removeChild(this) }
  }

  /**
   * Removes component from container's children if supported.
   *
   * @param component Child to be removed.
   *
   * @throws RuntimeException If the componentView does not support children.
   */
  internal abstract fun removeChild(component: ComponentView)

  /**
   * Function returning a contained child's coordinates within this [ComponentView] if supported.
   *
   * This method has to be overridden.
   *
   * Returns `null` on all [ComponentView]s not supporting this feature.
   *
   * @param child Child to find.
   *
   * @return Coordinate of given child in this [ComponentView] or `null` if not supported.
   */
  @Suppress("FunctionOnlyReturningConstant")
  internal open fun getChildPosition(child: ComponentView): Coordinate? = null

  /**
   * Function returning a contained child's coordinates within this [ComponentView] with scale if
   * supported.
   *
   * This method has to be overridden.
   *
   * Returns `null` on all [ComponentView]s not supporting this feature.
   *
   * @param child Child to find.
   *
   * @return Coordinate of given child in this [ComponentView] or `null` if not supported.
   */
  @Suppress("FunctionOnlyReturningConstant")
  internal open fun getActualChildPosition(child: ComponentView): Coordinate? = null

  /**
   * Puts the [ComponentView] to the front inside its [parent] and Changes its [zIndex] accordingly.
   *
   * @throws IllegalStateException if the [ComponentView] does not have a parent
   * @throws IllegalStateException if the [parent] is not [LayeredContainer] with the generic type
   * [ComponentView]
   */
  fun toFront() {
    if (parent != null) {
      zIndexProperty.value =
          when (parent) {
            is GameComponentContainer<*> ->
                (parent as GameComponentContainer<*>).components.last().zIndex
            is Pane<*> -> (parent as Pane<*>).components.last().zIndex
            is RootComponent<*> -> (parent as RootComponent<*>).scene.rootComponents.last().zIndex
            else -> 0
          }
    }
    checkNotNull(parent) { "$this does not have a parent" }
    if (parent is LayeredContainer<*>) {
      try {
        @Suppress("UNCHECKED_CAST") (parent as LayeredContainer<ComponentView>).toFront(this)
      } catch (_: ClassCastException) {
        error("$parent is not a compatible container type")
      }
    }
  }

  /**
   * Puts the [ComponentView] to the back inside its [parent] and Changes its [zIndex] accordingly.
   *
   * @throws IllegalStateException if the [ComponentView] does not have a parent
   * @throws IllegalStateException if the [parent] is not [LayeredContainer] with the generic type
   * [ComponentView]
   */
  fun toBack() {
    zIndexProperty.value =
        when (parent) {
          is GameComponentContainer<*> ->
              (parent as GameComponentContainer<*>).components.first().zIndex
          is Pane<*> -> (parent as Pane<*>).components.first().zIndex
          is RootComponent<*> -> (parent as RootComponent<*>).scene.rootComponents.first().zIndex
          else -> 0
        }
    checkNotNull(parent) { "$this does not have a parent" }
    if (parent is LayeredContainer<*>) {
      try {
        @Suppress("UNCHECKED_CAST") (parent as LayeredContainer<ComponentView>).toBack(this)
      } catch (_: ClassCastException) {
        error("$parent is not a compatible container type")
      }
    }
  }
}
