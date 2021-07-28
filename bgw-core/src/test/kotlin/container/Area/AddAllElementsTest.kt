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

package container.Area

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponents.GameToken
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
		gameTokenAreaContainer.addAll(listOf())
		assertTrue { gameTokenAreaContainer.components.isEmpty() }
	}
	
	@Test
	@DisplayName("Add a list containing two componentViews")
	fun addAllElementsNonEmptyList() {
		//add list with elements
		gameTokenAreaContainer.addAll(listOf(redGameToken, greenGameToken))
		assertContains(gameTokenAreaContainer.components, redGameToken)
		assertContains(gameTokenAreaContainer.components, greenGameToken)
	}
	
	@Test
	@DisplayName("Add a list containing two componentViews, where one is already contained")
	fun addAllElementsAlreadyContained() {
		//add list with one element already contained in tokenAreaContainer
		gameTokenAreaContainer.addAll(listOf(redGameToken, greenGameToken))
		assertThrows<IllegalArgumentException> { gameTokenAreaContainer.addAll(listOf(blueGameToken, redGameToken)) }
		assertContains(gameTokenAreaContainer.components, blueGameToken)
		//add list with one element already contained in another Container
		val cyanToken = GameToken(visual = ColorVisual(Color.CYAN))
		val otherAreaContainer = Area<GameToken>()
		otherAreaContainer.add(cyanToken)
		assertThrows<IllegalArgumentException> { gameTokenAreaContainer.addAll(listOf(cyanToken)) }
		assertFalse { gameTokenAreaContainer.components.contains(cyanToken) }
		assertContains(otherAreaContainer.components, cyanToken)
	}
	
	@Test
	@DisplayName("Add an empty list")
	fun addAllElementsVarArgsEmptyList() {
		gameTokenAreaContainer.addAll()
		assertTrue { gameTokenAreaContainer.components.isEmpty() }
	}
	
	@Test
	@DisplayName("Add a list containing two componentViews")
	fun addAllElementsVarArgsNonEmptyList() {
		//add list with elements
		gameTokenAreaContainer.addAll(listOf(redGameToken, greenGameToken))
		assertContains(gameTokenAreaContainer.components, redGameToken)
		assertContains(gameTokenAreaContainer.components, greenGameToken)
	}
	
	@Test
	@DisplayName("Add a list containing two componentViews, where one is already contained")
	fun addAllElementsVarArgsAlreadyContained() {
		//add list with one element already contained in tokenAreaContainer
		gameTokenAreaContainer.addAll(redGameToken, greenGameToken)
		assertThrows<IllegalArgumentException> { gameTokenAreaContainer.addAll(blueGameToken, redGameToken) }
		assertContains(gameTokenAreaContainer.components, blueGameToken)
		//add list with one element already contained in another Container
		val cyanToken = GameToken(visual = ColorVisual(Color.CYAN))
		val otherAreaContainer = Area<GameToken>()
		otherAreaContainer.add(cyanToken)
		assertThrows<IllegalArgumentException> { gameTokenAreaContainer.addAll(cyanToken) }
		assertFalse { gameTokenAreaContainer.components.contains(cyanToken) }
		assertContains(otherAreaContainer.components, cyanToken)
	}
}