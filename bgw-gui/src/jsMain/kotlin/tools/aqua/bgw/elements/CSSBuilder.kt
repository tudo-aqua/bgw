/*
 * Copyright 2025-2026 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua.bgw.elements

import AppData
import CardStackData
import ComboBoxData
import ComponentViewData
import LabeledUIComponentData
import TextInputUIComponentData
import UIComponentData
import csstype.Properties
import csstype.PropertiesBuilder
import kotlin.js.asDynamic
import tools.aqua.bgw.DraggableResult
import web.cssom.*

internal fun PropertiesBuilder.cssBuilder(componentViewData: ComponentViewData) {
  position = Position.absolute
  left = componentViewData.posX.bgw
  top = componentViewData.posY.bgw
  width = componentViewData.width.bgw
  height = componentViewData.height.bgw

  if (componentViewData.isGridCell) minWidth = componentViewData.width.bgw
  if (componentViewData.isGridCell) minHeight = componentViewData.height.bgw

  zIndex = integer(componentViewData.zIndex)
  display = if (componentViewData.isVisible) Display.flex else None.none
  pointerEvents = if (!componentViewData.isDisabled) Globals.inherit else None.none
  // rotate = componentViewData.rotation.deg.unsafeCast<Rotate>()
  // scale = "${componentViewData.scaleX} ${componentViewData.scaleY} 1".unsafeCast<Scale>()
  translate =
      "calc(var(--txAnim) * var(--bgwUnit)) calc(var(--tyAnim) * var(--bgwUnit))"
          .unsafeCast<Translate>()
  rotate = "var(--rotAnim)".unsafeCast<Rotate>()
  // scale = "var(--sxAnim) var(--syAnim)".unsafeCast<Scale>()

  transformOrigin = "center".unsafeCast<TransformOrigin>()
  transform =
      "translate(var(--tx), var(--ty)) rotateZ(var(--rot)) rotateY(var(--flipAnim, 0)) scale(var(--sxAnim, var(--sx)), var(--syAnim, var(--sy)))"
          .unsafeCast<Transform>()

  opacity = "var(--opaAnim, var(--opa))".unsafeCast<Opacity>()

  set(CustomPropertyName("--tx"), 0)
  set(CustomPropertyName("--ty"), 0)
  set(CustomPropertyName("--rot"), componentViewData.rotation.deg)
  set(CustomPropertyName("--sx"), componentViewData.scaleX)
  set(CustomPropertyName("--sy"), componentViewData.scaleY)
  set(CustomPropertyName("--opa"), componentViewData.opacity)
}

internal fun PropertiesBuilder.cssBackupTransformBuilder(componentViewData: ComponentViewData) {
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
  textOverflow = if (componentViewData.isWrapText) TextOverflow.clip else TextOverflow.ellipsis
  whiteSpace = if (componentViewData.isWrapText) WhiteSpace.normal else WhiteSpace.nowrap
  maxWidth = 100.pct
  maxHeight = 100.pct
  overflow = Overflow.hidden
  position = Position.absolute
  fontBuilder(componentViewData)
}

internal fun PropertiesBuilder.alignmentBuilder(componentViewData: LabeledUIComponentData) {
  justifyContent =
      when (componentViewData.alignment.first) {
        "left" -> JustifyContent.flexStart
        "center" -> JustifyContent.center
        "right" -> JustifyContent.flexEnd
        else -> JustifyContent.center
      }
  textAlign =
      when (componentViewData.alignment.first) {
        "left" -> TextAlign.left
        "center" -> TextAlign.center
        "right" -> TextAlign.right
        else -> TextAlign.center
      }
  alignItems =
      when (componentViewData.alignment.second) {
        "top" -> AlignItems.flexStart
        "center" -> AlignItems.center
        "bottom" -> AlignItems.flexEnd
        else -> AlignItems.center
      }
}

internal fun PropertiesBuilder.alignmentBuilder(componentViewData: CardStackData) {
  justifyContent =
      when (componentViewData.alignment.first) {
        "left" -> JustifyContent.flexStart
        "center" -> JustifyContent.center
        "right" -> JustifyContent.flexEnd
        else -> JustifyContent.center
      }
  alignItems =
      when (componentViewData.alignment.second) {
        "top" -> AlignItems.flexStart
        "center" -> AlignItems.center
        "bottom" -> AlignItems.flexEnd
        else -> AlignItems.center
      }
}

internal fun PropertiesBuilder.alignmentBuilder(data: AppData) {
  justifyContent =
      when (data.alignment.first) {
        "left" -> JustifyContent.flexStart
        "center" -> JustifyContent.center
        "right" -> JustifyContent.flexEnd
        else -> JustifyContent.center
      }
  alignItems =
      when (data.alignment.second) {
        "top" -> AlignItems.flexStart
        "center" -> AlignItems.center
        "bottom" -> AlignItems.flexEnd
        else -> AlignItems.center
      }
}

internal fun PropertiesBuilder.fontBuilder(componentViewData: UIComponentData) {
  fontStyle = componentViewData.font!!.fontStyle.let { it.unsafeCast<FontStyle>() }
  fontWeight = integer(componentViewData.font!!.fontWeight)
  fontSize = componentViewData.font!!.size.bgw
  fontFamily = cssFont(componentViewData.font!!.family)
  color = componentViewData.font!!.color.unsafeCast<Color>()
}

internal fun PropertiesBuilder.simpleFontBuilder(componentViewData: UIComponentData) {
  fontStyle = componentViewData.font!!.fontStyle.let { it.unsafeCast<FontStyle>() }
  fontWeight = integer(componentViewData.font!!.fontWeight)
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

internal fun PropertiesBuilder.styleBuilder(style: Map<String, String>) {
  borderRadius = cssBorderRadius(style["border-radius"] ?: "0rem")
  cursor = style["cursor"]?.unsafeCast<Cursor>() ?: "auto".unsafeCast<Cursor>()
}

internal fun PropertiesBuilder.filterBuilder(filters: Map<String, String>) {
  val filterList = mutableListOf<String>()
  filters.values.forEach {
    if (it.trim() != "") {
      filterList.add(it)
    }
  }
  filter = cssFilter(filterList)
}

internal fun PropertiesBuilder.flipBuilder(flipped: String, rotation: Double) {
  transform = flipAndRotationBuilder(flipped, rotation).unsafeCast<Transform>()
}

internal fun flipAndRotationBuilder(flipped: String, rotation: Double): String {
  return when (flipped) {
    "horizontal" -> "scaleX(-1) rotateZ(${rotation}deg)"
    "vertical" -> "scaleY(-1) rotateZ(${rotation}deg)"
    "both" -> "scaleX(-1) scaleY(-1) rotateZ(${rotation}deg)"
    else -> "rotateZ(${rotation}deg)"
  }
}

internal fun cssBorderRadius(value: String): LengthProperty = value.unsafeCast<LengthProperty>()

internal fun cssFont(value: String): FontFamily = "'$value'".unsafeCast<FontFamily>()

internal fun cssFilter(values: List<String>): FilterFunction {
  if (values.isEmpty()) return "none".unsafeCast<FilterFunction>()
  return values.joinToString(" ").unsafeCast<FilterFunction>()
}

internal fun applyDraggableTransform(
    draggable: DraggableResult,
    componentData: ComponentViewData
): Properties {
  // Get the drag delta in screen coordinates
  val screenX = draggable.transform?.x ?: 0.0
  val screenY = draggable.transform?.y ?: 0.0

  // Calculate parent's combined scale (excluding this component's own scale)
  val parentScaleX = componentData.propagatedScaleX / componentData.scaleX
  val parentScaleY = componentData.propagatedScaleY / componentData.scaleY

  // Counter-rotate the drag delta by the negative of parent's propagated rotation
  // to convert from screen coordinates to the local coordinate system
  val parentRotation = componentData.propagatedRotation - componentData.rotation
  val angleRad = -parentRotation * kotlin.math.PI / 180.0
  val cosAngle = kotlin.math.cos(angleRad)
  val sinAngle = kotlin.math.sin(angleRad)

  // Apply rotation matrix to transform coordinates
  val rotatedX = screenX * cosAngle - screenY * sinAngle
  val rotatedY = screenX * sinAngle + screenY * cosAngle

  // Divide by parent scale to compensate for parent container scaling
  val localX = rotatedX / parentScaleX
  val localY = rotatedY / parentScaleY

  return jsObject {
    val styleObj = asDynamic()
    styleObj.`--tx` = "${localX}px"
    styleObj.`--ty` = "${localY}px"
  }
}

internal fun resetDraggableTransform(): Properties {
  return jsObject {
    val styleObj = asDynamic()
    styleObj.transform = "none"
  }
}
