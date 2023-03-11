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

import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.messages.MessageList
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
import tools.aqua.bgw.net.server.view.components.editor.JsonEditor
import tools.aqua.bgw.net.server.view.components.editor.JsonFile

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
    private val schemaEditor = JsonEditor(validationService, notificationService).apply {
        width = "50%"
        height = "450px"
    }
    private val objectEditor = JsonEditor(validationService, notificationService).apply {
        width = "50%"
        height = "450px"
    }

    init {
        configureUpload()
        add(
            VerticalLayout(upload, HorizontalLayout(schemaEditor, objectEditor).apply { setWidthFull() }),
            msgList.apply { height = "500px" })
    }

    private fun configureUpload() {
        upload.addSucceededListener {
            val fileName = it.fileName
            val inputStream = buffer.inputStream
            val results = schemaEditor.validateSchema(JsonFile(inputStream))
            if (results.isEmpty()) notificationService.notify(
                "$fileName: JSON Schema is valid!", NotificationVariant.LUMO_SUCCESS
            )
            else notificationService.notify(
                "$fileName: Invalid JSON Schema!", NotificationVariant.LUMO_ERROR
            )
        }
    }
}