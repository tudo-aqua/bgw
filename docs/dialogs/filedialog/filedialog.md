---
parent : Dialogs 
title: FileDialog 
nav_order: 2 
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

This section showcases the different types of file dialogs in the BGW framework. FileDialog can be
used to display a popup to choose a file or directory either to load or save resources.

## Dialog creation

The [FileDialog](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-file-dialog/index.html) 
class provides different [FileDialogModes](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-file-dialog-mode/index.html):
* ``OPEN_FILE``
* ``OPEN_MULTIPLE_FILES``
* ``SAVE_FILE``
* ``CHOOSE_DIRECTORY``

Depending on the selection the [Dialog](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.dialog/-dialog/index.html) will request the appropriate type (file vs. directory) and enable 
multi-selection.

## Showing a FileDialog
To show a dialog use [#showFileDialog](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/show-file-dialog.html) 
in [BoardGameApplication](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/index.html).
The operation blocks user input until the dialog is closed. The function returns an [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html) 
containing the chosen list of [Files](https://docs.oracle.com/javase/8/docs/api/java/io/File.html). The Optional is 
empty if the dialog is canceled.
