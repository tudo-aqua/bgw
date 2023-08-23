package tools.aqua.bgw.elements.uicomponents

import ComponentViewData
import LabelData
import UIComponentData
import csstype.*
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.FC
import react.IntrinsicType
import react.Props
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.cssBuilder

external interface LabelProps : Props {
    var data: LabelData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: LabelData) {
    cssBuilder(componentViewData)
    justifyContent = JustifyContent.center
    alignItems = AlignItems.center
    fontSize = 30.rem
}

val Label = FC<LabelProps> { props ->
    bgwLabel {
        id = props.data.id
        className = ClassName("label")
        css {
            cssBuilderIntern(props.data)
        }

        div {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        h1 {
            className = ClassName("text")
            +props.data.text
        }
    }
}

inline val bgwLabel: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_label".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()