/*
 * Copyright 2025 The BoardGameWork Authors
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

@file:Suppress("DuplicatedCode")

package tools.aqua.bgw.builder

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.Frontend

internal object ComponentViewBuilder {
  fun build(componentView: ComponentView) {

    when (componentView) {
      is GameComponentContainer<out DynamicComponentView> ->
          GameComponentContainerBuilder.build(componentView)
      is GameComponentView -> GameComponentViewBuilder.build(componentView)
      is LayoutView<out ComponentView> -> LayoutViewBuilder.build(componentView)
      is CameraPane<out LayoutView<*>> -> CameraPaneBuilder.build(componentView)
      is UIComponent -> UIComponentBuilder.build(componentView)
    }
    registerObservers(componentView)

    VisualBuilder.build(componentView.visual)
  }

  @Suppress("DuplicatedCode")
  private fun registerObservers(componentView: ComponentView) {
    with(componentView) {
      zIndexProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      posXProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      posYProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      scaleXProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      scaleYProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      rotationProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      opacityProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      heightProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      widthProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      isVisibleProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      isDisabledProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      isFocusableProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      visualProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }

      dropAcceptorProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      onMouseEnteredProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
      onMouseExitedProperty.guiListener = { _, _ -> Frontend.updateComponent(componentView) }
    }
  }
}
