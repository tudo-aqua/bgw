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

import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

/** Layout for the secret view. */
@Route(value = "secret", layout = MainLayout::class)
@PageTitle("BGW-Net | Secret")
class SecretForm : FormLayout() {
  private val formName = "secret-form"
  private val oldSecret: Label = Label("Old Secret").apply { addClassName(formName) }
  private val newSecret: TextField = TextField("New Secret").apply { addClassName(formName) }

  private val save: Button =
      Button("Save").apply {
        addThemeVariants(ButtonVariant.MATERIAL_OUTLINED)
        addClassName(formName)
      }

  init {
    addClassName(formName)
    width = "400px"
    add(oldSecret, newSecret, save)
  }
}
