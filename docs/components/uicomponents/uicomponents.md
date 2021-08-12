---
parent: Components title: UIComponents nav_order: 3 layout: default
---

[UIComp]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-u-i-component/index.html

[LUIComp]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-labeled-u-i-component/index.html

[TIUIComp]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-text-input-u-i-component/index.html

[UserInput]: https://tudo-aqua.github.io/bgw/concepts/user-input/UserInput.html

# UIComponents

A [UIComponent][UIComp] may be used to present information to the user or retrieve input. There are three base classes
for UIComponents.

- [UIComponent][UIComp]: the baseclass for all UIComponents
- [LabeledUIComponents][LUIComp]: extends UIComponent and provides additional fields to define a text. E.g. a Button
  with a text.
- [TextInputUIComponents][TIUIComp]: extends UIComponent and provides a text where the user may write to.

**NOTE:** UIComponentViews are ComponentViews. This means all methods of handling user input discussed in the
[User Input Guide][UserInput] are available to UIComponentViews.

This is a visual example of all the available UIComponents in the framework. Parts of the source code will be used in
this tutorial to demonstrate the most important features of each UIComponent. The full source code can be found [here]()
.

![image](visualguide.png)

[LabelDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-label/index.html

## Label

A [Label][LabelDoc] is just a simple text. In this example a new label is instantiated with the text "I am a Label.",
aligned to the center and with text wrapping enabled. Enabled text wrapping allows the text to wrap onto a new line if
the width of the label is too small for the text.

````kotlin
private val outputLabel = Label(
	posX = 50,
	posY = 50,
	width = 300,
	text = "I am a Label.",
	alignment = Alignment.CENTER,
	isWrapText = true
)
````

[ButtonDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-button/index.html

## Button

A [Button][ButtonDoc] is a component that plays an animation when a mouse click is performed over the button.
Additionally, a text may be defined to describe the button. In this example a new button is instantiated, and the ``
onMouseClicked`` is set, so that the ``outputLabel`` displays "Someone pressed the Button!".

````kotlin
val button = Button(posX = 450, posY = 50, text = "I am a Button.")

button.onMouseClicked = {
	outputLabel.text = "Someone pressed the Button!"
}

````

[CheckBoxDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-check-box/index.html

## CheckBox

A [CheckBox][CheckBoxDoc] is a component that can be checked or unchecked. It can be enabled to allow an indeterminate
state. This can be used to express uncertainty. Whenever a CheckBox is clicked it changes its state in the following
order:

if indeterminate is not allowed

| ``checked``  | ``indeterminate`` |
| ------------ | ----------------- |
| ``false``    | ``false``         |
| ``true``     | ``false``         |

if indeterminate is allowed

| ``checked``  | ``indeterminate`` |
| ------------ | ----------------- |
| ``false``    | ``false``         |
| ``false``    | ``true``          |
| ``true``     | ``false``         |

In this example listeners are added to the ``checkedProperty`` and ``indeterminateProperty``, so that
``outputLabel`` displays the state of the CheckBox, whenever the state changes.

**NOTE:** it is possible to set ``isIndeterminate`` to ``true`` even when indeterminate is not allowed. It is just
impossible to reach the indeterminate state via clicks on the CheckBox.

````kotlin
val checkBox =
	CheckBox(posX = 50, posY = 150, width = 300, text = "I am a CheckBox.", alignment = Alignment.CENTER_LEFT)

checkBox.allowIndeterminate = true

checkBox.checkedProperty.addListener { _, newValue ->
	outputLabel.text = if (newValue) "The check box is checked!" else "The check box is unchecked!"
}

checkBox.indeterminateProperty.addListener { _, newValue ->
	if (newValue) outputLabel.text = "The check box is indeterminate!"
}
````

[ColPicDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-color-picker/index.html

## ColorPicker

A [ColorPicker][ColPickDoc] may be used to enable a user to specify a colour in an intuitive way. The selected colour
can be set and retrieved via the ``selectedColor``. To react to a new ``selectedColor``, a listener may be added to
the ``selectedColorProperty``. In this example the text colour of another label is changed, whenever a new colour is
picked.

````kotlin
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
````

[CombDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-combo-box/index.html

## ComboBox

A [ComboBox][CombDoc] is a drop down menu, where a user may choose an option. A ``prompt`` may be specified to inform
the user what is expected of him. The ComboBox has a type parameter to define a type for the objects that correspond to
the options. One string per contained object is needed to represent it as an option to the user. There are two methods
of obtaining said string.

A ``formatFunction``, that projects the contained objects to a string representation may be set. If
no ``formatFunction`` is set, the ``toString``
function of the contained object is used to obtain a string.

In this example a ComboBox typed to ``Double`` with the ``prompt`` "Select an option! This is the prompt." is
instantiated. Then the ``formatFunction`` is set, so the contained doubles get represented as their Int value with the
suffix "Option ".

````kotlin
val comboBox =
	ComboBox<Double>(posX = 50, posY = 350, width = 300, prompt = "Select an option! This is the prompt.")

comboBox.formatFunction = {
	"Option ${it.toInt()}"
}
````

To define the options simply set the ``items`` list. In the example three doubles get added.

````kotlin
comboBox.items = mutableListOf(0.0, 1.0, 2.0)
````

The ``selectedItem`` can be used to retrieve or set the current selected item. To react to a change of the selected
item, a listener can be added to the ``selectedItemProperty``. In the example the ``outputLabel`` should display the
newly selected option whenever it changes.

````kotlin
comboBox.selectedItemProperty.addListener { _, newValue ->
	outputLabel.text = "Combo box selection is : $newValue"
}
````