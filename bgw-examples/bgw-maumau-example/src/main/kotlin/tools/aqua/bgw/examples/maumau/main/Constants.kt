/*
 * Copyright 2022 The BoardGameWork Authors
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

package tools.aqua.bgw.examples.maumau.main

import tools.aqua.bgw.util.Font
import java.awt.Color

/** MauMauCard height. */
const val IMG_HEIGHT: Int = 200

/** MauMauCard width. */
const val IMG_WIDTH: Int = 130

/** MauMauCard front file. */
const val CARDS_FILE: String = "assets/card_deck.png"

/** Background file. */
const val BG_FILE: String = "assets/bg.jpg"

/** Button background file. */
const val BUTTON_BG_FILE: String = "assets/buttonBG.png"

/** Lightbulb background file. */
const val LIGHT_BULB_FILE: String = "assets/light-bulb.png"

/** Menu item height. */
const val MENU_ITEM_HEIGHT: Int = 60

/** Menu item width. */
const val MENU_ITEM_WIDTH: Int = 200

/** Game id for bgw-net. */
const val GAME_ID: String = "MauMau"

/** Secret for bgw-net. */
const val NETWORK_SECRET: String = "geheim"

/** Button font. */
val MENU_BUTTON_FONT: Font = Font(color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC)
