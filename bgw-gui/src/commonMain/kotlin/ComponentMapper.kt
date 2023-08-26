import ComponentMapper.fillData
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.*

object ComponentMapper {
    fun ComponentViewData.fillData(componentView: ComponentView) : ComponentViewData {
        return this.apply {
            id = componentView.id
            posX = componentView.posX
            posY = componentView.posY
            width = componentView.width
            height = componentView.height
            visual = VisualMapper.map(componentView.visual)
            // zIndex
            // opacity
            // isVisible
            // isDisabled
            // isFocusable
            // scaleX
            // scaleY
            // rotation
            // layoutFromCenter
            // isDraggable
        }
    }

    private fun mapSpecific(componentView: ComponentView) : ComponentViewData {
        return when(componentView) {
            is LabeledUIComponent -> {
                when(componentView) {
                    is BinaryStateButton -> BinaryStateButtonData().fillData(componentView) as BinaryStateButtonData
                    is Button -> ButtonData().fillData(componentView) as ButtonData
                    is CheckBox -> CheckBoxData().fillData(componentView) as CheckBoxData
                    is Label -> LabelData().fillData(componentView) as LabelData
                    is RadioButton -> RadioButtonData().fillData(componentView) as RadioButtonData
                    is ToggleButton -> ToggleButtonData().fillData(componentView) as ToggleButtonData
                    else -> throw IllegalArgumentException("Unknown component type: ${componentView::class.simpleName}")
                }.apply {
                    font = FontMapper.map(componentView.font)
                    // alignment
                    text = componentView.text
                    isWrapText = componentView.isWrapText
                }
            }

            is TextInputUIComponent -> {
                when(componentView) {
                    is PasswordField -> PasswordFieldData().fillData(componentView) as PasswordFieldData
                    is TextField -> TextFieldData().fillData(componentView) as TextFieldData
                    is TextArea -> TextAreaData().fillData(componentView) as TextAreaData
                    else -> throw IllegalArgumentException("Unknown component type: ${componentView::class.simpleName}")
                }.apply {
                    font = FontMapper.map(componentView.font)
                    text = componentView.text
                    prompt = componentView.prompt
                }
            }

            is UIComponent -> {
                when(componentView) {
                    is ComboBox<*> -> ComboBoxData().fillData(componentView) as ComboBoxData
                    is ColorPicker -> ColorPickerData().fillData(componentView) as ColorPickerData
                    is ProgressBar -> ProgressBarData().fillData(componentView) as ProgressBarData
                    else -> throw IllegalArgumentException("Unknown component type: ${componentView::class.simpleName}")
                }.apply {
                    font = FontMapper.map(componentView.font)
                }
            }

            is StructuredDataView<*> -> {
                when(componentView) {
                    is ListView<*> -> ListViewData().fillData(componentView) as ListViewData
                    is TableView<*> -> TableViewData().fillData(componentView) as TableViewData
                    else -> throw IllegalArgumentException("Unknown component type: ${componentView::class.simpleName}")
                }.apply {
                    items = componentView.items.map { it.toString() }
                    selectionMode = componentView.selectionMode.name.lowercase()
                    selectionBackground = VisualMapper.map(componentView.selectionBackground) as ColorVisualData
                    font = FontMapper.map(componentView.font)
                }
            }

            is CameraPane<*> -> (CameraPaneData().fillData(componentView) as CameraPaneData).apply {
                target = LayoutMapper.map(componentView.target)
                zoom = componentView.zoom
                interactive = componentView.interactive
                // ! nightly - isVerticalLocked
                // ! nightly - isHorizontalLocked
                // ! nightly - isZoomLocked
                // ! nightly - panMouseButton
            }

            is GameComponentView -> {
                when(componentView) {
                    is CardView -> CardViewData().fillData(componentView) as CardViewData
                    is DiceView -> DiceViewData().fillData(componentView) as DiceViewData
                    is HexagonView -> HexagonViewData().fillData(componentView) as HexagonViewData
                    is TokenView -> TokenViewData().fillData(componentView) as TokenViewData
                    else -> throw IllegalArgumentException("Unknown component type: ${componentView::class.simpleName}")
                }
            }

            else -> throw IllegalArgumentException("Unknown component type: ${componentView::class.simpleName}")
        }
    }

    fun map(componentView: ComponentView) : ComponentViewData {
        println("Mapping ComponentView: $componentView")
        return when (componentView) {

            // TODO - LabeledUIComponent
            is BinaryStateButton -> (mapSpecific(componentView) as BinaryStateButtonData).apply {
                isSelected = componentView.isSelected
                // buttons
            }
            is Button -> (mapSpecific(componentView) as ButtonData)
            is CheckBox -> (mapSpecific(componentView) as CheckBoxData).apply {
                isChecked = componentView.isChecked
                allowIndeterminate = componentView.isIndeterminateAllowed
                isIndeterminate = componentView.isIndeterminate
            }
            is Label -> (mapSpecific(componentView) as LabelData)
            is RadioButton -> (mapSpecific(componentView) as RadioButtonData).apply {
                isSelected = componentView.isSelected
                // buttons
            }
            is ToggleButton -> (mapSpecific(componentView) as ToggleButtonData).apply {
                isSelected = componentView.isSelected
                // buttons
            }

            // TODO - TextInputUIComponent
            is PasswordField -> (mapSpecific(componentView) as PasswordFieldData)
            is TextField -> (mapSpecific(componentView) as TextFieldData)
            is TextArea -> (mapSpecific(componentView) as TextAreaData)

            // TODO - UIComponent
            is ComboBox<*> -> (mapSpecific(componentView) as ComboBoxData).apply {
                prompt = componentView.prompt
                items = componentView.items.map { it.toString() }
                selectedItem = componentView.selectedItem.toString()
            }
            is ColorPicker -> (mapSpecific(componentView) as ColorPickerData).apply {
                selectedColor = "rgba(${componentView.selectedColor.red}, ${componentView.selectedColor.green}, ${componentView.selectedColor.blue}, ${componentView.selectedColor.alpha})"
            }
            is ProgressBar -> (mapSpecific(componentView) as ProgressBarData).apply {
                progress = componentView.progress
                barColor = "rgba(${componentView.barColor.red}, ${componentView.barColor.green}, ${componentView.barColor.blue}, ${componentView.barColor.alpha})"
            }

            // TODO - StructuredDataView
            is ListView<*> -> (mapSpecific(componentView) as ListViewData).apply {
                // orientation
            }
            is TableView<*> -> (mapSpecific(componentView) as TableViewData).apply {
                // columns (as TableColumnData)
            }

            // TODO - ComponentView
            is CameraPane<*> -> (mapSpecific(componentView) as CameraPaneData)

            // TODO - GameComponentView
            is CardView -> (mapSpecific(componentView) as CardViewData).apply {
                currentSide = componentView.currentSide.name.lowercase()
                front = VisualMapper.map(componentView.frontVisual)
                back = VisualMapper.map(componentView.backVisual)
            }
            is DiceView -> (mapSpecific(componentView) as DiceViewData).apply {
                currentSide = componentView.currentSide
                visuals = componentView.visuals.map { VisualMapper.map(it) }
            }
            is HexagonView -> (mapSpecific(componentView) as HexagonViewData).apply {
                size = componentView.size as Double
            }
            is TokenView -> (mapSpecific(componentView) as TokenViewData)

            else -> TODO("Not implemented")
        }
    }
}

object FontMapper {
    private val fontWeightMap = mapOf(
        Font.FontWeight.THIN to 100,
        Font.FontWeight.EXTRA_LIGHT to 200,
        Font.FontWeight.LIGHT to 300,
        Font.FontWeight.NORMAL to 400,
        Font.FontWeight.MEDIUM to 500,
        Font.FontWeight.SEMI_BOLD to 600,
        Font.FontWeight.BOLD to 700,
        Font.FontWeight.EXTRA_BOLD to 800,
        Font.FontWeight.BLACK to 900
    )
    fun map(font: Font?) : FontData? {
        return if (font != null) {
            FontData().apply {
                size = font.size.toInt()
                color = "rgba(${font.color.red}, ${font.color.green}, ${font.color.blue}, ${font.color.alpha})"
                family = font.family
                fontWeight = fontWeightMap[font.fontWeight] ?: 400
                fontStyle = font.fontStyle.name.lowercase()
            }
        } else {
            null
        }
    }
}

object LayoutMapper {
    fun map(layout: LayoutView<*>) : LayoutViewData {
        return when (layout) {
            is Pane<*> -> (PaneData().fillData(layout) as PaneData).apply {
                components = layout.components.map { RecursiveMapper.map(it) }
            }
            else -> throw IllegalArgumentException("Unknown layout type: ${layout::class.simpleName}")
        }
    }
}

object RecursiveMapper {
    fun map(component : ComponentView) : ComponentViewData {
        return when (component) {
            is LayoutView<*> -> {
                LayoutMapper.map(component)
            }
            is GameComponentContainer<*> -> {
                ContainerMapper.map(component)
            }
            is ComponentView -> {
                ComponentMapper.map(component)
            }
            else -> throw IllegalArgumentException("Unknown component type: ${component::class.simpleName}")
        }
    }
}

object ContainerMapper {
    fun map(container: GameComponentContainer<*>) : GameComponentContainerData {
        return when (container) {
            is Area<*> -> (AreaData().fillData(container) as AreaData).apply {
                components = container.components.map { RecursiveMapper.map(it) } as List<GameComponentViewData>
            }

            is CardStack<*> -> (CardStackData().fillData(container) as CardStackData).apply {
                components = container.components.map { RecursiveMapper.map(it) } as List<GameComponentViewData>
            }

            is HexagonGrid<*> -> {
                val tempMap = mutableMapOf<String, HexagonViewData>()
                container.map.forEach { (key, value) ->
                    tempMap["${key.first}/${key.second}"] = HexagonViewData().apply {
                        id = value.id
                        posX = 0.0
                        posY = 0.0
                        visual = VisualMapper.map(value.visual)
                        size = value.size as Double
                    }
                }

                (HexagonGridData().fillData(container) as HexagonGridData).apply {
                    coordinateSystem = container.coordinateSystem.name.lowercase()
                    map = tempMap
                    spacing = 0.0
                    // components ?!
                }
            }

            is LinearLayout<*> -> (LinearLayoutData().fillData(container) as LinearLayoutData).apply {
                components = container.components.map { RecursiveMapper.map(it) } as List<GameComponentViewData>
                spacing = container.spacing
                // orientation
                // alignment
            }

            is Satchel -> (SatchelData().fillData(container) as SatchelData).apply {
                components = container.components.map { RecursiveMapper.map(it) } as List<GameComponentViewData>
            }
        }
    }
}

object VisualMapper {
    fun map(visual: Visual) : VisualData {
        return when (visual) {
            is ColorVisual -> ColorVisualData().apply {
                id = visual.id
                color = "rgba(${visual.color.red}, ${visual.color.green}, ${visual.color.blue}, ${visual.color.alpha})"
                transparency = visual.transparency
                // style
            }

            is ImageVisual -> ImageVisualData().apply {
                id = visual.id
                path = visual.path
                width = visual.width.toDouble()
                height = visual.height.toDouble()
                offsetX = visual.offsetX.toDouble()
                offsetY = visual.offsetY.toDouble()
                transparency = visual.transparency
                // style
            }

            is TextVisual -> TextVisualData().apply {
                id = visual.id
                text = visual.text
                font = FontMapper.map(visual.font)
                offsetX = visual.offsetX.toDouble()
                offsetY = visual.offsetY.toDouble()
                transparency = visual.transparency
                // alignment
                // style
            }

            // --- Compound Visuals ---

            is CompoundVisual -> {
                CompoundVisualData().apply {
                    id = visual.id
                    children = visual.children.map { map(it) } as List<SingleLayerVisualData>
                }
            }
        }
    }
}

object SceneMapper {
    fun map(scene: Scene<*>) : SceneData {
        // FIXME - DONE
        return SceneData().apply {
            components = scene.components.map { RecursiveMapper.map(it) }
            width = scene.width
            height = scene.height
            background = VisualMapper.map(scene.background)
        }
    }
}