@file:Suppress("unused")

package tools.aqua.bgw.core

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.observable.BooleanProperty

/**
 * Superclass for BGW game scenes.
 * Extends from this class in order to create your own game scene.
 *
 * @param width Scene width in virtual coordinates
 * @param height Scene height in virtual coordinates
 */
open class BoardGameScene(width: Number = 1920, height: Number = 1080) :
	Scene<ElementView>(width = width, height = height) {
	
	//TODO: Missing Docs
	internal val lockedProperty = BooleanProperty(false)
	
	/**
	 * Locks scene from any user input.
	 *
	 * @see unlock
	 */
	fun lock() {
		lockedProperty.value = true
	}
	
	/**
	 * Unlocks scene for user input.
	 *
	 * @see lock
	 */
	fun unlock() {
		lockedProperty.value = false
	}
}