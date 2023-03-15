/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.container

import java.awt.Color
import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.visual.ColorVisual

/** Test base for GameComponentViewContainers. */
open class GameComponentViewContainerTestBase {

  /** A red [TokenView]. */
  protected lateinit var redTokenView: TokenView

  /** A green [TokenView]. */
  protected lateinit var greenTokenView: TokenView

  /** A blue [TokenView]. */
  protected lateinit var blueTokenView: TokenView

  /** Initializes tokens. */
  @BeforeEach
  fun setup() {
    redTokenView = TokenView(50, 50, 0, 0, ColorVisual(Color.RED))
    greenTokenView = TokenView(50, 50, 0, 0, ColorVisual(Color.GREEN))
    blueTokenView = TokenView(50, 50, 0, 0, ColorVisual(Color.BLUE))
  }
}
