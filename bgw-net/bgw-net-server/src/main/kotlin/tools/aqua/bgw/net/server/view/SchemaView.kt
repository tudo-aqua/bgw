/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

import com.fasterxml.jackson.core.exc.StreamReadException
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.json.JsonMapper
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.messages.MessageList
import com.vaadin.flow.component.messages.MessageListItem
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import tools.aqua.bgw.net.server.service.NotificationService
import tools.aqua.bgw.net.server.service.validation.ValidationService
import tools.aqua.bgw.net.server.view.components.JsonEditor
import java.io.InputStream

/**
 * Layout for the schema view.
 *
 * @property validationService Auto-Wired [ValidationService].
 * @property notificationService Auto-Wired [NotificationService].
 */
@Route(value = "schema", layout = MainLayout::class)
@PageTitle("BGW-Net | Validate Schema")
@CssImport(value = "./styles/message.css", themeFor = "vaadin-message")
class SchemaView(
    @Autowired private val validationService: ValidationService,
    @Autowired private val notificationService: NotificationService
) : VerticalLayout() {
    private val buffer: MemoryBuffer = MemoryBuffer()
    private val upload: Upload = Upload(buffer)
    private val msgList = MessageList()
    private val schemaEditor = JsonEditor().apply {
        width = "50%"
        height = "450px"
    }
    private val objectEditor = JsonEditor().apply {
        width = "50%"
        height = "450px"
    }
    private val mapper = JsonMapper.builder().enable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS).build()

    init {
        configureUpload()
        add(
            VerticalLayout(upload, HorizontalLayout(schemaEditor, objectEditor).apply { setWidthFull() }),
            msgList.apply { height = "500px" })
        schemaEditor.addTextListener {
            validateSchema(Json.JsonString(it))
        }
    }

    private fun validateSchema(input: Json): List<String> {
        var results: List<String> = listOf()
        val schemaNode: JsonNode
        try {
            schemaNode = mapper.readTree(input)
            if (input is Json.JsonFile) {
                schemaEditor.setValueSilent(
                    mapper.writerWithDefaultPrettyPrinter().writeValueAsString(schemaNode)
                )
            }
            results = validationService.validateMetaSchema(schemaNode)
        } catch (e: StreamReadException) {
            when (input) {
                //is Json.JsonString -> schemaEditor.setValueSilent(input.value)
                is Json.JsonString -> results = listOf(e.originalMessage)
                is Json.JsonFile -> notificationService.notify(
                    "Couldn't parse JSON Schema! Please upload a .json File", NotificationVariant.LUMO_ERROR
                )
            }
        }
        msgList.setItems(results.map { result -> MessageListItem(result) })
        return results
    }

    private fun configureUpload() {
        upload.addSucceededListener {
            val fileName = it.fileName
            val inputStream = buffer.inputStream
            val results = validateSchema(Json.JsonFile(inputStream))
            if (results.isEmpty()) notificationService.notify(
                "$fileName: JSON Schema is valid!", NotificationVariant.LUMO_SUCCESS
            )
            else notificationService.notify(
                "$fileName: Invalid JSON Schema!", NotificationVariant.LUMO_ERROR
            )
        }
    }
}

private fun ObjectMapper.readTree(input: Json): JsonNode = when (input) {
    is Json.JsonString -> readTree(input)
    is Json.JsonFile -> readTree(input)
}

private fun ObjectMapper.readTree(input: Json.JsonString): JsonNode = this.readTree(input.value)
private fun ObjectMapper.readTree(input: Json.JsonFile): JsonNode = this.readTree(input.inputStream)
sealed class Json {
    class JsonString(val value: String) : Json()
    class JsonFile(val inputStream: InputStream) : Json()
}
