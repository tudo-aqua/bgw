import data.animation.*
import data.event.*
import data.event.internal.*
import data.event.internal.LoadEventData
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import kotlinx.serialization.json.Json as KJson

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
        // ANIMATIONS
        subclass(FadeAnimationData::class)
        subclass(MovementAnimationData::class)
        subclass(RotationAnimationData::class)
        subclass(ScaleAnimationData::class)
        subclass(FlipAnimationData::class)
        subclass(SequentialAnimationData::class)
        subclass(ParallelAnimationData::class)
        subclass(RandomizeAnimationData::class)
        subclass(DiceAnimationData::class)
        subclass(DelayAnimationData::class)
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
        subclass(MouseEventData::class)
        subclass(KeyEventData::class)
        subclass(LoadEventData::class)
        subclass(SelectionChangedEventData::class)
        subclass(RadioChangedEventData::class)
        subclass(TextInputChangedEventData::class)
        subclass(ColorInputChangedEventData::class)
        subclass(ScrollChangedEventData::class)
        subclass(ZoomChangedEventData::class)
        subclass(InternalCameraPaneData::class)
        subclass(DragGestureStartedEventData::class)
        subclass(DragGestureMovedEventData::class)
        subclass(DragGestureEndedEventData::class)
        subclass(DragGestureEnteredEventData::class)
        subclass(DragGestureExitedEventData::class)
        subclass(DragDroppedEventData::class)
        subclass(CheckBoxChangedEventData::class)
        subclass(AnimationFinishedEventData::class)
        subclass(StructuredDataSelectEventData::class)
    }
    polymorphic(InputEventData::class) {
        subclass(MouseEnteredEventData::class)
        subclass(MouseExitedEventData::class)
        subclass(MouseEventData::class)
        subclass(KeyEventData::class)
    }
    polymorphic(MouseEventData::class) {
        subclass(MouseEnteredEventData::class)
        subclass(MouseExitedEventData::class)
    }
    // ANIMATIONS
    polymorphic(AnimationData::class) {
        subclass(FadeAnimationData::class)
        subclass(MovementAnimationData::class)
        subclass(RotationAnimationData::class)
        subclass(ScaleAnimationData::class)
        subclass(FlipAnimationData::class)
        subclass(DelayAnimationData::class)
    }
}

val jsonMapper = KJson {
    ignoreUnknownKeys = true
    serializersModule = module
}
