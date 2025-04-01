/*
 * Copyright 2022-2025 The BoardGameWork Authors
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
@GameActionClass abstract class GameAction {

    /**
     * Must be overridden to provide a string representation of the GameAction.
     * If the derived class is a data class, this method is already implemented.
     */
    abstract override fun toString(): String

    /**
     * Formats the message in a safe way.
     *
     * @return formatted message or [toString] if [formatMessage] returns `null`.
     */
    fun formatMessageSafe(): String = formatMessage() ?: toString()

    /**
     * Formats the message. Override this method to provide a formatted message.
     * If this method returns `null`, [toString] will be used instead.
     *
     * But don't rely on [toString] to format the message in a readable format.
     * Thus implement this method if you want to a properly formatted message to debug or display.
     *
     * @return formatted message or `null` if [toString] should be used.
     */
    abstract fun formatMessage(): String?
}
