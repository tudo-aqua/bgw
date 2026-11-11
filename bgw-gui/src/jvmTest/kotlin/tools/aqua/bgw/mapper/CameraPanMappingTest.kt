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
import data.event.InternalCameraPanData
import jsonMapper
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.frontend.app

class CameraPanMappingTest {
  /** The mapper requires an application instance. */
  private val application = app

  @Test
  fun repeatedIdenticalPanChangesTheState() {
    val camera =
        CameraPane(width = 100, height = 100, target = Pane<Label>(width = 400, height = 400))
    val scene = BoardGameScene(width = 100, height = 100).apply { addComponents(camera) }

    camera.panBy(0, 40, smooth = false)
    val first = jsonMapper.encodeToString(PropData(SceneMapper.map(gameScene = scene)))

    // The browser applied the pan.
    camera.reportedPanData = InternalCameraPanData(sequence = camera.panData.sequence)

    camera.panBy(0, 40, smooth = false)
    val second = jsonMapper.encodeToString(PropData(SceneMapper.map(gameScene = scene)))

    assertNotEquals(first, second)
  }

  private fun camera() =
      CameraPane(width = 100, height = 100, target = Pane<Label>(width = 400, height = 400))

  private fun CameraPane<*>.reportApplied() {
    reportedPanData =
        InternalCameraPanData(
            sequence = panData.sequence,
            panByTotalX = panData.panByTotalX,
            panByTotalY = panData.panByTotalY)
  }

  @Test
  fun relativePansSentAsOneStateAddUp() {
    val camera = camera()
    camera.panBy(0, 40, smooth = false)
    camera.panBy(0, 40, smooth = false)
    camera.panBy(10, 0, smooth = false)

    assertEquals(-10.0, camera.panData.panByTotalX)
    assertEquals(-80.0, camera.panData.panByTotalY)
  }

  @Test
  fun relativePanMovesPendingAbsolutePan() {
    val camera = camera()
    camera.pan(100, 100, smooth = false)
    camera.panBy(0, 40, smooth = false)

    assertTrue(!camera.panData.panBy)
    assertEquals(Pair(-100.0, -140.0), camera.panData.panTo)
  }

  @Test
  fun relativePanAfterAppliedAbsolutePanIsRelative() {
    val camera = camera()
    camera.pan(100, 100, smooth = false)
    camera.reportApplied()
    camera.panBy(0, 40, smooth = false)

    assertTrue(camera.panData.panBy)
  }

  @Test
  fun zoomDoesNotRepeatAppliedPan() {
    val camera = camera()
    camera.pan(10, 10, smooth = false)
    camera.reportApplied()
    camera.zoom = 2.0

    assertTrue(camera.panData.zoomOnly)
    assertNull(camera.panData.panTo)
  }

  @Test
  fun panKeepsOnlyPendingZoom() {
    val camera = camera()
    camera.zoom = 2.0
    camera.pan(0, 0, smooth = false)
    assertEquals(2.0, camera.panData.zoom)

    camera.reportApplied()
    camera.pan(5, 5, smooth = false)
    assertNull(camera.panData.zoom)
  }
}
