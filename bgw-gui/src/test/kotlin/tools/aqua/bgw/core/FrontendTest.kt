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

package tools.aqua.bgw.core

import javafx.scene.layout.Pane
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import tools.aqua.bgw.builder.Frontend

class FrontendTest {

  /**
   * Tests that the menu scene is null after calling [Frontend.hideMenuScene] so that the fade in
   * and out animations are played.
   */
  @Test
  fun testMenuSceneIsNull() {
    val menuPaneField =
        Frontend::class.java.getDeclaredField("menuPane").apply { isAccessible = true }
    val menuSceneField =
        Frontend::class.java.getDeclaredField("menuScene").apply { isAccessible = true }
    menuPaneField.set(Frontend, Pane())

    Frontend.showMenuScene(MenuScene(), DEFAULT_FADE_TIME.toDouble())

    assertNotNull(menuSceneField.get(Frontend))

    Frontend.hideMenuScene(0.0)

    assertNull(menuSceneField.get(Frontend))
  }
}
