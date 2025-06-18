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

package tools.aqua.bgw

import ActionProp
import AnimationData
import AppData
import Data
import DialogData
import ID
import JsonData
import PropData
import data.event.AnimationFinishedEventData
import data.event.LoadEventData
import jsonMapper
import kotlinx.browser.document
import org.w3c.dom.CustomEvent
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLStyleElement
import org.w3c.dom.NodeList
import org.w3c.dom.WebSocket
import react.*
import react.dom.client.Root
import react.dom.client.createRoot
import react.dom.render
import tools.aqua.bgw.elements.App
import tools.aqua.bgw.elements.Dialog
import tools.aqua.bgw.event.JCEFEventDispatcher
import web.dom.Element
import web.timers.setTimeout

internal var internalSocket: WebSocket? = null
internal var webSocket: WebSocket? = null
internal var handlers: MutableMap<ID, (Data) -> Unit> = mutableMapOf()

internal lateinit var container: HTMLElement
internal lateinit var dialogContainer: HTMLElement
internal lateinit var root: Root
internal lateinit var dialogRoot: Root
internal val dialogMap = mutableMapOf<ID, DialogData>()

internal fun main() {
  if (Config.USE_SOCKETS) {
    webSocket = WebSocket("ws://${document.location?.host}/ws")
    webSocket?.onopen = {}
    webSocket?.onmessage = { event ->
      val cont = document.getElementById("bgw-root")
      val dialog = document.getElementById("bgw-dialogs")

      if (cont != null) {
        dialogContainer = dialog as HTMLElement
      }

      if (cont != null) {
        container = cont as HTMLElement
        val receivedData = jsonMapper.decodeFromString<PropData>(event.data.toString()).data
        handleReceivedData(receivedData!!)
      }
    }
  } else {
    document.addEventListener(
        "BGW_MSG",
        {
          val event = it as CustomEvent
          val data = event.detail
          val jsonData = jsonMapper.decodeFromString<JsonData>(data.toString())
          val receivedData = jsonData.props.data
          val containerId = jsonData.container

          val cont = document.getElementById(containerId)
          if (cont != null) {
            container = cont as HTMLElement
            handleReceivedData(receivedData!!)
          }
        })
  }
}

internal fun resetAnimations() {
  val styles = document.body?.querySelectorAll("style") as NodeList
  for (i in 0 until styles.length) {
    val style = styles.item(i) as HTMLStyleElement
    document.body?.removeChild(style)
  }
}

internal fun stopAnimations() {
  Animator.clearAllTimeoutsAndIntervals()
  resetAnimations()
}

internal fun handleReceivedData(receivedData: Data) {
  when (receivedData) {
    is AppData -> {
      if (receivedData.action == ActionProp.HIDE_MENU_SCENE) {
        val element = document.querySelector("#menuScene") as HTMLElement
        element.classList.toggle("scene--visible", false)
        setTimeout(
            {
              if (!Config.USE_SOCKETS) {
                renderApp(receivedData)
              } else {
                renderAppFast(receivedData)
              }
            },
            300)
      } else if (receivedData.action == ActionProp.SHOW_MENU_SCENE) {
        if (!Config.USE_SOCKETS) {
          renderApp(receivedData)
        } else {
          renderAppFast(receivedData)
        }
        val element = document.querySelector("#menuScene") as HTMLElement
        setTimeout({ element.classList.toggle("scene--visible", true) }, 50)
      } else {
        // Cancel all pending animation timeouts before new render to prevent incorrect resets
        Animator.cancelCleanupTimeouts()
        if (!Config.USE_SOCKETS) {
          renderApp(receivedData)
        } else {
          renderAppFast(receivedData)
        }
      }
      if (!receivedData.forcedByAnimation) {
        // stopAnimations()
      }
    }
    is AnimationData -> {
      if (receivedData.isStop) {
        stopAnimations()
        return
      }
      Animator.startAnimation(receivedData) {
        JCEFEventDispatcher.dispatchEvent(AnimationFinishedEventData().apply { id = it })
      }
    }
    is DialogData -> {
      dialogMap[receivedData.id] = receivedData
      renderDialogs()
    }
    else -> {}
  }
}

/** Renders the app with React 17 syntax to provide fallback for BGW Playground web app. */
internal fun renderApp(appData: AppData) {
  render(
      App.create { data = appData },
      container as Element,
      callback = { JCEFEventDispatcher.dispatchEvent(LoadEventData()) })
}

/** Renders the app with React 18 syntax. */
internal fun renderAppFast(appData: AppData) {
  if (!::root.isInitialized) {
    root = createRoot(container as Element)
  }
  root.render(App.create { data = appData })
  JCEFEventDispatcher.dispatchEvent(LoadEventData())
}

internal fun renderDialogs() {
  println("Rendering Dialogs $dialogMap")
  if (!::dialogRoot.isInitialized) {
    dialogRoot = createRoot(dialogContainer as Element)
  }
  dialogRoot.render(Dialog.create { data = dialogMap.values.toList() })
}

internal fun ChildrenBuilder.appendChildren(components: List<ReactElement<*>>) =
    components.forEach { +it }
