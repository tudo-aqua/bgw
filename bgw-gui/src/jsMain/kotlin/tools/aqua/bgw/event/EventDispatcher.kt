package tools.aqua.bgw.event

interface EventDispatcher {
    fun dispatchEvent(event: Event)
}