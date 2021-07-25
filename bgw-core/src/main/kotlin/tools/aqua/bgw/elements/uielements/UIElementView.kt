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

@file:Suppress("unused")

package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.elements.gameelements.GameElementView
import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.Property
import tools.aqua.bgw.observable.StringProperty
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for all UI elements.
 *
 * @param height height for this [UIElementView].
 * @param width width for this [UIElementView].
 * @param posX horizontal coordinate for this [UIElementView].
 * @param posY vertical coordinate for this [UIElementView].
 * @param font font for this [UIElementView]. Usage depends on subclass.
 */
sealed class UIElementView(
	height: Number,
	width: Number,
	posX: Number,
	posY: Number,
	font: Font,
) : StaticView<UIElementView>(height = height, width = width, posX = posX, posY = posY, visual = Visual.EMPTY) {
	
	/**
	 * [Property] for the [Font] of this [LabeledUIElementView].
	 * @see Font
	 */
	val fontProperty: ObjectProperty<Font> = ObjectProperty(font)
	
	/**
	 * [Font] of this [LabeledUIElementView].
	 * @see Font
	 * @see fontProperty
	 */
	var font: Font
		get() = fontProperty.value
		set(value) {
			fontProperty.value = value
		}
	
	/**
	 * [Property] for the css style that gets applied to this [UIElementView]'s background.
	 * This gets applied last so it may override any changes made via other fields and functions of this
	 * [UIElementView]. Critical failures, bugs or other undefined behaviour could occur when using this feature.
	 */
	val backgroundStyleProperty: StringProperty = StringProperty("")
	
	/**
	 * Css style that gets applied to this [UIElementView]'s background.
	 * This gets applied last so it may override any changes made via other fields and functions of this
	 * [UIElementView]. Critical failures, bugs or other undefined behaviour could occur when using this feature.
	 * @see backgroundStyleProperty
	 */
	var backgroundStyle: String
		get() = backgroundStyleProperty.value
		set(value) {
			backgroundStyleProperty.value = value
		}
	
	/**
	 * [Property] for the css style that gets applied to this [UIElementView].
	 * This gets applied last so it may override any changes made via other fields and functions of this
	 * [UIElementView]. Critical failures, bugs or other undefined behaviour could occur when using this feature.
	 */
	val componentStyleProperty: StringProperty = StringProperty("")
	
	/**
	 * Css style that gets applied to this [UIElementView].
	 * This gets applied last so it may override any changes made via other fields and functions of this
	 * [UIElementView]. Critical failures, bugs or other undefined behaviour could occur when using this feature.
	 * @see componentStyleProperty
	 */
	var componentStyle: String
		get() = componentStyleProperty.value
		set(value) {
			componentStyleProperty.value = value
		}
	
	/**
	 * @throws RuntimeException if [GameElementView] does not support children.
	 */
	override fun removeChild(child: ElementView) {
		throw RuntimeException("This $this Element has no children.")
	}
}