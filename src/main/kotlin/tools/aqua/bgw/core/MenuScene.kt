@file:Suppress("unused")

package tools.aqua.bgw.core

import tools.aqua.bgw.core.Scene.Companion.DEFAULT_SCENE_HEIGHT
import tools.aqua.bgw.core.Scene.Companion.DEFAULT_SCENE_WIDTH
import tools.aqua.bgw.elements.StaticView

/**
 * Superclass for BGW menu scenes.
 * Extend this class in order to create your own menu scene
 *
 * @param width scene width in virtual coordinates. Default: [DEFAULT_SCENE_WIDTH].
 * @param height scene height in virtual coordinates. Default: [DEFAULT_SCENE_HEIGHT].
 */
open class MenuScene(width: Number = DEFAULT_SCENE_WIDTH, height: Number = DEFAULT_SCENE_HEIGHT) :
	Scene<StaticView<out StaticView<*>>>(width = width, height = height) {
	init {
		opacity = 0.0
	}
}