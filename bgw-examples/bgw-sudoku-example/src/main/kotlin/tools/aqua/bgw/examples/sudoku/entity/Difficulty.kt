/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

package tools.aqua.bgw.examples.sudoku.entity

/**
 * Enum class for the three difficulties.
 *
 * @property file csv file for sudoku puzzles.
 */
enum class Difficulty(val file: String) {
  /** Easy difficulty. */
  EASY("easy.csv"),

  /** Medium difficulty. */
  MEDIUM("medium.csv"),

  /** Hard difficulty. */
  HARD("hard.csv")
}
