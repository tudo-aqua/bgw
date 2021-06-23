package tools.aqua.bgw.observable

/**
 * Observable interface for observable properties.
 */
@FunctionalInterface
fun interface IObservable {
	/**
	 * Indicates property update.
	 */
	fun update()
}

/**
 * Observable interface for observable properties with values.
 */
@FunctionalInterface
fun interface IValueObservable<T> {
	/**
	 * Indicates property update.
	 *
	 * @param oldValue Old value of property.
	 * @param newValue New value of property.
	 */
	fun update(oldValue: T, newValue: T)
}

/**
 * Basic observable.
 */
open class Observable {
	/**
	 * Holds all listeners on this property.
	 */
	private val listeners: MutableList<IObservable> = mutableListOf()
	
	/**
	 * Used by renderer to listen on important properties for visualization.
	 */
	private var guiListenerHandler: IObservable? = null
	
	/**
	 * Used by renderer to listen on important properties for visualization.
	 */
	internal var guiListener: (() -> Unit)? = null
		set(value) {
			guiListenerHandler = if (value == null) null else IObservable(value)
		}
	
	/**
	 * Used by BGW framework containers to manage children.
	 * Should only be set by direct parent.
	 */
	private var internalListenerHandler: IObservable? = null
	
	/**
	 * Used by BGW framework containers to manage children.
	 * Should only be set by direct parent.
	 */
	internal var internalListener: (() -> Unit)? = null
		set(value) {
			internalListenerHandler = if (value == null) null else IObservable(value)
		}
	
	/**
	 * Sets GUI listener and calls update().
	 *
	 * @param listener Listener to add and notify.
	 */
	internal fun setGUIListenerAndInvoke(listener: () -> Unit) {
		guiListener = listener
		guiListenerHandler?.update()
	}
	
	/**
	 * Sets internal listener and calls update().
	 *
	 * @param listener Listener to add and notify.
	 */
	internal fun setInternalListenerAndInvoke(listener: () -> Unit) {
		internalListener = listener
		internalListenerHandler?.update()
	}
	
	/**
	 * Adds a listener and calls update() on this new listener.
	 *
	 * @param listener Listener to add and notify.
	 */
	fun addListenerAndInvoke(listener: IObservable) {
		listeners.add(listener)
		listener.update()
	}
	
	/**
	 * Adds a listener silently.
	 *
	 * @param listener Listener to add
	 */
	fun addListener(listener: IObservable) {
		listeners.add(listener)
	}
	
	/**
	 * Removes a listener.
	 *
	 * @param listener Listener to remove.
	 *
	 * @return `true` if the listener has been successfully remove, `false` if it was not found.
	 */
	fun removeListener(listener: IObservable) = listeners.remove(listener)
	
	/**
	 * Removes all listeners.
	 */
	fun clearListeners() {
		listeners.clear()
	}
	
	/**
	 * Notifies GUI listener by calling update().
	 */
	internal fun notifyGUIListener() {
		guiListenerHandler?.update()
	}
	
	/**
	 * Notifies internal listener by calling update().
	 */
	internal fun notifyInternalListener() {
		internalListenerHandler?.update()
	}
	
	/**
	 * Notifies all listeners by calling update().
	 */
	fun notifyChange() {
		listeners.forEach { it.update() }
		internalListenerHandler?.update()
		guiListenerHandler?.update()
	}
}

/**
 * Basic observable with value.
 */
open class ValueObservable<T> {
	/**
	 * Holds all listeners on this property.
	 */
	private val listeners: MutableList<IValueObservable<T>> = mutableListOf()
	
	/**
	 * Used by renderer to listen on important properties for visualization.
	 */
	private var guiListenerHandler: IValueObservable<T>? = null
	
	/**
	 * Used by renderer to listen on important properties for visualization.
	 */
	internal var guiListener: ((T, T) -> Unit)? = null
		set(value) {
			guiListenerHandler = if (value == null) null else IValueObservable(value)
		}
	
	/**
	 * Used by BGW framework containers to manage children.
	 * Should only be set by direct parent.
	 */
	private var internalListenerHandler: IValueObservable<T>? = null
	
	/**
	 * Used by BGW framework containers to manage children.
	 * Should only be set by direct parent.
	 */
	internal var internalListener: ((T, T) -> Unit)? = null
		set(value) {
			internalListenerHandler = if (value == null) null else IValueObservable(value)
		}
	
	/**
	 * Sets GUI listener and calls update().
	 *
	 * @param initialValue Initial value to notify.
	 * @param listener Listener to add and notify.
	 */
	internal fun setGUIListenerAndInvoke(initialValue: T, listener: ((T, T) -> Unit)) {
		guiListener = listener
		guiListenerHandler?.update(initialValue, initialValue)
	}
	
	/**
	 * Sets internal listener and calls update().
	 *
	 * @param initialValue Initial value to notify.
	 * @param listener Listener to add and notify.
	 */
	internal fun setInternalListenerAndInvoke(initialValue: T, listener: ((T, T) -> Unit)) {
		internalListener = listener
		internalListenerHandler?.update(initialValue, initialValue)
	}
	
	/**
	 * Adds a listener and calls update() on this new listener with given initial value.
	 *
	 * @param initialValue Initial value to notify.
	 * @param listener Listener to add and notify.
	 */
	fun addListenerAndInvoke(initialValue: T, listener: IValueObservable<T>) {
		listeners.add(listener)
		listener.update(initialValue, initialValue)
	}
	
	/**
	 * Adds a listener silently.
	 *
	 * @param listener Listener to add.
	 */
	fun addListener(listener: IValueObservable<T>) {
		listeners.add(listener)
	}
	
	/**
	 * Removes a listener.
	 *
	 * @param listener Listener to remove.
	 *
	 * @return `true` if the listener has been successfully remove, `false` if it was not found.
	 */
	fun removeListener(listener: IValueObservable<T>) = listeners.remove(listener)
	
	/**
	 * Removes all listeners.
	 */
	fun clearListeners() {
		listeners.clear()
	}
	
	/**
	 * Notifies GUI listener by calling update().
	 *
	 * @param oldValue Old value to notify.
	 * @param newValue New value to notify.
	 */
	internal fun notifyGUIListener(oldValue: T, newValue: T) {
		guiListenerHandler?.update(oldValue, newValue)
	}
	
	/**
	 * Notifies internal listener by calling update().
	 *
	 * @param oldValue Old value to notify.
	 * @param newValue New value to notify.
	 */
	internal fun notifyInternalListener(oldValue: T, newValue: T) {
		internalListenerHandler?.update(oldValue, newValue)
	}
	
	/**
	 * Notifies all listeners by calling update().
	 *
	 * @param oldValue Old value to notify.
	 * @param newValue New value to notify.
	 */
	fun notifyChange(oldValue: T, newValue: T) {
		listeners.forEach { it.update(oldValue, newValue) }
		internalListenerHandler?.update(oldValue, newValue)
		guiListenerHandler?.update(oldValue, newValue)
	}
}