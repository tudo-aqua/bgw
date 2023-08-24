package tools.aqua.bgw.application

import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.event.Event
import tools.aqua.bgw.event.MouseEvent

interface Application {
    fun start(callback : (Any) -> Unit)
    fun stop()

    fun registerMouseEventListener(component: ComponentView)
}
