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

package tools.aqua.bgw.elements

import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.visual.Visual

/**
 * The root element in the view hierarchy of a [Scene].
 *
 * @param scene scene of this root element.
 */
internal class RootElement<T : ElementView>(val scene: Scene<T>) : ElementView(0, 0, 0, 0, Visual.EMPTY) {
	
	/**
	 * Removes element from the [scene].
	 *
	 * @param child child to be removed.
	 *
	 * @throws RuntimeException if the child's type is incompatible with scene's type.
	 */
	override fun removeChild(child: ElementView) {
		try {
			@Suppress("UNCHECKED_CAST")
			this.scene.removeElements(child as T)
		} catch (_: ClassCastException) {
			throw RuntimeException("$child type is incompatible with scene's type.")
		}
	}
}