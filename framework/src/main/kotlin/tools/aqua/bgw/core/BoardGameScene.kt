@file:Suppress("unused")

package tools.aqua.bgw.core

import tools.aqua.bgw.core.Scene.Companion.DEFAULT_SCENE_HEIGHT
import tools.aqua.bgw.core.Scene.Companion.DEFAULT_SCENE_WIDTH
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.observable.BooleanProperty
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for BGW game scenes.
 * Extend this class in order to create your own game scene.
 *
 * @param width scene width in virtual coordinates. Default: [DEFAULT_SCENE_WIDTH].
 * @param height scene height in virtual coordinates. Default: [DEFAULT_SCENE_HEIGHT].
 * @param background [BoardGameScene] [background] [Visual]. Default: [ColorVisual.WHITE].
 */
open class BoardGameScene(
	width: Number = DEFAULT_SCENE_WIDTH,
	height: Number = DEFAULT_SCENE_HEIGHT,
	background: Visual = ColorVisual.WHITE
) : Scene<ElementView>(width = width, height = height, background = background) {
	
	/**
	 * Property that indicates if this [BoardGameScene] is locked from user input.
	 *
	 * @see lock
	 * @see unlock
	 */
	internal val lockedProperty = BooleanProperty(false)
	
	/**
	 * Locks [Scene] from any user input.
	 *
	 * @see unlock
	 */
	fun lock() {
		lockedProperty.value = true
	}
	
	/**
	 * Unlocks [Scene] for user input.
	 *
	 * @see lock
	 */
	fun unlock() {
		lockedProperty.value = false
	}
}