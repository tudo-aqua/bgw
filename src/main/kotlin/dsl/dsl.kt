package dsl

import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.elements.StaticView
import tools.aqua.bgw.elements.layoutviews.GridLayoutView
import tools.aqua.bgw.elements.uielements.*
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.Visual
import java.awt.Color

//region Util
/**.*/
fun menuScene(height: Number, width: Number, func: MenuScene.() -> Unit): MenuScene =
    MenuScene(height = height, width = width).apply(func)

/**.*/
fun UIElementView.componentStyle(func: () -> String) {
    componentStyle = func.invoke()
}

/**.*/
fun UIElementView.backgroundStyle(func: () -> String) {
    backgroundStyle = func.invoke()
}

/**.*/
fun UIElementView.visual(func: () -> Visual) {
    setVisuals(func())
}

/**.*/
fun UIElementView.colorVisual(func: () -> Color) {
    setVisuals(ColorVisual(func()))
}

/**.*/
fun UIElementView.dimensions(height: Number, width: Number) {
    this.height = height.toDouble()
    this.width = width.toDouble()
}

/**.*/
fun UIElementView.position(posX: Number, posY: Number) {
    this.posX = posX.toDouble()
    this.posY = posY.toDouble()
}
//endregion


//region grid
/**.*/
fun <T> MenuScene.grid(rows: Int, cols: Int, func: (GridLayoutView<T>.() -> Unit)) where T : StaticView<T> {
    addElements(GridLayoutView<T>(rows, cols).apply(func))
}
//endregion

//region ToggleGroup
/**.*/
fun MenuScene.toggleGroup(func: ToggleGroupBuilder.() -> Unit): ToggleGroup {
    val tgb = ToggleGroupBuilder().apply(func)
    val data = tgb.build()
    data.second.forEach { addElements(it) }
    return data.first
}

/**.*/
class ToggleGroupBuilder {
    internal val buttons = mutableListOf<ToggleButton>()
    internal fun build(): Pair<ToggleGroup, List<ToggleButton>> {
        val toggleGroup = ToggleGroup()
        buttons.forEach { it.toggleGroup = toggleGroup }
        return Pair(toggleGroup, buttons)
    }
}

/**.*/
fun ToggleGroupBuilder.toggleButton(func: ToggleButton.() -> Unit): ToggleButton {
    val toggleButton = ToggleButton().apply(func)
    buttons.add(toggleButton)
    return toggleButton
}

/**.*/
fun ToggleGroupBuilder.radioButton(func: RadioButton.() -> Unit): RadioButton {
    val radioButton = RadioButton().apply(func)
    buttons.add(radioButton)
    return radioButton
}
//endregion

//region Button
/**.*/
fun MenuScene.button(func: Button.() -> Unit): Button {
    val btn = Button().apply(func)
    addElements(btn)
    return btn
}
//endregion

//region CheckBox
/**.*/
fun MenuScene.checkBox(func: CheckBox.() -> Unit): CheckBox {
    val checkBox = CheckBox().apply(func)
    addElements(checkBox)
    return checkBox
}
//endregion

//region Label
/**.*/
fun MenuScene.label(func: Label.() -> Unit): Label {
    val label = Label().apply(func)
    addElements(label)
    return label
}
//endregion

//region ListView
/**.*/
fun <T> MenuScene.listView(func: ListView<T>.() -> Unit): ListView<T> {
    val listView = ListView<T>()
    addElements(listView.apply(func))
    return listView
}
//endregion

//region TableView
/**.*/
fun <T> MenuScene.tableView(func: TableView<T>.() -> Unit): TableView<T> {
    val tableView = TableView<T>()
    addElements(tableView.apply(func))
    return tableView
}

/**.*/
fun <T> TableView<T>.column(title: String, width: Number, func: T.() -> String) {
    columns.add(TableColumn(title = title, width = width, formatFunction = func))
}

/**.*/
fun <T> TableView<T>.data(data: List<T>) {
    items.clear()
    items.addAll(data)
}

/**.*/
fun <T> TableView<T>.appendData(data: List<T>) {
    items.addAll(data)
}
//endregion

//region TextArea
/**.*/
fun MenuScene.textArea(prompt: String, func: TextArea.() -> Unit): TextArea {
    val textArea = TextArea(prompt = prompt).apply(func)
    addElements(textArea)
    return textArea
}
//endregion
