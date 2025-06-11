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

package tools.aqua.bgw.components.uicomponents

import tools.aqua.bgw.core.Color
import tools.aqua.bgw.core.DEFAULT_PROGRESSBAR_HEIGHT
import tools.aqua.bgw.core.DEFAULT_PROGRESSBAR_WIDTH
import tools.aqua.bgw.observable.properties.DoubleProperty
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * A [ProgressBar].
 *
 * @constructor Creates a [ProgressBar].
 *
 * @param posX Horizontal coordinate for this [ProgressBar]. Default: 0.
 * @param posY Vertical coordinate for this [ProgressBar]. Default: 0.
 * @param width Width for this [ProgressBar]. Default: [DEFAULT_PROGRESSBAR_WIDTH].
 * @param height Height for this [ProgressBar]. Default: [DEFAULT_PROGRESSBAR_HEIGHT].
 * @param visual [Visual] that is used to represent this [ProgressBar]. Default: empty [Visual].
 * @param progress The initial progress of this [ProgressBar]. Default 0.
 * @param barVisual The initial bar [ColorVisual] of this [ProgressBar]. Default [ColorVisual.CYAN].
 *
 * @see Visual
 * @see Color
 * @see UIComponent
 *
 * @since 0.1
 */
open class ProgressBar(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_PROGRESSBAR_WIDTH,
    height: Number = DEFAULT_PROGRESSBAR_HEIGHT,
    visual: Visual = Visual.EMPTY,
    progress: Double = 0.0,
    barVisual: ColorVisual = ColorVisual.CYAN
) :
    UIComponent(
        posX = posX, posY = posY, width = width, height = height, font = Font(), visual = visual) {

  /**
   * A [ProgressBar].
   *
   * @constructor Creates a [ProgressBar].
   *
   * @param posX Horizontal coordinate for this [ProgressBar]. Default: 0.
   * @param posY Vertical coordinate for this [ProgressBar]. Default: 0.
   * @param width Width for this [ProgressBar]. Default: [DEFAULT_PROGRESSBAR_WIDTH].
   * @param height Height for this [ProgressBar]. Default: [DEFAULT_PROGRESSBAR_HEIGHT].
   * @param visual [Visual] that is used to represent this [ProgressBar]. Default: empty [Visual].
   * @param progress The initial progress of this [ProgressBar]. Default 0.
   * @param barColor The initial bar color of this [ProgressBar]. Default [Color.CYAN]
   *
   * @see Visual
   * @see Color
   * @see UIComponent
   *
   * @since 0.1
   */
  constructor(
      posX: Number = 0,
      posY: Number = 0,
      width: Number = DEFAULT_PROGRESSBAR_WIDTH,
      height: Number = DEFAULT_PROGRESSBAR_HEIGHT,
      visual: Visual = Visual.EMPTY,
      progress: Double = 0.0,
      barColor: Color = Color.CYAN
  ) : this(posX, posY, width, height, visual, progress, ColorVisual(barColor))
  /**
   * [Property] for the progress state of this [ProgressBar].
   *
   * Should be in range of 0 to 1.
   *
   * A value between 0 and 1 represents the percentage of progress where 0 is 0% and 1 is 100%
   * progress. Any value less than 0 gets represented as 0% progress, while any value greater than 1
   * gets represented as 100% progress.
   *
   * @see progress
   */
  internal val progressProperty: DoubleProperty = DoubleProperty(progress)

  /**
   * Progress state of this [ProgressBar].
   *
   * Should be in range of 0 to 1.
   *
   * A value between 0 and 1 represents the percentage of progress where 0 is 0% and 1 is 100%
   * progress. Any value less than 0 gets represented as 0% progress, while any value greater than 1
   * gets represented as 100% progress.
   */
  var progress: Double
    get() = progressProperty.value
    set(value) {
      progressProperty.value = value
    }

  /**
   * [Property] for the bar [ColorVisual] of this [ProgressBar].
   *
   * @see barColor
   * @see barVisual
   */
  internal val barVisualProperty: Property<ColorVisual> = Property(barVisual)

  /** Bar [Color] of this [ProgressBar]. */
  var barColor: Color
    get() = barVisualProperty.value.color
    set(value) {
      barVisualProperty.value.color = value
    }

  /**
   * Bar [Visual] of this [ProgressBar].
   *
   * @see barColor
   *
   * @since 0.10
   */
  var barVisual: ColorVisual
    get() = barVisualProperty.value
    set(value) {
      barVisualProperty.value = value
    }

  init {
    progressProperty.internalListener = { _, newValue -> onProgressed?.invoke(newValue) }
  }

  /**
   * Gets invoked whenever this [ProgressBar]'s bar progress changes.
   *
   * @see progress
   *
   * @since 0.10
   */
  var onProgressed: ((Double) -> Unit)? = null
}
