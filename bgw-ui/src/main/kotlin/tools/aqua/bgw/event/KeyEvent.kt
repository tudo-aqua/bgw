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

@file:Suppress("unused")

package tools.aqua.bgw.event

/**
 * [Event] that gets raised for key inputs.
 *
 * @constructor Creates a [KeyEvent].
 *
 * @param keyCode Corresponding key code enum value.
 * @param character Corresponding character string.
 * @param controlDown Whether control key was pressed.
 * @param shiftDown Whether shift key was pressed.
 * @param altDown Whether alt key was pressed.
 */
class KeyEvent(
	val keyCode: KeyCode = KeyCode.UNDEFINED,
	val character: String = "",
	val controlDown: Boolean,
	val shiftDown: Boolean,
	val altDown: Boolean
) : InputEvent()
