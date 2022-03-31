/*
 * Copyright 2021-2022 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.util.stack

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

/** Test pop function in Stack. */
class PopTest : StackTestBase() {

  /** Tests pop on empty stack. */
  @Test
  @DisplayName("Test pop on empty Stack")
  fun testPopOnEmptyStack() {
    assertEquals(0, emptyStack.size)
    assertThrows<NoSuchElementException> { emptyStack.pop() }
  }

  /** Tests popOrNull on empty stack. */
  @Test
  @DisplayName("Test popOrNull on empty Stack")
  fun testPopOrNullOnEmptyStack() {
    assertEquals(0, emptyStack.size)
    assertEquals(null, emptyStack.popOrNull())
  }

  /** Tests pop on filled stack. */
  @Test
  @DisplayName("Test pop")
  fun testPop() {
    assertEquals(5, stack.size)
    assertEquals(order[0], stack.pop())
    assertEquals(4, stack.size)
    assertEquals(order[1], stack.pop())
    assertEquals(3, stack.size)
    assertEquals(order[2], stack.pop())
    assertEquals(2, stack.size)
    assertEquals(order[3], stack.pop())
    assertEquals(1, stack.size)
    assertEquals(order[4], stack.pop())
    assertEquals(0, stack.size)
    assertThrows<NoSuchElementException> { stack.pop() }
  }

  /** Tests popOrNull on filled stack. */
  @Test
  @DisplayName("Test popOrNull")
  fun testPopOrNull() {
    assertEquals(5, stack.size)
    assertEquals(order[0], stack.popOrNull())
    assertEquals(4, stack.size)
    assertEquals(order[1], stack.popOrNull())
    assertEquals(3, stack.size)
    assertEquals(order[2], stack.popOrNull())
    assertEquals(2, stack.size)
    assertEquals(order[3], stack.popOrNull())
    assertEquals(1, stack.size)
    assertEquals(order[4], stack.popOrNull())
    assertEquals(0, stack.size)
    assertEquals(null, stack.popOrNull())
  }

  /** Tests popAll on empty stack. */
  @Test
  @DisplayName("Test popAll on empty stack")
  fun testPopAllOnEmptyStack() {
    assertEquals(0, emptyStack.size)
    assertEquals(0, emptyStack.popAll().size)
  }

  /** Tests popAll on filled stack. */
  @Test
  @DisplayName("Test popAll for all elements")
  fun testPopAllForAll() {
    assertEquals(5, stack.size)

    assertEquals(order, stack.popAll(order.size))

    assertEquals(0, stack.size)
  }

  /** Tests popAll for subset on filled stack. */
  @Test
  @DisplayName("Test popAll for some elements")
  fun testPopAllForSome() {
    assertEquals(5, stack.size)

    assertEquals(order.subList(0, 3), stack.popAll(3))

    assertEquals(2, stack.size)
  }

  /** Tests popAll for one element on filled stack. */
  @Test
  @DisplayName("Test popAll for one element")
  fun testPopAllForOne() {
    assertEquals(5, stack.size)

    assertEquals(listOf(order[0]), stack.popAll(1))

    assertEquals(4, stack.size)
  }

  /** Tests popAll for zero elements on filled stack. */
  @Test
  @DisplayName("Test popAll for zero argument")
  fun testPopAllForZero() {
    assertEquals(5, stack.size)

    assertThat(stack.popAll(0)).isEmpty()

    assertEquals(5, stack.size)
  }

  /** Tests popAll for a negative count on filled stack. */
  @Test
  @DisplayName("Test popAll for negative argument")
  fun testPopAllForNegative() {
    assertEquals(5, stack.size)

    assertThrows<IllegalArgumentException> { stack.popAll(-1) }

    assertEquals(5, stack.size)
  }

  /** Tests popAll with larger amount than existing on filled stack. */
  @Test
  @DisplayName("Test popAll for large argument")
  fun testPopAllForLarge() {
    assertEquals(5, stack.size)

    assertThrows<IllegalArgumentException> { stack.popAll(6) }

    assertEquals(5, stack.size)
  }
}
