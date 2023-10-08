package tools.aqua.bgw.application

import tools.aqua.bgw.components.ComponentView

interface Application {
    fun start(callback : (Any) -> Unit)
    fun stop()

    fun registerEventListeners(component: ComponentView)

    fun clearAllEventListeners() {}
}
