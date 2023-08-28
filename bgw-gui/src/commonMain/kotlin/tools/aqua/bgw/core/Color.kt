package tools.aqua.bgw.core

data class Color(val red: Int, val green: Int, val blue: Int, val alpha: Double = 1.0) {

    constructor(red: Int, green: Int, blue: Int, alpha: Int = 255) : this(red, green, blue, alpha.toDouble() / 255.0)
    constructor(red: Int, green: Int, blue: Int) : this(red, green, blue, 1.0)

    companion object {
        val TRANSPARENT: Color = Color(0,0,0, alpha = 0.0)
        val WHITE: Color = Color(255, 255, 255)
        val LIGHT_GRAY: Color = Color(192, 192, 192)
        val GRAY: Color = Color(128, 128, 128)
        val DARK_GRAY: Color = Color(64, 64, 64)
        val BLACK: Color = Color(0,0,0)
        val RED: Color = Color(255,0,0)
        val PINK: Color = Color(255,175,175)
        val ORANGE: Color = Color(255,200,0)
        val YELLOW: Color = Color(255,255,0)
        val GREEN: Color = Color(0,255,0)
        val MAGENTA: Color = Color(255,0,255)
        val CYAN: Color = Color(0,255,255)
        val BLUE: Color = Color(0,0,255)
    }
}