import kotlinx.serialization.Serializable

@Serializable
class Button() : ComponentView(0.0, 0.0, 0.0, 0.0, ColorVisual.RED) {
    constructor(posX: Double, posY: Double, width: Double, height: Double, visual: Visual) : this() {
        this.posX = posX
        this.posY = posY
        this.width = width
        this.height = height
        this.visual = visual
    }
}