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
            is GameComponentContainer<out DynamicComponentView> -> GameComponentContainerBuilder.build(componentView)
            is GameComponentView -> GameComponentViewBuilder.build(componentView)
            is LayoutView<out ComponentView> -> LayoutViewBuilder.build(componentView)
            is CameraPane<out LayoutView<*>> -> CameraPaneBuilder.build(componentView)
            is UIComponent -> UIComponentBuilder.build(componentView)
        }
        registerEventListeners(componentView)
        registerObservers(componentView)

        VisualBuilder.build(componentView.visual)
    }

    private fun registerEventListeners(componentView: ComponentView) {
        Frontend.applicationEngine.registerEventListeners(componentView)
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
            visualProperty.guiListener = { _, _ ->
                Frontend.updateComponent(componentView)
            }
        }
    }
}