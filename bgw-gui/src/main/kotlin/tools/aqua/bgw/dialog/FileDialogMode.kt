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

package tools.aqua.bgw.dialog

/** Enum for possible [FileDialog] modes. */
enum class FileDialogMode {
  /** Mode to open one file. */
  OPEN_FILE,

  /** Mode to open multiple files. */
  OPEN_MULTIPLE_FILES,

  /** Mode to save a file. */
  SAVE_FILE,

  /** Mode so select a directory. */
  CHOOSE_DIRECTORY
}
