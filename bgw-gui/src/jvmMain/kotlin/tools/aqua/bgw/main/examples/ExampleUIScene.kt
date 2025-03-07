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

package tools.aqua.bgw.main.examples

import PropData
import jsonMapper
import kotlin.reflect.KProperty0
import kotlinx.serialization.encodeToString
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.main.examples.ExampleUIScene as Scene
import tools.aqua.bgw.style.BorderRadius
import tools.aqua.bgw.visual.ColorVisual

/** Metadata: [Scene] */
internal class ExampleUIScene : BoardGameScene(width = 622, height = 300) {
  val map = mutableMapOf<String, String>()

  /** Create a light gray button. */
  val button =
      Button(
          posX = 100,
          posY = 50,
          width = 200,
          height = 100,
          text = "I am a Button.",
          visual = ColorVisual.LIGHT_GRAY)

  /** Creates a red button with large border radius. */
  val button2 =
      Button(
          posX = 0,
          posY = 0,
          width = 200,
          height = 200,
          text = "Also a Button.",
          visual = ColorVisual.RED.apply { style.borderRadius = BorderRadius.LARGE })

  init {
    setComponentAndSerialize(::button)
    setComponentAndSerialize(::button2)
  }

  internal fun <T : ComponentView> setComponentAndSerialize(prop: KProperty0<T>) {
    val comp = prop.get()

    clearComponents()
    addComponents(comp)

    val fullyQualifiedName = "${this::class.qualifiedName}.${prop.name}"

    val appData = SceneMapper.map(menuScene = null, gameScene = this)
    val json = jsonMapper.encodeToString(PropData(appData))
    map[fullyQualifiedName] = json

    clearComponents()
  }
}
