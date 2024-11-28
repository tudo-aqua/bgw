package tools.aqua.bgw.elements.container

import CardStackData
import LinearLayoutData
import SatchelData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.DroppableOptions
import tools.aqua.bgw.DroppableResult
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEnteredData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseExitedData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.alignmentBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.useDroppable
import web.dom.Element

internal external interface SatchelProps : Props {
    var data : SatchelData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: SatchelData) {
    cssBuilder(componentViewData)
}

internal val Satchel = FC<SatchelProps> { props ->
    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })

    val elementRef = useRef<Element>(null)

    bgwSatchel {
        id = props.data.id
        className = ClassName("satchel")
        css {
            cssBuilderIntern(props.data)
        }

        ref = elementRef
        useEffect {
            elementRef.current?.let { droppable.setNodeRef(it) }
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
        }

        bgwContents {
            className = ClassName("components")
            css {
                width = 100.pct
                height = 100.pct
                display = Display.flex
                justifyContent = JustifyContent.center
                alignItems = AlignItems.center
            }

            props.data.components.forEach {
                +NodeBuilder.build(it.apply {
                    opacity = 0.0
                })
            }
        }

        applyCommonEventHandlers(props.data)
    }
}

inline val bgwSatchel: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_satchel".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()