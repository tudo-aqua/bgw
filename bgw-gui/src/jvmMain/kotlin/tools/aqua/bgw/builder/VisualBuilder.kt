@file:Suppress("DuplicatedCode")

package tools.aqua.bgw.builder

import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.visual.*

internal object VisualBuilder {
    fun build(visual: Visual) {
        when (visual) {
            is CompoundVisual -> buildCompoundVisual(visual)
            is ColorVisual -> buildColorVisual(visual)
            is ImageVisual -> buildImageVisual(visual)
            is TextVisual -> buildTextVisual(visual)
        }
    }

    private fun buildCompoundVisual(visual: CompoundVisual) {
        visual.childrenProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.children.forEach { build(it) }
    }

    private fun buildSingleLayerVisual(visual: SingleLayerVisual) {
        visual.transparencyProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.styleProperty.guiListener = { Frontend.updateVisual(visual) }
        visual.filtersProperty.guiListener = { Frontend.updateVisual(visual) }
        visual.flippedProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        when (visual) {
            is ColorVisual -> buildColorVisual(visual)
            is ImageVisual -> buildImageVisual(visual)
            is TextVisual -> buildTextVisual(visual)
        }
    }

    private fun buildColorVisual(visual: ColorVisual) {
        visual.colorProperty.guiListener = { _, _ ->
            Frontend.updateVisual(visual)
        }
    }

    private fun buildImageVisual(visual: ImageVisual) {
        visual.pathProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    }

    private fun buildTextVisual(visual: TextVisual) {
        visual.textProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.fontProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.alignmentProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.offsetXProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.offsetYProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    }

}
