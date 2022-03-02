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
import com.networknt.schema.JsonSchema
import com.networknt.schema.JsonSchemaFactory
import com.networknt.schema.SpecVersion
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.upload.Upload
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.slf4j.LoggerFactory
import tools.aqua.bgw.net.server.META_SCHEMA_JSON_URL_STRING


@Route(value = "schema", layout = MainLayout::class)
@PageTitle("BGW-Net | JSON Schemas")
class SchemaView(
    //@Autowired private val frontendService: FrontendService,
    //@Autowired private val validationService: ValidationService
) : VerticalLayout() {
    private val buffer: MultiFileMemoryBuffer = MultiFileMemoryBuffer()
    private val upload: Upload = Upload(buffer)

    private val logger = LoggerFactory.getLogger(javaClass)

    init {
        upload.addSucceededListener {
            val fileName = it.fileName
            val inputStream = buffer.getInputStream(fileName)

            // val s: Scanner = Scanner(inputStream).useDelimiter("\\A")
            // val result = if (s.hasNext()) s.next() else ""

            val json: String = javaClass.getResource(META_SCHEMA_JSON_URL_STRING)!!.readText()
            //
            val metaSchema: JsonSchema =
                JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V7).getSchema(json)

            val mapper = ObjectMapper()
            val schemaNode: JsonNode = mapper.readTree(inputStream)

            val results = metaSchema.validate(schemaNode)

            logger.info("Size=${results.size}")
            logger.info("Donezo")

            // Do something with the file data
            // processFile(inputStream, fileName)
        }

        width = "400px"
        add(upload)
    }
}
