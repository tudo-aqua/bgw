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
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddElementTest : AreaTestBase() {
    
    @Test
    @DisplayName("Add an element")
    fun addElement() {
        //simple add
        assertTrue(gameTokenAreaContainer.components.isEmpty())
        gameTokenAreaContainer.add(redGameToken)
        assertContains(gameTokenAreaContainer.components, redGameToken)
        assertEquals(gameTokenAreaContainer, redGameToken.parent)
    }

    @Test
    @DisplayName("Add an element that is already contained in this")
    fun addElementAlreadyContainedInThis() {
        //add already contained in this
        gameTokenAreaContainer.add(redGameToken)
        assertThrows<IllegalArgumentException> { gameTokenAreaContainer.add(redGameToken) }
    }

    @Test
    @DisplayName("Add an element that is already contained in another container")
    fun addElementAlreadyContainedInOther() {
        //add already contained in other container
        Area<GameToken>().add(blueGameToken)
        assertThrows<IllegalArgumentException> { gameTokenAreaContainer.add(blueGameToken) }
    }

    @Test
    @DisplayName("Add element with custom index")
    fun addElementWithIndex() {
        //add with index
        gameTokenAreaContainer.add(redGameToken, 0)
        //index out of bounds
        assertThrows<IllegalArgumentException> { gameTokenAreaContainer.add(blueGameToken, 2) }
        assertFalse { gameTokenAreaContainer.components.contains(blueGameToken) }
        assertThrows<IllegalArgumentException> { gameTokenAreaContainer.add(blueGameToken, -1) }
        assertFalse { gameTokenAreaContainer.components.contains(blueGameToken) }
        //add in between two elements
        gameTokenAreaContainer.add(blueGameToken, 1)
        assertEquals(listOf(redGameToken, blueGameToken), gameTokenAreaContainer.components)
        gameTokenAreaContainer.add(greenGameToken, 1)
        assertEquals(listOf(redGameToken, greenGameToken, blueGameToken), gameTokenAreaContainer.components)
    }
}