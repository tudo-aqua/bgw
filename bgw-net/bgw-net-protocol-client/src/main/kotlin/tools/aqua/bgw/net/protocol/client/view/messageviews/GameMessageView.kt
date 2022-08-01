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

package tools.aqua.bgw.net.protocol.client.view.messageviews

import java.awt.Color
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.util.Font

class GameMessageView(
    timestamp: String,
    player: String,
    playerNameColor: Color,
    messageType: String,
    text: List<String>,
) : MessageView() {

  private val timestampLineHeight: Double = 10.0
  private val playerNameLineHeight: Double = 25.0
  private val messageLineHeight: Double = 20.0
  private val marginTop: Double = 10.0
  private val marginLeft: Double = 20.0
  private val marginRight: Double = 10.0
  private val marginBottom: Double = 0.0

  private val colorStyle = "-fx-background-color: #2969c0;"

  init {
    height = marginTop

    // Add timestamp
    add(
        Label(
            posX = marginLeft,
            posY = height,
            width = width - marginLeft - marginRight,
            height = timestampLineHeight,
            text = timestamp,
            font =
                Font(
                    size = 10,
                    color = Color.DARK_GRAY,
                    fontWeight = Font.FontWeight.LIGHT,
                    fontStyle = Font.FontStyle.ITALIC),
            alignment = Alignment.TOP_RIGHT))
    height += timestampLineHeight

    // Add player name
    add(
        Label(
            posX = marginLeft,
            posY = height,
            width = width - marginLeft - marginRight,
            height = playerNameLineHeight,
            text = player,
            font = Font(size = 14, color = playerNameColor, fontWeight = Font.FontWeight.BOLD),
            alignment = Alignment.TOP_LEFT))
    height += playerNameLineHeight

    // Add message type
    add(
        Label(
            posX = marginLeft,
            posY = height,
            width = width - marginLeft - marginRight,
            height = messageLineHeight + 10,
            text = messageType,
            font = Font(size = 16, color = playerNameColor, fontWeight = Font.FontWeight.BOLD),
            alignment = Alignment.TOP_CENTER))
    height += messageLineHeight + 10

    // Add text lines
    for (i in text.indices) {
      add(
          Label(
              posX = marginLeft,
              posY = height,
              width = width - marginLeft - marginRight,
              height = messageLineHeight,
              text = text[i],
              font = textFont,
              alignment = Alignment.TOP_LEFT))
      height += messageLineHeight
    }

    height += marginBottom

    // Add background
    add(
        Label(posX = 0, posY = 0, width = width, height = height).apply {
          backgroundStyle = "$colorStyle$cornerStyle"
        },
        0)
  }

  companion object {
    internal val textFont: Font = Font(size = 12, color = Color.WHITE)
  }
}
