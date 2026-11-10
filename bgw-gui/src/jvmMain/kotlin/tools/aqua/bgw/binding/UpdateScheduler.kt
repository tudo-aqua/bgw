/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

import AnimationData
import PropData
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import jsonMapper
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.animation.AnimationType
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.util.Logger

/**
 * Sends the application state to the browser.
 *
 * Property changes only mark the state as dirty, which is cheap. A single background thread
 * snapshots and sends the whole state at most once per burst of changes:
 * - Changes within [DEBOUNCE_MILLIS] are coalesced into one update.
 * - While an event handler runs (see [batch]), updates are held back and sent once the handler
 *   returned, so a click produces one consistent update. Long-running handlers are still flushed
 *   every [MAX_BATCH_MILLIS] so intermediate states stay visible.
 * - The browser acknowledges each rendered state ([onRendered]). Until then, no new state is sent;
 *   changes made in the meantime are sent as one update afterwards. This keeps the browser from
 *   falling behind when the game changes state faster than it can be rendered.
 * - A state identical to the last sent one is not sent again.
 */
internal object UpdateScheduler {
  private const val DEBOUNCE_MILLIS = 4L
  private const val MAX_BATCH_MILLIS = 50L
  private const val ACK_TIMEOUT_MILLIS = 1000L

  private val executor: ScheduledExecutorService =
      Executors.newSingleThreadScheduledExecutor { runnable ->
        Thread(runnable, "BGW Update Thread").apply { isDaemon = true }
      }

  private val dirty = AtomicBoolean(false)
  private val scheduled = AtomicBoolean(false)
  private val batchDepth = AtomicInteger(0)
  @Volatile private var batchStart = 0L

  /** Serializes snapshots and sends, so states reach the browser in the order they were taken. */
  private val sendLock = Any()
  @Volatile private var awaitingRenderSince = 0L
  @Volatile private var lastSentJson: String? = null

  /** Number of state updates sent to the browser, for diagnostics and tests. */
  @Volatile
  internal var sentUpdates = 0L
    private set

  /** Marks the application state as changed. Cheap, can be called for every property change. */
  fun markDirty() {
    dirty.set(true)
    schedule(DEBOUNCE_MILLIS)
  }

  private fun schedule(delayMillis: Long) {
    if (scheduled.compareAndSet(false, true)) {
      executor.schedule(::flush, delayMillis, TimeUnit.MILLISECONDS)
    }
  }

  /** Runs [block] (e.g. an event handler) and sends its changes as one update afterwards. */
  fun <T> batch(block: () -> T): T {
    if (batchDepth.getAndIncrement() == 0) batchStart = System.currentTimeMillis()
    try {
      return block()
    } finally {
      if (batchDepth.decrementAndGet() == 0 && dirty.get()) schedule(0)
    }
  }

  /** Called when the browser rendered a state sent by [flush]. */
  fun onRendered() {
    awaitingRenderSince = 0L
    if (dirty.get()) schedule(0)
  }

  /** Called when a browser (re)connects and receives the full state from the connection handler. */
  fun onClientConnected(json: String) {
    lastSentJson = json
    awaitingRenderSince = System.currentTimeMillis()
  }

  private fun flush() {
    scheduled.set(false)
    if (!dirty.get()) return

    val now = System.currentTimeMillis()
    if (batchDepth.get() > 0 && now - batchStart < MAX_BATCH_MILLIS) {
      schedule(MAX_BATCH_MILLIS - (now - batchStart))
      return
    }
    val waitingSince = awaitingRenderSince
    if (waitingSince != 0L && now - waitingSince < ACK_TIMEOUT_MILLIS) {
      // onRendered() reschedules. The timeout covers lost acknowledgements.
      schedule(ACK_TIMEOUT_MILLIS - (now - waitingSince))
      return
    }

    dirty.set(false)
    try {
      sendState()
    } catch (e: Exception) {
      // The state was modified concurrently while it was mapped (e.g. a list changed during
      // iteration). The modification marked the state as dirty again, otherwise retry anyway.
      Logger.debug("Retrying state update after ${e::class.simpleName}: ${e.message}")
      dirty.set(true)
      schedule(DEBOUNCE_MILLIS)
    }
  }

  private fun sendState() {
    synchronized(sendLock) {
      val (json, sentFlags) = snapshot()
      if (json == lastSentJson) return
      send(json)
      clearFinishedAnimationFlags(sentFlags)
    }
  }

  /**
   * Sends the current state immediately followed by [animationData]. Used when an animation starts,
   * so it starts on exactly the state it was created for.
   */
  fun sendStateWithAnimation(animationData: AnimationData) {
    synchronized(sendLock) {
      dirty.set(false)
      val (json, sentFlags) = snapshot(forcedByAnimation = true)
      send(json)
      // The flags were part of this state. Clear them, otherwise a later update would report them
      // again and the browser would revert the animation started now.
      clearFinishedAnimationFlags(sentFlags)
      runBlocking {
        componentChannel.sendToAllClients(jsonMapper.encodeToString(PropData(animationData)))
      }
    }
  }

  private fun snapshot(
      forcedByAnimation: Boolean = false
  ): Pair<String, Map<ComponentView, Set<AnimationType>>> {
    // Take the flags before mapping. Flags added while mapping may or may not be part of this
    // state, so they are kept and sent again with the next state (which is harmless).
    val flags =
        synchronized(Frontend.componentsWithFinishedAnimations) {
          Frontend.componentsWithFinishedAnimations.associateWith {
            it.animationsFinishedSinceLastUpdate.toSet()
          }
        }
    val appData = collectAppData().apply { this.forcedByAnimation = forcedByAnimation }
    return jsonMapper.encodeToString(PropData(appData)) to flags
  }

  private fun send(json: String) {
    lastSentJson = json
    if (componentChannel.activeSessions.isNotEmpty()) {
      awaitingRenderSince = System.currentTimeMillis()
    }
    sentUpdates++
    runBlocking { componentChannel.sendToAllClients(json) }
  }

  private fun clearFinishedAnimationFlags(sent: Map<ComponentView, Set<AnimationType>>) {
    synchronized(Frontend.componentsWithFinishedAnimations) {
      sent.forEach { (component, types) ->
        component.animationsFinishedSinceLastUpdate.removeAll(types)
        if (component.animationsFinishedSinceLastUpdate.isEmpty()) {
          Frontend.componentsWithFinishedAnimations.remove(component)
        }
      }
    }
  }

  /** Reports that all animations of [type] on [component] finished with the next state. */
  fun reportFinishedAnimation(component: ComponentView, type: AnimationType) {
    synchronized(Frontend.componentsWithFinishedAnimations) {
      component.animationsFinishedSinceLastUpdate.add(type)
      Frontend.componentsWithFinishedAnimations.add(component)
    }
    markDirty()
  }
}
