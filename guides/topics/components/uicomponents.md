[LabelKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-label/index.html
[ButtonKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-button/index.html
[CheckBoxKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-check-box/index.html
[ColorPickerKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-color-picker/index.html
[PasswordFieldKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-password-field/index.html
[ComboBoxKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-combo-box/index.html
[ProgressBarKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-progress-bar/index.html
[ToggleButtonKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-toggle-button/index.html
[RadioButtonKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-radio-button/index.html
[ToggleGroupKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-toggle-group/index.html
[TextAreaKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-text-area/index.html
[TextFieldKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-text-field/index.html
[ListViewKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-list-view/index.html
[TableViewKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-table-view/index.html
[TableColumnKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-table-column/index.html
[UIComponentKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-u-i-component/index.html
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html
[ComponentViewDoc]: /guides/components/componentview
[UserInputDoc]: /guides/concepts/user-input
[GameComponentsDoc]: /guides/components/gamecomponents
[LayoutViewDoc]: /guides/components/layout
[UIComponentsDoc]: /guides/components/uicomponents
[ContainerDoc]: /guides/components/container

# UIComponents

<tldr>Components for displaying information and gathering input</tldr>

## Introduction

[UIComponents][UIComponentKDoc] serve as a medium to display information to the user or to gather user input. They can be used in any scene and are **not limited** to [BoardGameScenes][BoardGameSceneKDoc].

All [UIComponents][UIComponentKDoc] inherit from the [ComponentView][ComponentViewDoc] superclass. It is therefore helpful to read those documentations first as the features from those superclasses don't get repeated here. This also means that they can utilize all methods for handling user input, as detailed in the [User Input Guide][UserInputDoc].

Please also take a look at corresponding [Containers][ContainerDoc], [GameComponents][GameComponentsDoc] and [Layouts][LayoutViewDoc].

---

> The following examples are visually accurate representations of BGW components based on the provided code snippets.
> All example listeners however are purely illustrative and do not execute any code when interacting with the components in this guide.
> {style="warning"}

<br>

## Label

<tldr>Component for displaying a simple text label</tldr>

A [Label][LabelKDoc] is a basic text element. The text wrapping feature allows the text to flow onto the next line if the label's width is insufficient to accommodate the entire text. If disabled, the text will be truncated with ellipsis.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.labelExample">
val labelNoWrap = Label(
    posX = 0,
    posY = 0,
    width = 200,
    height = 75,
    text = "I am a Label with no wrapping.",
    alignment = Alignment.CENTER,
    font = Font(20.0, Color.WHITE)
)
&#13;
val labelWrap = Label(
    posX = 0,
    posY = 100,
    width = 200,
    height = 100,
    text = "This label has a long text that will wrap around.",
    alignment = Alignment.CENTER_LEFT,
    font = Font(20.0, Color(0x6dbeff)),
    isWrapText = true
)
</preview>

---

## Button

<tldr>Component for displaying an interactive button</tldr>

A [Button][ButtonKDoc] is a component that has a slight visual change on hover and a different cursor. It can also be labeled with descriptive text.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.buttonExample">
val button = Button(
    posX = 0,
    posY = 0,
    width = 250,
    height = 75,
    text = "I am a Button.",
    font = Font(20.0, Color(0x0f141f)),
    visual = ColorVisual(Color(0xc6ff6e))
)
&nbsp;
/* Add a listener to fire when the Button gets clicked */
button.onMouseClicked = {
    println("Someone pressed the Button!")
}
</preview>

##### Listeners

Because a button is designed to be interactive, it may be clicked by the user. In this example, we create a new button and apply an `onMouseClicked` event. When the button is clicked, the text `"Someone pressed the Button!"` is printed to the console.

<signature>
<code-block lang="kotlin" copy="false">
onMouseClicked: ((MouseEvent) -> Unit)?
</code-block>

Reminder that this listener gets invoked when the mouse is clicked, i.e. pressed and released, inside
this component.

> The `onMouseClicked` event is not limited to buttons but can be applied to any [ComponentView][ComponentViewDoc].
> {style="note"}

</signature>

---

## CheckBox

<tldr>Component for displaying a checkbox</tldr>

A [CheckBox][CheckBoxKDoc] is a versatile component that can either be checked or unchecked. It also supports an indeterminate state, which can be utilized to represent ambiguity.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.checkBoxExample">
val checkBox = CheckBox(
    posX = 0,
    posY = 0,
    width = 250,
    height = 50,
    text = "I am a CheckBox.",
    alignment = Alignment.CENTER,
    font = Font(20.0, Color.WHITE),
    allowIndeterminate = true
)
&#13;
checkBox.isChecked = true
&#13;
/* Add a listener to fire when the checked state changes */
checkBox.onCheckedChanged = { newValue ->
    if (newValue) {
        println("The check box is checked!")
    } else {
        println("The check box is unchecked!")
    }
}
&#13;
/* Add a listener to fire when the CheckBox gets ambiguous */
checkBox.onIndeterminateChanged = { newValue ->
    if (newValue) {
        println("The check box is indeterminate!")
    }
}
</preview>

##### States

By clicking on a CheckBox, the state changes. If `allowIndeterminate` is set to `false`, the CheckBox can only be checked or unchecked. If `allowIndeterminate` is set to `true`, the CheckBox will have an intermediate state where it is not checked but indeterminate.

For a CheckBox with `allowIndeterminate = false`, the state for each click transitions as follows:

| State         | `isChecked`                                                  | `isIndeterminate`                                            |
| ------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `Default`     | <i color="#ef4444">fiber_manual_record</i><code>false</code> | <i color="#ef4444">fiber_manual_record</i><code>false</code> |
| `First click` | <i color="#c6ff6e">adjust</i><code>true</code>               | <i color="#ef4444">fiber_manual_record</i><code>false</code> |

For a CheckBox with `allowIndeterminate = true`, for every click the state transitions like this:

| State          | `isChecked`                                                  | `isIndeterminate`                                            |
| -------------- | ------------------------------------------------------------ | ------------------------------------------------------------ |
| `Default`      | <i color="#ef4444">fiber_manual_record</i><code>false</code> | <i color="#ef4444">fiber_manual_record</i><code>false</code> |
| `First click`  | <i color="#ef4444">fiber_manual_record</i><code>false</code> | <i color="#c6ff6e">adjust</i><code>true</code>               |
| `Second click` | <i color="#c6ff6e">adjust</i><code>true</code>               | <i color="#ef4444">fiber_manual_record</i><code>false</code> |

> It is always possible to programmatically set `isIndeterminate` to `true`, however this state cannot be achieved through clicking, iff `allowIndeterminate = false`.
> {style="note"}

##### Listeners

CheckBoxes feature the two unique listeners `onCheckedChanged` and `onIndeterminateChanged`, which are used in the given example to print the current state of the CheckBox to the console.

<signature>
<code-block lang="kotlin" copy="false">
onCheckedChanged: ((Boolean) -> Unit)? = null
</code-block>

This listener gets invoked when the CheckBox is checked or unchecked. The listener receives a boolean value that indicates the new state of the CheckBox.

<br>

<code-block lang="kotlin" copy="false">
onIndeterminateChanged: ((Boolean) -> Unit)? = null
</code-block>

This listener gets invoked when the CheckBox is set to an indeterminate state or the indeterminate state is removed.The listener receives a boolean value that indicates the new indeterminate state of the CheckBox.

> Both values can also be obtained by accessing the `isChecked` and `isIndeterminate` properties.
> {style="note"}

</signature>

---

## ColorPicker

<tldr>Component for selecting a color</tldr>

A [ColorPicker][ColorPickerKDoc] allows users to easily select a color. The currently selected color can be accessed and modified through the `selectedColor` property.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.colorPickerExample">
val colorPicker = ColorPicker(
    posX = 0,
    posY = 0,
    width = 100,
    height = 100,
    initialColor = Color(0xfa6c56)
)
&#13;
/* Add a listener to fire when the selected color changes */
colorPicker.onColorSelected = { newValue ->
    println("The color is now $newValue!")
}
</preview>

##### Listeners

In this example, the ColorPicker is initialized with an initial color of black. To execute specific actions when the selected color changes, you can add the `onColorSelected` listener.
Here the listener prints the new color value to the console.

<signature>
<code-block lang="kotlin" copy="false">
onColorSelected: ((Color) -> Unit)? = null
</code-block>

This listener gets invoked when the selected color changes. The listener receives the new color value.

> The listener invokes after a brief delay when the user stops changing colors, avoiding rapid intermediate updates.
> {style="warning"}

</signature>

---

## ComboBox

<tldr>Component for selecting an option from a list</tldr>

The [ComboBox][ComboBoxKDoc] is a drop-down menu, where a user may choose an option. A `prompt` can be specified to
inform the user, what is expected of him. Every ComboBox has a generic type parameter `T` to define a type for the objects that
correspond to the options.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.comboBoxExample">
val comboBox = ComboBox&lt;Double&gt;(
    posX = 0,
    posY = 0,
    width = 300,
    height = 50,
    visual = ColorVisual(Color(0xffc656)),
    prompt = "Select an option...",
    font = Font(20.0, Color(0x0f141f))
)
&#13;
comboBox.items = mutableListOf(0.0, 1.0, 2.0)
&#13;
comboBox.formatFunction = {
    "Option ${it.toInt()}"
}
&#13;
comboBox.onItemSelected = { newValue ->
    println("Combo box selection is: $newValue")
}
</preview>

##### Format Function

Every object in the ComboBox will be projected to a string representation. By default, the `toString()` function is used, but a custom `formatFunction` can be set to define how objects should be displayed.

<signature>
<code-block lang="kotlin" copy="false">
formatFunction: ((T) -> String)? = null
</code-block>

The `formatFunction` is a lambda expression that takes an object of type `T` and returns a string representation of it.
You can use this to define a custom representation for objects in the ComboBox.

</signature>

In this example, a ComboBox with item type `Double` is instantiated with the prompt "Select an option..." The `formatFunction` is set to display doubles as their Int value prefixed with "Option". The available options are defined by setting the `items` list with three doubles.

##### Listeners

The `selectedItem` can be used to retrieve or set the current selected item. To react to a change of the selected
item, the `onItemSelected` listener can be added. In the example the listener prints the new selected item to the console.

<signature>
<code-block lang="kotlin" copy="false">
onItemSelected: ((T?) -> Unit)? = null
</code-block>

This listener gets invoked when the selected item changes. The listener receives the new selected item. If no item is selected, `null` is passed.

> The return type of the listener is always nullable, as the ComboBox selection can be empty.
> {style="note"}

</signature>

---

## ProgressBar

<tldr>Component for indicating progress</tldr>

A [ProgressBar][ProgressBarKDoc] is a coloured bar that can indicate progress to the user. The current progress and
colour can be retrieved and set via `progress` and `barColor` properties respectively.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.progressBarExample">
val progressBar = ProgressBar(
    posX = 0,
    posY = 0,
    width = 300,
    height = 50,
    progress = 0.85,
    visual = ColorVisual(Color(0x0f141f)),
    barColor = Color(0xef4444)
)
&#13;
progressBar.onMouseClicked = {
    progressBar.progress = if (progressBar.progress > 1.0)
        0.0 
    else 
        progressBar.progress + 0.05
}
&#13;
progressBar.onProgressed = { done ->
    when {
        done > 0.8 -> progressBar.barColor = Color(0xef4444)
        done > 0.5 -> progressBar.barColor = Color(0xffc656)
        else -> progressBar.barColor = Color(0xc6ff6e)
    }
}
</preview>

In this example a new ProgressBar is instantiated and the `onMouseClicked` is set, such that the progress of the bar is advanced by 5% on a click or reset if the progress is greater than 100%.

> The progress is modelled as a `Double`, where any value equal or less than 0 means 0% progress and any value equal or greater than 1 means 100% progress.
> {style="note"}

##### Listeners

To react to progression in this example, the `onProgressed` listener is added, in this case updating
the `barColor` based on the current progress level.

<signature>
<code-block lang="kotlin" copy="false">
onProgressed: ((Double) -> Unit)? = null
</code-block>

This listener gets invoked when the progress changes. It receives the new progress value as a `Double`.

</signature>

---

## ToggleButton and RadioButton

<tldr>Components for toggling between two states</tldr>

Both [ToggleButton][ToggleButtonKDoc] and [RadioButton][RadioButtonKDoc] are components that can be either selected or unselected, controlled through their `isSelected` property. While they share the same functionality, they differ in their visual representation.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.binaryButtonExample">
val toggleGroup = ToggleGroup()
&#13;
val toggleButton = ToggleButton(
    posX = 0,
    posY = 0,
    width = 300,
    height = 50,
    text = "I am a ToggleButton.",
    alignment = Alignment.CENTER,
    font = Font(20.0, Color.WHITE),
    toggleGroup = toggleGroup
)
&#13;
val radioButton = RadioButton(
    posX = 0,
    posY = 100,
    width = 300,
    height = 50,
    text = "I am a RadioButton.",
    alignment = Alignment.CENTER,
    font = Font(20.0, Color.WHITE),
    toggleGroup = toggleGroup
)
&#13;
radioButton.onSelectionChanged = { newValue ->
    if (newValue) {
        println("This radio button is now selected!")
    } else {
        println("This radio button is now deselected!")
    }
}
</preview>

##### Grouping

[ToggleGroups][ToggleGroupKDoc] allow you to group multiple ToggleButtons and RadioButtons together. Only one button in a ToggleGroup can be selected at a time - when you select one button, all other buttons in the group are automatically deselected. This enforces mutual exclusivity between options in the group.

In this example a new ToggleGroup, ToggleButton and RadioButton are instantiated. The group is set as the `toggleGroup` for the both buttons.

##### Listeners

To react to change of the selected state, multiple different listeners can be added. This example uses the `onSelectionChanged` listener to print the current state of the RadioButton to the console.

<signature>
<code-block lang="kotlin" copy="false">
onSelected: (() -> Unit)? = null
</code-block>

This listener gets invoked when the component gets selected.

<br>

<code-block lang="kotlin" copy="false">
onDeselected: (() -> Unit)? = null
</code-block>

This listener gets invoked when the component gets deselected.

<br>

<code-block lang="kotlin" copy="false">
onSelectionChanged: ((Boolean) -> Unit)? = null
</code-block>

This listener gets invoked when the selection state changes. It receives a boolean value that indicates the new selection state.

> The `onSelectionChanged` listener combines `onSelected` and `onDeselected` but can still be used in combination with the other two listeners.
> {style="note"}

</signature>

---

## TextArea, TextField and PasswordField

<tldr>Components for textual input</tldr>

For text input, the three components [TextArea][TextAreaKDoc], [TextField][TextFieldKDoc] and [PasswordField][PasswordFieldKDoc] are available.
A TextArea allows for multi-line text input, while TextFields and PasswordFields are limited to a single line. PasswordFields function similarly to TextFields but masks the entered text with dots for security.

All components support setting and retrieving text via the `text` property and can display a `prompt` to guide users on the expected input.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.textExample">
val textArea = TextArea(
    posX = 0,
    posY = 0,
    width = 300,
    height = 75,
    font = Font(20.0, Color(0x0f141f)),
    prompt = "I am a\nTextArea."
)
&#13;
textArea.onTextChanged = { newValue ->
    println("The text in the TextArea is now: $newValue")
}
&#13;
val textField = TextField(
    posX = 0,
    posY = 100,
    width = 300,
    height = 50,
    visual = ColorVisual(Color(0x0f141f)),
    font = Font(20.0, Color.WHITE),
    prompt = "I am a TextField.",
    text = "Initial text"
)
&#13;
val passwordField = PasswordField(
    posX = 0,
    posY = 175,
    width = 300,
    height = 50,
    visual = ColorVisual(Color(0x6dbeff)),
    font = Font(20.0, Color(0x0f141f)),
    prompt = "I am a PasswordField.",
    text = "Hidden"
)
</preview>

##### Listeners

In this example a TextArea, a TextField and a PasswordField with prompts are instantiated, while the TextField is also initialized with the initial text "Initial text".
Furthermore, the `onTextChanged` listener is added to the TextArea to print the current text to the console.

<signature>
<code-block lang="kotlin" copy="false">
onTextChanged: ((String) -> Unit)? = null
</code-block>

This listener gets invoked when the text changes. It receives the new text value as a `String`.

</signature>

> The `onKeyPressed` or `onKeyReleased` listeners of [ComponentViews][ComponentViewDoc] are a great way of executing code when textual input occurs.
> {style="note"}

---

## ListView

<tldr>Component for displaying a list of items</tldr>

A [ListView][ListViewKDoc] can be used to display a list of strings. Every ListView has a generic type parameter `T` to define a type for the objects that
correspond to the options and will be inferred from the initial `items`.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.listViewExample">
val listView = ListView(
    posX = 0,
    posY = 0,
    width = 150,
    height = 200,
    items = mutableListOf(42, 1337, 1, 2, 3),
    visual = ColorVisual(Color(0x0f141f)),
    font = Font(20.0, Color.WHITE),
    selectionMode = SelectionMode.SINGLE,
    selectionBackground = ColorVisual(Color(0xbb6dff)),
)
&#13;
listView.onSelectionChanged = { selectedItems ->
    println("Selected items: $selectedItems")
}
</preview>

##### Selection

Every ListView has a `selectedItem` property that can be used to retrieve the currently selected item. To specify if one or multiple items can be selected, the `selectionMode` property can be set to either `SelectionMode.SINGLE` or `SelectionMode.MULTIPLE`. The use of `SelectionMode.NONE` disables selection entirely.

##### Format Function

Every object in the ListView will be projected to a string representation. By default, the `toString()` function is used, but a custom `formatFunction` can be set to define how objects should be displayed.

<signature>
<code-block lang="kotlin" copy="false">
formatFunction: ((T) -> String)? = null
</code-block>

The `formatFunction` is a lambda expression that takes an object of type `T` and returns a string representation of it.
You can use this to define a custom representation for objects in the ListView.

</signature>

In this example, a ListView with initial `Int` items is created and no custom `formatFunction` is set. The ListView will display the `toString()` value of the contained object.

##### Listeners

To react to a change of the selected item, the `onSelectionChanged` listener can be added. In the example the listener prints the currently selected items to the console.

<signature>
<code-block lang="kotlin" copy="false">
onSelectionChanged: ((List&lt;T&gt;) -> Unit)? = null
</code-block>

This listener gets invoked when the selection changes. The listener receives a list of the selected items.

</signature>

---

## TableView

<tldr>Component for displaying a table of items with multiple columns</tldr>

A [TableView][TableViewKDoc] displays data in a tabular format, where each row represents an item, and columns show different properties of those items.
Similar to ListViews, TableViews have a generic type parameter `T` to define the type of objects they contain.

<preview key="tools.aqua.bgw.main.examples.ExampleDocsScene.tableViewExample">
val tableView = TableView<Int>(
    posX = 0,
    posY = 0,
    width = 300,
    height = 200,
    visual = ColorVisual(Color(0x0f141f)),
    selectionMode = SelectionMode.MULTIPLE,
    selectionBackground = ColorVisual(Color(0xef4444)),
)
&#13;
tableView.columns.add(
    TableColumn(
        title = "Value",
        width = 100,
        font = Font(20.0, Color.WHITE),
        formatFunction = { "$it" }
    )
)
&#13;
tableView.columns.add(
    TableColumn(
        title = "Squared",
        width = 100,
        font = Font(20.0, Color.WHITE),
        formatFunction = { "${it * it}" }
    )
)
&#13;
tableView.columns.add(
    TableColumn(
        title = "Even?",
        width = 100,
        font = Font(20.0, Color.WHITE),
        formatFunction = { "${it % 2 == 0}" }
    )
)
&#13;
tableView.items.addAll(listOf(42, 1337, 1, 2, 3))
&#13;
tableView.onSelectionChanged = { selectedItems ->
    println("Selected items: $selectedItems")
}
</preview>

##### Columns

[TableColumns][TableColumnKDoc] define the structure and data presentation in the table.
Each column needs a `title` that appears in the header, a `width` and its own `formatFunction` that determines how the items in that column should be displayed as text.

In the example above, three columns are created: one showing the raw value, another showing the squared value and a third indicating whether the number is even.

##### Selection

Like ListViews, every TableView has a `selectedItem` property that can be used to retrieve the currently selected item. To specify if one or multiple items can be selected, the `selectionMode` property can be set to either `SelectionMode.SINGLE` or `SelectionMode.MULTIPLE`. The use of `SelectionMode.NONE` disables selection entirely.

##### Listeners

The TableView provides the same selection listener as [ListView][ListViewKDoc] to react to selection changes.

<signature>
<code-block lang="kotlin" copy="false">
onSelectionChanged: ((List&lt;T&gt;) -> Unit)? = null
</code-block>

This listener gets invoked when the row selection changes. It receives a list of the selected items.

</signature>

This example adds a `onSelectionChanged` listener to print the selected items to the console on selection change.
