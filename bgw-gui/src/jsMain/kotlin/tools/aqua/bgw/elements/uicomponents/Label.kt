package tools.aqua.bgw.elements.uicomponents

import ComponentViewData
import LabelData
import UIComponentData
import csstype.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers

external interface LabelProps : Props {
    var data: LabelData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: LabelData) {
    cssBuilder(componentViewData)
}

val Label = FC<LabelProps> { props ->
    bgwLabel {
        tabIndex = 0
        id = props.data.id
        className = ClassName("label")
        css {
            cssBuilderIntern(props.data)
        }

        +VisualBuilder.build(props.data.visual)

        bgwText {
            className = ClassName("text")
            +props.data.text
        }

        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwLabel: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_label".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()