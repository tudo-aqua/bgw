---
parent : Dialogs
title: Dialog 
nav_order: 1 
layout: default
---

# Dialog
{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

This section showcases the different types of [Dialogs](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-dialog/index.html) 
in the BGW framework. A Dialog can be used to
display a popup informing the user about warnings, and errors or text.

## Dialog creation

The [Dialog](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-dialog/index.html) class provides two public constructors for two different types of dialogs:

### Information Dialog
In the information dialog's constructor the type of the dialog can be declared by the [DialogType](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-dialog-type/index.html) as

* ``INFORMATION``
* ``WARNING``
* ``ERROR``
* ``CONFIRMATION``
* ``NONE``

which directly affects the displayed icon and default buttons. The buttons can be altered by passing [ButtonTypes](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-button-type/index.html).  
Note that ``EXCEPTION`` may not be used, as it is created through the second constructor [here](https://tudo-aqua.github.io/bgw/dialogs/dialog/dialog.html#exception-dialog).
Additionally, the ``title`` of the popup, the ``header`` and the ``content`` parameters have to be passed.
The following code example creates a warning dialog informing the user about an empty player name:

````kotlin
Dialog(
  alertType = AlertType.WARNING,
  title = "Warning",
  header = "Empty player name",
  message = "Player name must not be empty!"
)
````
![warning_dialog](warning_dialog.png)

### Exception dialog
To display an exception stack trace the second constructor can be used. It takes a ``title``, ``header``, and 
``message``, as well as the ``exception`` to display.
It contains an expandable content for the exception stack trace.

````kotlin
Dialog(
  title = "Exception Dialog",
  header = "Exception",
  message = "An exception Dialog.",
  exception = IllegalArgumentException("IllegalArgument passed.")
)
````
![exception_dialog](exception_dialog.png)

An example with all dialog types can be found [here](https://github.com/tudo-aqua/bgw/blob/main/bgw-docs-examples/src/main/kotlin/examples/dialog/DialogExample.kt)

## Showing a dialog
To show a dialog the method [#showDialog](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/show-dialog.html) 
in [BoardGameApplication](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/index.html) 
has to be used.
The operation blocks user input until the dialog is closed. The function returns an 
[Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) containing the chosen 
[ButtonType](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-button-type/index.html). 
The Optional is empty if the dialog is closed by the *X* or in any other way aside selecting any button.

````kotlin
val dialog: Dialog = Dialog(
    alertType = AlertType.CONFIRMATION, 
    title = "Confirmation required", 
    header = "Confirmation", 
    message = "Do you really want to proceed?"
)

showDialog(dialog).ifPresentOrElse({ 
    if (it == ButtonType.YES) {
        //Button 'YES' was clicked 
    } else {
        //Button 'NO' was clicked 
    }
}) {
	//Dialog was closed
}
````
