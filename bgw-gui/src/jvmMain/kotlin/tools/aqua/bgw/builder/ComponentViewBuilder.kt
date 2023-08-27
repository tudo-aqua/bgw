package tools.aqua.bgw.builder

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.DynamicComponentView
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.gamecomponentviews.GameComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.uicomponents.UIComponent

object ComponentViewBuilder {
    fun build(componentView: ComponentView) {
        when(componentView) {
            is GameComponentContainer<out DynamicComponentView> -> GameComponentContainerBuilder.build(componentView)
            is GameComponentView -> GameComponentViewBuilder.build(componentView)
            is LayoutView<out ComponentView> -> LayoutViewBuilder.build(componentView)
            is CameraPane<out LayoutView<*>> -> CameraPaneBuilder.build(componentView)
            is UIComponent -> UIComponentBuilder.build(componentView)
        }
        registerEventListeners(componentView)
        registerObservers(componentView)
    }

    private fun registerEventListeners(componentView: ComponentView) {
        Frontend.applicationEngine.registerEventListeners(componentView)
    }

    @Suppress("DuplicatedCode")
    private fun registerObservers(componentView: ComponentView) {
        with(componentView) {
            zIndexProperty.guiListener = { _, _ -> Frontend.updateScene() }
            posXProperty.guiListener = { _, _ -> Frontend.updateScene() }
            posYProperty.guiListener = { _, _ -> Frontend.updateScene() }
            scaleXProperty.guiListener = { _, _ -> Frontend.updateScene() }
            scaleYProperty.guiListener = { _, _ -> Frontend.updateScene() }
            rotationProperty.guiListener = { _, _ -> Frontend.updateScene() }
            opacityProperty.guiListener = { _, _ -> Frontend.updateScene() }
            heightProperty.guiListener = { _, _ -> Frontend.updateScene() }
            widthProperty.guiListener = { _, _ -> Frontend.updateScene() }
            isVisibleProperty.guiListener = { _, _ -> Frontend.updateScene() }
            isDisabledProperty.guiListener = { _, _ -> Frontend.updateScene() }
            isFocusableProperty.guiListener = { _, _ -> Frontend.updateScene() }
            visualProperty.guiListener = { _, _ -> Frontend.updateScene() }
        }
    }
}