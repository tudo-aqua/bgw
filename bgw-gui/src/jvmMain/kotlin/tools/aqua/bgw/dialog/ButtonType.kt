/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.dialog

/**
 * Enum for all available button types.
 *
 * @property text displayed button text.
 *
 * @see Dialog
 * @see DialogType
 *
 * @since 0.1
 */
enum class ButtonType(
    internal val text: String,
    internal val bg: String = "#121824",
    internal val fg: String = "#ffffff"
) {
  /** A [ButtonType] that displays "Apply". */
  APPLY("Apply"),

  /** A [ButtonType] that displays "OK". */
  OK("OK"),

  /** A [ButtonType] that displays "Cancel". */
  CANCEL("Cancel"),

  /** A [ButtonType] that displays "Close". */
  CLOSE("Close"),

  /** A [ButtonType] that displays "Yes". */
  YES("Yes"),

  /** A [ButtonType] that displays "No". */
  NO("No"),

  /** A [ButtonType] that displays "Finish". */
  FINISH("Finish"),

  /** A [ButtonType] that displays "Next". */
  NEXT("Next"),

  /** A[ButtonType] that displays "Previous". */
  PREVIOUS("Previous"),

  /** A [ButtonType] that displays "Dismiss". */
  DISMISS("Dismiss"),
}
