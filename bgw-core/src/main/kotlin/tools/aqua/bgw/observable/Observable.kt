/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.observable

/**
 * Basic observable.
 *
 * @constructor Empty constructor.
 */
@Suppress("UnnecessaryAbstractClass")
abstract class Observable {
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
	 *
	 * Should only be set by direct parent.
	 */
	private var internalListenerHandler: IObservable? = null
	
	/**
	 * Used by BGW framework containers to manage children.
	 *
	 * Should only be set by direct parent.
	 */
	internal var internalListener: (() -> Unit)? = null
		set(value) {
			internalListenerHandler = if (value == null) null else IObservable(value)
		}
	
	/**
	 * Sets [guiListener] and calls [IObservable.update].
	 *
	 * @param listener Listener to add and notify.
	 */
	internal fun setGUIListenerAndInvoke(listener: () -> Unit) {
		guiListener = listener
		guiListenerHandler?.update()
	}
	
	/**
	 * Sets [internalListener] and calls [IObservable.update].
	 *
	 * @param listener Listener to add and notify.
	 */
	internal fun setInternalListenerAndInvoke(listener: () -> Unit) {
		internalListener = listener
		internalListenerHandler?.update()
	}
	
	/**
	 * Adds a [listener] and calls [IObservable.update] on this new listener.
	 *
	 * @param listener Listener to add and notify.
	 */
	fun addListenerAndInvoke(listener: IObservable) {
		listeners.add(listener)
		listener.update()
	}
	
	/**
	 * Adds a [listener] silently.
	 *
	 * @param listener Listener to add
	 */
	fun addListener(listener: IObservable) {
		listeners.add(listener)
	}
	
	/**
	 * Removes a [listener].
	 *
	 * @param listener Listener to remove.
	 *
	 * @return `true` if the listener has been successfully removed, `false` if it was not found.
	 */
	fun removeListener(listener: IObservable): Boolean = listeners.remove(listener)
	
	/**
	 * Removes all listeners.
	 */
	fun clearListeners() {
		listeners.clear()
	}
	
	/**
	 * Notifies [guiListener] by calling [IObservable.update].
	 */
	internal fun notifyGUIListener() {
		guiListenerHandler?.update()
	}
	
	/**
	 * Notifies [internalListener] by calling [IObservable.update].
	 */
	internal fun notifyInternalListener() {
		internalListenerHandler?.update()
	}
	
	/**
	 * Notifies all [listeners] by calling [IObservable.update].
	 */
	fun notifyChange() {
		listeners.forEach { it.update() }
		internalListenerHandler?.update()
		guiListenerHandler?.update()
	}
}