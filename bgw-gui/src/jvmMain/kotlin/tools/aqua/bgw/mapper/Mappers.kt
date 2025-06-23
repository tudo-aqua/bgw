package tools.aqua.bgw.mapper

import AppData
import ColorVisualData
import ComponentViewData
import CompoundVisualData
import FontData
import ImageVisualData
import SceneData
import SingleLayerVisualData
import TextVisualData
import VisualData
import tools.aqua.bgw.application.Constants
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.GameComponentContainer
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.core.DEFAULT_BLUR_RADIUS
import tools.aqua.bgw.core.Frontend
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.style.Filter
import tools.aqua.bgw.style.Style
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.ImageVisual
import tools.aqua.bgw.visual.SingleLayerVisual
import tools.aqua.bgw.visual.TextVisual
import tools.aqua.bgw.visual.Visual


internal object StyleMapper {
    fun map(style: Style): Map<String, String> {
        return style.mapDeclarations()
    }
}

internal object FilterMapper {
    fun map(filters: Filter): Map<String, String> {
        return filters.mapDeclarations()
    }
}

internal object FontMapper {
    private val fontWeightMap =
        mapOf(
            Font.FontWeight.THIN to 100,
            Font.FontWeight.EXTRA_LIGHT to 200,
            Font.FontWeight.LIGHT to 300,
            Font.FontWeight.NORMAL to 400,
            Font.FontWeight.MEDIUM to 500,
            Font.FontWeight.SEMI_BOLD to 600,
            Font.FontWeight.BOLD to 700,
            Font.FontWeight.EXTRA_BOLD to 800,
            Font.FontWeight.BLACK to 900)

    fun map(font: Font): FontData {
        return FontData().apply {
            size = font.size.toInt()
            color =
                "rgba(${font.color.red}, ${font.color.green}, ${font.color.blue}, ${font.color.alpha})"
            family = font.family
            fontWeight = fontWeightMap[font.fontWeight] ?: 400
            fontStyle = font.fontStyle.name.lowercase()
        }
    }
}

internal object VisualMapper {
    fun map(visual: Visual): VisualData {
        val visualData =
            when (visual) {
                is ColorVisual ->
                    ColorVisualData().apply {
                        id = visual.id
                        color =
                            "rgba(${visual.color.red}, ${visual.color.green}, ${visual.color.blue}, ${visual.color.alpha})"
                        transparency = visual.transparency
                        style = StyleMapper.map(visual.style)
                        filters = FilterMapper.map(visual.filters)
                        flipped = visual.flipped.name.lowercase()
                        rotation = visual.rotation
                    }
                is ImageVisual ->
                    ImageVisualData().apply {
                        id = visual.id
                        if (isRelativeFilePath(visual.path))
                            path = "http://localhost:${Constants.PORT}/static/${visual.path}"
                        else path = visual.path
                        width = visual.width
                        height = visual.height
                        offsetX = visual.offsetX
                        offsetY = visual.offsetY
                        transparency = visual.transparency
                        style = StyleMapper.map(visual.style)
                        filters = FilterMapper.map(visual.filters)
                        flipped = visual.flipped.name.lowercase()
                        rotation = visual.rotation
                    }
                is TextVisual ->
                    TextVisualData().apply {
                        id = visual.id
                        text = visual.text
                        font = FontMapper.map(visual.font)
                        offsetX = visual.offsetX.toInt()
                        offsetY = visual.offsetY.toInt()
                        transparency = visual.transparency
                        style = StyleMapper.map(visual.style)
                        filters = FilterMapper.map(visual.filters)
                        flipped = visual.flipped.name.lowercase()
                        rotation = visual.rotation
                        alignment =
                            Pair(
                                visual.alignment.horizontalAlignment.name.lowercase(),
                                visual.alignment.verticalAlignment.name.lowercase())
                    }

                // --- Compound Visuals ---

                is CompoundVisual -> {
                    CompoundVisualData().apply {
                        id = visual.id
                        children = visual.children.map { mapChildren(it) }
                    }
                }
                else -> {
                    throw IllegalArgumentException("Unknown visual type: ${visual::class.simpleName}")
                }
            }
        return if (visualData is SingleLayerVisualData) {
            CompoundVisualData().apply {
                id = visualData.id
                children = listOf(visualData)
            }
        } else {
            visualData
        }
    }

    private fun mapChildren(visual: SingleLayerVisual): SingleLayerVisualData {
        return when (visual) {
            is ColorVisual ->
                ColorVisualData().apply {
                    id = visual.id
                    color =
                        "rgba(${visual.color.red}, ${visual.color.green}, ${visual.color.blue}, ${visual.color.alpha})"
                    transparency = visual.transparency
                    style = StyleMapper.map(visual.style)
                    filters = FilterMapper.map(visual.filters)
                    flipped = visual.flipped.name.lowercase()
                    rotation = visual.rotation
                }
            is ImageVisual ->
                ImageVisualData().apply {
                    id = visual.id
                    if (isRelativeFilePath(visual.path))
                        path = "http://localhost:${Constants.PORT}/static/${visual.path}"
                    else path = visual.path
                    width = visual.width
                    height = visual.height
                    offsetX = visual.offsetX
                    offsetY = visual.offsetY
                    transparency = visual.transparency
                    style = StyleMapper.map(visual.style)
                    filters = FilterMapper.map(visual.filters)
                    flipped = visual.flipped.name.lowercase()
                    rotation = visual.rotation
                }
            is TextVisual ->
                TextVisualData().apply {
                    id = visual.id
                    text = visual.text
                    font = FontMapper.map(visual.font)
                    offsetX = visual.offsetX.toInt()
                    offsetY = visual.offsetY.toInt()
                    transparency = visual.transparency
                    style = StyleMapper.map(visual.style)
                    filters = FilterMapper.map(visual.filters)
                    flipped = visual.flipped.name.lowercase()
                    rotation = visual.rotation
                    alignment =
                        Pair(
                            visual.alignment.horizontalAlignment.name.lowercase(),
                            visual.alignment.verticalAlignment.name.lowercase())
                }
        }
    }

    // TODO: Check properly if path is URL or local path
    private fun isRelativeFilePath(path: String): Boolean {
        return !path.startsWith("http://") &&
                !path.startsWith("https://") &&
                !path.startsWith("data:image/png;base64,")
    }
}

internal object FontFaceMapper {
    private val fontWeightMap =
        mapOf(
            Font.FontWeight.THIN to 100,
            Font.FontWeight.EXTRA_LIGHT to 200,
            Font.FontWeight.LIGHT to 300,
            Font.FontWeight.NORMAL to 400,
            Font.FontWeight.MEDIUM to 500,
            Font.FontWeight.SEMI_BOLD to 600,
            Font.FontWeight.BOLD to 700,
            Font.FontWeight.EXTRA_BOLD to 800,
            Font.FontWeight.BLACK to 900)

    fun map(font: Triple<String, String, Font.FontWeight>): Triple<String, String, Int> {
        return Triple(font.first, font.second, fontWeightMap[font.third] ?: 400)
    }
}

internal object SceneMapper {
    private fun mapScene(scene: Scene<*>): SceneData {
        return SceneData().apply {
            id = scene.rootNode.id
            components = scene.components.map { RecursiveMapper.map(it) }.toMutableList()
            locked = if (scene is BoardGameScene) scene.lockedProperty.value else false
            width = scene.width.toInt()
            height = scene.height.toInt()
            background = VisualMapper.map(scene.background)
        }
    }

    fun map(menuScene: MenuScene? = null, gameScene: BoardGameScene? = null): AppData {
        return AppData().apply {
            this.width = Frontend.widthProperty.value.toInt()
            this.height = Frontend.heightProperty.value.toInt()
            this.background = VisualMapper.map(Frontend.backgroundProperty.value)
            this.alignment =
                Pair(
                    Frontend.alignmentProperty.value.horizontalAlignment.name.lowercase(),
                    Frontend.alignmentProperty.value.verticalAlignment.name.lowercase())
            this.menuScene = if (menuScene != null) mapScene(menuScene) else null
            this.gameScene = if (gameScene != null) mapScene(gameScene) else null
            this.fadeTime = Frontend.lastFadeTime.toInt()
            this.blurRadius = menuScene?.blurRadius ?: DEFAULT_BLUR_RADIUS
        }
    }
}