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
import PropData
import SceneMapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import java.time.Duration
import jsonMapper
import kotlinx.coroutines.*
import kotlinx.html.*
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.application.JCEFApplication
import tools.aqua.bgw.core.Frontend
import java.io.ByteArrayOutputStream
import java.util.Base64
import java.util.zip.GZIPInputStream
import java.util.zip.GZIPOutputStream
import kotlin.text.Charsets.UTF_8

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
        if (!uiJob.isActive) uiJob.start()
      }
    }

internal val internalChannel: Channel =
    Channel("/internal").apply { onClientMessage = { session, text -> } }

internal fun HTML.index() {
  body {
    div {
      classes = setOf("bgw-root-container")
      div {
        classes = setOf("bgw-portal")
        id = "bgw-portal"
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

internal val messageQueue = mutableListOf<ActionProp>()

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

internal var uiJob =
    CoroutineScope(Dispatchers.IO).launchPeriodicAsync(10) {
      if ((Frontend.applicationEngine as JCEFApplication).getTitle() !==
          Frontend.application.title) {
        (Frontend.applicationEngine as JCEFApplication).setTitle(Frontend.application.title)
      }

      if (messageQueue.isNotEmpty()) {
        val isSceneLoaded = Frontend.boardGameScene != null
        val message = messageQueue.removeFirst()
        val result = runCatching {
          val appData =
              SceneMapper.map(menuScene = Frontend.menuScene, gameScene = Frontend.boardGameScene)
                  .apply {
                    fonts =
                        Frontend.loadedFonts.map { (path, fontName, weight) ->
                          Triple(path, fontName, weight.toInt())
                        }
                  }
          appData.action = message
          val json = jsonMapper.encodeToString(PropData(appData))
          runBlocking { componentChannel.sendToAllClients(json) }
        }
        messageQueue.clear()
      }
    }
