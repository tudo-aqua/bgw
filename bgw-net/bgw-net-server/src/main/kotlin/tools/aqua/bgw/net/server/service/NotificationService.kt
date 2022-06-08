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

package tools.aqua.bgw.net.server.service

import com.vaadin.flow.component.notification.Notification
import com.vaadin.flow.component.notification.NotificationVariant
import org.springframework.stereotype.Service

/** The [NotificationService] is responsible for notifying the user in the UI about messages. */
@Service
class NotificationService {
  /** Opens a notification snackbar displaying a certain message. */
  fun notify(msg: String, variant: NotificationVariant = NotificationVariant.LUMO_PRIMARY) {
    val notification = Notification(msg, 5000, Notification.Position.TOP_CENTER)
    notification.addThemeVariants(variant)
    notification.open()
  }
}
