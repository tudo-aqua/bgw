package tools.aqua.bgw.net.server.entity.observable


@FunctionalInterface
fun interface Observer<T> {
    fun update(value: T)
}