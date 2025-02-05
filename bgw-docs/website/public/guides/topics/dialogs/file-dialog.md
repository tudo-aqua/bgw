[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[DialogKDoc]: /docs/tools.aqua.bgw.dialog/-dialog/index.html
[FileDialogKDoc]: /docs/tools.aqua.bgw.dialog/-file-dialog/index.html
[FileDialogModeKDoc]: /docs/tools.aqua.bgw.dialog/-file-dialog-mode/index.html
[showFileDialogKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-file-dialog.html
[OptionalDoc]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Optional.html
[FilesDoc]: https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/nio/file/Files.html

> This guide is currently being rewritten. Content may be incomplete, incorrect or subject to change.
> {style="danger"}

# FileDialog

This section showcases the different types of file dialogs in the BGW framework. FileDialog can be
used to display a popup to choose a file or directory either to load or save resources.

## Dialog creation

The [FileDialog][FileDialogKDoc]
class provides different [FileDialogModes][FileDialogModeKDoc]:

- `OPEN_FILE`
- `OPEN_MULTIPLE_FILES`
- `SAVE_FILE`
- `CHOOSE_DIRECTORY`

Depending on the selection the [Dialog][DialogKDoc] will request the appropriate type (file vs. directory) and enable
multi-selection.

## Showing a FileDialog

To show a dialog use [#showFileDialog][showFileDialogKDoc]
in [BoardGameApplication][BoardGameApplicationKDoc].
The operation blocks user input until the dialog is closed. The function returns an [Optional][OptionalDoc]
containing the chosen list of [Files][FilesDoc]. The Optional is
empty if the dialog is canceled.
