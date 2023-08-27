package tools.aqua.bgw.builder

import ButtonData
import CameraPaneData
import CardViewData
import ComponentViewData
import DiceViewData
import GameComponentContainerData
import GameComponentViewData
import HexagonGridData
import HexagonViewData
import LabelData
import LayoutViewData
import TextFieldData
import TokenViewData
import react.ReactElement
import react.create
import kotlinx.js.jso
import react.*
import tools.aqua.bgw.elements.uicomponents.Button as ReactButton
import tools.aqua.bgw.elements.uicomponents.Label as ReactLabel
import tools.aqua.bgw.elements.uicomponents.TextField as ReactTextField
import tools.aqua.bgw.elements.gamecomponentviews.HexagonView as ReactHexagonView
import tools.aqua.bgw.elements.container.HexagonGrid as ReactHexagonGrid
import tools.aqua.bgw.elements.layoutviews.CameraPane as ReactCameraPane
import tools.aqua.bgw.elements.gamecomponentviews.TokenView as ReactTokenView

object NodeBuilder {
    fun build(componentViewData: ComponentViewData): ReactElement<*> {
        return when (componentViewData) {
            is LayoutViewData -> LayoutNodeBuilder.build(componentViewData)
            is LabelData -> ReactLabel.create { data = componentViewData }
            is ButtonData -> ReactButton.create { data = componentViewData }
            is TextFieldData -> ReactTextField.create { data = componentViewData }
            is HexagonGridData -> ReactHexagonGrid.create { data = componentViewData }
            is CameraPaneData -> ReactCameraPane.create { data = componentViewData }

            is GameComponentContainerData -> ContainerBuilder.build(componentViewData)
            //is CardViewData -> ReactCardView.create { data = componentViewData }
            //is DiceViewData -> ReactDiceView.create { data = componentViewData }
            is HexagonViewData -> ReactHexagonView.create { data = componentViewData }
            is TokenViewData -> ReactTokenView.create { data = componentViewData }

            else -> throw IllegalArgumentException("Unknown component type: ${componentViewData::class.simpleName}")
        }
    }
}