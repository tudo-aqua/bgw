package tools.aqua.bgw.elements

import CardStackData
import ComboBoxData
import ComponentViewData
import GameComponentViewData
import LabeledUIComponentData
import LayoutViewData
import TextInputUIComponentData
import UIComponentData
import VisualData
import csstype.PropertiesBuilder
import csstype.RuleBuilder
import web.cssom.*
import org.w3c.dom.css.CSSRule

internal fun PropertiesBuilder.cssBuilder(componentViewData: ComponentViewData) {
    position = Position.absolute
    left = componentViewData.posX.em
    top = componentViewData.posY.em
    width = componentViewData.width.em
    height = componentViewData.height.em
    zIndex = integer(componentViewData.zIndex)
    opacity = number(componentViewData.opacity)
    display = if(componentViewData.isVisible) Display.flex else None.none
    pointerEvents = if(!componentViewData.isDisabled) PointerEvents.all else None.none
    rotate = componentViewData.rotation.deg.unsafeCast<Rotate>()
    scale = "${componentViewData.scaleX} ${componentViewData.scaleY} 1".unsafeCast<Scale>()
    transformOrigin = "center".unsafeCast<TransformOrigin>()
}

internal fun PropertiesBuilder.cssBuilder(componentViewData: UIComponentData) {
    cssBuilder(componentViewData as ComponentViewData)
}

internal fun PropertiesBuilder.cssBuilder(componentViewData: LabeledUIComponentData) {
    cssBuilder(componentViewData as ComponentViewData)
    alignmentBuilder(componentViewData)
}

internal fun PropertiesBuilder.cssTextBuilder(componentViewData: LabeledUIComponentData) {
    textOverflow = if(componentViewData.isWrapText) TextOverflow.clip else TextOverflow.ellipsis
    whiteSpace = if(componentViewData.isWrapText) WhiteSpace.normal else WhiteSpace.nowrap
    maxWidth = 100.pct
    maxHeight = 100.pct
    overflow = Overflow.hidden
    position = Position.absolute
    fontBuilder(componentViewData)
}

internal fun PropertiesBuilder.alignmentBuilder(componentViewData: LabeledUIComponentData) {
    justifyContent = when(componentViewData.alignment.first) {
        "left" -> JustifyContent.flexStart
        "center" -> JustifyContent.center
        "right" -> JustifyContent.flexEnd
        else -> JustifyContent.center
    }
    textAlign = when(componentViewData.alignment.first) {
        "left" -> TextAlign.left
        "center" -> TextAlign.center
        "right" -> TextAlign.right
        else -> TextAlign.center
    }
    alignItems = when(componentViewData.alignment.second) {
        "top" -> AlignItems.flexStart
        "center" -> AlignItems.center
        "bottom" -> AlignItems.flexEnd
        else -> AlignItems.center
    }
}

internal fun PropertiesBuilder.alignmentBuilder(componentViewData: CardStackData) {
    justifyContent = when(componentViewData.alignment.first) {
        "left" -> JustifyContent.flexStart
        "center" -> JustifyContent.center
        "right" -> JustifyContent.flexEnd
        else -> JustifyContent.center
    }
    alignItems = when(componentViewData.alignment.second) {
        "top" -> AlignItems.flexStart
        "center" -> AlignItems.center
        "bottom" -> AlignItems.flexEnd
        else -> AlignItems.center
    }
}

internal fun PropertiesBuilder.fontBuilder(componentViewData: UIComponentData) {
    fontStyle = componentViewData.font!!.fontStyle.let { it.unsafeCast<FontStyle>() }
    fontWeight = integer(componentViewData.font!!.fontWeight)
    fontSize = componentViewData.font!!.size.em
    fontFamily = cssFont(componentViewData.font!!.family)
    color = componentViewData.font!!.color.unsafeCast<Color>()
}

internal fun PropertiesBuilder.placeholderFontBuilder(componentViewData: UIComponentData) {
    color = componentViewData.font!!.color.unsafeCast<Color>()
    opacity = number(0.65)
}

internal fun PropertiesBuilder.inputBuilder(componentViewData: TextInputUIComponentData) {
    position = Position.absolute
    width = 100.pct
    height = 100.pct
    padding = 0.px
    margin = 0.px
    border = None.none
    appearance = None.none
    backgroundColor = rgb(0, 0, 0, 0.0)
}

internal fun PropertiesBuilder.comboBoxBuilder(componentViewData: ComboBoxData) {
    position = Position.absolute
    width = 100.pct
    height = 100.pct
    padding = 0.px
    margin = 0.px
    border = None.none
    appearance = None.none
    backgroundColor = rgb(0, 0, 0, 0.0)
}

internal fun PropertiesBuilder.styleBuilder(style : Map<String, String>) {
    borderRadius = cssBorderRadius(style["border-radius"] ?: "0rem")
    cursor = style["cursor"]?.unsafeCast<Cursor>() ?: "auto".unsafeCast<Cursor>()
}

internal fun PropertiesBuilder.filterBuilder(filters : Map<String, String?>) {
    val filterList = mutableListOf<String>()
    filters.values.forEach {
        if(it != null) {
            filterList.add(it)
        }
    }
    filter = cssFilter(filterList)
}

internal fun PropertiesBuilder.flipBuilder(flipped : String) {
    if(flipped == "horizontal") {
        transform = scalex(-1)
    } else if(flipped == "vertical") {
        transform = scaley(-1)
    } else if(flipped == "both") {
        transform = scale(-1, -1)
    }
}

internal fun cssBorderRadius(value : String): LengthProperty =
    value.unsafeCast<LengthProperty>()

internal fun cssFont(value : String): FontFamily = "'$value'".unsafeCast<FontFamily>()

internal fun cssFilter(values : List<String>): FilterFunction {
    if(values.isEmpty()) return "none".unsafeCast<FilterFunction>()
    return values.joinToString(" ").unsafeCast<FilterFunction>()
}
