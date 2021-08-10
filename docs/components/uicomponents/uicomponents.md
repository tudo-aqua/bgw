---
parent: Components 
title: UIComponents 
nav_order: 3 
layout: default
---

[UIComp]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-u-i-component/index.html

[LUIComp]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-labeled-u-i-component/index.html

[TIUIComp]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-text-input-u-i-component/index.html

[UserInput]: https://tudo-aqua.github.io/bgw/concepts/user-input/UserInput.html

[LabelDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-label/index.html

# UIComponents

A [UIComponent][UIComp] may be used to present information to the user or retrieve input. There are three base classes
for UIComponents.

- [UIComponent][UIComp]: the baseclass for all UIComponents
- [LabeledUIComponents][LUIComp]: extends UIComponent and provides additional fields to define a text. E.g. a Button
  with a text.
- [TextInputUIComponents][TIUIComp]: extends UIComponent and provides a text where the user may write to.

**NOTE:** UIComponentViews are ComponentViews. This means all methods of handling user input discussed in the
[User Input Guide][UserInput] are available to UIComponentViews.

In this section example code for the following application will be shown and used to demonstrate all important features
of UIComponents.

View the complete source code [here]().

[!image](docs/components/uicomponents/visualguide.png)

## Label

A [Label][LabelDoc] is just a simple text. In this example a new label is instantiated with the text 'I am a Label.',
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

A [Button][ButtonDoc] is a component that plays a 'click' animation, when a mouse click is performed over the button.
Additionally, a text may be defined to describe the button. In this example a new button is instantiated, and the '
onMouseClicked' is set, so that the 'outputLabel' display "Someone pressed the Button!".

````kotlin
val button = Button(posX = 450, posY = 50, text = "I am a Button.")

button.onMouseClicked = {
	outputLabel.text = "Someone pressed the Button!"
}

````

[CheckBoxDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.uicomponents/-check-box/index.html

## CheckBox

A [CheckBox][CheckBoxDoc] is a component that can be checked or unchecked. It can be enabled to allow an indeterminate
state. This can be used to express no selection. Whenever a CheckBox is clicked it changes its state in the following
order:

if indeterminate is not allowed

| ``checked``  |
| ------------ |
| ``false``    |
| ``true``     |

if indeterminate is allowed

| ``checked``  | ``indeterminate`` |
| ------------ | ----------------- |
| ``false``    | ``false``         |
| ``false``    | ``true``          |
| ``true``     | ``false``         |

In this example listeners are added to the ``checkedProperty`` and ``indeterminateProperty``, so that
``outputLabel`` displays the state of the CheckBox, whenever the state changes.

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



