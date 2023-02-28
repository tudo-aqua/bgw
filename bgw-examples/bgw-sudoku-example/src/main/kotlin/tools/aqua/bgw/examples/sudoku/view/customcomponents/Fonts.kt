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

package tools.aqua.bgw.examples.sudoku.view.customcomponents

import java.awt.Color
import tools.aqua.bgw.util.Font

/** Black [Font] for fixed digits. */
val blackFont: Font = Font(size = 40, color = Color.BLACK, fontWeight = Font.FontWeight.BOLD)

/** Blue [Font] for regular digits. */
val blueFont: Font = Font(size = 40, color = Color.BLUE, fontWeight = Font.FontWeight.BOLD)

/** [Color] for selected cells. */
val selectedColor: Color = Color(255, 238, 143)

/** [Color] for errors in cells. */
val errorColor: Color = Color(255, 150, 150)

/** [Color] for for finished game. */
val wonColor: Color = Color(150, 255, 150)
