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

package tools.aqua.bgw.elements.container

import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.HorizontalAlignment
import tools.aqua.bgw.core.VerticalAlignment
import tools.aqua.bgw.elements.gameelements.CardView
import tools.aqua.bgw.elements.gameelements.CardView.Companion.DEFAULT_CARD_HEIGHT
import tools.aqua.bgw.elements.gameelements.CardView.Companion.DEFAULT_CARD_WIDTH
import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.visual.Visual

/**
 * A CardStackView may be used to visualize a card stack.
 *
 * Visualization:
 * The [Visual] is used to visualize a background.
 * The positioning of contained [CardView]s gets ignored and the specified alignment gets used to position them instead.
 *
 * @param height height for this [CardStackView]. Default: the suggested card height.
 * @param width width for this [CardStackView]. Default: the suggested card width.
 * @param posX horizontal coordinate for this [CardStackView]. Default: 0.
 * @param posY vertical coordinate for this [CardStackView]. Default: 0.
 * @param alignment specifies how the contained [CardView]s should be aligned. Default: [Alignment.CENTER]
 * @param visual visual for this [CardStackView]. Default: [Visual.EMPTY].
 *
 * @see CardView
 */
open class CardStackView<T : CardView>(
	height: Number = DEFAULT_CARD_HEIGHT,
	width: Number = DEFAULT_CARD_WIDTH,
	posX: Number = 0,
	posY: Number = 0,
	alignment: Alignment = Alignment.CENTER,
	visual: Visual = Visual.EMPTY
) : GameElementContainerView<T>(height = height, width = width, posX = posX, posY = posY, visual = visual) {
	
	/**
	 * [Property] for the [Alignment] of [CardView]s in this [CardStackView].
	 */
	val alignmentProperty: ObjectProperty<Alignment> = ObjectProperty(alignment)
	
	/**
	 * [Alignment] of [CardView]s in this [CardStackView].
	 * @see alignmentProperty
	 */
	var alignment: Alignment
		get() = alignmentProperty.value
		set(value) {
			alignmentProperty.value = value
		}
	
	init {
		alignmentProperty.internalListener = { _, _ ->
			observableElements.forEach { it.layoutX(); it.layoutY() }
		}
	}

	/**
	 * Pops the topmost [CardView] from this [CardStackView] and returns it,
	 * or null, if the stack is empty.
	 * Removes it from the [CardStackView].
	 */
	fun popOrNull(): T? = observableElements.removeLastOrNull()?.apply { removePosListeners(); parent = null }
	
	/**
	 * Pops the topmost [CardView] from this [CardStackView] and returns it.
	 * Removes it from the [CardStackView].
	 *
	 * @throws NoSuchElementException if stack was empty.
	 */
	fun pop(): T = popOrNull() ?: throw NoSuchElementException()
	
	/**
	 * Returns the topmost [CardView], or null, if the stack is empty.
	 * Does not modify the [CardStackView].
	 */
	fun peekOrNull(): T? = observableElements.lastOrNull()
	
	/**
	 * Returns the topmost [CardView]. Does not modify the [CardStackView].
	 *
	 * @throws NoSuchElementException if stack was empty.
	 */
	fun peek(): T = peekOrNull() ?: throw NoSuchElementException()
	
	/**
	 * Adds a [CardView] on top of this [CardStackView].
	 */
	fun push(cardView: T) {
		observableElements.add(cardView)
		cardView.parent = this
		cardView.addPosListeners()
	}
	
	
	override fun add(element: T, index: Int) {
		super.add(element, index)
		element.addPosListeners()
	}
	
	override fun addAll(collection: Collection<T>) {
		super.addAll(collection)
		collection.forEach { it.addPosListeners() }
	}
	
	override fun addAll(vararg elements: T) {
		addAll(elements.toList())
	}
	
	override fun remove(element: T) : Boolean = when (super.remove(element)) {
			true -> { element.removePosListeners(); true }
			false -> false
		}
	
	override fun clear(): List<T> {
		return super.clear().onEach {
			it.removePosListeners()
		}
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
				HorizontalAlignment.CENTER -> (this@CardStackView.width - this.width) / 2
				HorizontalAlignment.RIGHT -> this@CardStackView.width - this.width
			}
		)
	}
	
	private fun T.layoutY() {
		posYProperty.setSilent(
			when (alignment.verticalAlignment) {
				VerticalAlignment.TOP -> 0.0
				VerticalAlignment.CENTER -> (this@CardStackView.height - this.height) / 2
				VerticalAlignment.BOTTOM -> this@CardStackView.height - this.height
			}
		)
	}
}