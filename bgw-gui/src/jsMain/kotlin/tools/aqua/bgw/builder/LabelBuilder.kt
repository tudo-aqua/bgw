package tools.aqua.bgw.builder

import ComponentView
import Label
import LabelData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.uicomponents.Label as ReactLabel
import tools.aqua.bgw.randomHexColor

object LabelBuilder {
    fun build(labelData: LabelData): ReactElement<*> {
        return ReactLabel.create { data = labelData }
    }
}
