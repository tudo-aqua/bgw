@file:Suppress("unused")

package tools.aqua.bgw.core

import tools.aqua.bgw.core.Scene.Companion.DEFAULT_SCENE_HEIGHT
import tools.aqua.bgw.core.Scene.Companion.DEFAULT_SCENE_WIDTH
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual

/**
 * Baseclass for BGW menu scenes.
 * Extend this class in order to create your own menu scene.
 *
 * @param width scene width in virtual coordinates. Default: [DEFAULT_SCENE_WIDTH].
 * @param height scene height in virtual coordinates. Default: [DEFAULT_SCENE_HEIGHT].
 * @param background [BoardGameScene] [background] [Visual]. Default: [ColorVisual.WHITE].
 */
open class MenuScene(
	width: Number = DEFAULT_SCENE_WIDTH,
	height: Number = DEFAULT_SCENE_HEIGHT,
	background: Visual = ColorVisual.WHITE
) : Scene<StaticView<out StaticView<*>>>(width = width, height = height, background = background) {
	
	init {
		opacity = DEFAULT_MENU_SCENE_OPACITY
	}
	
	companion object {
		/**
		 * Default menu scene opacity.
		 */
		const val DEFAULT_MENU_SCENE_OPACITY: Double = 0.75
	}
}