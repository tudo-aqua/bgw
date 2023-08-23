import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.visual.*

object ComponentMapper {
    fun map(componentView: ComponentView) : ComponentViewData {
        return when (componentView) {
            is Button -> ButtonData().apply {
                id = componentView.id
                posX = componentView.posX
                posY = componentView.posY
                width = componentView.width
                height = componentView.height
                visual = VisualMapper.map(componentView.visual)
            }

            is Label -> LabelData().apply {
                id = componentView.id
                posX = componentView.posX
                posY = componentView.posY
                width = componentView.width
                height = componentView.height
                visual = VisualMapper.map(componentView.visual)
                text = componentView.text
            }
            else -> TODO("Not implemented")
        }
    }
}

object VisualMapper {
    fun map(visual: Visual) : VisualData {
        return when (visual) {
            is ColorVisual -> ColorVisualData().apply {
                id = visual.id
                color = "rgb(${visual.color.red}, ${visual.color.green}, ${visual.color.blue})"
            }
            is CompoundVisual -> TODO("Not implemented")
            is ImageVisual -> TODO("Not implemented")
            is TextVisual -> TODO("Not implemented")
        }
    }
}

object SceneMapper {
    fun map(scene: Scene<*>) : SceneData {
        return SceneData().apply {
            components = scene.components.map { ComponentMapper.map(it) }
            width = scene.width
            height = scene.height
            background = VisualMapper.map(scene.background)
        }
    }
}