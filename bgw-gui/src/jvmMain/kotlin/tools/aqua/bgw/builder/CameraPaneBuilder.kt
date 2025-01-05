@file:Suppress("DuplicatedCode")

package tools.aqua.bgw.builder

import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.core.Frontend

internal object CameraPaneBuilder {
    fun build(cameraPane: CameraPane<out LayoutView<*>>) {
        cameraPane.zoomProperty.guiListener = { _, _ -> Frontend.updateComponent(cameraPane) }
        cameraPane.interactiveProperty.guiListener = { _, _ -> Frontend.updateComponent(cameraPane) }
        cameraPane.anchorPointProperty.guiListener = { _, _ -> Frontend.updateComponent(cameraPane) }
        cameraPane.panDataProperty.guiListener = { _, _ -> Frontend.updateComponent(cameraPane) }
        cameraPane.panMouseButtonProperty.guiListener = { _, _ -> Frontend.updateComponent(cameraPane) }
        ComponentViewBuilder.build(cameraPane.target)
    }
}
