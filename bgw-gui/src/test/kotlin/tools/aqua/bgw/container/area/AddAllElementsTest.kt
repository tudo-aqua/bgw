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

package tools.aqua.bgw.container.area

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color
import kotlin.test.assertContains
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddAllElementsTest : AreaTestBase() {
	
	@Test
	@DisplayName("Add an empty list")
	fun addAllElementsEmptyList() {
		//add empty list
		tokenViewArea.addAll(listOf())
		assertTrue { tokenViewArea.components.isEmpty() }
	}
	
	@Test
	@DisplayName("Add a list containing two componentViews")
	fun addAllElementsNonEmptyList() {
		//add list with elements
		tokenViewArea.addAll(listOf(redTokenView, greenTokenView))
		assertContains(tokenViewArea.components, redTokenView)
		assertContains(tokenViewArea.components, greenTokenView)
	}
	
	@Test
	@DisplayName("Add a list containing two componentViews, where one is already contained")
	fun addAllElementsAlreadyContained() {
		//add list with one element already contained in tokenAreaContainer
		tokenViewArea.addAll(listOf(redTokenView, greenTokenView))
		assertThrows<IllegalArgumentException> { tokenViewArea.addAll(listOf(blueTokenView, redTokenView)) }
		assertContains(tokenViewArea.components, blueTokenView)
		//add list with one element already contained in another Container
		val cyanToken = TokenView(visual = ColorVisual(Color.CYAN))
		val otherAreaContainer = Area<TokenView>()
		otherAreaContainer.add(cyanToken)
		assertThrows<IllegalArgumentException> { tokenViewArea.addAll(listOf(cyanToken)) }
		assertFalse { tokenViewArea.components.contains(cyanToken) }
		assertContains(otherAreaContainer.components, cyanToken)
	}
	
	@Test
	@DisplayName("Add an empty list")
	fun addAllElementsVarArgsEmptyList() {
		tokenViewArea.addAll()
		assertTrue { tokenViewArea.components.isEmpty() }
	}
	
	@Test
	@DisplayName("Add a list containing two componentViews")
	fun addAllElementsVarArgsNonEmptyList() {
		//add list with elements
		tokenViewArea.addAll(listOf(redTokenView, greenTokenView))
		assertContains(tokenViewArea.components, redTokenView)
		assertContains(tokenViewArea.components, greenTokenView)
	}
	
	@Test
	@DisplayName("Add a list containing two componentViews, where one is already contained")
	fun addAllElementsVarArgsAlreadyContained() {
		//add list with one element already contained in tokenAreaContainer
		tokenViewArea.addAll(redTokenView, greenTokenView)
		assertThrows<IllegalArgumentException> { tokenViewArea.addAll(blueTokenView, redTokenView) }
		assertContains(tokenViewArea.components, blueTokenView)
		//add list with one element already contained in another Container
		val cyanToken = TokenView(visual = ColorVisual(Color.CYAN))
		val otherAreaContainer = Area<TokenView>()
		otherAreaContainer.add(cyanToken)
		assertThrows<IllegalArgumentException> { tokenViewArea.addAll(cyanToken) }
		assertFalse { tokenViewArea.components.contains(cyanToken) }
		assertContains(otherAreaContainer.components, cyanToken)
	}
}