package tools.aqua.bgw.style

import java.awt.Color

open class Cursor internal constructor(override val value: String = "") : StyleAttribute() {
    override val key: String = "-fx-cursor"

    companion object {
        val NULL = Cursor("null")
        val CROSSHAIR = Cursor("crosshair")
        val E_RESIZE = Cursor("e-resize")
        val H_RESIZE = Cursor("h-resize")
        val NE_RESIZE = Cursor("ne-resize")
        val NW_RESIZE = Cursor("nw-resize")
        val N_RESIZE = Cursor("n-resize")
        val SE_RESIZE = Cursor("se-resize")
        val SW_RESIZE = Cursor("sw-resize")
        val S_RESIZE = Cursor("s-resize")
        val W_RESIZE = Cursor("w-resize")
        val V_RESIZE = Cursor("v-resize")
        val TEXT = Cursor("text")
        val WAIT = Cursor("wait")
    }
}