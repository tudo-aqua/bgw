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

package tools.aqua.bgw.binding

import kotlin.test.Test
import kotlin.test.assertTrue
import tools.aqua.bgw.builder.SceneBuilder
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.frontend.app
import tools.aqua.bgw.visual.ColorVisual

class UpdateSchedulerTest {

  /** Waits until no further update is sent. */
  private fun awaitIdle() {
    var last = UpdateScheduler.sentUpdates
    var stableSince = System.currentTimeMillis()
    while (System.currentTimeMillis() - stableSince < 1500) {
      Thread.sleep(20)
      val now = UpdateScheduler.sentUpdates
      if (now != last) {
        last = now
        stableSince = System.currentTimeMillis()
      }
    }
  }

  private fun sceneWithTokens(count: Int): Pair<BoardGameScene, List<TokenView>> {
    val tokens =
        (0 until count).map { TokenView(width = 10, height = 10, visual = ColorVisual.RED) }
    val scene = BoardGameScene(width = 1000, height = 1000).apply { addComponents(tokens) }
    app.showGameScene(scene)
    SceneBuilder.build(scene)
    awaitIdle()
    return scene to tokens
  }

  @Test
  fun burstOfChangesIsSentAsOneUpdate() {
    val (_, tokens) = sceneWithTokens(200)
    val before = UpdateScheduler.sentUpdates

    val start = System.nanoTime()
    repeat(5) { round -> tokens.forEach { it.posX = round * 10.0 + 1 } }
    val callerMillis = (System.nanoTime() - start) / 1e6
    awaitIdle()

    val sent = UpdateScheduler.sentUpdates - before
    assertTrue(sent in 1..2, "1000 property changes caused $sent updates")
    // Property changes only mark the state as dirty, mapping happens on the update thread.
    assertTrue(callerMillis < 500, "1000 property changes took $callerMillis ms")
  }

  @Test
  fun unchangedStateIsNotSentAgain() {
    val (_, tokens) = sceneWithTokens(10)
    tokens.first().posX = 123.0
    awaitIdle()
    val before = UpdateScheduler.sentUpdates

    // Changes that cancel out within one burst leave the state unchanged.
    tokens.first().posX = 5.0
    tokens.first().posX = 123.0
    awaitIdle()

    assertTrue(UpdateScheduler.sentUpdates == before, "An identical state was sent again")
  }

  @Test
  fun changesOfComponentsAddedLaterAreSent() {
    val (scene, _) = sceneWithTokens(1)
    val late = TokenView(width = 10, height = 10, visual = ColorVisual.RED)
    scene.addComponents(late)
    awaitIdle()
    val before = UpdateScheduler.sentUpdates

    late.posX = 77.0
    awaitIdle()
    assertTrue(UpdateScheduler.sentUpdates > before, "Change of a component added later not sent")

    // The same holds for visuals that replaced the original one and for transparency changes.
    val visual = ColorVisual.BLUE
    late.visual = visual
    awaitIdle()
    val beforeTransparency = UpdateScheduler.sentUpdates
    visual.transparency = 0.5
    awaitIdle()
    assertTrue(
        UpdateScheduler.sentUpdates > beforeTransparency,
        "Transparency change of a replaced visual not sent")
  }

  @Test
  fun acknowledgementSendsWaitingChangesImmediately() {
    val (_, tokens) = sceneWithTokens(1)
    // A state was sent and the browser did not acknowledge it yet.
    UpdateScheduler.onClientConnected("")
    val before = UpdateScheduler.sentUpdates
    tokens.first().posX = 42.0
    Thread.sleep(100)
    assertTrue(UpdateScheduler.sentUpdates == before, "Sent before the acknowledgement")

    val acknowledged = System.currentTimeMillis()
    UpdateScheduler.onRendered()
    while (UpdateScheduler.sentUpdates == before &&
        System.currentTimeMillis() - acknowledged < 2000) {
      Thread.sleep(5)
    }
    val waited = System.currentTimeMillis() - acknowledged
    assertTrue(waited < 300, "Waiting change was sent $waited ms after the acknowledgement")
  }
}
