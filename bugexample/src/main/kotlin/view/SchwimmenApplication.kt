package view

import tools.aqua.bgw.core.BoardGameApplication

class SchwimmenApplication : BoardGameApplication("Schwimmen") {
    private val testScene = TestScene()
    private val testSceneGrid = TestSceneGrid()
    private val testSceneGrid2 = TestSceneGrid2()

    init {
        //showGameScene(testScene)
        //showGameScene(testSceneGrid)
        showGameScene(testSceneGrid2)
    }
}