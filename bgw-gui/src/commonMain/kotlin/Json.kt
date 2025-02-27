/*
 * Copyright 2025 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused")

import data.animation.*
import data.event.*
import kotlinx.serialization.json.Json as KJson
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

private val module = SerializersModule {
  polymorphic(Data::class) {
    // APPLICATION
    subclass(AppData::class)
    // SCENE
    subclass(SceneData::class)
    // UI COMPONENTS
    subclass(ButtonData::class)
    subclass(LabelData::class)
    subclass(TextFieldData::class)
    subclass(TextAreaData::class)
    subclass(ProgressBarData::class)
    subclass(ColorPickerData::class)
    subclass(ToggleButtonData::class)
    subclass(RadioButtonData::class)
    subclass(CheckBoxData::class)
    subclass(BinaryStateButtonData::class)
    subclass(ComboBoxData::class)
    subclass(PasswordFieldData::class)
    subclass(ListViewData::class)
    subclass(TableViewData::class)
    // LAYOUT VIEWS
    subclass(PaneData::class)
    subclass(GridPaneData::class)
    subclass(CameraPaneData::class)
    // CONTAINER
    subclass(AreaData::class)
    subclass(CardStackData::class)
    subclass(HexagonGridData::class)
    subclass(LinearLayoutData::class)
    subclass(SatchelData::class)
    // GAME COMPONENTS
    subclass(CardViewData::class)
    subclass(DiceViewData::class)
    subclass(HexagonViewData::class)
    subclass(TokenViewData::class)
    // VISUALS
    subclass(ColorVisualData::class)
    subclass(ImageVisualData::class)
    subclass(TextVisualData::class)
    subclass(CompoundVisualData::class)
    // DIALOGS
    subclass(DialogData::class)
    subclass(FileDialogData::class)
    // ANIMATIONS
    subclass(DelayAnimationData::class)
    subclass(DiceAnimationData::class)
    subclass(FadeAnimationData::class)
    subclass(FlipAnimationData::class)
    subclass(MovementAnimationData::class)
    subclass(ParallelAnimationData::class)
    subclass(RandomizeAnimationData::class)
    subclass(RotationAnimationData::class)
    subclass(ScaleAnimationData::class)
    subclass(SequentialAnimationData::class)
  }
  polymorphic(LayoutViewData::class) {
    subclass(PaneData::class)
    subclass(GridPaneData::class)
  }
  polymorphic(ComponentViewData::class) {
    // UI COMPONENTS
    subclass(ButtonData::class)
    subclass(LabelData::class)
    subclass(TextFieldData::class)
    subclass(TextAreaData::class)
    subclass(ProgressBarData::class)
    subclass(ColorPickerData::class)
    subclass(ToggleButtonData::class)
    subclass(RadioButtonData::class)
    subclass(CheckBoxData::class)
    subclass(BinaryStateButtonData::class)
    subclass(ComboBoxData::class)
    subclass(PasswordFieldData::class)
    subclass(ListViewData::class)
    subclass(TableViewData::class)
    // LAYOUT VIEWS
    subclass(PaneData::class)
    subclass(GridPaneData::class)
    subclass(CameraPaneData::class)
    // CONTAINER
    subclass(AreaData::class)
    subclass(CardStackData::class)
    subclass(HexagonGridData::class)
    subclass(LinearLayoutData::class)
    subclass(SatchelData::class)
    // GAME COMPONENTS
    subclass(CardViewData::class)
    subclass(DiceViewData::class)
    subclass(HexagonViewData::class)
    subclass(TokenViewData::class)
  }
  polymorphic(TextInputUIComponentData::class) {
    subclass(TextFieldData::class)
    subclass(TextAreaData::class)
    subclass(PasswordFieldData::class)
  }
  polymorphic(GameComponentViewData::class) {
    subclass(CardViewData::class)
    subclass(DiceViewData::class)
    subclass(HexagonViewData::class)
    subclass(TokenViewData::class)
  }
  polymorphic(VisualData::class) {
    subclass(ColorVisualData::class)
    subclass(ImageVisualData::class)
    subclass(TextVisualData::class)
    subclass(CompoundVisualData::class)
  }
  polymorphic(SingleLayerVisualData::class) {
    subclass(ColorVisualData::class)
    subclass(ImageVisualData::class)
    subclass(TextVisualData::class)
  }
  polymorphic(EventData::class) {
    subclass(MouseEnteredEventData::class)
    subclass(MouseExitedEventData::class)
    subclass(MousePressedEventData::class)
    subclass(MouseReleasedEventData::class)
    subclass(MouseEventData::class)
    subclass(KeyEventData::class)
    subclass(LoadEventData::class)
    subclass(SelectionChangedEventData::class)
    subclass(RadioChangedEventData::class)
    subclass(TextInputChangedEventData::class)
    subclass(ColorInputChangedEventData::class)
    subclass(TransformChangedEventData::class)
    subclass(InternalCameraPanData::class)
    subclass(DragGestureStartedEventData::class)
    subclass(DragGestureMovedEventData::class)
    subclass(DragGestureEndedEventData::class)
    subclass(DragGestureEnteredEventData::class)
    subclass(DragGestureExitedEventData::class)
    subclass(DragDroppedEventData::class)
    subclass(CheckBoxChangedEventData::class)
    subclass(AnimationFinishedEventData::class)
    subclass(StructuredDataSelectEventData::class)
    subclass(ScrollEventData::class)
    subclass(FilesPickedEventData::class)
  }
  polymorphic(InputEventData::class) {
    subclass(MouseEnteredEventData::class)
    subclass(MouseExitedEventData::class)
    subclass(MousePressedEventData::class)
    subclass(MouseReleasedEventData::class)
    subclass(MouseEventData::class)
    subclass(KeyEventData::class)
  }
  polymorphic(MouseEventData::class) {
    subclass(MouseEnteredEventData::class)
    subclass(MouseExitedEventData::class)
    subclass(MousePressedEventData::class)
    subclass(MouseReleasedEventData::class)
  }
  // ANIMATIONS
  polymorphic(AnimationData::class) {
    subclass(DelayAnimationData::class)
    subclass(DiceAnimationData::class)
    subclass(FadeAnimationData::class)
    subclass(FlipAnimationData::class)
    subclass(MovementAnimationData::class)
    subclass(ParallelAnimationData::class)
    subclass(RandomizeAnimationData::class)
    subclass(RotationAnimationData::class)
    subclass(ScaleAnimationData::class)
    subclass(SequentialAnimationData::class)
  }
}

internal val jsonMapper = KJson {
  ignoreUnknownKeys = true
  serializersModule = module
}
