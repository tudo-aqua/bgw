package tools.aqua.bgw.builder

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.BoardGameScene

object SceneBuilder {
    fun build(boardGameScene: BoardGameScene) {
        registerEventListeners(boardGameScene)
    }

    private fun registerEventListeners(boardGameScene: BoardGameScene) {
        boardGameScene.components.forEach {
            registerEventListeners(it)
        }
    }

    private fun registerEventListeners(componentView: ComponentView) {
        Frontend.applicationEngine.registerMouseEventListener(componentView)
        when(componentView) {
            is CameraPane<*> -> registerEventListeners(componentView.target)
            is Pane<*> -> componentView.components.forEach { component ->
                registerEventListeners(component)
            }
        }
    }

}
