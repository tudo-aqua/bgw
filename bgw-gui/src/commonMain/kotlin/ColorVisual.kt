import kotlinx.serialization.Serializable

@Serializable
class ColorVisual(val red: Int, val green: Int, val blue: Int) : Visual() {
  companion object {
    val BLACK = ColorVisual(0, 0, 0)
    val WHITE = ColorVisual(255, 255, 255)
    val RED = ColorVisual(255, 0, 0)
    val GREEN = ColorVisual(0, 255, 0)
    val BLUE = ColorVisual(0, 0, 255)
    val YELLOW = ColorVisual(255, 255, 0)
    val CYAN = ColorVisual(0, 255, 255)
    val MAGENTA = ColorVisual(255, 0, 255)
  }

  override fun copy(): Visual {
    return ColorVisual(red, green, blue)
  }
}
