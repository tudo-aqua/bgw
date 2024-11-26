package tools.aqua.bgw.elements.uicomponents

import LinearLayoutData
import ListViewData
import csstype.PropertiesBuilder
import web.cssom.*
import data.event.KeyEventAction
import data.event.internal.CheckBoxChangedEventData
import data.event.internal.StructuredDataSelectEventData
import emotion.react.css
import kotlinx.browser.document
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import org.w3c.dom.get
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
import tools.aqua.bgw.elements.*
import tools.aqua.bgw.event.JCEFEventDispatcher
import tools.aqua.bgw.event.applyCommonEventHandlers
import tools.aqua.bgw.handlers
import tools.aqua.bgw.useDroppable
import web.dom.Element

external interface ListViewProps : Props {
    var data : ListViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: ListViewData) {
    cssBuilder(componentViewData)
}

val ListView = FC<ListViewProps> { props ->
    val droppable = useDroppable(object : DroppableOptions {
        override var id: String = props.data.id
        override var disabled = !props.data.isDroppable
    })

    val elementRef = useRef<Element>(null)

    bgwListView {
        id = props.data.id
        className = ClassName("listView")
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

        bgwScroll {
            className = ClassName("scroll")
            css {
                width = 100.pct
                height = 100.pct
                overflow = Auto.auto
                position = Position.relative
            }

            bgwContents {
                className = ClassName("components")
                id = props.data.id + "--components"
                css {
                    width = fit()
                    display = Display.flex
                    flexDirection = FlexDirection.column
                    alignItems = AlignItems.start
                    minWidth = 100.pct - 10.em

                    if(props.data.orientation == "horizontal") {
                        flexDirection = FlexDirection.row
                        alignItems = AlignItems.center
                    }
                }

                props.data.items.forEachIndexed { index, item ->
                    bgwText {
                        className = ClassName("text")
                        css {
                            padding = 5.em
                            paddingTop = 3.em
                            paddingBottom = 3.em
                            width = 100.pct
                            fontStyle = props.data.font!!.fontStyle.let { it.unsafeCast<FontStyle>() }
                            fontWeight = integer(props.data.font!!.fontWeight)
                            fontSize = props.data.font!!.size.em
                            fontFamily = cssFont(props.data.font!!.family)
                            color = props.data.font!!.color.unsafeCast<Color>()
                            minWidth = fit()

                            if(props.data.selectedItems.contains(index)) {
                                backgroundColor = props.data.selectionBackground.unsafeCast<Color>()
                            }
                        }
                        +item

                        onClick = { JCEFEventDispatcher.dispatchEvent(StructuredDataSelectEventData(index).apply { id = props.data.id }) }
                    }
                }
            }

            applyCommonEventHandlers(props.data)
        }
    }
}

inline val bgwListView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_list_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwScroll : IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_scroll".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()