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

package tools.aqua.bgw.examples.maumau.service.messages

import tools.aqua.bgw.examples.maumau.entity.GameActionType
import tools.aqua.bgw.examples.maumau.entity.MauMauCard
import tools.aqua.bgw.examples.maumau.service.Serialization.serialize
import tools.aqua.bgw.net.common.GameAction

/**
 * GameActionMessage data class for serialization.
 *
 * @property action Associated game action as String.
 * @property card Played card as String.
 */
@Suppress("DataClassPrivateConstructor")
data class MauMauGameAction private constructor(val action: String, val card: String) : GameAction() {

  /**
   * GameActionMessage data class for serialization.
   *
   * @param gameAction Associated game action as [GameActionType].
   * @param card Played card as [MauMauCard].
   */
  constructor(
    gameAction: GameActionType,
    card: MauMauCard? = null
  ) : this(gameAction.toString(), card?.serialize().orEmpty())
}
