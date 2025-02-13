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

@file:Suppress("unused", "SpreadOperator")

package tools.aqua.bgw.dialog

/**
 * Shows a dialog containing the given [message] and [buttons].
 *
 * @constructor Private constructor. Refer to secondary constructors.
 *
 * @property dialogType The [DialogType] of the dialog. Affects the displayed icon.
 * @property title Title to be shown.
 * @property header Headline to be shown in the dialogs content.
 * @property message Message to be shown.
 * @property exception [Throwable] to be shown in expandable content.
 * @property buttons Buttons to be shown. Standard set of buttons according to [dialogType] will be
 * used if you don't pass any [ButtonType]s.
 *
 * @see DialogType
 * @see ButtonType
 *
 * @since 0.1
 */
data class Dialog
internal constructor(
    val dialogType: DialogType,
    val title: String,
    val header: String,
    val message: String,
    val exception: Throwable,
    val buttons: List<ButtonType>
) {
  internal val id = IDGenerator.generateDialogID()

  /**
   * Creates a Dialog.
   *
   * For exception dialogs refer to exception constructor.
   *
   * @param dialogType The [DialogType] of the dialog. Affects the displayed icon.
   * @param title Title to be shown.
   * @param header Headline to be shown in the dialogs content.
   * @param message Message to be shown in the dialogs content.
   * @param buttons Buttons to be shown. Standard set of buttons according to [dialogType] will be
   * used if you don't pass any [ButtonType]s.
   *
   * @see DialogType
   * @see ButtonType
   *
   * @since 0.1
   */
  constructor(
      dialogType: DialogType,
      title: String,
      header: String,
      message: String,
      vararg buttons: ButtonType
  ) : this(dialogType, title, header, message, Exception("empty"), buttons.asList()) {
    require(dialogType != DialogType.EXCEPTION) {
      "To create an Exception dialog use exception dialog constructor."
    }
  }

  /**
   * Creates an exception Dialog.
   *
   * For information dialogs refer to information constructor.
   *
   * @param title Title to be shown.
   * @param header Headline to be shown in the dialogs content.
   * @param message Message to be shown.
   * @param exception [Throwable] to be shown in expandable content.
   *
   * @since 0.1
   */
  constructor(
      title: String,
      header: String,
      message: String,
      exception: Throwable
  ) : this(DialogType.EXCEPTION, title, header, message, exception, emptyList())
}
