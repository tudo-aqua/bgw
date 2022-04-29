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
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import tools.aqua.bgw.net.server.service.oauth.SecuredByRole

/** Layout for the secret view. */
@SecuredByRole("admin")
@Route(value = "secret", layout = MainLayout::class)
@PageTitle("BGW-Net | SoPra Secret")
class SoPraSecretForm : FormLayout() {
  private var oldSecret: PasswordField =
      PasswordField("", "Current Secret").apply { isRequired = true }
  private var newSecret: PasswordField = PasswordField("", "Secret")
  private var confirmNewSecret: PasswordField = PasswordField("", "Confirm Secret")
  private val confirmButton: Button =
      Button("Change Secret").apply {
        addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        addClickListener {}
      }

  private val binder: Binder<String> = Binder()
  private var oldSecretValue: String = ""

  fun updateButton() {}

  fun configureForm() {
    oldSecret.addValueChangeListener { !it.hasValue.isEmpty.also { confirmButton.isEnabled = it } }
  }

  fun initBinder() {
    binder
        .forField(oldSecret)
        .asRequired()
        .bind({ this.oldSecretValue }, { _, v -> this.oldSecretValue = v })
  }

  init {
    initBinder()
    addClassName("secret-form")
    width = "400px"
    add(oldSecret, newSecret, confirmNewSecret, confirmButton)
  }
}
