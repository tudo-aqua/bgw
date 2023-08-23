package tools.aqua.bgw.builder

import ButtonData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.uicomponents.ReactButton
import tools.aqua.bgw.randomHexColor

object ButtonBuilder {
    fun build(buttonData: ButtonData): ReactElement<*> {
        return ReactButton.create { }
    }
}