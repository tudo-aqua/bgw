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

@file:Suppress("MemberVisibilityCanBePrivate", "TooManyFunctions")

package tools.aqua.bgw.builder

import java.util.*
import javafx.geometry.Pos
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType as FXButtonType
import javafx.scene.input.KeyCode as FXKeyCode
import javafx.scene.input.KeyEvent as FXKeyEvent
import javafx.scene.input.MouseButton
import javafx.scene.input.MouseEvent as FXMouseEvent
import javafx.scene.paint.Color
import tools.aqua.bgw.components.uicomponents.Orientation
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.dialog.ButtonType
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.KeyEvent
import tools.aqua.bgw.event.MouseButtonType
import tools.aqua.bgw.event.MouseEvent
import tools.aqua.bgw.util.Font

/** Helper class for conversion functions between BGW and JavaFX and backwards. */
object FXConverters {
  /** Converts the [java.awt.Color] to [Color]. */
  internal fun java.awt.Color.toFXColor(): Color =
      Color(
          red / VisualBuilder.MAX_HEX,
          green / VisualBuilder.MAX_HEX,
          blue / VisualBuilder.MAX_HEX,
          alpha / VisualBuilder.MAX_HEX,
      )

  /** Converts the BGW [Font] to [javafx.scene.text.Font]. */
  internal fun Font.toFXFontCSS(): String {
    val weight =
        when (fontWeight) {
          Font.FontWeight.LIGHT -> "lighter"
          Font.FontWeight.NORMAL -> "normal"
          Font.FontWeight.SEMI_BOLD -> "bolder"
          Font.FontWeight.BOLD -> "bold"
        }
    val style =
        when (fontStyle) {
          Font.FontStyle.NORMAL -> "normal"
          Font.FontStyle.ITALIC -> "italic"
          Font.FontStyle.OBLIQUE -> "oblique"
        }
    return "-fx-font-size: ${size}px;" +
        "-fx-font-family: '$family';" +
        "-fx-font-weight: $weight;" +
        "-fx-font-style: $style;" +
        "-fx-text-fill: " +
        String.format(
            locale = Locale.getDefault(),
            format = "#%02x%02x%02x%02x;",
            color.red,
            color.green,
            color.blue,
            color.alpha)
  }

  /** Converts the [FXMouseEvent] to [MouseEvent]. */
  internal fun FXMouseEvent.toMouseEvent(): MouseEvent =
      MouseEvent(
          when (button) {
            MouseButton.PRIMARY -> MouseButtonType.LEFT_BUTTON
            MouseButton.SECONDARY -> MouseButtonType.RIGHT_BUTTON
            MouseButton.MIDDLE -> MouseButtonType.MOUSE_WHEEL
            else -> MouseButtonType.UNSPECIFIED
          })

  /** Converts the [javafx.scene.input.KeyEvent] to [KeyEvent]. */
  internal fun FXKeyEvent.toKeyEvent(): KeyEvent =
      KeyEvent(
          keyCode = code.toKeyCode(),
          character = character,
          shiftDown = isShiftDown,
          altDown = isAltDown,
          controlDown = isControlDown)

  /** Converts the [javafx.scene.input.KeyCode] to [KeyCode]. */
  @Suppress("LongMethod")
  internal fun FXKeyCode.toKeyCode(): KeyCode =
      when (this) {
        FXKeyCode.SHIFT -> KeyCode.SHIFT
        FXKeyCode.CONTROL -> KeyCode.CONTROL
        FXKeyCode.ALT -> KeyCode.ALT
        FXKeyCode.ALT_GRAPH -> KeyCode.ALT_GRAPH
        FXKeyCode.WINDOWS -> KeyCode.WINDOWS
        FXKeyCode.CONTEXT_MENU -> KeyCode.CONTEXT_MENU
        FXKeyCode.TAB -> KeyCode.TAB
        FXKeyCode.CAPS -> KeyCode.CAPS
        FXKeyCode.ENTER -> KeyCode.ENTER
        FXKeyCode.SPACE -> KeyCode.SPACE
        FXKeyCode.BACK_SPACE -> KeyCode.BACK_SPACE
        FXKeyCode.PAUSE -> KeyCode.PAUSE
        FXKeyCode.SCROLL_LOCK -> KeyCode.SCROLL_LOCK
        FXKeyCode.ESCAPE -> KeyCode.ESCAPE
        FXKeyCode.DELETE -> KeyCode.DELETE
        FXKeyCode.HOME -> KeyCode.HOME_POS1
        FXKeyCode.END -> KeyCode.END
        FXKeyCode.PAGE_UP -> KeyCode.PAGE_UP
        FXKeyCode.PAGE_DOWN -> KeyCode.PAGE_DOWN
        FXKeyCode.LEFT -> KeyCode.LEFT
        FXKeyCode.UP -> KeyCode.UP
        FXKeyCode.RIGHT -> KeyCode.RIGHT
        FXKeyCode.DOWN -> KeyCode.DOWN
        FXKeyCode.PRINTSCREEN -> KeyCode.PRINT_SCREEN
        FXKeyCode.INSERT -> KeyCode.INSERT
        FXKeyCode.DIGIT0 -> KeyCode.DIGIT0
        FXKeyCode.DIGIT1 -> KeyCode.DIGIT1
        FXKeyCode.DIGIT2 -> KeyCode.DIGIT2
        FXKeyCode.DIGIT3 -> KeyCode.DIGIT3
        FXKeyCode.DIGIT4 -> KeyCode.DIGIT4
        FXKeyCode.DIGIT5 -> KeyCode.DIGIT5
        FXKeyCode.DIGIT6 -> KeyCode.DIGIT6
        FXKeyCode.DIGIT7 -> KeyCode.DIGIT7
        FXKeyCode.DIGIT8 -> KeyCode.DIGIT8
        FXKeyCode.DIGIT9 -> KeyCode.DIGIT9
        FXKeyCode.DEAD_CIRCUMFLEX -> KeyCode.CIRCUMFLEX
        FXKeyCode.DEAD_ACUTE -> KeyCode.ACUTE
        FXKeyCode.A -> KeyCode.A
        FXKeyCode.B -> KeyCode.B
        FXKeyCode.C -> KeyCode.C
        FXKeyCode.D -> KeyCode.D
        FXKeyCode.E -> KeyCode.E
        FXKeyCode.F -> KeyCode.F
        FXKeyCode.G -> KeyCode.G
        FXKeyCode.H -> KeyCode.H
        FXKeyCode.I -> KeyCode.I
        FXKeyCode.J -> KeyCode.J
        FXKeyCode.K -> KeyCode.K
        FXKeyCode.L -> KeyCode.L
        FXKeyCode.M -> KeyCode.M
        FXKeyCode.N -> KeyCode.N
        FXKeyCode.O -> KeyCode.O
        FXKeyCode.P -> KeyCode.P
        FXKeyCode.Q -> KeyCode.Q
        FXKeyCode.R -> KeyCode.R
        FXKeyCode.S -> KeyCode.S
        FXKeyCode.T -> KeyCode.T
        FXKeyCode.U -> KeyCode.U
        FXKeyCode.V -> KeyCode.V
        FXKeyCode.W -> KeyCode.W
        FXKeyCode.X -> KeyCode.X
        FXKeyCode.Y -> KeyCode.Y
        FXKeyCode.Z -> KeyCode.Z
        FXKeyCode.COMMA -> KeyCode.COMMA
        FXKeyCode.PERIOD -> KeyCode.PERIOD
        FXKeyCode.MINUS -> KeyCode.MINUS
        FXKeyCode.LESS -> KeyCode.LESS
        FXKeyCode.NUMBER_SIGN -> KeyCode.NUMBER_SIGN
        FXKeyCode.PLUS -> KeyCode.PLUS
        FXKeyCode.NUM_LOCK -> KeyCode.NUM_LOCK
        FXKeyCode.NUMPAD0 -> KeyCode.NUMPAD0
        FXKeyCode.NUMPAD1 -> KeyCode.NUMPAD1
        FXKeyCode.NUMPAD2 -> KeyCode.NUMPAD2
        FXKeyCode.NUMPAD3 -> KeyCode.NUMPAD3
        FXKeyCode.NUMPAD4 -> KeyCode.NUMPAD4
        FXKeyCode.NUMPAD5 -> KeyCode.NUMPAD5
        FXKeyCode.NUMPAD6 -> KeyCode.NUMPAD6
        FXKeyCode.NUMPAD7 -> KeyCode.NUMPAD7
        FXKeyCode.NUMPAD8 -> KeyCode.NUMPAD8
        FXKeyCode.NUMPAD9 -> KeyCode.NUMPAD9
        FXKeyCode.DIVIDE -> KeyCode.DIVIDE
        FXKeyCode.MULTIPLY -> KeyCode.MULTIPLY
        FXKeyCode.SUBTRACT -> KeyCode.SUBTRACT
        FXKeyCode.ADD -> KeyCode.ADD
        FXKeyCode.DECIMAL -> KeyCode.DECIMAL
        FXKeyCode.F1 -> KeyCode.F1
        FXKeyCode.F2 -> KeyCode.F2
        FXKeyCode.F3 -> KeyCode.F3
        FXKeyCode.F4 -> KeyCode.F4
        FXKeyCode.F5 -> KeyCode.F5
        FXKeyCode.F6 -> KeyCode.F6
        FXKeyCode.F7 -> KeyCode.F7
        FXKeyCode.F8 -> KeyCode.F8
        FXKeyCode.F9 -> KeyCode.F9
        FXKeyCode.F10 -> KeyCode.F10
        FXKeyCode.F11 -> KeyCode.F11
        FXKeyCode.F12 -> KeyCode.F12
        else -> KeyCode.UNDEFINED
      }
  // endregion

  /** Converts the [DialogType] to [Alert.AlertType]. */
  internal fun DialogType.toFXAlertType(): Alert.AlertType =
      when (this) {
        DialogType.NONE -> Alert.AlertType.NONE
        DialogType.INFORMATION -> Alert.AlertType.INFORMATION
        DialogType.WARNING -> Alert.AlertType.WARNING
        DialogType.CONFIRMATION -> Alert.AlertType.CONFIRMATION
        DialogType.ERROR -> Alert.AlertType.ERROR
        DialogType.EXCEPTION -> Alert.AlertType.ERROR
      }

  /** Converts the [Alert.AlertType] to [DialogType] . */
  internal fun Alert.AlertType.toAlertType(): DialogType =
      when (this) {
        Alert.AlertType.NONE -> DialogType.NONE
        Alert.AlertType.INFORMATION -> DialogType.INFORMATION
        Alert.AlertType.WARNING -> DialogType.WARNING
        Alert.AlertType.CONFIRMATION -> DialogType.CONFIRMATION
        Alert.AlertType.ERROR -> DialogType.ERROR
      }

  /** Converts the [ButtonType] to [javafx.scene.control.ButtonType]. */
  internal fun ButtonType.toFXButtonType(): FXButtonType =
      when (this) {
        ButtonType.APPLY -> FXButtonType.APPLY
        ButtonType.OK -> FXButtonType.OK
        ButtonType.CANCEL -> FXButtonType.CANCEL
        ButtonType.CLOSE -> FXButtonType.CLOSE
        ButtonType.YES -> FXButtonType.YES
        ButtonType.NO -> FXButtonType.NO
        ButtonType.FINISH -> FXButtonType.FINISH
        ButtonType.NEXT -> FXButtonType.NEXT
        ButtonType.PREVIOUS -> FXButtonType.PREVIOUS
      }

  /** Converts the [javafx.scene.control.ButtonType] constant to [ButtonType]. */
  internal fun FXButtonType.toButtonType(): ButtonType =
      when (this) {
        FXButtonType.APPLY -> ButtonType.APPLY
        FXButtonType.OK -> ButtonType.OK
        FXButtonType.CANCEL -> ButtonType.CANCEL
        FXButtonType.CLOSE -> ButtonType.CLOSE
        FXButtonType.YES -> ButtonType.YES
        FXButtonType.NO -> ButtonType.NO
        FXButtonType.FINISH -> ButtonType.FINISH
        FXButtonType.NEXT -> ButtonType.NEXT
        FXButtonType.PREVIOUS -> ButtonType.PREVIOUS
        else -> throw IllegalArgumentException("Unsupported button type.")
      }

  /** Converts the [Orientation] constant to [javafx.geometry.Orientation]. */
  internal fun Orientation.toJavaFXOrientation(): javafx.geometry.Orientation =
      when (this) {
        Orientation.HORIZONTAL -> javafx.geometry.Orientation.HORIZONTAL
        Orientation.VERTICAL -> javafx.geometry.Orientation.VERTICAL
      }

  /** Converts the [Alignment] constant to [Pos]. */
  internal fun Alignment.toFXPos(): Pos =
      when (this) {
        Alignment.TOP_LEFT -> Pos.TOP_LEFT
        Alignment.TOP_RIGHT -> Pos.TOP_RIGHT
        Alignment.TOP_CENTER -> Pos.TOP_CENTER
        Alignment.BOTTOM_LEFT -> Pos.BOTTOM_LEFT
        Alignment.BOTTOM_RIGHT -> Pos.BOTTOM_RIGHT
        Alignment.BOTTOM_CENTER -> Pos.BOTTOM_CENTER
        Alignment.CENTER_LEFT -> Pos.CENTER_LEFT
        Alignment.CENTER_RIGHT -> Pos.CENTER_RIGHT
        Alignment.CENTER -> Pos.CENTER
      }
}
