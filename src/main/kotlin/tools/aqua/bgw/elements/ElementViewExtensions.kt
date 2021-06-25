package tools.aqua.bgw.elements

import tools.aqua.bgw.visual.Visual
import java.lang.IllegalArgumentException

fun ElementView.reposition(posX : Number, posY : Number) {
    this.posX = posX.toDouble()
    this.posY = posY.toDouble()
}

fun ElementView.offset(offsetX : Number, offsetY : Number) {
    this.posX += offsetX.toDouble()
    this.posY += offsetY.toDouble()
}

fun ElementView.resize(height: Number, width: Number) {
    this.height = height.toDouble()
    this.width = width.toDouble()
}

fun ElementView.scale(scaleFactor: Number) {
    val scaleFactorDoubleValue = scaleFactor.toDouble()
    require(scaleFactorDoubleValue >= 0) {
        "Only non-negative scale factors are allowed. Provided scale factor was $scaleFactorDoubleValue."
    }
    this.height = height * scaleFactorDoubleValue
    this.width = width * scaleFactorDoubleValue
}

fun ElementView.rotate(degrees: Number) {
    this.rotation += degrees.toDouble()
}

fun ElementView.visual(func: () -> Visual) {
    this.setVisuals(func())
}