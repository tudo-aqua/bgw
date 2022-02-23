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

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.util.Stack

/** Test base for Stack tests. */
open class StackTestBase {
  /** initial order of elements in stack. */
  protected val order: List<Int> = listOf(3, 2, 5, 4, 1)

  /** Stack filled with given order. */
  protected lateinit var stack: Stack<Int>
  
  /** An empty stack. */
  protected lateinit var emptyStack: Stack<Int>

  /** Fills stacks with given orders before tests. */
  @BeforeEach
  fun setUp() {
    stack = Stack(order.reversed())
    emptyStack = Stack()
  }
}
