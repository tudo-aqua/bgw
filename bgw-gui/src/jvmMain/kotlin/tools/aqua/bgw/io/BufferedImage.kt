package tools.aqua.bgw.io

interface BufferedImage {
    val height: Int
    val width: Int
    fun getSubimage(offsetX: Int, offsetY: Int, subWidth: Int, subHeight: Int): BufferedImage
}