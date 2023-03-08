package tools.aqua.bgw.net.server.entity.observable

open class Observable {
    private val observers: MutableList<Observer> = mutableListOf()
    fun onChange(callback: (() -> Unit)) {
        observers.add(callback)
    }

    protected fun notifyChange() {
        observers.forEach { it.update() }
    }
}