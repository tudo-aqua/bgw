---
title: FileDialog
nav_order: 1
layout: default
---

# FileDialog
{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

In this section we are going to showcase the differend types of file dialogs in the BGW framework.
FileDialog can be used to display a popup to choose a file or directory either to load or save resources.

## Dialog creation

The FileDialog class provides different [FileDialogMode](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-file-dialog/-file-dialog-mode/index.html)s:
* OPEN_FILE
* OPEN_MULTIPLE_FILES
* SAVE_FILE
* CHOOSE_DIRECTORY

Depending on the selection the dialog will request the appropriate type (file vs. directory) and enable multiselction.

## Showing a FileDialog
To show a dialog use [#showFileDialog](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/show-file-dialog.html) in [BoardGameApplication](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/index.html).
The operation blocks user input until the dialog was closed. The function returns an [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) containing the chosen List of [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)s. The optional is empty if the dialog was canceled.
