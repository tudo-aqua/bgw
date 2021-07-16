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

package container.AreaContainerView

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.elements.container.AreaContainerView
import tools.aqua.bgw.elements.gameelements.TokenView
import java.lang.IllegalArgumentException
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddElementTest: AreaContainerViewTestBase() {

    @Test
    @DisplayName("Add an element")
    fun addElement() {
        //simple add
        assertTrue(tokenAreaContainer.elements.isEmpty())
        tokenAreaContainer.add(redToken)
        assertContains(tokenAreaContainer.elements, redToken)
        assertEquals(tokenAreaContainer, redToken.parent)
    }

    @Test
    @DisplayName("Add an element that is already contained in this")
    fun addElementAlreadyContainedInThis() {
        //add already contained in this
        tokenAreaContainer.add(redToken)
        assertThrows<IllegalArgumentException> { tokenAreaContainer.add(redToken) }
    }

    @Test
    @DisplayName("Add an element that is already contained in another container")
    fun addElementAlreadyContainedInOther() {
        //add already contained in other container
        AreaContainerView<TokenView>().add(blueToken)
        assertThrows<IllegalArgumentException> { tokenAreaContainer.add(blueToken) }
    }

    @Test
    @DisplayName("Add element with custom index")
    fun addElementWithIndex() {
        //add with index
        tokenAreaContainer.add(redToken, 0)
        //index out of bounds
        assertThrows<IllegalArgumentException> { tokenAreaContainer.add(blueToken, 2) }
        assertFalse { tokenAreaContainer.elements.contains(blueToken) }
        assertThrows<IllegalArgumentException> { tokenAreaContainer.add(blueToken, -1) }
        assertFalse { tokenAreaContainer.elements.contains(blueToken) }
        //add in between two elements
        tokenAreaContainer.add(blueToken, 1)
        assertEquals(listOf(redToken, blueToken), tokenAreaContainer.elements)
        tokenAreaContainer.add(greenToken, 1)
        assertEquals(listOf(redToken, greenToken, blueToken), tokenAreaContainer.elements)
    }
}