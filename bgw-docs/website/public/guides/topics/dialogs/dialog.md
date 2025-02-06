[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[DialogKDoc]: /docs/tools.aqua.bgw.dialog/-dialog/index.html
[DialogTypeKDoc]: /docs/tools.aqua.bgw.dialog/-dialog-type/index.html
[ButtonTypeKDoc]: /docs/tools.aqua.bgw.dialog/-button-type/index.html
[showDialogKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-dialog.html
[showDialogNonBlockingKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-dialog-non-blocking.html
[OptionalDoc]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Optional.html

> This guide is currently being rewritten. Content may be incomplete, incorrect or subject to change.
> {style="danger"}

# Dialog

This section showcases the different types of [Dialogs][DialogKDoc]
in the BGW framework. A Dialog can be used to
display a popup informing the user about warnings, and errors or text.

## Dialog creation

The [Dialog][DialogKDoc] class provides two public constructors for two different types of dialogs:

### Information Dialog

In the information dialog's constructor the type of the dialog can be declared by the [DialogType][DialogTypeKDoc] as

- `INFORMATION`
- `WARNING`
- `ERROR`
- `CONFIRMATION`
- `NONE`

which directly affects the displayed icon and default buttons. The buttons can be altered by passing [ButtonTypes][ButtonTypeKDoc].  
Note that `EXCEPTION` may not be used, as it is created through the second constructor [here](#Exception dialog).
Additionally, the `title` of the popup, the `header` and the `content` parameters have to be passed.
The following code example creates a warning dialog informing the user about an empty player name:

```kotlin
Dialog(
  dialogType = DialogType.WARNING,
  title = "Warning",
  header = "Empty player name",
  message = "Player name must not be empty!"
)
```

![warning_dialog](warning_dialog.png)

### Exception dialog

To display an exception stack trace the second constructor can be used. It takes a `title`, `header`, and
`message`, as well as the `exception` to display.
It contains an expandable content for the exception stack trace.

```kotlin
Dialog(
  title = "Exception Dialog",
  header = "Exception",
  message = "An exception Dialog.",
  exception = IllegalArgumentException("IllegalArgument passed.")
)
```

![exception_dialog](exception_dialog.png)

## Showing a dialog

To show a dialog the method [#showDialog][showDialogKDoc] in [BoardGameApplication][BoardGameApplicationKDoc] has to be
used. The operation blocks user input until the dialog is closed. The function returns an [Optional][OptionalDoc]
containing the chosen [ButtonType][ButtonTypeKDoc].
The Optional is empty if the dialog is closed by the _X_ or in any other way aside selecting any button.

Alternatively a dialog may be shown by [#showDialogNonBlocking][showDialogNonBlockingKDoc] which shows the Dialog
without blocking further thread execution. This functions returns `Unit`.

```kotlin
val dialog: Dialog = Dialog(
    dialogType = DialogType.CONFIRMATION,
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
```
