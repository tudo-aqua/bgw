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

import com.vaadin.flow.component.UI
import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.icon.Icon
import com.vaadin.flow.component.icon.VaadinIcon
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.dom.ThemeList
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo

/** Layout for the main view. */
@CssImport("./styles/styles.css")
@Theme(Lumo::class, variant = Lumo.LIGHT)
class MainLayout : AppLayout() {
  init {
    createHeader()
    createDrawer()
    createToggleButton()
  }

  private fun createToggleButton() {
    val toggleButton =
        Button(Icon(VaadinIcon.ADJUST)) {
          val themeList: ThemeList = UI.getCurrent().element.themeList
          if (themeList.contains(Lumo.DARK)) {
            themeList.remove(Lumo.DARK)
          } else {
            themeList.add(Lumo.DARK)
          }
        }
    toggleButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY)
    addToNavbar(toggleButton)
  }

  private fun createHeader() {
    val logo = H1("BGW Net")
    logo.addClassName("logo")
    val header = HorizontalLayout(DrawerToggle(), logo)
    header.width = "100%"
    header.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
    addToNavbar(header)
  }

  private fun createDrawer() {
    val connectionsLink =
        RouterLink("Connections and Sessions", ConnectionsView::class.java).apply {
          highlightCondition = HighlightConditions.sameLocation()
        }
    val secretLink =
        RouterLink("SoPra Secret", SecretForm::class.java).apply {
          highlightCondition = HighlightConditions.sameLocation()
        }
    val schemaLink =
        RouterLink("Validate Schema", SchemaView::class.java).apply {
          highlightCondition = HighlightConditions.sameLocation()
        }
    val uploadLink =
        RouterLink("Games and Schemas", UploadSchemaView::class.java).apply {
          highlightCondition = HighlightConditions.sameLocation()
        }

    addToDrawer(VerticalLayout(connectionsLink, secretLink, schemaLink, uploadLink))
  }
}
