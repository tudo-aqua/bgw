/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.core.*
import tools.aqua.bgw.observable.properties.Property
import tools.aqua.bgw.visual.Visual

/**
 * A CardStackView may be used to visualize a card stack.
 *
 * Visualization:
 *
 * The [Visual] is used to visualize a background.
 *
 * The positioning of contained [CardView]s gets ignored and the specified alignment gets used to
 * position them instead.
 *
 * @constructor Creates a [CardStack].
 *
 * @param posX Horizontal coordinate for this [CardStack]. Default: 0.
 * @param posY Vertical coordinate for this [CardStack]. Default: 0.
 * @param width Width for this [CardStack]. Default: [DEFAULT_CARD_STACK_WIDTH].
 * @param height Height for this [CardStack]. Default: [DEFAULT_CARD_STACK_HEIGHT].
 * @param alignment Specifies how the contained [CardView]s should be aligned. Default:
 * [Alignment.CENTER]
 * @param visual Visual for this [CardStack]. Default: [Visual.EMPTY].
 *
 * @see CardView
 */
open class CardStack<T : CardView>(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_CARD_STACK_WIDTH,
    height: Number = DEFAULT_CARD_STACK_HEIGHT,
    alignment: Alignment = Alignment.CENTER,
    visual: Visual = Visual.EMPTY
) :
    GameComponentContainer<T>(
        posX = posX, posY = posY, width = width, height = height, visual = visual) {

  /**
   * [Property] for the [Alignment] of [CardView]s in this [CardStack].
   *
   * @see alignment
   */
  val alignmentProperty: Property<Alignment> = Property(alignment)

  /**
   * [Alignment] of [CardView]s in this [CardStack].
   *
   * @see alignmentProperty
   */
  var alignment: Alignment
    get() = alignmentProperty.value
    set(value) {
      alignmentProperty.value = value
    }

  init {
    alignmentProperty.internalListener =
        { _, _ ->
          observableComponents.forEach {
            it.layoutX()
            it.layoutY()
          }
        }
  }

  /**
   * Pops the topmost [CardView] from this [CardStack] and returns it, or null, if the stack is
   * empty. Removes it from the [CardStack].
   */
  fun popOrNull(): T? =
      observableComponents.removeLastOrNull()?.apply {
        removePosListeners()
        parent = null
      }

  /**
   * Pops the topmost [CardView] from this [CardStack] and returns it. Removes it from the
   * [CardStack].
   *
   * @throws NoSuchElementException if stack was empty.
   */
  fun pop(): T = popOrNull() ?: throw NoSuchElementException()

  /**
   * Returns the topmost [CardView], or null, if the stack is empty. Does not modify the [CardStack]
   * .
   */
  fun peekOrNull(): T? = observableComponents.lastOrNull()

  /**
   * Returns the topmost [CardView]. Does not modify the [CardStack].
   *
   * @throws NoSuchElementException if stack was empty.
   */
  fun peek(): T = peekOrNull() ?: throw NoSuchElementException()

  /** Adds a [CardView] on top of this [CardStack]. */
  fun push(cardView: T) {
    observableComponents.add(cardView)
    cardView.parent = this
    cardView.addPosListeners()
  }

  override fun T.onAdd() {
    addPosListeners()
  }

  override fun T.onRemove() {
    removePosListeners()
  }

  private fun T.addPosListeners() {
    posXProperty.setInternalListenerAndInvoke(0.0) { _, _ -> layoutX() }
    posYProperty.setInternalListenerAndInvoke(0.0) { _, _ -> layoutY() }
  }

  private fun T.removePosListeners() {
    posXProperty.internalListener = null
    posYProperty.internalListener = null
  }

  private fun T.layoutX() {

    posXProperty.setSilent(
        when (alignment.horizontalAlignment) {
          HorizontalAlignment.LEFT -> 0.0
          HorizontalAlignment.CENTER -> (this@CardStack.width - this.width) / 2
          HorizontalAlignment.RIGHT -> this@CardStack.width - this.width
        })
  }

  private fun T.layoutY() {
    posYProperty.setSilent(
        when (alignment.verticalAlignment) {
          VerticalAlignment.TOP -> 0.0
          VerticalAlignment.CENTER -> (this@CardStack.height - this.height) / 2
          VerticalAlignment.BOTTOM -> this@CardStack.height - this.height
        })
  }
}
