package tools.aqua.bgw.application

import DialogData
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.dialog.DialogType

interface Application {
    fun start(callback : (Any) -> Unit)
    fun stop()

    fun registerEventListeners(component: ComponentView)

    fun clearAllEventListeners() {}

    fun openNewDialog(dialogData: DialogData) {}
}
