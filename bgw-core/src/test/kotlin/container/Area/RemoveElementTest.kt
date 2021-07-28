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
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RemoveElementTest : AreaTestBase() {
    
    @Test
    @DisplayName("Removes an element")
    fun removeElement() {
        gameTokenAreaContainer.add(redGameToken)
        gameTokenAreaContainer.add(blueGameToken)
        gameTokenAreaContainer.add(greenGameToken)
        gameTokenAreaContainer.remove(redGameToken)
        assertEquals(listOf(blueGameToken, greenGameToken), gameTokenAreaContainer.components)
        assertNull(redGameToken.parent)
        gameTokenAreaContainer.remove(redGameToken)
        assertEquals(listOf(blueGameToken, greenGameToken), gameTokenAreaContainer.components)
    }

    @Test
    @DisplayName("Remove all Elements")
    fun removeAllElements() {
        gameTokenAreaContainer.add(redGameToken)
        gameTokenAreaContainer.add(blueGameToken)
        val result = gameTokenAreaContainer.clear()
        assertEquals(listOf(redGameToken, blueGameToken), result)
        assertNull(redGameToken.parent)
        assertNull(blueGameToken.parent)
    }
}