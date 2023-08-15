package tools.aqua.bgw.examples.performance.view

import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.examples.performance.view.scenes.CameraPaneScene
import tools.aqua.bgw.examples.performance.view.scenes.PerformanceMenuScene

class PerformanceViewController : BoardGameApplication(windowTitle = "Performance Test") {
    /** The main menu scene. */
    val performanceMenuScene: PerformanceMenuScene = PerformanceMenuScene(this)
    val cameraPaneScene: CameraPaneScene = CameraPaneScene(this)

    init {
        showMenuScene(performanceMenuScene)
        show()
    }
}