package tools.aqua.bgw.elements.uicomponents

import LinearLayoutData
import ListViewData
import TableViewData
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
import web.dom.Element

external interface TableViewProps : Props {
    var data : TableViewData
}

fun PropertiesBuilder.cssBuilderIntern(componentViewData: TableViewData) {
    cssBuilder(componentViewData)
}

val TableView = FC<TableViewProps> { props ->
    bgwTableView {
        id = props.data.id
        className = ClassName("tableView")
        css {
            cssBuilderIntern(props.data)
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
                display = Display.flex
                alignItems = AlignItems.start
            }

            props.data.columns.forEachIndexed { i, column ->
                bgwContents {
                    className = ClassName("column")
                    id = props.data.id + "--column-$i"
                    css {
                        width = column.width.em
                        minWidth = column.width.em
                        display = Display.flex
                        flexDirection = FlexDirection.column
                        alignItems = AlignItems.start
                    }

                    bgwText {
                        className = ClassName("text")
                        css {
                            padding = 5.em
                            paddingTop = 3.em
                            paddingBottom = 8.em
                            width = 100.pct - 10.em
                            fontStyle = column.font.fontStyle.let { it.unsafeCast<FontStyle>() }
                            fontWeight = integer(column.font.fontWeight)
                            fontSize = column.font.size.em
                            fontFamily = cssFont(column.font.family)
                            color = column.font.color.unsafeCast<Color>()
                            minWidth = fit()
                        }
                        +column.title
                    }

                    column.items.forEachIndexed { index, item ->
                        bgwText {
                            className = ClassName("text")
                            css {
                                padding = 5.em
                                paddingTop = 3.em
                                paddingBottom = 3.em
                                width = 100.pct - 10.em
                                fontStyle = column.font.fontStyle.let { it.unsafeCast<FontStyle>() }
                                fontWeight = integer(column.font.fontWeight)
                                fontSize = column.font.size.em
                                fontFamily = cssFont(column.font.family)
                                color = column.font.color.unsafeCast<Color>()
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
            }

            applyCommonEventHandlers(props.data)
        }
    }
}

inline val bgwTableView: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_list_view".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

inline val bgwTableColumn: IntrinsicType<HTMLAttributes<Element>>
    get() = "bgw_table_column".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()