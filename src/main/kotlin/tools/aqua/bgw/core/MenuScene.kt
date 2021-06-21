package tools.aqua.bgw.core

import tools.aqua.bgw.elements.StaticView

/**
 * Superclass for BGW menu scenes.
 * Extends from this class in order to create your own menu scene.
 *
 * @param width Scene width in virtual coordinates
 * @param height Scene height in virtual coordinates
 */
open class MenuScene(width: Number = 1920, height: Number = 1080) :
	Scene<StaticView<out StaticView<*>>>(width = width, height = height) {
	init {
		opacity = 0.0
	}
}