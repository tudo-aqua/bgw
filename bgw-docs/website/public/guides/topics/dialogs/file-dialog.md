[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[FileDialogKDoc]: /docs/tools.aqua.bgw.dialog/-file-dialog/index.html
[FileDialogModeKDoc]: /docs/tools.aqua.bgw.dialog/-file-dialog-mode/index.html
[ExtensionFilterKDoc]: /docs/tools.aqua.bgw.dialog/-extension-filter/index.html

<!-- GH-Pages Doc -->

[DialogDoc]: /guides/dialogs/dialog
[HandlingEvents]: /guides/handling-events

# File Dialog

<tldr>Let the player choose files and folders</tldr>

## Introduction

A [FileDialog][FileDialogKDoc] opens the file chooser of the operating system. Use it to load or save game states, to pick an image or to choose a folder. Like [message dialogs][DialogDoc], it is **non-blocking**: the result is delivered to a handler.

---

## Creating a file dialog

Choose what the player is asked for.

<signature>
<code-block lang="kotlin" copy="false">
FileDialog(
    mode: FileDialogMode,
    title: String = "",
    initialFileName: String = "",
    extensionFilters: List&lt;ExtensionFilter&gt; = emptyList()
)
</code-block>

The [FileDialogMode][FileDialogModeKDoc] decides what the dialog asks for:

<table orientation="horizontal">
    <tr>
        <td width="30%"><code>OPEN_FILE</code></td>
        <td>Choose exactly one existing file.</td>
    </tr>
    <tr>
        <td><code>OPEN_MULTIPLE_FILES</code></td>
        <td>Choose one or more existing files.</td>
    </tr>
    <tr>
        <td><code>SAVE_FILE</code></td>
        <td>Choose a location and a name for a file that is about to be written.</td>
    </tr>
    <tr>
        <td><code>CHOOSE_DIRECTORY</code></td>
        <td>Choose a folder.</td>
    </tr>
</table>

`title` is the text in the title bar and `initialFileName` the pre-filled name, for example `"savegame.json"` for saving.

</signature>

### Filtering files

An [ExtensionFilter][ExtensionFilterKDoc] limits the files that can be chosen. It consists of a description and the extensions that are allowed, with or without a leading dot:

```kotlin
val dialog = FileDialog(
    mode = FileDialogMode.OPEN_FILE,
    title = "Choose an image",
    extensionFilters = listOf(
        ExtensionFilter("Images", "png", "jpg", "jpeg")
    )
)
```

---

## Showing a file dialog and reading the result

Open the dialog and receive the selected paths.

<signature>
<code-block lang="kotlin" copy="false">
showFileDialog(dialog: FileDialog)
</code-block>

Opens the dialog above the application window. The function is provided by the [BoardGameApplication][BoardGameApplicationKDoc].

<br>

<code-block lang="kotlin" copy="false">
onPathsSelected: ((List&lt;String&gt;) -&gt; Unit)?
onSelectionCancelled: (() -&gt; Unit)?
</code-block>

Exactly one of the handlers is invoked after the dialog was closed. `onPathsSelected` receives the **absolute paths** of the selection. For `OPEN_FILE`, `SAVE_FILE` and `CHOOSE_DIRECTORY` the list contains one entry, for `OPEN_MULTIPLE_FILES` one entry per selected file. `onSelectionCancelled` is invoked if the player closed the dialog without a selection.

</signature>

```kotlin
val dialog = FileDialog(
    mode = FileDialogMode.SAVE_FILE,
    title = "Save game",
    initialFileName = "savegame.json"
)

dialog.onPathsSelected = { paths ->
    File(paths.first()).writeText(serializedGame)
}
dialog.onSelectionCancelled = {
    println("Nothing was saved")
}

showFileDialog(dialog)
```

> The handlers are invoked after the code that opened the dialog has already continued. Place everything that depends on the selection in the handler, like in [Handling Events][HandlingEvents].
> {style="warning"}
