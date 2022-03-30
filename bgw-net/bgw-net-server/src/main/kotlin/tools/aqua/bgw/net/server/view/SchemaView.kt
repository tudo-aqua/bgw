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

import com.fasterxml.jackson.core.exc.StreamReadException
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.messages.MessageList
import com.vaadin.flow.component.messages.MessageListItem
import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MemoryBuffer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import tools.aqua.bgw.net.server.service.validation.ValidationService

/**
 * Layout for the schema view.
 *
 * @property validationService Auto-Wired [ValidationService].
 */
@Route(value = "schema", layout = MainLayout::class)
@PageTitle("BGW-Net | JSON Schemas")
@CssImport(value = "./styles/message.css", themeFor = "vaadin-message")
class SchemaView(@Autowired private val validationService: ValidationService) : VerticalLayout() {
  private val buffer: MemoryBuffer = MemoryBuffer()
  private val upload: Upload = Upload(buffer)
  private val msgList = MessageList()

  init {
    configureUpload()
    add(VerticalLayout(upload, msgList))
  }

  private fun notify(msg: String, variant: NotificationVariant = NotificationVariant.LUMO_PRIMARY) {
    val notification = Notification(msg, 5000, Notification.Position.TOP_CENTER)
    notification.addThemeVariants(variant)
    notification.open()
  }

  private fun configureUpload() {
    upload.addSucceededListener {
      val fileName = it.fileName
      val inputStream = buffer.inputStream

      var results: List<String> = listOf()

      try {
        val mapper = ObjectMapper()
        val schemaNode: JsonNode = mapper.readTree(inputStream)
        results = validationService.validateMetaSchema(schemaNode)
      } catch (_: StreamReadException) {
        notify(
            "Couldn't parse JSON Schema! Please upload a .json File",
            NotificationVariant.LUMO_ERROR)
      }

      msgList.setItems(results.map { result -> MessageListItem(result) })

      if (results.isEmpty())
          notify("$fileName: JSON Schema is valid!", NotificationVariant.LUMO_SUCCESS)
      else notify("$fileName: Invalid JSON Schema!", NotificationVariant.LUMO_ERROR)
    }
  }
}
