package tools.aqua.bgw.builder

import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView

object CameraPaneBuilder {
    fun build(cameraPane: CameraPane<out LayoutView<*>>) {
        cameraPane.zoomProperty.guiListener = { _, _ -> Frontend.updateComponent(cameraPane) }
        cameraPane.interactiveProperty.guiListener = { _, _ -> Frontend.updateComponent(cameraPane) }
        cameraPane.anchorPointProperty.guiListener = { _, _ -> Frontend.updateComponent(cameraPane) }
        ComponentViewBuilder.build(cameraPane.target)
    }

}
