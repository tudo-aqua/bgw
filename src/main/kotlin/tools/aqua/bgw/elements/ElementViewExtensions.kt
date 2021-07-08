@file:Suppress("unused")

package tools.aqua.bgw.elements

/**
 * Repositions this [ElementView] to the specified coordinates.
 * @param posX the new X coordinate.
 * @param posY the new Y coordinate.
 */
fun ElementView.reposition(posX: Number, posY: Number) {
	this.posX = posX.toDouble()
	this.posY = posY.toDouble()
}

/**
 * Adds an offset to this [ElementView]'s Position.
 * @param offsetX the offset for the X coordinate.
 * @param offsetY the offset for the Y coordinate.
 */
fun ElementView.offset(offsetX : Number, offsetY : Number) {
	this.posX += offsetX.toDouble()
	this.posY += offsetY.toDouble()
}

/**
 * Resizes this [ElementView] to the specified coordinates.
 * @param height the new height.
 * @param width the new width.
 */
fun ElementView.resize(height: Number, width: Number) {
	this.height = height.toDouble()
	this.width = width.toDouble()
}

/**
 * Scales this [ElementView]'s [ElementView.height] and [ElementView.width] by the given [scaleFactor].
 * @throws IllegalArgumentException if the given [scaleFactor] is negative.
 */
fun ElementView.scale(scaleFactor: Number) {
	val scaleFactorDoubleValue = scaleFactor.toDouble()
	require(scaleFactorDoubleValue >= 0) {
		"Only non-negative scale factors are allowed. Provided scale factor was $scaleFactorDoubleValue."
	}
	this.height = height * scaleFactorDoubleValue
	this.width = width * scaleFactorDoubleValue
}

/**
 *
 * Scales this [ElementView]'s [ElementView.width] by the given [scaleFactor].
 * @throws IllegalArgumentException if the given [scaleFactor] is negative.
 */
fun ElementView.scaleWidth(scaleFactor: Number) {
	val scaleFactorDoubleValue = scaleFactor.toDouble()
	require(scaleFactorDoubleValue >= 0) {
		"Only non-negative scale factors are allowed. Provided scale factor was $scaleFactorDoubleValue."
	}
	this.width = width * scaleFactorDoubleValue
}

/**
 *
 * Scales this [ElementView]'s [ElementView.height] by the given [scaleFactor].
 * @throws IllegalArgumentException if the given [scaleFactor] is negative.
 */
fun ElementView.scaleHeight(scaleFactor: Number) {
	val scaleFactorDoubleValue = scaleFactor.toDouble()
	require(scaleFactorDoubleValue >= 0) {
		"Only non-negative scale factors are allowed. Provided scale factor was $scaleFactorDoubleValue."
	}
	this.height = height * scaleFactorDoubleValue
}

/**
 * Rotates this [ElementView] by the given number of [degrees].
 */
fun ElementView.rotate(degrees: Number) {
	this.rotation += degrees.toDouble()
}