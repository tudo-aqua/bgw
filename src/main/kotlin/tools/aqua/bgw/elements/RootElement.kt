package tools.aqua.bgw.elements

import tools.aqua.bgw.core.Scene

/**
 * The root element in the view hierarchy of a scene.
 *
 * @param scene The scene of this root element,.
 */
internal class RootElement<T : ElementView>(val scene: Scene<T>) : ElementView() {
	/**
	 * {@inheritDoc}.
	 */
	@Suppress("UNCHECKED_CAST")
	override fun removeChild(child: ElementView) {
		try {
			this.scene.removeElements(child as T)
		} catch (_: ClassCastException) {
		}
	}
}