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
	 * @param newValue New value of property
	 */
	fun update(newValue: T)
}

/**
 * Basic observable.
 */
open class Observable {
	private val listeners: MutableList<IObservable> = mutableListOf()
	
	/**
	 * Used by renderer to listen on important properties for visualization.
	 */
	private var guiListenerHandler: IObservable? = null
	internal var guiListener: (() -> Unit)? = null
		set(value) {
			guiListenerHandler = if (value == null) null else IObservable(value)
		}
	
	/**
	 * Used by BGW framework containers to manage children.
	 * Should only be set by direct parent.
	 */
	private var internalListenerHandler: IObservable? = null
	internal var internalListener: (() -> Unit)? = null
		set(value) {
			internalListenerHandler = if (value == null) null else IObservable(value)
		}
	
	/**
	 * Sets GUI listener and calls update().
	 */
	internal fun setGUIListenerAndInvoke(listener: () -> Unit) {
		guiListener = listener
		guiListenerHandler?.update()
	}
	
	/**
	 * Sets internal listener and calls update().
	 */
	internal fun setInternalListenerAndInvoke(listener: () -> Unit) {
		internalListener = listener
		internalListenerHandler?.update()
	}
	
	/**
	 * Adds a listener and calls update() on this new listener.
	 *
	 * @param listener Listener to add and notify
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
	 * @param listener Listener to remove
	 */
	fun removeListener(listener: IObservable) {
		listeners.remove(listener)
	}
	
	/**
	 * Removes all listeners.
	 */
	fun clearListeners() {
		listeners.clear()
	}
	
	/**
	 * Notifies all listeners by calling update().
	 */
	fun notifyChange() {
		listeners.forEach { it.update() }
		internalListenerHandler?.update()
		guiListenerHandler?.update()
	}
	
	/**
	 * Notifies internal listener by calling update().
	 */
	internal fun notifyInternalListener() {
		internalListenerHandler?.update()
	}
	
	/**
	 * Notifies GUI listener by calling update().
	 */
	internal fun notifyGUIListener() {
		guiListenerHandler?.update()
	}
}

/**
 * Basic observable with value.
 */
open class ValueObservable<T> {
	private val listeners: MutableList<IValueObservable<T>> = mutableListOf()
	
	/**
	 * Used by renderer to listen on important properties for visualization.
	 */
	private var guiListenerHandler: IValueObservable<T>? = null
	internal var guiListener: ((T) -> Unit)? = null
		set(value) {
			guiListenerHandler = if (value == null) null else IValueObservable(value)
		}
	
	/**
	 * Used by BGW framework containers to manage children.
	 * Should only be set by direct parent.
	 */
	private var internalListenerHandler: IValueObservable<T>? = null
	internal var internalListener: ((T) -> Unit)? = null
		set(value) {
			internalListenerHandler = if (value == null) null else IValueObservable(value)
		}
	
	/**
	 * Sets GUI listener and calls update().
	 *
	 * @param initialValue Initial value to notify
	 */
	internal fun setGUIListenerAndInvoke(initialValue: T, listener: ((T)) -> Unit) {
		guiListener = listener
		guiListenerHandler?.update(initialValue)
	}
	
	/**
	 * Sets internal listener and calls update().
	 *
	 * @param initialValue Initial value to notify
	 */
	internal fun setInternalListenerAndInvoke(initialValue: T, listener: ((T)) -> Unit) {
		internalListener = listener
		internalListenerHandler?.update(initialValue)
	}
	
	/**
	 * Adds a listener and calls update() on this new listener with given initial value.
	 *
	 * @param initialValue Initial value to notify
	 * @param listener Listener to add and notify
	 */
	fun addListenerAndInvoke(initialValue: T, listener: IValueObservable<T>) {
		listeners.add(listener)
		listener.update(initialValue)
	}
	
	/**
	 * Adds a listener silently.
	 *
	 * @param listener Listener to add
	 */
	fun addListener(listener: IValueObservable<T>) {
		listeners.add(listener)
	}
	
	/**
	 * Removes a listener.
	 *
	 * @param listener Listener to remove
	 */
	fun removeListener(listener: IValueObservable<T>) {
		listeners.remove(listener)
	}
	
	/**
	 * Removes all listeners.
	 */
	fun clearListeners() {
		listeners.clear()
	}
	
	/**
	 * Notifies all listeners by calling update().
	 *
	 * @param newValue New value to notify.
	 */
	fun notifyChange(newValue: T) {
		listeners.forEach { it.update(newValue) }
		internalListenerHandler?.update(newValue)
		guiListenerHandler?.update(newValue)
	}
	
	/**
	 * Notifies internal listener by calling update().
	 *
	 * @param newValue New value to notify.
	 */
	internal fun notifyInternalListener(newValue: T) {
		internalListenerHandler?.update(newValue)
	}
	
	/**
	 * Notifies GUI listener by calling update().
	 *
	 * @param newValue New value to notify.
	 */
	internal fun notifyGUIListener(newValue: T) {
		guiListenerHandler?.update(newValue)
	}
}