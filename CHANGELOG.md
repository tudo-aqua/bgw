[0.8]: https://github.com/tudo-aqua/bgw/releases/tag/v0.8
[0.7.3]: https://github.com/tudo-aqua/bgw/releases/tag/v0.7.3
[0.7.2]: https://github.com/tudo-aqua/bgw/releases/tag/v0.7.2
[0.7.1]: https://github.com/tudo-aqua/bgw/releases/tag/v0.7.1
[0.7]: https://github.com/tudo-aqua/bgw/releases/tag/v0.7
[0.6]: https://github.com/tudo-aqua/bgw/releases/tag/v0.6
[0.5]: https://github.com/tudo-aqua/bgw/releases/tag/v0.5
[0.4]: https://github.com/tudo-aqua/bgw/releases/tag/v0.4
[0.3]: https://github.com/tudo-aqua/bgw/releases/tag/v0.3
[0.2]: https://github.com/tudo-aqua/bgw/releases/tag/v0.2
[0.1]: https://github.com/tudo-aqua/bgw/releases/tag/v0.1
<!-- ### Added -->
<!-- ### Changed -->
<!-- ### Fixed -->
<!-- ### Removed -->
<!-- ### Security -->
<!-- ### Deprecated -->

# Changelog
All notable changes to this project will be documented in this file.

## [0.8] - 22.05.2023

### Fixed
- `onKeyPressed` listener not working on scenes.

## [0.7.3] - 31.08.2022

### Added
- Spectator join feature in bgw-net.
- PasswordField.

### Fixed
- Wrong main class in build config of bgw-net protocol client
- Prompt text displayed twice in ComboBox.

## [0.7.2] - 14.08.2022

### Added
 - Debug mode in network client now prints JSON of sent game message.

### Changed
 - Hide "Connections and Sessions" from non-admin users in BGW-net frontend.
 - Unified named arguments in ``KeyEvent``.

### Removed
 - DSL package.

### Fixed
 - Messages passing each other in clients asynchronous receiver function resolution.
 - PlayerLeftNotification not sent upon socket close.
 - Exception thrown in asynchronous coroutine not displayed in default exception handler.
 - Validation of non-JSON files in bgw-net frontend showing incorrect messages.

## [0.7.1] - 05.08.2022

### Added
 - BGW-Net Protocol client.
 - onScroll event.
 - Style property for SingleLayerVisuals.
 - flip() function in CardView.

### Changed
 - BoardGameApplication.runOnGUIThread may now be called without starting an application for headless testing.

### Removed
 - Disabled default undo operation in TextField and TextArea as it causes NPEs in JavaFX.
 - Write access to Animation.isRunning

### Fixed
 - Grid.grow() not updating row heights array.
 - Format function not getting applied to selected item in ComboBox.
 - Drag and Drop rollback on panes.
 - Exception when changing GameScenes in onDragDropped
 - Flip Animation resetting size of ImageVisuals.
 
## [0.7] - 15.07.2022

### Added
 - BGW-Net.
 - Non-blocking Dialog option.

### Changed
 - Keyboard input events for elements on BoardGameScene are now blocked while MenuScene is shown.

## [0.6] - 01.04.2022

### Added
- Fullscreen mode and Fullscreen-Exit-Combination.
- Taskbar Icon.
- Text for ``RadioButtons`` and changed default width.
- Selection model for ``ListView`` and ``TableView``.
- Custom fonts may now be loaded.
- Additional functions for ``GridPane`` to set all column widths or row heights.

### Changed
- Renamed module bgw-core into bgw-gui.
- Background of ``ListView`` and ``TableView`` is now transparent.
- Dragged elements keep their stacked rotation instead of snapping to components rotation.
- Updated OpenFX to Version 17.

### Fixed
- ``MovementAnimation.toComponentView`` now considers rotation and scale of containers.
- Drag & Drop now works from rotated ``GridPanes``.
- Background of ``MenuScene`` no longer changes when switching between menus.
- ``Scene`` background can now be changed.
- Removed blur animation between menu scene changes.
- Position of nested ``GridPanes`` no longer resets on update.
- ``FontFamily`` with whitespace in name no longer has to be escaped.

## [0.5] - 31.10.2021

### Added
- ``ParallelAnimation`` and ``SequentialAnimation``.
- Scene wide key events.
- ``BoardGameApplication.runOnGUIThread`` function to update components from asynchronous environments.
- Sudoku example.
- Tetris example.

### Changed
 - Moved examples to dedicated submodule
 - Disable deselection of last ``RadioButton`` in ``ToggleGroup``.
 - Empty grid columns and rows no longer get rendered size 0.0 in case of fixed dimensions. 

### Fixed
 - Drag and drop target for custom inter-cell-alignments.
 - Offset for grids nested in grid cells.
 - ``MovementAnimation.toComponentView`` missing ``layoutFromCenter`` offset for ``Grid``.
 - ``MovementAnimation.toComponentView`` now working with ``scale``.
 - wrong parent in rollback search for containers after drag and drop.
 - ``BoardGameScene`` getting shown blurred if ``showGameScene`` gets called after ``hideMenuScene``.
 - ``FileDialogs`` returning list of nulls instead of ``Optional.EMPTY``.

## [0.4] - 22.09.2021

### Fixed
- Empty grid columns and rows no longer get rendered size 0.0 in case of fixed dimensions.
- Race condition while changing GameScenes caused by slow renderer.
- FileDialogs returning list of nulls instead of empty optional.
- Snap back from Drag and Drop.
- MovementAnimation#toComponentView offset when animating to GridPane.
- BoardGameScene getting shown blurred if showGameScene gets called after hideMenuScene.

## [0.3] - 09.09.2021

### Added
 - Value ``Scene.components`` for getting a snapshot of the currently contained root components of a ``Scene``.
 - ``onAdd`` and ``onRemove`` function references in ``Pane``.
 - Readonly Properties.
 - Scale Animation.
 - Additional constructors, default parameters and nullable functions for Stack.
 - *div* and *times* operator for Coordinate.

### Changed
- Changed ``removeAll()`` in ``Pane`` to now accept a collection of components to remove.
- Changed various Exception types to more meaningful ones.
- Opacity property is now restricted to \[0.0, 1.0].
- Increased render performance by caching and only updating deltas.

### Removed
- Items property from ListView as it was not observable.

### Fixed
- Items not being updated dynamically in ListView.
- Components in grid not aligned correctly and size calculation not accounting for scale and rotation.
- Font color in TableView.
- Complete Drag and Drop reworking to account for scale, rotation, and nesting.
- ColorVisuals with colors near black caused CSS parse error.


## [0.2] - 10.08.2021
### Added
- Visual as default parameter for all components.
- Introduced alignment feature for ``UIComponents``.
- Introduced alignment feature for ``TextVisual``.
- Text wrapping in ``LabledUIComponents`` and optional parameter ``isWrapText``.
- Additional checks in ``ImageView`` for sub-image parameters to check for bounds.
- New helper functions in ``CoordinatePlain``.
- Constants file with default parameter values.

### Changed
- Changed order of constructor parameters for all components to ``height`` > ``width`` > ``posX`` > ``posY``.
- Added minimum spacing in ``LinearLayout``.
- Scale feature now applies scaling factor instead of altering dimensions directly.
- Renamed ``label`` to ``text`` in ``LabledUIComponents``.
- Renamed ``Table`` to ``TableView``.
- Renamed ``KStack`` to ``Stack``.

### Fixed
- Fixed text color not working on all components.
- Fixed ``ToggleButton`` not being added to specified ``ToggleGroup``.

### Removed
- Infix operators for ``GameComponentViews``.

## [0.1] - 03.08.2021
First release of the BGW framework.

