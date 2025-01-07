/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.builder

import AreaData
import CardStackData
import GameComponentContainerData
import LinearLayoutData
import SatchelData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.container.Area as ReactArea
import tools.aqua.bgw.elements.container.CardStack as ReactCardStack
import tools.aqua.bgw.elements.container.LinearLayout as ReactLinearLayout
import tools.aqua.bgw.elements.container.Satchel as ReactSatchel

internal object ContainerBuilder {
  fun build(containerViewData: GameComponentContainerData): ReactElement<*> {
    return when (containerViewData) {
      is AreaData -> ReactArea.create { data = containerViewData }
      is LinearLayoutData -> ReactLinearLayout.create { data = containerViewData }
      is CardStackData -> ReactCardStack.create { data = containerViewData }
      is SatchelData -> ReactSatchel.create { data = containerViewData }
      else ->
          throw IllegalArgumentException(
              "Unknown component type: ${containerViewData::class.simpleName}")
    }
  }
}
