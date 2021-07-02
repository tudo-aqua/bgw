package tools.aqua.bgw.builder

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.image.WritableImage
import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import javafx.scene.layout.StackPane
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.visual.*
import java.awt.image.BufferedImage

/**
 * VisualBuilder.
 * Factory for BGW visuals.
 */
internal class VisualBuilder {
	companion object {
		internal const val MAX_HEX = 255.0
		
		internal fun build(elementView: ElementView): Pane {
			val root = Pane()
			
			elementView.visualProperty.setGUIListenerAndInvoke(elementView.visual) { _, nV ->
				root.children.clear()
				root.children.add(buildVisual(nV).apply {
					prefWidthProperty().bind(root.prefWidthProperty())
					prefHeightProperty().bind(root.prefHeightProperty())
				})
			}
			
			return root
		}
		
		internal fun buildVisual(visual: Visual): Region =
			when (visual) {
				is ColorVisual ->
					Pane().apply {
						visual.colorProperty.setGUIListenerAndInvoke(visual.color) { _, nV ->
							style = "-fx-background-color: #${Integer.toHexString(nV.rgb).substring(2)};"
							opacity = (nV.alpha / MAX_HEX) * visual.transparency
						}

						visual.transparencyProperty.setGUIListenerAndInvoke(visual.transparency) { _, nV ->
							opacity = (visual.color.alpha / MAX_HEX) * nV
						}
					}
				
				is ImageVisual ->
					Pane().apply {
						val imageView = ImageView()
						
						visual.imageProperty.setGUIListenerAndInvoke(visual.image) { _, nV ->
							imageView.image = nV.readImage()
						}

						visual.transparencyProperty.setGUIListenerAndInvoke(visual.transparency) { _, nV ->
							opacity = nV
						}
						
						imageView.fitWidthProperty().bind(prefWidthProperty())
						imageView.fitHeightProperty().bind(prefHeightProperty())
						children.add(imageView)
					}
				
				is TextVisual ->
					Label().apply {
						visual.textProperty.setGUIListenerAndInvoke(visual.text) { _, nV ->
							text = nV
						}
						visual.transparencyProperty.setGUIListenerAndInvoke(visual.transparency) { _, nV ->
							opacity = nV
						}
						visual.fontProperty.setGUIListenerAndInvoke(visual.font) { _, nV ->
							font = nV.toFXFont()
							textFill = nV.color.toFXColor()
						}
					}
				
				is CompoundVisual -> {
					StackPane().apply {
						visual.setGUIListenerAndInvoke { this.rebuildCompoundVisual(visual) }
					}
				}
			}
		
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

		private fun StackPane.rebuildCompoundVisual(compoundVisual: CompoundVisual) {
			children.clear()
			children.addAll(compoundVisual.children.map { buildVisual(it) }.onEach {
				it.prefWidthProperty().bind(prefWidthProperty())
				it.prefHeightProperty().bind(prefHeightProperty())
			})
			compoundVisual.transparencyProperty.setGUIListenerAndInvoke(compoundVisual.transparency) { _, nV ->
				opacity = nV
			}
		}
	}
}