---
parent: Concepts
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

In this section we are going to showcase the differend types of dialogs in the BGW framework.
Dialog can be used to display a popup ingorming the user about warnings, and errors or just displaying text.

## Dialog creation

The Dialog class provides two public constructors for two different types of dialogs:

### Information Dialog
In the information dialog's constructor the type of the dialog can be declared by the [AlertType](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-alert-type/) as

* INFORMATION
* WARNING
* ERROR
* CONFIRMATION
* NONE

which directly affects the displayed icon and default buttons. The buttons can be altered by passing [ButtonType](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-button-type/)s.  Not that you may not use EXCEPTION as it is created through the second constructor [here](bgw/dialog/dialog.html#exception%20dialog)
Additional the title of the popup, the header and the content text has to be passed.
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
To display an exception stack trace the second constructor can be used. It takes a title, header, and message, as well as the exception to display.
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

An example with all dialog types can be found [here](https://github.com/tudo-aqua/bgw/blob/documentation/bgw-docs-examples/src/main/kotlin/examples/dialog/DialogExample.kt)

## Showing a dialog
To show a dialog use [#showDialog](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/show-dialog.html) in [BoardGameApplication](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/index.html).
The operation blocks user input until the dialog was closed. The function returns an [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) containing the chosen [ButtonType](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-button-type/index.html). The optional is empty if the dialog was closed by the X or in any other way aside selecting any button.

````kotlin
val dialog = Dialog(
  alertType = AlertType.CONFIRMATION,
  title = "Confirmation required",
  header = "Confirmation",
  message = "Do you really want to proceed?"
)
		
showDialog(dialog).ifPresentOrElse({
  if(it == ButtonType.YES) {
    //Button YES was clicked
  }else{
    //Button NO was clicked
  }
}) { 
  //Dialog was closed
}
````
