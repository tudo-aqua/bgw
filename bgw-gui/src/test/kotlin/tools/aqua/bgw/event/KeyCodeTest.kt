/*
 * Copyright 2022 The BoardGameWork Authors
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

package tools.aqua.bgw.event

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.event.KeyCode.*

/** Tests for [KeyCode]. */
class KeyCodeTest {

  /** Tests toString. */
  @Test
  @DisplayName("Test toString")
  fun testToStringEvent() {
    KeyCode.values().forEach { assertEquals(it.string, it.toString()) }
  }

  /** Tests isModifier. */
  @Test
  @DisplayName("Test isModifier")
  fun testIsModifier() {
    val modifiers = listOf(SHIFT, CONTROL, ALT, ALT_GRAPH, WINDOWS)

    KeyCode.values().forEach { assertEquals(it in modifiers, it.isModifier()) }
  }

  /** Tests isLetter. */
  @Test
  @DisplayName("Test isLetter")
  fun testIsLetter() {
    val letters =
        listOf(A, B, C, D, E, F, G, H, I, J, K, L, M, N, O, P, Q, R, S, T, U, V, W, X, Y, Z)

    KeyCode.values().forEach { assertEquals(it in letters, it.isLetter()) }
  }

  /** Tests isDigit. */
  @Test
  @DisplayName("Test isDigit")
  fun testIsDigit() {
    val digits =
        listOf(
            DIGIT1,
            DIGIT2,
            DIGIT3,
            DIGIT4,
            DIGIT5,
            DIGIT6,
            DIGIT7,
            DIGIT8,
            DIGIT9,
            DIGIT0,
            NUMPAD0,
            NUMPAD1,
            NUMPAD2,
            NUMPAD3,
            NUMPAD4,
            NUMPAD5,
            NUMPAD6,
            NUMPAD7,
            NUMPAD8,
            NUMPAD9)

    KeyCode.values().forEach { assertEquals(it in digits, it.isDigit()) }
  }

  /** Tests isOnNumpad. */
  @Test
  @DisplayName("Test isOnNumpad")
  fun testIsOnNumpad() {
    val onNumpad =
        listOf(
            NUMPAD0,
            NUMPAD1,
            NUMPAD2,
            NUMPAD3,
            NUMPAD4,
            NUMPAD5,
            NUMPAD6,
            NUMPAD7,
            NUMPAD8,
            NUMPAD9,
            NUM_LOCK,
            DIVIDE,
            MULTIPLY,
            SUBTRACT,
            ADD,
            DECIMAL)

    KeyCode.values().forEach { assertEquals(it in onNumpad, it.isOnNumpad()) }
  }

  /** Tests isArrow. */
  @Test
  @DisplayName("Test isArrow")
  fun testIsArrow() {
    val arrows = listOf(LEFT, UP, RIGHT, DOWN)

    KeyCode.values().forEach { assertEquals(it in arrows, it.isArrow()) }
  }

  /** Tests isNavigation. */
  @Test
  @DisplayName("Test isNavigation")
  fun testIsNavigation() {
    val navigation = listOf(PAGE_UP, PAGE_DOWN, LEFT, UP, RIGHT, DOWN)

    KeyCode.values().forEach { assertEquals(it in navigation, it.isNavigation()) }
  }

  /** Tests isWhitespace. */
  @Test
  @DisplayName("Test isWhitespace")
  fun testIsWhitespace() {
    val whitespaces = listOf(SPACE, TAB)

    KeyCode.values().forEach { assertEquals(it in whitespaces, it.isWhitespace()) }
  }

  /** Tests isFunction. */
  @Test
  @DisplayName("Test isFunction")
  fun testIsFunction() {
    val functions = listOf(F1, F2, F3, F4, F5, F6, F7, F8, F9, F10, F11, F12)

    KeyCode.values().forEach { assertEquals(it in functions, it.isFunction()) }
  }

  /** Tests KeyType. */
  @Test
  @DisplayName("Test KeyType")
  fun testKeyType() {
    KeyType.apply {
      assertEquals(0b10000000, MODIFIER)
      assertEquals(0b01000000, LETTER)
      assertEquals(0b00100000, DIGIT)
      assertEquals(0b00010000, NUMPAD)
      assertEquals(0b00001000, ARROW)
      assertEquals(0b00000100, NAVIGATION)
      assertEquals(0b00000010, WHITESPACE)
      assertEquals(0b00000001, FUNCTION)
      assertEquals(0b00000000, OTHER)
    }
  }
}
