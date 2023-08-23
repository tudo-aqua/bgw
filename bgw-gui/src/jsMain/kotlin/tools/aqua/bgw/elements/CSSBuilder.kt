package tools.aqua.bgw.elements

import ComponentViewData
import UIComponentData
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
    fontSize = componentViewData.font?.size?.rem
    color = Color(componentViewData.font?.color ?: "black")
    // TODO...
}