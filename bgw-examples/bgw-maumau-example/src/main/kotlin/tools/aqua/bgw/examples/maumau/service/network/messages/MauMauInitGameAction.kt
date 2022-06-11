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

package tools.aqua.bgw.examples.maumau.service.network.messages

import tools.aqua.bgw.net.common.GameAction
import tools.aqua.bgw.net.common.annotations.GameActionClass

/**
 * InitGameMessage data class for serialization.
 *
 * @property hostCards [List] of host player's cards.
 * @property yourCards [List] of your cards.
 * @property drawStack [List] of draw stack cards.
 * @property gameStack The game stack card.
 */
@GameActionClass
data class MauMauInitGameAction(
    val hostCards: List<MauMauGameCard>,
    val yourCards: List<MauMauGameCard>,
    val drawStack: List<MauMauGameCard>,
    val gameStack: MauMauGameCard,
) : GameAction()
