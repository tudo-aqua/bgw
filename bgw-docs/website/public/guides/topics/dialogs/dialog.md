[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[DialogKDoc]: /docs/tools.aqua.bgw.dialog/-dialog/index.html
[DialogTypeKDoc]: /docs/tools.aqua.bgw.dialog/-dialog-type/index.html
[ButtonTypeKDoc]: /docs/tools.aqua.bgw.dialog/-button-type/index.html

<!-- GH-Pages Doc -->

[FileDialogDoc]: /guides/dialogs/file-dialog
[HandlingEvents]: /guides/handling-events

# Dialog

<tldr>Inform the player or ask for a decision</tldr>

## Introduction

A [Dialog][DialogKDoc] is a small window that opens above your game. Use it to inform the player about something that happened, to warn about a problem or to ask a question that needs an answer before the game can continue, like _"Do you really want to quit?"_.

A dialog opens as **its own window** next to the game window. It uses the dark theme of BGW and contains a header, a message and one or more buttons.

> Dialogs are **non-blocking**: `showDialog()` returns immediately and the game keeps running. The player's answer is delivered through the `onButtonClicked` handler, see [Reacting to buttons](#reacting-to-buttons).
> {style="warning"}

---

## Creating a dialog

There are two ways to create a dialog: one for messages and one for exceptions.

### Message dialogs

Show a title, a headline and a message with buttons.

<signature>
<code-block lang="kotlin" copy="false">
Dialog(
    dialogType: DialogType,
    title: String,
    header: String,
    message: String,
    vararg buttons: ButtonType
)
</code-block>

`title` is shown in the title bar of the dialog window, `header` is the headline in its content and `message` the text below. The [DialogType][DialogTypeKDoc] selects the icon and the default buttons.

</signature>

| DialogType | Default buttons | Use it for |
| --- | --- | --- |
| `NONE` | OK | A plain message without any icon |
| `INFORMATION` | OK | Informing about something, like the end of a game |
| `WARNING` | OK | Warning about something that is not right yet |
| `CONFIRMATION` | Yes, No | Asking the player for a decision |
| `ERROR` | OK | Telling the player that something went wrong |

```kotlin
val dialog = Dialog(
    dialogType = DialogType.WARNING,
    title = "Warning",
    header = "Empty player name",
    message = "Player name must not be empty!"
)
```

To use other buttons than the defaults, pass them as additional arguments. Available [ButtonTypes][ButtonTypeKDoc] are `APPLY`, `OK`, `CANCEL`, `CLOSE`, `YES`, `NO`, `FINISH`, `NEXT`, `PREVIOUS` and `DISMISS`:

```kotlin
val dialog = Dialog(
    dialogType = DialogType.CONFIRMATION,
    title = "Save game",
    header = "Unsaved progress",
    message = "Do you want to save before leaving?",
    ButtonType.YES, ButtonType.NO, ButtonType.CANCEL
)
```

> `DialogType.EXCEPTION` can not be used with this constructor, an `IllegalArgumentException` is thrown. Use the exception dialog instead.
> {style="warning"}

### Exception dialogs

Show an exception together with its stack trace.

<signature>
<code-block lang="kotlin" copy="false">
Dialog(
    title: String,
    header: String,
    message: String,
    exception: Throwable
)
</code-block>

Displays an exception together with its stack trace. This is the right choice if something unexpected happened that the player should be able to report. It has a single `DISMISS` button and opens as a wider window to fit the stack trace.

</signature>

```kotlin
val dialog = Dialog(
    title = "Exception Dialog",
    header = "Exception",
    message = "An exception Dialog.",
    exception = IllegalArgumentException("IllegalArgument passed.")
)
```

---

## Showing a dialog

Dialogs are opened by the application.

<signature>
<code-block lang="kotlin" copy="false">
showDialog(dialog: Dialog)
showDialogNonBlocking(dialog: Dialog)
</code-block>

Both functions of the [BoardGameApplication][BoardGameApplicationKDoc] open the dialog and return immediately. As all dialogs are shown without blocking, the two behave the same.

</signature>

```kotlin
object DiceDuelApplication : BoardGameApplication(windowTitle = "Dice Duel") {
    fun showHelp() {
        showDialog(Dialog(
            dialogType = DialogType.INFORMATION,
            title = "How to play",
            header = "Roll the dice",
            message = "The higher number wins the round. Three points win the game."
        ))
    }
}
```

> Only the application can show dialogs. If you need a dialog from within a scene, pass a function from the application to the scene, like the `onGameOver` handler in [Handling Events][HandlingEvents].
> {style="note"}

---

## Reacting to buttons

Find out which button the player clicked.

<signature>
<code-block lang="kotlin" copy="false">
onButtonClicked: ((ButtonType) -&gt; Unit)?
</code-block>

Gets invoked with the [ButtonType][ButtonTypeKDoc] the player clicked. The dialog is closed afterwards. Set the handler **before** showing the dialog.

</signature>

As the game continues while the dialog is open, the code that depends on the answer has to be placed in this handler instead of after `showDialog`:

```kotlin
val dialog = Dialog(
    dialogType = DialogType.CONFIRMATION,
    title = "Confirmation required",
    header = "Quit",
    message = "Do you really want to quit?"
)

dialog.onButtonClicked = { button ->
    if (button == ButtonType.YES) {
        exit()
    }
}

showDialog(dialog)
```

> `onButtonClicked` is only invoked when one of the dialog's buttons is clicked. For decisions that must always be answered, consider locking the `BoardGameScene` with `lock()` until the handler was called.
> {style="warning"}
