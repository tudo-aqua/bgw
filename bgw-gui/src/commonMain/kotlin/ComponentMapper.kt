import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
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
                text = componentView.text
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

object LayoutMapper {
    fun map(layout: LayoutView<*>) : ComponentViewData {
        return when (layout) {
            is Pane<*> -> PaneData<ComponentViewData>().apply {
                id = layout.id
                posX = layout.posX
                posY = layout.posY
                width = layout.width
                height = layout.height
                visual = VisualMapper.map(layout.visual)
                components = layout.components.map {
                    println("Mapping component INSIDE: $it")
                    when(it) {
                        is LayoutView<*> -> {
                            println("Mapping INSIDE >> Layout: $it")
                            LayoutMapper.map(it)
                        }
                        is ComponentView -> {
                            println("Mapping INSIDE >> Component: $it")
                            ComponentMapper.map(it)
                        }
                        else -> throw IllegalArgumentException("Unknown component type: ${it::class.simpleName}")
                    }
                }
            }
            else -> throw IllegalArgumentException("Unknown layout type: ${layout::class.simpleName}")
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
            components = scene.components.map {
                println("Mapping component: $it")
                when(it) {
                    is LayoutView<*> -> {
                        println("Mapping >> Layout: $it")
                        LayoutMapper.map(it)
                    }
                    is ComponentView -> {
                        println("Mapping >> Component: $it")
                        ComponentMapper.map(it)
                    }
                    else -> throw IllegalArgumentException("Unknown component type: ${it::class.simpleName}")
                }
            }
            width = scene.width
            height = scene.height
            background = VisualMapper.map(scene.background)
        }
    }
}