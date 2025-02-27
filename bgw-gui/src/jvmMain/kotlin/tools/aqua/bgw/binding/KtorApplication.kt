/*
 * Copyright 2025 The BoardGameWork Authors
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

import ActionProp
import AppData
import PropData
import SceneMapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.io.ByteArrayOutputStream
import java.time.Duration
import java.util.*
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import jsonMapper
import kotlin.text.Charsets.UTF_8
import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.html.*
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.core.Frontend

internal val componentChannel: Channel =
    Channel("/ws").apply {
      onClientConnected = {
        Frontend.application
        val json =
            jsonMapper.encodeToString(
                PropData(
                    SceneMapper.map(
                            menuScene = Frontend.menuScene, gameScene = Frontend.boardGameScene)
                        .apply {
                          fonts =
                              Frontend.loadedFonts.map { (path, fontName, weight) ->
                                Triple(path, fontName, weight.toInt())
                              }
                        }))
        it.send(json)
        // if (!uiJob.isActive) uiJob.start()
      }
    }

internal val internalChannel: Channel =
    Channel("/internal").apply { onClientMessage = { session, text -> } }

internal fun HTML.index() {
  body {
    style = "width: 100%; height: 100%; overflow: hidden; overscroll-behavior: none;"
    div {
      classes = setOf("bgw-root-container")
      div {
        classes = setOf("bgw-dialogs")
        id = "bgw-dialogs"
      }
      div {
        classes = setOf("bgw-root")
        id = "bgw-root"
      }
    }
    script(src = "/static/bgw-gui.js") {}
  }
}

internal fun Application.module() {
  configureRouting()
  install(WebSockets) {
    pingPeriod = Duration.ofSeconds(15)
    timeout = Duration.ofSeconds(15)
    maxFrameSize = Long.MAX_VALUE
    masking = false
  }
  componentChannel.install(this)
  internalChannel.install(this)
}

internal fun Application.configureRouting() {
  routing {
    get("/") { call.respondHtml(HttpStatusCode.OK, HTML::index) }
    static("/static") { resources() }
  }
}

internal fun CoroutineScope.launchPeriodicAsync(repeatMillis: Long, action: (suspend () -> Unit)) =
    this.async {
      if (repeatMillis > 0) {
        while (isActive) {
          action()
          delay(repeatMillis)
        }
      } else {
        action()
      }
    }

internal fun gzip(content: String): String {
  val bos = ByteArrayOutputStream()
  GZIPOutputStream(bos).bufferedWriter(UTF_8).use { it.write(content) }
  return Base64.getEncoder().encodeToString(bos.toByteArray())
}

internal fun ungzip(content: ByteArray): String =
    GZIPInputStream(content.inputStream()).bufferedReader(UTF_8).use { it.readText() }

private val updateStack = Collections.synchronizedList(Stack<AppData>())
private val debounceTimeMillis = 5L // Adjust this value as needed
private val debounceMutex = Mutex()
private var debounceJob: Job? = null

private var refreshJob: Job? = null

private var finalUpdate: String = ""

internal fun enqueueUpdate(data: AppData) {
  updateStack.add(data)
  refreshJob?.cancel()
  debounceJob?.cancel()
  debounceJob =
      CoroutineScope(Dispatchers.IO).launch {
        delay(debounceTimeMillis)
        debounceMutex.withLock { processLastUpdate() }
      }
}

internal fun forceUpdate() {
  debounceJob?.cancel()
  CoroutineScope(Dispatchers.IO).launch { debounceMutex.withLock { processLastUpdate() } }
}

private suspend fun processLastUpdate() {
  val lastUpdate = updateStack.lastOrNull()
  updateStack.clear()
  lastUpdate?.let {
    try {
      val json = jsonMapper.encodeToString(PropData(lastUpdate))
      finalUpdate = json
      // println("Processing update: $json")
      componentChannel.sendToAllClients(json)
      refreshJob =
          CoroutineScope(Dispatchers.IO).launch {
            delay(50)
            checkForMissingState()
          }
    } catch (e: Exception) {
      println("Error sending update: $e")
    }
  }
}

internal fun checkForMissingState() {
  val appData = collectAppData()
  val json = jsonMapper.encodeToString(PropData(appData))
  if (json != finalUpdate) {
    // println("Missing state detected. Sending update...")
    enqueueUpdate(appData)
  }
  finalUpdate = ""

  refreshJob?.cancel()
}

internal fun collectAppData(): AppData {
  val appData =
      SceneMapper.map(menuScene = Frontend.menuScene, gameScene = Frontend.boardGameScene).apply {
        fonts =
            Frontend.loadedFonts.map { (path, fontName, weight) ->
              Triple(path, fontName, weight.toInt())
            }
      }
  appData.action = ActionProp.UPDATE_COMPONENT
  return appData
}

internal fun markDirty(prop: ActionProp) {
  runCatching {
    val appData = collectAppData()
    // println("Collecting updates... Size: " + updateStack.size)
    enqueueUpdate(appData)
  }
}
