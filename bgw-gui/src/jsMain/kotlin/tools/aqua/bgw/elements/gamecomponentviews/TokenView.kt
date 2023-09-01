package tools.aqua.bgw.elements.gamecomponentviews

import TokenViewData
import csstype.*
import data.event.KeyEventAction
import emotion.react.css
import org.w3c.dom.HTMLDivElement
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.builder.ReactConverters.toKeyEventData
import tools.aqua.bgw.builder.ReactConverters.toMouseEventData
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgwText
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.handlers

external interface TokenViewProps : Props {
    var data: TokenViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: TokenViewData) {
    cssBuilder(componentViewData)
}

val TokenView = FC<TokenViewProps> { props ->
    bgwTokenView {
        id = props.data.id
        className = ClassName("tokenView")
        css {
            cssBuilderIntern(props.data)
        }

        +VisualBuilder.build(props.data.visual)

               onContextMenu = {
            it.preventDefault()
            JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) 
        }
        onClick = { JCEFEventDispatcher.dispatchEvent(it.toMouseEventData(id)) }
        onKeyDown = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.PRESS)) }
        onKeyUp = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.RELEASE)) }
        onKeyPress = { JCEFEventDispatcher.dispatchEvent(it.toKeyEventData(id, KeyEventAction.TYPE)) }
    }
}

inline val bgwTokenView: IntrinsicType<HTMLAttributes<HTMLDivElement>>
    get() = "bgw_token_view".unsafeCast<IntrinsicType<HTMLAttributes<HTMLDivElement>>>()