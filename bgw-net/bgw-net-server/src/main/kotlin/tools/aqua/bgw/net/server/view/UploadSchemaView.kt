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
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import sun.jvm.hotspot.oops.CellTypeState.value
import tools.aqua.bgw.net.server.entity.tables.SchemasByGame
import tools.aqua.bgw.net.server.entity.tables.SchemasByGameRepository
import tools.aqua.bgw.net.server.service.FrontendService
import tools.aqua.bgw.net.server.service.NotificationService
import tools.aqua.bgw.net.server.service.validation.ValidationService
import tools.aqua.bgw.net.server.view.components.ValidTextField

@Route(value = "upload", layout = MainLayout::class)
@PageTitle("BGW-Net | Upload JSON Schemas")
class UploadSchemaView(
    @Autowired private val validationService: ValidationService,
    @Autowired private val frontendService: FrontendService,
    @Autowired private val notificationService: NotificationService,
    private val schemasByGameRepository: SchemasByGameRepository
) : VerticalLayout() {
    private val logger: Logger = LoggerFactory.getLogger(javaClass)
    private val buffer: MultiFileMemoryBuffer = MultiFileMemoryBuffer()

    private val upload: Upload = Upload(buffer).apply {
        addSucceededListener {
            var results: List<String> = listOf()
            val mapper = ObjectMapper()
            var schemaNode: JsonNode = mapper.createObjectNode()
            try {
                schemaNode = mapper.readTree(buffer.getInputStream(it.fileName))
                results = validationService.validateMetaSchema(schemaNode)
            } catch (_: IOException) {
                notificationService.notify(
                    "Couldn't parse JSON Schema! Please upload a .json File", NotificationVariant.LUMO_ERROR
                )
            }
            if (results.isEmpty()) {
                notificationService.notify(
                    "${it.fileName}: JSON Schema is valid!", NotificationVariant.LUMO_SUCCESS
                )
                val json = schemaNode.toString()
                if (!schemasByGameRepository.existsById(json)) {
                    schemasByGameRepository.save(SchemasByGame(gameIdSelect.value, json))
                    gameGrid.setItems(frontendService.allGameIds.map { gameId ->
                        Game(gameId, schemasByGameRepository.findAllByGameID(gameId))
                    })
                    notificationService.notify(
                        "${it.fileName}: Saved JSON Schema for game ${gameIdSelect.value}",
                        NotificationVariant.LUMO_SUCCESS
                    )
                } else {
                    notificationService.notify(
                        "${it.fileName}: JSON Schema already exists for game ${
                            schemasByGameRepository.findById(json).get().gameID
                        }", NotificationVariant.LUMO_ERROR
                    )
                }
            } else notificationService.notify(
                "${it.fileName}: Invalid JSON Schema!", NotificationVariant.LUMO_ERROR
            )
        }
    }

    private val gameIds: MutableList<String> = mutableListOf<String>().apply { addAll(frontendService.allGameIds) }

    private val gameIdField = ValidTextField("Game ID").apply {
        addValidator({ value -> !gameIds.contains(value) }, "Game ID not unique")
    }

    private val gameIdButton = Button("Add").apply {
        addClickListener {
            if (!gameIds.contains(gameIdField.value)) {
                gameIds.add(gameIdField.value)
                gameIdSelect.setItems(gameIds)
                gameIdField.reset()
            }
        }
    }

    private var gameIdSelect: Select<String> = Select<String>().apply {
        this.setItems(gameIds)
        this.label = "Game ID"
        addValueChangeListener { upload.element.setPropertyJson("files", Json.createArray()) }
    }

    private val gameGrid = Grid<Game>().apply {
        addColumn(Game::gameId).setHeader("Game ID")
        val schemaRenderer = ComponentRenderer<Grid<SchemasByGame>, Game> { game ->
            Grid<SchemasByGame>().apply {
                addColumn(ComponentRenderer { item ->
                    TextArea().apply {
                        val mapper = ObjectMapper()
                        val node = mapper.readTree(item.schema)
                        value = node.toPrettyString()
                        isReadOnly = true
                        setWidthFull()
                    }
                }).setHeader("Schemas")
                setItems(game.schemas)
            }
        }
        setItemDetailsRenderer(schemaRenderer)
        setItems(frontendService.allGameIds.map {
            Game(it, schemasByGameRepository.findAllByGameID(it))
        })
        minWidth = "400px"
    }

    init {
        add(
            VerticalLayout(
                gameGrid,
                HorizontalLayout(
                    FormLayout(gameIdField, gameIdButton).apply { maxWidth = "400px" }, VerticalLayout(
                        gameIdSelect,
                        upload,
                    )
                ),
            )
        )
    }
}

class Game(val gameId: String, val schemas: List<SchemasByGame>)
