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

package tools.aqua.bgw.examples.tetris.view

import tools.aqua.bgw.examples.tetris.entity.Tetris

/** Refreshable interface. */
interface Refreshable {
  /**
   * Refreshes tetris grid.
   *
   * @param tetris Tetris to display.
   */
  fun refresh(tetris: Tetris)

  /** Hides start instructions. */
  fun hideStartInstructions()

  /** Shows loose text. */
  fun loose()

  /**
   * Refreshes speed label.
   *
   * @param speed New speed to display.
   */
  fun refreshSpeed(speed: Long)

  /**
   * Refreshes points label.
   *
   * @param points New points to display.
   */
  fun refreshPoints(points: Int)
}
