import ComponentMapper.fillData
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.container.*
import tools.aqua.bgw.components.gamecomponentviews.*
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.GridPane
import tools.aqua.bgw.components.layoutviews.LayoutView
import tools.aqua.bgw.components.layoutviews.Pane
import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.*
import tools.aqua.bgw.style.Filter
import tools.aqua.bgw.style.Style
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.*
import kotlin.math.max

object ComponentMapper {
    fun ComponentViewData.fillData(componentView: ComponentView) : ComponentViewData {
        return this.apply {
            id = componentView.id
            posX = componentView.posX.toInt()
            posY = componentView.posY.toInt()
            width = componentView.width.toInt()
            height = componentView.height.toInt()
            visual = VisualMapper.map(componentView.visual)
            // zIndex
            opacity = componentView.opacity
            isVisible = componentView.isVisible
            isDisabled = componentView.isDisabled
            // isFocusable
            scaleX = componentView.scaleX
            scaleY = componentView.scaleY
            rotation = componentView.rotation
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
                    alignment = Pair(componentView.alignment.horizontalAlignment.name.lowercase(), componentView.alignment.verticalAlignment.name.lowercase())
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
                scroll = CoordinateData(componentView.scroll.xCoord, componentView.scroll.yCoord)
                internalData = componentView.internalData
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
                }.apply {
                    isDraggable = componentView.isDraggable
                }
            }

            else -> throw IllegalArgumentException("Unknown component type: ${componentView::class.simpleName}")
        }
    }

    fun map(componentView: ComponentView) : ComponentViewData {
        //println("Mapping ComponentView: $componentView")
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
            is ComboBox<*> -> mapComboBox(componentView)
            is ColorPicker -> (mapSpecific(componentView) as ColorPickerData).apply {
                selectedColor = "rgba(${componentView.selectedColor.red}, ${componentView.selectedColor.green}, ${componentView.selectedColor.blue}, ${componentView.selectedColor.alpha})"
            }
            is ProgressBar -> (mapSpecific(componentView) as ProgressBarData).apply {
                progress = componentView.progress
                barColor = "rgba(${componentView.barColor.red}, ${componentView.barColor.green}, ${componentView.barColor.blue}, ${componentView.barColor.alpha})"
            }

            // TODO - StructuredDataView
            is ListView<*> -> (mapSpecific(componentView) as ListViewData).apply {
                orientation = componentView.orientation.name.lowercase()
            }
            is TableView<*> -> (mapSpecific(componentView) as TableViewData).apply {
                // columns (as TableColumnData)
            }

            // TODO - ComponentView
            is CameraPane<*> -> (mapSpecific(componentView) as CameraPaneData)

            // TODO - GameComponentView
            is CardView -> (mapSpecific(componentView) as CardViewData).apply {
                front = VisualMapper.map(componentView.frontVisual)
                back = VisualMapper.map(componentView.backVisual)
                currentVisual = if(componentView.currentSide == CardView.CardSide.BACK)
                    VisualMapper.map(componentView.backVisual) else VisualMapper.map(componentView.frontVisual)
            }
            is DiceView -> (mapSpecific(componentView) as DiceViewData).apply {
                currentSide = componentView.currentSide
                visuals = componentView.visuals.map { VisualMapper.map(it) }
            }
            is HexagonView -> (mapSpecific(componentView) as HexagonViewData).apply {
                size = componentView.size.toInt()
            }
            is TokenView -> (mapSpecific(componentView) as TokenViewData)

            else -> TODO("Not implemented")
        }
    }

    fun <T> mapComboBox(comboBox: ComboBox<T>) : ComboBoxData {
        return (mapSpecific(comboBox) as ComboBoxData).apply {
            val selItem = comboBox.selectedItem
            prompt = comboBox.prompt
            items = comboBox.items.mapIndexed { index, it ->
                Pair(index, comboBox.formatFunction?.invoke(it) ?: it.toString())
            }
            selectedItem =
                if(selItem == null) null
                else Pair(comboBox.getSelectedIndex(), comboBox.formatFunction?.invoke(selItem) ?: comboBox.selectedItem.toString())
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
            is GridPane<*> -> (GridPaneData().fillData(layout) as GridPaneData).apply {
                val grid = layout.grid.clone().apply {
                    removeEmptyColumns()
                    removeEmptyRows()
                }
                columns = grid.columns
                rows = grid.rows
                this.grid = grid.map {
                    val alignment = layout.getCellCenterMode(it.columnIndex, it.rowIndex)
                    GridElementData(
                        it.columnIndex,
                        it.rowIndex,
                        if(it.component != null) RecursiveMapper.map(it.component) else null,
                        alignment = alignment.horizontalAlignment.name.lowercase() to alignment.verticalAlignment.name.lowercase()
                    )
                }
                spacing = layout.spacing.toInt()
                layoutFromCenter = layout.isLayoutFromCenter
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
                        posX = value.posX.toInt()
                        posY = value.posY.toInt()
                        visual = VisualMapper.map(value.visual)
                        size = value.size.toInt()
                    }
                }

                (HexagonGridData().fillData(container) as HexagonGridData).apply {
                    coordinateSystem = container.coordinateSystem.name.lowercase()
                    map = tempMap
                    spacing = 0
                    // components ?!
                }
            }

            is LinearLayout<*> -> (LinearLayoutData().fillData(container) as LinearLayoutData).apply {
                components = container.components.map { RecursiveMapper.map(it) } as List<GameComponentViewData>
                spacing = container.spacing.toInt()
                orientation = container.orientation.name.lowercase()
                alignment = Pair(container.alignment.horizontalAlignment.name.lowercase(), container.alignment.verticalAlignment.name.lowercase())
            }

            is Satchel -> (SatchelData().fillData(container) as SatchelData).apply {
                components = container.components.map { RecursiveMapper.map(it) } as List<GameComponentViewData>
            }
        }
    }
}

object StyleMapper {
    fun map(style: Style) : Map<String, String> {
        return style.getDeclarations()
    }
}

object FilterMapper {
    fun map(filters: Filter) : Map<String, String?> {
        return filters.getDeclarations()
    }
}

object VisualMapper {
    fun map(visual: Visual) : VisualData {
        val visualData = when (visual) {
            is ColorVisual -> ColorVisualData().apply {
                id = visual.id
                color = "rgba(${visual.color.red}, ${visual.color.green}, ${visual.color.blue}, ${visual.color.alpha})"
                transparency = visual.transparency
                style = StyleMapper.map(visual.style)
                filters = FilterMapper.map(visual.filters)
                flipped = visual.flipped.name.lowercase()
            }

            is ImageVisual -> ImageVisualData().apply {
                id = visual.id
                if(isRelativeFilePath(visual.path)) path = "http://localhost:8080/static/${visual.path}"
                else path = visual.path
                width = visual.width
                height = visual.height
                offsetX = visual.offsetX
                offsetY = visual.offsetY
                transparency = visual.transparency
                style = StyleMapper.map(visual.style)
                filters = FilterMapper.map(visual.filters)
                flipped = visual.flipped.name.lowercase()
            }

            is TextVisual -> TextVisualData().apply {
                id = visual.id
                text = visual.text
                font = FontMapper.map(visual.font)
                offsetX = visual.offsetX.toInt()
                offsetY = visual.offsetY.toInt()
                transparency = visual.transparency
                style = StyleMapper.map(visual.style)
                filters = FilterMapper.map(visual.filters)
                flipped = visual.flipped.name.lowercase()
                alignment = Pair(visual.alignment.horizontalAlignment.name.lowercase(), visual.alignment.verticalAlignment.name.lowercase())
            }

            // --- Compound Visuals ---

            is CompoundVisual -> {
                CompoundVisualData().apply {
                    id = visual.id
                    children = visual.children.map { mapChildren(it) }
                }
            }
        }
        return if(visualData is SingleLayerVisualData) {
            CompoundVisualData().apply {
                id = visualData.id
                children = listOf(visualData)
            }
        } else {
            visualData
        }
    }

    private fun mapChildren(visual: SingleLayerVisual) : SingleLayerVisualData {
        return when (visual) {
            is ColorVisual -> ColorVisualData().apply {
                id = visual.id
                color = "rgba(${visual.color.red}, ${visual.color.green}, ${visual.color.blue}, ${visual.color.alpha})"
                transparency = visual.transparency
                style = StyleMapper.map(visual.style)
                filters = FilterMapper.map(visual.filters)
                flipped = visual.flipped.name.lowercase()
            }

            is ImageVisual -> ImageVisualData().apply {
                id = visual.id
                if (isRelativeFilePath(visual.path)) path = "http://localhost:8080/static/${visual.path}"
                else path = visual.path
                width = visual.width
                height = visual.height
                offsetX = visual.offsetX
                offsetY = visual.offsetY
                transparency = visual.transparency
                style = StyleMapper.map(visual.style)
                filters = FilterMapper.map(visual.filters)
                flipped = visual.flipped.name.lowercase()
            }

            is TextVisual -> TextVisualData().apply {
                id = visual.id
                text = visual.text
                font = FontMapper.map(visual.font)
                offsetX = visual.offsetX.toInt()
                offsetY = visual.offsetY.toInt()
                transparency = visual.transparency
                style = StyleMapper.map(visual.style)
                filters = FilterMapper.map(visual.filters)
                flipped = visual.flipped.name.lowercase()
                alignment = Pair(
                    visual.alignment.horizontalAlignment.name.lowercase(),
                    visual.alignment.verticalAlignment.name.lowercase()
                )
            }
        }
    }

    //TODO: Check properly if path is URL or local path
    private fun isRelativeFilePath(path: String): Boolean {
        return !path.startsWith("http://") && !path.startsWith("https://")
    }
}

object FontFaceMapper {
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

    fun map(font : Triple<String, String, Font.FontWeight>) : Triple<String, String, Int> {
        return Triple(font.first, font.second, fontWeightMap[font.third] ?: 400)
    }
}

object SceneMapper {
    private fun mapScene(scene: Scene<*>) : SceneData {
        // FIXME - DONE
        return SceneData().apply {
            components = scene.components.map { RecursiveMapper.map(it) }
            width = scene.width.toInt()
            height = scene.height.toInt()
            background = VisualMapper.map(scene.background)
        }
    }

    fun map(menuScene: MenuScene? = null, gameScene: BoardGameScene? = null) : AppData {
        return AppData().apply {
            this.width = max(menuScene?.width?.toInt() ?: DEFAULT_SCENE_WIDTH.toInt(), gameScene?.width?.toInt() ?: DEFAULT_SCENE_WIDTH.toInt())
            this.height = max(menuScene?.height?.toInt() ?: DEFAULT_SCENE_HEIGHT.toInt(), gameScene?.height?.toInt() ?: DEFAULT_SCENE_HEIGHT.toInt())
            this.menuScene = if(menuScene != null) mapScene(menuScene) else null
            this.gameScene = if(gameScene != null) mapScene(gameScene) else null
        }
    }
}