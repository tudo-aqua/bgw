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
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.alignmentBuilder
import tools.aqua.bgw.elements.bgwContents
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import web.dom.Element

external interface SatchelProps : Props {
    var data : SatchelData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: SatchelData) {
    cssBuilder(componentViewData)
}

val Satchel = FC<SatchelProps> { props ->
    bgwSatchel {
        id = props.data.id
        className = ClassName("satchel")
        css {
            cssBuilderIntern(props.data)
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
                opacity = number(0.0)
            }

            props.data.components.forEach {
                +NodeBuilder.build(it)
            }
        }

        onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id))
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
    }
}

inline val bgwSatchel: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_satchel".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()