/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.components.container.Area
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.container.Satchel
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.gamecomponentviews.DiceView
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.util.Font

// region Core
/** Default window title. */
const val DEFAULT_WINDOW_TITLE: String = "BoardGameWork Application"

/** Default window width in non-maximized mode. */
const val DEFAULT_WINDOW_WIDTH: Double = 1280.0

/** Default window height in non-maximized mode. */
const val DEFAULT_WINDOW_HEIGHT: Double = 747.0

/** Default minimization factor of window. */
const val DEFAULT_WINDOW_BORDER: Double = 0.8

/** Default scene width in virtual pixels. */
const val DEFAULT_SCENE_WIDTH: Double = 1920.0

/** Default scene height in virtual pixels. */
const val DEFAULT_SCENE_HEIGHT: Double = 1080.0

/** Default blur radius for menuScene overlay. */
const val DEFAULT_BLUR_RADIUS: Double = 63.0

/**
 * The default fade time for [MenuScene]s in [BoardGameApplication.showMenuScene] or
 * [BoardGameApplication.hideMenuScene] in milliseconds.
 */
const val DEFAULT_FADE_TIME: Int = 250

/** Default menu scene opacity. */
const val DEFAULT_MENU_SCENE_OPACITY: Double = 0.75
// endregion

// region Animation
/** Default [Animation] duration in milliseconds. */
const val DEFAULT_ANIMATION_DURATION: Int = 1000

/** Default [Animation] speed in steps. */
const val DEFAULT_ANIMATION_SPEED: Int = 50
// endregion

// region Components
/** Default [CardView] width. */
const val DEFAULT_CARD_WIDTH: Double = 130.0

/** Default [CardView] height. */
const val DEFAULT_CARD_HEIGHT: Double = 200.0

/** Default [TokenView] width. */
const val DEFAULT_TOKEN_WIDTH: Double = 50.0

/** Default [TokenView] height. */
const val DEFAULT_TOKEN_HEIGHT: Double = 50.0

/** Default [DiceView] width. */
const val DEFAULT_DICE_WIDTH: Double = 80.0

/** Default [DiceView] height. */
const val DEFAULT_DICE_HEIGHT: Double = 80.0
// endregion

// region Container
/** Default [Area] width. */
const val DEFAULT_AREA_WIDTH: Double = DEFAULT_TOKEN_WIDTH

/** Default [Area] height. */
const val DEFAULT_AREA_HEIGHT: Double = DEFAULT_TOKEN_HEIGHT

/** Default [CardStack] width. */
const val DEFAULT_CARD_STACK_WIDTH: Double = DEFAULT_CARD_WIDTH

/** Default [CardStack] height. */
const val DEFAULT_CARD_STACK_HEIGHT: Double = DEFAULT_CARD_HEIGHT

/** Default [Satchel] width. */
const val DEFAULT_SATCHEL_WIDTH: Double = DEFAULT_TOKEN_WIDTH

/** Default [Satchel] height. */
const val DEFAULT_SATCHEL_HEIGHT: Double = DEFAULT_TOKEN_HEIGHT

/** Default [LinearLayout] width. */
const val DEFAULT_LINEAR_LAYOUT_WIDTH: Double = DEFAULT_TOKEN_WIDTH

/** Default [LinearLayout] height. */
const val DEFAULT_LINEAR_LAYOUT_HEIGHT: Double = DEFAULT_TOKEN_HEIGHT

/** Default [LinearLayout] spacing. */
const val DEFAULT_LINEAR_LAYOUT_SPACING: Double = 0.0
// endregion

// region LayoutViews
/** Default [GridPane] spacing. */
const val DEFAULT_GRID_SPACING: Double = 0.0
// endregion

// region UIComponents
/** Default [Label] width. */
const val DEFAULT_LABEL_WIDTH: Double = 120.0

/** Default [Label] height. */
const val DEFAULT_LABEL_HEIGHT: Double = 30.0

/** Default [Button] width. */
const val DEFAULT_BUTTON_WIDTH: Double = 120.0

/** Default [Button] height. */
const val DEFAULT_BUTTON_HEIGHT: Double = 45.0

/** Default [ToggleButton] width. */
const val DEFAULT_TOGGLE_BUTTON_WIDTH: Double = DEFAULT_BUTTON_WIDTH

/** Default [ToggleButton] height. */
const val DEFAULT_TOGGLE_BUTTON_HEIGHT: Double = DEFAULT_BUTTON_HEIGHT

/** Default [RadioButton] width. */
const val DEFAULT_RADIO_BUTTON_WIDTH: Double = DEFAULT_BUTTON_WIDTH

/** Default [RadioButton] height. */
const val DEFAULT_RADIO_BUTTON_HEIGHT: Double = DEFAULT_BUTTON_HEIGHT

/** Default [TextField] width. */
const val DEFAULT_TEXT_FIELD_WIDTH: Double = 140.0

/** Default [TextField] height. */
const val DEFAULT_TEXT_FIELD_HEIGHT: Double = 30.0

/** Default [TextArea] width. */
const val DEFAULT_TEXT_AREA_WIDTH: Double = 200.0

/** Default [TextArea] height. */
const val DEFAULT_TEXT_AREA_HEIGHT: Double = 100.0

/** Default [ComboBox] height. */
const val DEFAULT_COMBOBOX_HEIGHT: Int = 30

/** Default [ComboBox] width. */
const val DEFAULT_COMBOBOX_WIDTH: Int = 120

/** Default [CheckBox] height. */
const val DEFAULT_CHECKBOX_HEIGHT: Int = 30

/** Default [CheckBox] width. */
const val DEFAULT_CHECKBOX_WIDTH: Int = 120

/** Default [ColorPicker] height. */
const val DEFAULT_COLOR_PICKER_HEIGHT: Int = 30

/** Default [ColorPicker] width. */
const val DEFAULT_COLOR_PICKER_WIDTH: Int = 120

/** Default [ProgressBar] height. */
const val DEFAULT_PROGRESSBAR_HEIGHT: Int = 20

/** Default [ProgressBar] width. */
const val DEFAULT_PROGRESSBAR_WIDTH: Int = 250

/** Default [ListView] width. */
const val DEFAULT_LISTVIEW_HEIGHT: Int = 400

/** Default [ListView] width. */
const val DEFAULT_LISTVIEW_WIDTH: Int = 200

/** Default [TableView] width. */
const val DEFAULT_TABLE_VIEW_WIDTH: Double = 400.0

/** Default [TableView] height. */
const val DEFAULT_TABLE_VIEW_HEIGHT: Double = 500.0
// endregion

// region Util
/** Default [Font] size. */
const val DEFAULT_FONT_SIZE: Double = 14.0
// endregion
