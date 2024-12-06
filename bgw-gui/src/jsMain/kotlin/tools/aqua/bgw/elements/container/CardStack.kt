package tools.aqua.bgw.elements.container

import CardStackData
import LinearLayoutData
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

internal external interface CardStackProps : Props {
    var data : CardStackData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: CardStackData) {
    cssBuilder(componentViewData)
}

internal val CardStack = FC<CardStackProps> { props ->
    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })

    val elementRef = useRef<Element>(null)

    bgwCardStack {
        id = props.data.id
        className = ClassName("cardStack")
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
                alignmentBuilder(props.data)
            }

            props.data.components.forEach {
                +NodeBuilder.build(it)
            }
        }

        applyCommonEventHandlers(props.data)
    }
}

internal inline val bgwCardStack: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_card_stack".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()