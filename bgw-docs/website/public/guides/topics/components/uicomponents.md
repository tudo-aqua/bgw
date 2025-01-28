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
[LabeledUIComponentKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-labeled-u-i-component/index.html
[TextInputUIComponentKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-text-input-u-i-component/index.html
[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html
[ComponentViewDoc]: /guides/components/componentview
[UserInputDoc]: /guides/concepts/user-input

# UIComponents

[UIComponents][UIComponentKDoc] serve as a medium to display information to the user or to gather user input. They can be used in any scene and are **not limited** to [BoardGameScenes][BoardGameSceneKDoc].

---

<!-- TODO: Add code/result snippets for each component -->

## Inheritance

All UI components inherit from the [ComponentView][ComponentViewDoc] superclass. This means that they can utilize all methods for handling user input, as detailed in the [User Input Guide][UserInputDoc]. There are three primary classes of UIComponentViews:

<deflist style="full" sorted="desc">
    <def title="UIComponent">
        This is the base class for all UIComponents.
    </def>
    <def title="LabeledUIComponent">
        This class extends UIComponent and offers additional fields to specify a text, for instance, a Button with a text.
    </def>
    <def title="TextInputUIComponent">
          This class extends UIComponent and provides a text input field for user-defined text.
    </def>
</deflist>

---

## Label

<tldr>Component for displaying a styled text label</tldr>

A [Label][LabelKDoc] is a basic text element. In this instance, a new label is created with the text "I am a Label", centered alignment, and enabled text wrapping. The text wrapping feature allows the text to flow onto the next line if the label's width is insufficient to accommodate the entire text.

<preview key="tools.aqua.bgw.main.examples.ExampleUIScene.button">
val outputLabel = Label(
    posX = 50,
    posY = 50,
    width = 300,
    text = "I am a Label.",
    alignment = Alignment.CENTER,
    isWrapText = true
)
</preview>

<tabs group="gradleMaven">
    <tab title="Gradle" group-key="gradle">
        <code-block lang="apache">
            compileKotlin {
    kotlinOptions.jvmTarget = "11"
}
        </code-block>
    </tab>
    <tab title="Maven" group-key="maven">
        <code-block lang="xml">
          private val outputLabel = Label(
    posX = 50,
    posY = 50,
    width = 300,
    text = "I am a Label.",
    alignment = Alignment.CENTER,
    isWrapText = true
)
        </code-block>
    </tab>
</tabs>

## Button

<tldr>Component for displaying a styled interactive button</tldr>

A [Button][ButtonKDoc] is a component that has an animation upon mouse click. It can also be labeled with descriptive text. In this example, we create a new button and apply an `onMouseClicked` event. When the button is clicked, the text `"Someone pressed the Button!"` is printed to the console.

<tabs>
    <tab title="Component">
        <img src="../images/components/uicomponents/button.svg" width="706" />
    </tab>
    <tab title="Code">
        <code-block lang="kotlin">
            val button = Button(
                posX = 450,
                posY = 50,
                text = "I am a Button.",
                visual = ColorVisual.LIGHT_GRAY
            )
            &#13;
            // Add a listener to fire when the Button gets clicked
            button.onMouseClicked = {
                println("Someone pressed the Button!")
            }
        </code-block>
    </tab>
</tabs>

## CheckBox

<tldr>
    <p><format style="bold">Component for displaying a styled checkbox</format></p>
    <p>→ &nbsp; <a href="http://">LabeledUIComponent</a> &nbsp; | &nbsp; <a href="http://">UIComponent</a></p>
</tldr>

A [CheckBox][CheckBoxKDoc] is a versatile component that can either be checked or unchecked. It also supports an indeterminate state, which can be utilized to represent ambiguity. The state of a CheckBox transitions as follows:

#### Indeterminate: Not Allowed

<table>
<tr><td width="50%">isChecked</td><td width="50%">isIndeterminate</td></tr>
<tr><td><code>false</code></td><td><code>false</code></td></tr>
<tr><td><code>true</code></td><td><code>false</code></td></tr>
</table>

> It is possible to manually set `isIndeterminate` to `true`, however this state cannot be achieved through clicking, iff `allowIndeterminate = false`.
> {style="note"}

#### Indeterminate: Allowed

<table>
<tr><td width="50%">isChecked</td><td width="50%">isIndeterminate</td></tr>
<tr><td><code>false</code></td><td><code>false</code></td></tr>
<tr><td><code>false</code></td><td><code>true</code></td></tr>
<tr><td><code>true</code></td><td><code>false</code></td></tr>
</table>

In the given example, listeners are attached to the `isCheckedProperty` and `isIndeterminateProperty`. These
listeners print the current state of the CheckBox to the console.

<tabs>
    <tab title="Component">
        <img src="../images/components/uicomponents/checkbox.svg" width="706" />
    </tab>
    <tab title="Code">
        <code-block lang="kotlin">
            val checkBox = CheckBox(
                posX = 50,
                posY = 150,
                width = 300,
                text = "I am a CheckBox.",
                alignment = Alignment.CENTER_LEFT,
                allowIndeterminate = true
            )
            &#13;
            // Add a listener to fire when the CheckBox gets checked or unchecked
            checkBox.isCheckedProperty.addListener { _, newValue ->
                if (newValue)
                    println("The check box is checked!")
                else
                    println("The check box is unchecked!")
            }
            &#13;
            // Add a listener to fire when the CheckBox gets ambiguous
            checkBox.isIndeterminateProperty.addListener { _, newValue ->
                if (newValue)
                    println("The check box is indeterminate!")
            }
        </code-block>
    </tab>
</tabs>

## ColorPicker

A [ColorPicker][ColorPickerKDoc] allows users to easily select a color. The currently selected color can be accessed and modified through the `selectedColor` property. To execute specific actions when the selected color changes, you can add a listener to the `selectedColorProperty`.

In this code, the ColorPicker is initialized with an initial color of black. The listener attached to the selectedColorProperty prints the new color value each time it changes.

<tabs>
    <tab title="Component">
        <img src="../images/components/uicomponents/colorpicker.svg" width="706" />
    </tab>
    <tab title="Code">
        <code-block lang="kotlin">
            val colorPicker = ColorPicker(
                posX = 450,
                posY = 200,
                width = 300,
                initialColor = Color.BLACK
            )
            &#13;
            // Add a listener to fire when the selected color changes
            colorPicker.selectedColorProperty.addListener { _, newValue ->
                println("The color is now $newValue!")
            }
        </code-block>
    </tab>
</tabs>

## ComboBox

A [ComboBox][ComboBoxKDoc] is a drop-down menu, where a user may choose an option. A `prompt` may be specified to
inform the user, what is expected of him. The ComboBox has a type parameter to define a type for the objects that
correspond to the options. One string per contained object is necessary to represent it as an option to the user.
There are two methods of obtaining said string.

A `formatFunction`, that projects the contained objects to a string representation may be set. If
no `formatFunction` is set, the `toString`
function of the contained object is used to obtain a string.

In this example a ComboBox typed to `Double` with the `prompt` "Select an option! This is the prompt." is
instantiated. Then the `formatFunction` is set, so the contained doubles get represented as their Int value with the
suffix "Option ".

<tabs>
    <tab title="Component">
        <img src="../images/components/uicomponents/combobox.svg" width="706" />
    </tab>
    <tab title="Code">
        <code-block lang="kotlin">
            val comboBox =
                ComboBox<Double>(posX = 50, posY = 350, width = 300, prompt = "Select an option! This is the prompt.")
            &#13;
            comboBox.formatFunction = {
                "Option ${it.toInt()}"
            }
        </code-block>
    </tab>
</tabs>

To define the options simply set the `items` list. In the example three doubles are set.

```kotlin
comboBox.items = mutableListOf(0.0, 1.0, 2.0)
```

The `selectedItem` can be used to retrieve or set the current selected item. To react to a change of the selected
item, a listener can be added to the `selectedItemProperty`. In the example the `outputLabel` should display the
newly selected option whenever it changes.

```kotlin
comboBox.selectedItemProperty.addListener { _, newValue ->
    outputLabel.text = "Combo box selection is : $newValue"
}
```

## ProgressBar

A [ProgressBar][ProgressBarKDoc] is a coloured bar that can indicate progress to the user. The current progress and
colour can be retrieved and set via `progress` and `barColor` respectively.

In this example a new ProgressBar is instantiated and the `onMouseClicked` is set, such that the progress of the
bar is
advanced by 5% on a click or reset if the progress is greater than 100%.

> The progress is modelled as a `Double`, where any value equal or less than 0 means 0% progress and any value equal or greater than 1 means 100% progress.
> {style="note"}

```kotlin
val progressBar = ProgressBar(posX = 450, posY = 350, width = 300, progress = 0.5, barColor = Color.GREEN)

progressBar.onMouseClicked = {
    progressBar.progress = if (progressBar.progress > 1.0) 0.0 else progressBar.progress + 0.05
}
```

Additionally, to react to change to the progress a listener is added to the `progressProperty`. In this case updating
the `barColor` based on the current progress level.

```kotlin
progressBar.progressProperty.addListener { _, newValue ->
    when {
        newValue > 0.8 -> progressBar.barColor = Color.RED
        newValue > 0.5 -> progressBar.barColor = Color.YELLOW
        else -> progressBar.barColor = Color.GREEN
    }
}
```

## ToggleButton and RadioButton

A [ToggleButton][ToggleButtonKDoc] can either be selected or not selected. A [RadioButton][RadioButtonKDoc] is just a
ToggleButton with a different visual representation. It has exactly the same functionalities as a ToggleButton.

To retrieve whether the ToggleButton is toggled on or off the `isSelected` field can be used.

ToggleButtons have a feature where different ToggleButtons may be linked via a [ToggleGroup][ToggleGroupKDoc].
ToggleGroups enforce a rule, where only one ToggleButton in a ToggleGroup may be selected at a time. So whenever a
ToggleButton in
a ToggleGroup changes its selected state to `true`, all other ToggleButtons in the ToggleGroup automatically have
their selected state set to `false`.

> Setting a ToggleGroup on a ToggleButton is an exception. The ToggleGroup rule only gets enforced whenever a contained ToggleButton changes its state. So it is technically possible to have multiple selected ToggleButtons in a ToggleGroup by setting the ToggleGroup on multiple pre-selected ToggleButtons.
> {style="note"}

In this example a new ToggleGroup, Toggle- and RadioButton are instantiated. The `toggleGroup` is set as
the `toggleGroup` for both buttons.

```kotlin
val toggleGroup = ToggleGroup()

val radioButton = RadioButton(posX = 50, posY = 450, toggleGroup = toggleGroup)

val toggleButton = ToggleButton(posX = 450, posY = 450, toggleGroup = toggleGroup)
```

To react to change of the selected state, a listener can be added to the `selectedProperty`. In the example the text
of the Label `radioLabel` gets updated, whenever the selected state of `radioButton` changes.

```kotlin
radioButton.selectedProperty.addListener { _, newValue ->
    radioLabel.text = if (newValue) "This is a selected radio button!" else "This is a deselected radio button!"
}
```

## TextArea, TextField and PasswordField

[TextArea][TextAreaKDoc], [TextField][TextFieldKDoc] and [PasswordField][PasswordFieldKDoc] can be used to allow users
to type texts. The main difference between TextArea and TextField/PasswordField, is that [TextArea][TextAreaKDoc] has
multiple lines of input, while [TextField][TextFieldKDoc] and [PasswordField][PasswordFieldKDoc] only have one. The
text can be set and retrieved via `text`. A prompt can be specified that informs the user, which textual input is
requested. To react to user input, a listener on the `textProperty` can be added. Additionally, the
`onKeyPressed`, `onKeyReleased` and `onKeyTyped` handlers are a great way of executing code when textual input
occurs. [PasswordField][PasswordFieldKDoc] acts like a [TextField][TextFieldKDoc] but uses stars to display the typed
text.

In this example a TextArea, and a TextField with `prompt`s are instantiated and listeners on their `textProperty`s
are added, that update the `outputLabel` with the current `text`.

```kotlin
val textArea = TextArea(posX = 50, posY = 600, prompt = "Type something! This is the prompt.")
textArea.textProperty.addListener { _, newValue ->
    outputLabel.text = newValue
}

val textField = TextField(posX = 450, posY = 600, width = 300, prompt = "Type something! This is the prompt.")
textField.textProperty.addListener { _, newValue ->
    outputLabel.text = newValue
}
```

## ListView

A [ListView][ListViewKDoc] can be used to display a list of strings. The ListView has a type parameter, so any type of
objects can be contained in the ListView. By default, the `toString()` function is applied to the contained objects to
obtain a string representation of each object. A `formatFunction` can be set that gets used instead of
the `toString()` function to obtain the string representations.

In this example, a ListView with initial `Int` items is created..

```kotlin
val listView = ListView(posX = 50, posY = 800, width = 300, height = 200, items = mutableListOf(42, 1337, 1, 2, 3))
```

A custom `formatFunction` is set, so the ListView displays "Value for this cell is" concatenated with the actual value
of the contained object.

```kotlin
listView.formatFunction = {
    "Value for this cell is $it"
}
```

## TableView

A [TableView][TableViewKDoc] can be used to display multiple representations of the same objects in pre-defined columns.
The TableView has a type parameter, so any type of objects can be contained in the TableView.

In this example a TableView typed to `Int` is instantiated.

```kotlin
val table = TableView<Int>(posX = 450, posY = 800, width = 300, height = 200)
```

Three custom [TableColumn][TableColumnKDoc]s are added to the columns list of the TableView. A TableColumn consists of
a `title`, a `width` and a `formatFunction` that defines how to project a contained object to a string for this
column.

The three columns are as follows:

- `"Value"` displays the actual value of the contained int, so the `fromatFunction` just wraps the value in a
  string.
- `"Squared"` display the squared value of the contained int, so the `formatFunction` calculates the square of the
  value
  and wraps it in a string.
- `"Even?"` displays whether the value of the contained int is even, so the `formatFucntion` checks if the value is
  divisible by two and wraps the resulting boolean in a string.

```kotlin
table.columns.add(TableColumn(title = "Value", width = 100) { "$it" })

table.columns.add(TableColumn(title = "Squared", width = 100) { "${it * it}" })

table.columns.add(TableColumn(title = "Even?", width = 100) { "${it % 2 == 0}" })
```

Lastly, to specify the data model for the TableView, some ints are added to the `items` list.

```kotlin
table.items.addAll(listView.items)
```

## Loading custom fonts

The [UIComponent][UIComponentKDoc] class has a parameter font, which can be passed. This means all derived component
classes like e.g. [Label][LabelKDoc] can be constructed with a custom font.

In order to load a custom font use the [BoardGameApplication][BoardGameApplicationKDoc] class.

```kotlin
// Load Roboto-Regular.ttf from root of resource folder
val resource = UIComponentExample::class.java.getResource("/Roboto-Regular.ttf") ?: throw FileNotFoundException()
val fontFile = File(resource.toURI())
BoardGameApplication.loadFont(fontFile)
```

Now the font is registered and ready to use.

```kotlin
private val label = Label(
    text = "I am a Label.",
    font = Font(family = "Roboto")
)
```

## Complete source code for the example

[View it on GitHub](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-docs-examples/src/main/kotlin/examples/components/uicomponents/UIComponentExample.kt){:
.btn }

```kotlin
fun main() {
    UIComponentExample()
}

class UIComponentExample : BoardGameApplication("UIComponent Example") {
    private val menuScene = MenuScene(width = 800).apply {
        this.opacity = 1.0
    }

    private val outputLabel = Label(
        posX = 50,
        posY = 50,
        width = 300,
        text = "I am a Label.",
        alignment = Alignment.CENTER,
        isWrapText = true
    )

    init {
        menuScene.addComponents(outputLabel)

        //Button
        val button = Button(posX = 450, posY = 50, text = "I am a Button.", visual = ColorVisual.LIGHT_GRAY)

        button.onMouseClicked = {
            outputLabel.text = "Someone pressed the Button!"
        }

        menuScene.addComponents(button)

        //CheckBox
        val checkBox =
            CheckBox(posX = 50, posY = 150, width = 300, text = "I am a CheckBox.", alignment = Alignment.CENTER_LEFT)

        checkBox.allowIndeterminate = true

        checkBox.checkedProperty.addListener { _, newValue ->
            outputLabel.text = if (newValue) "The check box is checked!" else "The check box is unchecked!"
        }

        checkBox.indeterminateProperty.addListener { _, newValue ->
            if (newValue) outputLabel.text = "The check box is indeterminate!"
        }

        menuScene.addComponents(checkBox)

        //ColorPicker
        val colorPicker = ColorPicker(posX = 450, posY = 200, width = 300, initialColor = Color.BLACK)

        val colorPickerLabel = Label(
            posX = colorPicker.posX,
            posY = colorPicker.posY - 50,
            width = colorPicker.width,
            height = 50,
            alignment = Alignment.CENTER,
            font = Font(color = colorPicker.selectedColor),
            text = "This is a ColorPicker. Use it to change the colour of this text!"
        ).apply { isWrapText = true }

        colorPicker.selectedColorProperty.addListener { _, newValue ->
            colorPickerLabel.font = Font(color = newValue)
        }

        menuScene.addComponents(colorPicker, colorPickerLabel)

        //ComboBox
        val comboBox =
            ComboBox<Double>(posX = 50, posY = 350, width = 300, prompt = "Select an option! This is the prompt.")

        comboBox.formatFunction = {
            "Option ${it.toInt()}"
        }

        comboBox.items = mutableListOf(0.0, 1.0, 2.0)

        comboBox.selectedItemProperty.addListener { _, newValue ->
            outputLabel.text = "Combo box selection is : $newValue"
        }

        val comboBoxLabel = Label(
            posX = comboBox.posX,
            posY = comboBox.posY - 50,
            width = comboBox.width,
            height = comboBox.height,
            alignment = Alignment.CENTER,
            text = "This is a ComboBox"
        )

        menuScene.addComponents(comboBox, comboBoxLabel)

        //ProgressBar
        val progressBar = ProgressBar(posX = 450, posY = 350, width = 300, progress = 0.5, barColor = Color.GREEN)

        progressBar.onMouseClicked = {
            progressBar.progress = if (progressBar.progress > 1.0) 0.0 else progressBar.progress + 0.05
        }

        progressBar.progressProperty.addListener { _, newValue ->
            when {
                newValue > 0.8 -> progressBar.barColor = Color.RED
                newValue > 0.5 -> progressBar.barColor = Color.YELLOW
                else -> progressBar.barColor = Color.GREEN
            }
        }

        val progressBarLabel = Label(
            posX = progressBar.posX,
            posY = progressBar.posY - 50,
            width = progressBar.width,
            height = 50,
            alignment = Alignment.CENTER,
            text = "This is a ProgressBar. Click it to progress it!"
        )

        menuScene.addComponents(progressBar, progressBarLabel)

        //RadioButton and ToggleButton
        val toggleGroup = ToggleGroup()

        val radioButton = RadioButton(posX = 50, posY = 450, toggleGroup = toggleGroup)

        val radioLabel = Label(
            posX = radioButton.posX + radioButton.width,
            posY = radioButton.posY,
            width = 300 - radioButton.width,
            height = radioButton.height,
            text = "This is a RadioButton.",
            alignment = Alignment.CENTER_LEFT
        ).apply { isWrapText = true }

        radioButton.selectedProperty.addListener { _, newValue ->
            radioLabel.text = if (newValue) "This is a selected radio button!" else "This is a deselected radio button!"
        }

        menuScene.addComponents(radioButton, radioLabel)

        val toggleButton = ToggleButton(posX = 450, posY = 450, toggleGroup = toggleGroup)

        val toggleLabel = Label(
            posX = toggleButton.posX + toggleButton.width,
            posY = toggleButton.posY,
            width = 300 - toggleButton.width,
            height = toggleButton.height,
            text = "This is a ToggleButton.",
            alignment = Alignment.CENTER_LEFT
        ).apply { isWrapText = true }

        toggleButton.selectedProperty.addListener { _, newValue ->
            toggleLabel.text =
                if (newValue) "This is a selected toggle button!" else "This is a deselected toggle button!"
        }

        menuScene.addComponents(toggleButton, toggleLabel)

        //TextArea
        val textArea = TextArea(posX = 50, posY = 600, prompt = "Type something! This is the prompt.")
        textArea.textProperty.addListener { _, newValue ->
            outputLabel.text = newValue
        }

        val textAreaLabel = Label(
            posX = textArea.posX,
            posY = textArea.posY - 50,
            width = textArea.width,
            height = 50,
            alignment = Alignment.CENTER,
            text = "This is a TextArea."
        )

        menuScene.addComponents(textArea, textAreaLabel)

        //TextField
        val textField = TextField(posX = 450, posY = 600, width = 300, prompt = "Type something! This is the prompt.")
        textField.textProperty.addListener { _, newValue ->
            outputLabel.text = newValue
        }

        val textFieldLabel = Label(
            posX = textField.posX,
            posY = textField.posY - 50,
            width = textField.width,
            height = 50,
            alignment = Alignment.CENTER,
            text = "This is a TextField."
        )

        menuScene.addComponents(textField, textFieldLabel)

        //ListView
        val listView = ListView<Int>(posX = 50, posY = 800, width = 300, height = 200)
        listView.formatFunction = {
            "Value for this cell is $it"
        }

        listView.items = mutableListOf(42, 1337, 1, 2, 3)
        val listViewLabel = Label(
            posX = listView.posX,
            posY = listView.posY - 50,
            width = listView.width,
            height = 50,
            text = "This is a ListView.",
            alignment = Alignment.CENTER
        )

        menuScene.addComponents(listView, listViewLabel)

        //TableView
        val table = TableView<Int>(posX = 450, posY = 800, width = 300, height = 200)

        table.columns.add(TableColumn(title = "Value", width = 100) { "$it" })

        table.columns.add(TableColumn(title = "Squared", width = 100) { "${it * it}" })

        table.columns.add(TableColumn(title = "Even?", width = 100) { "${it % 2 == 0}" })

        table.items.addAll(mutableListOf(42, 1337, 1, 2, 3))

        val tableLabel = Label(
            posX = table.posX,
            posY = table.posY - 50,
            width = table.width,
            height = 50,
            text = "This is a TableView.",
            alignment = Alignment.CENTER
        )

        menuScene.addComponents(table, tableLabel)

        showMenuScene(menuScene)
        show()
    }
}
```
