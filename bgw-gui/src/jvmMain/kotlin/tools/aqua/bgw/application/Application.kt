package tools.aqua.bgw.application

import DialogData
import tools.aqua.bgw.components.ComponentView

internal interface Application {
    fun start(callback: (Any) -> Unit)
    fun stop()

    fun registerEventListeners(component: ComponentView)

    fun clearAllEventListeners() {}

    fun openNewDialog(dialogData: DialogData) {}
}
