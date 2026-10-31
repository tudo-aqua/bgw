[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[WindowModeKDoc]: /docs/tools.aqua.bgw.core/-window-mode/index.html
[AlignmentKDoc]: /docs/tools.aqua.bgw.core/-alignment/index.html
[KeyEventKDoc]: /docs/tools.aqua.bgw.event/-key-event/index.html
[VisualKDoc]: /docs/tools.aqua.bgw.visual/-visual/index.html
[ImageVisualKDoc]: /docs/tools.aqua.bgw.visual/-image-visual/index.html
[FontKDoc]: /docs/tools.aqua.bgw.util/-font/index.html

<!-- GH-Pages Doc -->

[GettingStarted]: /guides/getting-started
[DeclaringScenes]: /guides/declaring-scenes
[DialogDoc]: /guides/dialogs/dialog
[FileDialogDoc]: /guides/dialogs/file-dialog
[VisualDoc]: /guides/concepts/visual
[AdvancedUsage]: /guides/concepts/advanced-usage

# Application

<tldr>Configure the window of your game and manage the application</tldr>

## Introduction

The [BoardGameApplication][BoardGameApplicationKDoc] is the entry point of every BGW game. It owns the window, decides which scenes are shown and offers application wide functionality like dialogs and fonts. [Getting Started][GettingStarted] showed the minimal setup, this guide covers everything else.

> Only **one** application can exist per execution. Creating a second one throws an `IllegalStateException`. This is why an application is usually declared as an `object`.
> {style="warning"}

---

## Creating the application

The constructor of the application defines the window and the size of the virtual screen.

<signature>
<code-block lang="kotlin" copy="false">
BoardGameApplication(
    windowTitle: String = "BoardGameWork Application",
    width: Number = 1920,
    height: Number = 1080,
    windowMode: WindowMode? = null,
    headless: Boolean = false
)
</code-block>

`width` and `height` define the initial size of the window **and** the size of the virtual screen that scenes are placed on, see [Size and scaling][DeclaringScenes]. If the window is resized later, the virtual screen is scaled with it.

`windowMode` selects how the window starts and overrides `isMaximized` and `isFullScreen` if passed. Use `headless = true` to run the application without a visible window, for example in automated tests.

</signature>

```kotlin
object DiceDuelApplication : BoardGameApplication(
    windowTitle = "Dice Duel",
    width = 1280,
    height = 720,
    windowMode = WindowMode.NORMAL
)
```

> Choose the window size with the same aspect ratio as your scenes. A mismatch results in empty bars around the scene, see [Background](#background).
> {style="note"}

---

## Window

All window related settings are properties of the application. They can be changed at any time, also while the window is open.

<signature>
<code-block lang="kotlin" copy="false">
title: String
icon: ImageVisual?
</code-block>

The text in the title bar and the window icon as [ImageVisual][ImageVisualKDoc]. Both are applied immediately.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
windowWidth: Number
windowHeight: Number
</code-block>

The preferred size of the window. It only has an effect while the window is neither maximized nor in fullscreen mode.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
isMaximized: Boolean
isFullScreen: Boolean
</code-block>

Maximizes the window or switches it to fullscreen mode. The initial state can be chosen with the [WindowMode][WindowModeKDoc] in the constructor:

<table orientation="horizontal">
    <tr>
        <td width="22%"><code>NORMAL</code></td>
        <td>A regular window with the given dimensions.</td>
    </tr>
    <tr>
        <td><code>MAXIMIZED</code></td>
        <td>A maximized window that is not in fullscreen mode.</td>
    </tr>
    <tr>
        <td><code>FULLSCREEN</code></td>
        <td>Fullscreen mode. <code>isMaximized</code> is not affected, which becomes relevant after leaving fullscreen.</td>
    </tr>
</table>

</signature>

<signature>
<code-block lang="kotlin" copy="false">
fullscreenExitCombination: KeyEvent?
fullscreenExitCombinationHint: String?
</code-block>

The key combination that lets the player leave fullscreen mode, and the hint that is displayed when entering it. Setting the combination to `null` removes the possibility to exit with the keyboard, so make sure to offer another way in this case. A hint of `null` shows the default text, an empty string shows no text at all.

```kotlin
fullscreenExitCombination = KeyEvent(KeyCode.ESCAPE, isControlDown = false, isShiftDown = false, isAltDown = false)
fullscreenExitCombinationHint = "Press ESC to leave fullscreen"
```

</signature>

---

## Background

The area around the scenes can get a look of its own.

<signature>
<code-block lang="kotlin" copy="false">
background: Visual
</code-block>

The [Visual][VisualKDoc] that is displayed in the space around the scenes. It is visible whenever the aspect ratio of the window does not match the aspect ratio of the application, and must not be confused with the `background` of a [Scene][DeclaringScenes]. Any [Visual][VisualDoc] works, from a plain color to an image.

</signature>

```kotlin
background = ColorVisual(Color(0x0F141F))
```

---

## Scene alignment

Position all scenes on the virtual screen.

<signature>
<code-block lang="kotlin" copy="false">
setSceneAlignment(newAlignment: Alignment)
setHorizontalSceneAlignment(newHorizontalAlignment: HorizontalAlignment)
setVerticalSceneAlignment(newVerticalAlignment: VerticalAlignment)
</code-block>

Scenes that are smaller than the application are centered by default. These functions move **all** scenes to another position of the virtual screen, using an [Alignment][AlignmentKDoc] like `Alignment.TOP_LEFT`. This also affects menu scenes.

</signature>

---

## Showing scenes and dialogs

The application decides which scenes and dialogs the player sees.

<signature>
<code-block lang="kotlin" copy="false">
showGameScene(scene: BoardGameScene)
showMenuScene(scene: MenuScene, fadeTime: Number = 250)
hideMenuScene(fadeTime: Number = 250)
</code-block>

Control the displayed scenes. See [Declaring Scenes][DeclaringScenes] for details.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
showDialog(dialog: Dialog)
showFileDialog(dialog: FileDialog)
</code-block>

Open dialogs above the application window. See [Dialogs][DialogDoc] and [File Dialogs][FileDialogDoc].

</signature>

---

## Life cycle

Open and close the window and react to it.

<signature>
<code-block lang="kotlin" copy="false">
show()
exit()
</code-block>

`show()` opens the window and **blocks** until it is closed. `exit()` closes the window, which makes `show()` return and lets the program end.

<br>

<code-block lang="kotlin" copy="false">
onWindowShown: (() -&gt; Unit)?
onWindowClosed: (() -&gt; Unit)?
</code-block>

Gets invoked after the window was shown or closed. Use `onWindowClosed` to save the game state or to release resources.

</signature>

```kotlin
object DiceDuelApplication : BoardGameApplication(windowTitle = "Dice Duel") {
    init {
        onWindowShown = { println("Window is open") }
        onWindowClosed = { println("Goodbye!") }
        show()
    }
}
```

<signature>
<code-block lang="kotlin" copy="false">
repaint()
</code-block>

Manually refreshes the currently displayed scenes. BGW updates the screen automatically whenever a property changes, so you rarely need this.

</signature>

---

## Fonts

Use your own fonts in the components of your game.

<signature>
<code-block lang="kotlin" copy="false">
BoardGameApplication.loadFont(path: String, fontName: String, weight: Font.FontWeight): Boolean
BoardGameApplication.loadFont(path: String): Boolean
</code-block>

Registers a font file from the **resources** folder, so that it can be used in a [Font][FontKDoc]. The second variant derives the name from the file name and uses `FontWeight.NORMAL`. The function returns `false` if the file could not be loaded. See [Advanced Usage][AdvancedUsage] for a complete example.

</signature>

---

## Threads

Update the GUI from asynchronous code.

<signature>
<code-block lang="kotlin" copy="false">
BoardGameApplication.runOnGUIThread(task: Runnable)
</code-block>

Executes the given task in the context of the GUI. Use it to update components from asynchronous environments like network callbacks or timers. If no application was started yet, the task is executed on the calling thread.

</signature>

```kotlin
override fun onGameActionReceived(message: GameAction, sender: String) {
    BoardGameApplication.runOnGUIThread {
        scoreLabel.text = "Opponent played"
    }
}
```
