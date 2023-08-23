package tools.aqua.bgw.builder


import LabelData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.uicomponents.Label as ReactLabel

object LabelBuilder {
    fun build(labelData: LabelData): ReactElement<*> {
        return ReactLabel.create { data = labelData }
    }
}
