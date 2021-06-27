@file:Suppress("unused")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObjectProperty
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * Visual showing an image.
 * The image gets stretched to the size of the element that this visual is embedded in.
 *
 * @constructor Loads an ImageVisual from a BufferedImage.
 *
 * @param image image to show.
 */
class ImageVisual(image: BufferedImage) : SingleLayerVisual() {
	
	/**
	 * The property for the displayed image.
	 */
	val imageProperty: ObjectProperty<BufferedImage> = ObjectProperty(image)
	
	/**
	 * The displayed image.
	 */
	var image: BufferedImage
		get() = imageProperty.value
		set(value) {
			imageProperty.value = value
		}
	
	/**
	 * Loads an ImageVisual from a file.
	 *
	 * @param path path to image file to show
	 */
	constructor(path: String) : this(load(path))
	
	companion object {
		/**
		 * Creates an ImageVisual from a file.
		 *
		 * @param file image file to load.
		 */
		fun loadImage(file: String): ImageVisual = ImageVisual(load(file))
		
		/**
		 * Creates an ImageVisual from a file.
		 * Uses a sub-image to display.
		 *
		 * @param file image file to load
		 * @param offsetX left bound of sub-image
		 * @param offsetY top bound of sub-image
		 * @param width width of sub-image
		 * @param height height of sub-image
		 */
		fun loadSubImage(file: String, offsetX: Int = 0, offsetY: Int = 0, width: Int = 0, height: Int): ImageVisual =
			ImageVisual(load(file).getSubimage(offsetX, offsetY, width, height))
		
		/**
		 * Loads an image from a file.
		 */
		private fun load(file: String): BufferedImage = ImageIO.read(this::class.java.classLoader.getResource(file))
	}
}