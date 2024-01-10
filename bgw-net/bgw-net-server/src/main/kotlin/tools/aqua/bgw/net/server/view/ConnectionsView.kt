/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

package tools.aqua.bgw.net.server.view

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import tools.aqua.bgw.net.server.entity.GameInstance
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.service.FrontendService
import tools.aqua.bgw.net.server.service.oauth.SecuredByRole

/**
 * Layout for the connections view.
 *
 * @property frontendService Auto-Wired [FrontendService].
 */
@Route(value = "", layout = MainLayout::class)
@PageTitle("BGW-Net | Active Connections")
@SecuredByRole("admin", "tutor")
class ConnectionsView(@Autowired private val frontendService: FrontendService) : VerticalLayout() {
  private val playerGrid =
      Grid<Player>().apply {
        addColumn(Player::name).setHeader("Name")
        addColumn { it.session.remoteAddress?.port ?: "n/a" }.setHeader("Socket Port")
        addColumn { "${it.game?.gameID}:${it.game?.sessionID}" ?: "n/a" }
            .setHeader("Connected Game")
        setItems(frontendService.activePlayers)
        minWidth = "500px"
      }

  private val gameGrid =
      Grid<GameInstance>().apply {
        addColumn(GameInstance::gameID).setHeader("Game ID")
        addColumn(GameInstance::sessionID).setHeader("Session ID")
        val playerRenderer =
            ComponentRenderer<Grid<Player>, GameInstance> { game ->
              Grid<Player>().apply {
                addColumn(Player::name).setHeader("Name")
                addColumn { it.session.remoteAddress?.address?.hostAddress ?: "n/a" }
                    .setHeader("IP Address")
                setItems(game.players)
              }
            }
        setItemDetailsRenderer(playerRenderer)
        setItems(frontendService.activeGames)
        minWidth = "400px"
      }

  init {
    width = "100%"
    height = "100%"
    add(HorizontalLayout(playerGrid, gameGrid))
  }
}
