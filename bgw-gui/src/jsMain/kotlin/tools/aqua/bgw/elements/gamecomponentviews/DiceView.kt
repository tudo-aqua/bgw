package tools.aqua.bgw.elements.gamecomponentviews

import DiceViewData
import TokenViewData
import csstype.PropertiesBuilder
import web.cssom.*
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
import web.dom.Element

external interface DiceViewProps : Props {
    var data: DiceViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: DiceViewData) {
    cssBuilder(componentViewData)
}

val DiceView = FC<DiceViewProps> { props ->
    bgwDiceView {
        id = props.data.id
        className = ClassName("diceView")
        css {
            cssBuilderIntern(props.data)
        }

        bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visuals[props.data.currentSide])
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

inline val bgwDiceView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_dice_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()