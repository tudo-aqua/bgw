@file:Suppress("unused")

package tools.aqua.bgw.elements.layoutviews

import tools.aqua.bgw.elements.ElementView

/**
 * A pane containing elements.
 */
open class ElementPane<T : ElementView> : LayoutElement<T>() {
	/**
	 * {@inheritDoc}.
	 */
	override fun removeChild(child: ElementView) {
		TODO("Not yet implemented")
	}
}