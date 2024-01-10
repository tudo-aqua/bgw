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

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.select.Select
import com.vaadin.flow.component.textfield.TextArea
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import elemental.json.Json
import java.io.IOException
import org.springframework.beans.factory.annotation.Autowired
import tools.aqua.bgw.net.server.entity.tables.SchemasByGame
import tools.aqua.bgw.net.server.entity.tables.SchemasByGameRepository
import tools.aqua.bgw.net.server.service.FrontendService
import tools.aqua.bgw.net.server.service.NotificationService
import tools.aqua.bgw.net.server.service.oauth.SecuredByRole
import tools.aqua.bgw.net.server.service.validation.ValidationService
import tools.aqua.bgw.net.server.view.components.ValidTextField

/**
 * Layout for the upload view.
 *
 * @property validationService Auto-Wired [ValidationService].
 * @property frontendService Auto-Wired [FrontendService].
 * @property notificationService Auto-Wired [NotificationService].
 * @property schemasByGameRepository injected [SchemasByGameRepository].
 */
@SecuredByRole("admin")
@Route(value = "upload", layout = MainLayout::class)
@PageTitle("BGW-Net | Games and Schemas")
class UploadSchemaView(
    @Autowired private val validationService: ValidationService,
    @Autowired private val frontendService: FrontendService,
    @Autowired private val notificationService: NotificationService,
    private val schemasByGameRepository: SchemasByGameRepository
) : VerticalLayout() {
  private val buffer: MultiFileMemoryBuffer = MultiFileMemoryBuffer()

  private var selectedSchemas: Set<SchemasByGame> = HashSet()

  private val deleteButton: Button =
      Button("Delete").apply {
        addClickListener {
          for (selected in selectedSchemas) {
            schemasByGameRepository.delete(selected)
            gameGrid.setItems(
                frontendService.allGameIds.map { gameId ->
                  Game(gameId, schemasByGameRepository.findAllByGameID(gameId))
                })
            validationService.flushSchemaCache()
            notificationService.notify("Schema was deleted!", NotificationVariant.LUMO_SUCCESS)
          }
        }
      }

  private val gameIdLabel = "Game ID"

  private val upload: Upload =
      Upload(buffer).apply {
        addSucceededListener {
          val results: List<String>
          val mapper = ObjectMapper()
          val schemaNode: JsonNode
          try {
            schemaNode = mapper.readTree(buffer.getInputStream(it.fileName))
            results = validationService.validateMetaSchema(schemaNode)
          } catch (_: IOException) {
            notificationService.notify(
                "Couldn't parse JSON Schema! Please upload a .json File",
                NotificationVariant.LUMO_ERROR)
            return@addSucceededListener
          }
          if (results.isEmpty()) {
            notificationService.notify(
                "${it.fileName}: JSON Schema is valid!", NotificationVariant.LUMO_SUCCESS)
            val json = schemaNode.toString()
            if (!schemasByGameRepository.existsBySchemaAndGameID(json, gameIdSelect.value)) {
              schemasByGameRepository.save(SchemasByGame(gameIdSelect.value, json))
              gameGrid.setItems(
                  frontendService.allGameIds.map { gameId ->
                    Game(gameId, schemasByGameRepository.findAllByGameID(gameId))
                  })
              notificationService.notify(
                  "${it.fileName}: Saved JSON Schema for game ${gameIdSelect.value}",
                  NotificationVariant.LUMO_SUCCESS)
            } else {
              notificationService.notify(
                  "${it.fileName}: JSON Schema already exists for game ${gameIdSelect.value}",
                  NotificationVariant.LUMO_ERROR)
            }
          } else
              notificationService.notify(
                  "${it.fileName}: Invalid JSON Schema!", NotificationVariant.LUMO_ERROR)
        }
      }

  private val gameIds: MutableList<String> =
      mutableListOf<String>().apply { addAll(frontendService.allGameIds) }

  private val gameIdField =
      ValidTextField(gameIdLabel).apply {
        addValidator({ value -> !gameIds.contains(value) }, "Game already exists")
      }

  private val gameIdButton =
      Button("Add").apply {
        addClickListener {
          if (!gameIds.contains(gameIdField.value)) {
            gameIds.add(gameIdField.value)
            gameIdSelect.setItems(gameIds)
            gameIdField.reset()
          }
        }
      }

  private var gameIdSelect: Select<String> =
      Select<String>().apply {
        this.setItems(gameIds)
        this.label = gameIdLabel
        addValueChangeListener { upload.element.setPropertyJson("files", Json.createArray()) }
      }

  private val gameGrid =
      Grid<Game>().apply {
        addColumn(Game::gameId).setHeader(gameIdLabel)
        val schemaRenderer =
            ComponentRenderer<Grid<SchemasByGame>, Game> { game ->
              Grid<SchemasByGame>().apply {
                addColumn(
                    ComponentRenderer { item ->
                      TextArea().apply {
                        val mapper = ObjectMapper()
                        val node = mapper.readTree(item.schema)
                        value = node.toPrettyString()
                        isReadOnly = true
                        setWidthFull()
                      }
                    })
                setItems(game.schemas)
                addSelectionListener { selectedSchemas = it.allSelectedItems }
              }
            }
        setItemDetailsRenderer(schemaRenderer)
        setItems(
            frontendService.allGameIds.map {
              Game(it, schemasByGameRepository.findAllByGameID(it))
            })
        minWidth = "400px"
      }

  init {
    add(
        VerticalLayout(
            gameGrid,
            HorizontalLayout(
                FormLayout(gameIdField, gameIdButton).apply { maxWidth = "400px" },
                VerticalLayout(
                    gameIdSelect,
                    upload,
                ),
                deleteButton.apply { width = "150px" }),
        ))
  }
}
