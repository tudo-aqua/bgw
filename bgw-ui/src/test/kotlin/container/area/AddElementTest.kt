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

package container.area

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AddElementTest : AreaTestBase() {
    
    @Test
    @DisplayName("Add an element")
    fun addElement() {
        //simple add
        assertTrue(tokenViewArea.components.isEmpty())
        tokenViewArea.add(redTokenView)
        assertContains(tokenViewArea.components, redTokenView)
        assertEquals(tokenViewArea, redTokenView.parent)
    }

    @Test
    @DisplayName("Add an element that is already contained in this")
    fun addElementAlreadyContainedInThis() {
        //add already contained in this
        tokenViewArea.add(redTokenView)
        assertThrows<IllegalArgumentException> { tokenViewArea.add(redTokenView) }
    }

    @Test
    @DisplayName("Add an element that is already contained in another container")
    fun addElementAlreadyContainedInOther() {
        //add already contained in other container
        Area<TokenView>().add(blueTokenView)
        assertThrows<IllegalArgumentException> { tokenViewArea.add(blueTokenView) }
    }

    @Test
    @DisplayName("Add element with custom index")
    fun addElementWithIndex() {
        //add with index
        tokenViewArea.add(redTokenView, 0)
        //index out of bounds
        assertThrows<IllegalArgumentException> { tokenViewArea.add(blueTokenView, 2) }
        assertFalse { tokenViewArea.components.contains(blueTokenView) }
        assertThrows<IllegalArgumentException> { tokenViewArea.add(blueTokenView, -1) }
        assertFalse { tokenViewArea.components.contains(blueTokenView) }
        //add in between two elements
        tokenViewArea.add(blueTokenView, 1)
        assertEquals(listOf(redTokenView, blueTokenView), tokenViewArea.components)
        tokenViewArea.add(greenTokenView, 1)
        assertEquals(listOf(redTokenView, greenTokenView, blueTokenView), tokenViewArea.components)
    }
}