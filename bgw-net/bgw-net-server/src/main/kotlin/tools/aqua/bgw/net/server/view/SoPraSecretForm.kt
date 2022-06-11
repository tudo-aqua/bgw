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
import com.vaadin.flow.component.notification.NotificationVariant
import com.vaadin.flow.component.textfield.PasswordField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import tools.aqua.bgw.net.server.entity.tables.KeyValueRepository
import tools.aqua.bgw.net.server.service.NotificationService
import tools.aqua.bgw.net.server.service.oauth.SecuredByRole

/** Layout for the secret view. */
@SecuredByRole("admin")
@Route(value = "secret", layout = MainLayout::class)
@PageTitle("BGW-Net | SoPra Secret")
class SoPraSecretForm(
    private val keyValueRepository: KeyValueRepository,
    @Autowired private val notificationService: NotificationService
) : FormLayout() {
  private var newSecret: PasswordField = PasswordField("", "Secret").apply { isRequired = true }
  private val confirmButton: Button =
      Button("Change Secret").apply {
        addThemeVariants(ButtonVariant.LUMO_PRIMARY)
        addClickListener {
          val entry = keyValueRepository.findById("SoPra Secret").get()
          keyValueRepository.save(entry.apply { value = newSecret.value })
          notificationService.notify(
              "SoPra Secret was updated successfully!", NotificationVariant.LUMO_SUCCESS)
          newSecret.clear()
        }
      }

  init {
    addClassName("secret-form")
    width = "400px"
    add(newSecret, confirmButton)
  }
}
