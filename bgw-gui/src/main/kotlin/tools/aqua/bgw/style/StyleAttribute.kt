/*
 * Copyright 2023 The BoardGameWork Authors
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

package tools.aqua.bgw.style

import tools.aqua.bgw.visual.SingleLayerVisual
import tools.aqua.bgw.visual.Visual

abstract class StyleAttribute {
  internal abstract val key: String
  internal abstract val value: String
  internal open fun toCSS(): String = "$key : $value"
  override fun toString(): String = if (value.isNotEmpty()) toCSS() else ""
}

private fun String.addPostfixIfNotEmpty(postfix: String): String =
    if (isNotEmpty()) this + postfix else this

fun List<StyleAttribute>.toCSS(postfix: String = ";"): String =
    map { it.toCSS() }
        .filter { it.isNotEmpty() }
        .joinToString(separator = "; ")
        .addPostfixIfNotEmpty(postfix)

fun Visual.toCSS(postfix: String = ";"): String = listOf(backgroundRadius, borderRadius, borderStyle, borderColor, borderWidth, cursor).toCSS(postfix)
