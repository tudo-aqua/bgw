/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.components.container

import tools.aqua.bgw.components.gamecomponents.Card
import tools.aqua.bgw.components.gamecomponents.Card.Companion.DEFAULT_CARD_HEIGHT
import tools.aqua.bgw.components.gamecomponents.Card.Companion.DEFAULT_CARD_WIDTH
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.core.VerticalAlignment
import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.visual.Visual

/**
 * A CardStackView may be used to visualize a card stack.
 *
 * Visualization:
 * The [Visual] is used to visualize a background.
 * The positioning of contained [Card]s gets ignored and the specified alignment gets used to position them instead.
 *
 * @param height height for this [CardStack]. Default: the suggested card height.
 * @param width width for this [CardStack]. Default: the suggested card width.
 * @param posX horizontal coordinate for this [CardStack]. Default: 0.
 * @param posY vertical coordinate for this [CardStack]. Default: 0.
 * @param alignment specifies how the contained [Card]s should be aligned. Default: [Alignment.CENTER]
 * @param visual visual for this [CardStack]. Default: [Visual.EMPTY].
 *
 * @see Card
 */
open class CardStack<T : Card>(
	height: Number = DEFAULT_CARD_HEIGHT,
	width: Number = DEFAULT_CARD_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	alignment: Alignment = Alignment.CENTER,
	visual: Visual = Visual.EMPTY
) : GameComponentContainer<T>(height = height, width = width, posX = posX, posY = posY, visual = visual) {
	
	/**
	 * [Property] for the [Alignment] of [Card]s in this [CardStack].
	 */
	val alignmentProperty: ObjectProperty<Alignment> = ObjectProperty(alignment)
	
	/**
	 * [Alignment] of [Card]s in this [CardStack].
	 * @see alignmentProperty
	 */
	var alignment: Alignment
		get() = alignmentProperty.value
		set(value) {
			alignmentProperty.value = value
		}
	
	init {
		alignmentProperty.internalListener = { _, _ ->
			observableComponents.forEach { it.layoutX(); it.layoutY() }
		}
	}
	
	/**
	 * Pops the topmost [Card] from this [CardStack] and returns it,
	 * or null, if the stack is empty.
	 * Removes it from the [CardStack].
	 */
	fun popOrNull(): T? = observableComponents.removeLastOrNull()?.apply { removePosListeners(); parent = null }
	
	/**
	 * Pops the topmost [Card] from this [CardStack] and returns it.
	 * Removes it from the [CardStack].
	 *
	 * @throws NoSuchElementException if stack was empty.
	 */
	fun pop(): T = popOrNull() ?: throw NoSuchElementException()
	
	/**
	 * Returns the topmost [Card], or null, if the stack is empty.
	 * Does not modify the [CardStack].
	 */
	fun peekOrNull(): T? = observableComponents.lastOrNull()
	
	/**
	 * Returns the topmost [Card]. Does not modify the [CardStack].
	 *
	 * @throws NoSuchElementException if stack was empty.
	 */
	fun peek(): T = peekOrNull() ?: throw NoSuchElementException()
	
	/**
	 * Adds a [Card] on top of this [CardStack].
	 */
	fun push(cardView: T) {
		observableComponents.add(cardView)
		cardView.parent = this
		cardView.addPosListeners()
	}
	
	
	override fun add(component: T, index: Int) {
		super.add(component, index)
		component.addPosListeners()
	}
	
	override fun remove(component: T): Boolean = when (super.remove(component)) {
		true -> {
			component.removePosListeners(); true
		}
		false -> false
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
			}
		)
	}
	
	private fun T.layoutY() {
		posYProperty.setSilent(
			when (alignment.verticalAlignment) {
				VerticalAlignment.TOP -> 0.0
				VerticalAlignment.CENTER -> (this@CardStack.height - this.height) / 2
				VerticalAlignment.BOTTOM -> this@CardStack.height - this.height
			}
		)
	}
}