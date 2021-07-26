/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package container

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.elements.gameelements.TokenView
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color

abstract class GameElementContainerViewTestBase {
    protected lateinit var redToken: TokenView
    protected lateinit var blueToken: TokenView
    protected lateinit var greenToken: TokenView
    
    @BeforeEach
    fun setup() {
        redToken = TokenView(50, 50, 0, 0, ColorVisual(Color.RED))
        blueToken = TokenView(50, 50, 0, 0, ColorVisual(Color.BLUE))
        greenToken = TokenView(50, 50, 0, 0, ColorVisual(Color.GREEN))
    }
}