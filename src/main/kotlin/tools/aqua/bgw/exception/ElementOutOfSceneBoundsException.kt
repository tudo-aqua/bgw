@file:Suppress("unused")

package tools.aqua.bgw.exception

import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView

internal class ElementOutOfSceneBoundsException(element: ElementView, scene: Scene<*>) :
	RuntimeException(
		"Element $element is placed out of scene bounds: " +
				"PosX = ${element.posX}, element width = ${element.width}, scene width = ${scene.width}; " +
				"PosY = ${element.posY}, element height = ${element.height}, scene height = ${scene.height}"
	)