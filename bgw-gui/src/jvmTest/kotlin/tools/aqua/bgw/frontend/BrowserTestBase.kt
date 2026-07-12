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

package tools.aqua.bgw.frontend

import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import tools.aqua.bgw.core.BoardGameScene

abstract class BrowserTestBase {
  protected lateinit var tester: BGWTester

  init {
    app.showNonBlocking()
  }

  @BeforeTest
  fun createBrowser() {
    tester = BGWTester()
  }

  @AfterTest
  fun closeBrowser() {
    tester.close()
  }

  protected fun show(scene: BoardGameScene) {
    app.showGameScene(scene)
    tester.load(app.headlessEnvironment, width = scene.width, height = scene.height)
  }
}
