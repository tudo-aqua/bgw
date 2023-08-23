package tools.aqua.bgw.elements.uicomponents

import ComponentViewData
import LabelData
import UIComponentData
import csstype.*
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.cssBuilder

external interface LabelProps : Props {
    var data: LabelData
}

fun PropertiesBuilder.cssBuilderLabel(componentViewData: LabelData) {
    cssBuilder(componentViewData)
    justifyContent = JustifyContent.center
    alignItems = AlignItems.center
}

val Label = FC<LabelProps> { props ->
    div {
        id = props.data.id
        className = ClassName("label")
        css {
            cssBuilderLabel(props.data)
        }

        div {
            className = ClassName("visuals")

            VisualBuilder.build(props.data.visual)
        }

        h1 {
            className = ClassName("text")
            +props.data.text
        }
    }
}