/*
 * Copyright 2026 The BoardGameWork Authors
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

package tools.aqua.bgw.mapper

import PropData
import SceneMapper
import jsonMapper
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.frontend.app

class NonFiniteValueMappingTest {
  /** The mapper requires an application instance. */
  private val application = app

  @Test
  fun nonFiniteSizesAreSentAsZero() {
    val label = Label(width = 10, height = 10).apply { width = 0.0 / 0.0 }
    val stretched = Label(width = 10, height = 10).apply { posX = Double.POSITIVE_INFINITY }
    val scene = BoardGameScene(width = 100, height = 100).apply { addComponents(label, stretched) }

    val json = jsonMapper.encodeToString(PropData(SceneMapper.map(gameScene = scene)))

    assertFalse(json.contains("NaN") || json.contains("Infinity"), json)
    assertTrue(json.contains("\"width\":0.0"), json)
  }

  @Test
  fun emptyGridIsMapped() {
    val grid = GridPane<Label>(columns = 4, rows = 0)
    val scene = BoardGameScene(width = 100, height = 100).apply { addComponents(grid) }

    val json = jsonMapper.encodeToString(PropData(SceneMapper.map(gameScene = scene)))

    assertTrue(json.contains("\"columnWidths\":[0.0,0.0,0.0,0.0]"), json)
  }
}
