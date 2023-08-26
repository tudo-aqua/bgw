package tools.aqua.bgw.builder

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.UIComponent
import tools.aqua.bgw.core.BoardGameScene

object SceneBuilder {
    fun build(boardGameScene: BoardGameScene) {
        registerEventListeners(boardGameScene)
        registerPropertyObservers(boardGameScene)
    }

    private fun registerPropertyObservers(boardGameScene: BoardGameScene) {
        boardGameScene.components.forEach {
            registerPropertyObservers(it)
        }
    }

    private fun registerPropertyObservers(componentView: ComponentView) {
        when(componentView) {
            is Button -> {
                println("Registering property observers")
                componentView.visualProperty.guiListener = { _, nV ->
                    //TODO: Only update visual of this specific component
                    //Frontend.applicationEngine.updateComponent(componentView)
                    println("Visual changed")
                    Frontend.updateScene()
                }
            }
            is CameraPane<*> -> registerPropertyObservers(componentView.target)
            is Pane<*> -> componentView.components.forEach { component ->
                registerPropertyObservers(component)
            }
        }
    }

    private fun registerEventListeners(boardGameScene: BoardGameScene) {
        println("Registering event listeners")
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
