package tools.aqua.bgw.net.server.entity.observable

open class Observable<T>(initialValue: T) {
    protected var value: T = initialValue
        set(value) {
            field = value
            notifyChange()
        }
    private val observers: MutableList<Observer<T>> = mutableListOf()
    fun onChange(callback: ((T) -> Unit)) {
        observers.add(callback)
    }

    private fun notifyChange() {
        observers.forEach { it.update(value) }
    }
}