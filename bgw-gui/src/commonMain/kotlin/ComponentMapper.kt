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
        }
    }
}

object VisualMapper {
    fun map(visual: Visual) : VisualData {
        return when (visual) {
            is ColorVisual -> ColorVisualData().apply {
                id = visual.id
                color = "rgb(${visual.red}, ${visual.green}, ${visual.blue})"
            }
        }
    }
}

object SceneMapper {
    fun map(scene: Scene<*>) : SceneData {
        return SceneData().apply {
            components = scene.components.map { ComponentMapper.map(it) }
        }
    }
}