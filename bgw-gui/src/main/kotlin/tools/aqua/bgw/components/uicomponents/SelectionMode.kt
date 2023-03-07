/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.components.uicomponents

/** Enum indicating allowed selection mode. */
enum class SelectionMode {
  /** Enum constant indicating that NO selection is possible. */
  NONE,

  /** Enum constant indicating that only a single element may be selected. */
  SINGLE,

  /** Enum constant indicating that any number of selected elements is valid. */
  MULTIPLE
}
