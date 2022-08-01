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

package tools.aqua.bgw.net.common

import tools.aqua.bgw.net.common.annotations.GameActionClass
import tools.aqua.bgw.net.common.annotations.GameActionReceiver

/**
 * Baseclass for all Game action classes.
 *
 * Extend this class in order to create data classes that can be sent by a BoardGameClient. All
 * implementations additionally need the [GameActionClass] annotation to work properly.
 *
 * @see GameActionReceiver
 */
@GameActionClass
abstract class GameAction {
  abstract fun printToString(): String
}
