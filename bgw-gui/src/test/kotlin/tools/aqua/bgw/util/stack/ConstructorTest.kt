/*
 * Copyright 2021-2024 The BoardGameWork Authors
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
import tools.aqua.bgw.util.Stack

/** Test peek function in Stack. */
class ConstructorTest : StackTestBase() {

  /** Test primary constructor with elements. */
  @Test
  @DisplayName("Test primary constructor with elements")
  fun testPrimaryConstructor() {
    val stack = Stack(listOf(1, 2, 3))
    assertEquals(3, stack.size)
    assertEquals(3, stack.pop())
    assertEquals(2, stack.pop())
    assertEquals(1, stack.pop())
  }

  /** Test primary constructor without elements. */
  @Test
  @DisplayName("Test primary constructor without elements")
  fun testPrimaryConstructorEmpty() {
    val stack = Stack(listOf<Int>())
    assertEquals(0, stack.size)
  }

  /** Test secondary constructor with elements. */
  @Test
  @DisplayName("Test secondary constructor with elements")
  fun testSecondaryConstructor() {
    val stack = Stack(1, 2, 3)
    assertEquals(3, stack.size)
    assertEquals(3, stack.pop())
    assertEquals(2, stack.pop())
    assertEquals(1, stack.pop())
  }

  /** Test secondary constructor without elements. */
  @Test
  @DisplayName("Test secondary constructor without elements")
  fun testSecondaryConstructorEmpty() {
    val stack = Stack<Int>()
    assertEquals(0, stack.size)
  }
}
