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

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test

/** Test push function in Stack. */
class PushTest : StackTestBase() {

  /** Tests push on empty stack. */
  @Test
  @DisplayName("Test push on empty Stack")
  fun testPushOnEmptyStack() {
    assertEquals(0, emptyStack.size)

    emptyStack.push(42)

    assertEquals(1, emptyStack.size)
    assertEquals(42, emptyStack.peek())
  }

  /** Tests push on filled stack. */
  @Test
  @DisplayName("Test push")
  fun testPush() {
    assertEquals(5, stack.size)

    stack.push(42)

    assertEquals(6, stack.size)
    assertEquals(42, stack.peek())
  }

  /** Tests push without elements. */
  @Test
  @DisplayName("Test pushAll without elements in varargs")
  fun testPushAllEmptyVarargs() {
    assertEquals(5, stack.size)

    stack.pushAll()

    assertEquals(5, stack.size)
  }

  /** Tests push with empty list of elements. */
  @Test
  @DisplayName("Test pushAll with empty list")
  fun testPushAllEmptyList() {
    assertEquals(5, stack.size)

    stack.pushAll(listOf())

    assertEquals(5, stack.size)
  }

  /** Tests push with one element in varargs. */
  @Test
  @DisplayName("Test pushAll with one element in varargs")
  fun testPushAllOneElementVarargs() {
    assertEquals(5, stack.size)

    stack.pushAll(42)

    assertEquals(6, stack.size)
    assertEquals(42, stack.peek())
  }

  /** Tests push with one element in list. */
  @Test
  @DisplayName("Test pushAll with one element in list")
  fun testPushAllOneElementList() {
    assertEquals(5, stack.size)

    stack.pushAll(listOf(42))

    assertEquals(6, stack.size)
    assertEquals(42, stack.peek())
  }

  /** Tests push with multiple elements in varargs. */
  @Test
  @DisplayName("Test pushAll with multiple elements in varargs")
  fun testPushAllMultipleElementsVarargs() {
    assertEquals(5, stack.size)

    stack.pushAll(42, 96, 666)

    assertEquals(8, stack.size)
    assertEquals(666, stack.pop())
    assertEquals(96, stack.pop())
    assertEquals(42, stack.pop())
  }

  /** Tests push with multiple elements in list. */
  @Test
  @DisplayName("Test pushAll with multiple elements in list")
  fun testPushAllMultipleElementsList() {
    assertEquals(5, stack.size)

    stack.pushAll(listOf(42, 96, 666))

    assertEquals(8, stack.size)
    assertEquals(666, stack.pop())
    assertEquals(96, stack.pop())
    assertEquals(42, stack.pop())
  }
}
