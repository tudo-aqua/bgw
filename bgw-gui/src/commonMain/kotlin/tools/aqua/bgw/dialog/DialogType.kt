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
 * Enum for all available alert types.
 *
 * @since 0.1
 */
enum class DialogType {
  /**
   * The [NONE] alert type has the effect of not setting any default properties in the Alert besides
   * an [ButtonType.OK] button for the user to click on to dismiss the dialog.
   */
  NONE,

  /**
   * The [INFORMATION] alert type configures the Alert dialog to appear in a way that suggests the
   * content of the dialog is informing the user of a piece of information. This includes an
   * 'information' image, an appropriate title and header, and just a [ButtonType.OK] button for the
   * user to click on to dismiss the dialog.
   */
  INFORMATION,

  /**
   * The [WARNING] alert type configures the Alert dialog to appear in a way that suggests the
   * content of the dialog is warning the user about some fact or action. This includes a 'warning'
   * image, an appropriate title and header, and just a [ButtonType.OK] button for the user to click
   * on to dismiss the dialog.
   */
  WARNING,

  /**
   * The [CONFIRMATION] alert type configures the Alert dialog to appear in a way that suggests the
   * content of the dialog is seeking confirmation from the user. This includes a 'confirmation'
   * image, an appropriate title and header, and both [ButtonType.YES] and [ButtonType.NO] buttons
   * for the user to click on to dismiss the dialog.
   */
  CONFIRMATION,

  /**
   * The [ERROR] alert type configures the Alert dialog to appear in a way that suggests that
   * something has gone wrong. This includes an 'error' image, an appropriate title and header, and
   * just a [ButtonType.OK] button for the user to click on to dismiss the dialog.
   */
  ERROR,

  /**
   * The [EXCEPTION] alert type configures the Alert dialog to show an exception stack trace. This
   * includes an 'exception' image, an appropriate title and header, and just a [ButtonType.OK]
   * button for the user to click on to dismiss the dialog.
   */
  EXCEPTION
}
