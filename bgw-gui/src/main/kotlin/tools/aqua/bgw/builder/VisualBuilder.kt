/*
 * Copyright 2021-2024 The BoardGameWork Authors
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

package tools.aqua.bgw.builder

import java.awt.image.BufferedImage
import javafx.geometry.Insets
import javafx.scene.CacheHint
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import kotlin.math.abs
import tools.aqua.bgw.builder.FXConverters.toFXColor
import tools.aqua.bgw.builder.FXConverters.toFXFontCSS
import tools.aqua.bgw.builder.FXConverters.toFXPos
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.visual.*

/** VisualBuilder. Factory for BGW visuals. */
object VisualBuilder {
  /** Max value of HEX ind decimal. */
  internal const val MAX_HEX = 255.0

  /** Builds [Visual]. */
  internal fun build(componentView: ComponentView): Pane {
    val root = Pane()

    componentView.visualProperty.setGUIListenerAndInvoke(componentView.visual) { _, nV ->
      root.children.clear()
      root.children.add(
          buildVisual(nV).apply {
            prefWidthProperty().bind(root.prefWidthProperty())
            prefHeightProperty().bind(root.prefHeightProperty())
          })
    }

    return root
  }

  /** Switches between visuals. */
  internal fun buildVisual(visual: Visual): Region =
      when (visual) {
        is ColorVisual -> buildColorVisual(visual)
        is ImageVisual -> buildImageVisual(visual)
        is TextVisual -> buildTextVisual(visual)
        is CompoundVisual -> {
          buildCompoundVisual(visual)
        }
        else -> throw IllegalArgumentException("Unknown visual type: ${visual::class.simpleName}")
      }

  /** Builds [ColorVisual]. */
  private fun buildColorVisual(visual: ColorVisual) =
      Pane().apply {
        visual.colorProperty.setGUIListenerAndInvoke(visual.color) { _, nV ->
          style = "-fx-background-color: #${"%02x".format(nV.rgb).substring(2)};${visual.style}"
          opacity = nV.alpha / MAX_HEX * visual.transparency
        }

        visual.transparencyProperty.setGUIListenerAndInvoke(visual.transparency) { _, nV ->
          opacity = visual.color.alpha / MAX_HEX * nV
        }

        visual.styleProperty.setGUIListenerAndInvoke(visual.style) { _, nV ->
          style = "-fx-background-color: #${"%02x".format(visual.color.rgb).substring(2)};${nV}"
        }
      }

  private val cache: MutableMap<String, Image> = mutableMapOf()

  /** Builds [ImageVisual]. */
  private fun buildImageVisual(visual: ImageVisual) =
      Pane().apply {
        val imageView =
            ImageView().apply {
              isCache = true
              cacheHint = CacheHint.SPEED
            }

        visual.imageProperty.setGUIListenerAndInvoke(visual.image) { _, nV ->
          imageView.image = nV.readImage()
        }

        visual.transparencyProperty.setGUIListenerAndInvoke(visual.transparency) { _, nV ->
          opacity = nV
        }

        visual.styleProperty.setGUIListenerAndInvoke(visual.style) { _, nV -> style = nV }

        imageView.fitWidthProperty().bind(prefWidthProperty())
        imageView.fitHeightProperty().bind(prefHeightProperty())
        children.add(imageView)
      }

  /** Builds [TextVisual]. */
  private fun buildTextVisual(visual: TextVisual) =
      StackPane().apply {
        val label =
            Label().apply {
              minWidth = 0.0
              minHeight = 0.0
              visual.textProperty.setGUIListenerAndInvoke(visual.text) { _, nV -> text = nV }
              visual.transparencyProperty.setGUIListenerAndInvoke(visual.transparency) { _, nV ->
                opacity = nV
              }
              visual.fontProperty.setGUIListenerAndInvoke(visual.font) { _, nV ->
                style = nV.toFXFontCSS() + visual.style
                textFill = nV.color.toFXColor()
              }
              visual.styleProperty.setGUIListenerAndInvoke(visual.style) { _, nV ->
                style = visual.font.toFXFontCSS() + nV
              }
              wrapTextProperty().set(true)
            }
        visual.alignmentProperty.setGUIListenerAndInvoke(visual.alignment) { _, nV ->
          this.alignment = nV.toFXPos()
        }
        visual.offsetXProperty.setGUIListenerAndInvoke(visual.offsetX) { _, _ ->
          this.newPadding(visual.offsetX, visual.offsetY)
        }
        visual.offsetYProperty.setGUIListenerAndInvoke(visual.offsetX) { _, _ ->
          this.newPadding(visual.offsetX, visual.offsetY)
        }
        children.add(label)
      }

  /** Sets a new padding for this [StackPane] based on the supplied offsets. */
  private fun StackPane.newPadding(offsetX: Double, offsetY: Double) {
    val topInset = if (offsetY >= 0) offsetY else 0.0
    val rightInset = if (offsetX < 0) abs(offsetX) else 0.0
    val bottomInset = if (offsetY < 0) abs(offsetY) else 0.0
    val leftInset = if (offsetX >= 0) offsetX else 0.0
    padding = Insets(topInset, rightInset, bottomInset, leftInset)
  }

  /** Builds [CompoundVisual]. */
  private fun buildCompoundVisual(visual: CompoundVisual) =
      StackPane().apply {
        visual.setGUIListenerAndInvoke {
          children.clear()
          children.addAll(
              visual.children
                  .map { buildVisual(it) }
                  .onEach {
                    it.prefWidthProperty().bind(prefWidthProperty())
                    it.prefHeightProperty().bind(prefHeightProperty())
                  })
        }
      }

  /** Reads [BufferedImage] to [Image]. */
  private fun BufferedImage.readImage(): Image {
    val imageWriter = WritableImage(width, height)
    val pixelWriter = imageWriter.pixelWriter
    for (x in 0 until width) {
      for (y in 0 until height) {
        pixelWriter.setArgb(x, y, getRGB(x, y))
      }
    }
    return imageWriter
  }
}
