[0.10]: https://github.com/tudo-aqua/bgw/releases/tag/v0.10
[0.9]: https://github.com/tudo-aqua/bgw/releases/tag/v0.9
[0.8.1]: https://github.com/tudo-aqua/bgw/releases/tag/v0.8.1
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

## [0.10] - 04.03.2025

### Breaking Changes

- Migrated backend from _JavaFX_ to _JCEF_ for better performance and compatibility.
- All `properties` are now internal and cannot be accessed directly anymore - use the corresponding attributes and the new _event-based API_ instead.
- `ImageVisual`s should now be instantiated using a `path` and cropped with the respective parameters instead of using `BufferedImage`s.
- The `actualPosX` and `actualPosY` properties in `ComponentView` have been changed to return the absolute position of the component in the scene.
- Dialogs were adjusted to use the new _event-based API_ and are now non-blocking by default.

### Added

**Events:**
- Added `onPathsSelected` event to `FileDialog` to listen for file selection.
- Added `onSelectionCancelled` event to `FileDialog` to listen for file selection cancellation.
- Added `onButtonClicked` event to `Dialog` to listen for button clicks.
- Added `onZoomed` event to `CameraPane` to listen for zoom events.
- Added `onSelected` event to `BinaryStateButton` to listen for selection of the button.
- Added `onDeselected` event to `BinaryStateButton` to listen for deselection of the button.
- Added `onSelectionChanged` event to `BinaryStateButton` to listen for all selection changes of the button.
- Added `onCheckedChanged` event to `CheckBox` to listen for all checked state changes of the checkbox.
- Added `onIndeterminateChanged` event to `CheckBox` to listen for all indeterminate state changes of the checkbox.
- Added `onColorSelected` event to `ColorPicker` to listen for color selection.
- Added `onItemSelected` event to `ComboBox` to listen for item selection.
- Added `onSelectionChanged` event to `ListView` and `TableView` to listen for selection changes.
- Added `onProgressed` event to `ProgressBar` to listen for progress changes.
- Added `onSelected` event to `ToggleGroup` to listen for selection of a `BinaryStateButton` in the group.
- Added `onDeselected` event to `ToggleGroup` to listen for deselection of a `BinaryStateButton` in the group.
- Added `onLockChanged` event to `BoardGameScene` to listen for lock changes.

**Styling:**
- Added `Color` class to represent colors in BGW.
- Added `Flip` style class to support flipping of `Visual` elements.
- Added `BlurFilter`, `SaturationFilter` and `SepiaFilter` style classes to support image filters on `Visual` elements.
- Added `BorderRadius` and `Cursor` style classes to support custom styling of `Visual` elements.
- Added `style`, `filters` and `flipped` properties to `SingleLayerVisual` to apply custom styling.
- Added `rotation` as an initial parameter to `ImageVisual` and `TextVisual` to set the initial rotation of the visual.
- Added `blurRadius` property to `MenuScene` constructor to define the initial blur radius of the menu scene background.

**Misc:**
- Added `Ä`, `Ö`, `Ü` to `KeyCode` enum for German keyboard layout.
- Added `LIME`, `BROWN` and `PURPLE` variants to `ColorVisual`.
- Added `HexOrientation` class to support different orientations of hexagons in `HexagonGrid` and `HexagonView`.
- Added `getCoordinateMap()` function to `HexagonGrid` to get a map of all hexagons with their respective `HexCoordinate`.
- Added `remove(columnIndex, rowIndex)` function to `HexagonGrid` to remove a hexagon at a specific position.
- Added `limitBounds` property to `CameraPane` to limit the camera's movement to the target bounds.
- Added `pan(x, y, zoom, smooth)` function to `CameraPane` to zoom while panning the camera to a specific position.
- Added `panBy(xOffset, yOffset, zoom, smooth)` function to `CameraPane` to zoom while panning the camera by a specific offset.
- Added `getSelectedIndex()` function to `ComboBox` to get the index of the selected item.
- Added `formatItem(item)` function to `ComboBox` to format any item without displaying it.
- Added `isReadonly` property to `TextInputUIComponent` to make input fields read-only.
- Added `formatItem(item)` function to `ListView`, `TableView` and `TableColumn` to format any item without displaying it.
- Added `selectIndex(index)` function to `ListView` and `TableView` to select an item by index.
- Added `loadFont(path, fontName, weight)` function to `BoardGameApplication` to define name and weight of a font loaded from a file.
- Added `dropTarget` to `DragEvent` to get the target of a drag event.
- Added `THIN`, `EXTRA_LIGHT`, `MEDIUM`, `EXTRA_BOLD`, `BLACK` font weights to `FontWeight` enum to support a wider range of fonts.
- Added `inParentPosX` and `inParentPosY` properties to `ComponentView` to get the position of a component in its parent.
- Added `stopAllAnimations()` function to `Scene` to immediately stop all animations currently running.

### Fixed

- Fixed broken `CardStack` alignment.
- Fixed `TextInputUIComponent` prompt not displaying.
- Fixed `FileDialog` extension filters not showing specified file types.
- Fixed font loading not working from packaged builds.
- Fixed `CardView` visual not updating properly.
- Fixed first `Button` in a scene being focused by default.
- Fixed window resizing leading to wrongly scaled game view.
- Fixed `actualPosX` and `actualPosY` properties in `ComponentView` to return the correct absolute position of the component.

### Changed

- Reworked `CameraPane` to make it more intuitive.
- Changed behavior of `LinearLayout` to layout components instead of the child components positioning themselves.

### Removed

- Removed previously added horizontal and vertical pan lock as well as zoom lock for `CameraPane`.
- Removed previously deprecated `getDomain()` and `getCoDomain()` in `BiDirectionalMap` in favor of `keysForward` and `keysBackward`.
- Removed misleading `CardStack.first()` and `CardStack.last()` functions in favor for actual stack operations.
- Removed `Optional` return value from `showDialog()` and `showFileDialog()` functions in `BoardGameApplication`.

### Deprecated

- Deprecated `properties` in favor of the new _event-based API_.
- Deprecated `BufferedImage` constructor in `ImageVisual` in favor of the new `path` constructor.
- Deprecated `ScaleMode` enum for defining the scaling behavior of `Scene`s.
- Deprecated `setScaleMode(newScaleMode)` function in `BoardGameApplication`.
- Deprecated `AspectRatio` for defining the initial aspect ratio of the `BoardGameApplication`.
- Deprecated `onKeyTyped` in favor of `onKeyPressed` and `onKeyReleased` in `Scene` and `ComponentView`.
- Deprecated `selectionStyle`, `componentStyle` and `backgroundStyle` css styles in favor of the new dedicated style classes.
- Deprecated `runOnGUIThread(task)` in `BoardGameApplication` and `runLater(task)` in `Frontend` as they are no longer needed.

## [0.9] - 27.11.2023

### Added
- Simple CSS support for components e.g. `BorderRadius`, `BorderWidth` and `BorderColor`
- Smooth scrolling for `CameraPane`
- Horizontal and vertical pan lock for `CameraPane`
- Added zoom lock for `CameraPane`
- Added `keysForward` and `keysBackward` attributes to `BiDirectionalMap`
- Added `put` and `putAll` functions to `BiDirectionalMap`
- Added conventional map indexing syntax to `BiDirectionalMap`
- Added visuals to `TextInputUIComponent`

### Fixed
- Fixed `HexagonView` not updating visual properly
- Fixed rotation of hexagons in `HexagonGrid` 
- Fixed `CameraPane` panning to work only with specified mouse button
- Fixed `CameraPane` panning only allowed when interactive is set to true
- Fixed Drag and Drop in `CameraPane`
- Fixed `ComboBox` throwing exception when trying to deselect an item
- Fixed `CameraPane` zoom to work properly with `panBy` and `pan function
- Fixed constructor overload for individual alignments in `LinearLayout`

### Deprecated

- Deprecated `getDomain()` and `getCoDomain()` in `BiDirectionalMap` in favor of `keysForward` and `keysBackward`

## [0.8.1] - 21.07.2023

### Fixed
- Library dependency back to Java 11 (accidentally was 17 in 0.8)

### Removed
- faulty config from spring-vaadin-conventions.

## [0.8] - 18.07.2023

### Added
- `setZIndex` ability for components to change there view order in parent components.
- `CameraPane` component.
- `HexagonView` component.
- `HexagonGrid` with two coordinate systems. axial and offset coordinates.
- Additional `onSceneShown` and `onSceneHid` event handlers.
- `MouseEvent` now also gives information about the coordinates.

### Fixed
- `onKeyPressed` listener not working on scenes.
- `showMenuScene` fade animation not firing on consecutive runs.

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

