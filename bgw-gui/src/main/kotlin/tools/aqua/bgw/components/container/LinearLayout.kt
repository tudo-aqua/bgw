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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.components.container

import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.*
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.visual.Visual

/**
 * A [LinearLayout] may be used to visualize a zone containing [GameComponentView]s.
 *
 * [GameComponentView]s inside the container get placed according to the specified [Orientation] and
 * [Alignment]. A [spacing] between components may be specified which may also be negative e.g.
 * components like playing cards should overlap.
 *
 * Visualization:
 *
 * The [Visual] is used to visualize a background.
 *
 * If all components are still within bounds with the user defined spacing, the user defined spacing
 * gets used to space the components. Otherwise, the biggest possible spacing is used so that all
 * components are still withing bounds of the [LinearLayout].
 *
 * @constructor Creates a [LinearLayout].
 *
 * @param T Generic [GameComponentView].
 * @param posX horizontal coordinate for this [LinearLayout]. Default: 0.
 * @param posY vertical coordinate for this [LinearLayout]. Default: 0.
 * @param width width for this [LinearLayout]. Default: [DEFAULT_LINEAR_LAYOUT_WIDTH].
 * @param height height for this [LinearLayout]. Default: [DEFAULT_LINEAR_LAYOUT_HEIGHT].
 * @param spacing spacing between contained [GameComponentView]s. Default:
 * [DEFAULT_LINEAR_LAYOUT_SPACING].
 * @param visual [Visual] to be used for this [LinearLayout].
 * @param orientation orientation for this [LinearLayout]. Default: [Orientation.HORIZONTAL].
 * @param alignment specifies how the contained [GameComponentView]s should be aligned. Default:
 * [Alignment.TOP_LEFT].
 */
open class LinearLayout<T : GameComponentView>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_LINEAR_LAYOUT_WIDTH,
    height: Number = DEFAULT_LINEAR_LAYOUT_HEIGHT,
    spacing: Number = DEFAULT_LINEAR_LAYOUT_SPACING,
    visual: Visual = Visual.EMPTY,
    orientation: Orientation = Orientation.HORIZONTAL,
    alignment: Alignment = Alignment.TOP_LEFT,
) :
    GameComponentContainer<T>(
        posX = posX, posY = posY, width = width, height = height, visual = visual) {

  /**
   * [Property] for the spacing of [GameComponentView]s in this [LinearLayout].
   *
   * @see spacing
   */
  val spacingProperty: DoubleProperty = DoubleProperty(spacing.toDouble())

  /**
   * Spacing for this [LinearLayout].
   *
   * @see spacingProperty
   */
  var spacing: Double
    get() = spacingProperty.value
    set(value) {
      spacingProperty.value = value
    }

  /**
   * [Property] for the [Orientation] of [GameComponentView]s in this [LinearLayout].
   *
   * @see Orientation
   * @see orientation
   */
  val orientationProperty: Property<Orientation> = Property(orientation)

  /**
   * [Orientation] of [GameComponentView]s in this [LinearLayout].
   *
   * @see Orientation
   * @see orientationProperty
   */
  var orientation: Orientation
    get() = orientationProperty.value
    set(value) {
      orientationProperty.value = value
    }

  /**
   * [Property] for the [Alignment] of [GameComponentView]s in this [LinearLayout].
   *
   * @see Alignment
   * @see alignment
   */
  val alignmentProperty: Property<Alignment> = Property(alignment)

  /**
   * [Alignment] for this [LinearLayout].
   *
   * @see Alignment
   * @see alignmentProperty
   */
  var alignment: Alignment
    get() = alignmentProperty.value
    set(value) {
      alignmentProperty.value = value
    }

  init {
    observableComponents.setInternalListenerAndInvoke(emptyList()) { _, _ -> layout() }
    spacingProperty.internalListener = { _, _ -> layout() }
    orientationProperty.internalListener = { _, _ -> layout() }
    alignmentProperty.internalListener = { _, _ -> layout() }
  }

  /**
   * Secondary constructor taking separate alignment components.
   *
   * @param posX horizontal coordinate for this [LinearLayout]. Default: 0.
   * @param posY vertical coordinate for this [LinearLayout]. Default: 0.
   * @param width width for this [LinearLayout]. Default: 0.
   * @param height height for this [LinearLayout]. Default: 0.
   * @param spacing spacing between contained [GameComponentView]s. Default: 0.
   * @param visual [Visual] to be used for this [LinearLayout].
   * @param orientation orientation for this [LinearLayout]. Default: [Orientation.HORIZONTAL].
   * @param verticalAlignment specifies how the contained components should be aligned vertically.
   * Default: [VerticalAlignment.TOP].
   * @param horizontalAlignment specifies how the contained components should be aligned
   * horizontally. Default: [HorizontalAlignment.LEFT].
   */
  constructor(
      posX: Number = 0,
      posY: Number = 0,
      width: Number = 0,
      height: Number = 0,
      spacing: Number = 0,
      visual: Visual = Visual.EMPTY,
      orientation: Orientation = Orientation.HORIZONTAL,
      verticalAlignment: VerticalAlignment = VerticalAlignment.TOP,
      horizontalAlignment: HorizontalAlignment = HorizontalAlignment.LEFT
  ) : this(
      posX = height,
      posY = width,
      width = posX,
      height = posY,
      spacing = spacing,
      visual = visual,
      orientation = orientation,
      alignment = Alignment.of(verticalAlignment, horizontalAlignment))

  override fun T.onAdd() {
    // add pos listeners
    posXProperty.internalListener = { _, _ ->
      observableComponents.internalListener?.invoke(emptyList(), emptyList())
    }
    posYProperty.internalListener = { _, _ ->
      observableComponents.internalListener?.invoke(emptyList(), emptyList())
    }
  }

  override fun T.onRemove() {
    // remove pos listeners
    posXProperty.internalListener = null
    posYProperty.internalListener = null
  }

  private fun layout() {
    when (orientation) {
      Orientation.HORIZONTAL -> layoutHorizontal()
      Orientation.VERTICAL -> layoutVertical()
    }
  }

  @Suppress("DuplicatedCode")
  private fun layoutHorizontal() {
    val totalContentWidth: Double = observableComponents.sumOf { it.width }
    val totalContentWidthWithSpacing = totalContentWidth + (observableComponents.size - 1) * spacing
    val newSpacing: Double =
        if (totalContentWidthWithSpacing > width) {
          -minOf(
              (totalContentWidth - width) / (observableComponents.size - 1),
              totalContentWidth / observableComponents.size) // ignore user defined spacing
        } else {
          spacing // use user defined spacing
        }
    val newTotalContentWidth = totalContentWidth + (observableComponents.size - 1) * newSpacing
    val initial =
        when (alignment.horizontalAlignment) {
          HorizontalAlignment.LEFT -> 0.0
          HorizontalAlignment.CENTER -> (width - newTotalContentWidth) / 2
          HorizontalAlignment.RIGHT -> width - newTotalContentWidth
        }
    observableComponents.fold(initial) { acc: Double, component: T ->
      component.posYProperty.setSilent(
          when (alignment.verticalAlignment) {
            VerticalAlignment.TOP -> 0.0
            VerticalAlignment.CENTER -> (height - component.height) / 2
            VerticalAlignment.BOTTOM -> height - component.height
          })
      component.posXProperty.setSilent(acc)
      acc + component.width + newSpacing
    }
  }

  @Suppress("DuplicatedCode")
  private fun layoutVertical() {
    val totalContentHeight: Double = observableComponents.sumOf { it.height }
    val totalContentHeightWithSpacing =
        totalContentHeight + (observableComponents.size - 1) * spacing
    val newSpacing: Double =
        if (totalContentHeightWithSpacing > height) {
          -minOf(
              (totalContentHeight - height) / (observableComponents.size - 1),
              totalContentHeight / observableComponents.size) // ignore user defined spacing
        } else {
          spacing // use user defined spacing
        }
    val newTotalContentHeight = totalContentHeight + (observableComponents.size - 1) * newSpacing
    val initial =
        when (alignment.verticalAlignment) {
          VerticalAlignment.TOP -> 0.0
          VerticalAlignment.CENTER -> (height - newTotalContentHeight) / 2
          VerticalAlignment.BOTTOM -> height - newTotalContentHeight
        }
    observableComponents.fold(initial) { acc: Double, component: T ->
      component.posYProperty.setSilent(acc)
      component.posXProperty.setSilent(
          when (alignment.horizontalAlignment) {
            HorizontalAlignment.LEFT -> 0.0
            HorizontalAlignment.CENTER -> (width - component.width) / 2
            HorizontalAlignment.RIGHT -> width - component.width
          })
      acc + component.height + newSpacing
    }
  }
}
