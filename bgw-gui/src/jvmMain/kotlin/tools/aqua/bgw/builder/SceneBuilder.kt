package tools.aqua.bgw.builder

import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.core.MenuScene

internal object SceneBuilder {
    fun build(boardGameScene: BoardGameScene) {
        boardGameScene.lockedProperty.guiListener = { _, _ -> Frontend.updateScene() }
        boardGameScene.internalLockedProperty.guiListener = { _, _ -> Frontend.updateScene() }
        boardGameScene.rootComponents.guiListener = { _, _ -> Frontend.updateScene() }
        boardGameScene.components.forEach { ComponentViewBuilder.build(it) }
    }

    fun build(menuScene: MenuScene) {
        menuScene.rootComponents.guiListener = { _, _ -> Frontend.updateScene() }
        menuScene.components.forEach { ComponentViewBuilder.build(it) }
    }
}
