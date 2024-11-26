package tools.aqua.bgw.elements.uicomponents

import ComponentViewData
import LabelData
import ProgressBarData
import UIComponentData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import react.*
import react.dom.html.HTMLAttributes
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1
import tools.aqua.bgw.*
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import web.dom.Element

external interface ProgressBarProps : Props {
    var data: ProgressBarData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ProgressBarData) {
    cssBuilder(componentViewData)
}

val ProgressBar = FC<ProgressBarProps> { props ->
    var droppable : DroppableResult? = null

    if(props.data.isDroppable) {
        droppable = useDroppable(object : DroppableOptions {
            override var id: String = props.data.id
        })
    }

    val elementRef = useRef<Element>(null)

    bgwProgress {
        tabIndex = 0
        id = props.data.id
        className = ClassName("progress")
        css {
            cssBuilderIntern(props.data)
        }

        ref = elementRef

        if(props.data.isDroppable) {
            useEffect {
                elementRef.current?.let { droppable!!.setNodeRef(it) }
            }
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        bgwProgressBar {
            id = "${props.data.id}--bar"
            className = ClassName("progress-bar")
            css {
                width = (props.data.progress * 100).pct
                height = 100.pct
                backgroundColor = Color(props.data.barColor)
                position = Position.absolute
                left = 0.px
                top = 0.px
                transition = transition(500, "width")
            }
        }

        applyCommonEventHandlers(props.data)
    }
}

inline val bgwProgress: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_progress".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwProgressBar: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_progress_bar".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()