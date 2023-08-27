package tools.aqua.bgw.builder

import tools.aqua.bgw.visual.*

object VisualBuilder {
    fun build(visual: Visual) {
        when(visual) {
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

    private fun buildColorVisual(visual: ColorVisual) {
        visual.transparencyProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.colorProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    }

    private fun buildImageVisual(visual: ImageVisual) {
        visual.transparencyProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    }

    private fun buildTextVisual(visual: TextVisual) {
        visual.textProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.fontProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.alignmentProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.offsetXProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
        visual.offsetYProperty.guiListener = { _, _ -> Frontend.updateVisual(visual) }
    }

}
