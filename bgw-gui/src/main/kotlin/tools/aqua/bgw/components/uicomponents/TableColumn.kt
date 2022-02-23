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

@file:Suppress("unused")

package tools.aqua.bgw.components.uicomponents

/**
 * A [TableColumn] may be used to represent a column in a [TableView].
 *
 * @constructor Creates a [TableColumn].
 *
 * @param title The title for this [TableColumn]. It gets displayed in the header row of the
 * [TableView].
 * @param width The [width] for this [TableColumn].
 * @param formatFunction The format function for this [TableColumn]. It gets applied to each item in
 * the [TableView] to get a [String] for its cell.
 *
 * @see TableView
 */
open class TableColumn<T>(val title: String, val width: Number, val formatFunction: (T) -> String)
