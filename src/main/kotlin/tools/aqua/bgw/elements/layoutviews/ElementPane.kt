@file:Suppress("unused")

package tools.aqua.bgw.elements.layoutviews

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.visual.Visual

/**
 * A pane containing elements.
 *
 * @param height height for this [ElementPane]. Default: 0.
 * @param width width for this [ElementPane]. Default: 0.
 * @param posX horizontal coordinate for this [ElementPane]. Default: 0.
 * @param posY vertical coordinate for this [ElementPane]. Default: 0.
 * @param visual initial visual for this [ElementPane]. Default: [Visual.EMPTY].
 */
open class ElementPane<T : ElementView>(
	height: Number = 0,
	width: Number = 0,
	posX: Number = 0,
	posY: Number = 0,
	visual: Visual = Visual.EMPTY
) : LayoutElement<T>(height, width, posX, posY, visual) {
	
	override fun removeChild(child: ElementView) {
		TODO("Not yet implemented")
	}
}