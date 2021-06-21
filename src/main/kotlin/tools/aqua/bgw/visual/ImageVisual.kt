package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObjectProperty
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * Visual showing an image.
 *
 * @constructor Loads an ImageVisual from an image.
 *
 * @param image Image to show
 */
class ImageVisual(image: BufferedImage) : SingleLayerVisual() {
	
	/**
	 * The property for the displayed image.
	 */
	val imageProperty = ObjectProperty(image)
	
	/**
	 * The displayed image.
	 */
	var image
		get() = imageProperty.value
		set(value) {
			imageProperty.value = value
		}
	
	/**
	 * Loads an ImageVisual from a file.
	 *
	 * @param path Path to image file to show
	 */
	constructor(path: String) : this(load(path))
	
	companion object {
		/**
		 * Creates an ImageVisual from a file.
		 *
		 * @param file Image file to load
		 */
		fun loadImage(file: String) = ImageVisual(load(file))
		
		/**
		 * Creates an ImageVisual from a file.
		 * Uses a sub-image to display.
		 *
		 * @param file Image file to load
		 * @param offsetX Left bound of sub-image
		 * @param offsetY Top bound of sub-image
		 * @param width Width of sub-image
		 * @param height Height of sub-image
		 */
		fun loadSubImage(file: String, offsetX: Int = 0, offsetY: Int = 0, width: Int = 0, height: Int) =
			ImageVisual(load(file).getSubimage(offsetX, offsetY, width, height))
		
		/**
		 * Loads an image from a file.
		 */
		private fun load(file: String): BufferedImage = ImageIO.read(this::class.java.classLoader.getResource(file))
	}
}