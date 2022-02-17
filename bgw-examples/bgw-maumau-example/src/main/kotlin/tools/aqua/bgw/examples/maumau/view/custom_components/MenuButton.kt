package tools.aqua.bgw.examples.maumau.view.custom_components

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.examples.maumau.main.BUTTON_BG_FILE
import tools.aqua.bgw.examples.maumau.main.MENU_BUTTON_FONT
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_HEIGHT
import tools.aqua.bgw.examples.maumau.main.MENU_ITEM_WIDTH
import tools.aqua.bgw.visual.ImageVisual

class MenuButton(text: String) : Button(
	height = MENU_ITEM_HEIGHT,
	width = MENU_ITEM_WIDTH,
	text = text,
	font = MENU_BUTTON_FONT,
	visual = ImageVisual(BUTTON_BG_FILE)
)