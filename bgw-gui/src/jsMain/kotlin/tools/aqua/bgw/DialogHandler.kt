package tools.aqua.bgw

import kotlinx.browser.window
import org.w3c.dom.Window

class DialogHandler {
    fun openDialog(text : String) {
        val dialog = window.open("about:blank", "hello", "popup=on,width=600,height=300,left=100,top=100") ?: return
        dialog.focus()

        dialog.document.write(
            text
        )
    }
}