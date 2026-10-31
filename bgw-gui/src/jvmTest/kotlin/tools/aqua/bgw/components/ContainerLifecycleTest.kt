/*
 * Copyright 2021-2026 The BoardGameWork Authors
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

package tools.aqua.bgw.components

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.visual.Visual

/** Regression tests for container ownership and lifecycle handling. */
class ContainerLifecycleTest {

  @Test
  fun `card stack push and pop use normal lifecycle callbacks`() {
    val stack = CardStack<CardView>()
    val card = CardView(front = Visual.EMPTY)
    var added = 0
    var removed = 0
    stack.onAdd = { added++ }
    stack.onRemove = { removed++ }

    stack.push(card)
    assertSame(stack, card.parent)
    assertEquals(1, added)

    assertSame(card, stack.pop())
    assertNull(card.parent)
    assertEquals(1, removed)
  }

  @Test
  fun `satchel restores state and releases position listeners on removal`() {
    val satchel = Satchel<TokenView>(width = 80, height = 90)
    val token =
        TokenView(posX = 12, posY = 34, width = 20, height = 30, visual = Visual.EMPTY).apply {
          opacity = 0.6
          isDraggable = false
        }

    satchel.add(token)
    assertEquals(0.0, token.posX)
    assertEquals(0.0, token.posY)
    assertEquals(80.0, token.width)
    assertEquals(90.0, token.height)
    assertEquals(0.0, token.opacity)
    assertTrue(token.isDraggable)

    satchel.remove(token)
    assertEquals(12.0, token.posX)
    assertEquals(34.0, token.posY)
    assertEquals(20.0, token.width)
    assertEquals(30.0, token.height)
    assertEquals(0.6, token.opacity)
    assertFalse(token.isDraggable)

    token.posX = 55.0
    token.posY = 66.0
    assertEquals(55.0, token.posX)
    assertEquals(66.0, token.posY)
  }

  @Test
  fun `bulk pane add validates every component before mutating`() {
    val firstPane = Pane<TokenView>(width = 100, height = 100)
    val secondPane = Pane<TokenView>(width = 100, height = 100)
    val free = TokenView(visual = Visual.EMPTY)
    val occupied = TokenView(visual = Visual.EMPTY)
    firstPane.add(occupied)

    assertThrows(IllegalArgumentException::class.java) { secondPane.addAll(free, occupied) }

    assertTrue(secondPane.isEmpty())
    assertNull(free.parent)
    assertSame(firstPane, occupied.parent)
  }
}
