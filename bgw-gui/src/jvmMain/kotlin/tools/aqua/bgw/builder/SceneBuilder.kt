package tools.aqua.bgw.builder

import tools.aqua.bgw.core.BoardGameScene

object SceneBuilder {
    fun build(boardGameScene: BoardGameScene) {
        boardGameScene.lockedProperty.guiListener = { _, _ -> Frontend.updateScene() }
        boardGameScene.internalLockedProperty.guiListener = { _, _ -> Frontend.updateScene() }
        boardGameScene.rootComponents.guiListener = { _, _ -> Frontend.updateScene() }
        boardGameScene.components.forEach { ComponentViewBuilder.build(it) }
    }
}
