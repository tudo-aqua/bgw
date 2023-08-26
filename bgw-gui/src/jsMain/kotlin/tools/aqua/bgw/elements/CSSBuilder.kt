package tools.aqua.bgw.elements

import ComponentViewData
import LayoutViewData
import UIComponentData
import VisualData
import csstype.*

fun PropertiesBuilder.cssBuilder(componentViewData: ComponentViewData) {
    position = Position.absolute
    left = componentViewData.posX.rem
    top = componentViewData.posY.rem
    width = componentViewData.width.rem
    height = componentViewData.height.rem
    zIndex = integer(componentViewData.zIndex)
    opacity = number(componentViewData.opacity)
    display = if(componentViewData.isVisible) Display.flex else None.none
    pointerEvents = if(!componentViewData.isDisabled) PointerEvents.all else None.none
    // TODO...
}

fun PropertiesBuilder.cssBuilder(componentViewData: UIComponentData) {
    cssBuilder(componentViewData as ComponentViewData)
    fontBuilder(componentViewData)
    // TODO...
}

fun PropertiesBuilder.fontBuilder(componentViewData: UIComponentData) {
    fontFamily = (componentViewData.font?.family ?: "Arial") as FontFamily?
    fontWeight = (componentViewData.font?.fontWeight ?: "normal") as FontWeight?
    fontStyle = (componentViewData.font?.fontStyle ?: "normal") as FontStyle?
    fontSize = componentViewData.font?.size?.rem
    color = Color(componentViewData.font?.color ?: "black")
}

fun PropertiesBuilder.styleBuilder(style : Map<String, String>) {
    borderRadius = cssBorderRadius(style["border-radius"] ?: "0rem")
}

fun PropertiesBuilder.filterBuilder(filters : Map<String, String?>) {
    val filterList = mutableListOf<String>()
    filters.values.forEach {
        if(it != null) {
            filterList.add(it)
        }
    }
    filter = cssFilter(filterList)
}

fun PropertiesBuilder.flipBuilder(flipped : String) {
    if(flipped == "horizontal") {
        transform = scalex(-1)
    } else if(flipped == "vertical") {
        transform = scaley(-1)
    } else if(flipped == "both") {
        transform = scale(-1, -1)
    }
}

fun cssBorderRadius(value : String): LengthProperty =
    value.unsafeCast<LengthProperty>()

fun cssFilter(values : List<String>): FilterFunction {
    if(values.isEmpty()) return "none".unsafeCast<FilterFunction>()
    return values.joinToString(" ").unsafeCast<FilterFunction>()
}
