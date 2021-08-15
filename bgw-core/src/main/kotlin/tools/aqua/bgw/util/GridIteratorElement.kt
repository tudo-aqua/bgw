/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package tools.aqua.bgw.util

import tools.aqua.bgw.util.ComponentViewGrid.GridIterator

/**
 * Data class containing meta info about current grid element returned by its [GridIterator].
 *
 * @constructor Creates a [GridIteratorElement].
 *
 * @param columnIndex Current column index.
 * @param rowIndex Current row index.
 * @param component Current component or `null` if there is no component present in this cell.
 */
data class GridIteratorElement<T>(
	val columnIndex: Int,
	val rowIndex: Int,
	val component: T?
)