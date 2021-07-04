@file:Suppress("unused")

package tools.aqua.bgw.visual

import tools.aqua.bgw.observable.ObjectProperty
import tools.aqua.bgw.observable.Property
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

/**
 * Visual showing an image.
 * The image gets stretched to the size of the element that this visual is embedded in.
 *
 * A sub-image can be loaded by passing offsetX/offsetY for the TOP_LEFT corner and width/height for the sub-image size.
 * If width or height is passed as -1, the remaining image from offsetX/offsetY will be loaded.
 *
 * @constructor Loads an [ImageVisual] from a [BufferedImage].
 *
 * @param image image to show.
 * @param width width of sub-image. Pass -1 to use full width. Default: -1.
 * @param height height of sub-image. Pass -1 to use full height. Default: -1.
 * @param offsetX left bound of sub-image. Default: 0.
 * @param offsetY top bound of sub-image. Default: 0.
 */
class ImageVisual(
	image: BufferedImage,
	width: Int = -1,
	height: Int = -1,
	offsetX: Int = 0,
	offsetY: Int = 0
) : SingleLayerVisual() {
	
	/**
	 * [Property] for the displayed [image].
	 */
	val imageProperty: ObjectProperty<BufferedImage>
	
	/**
	 * The displayed [image].
	 */
	var image: BufferedImage
		get() = imageProperty.value
		set(value) {
			imageProperty.value = value
		}
	
	/**
	 * Loads an [ImageVisual] from a path.
	 *
	 * @param path path to image file to show.
	 * @param width width of sub-image. Pass -1 to use full width. Default: -1.
	 * @param height height of sub-image. Pass -1 to use full height. Default: -1.
	 * @param offsetX left bound of sub-image. Gets ignored if width is passed as -1. Default: 0.
	 * @param offsetY top bound of sub-image. Gets ignored if height is passed as -1. Default: 0.
	 */
	constructor(
		path: String,
		width: Int = -1,
		height: Int = -1,
		offsetX: Int = 0,
		offsetY: Int = 0
	) : this(load(path), width, height, offsetX, offsetY)
	
	/**
	 * Loads an [ImageVisual] from a file.
	 *
	 * @param file image file to show.
	 * @param width width of sub-image. Pass -1 to use full width. Default: -1.
	 * @param height height of sub-image. Pass -1 to use full height. Default: -1.
	 * @param offsetX left bound of sub-image. Gets ignored if width is passed as -1. Default: 0.
	 * @param offsetY top bound of sub-image. Gets ignored if height is passed as -1. Default: 0.
	 */
	constructor(
		file: File,
		width: Int = -1,
		height: Int = -1,
		offsetX: Int = 0,
		offsetY: Int = 0
	) : this(load(file.absolutePath), width, height, offsetX, offsetY)
	
	init {
		require(offsetX < image.width) { "OffsetX is larger than image width." }
		require(offsetY < image.height) { "OffsetY is larger than image height." }
		
		require(width > 0 || width == -1) { "Width must be positive or -1." }
		require(height > 0 || height == -1) { "Height must be positive or -1." }
		
		val subWidth = if (width != -1) width else image.width - offsetX
		val subHeight = if (height != -1) height else image.height - offsetY
		
		require(subWidth > 0) { "Width exceeds image width." }
		require(subHeight > 0) { "Height exceeds image height." }
		
		val img = image.getSubimage(offsetX, offsetY, subWidth, subHeight)
		
		imageProperty = ObjectProperty(img)
	}
	
	companion object {
		/**
		 * Loads an image from a file.
		 */
		private fun load(file: String): BufferedImage = ImageIO.read(this::class.java.classLoader.getResource(file))
	}
}