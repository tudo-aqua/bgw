/*
*   Todo:
*    # Fix alignment for buttons (vertical + horizontal)
*    # Fix horizontal alignment for text visuals (horizontal?)
*    # Fix camerapane overflow: "bgw_camera_content { overflow: hidden; }"
*    - Finish combobox
*    - Change website layout for 1920x1080 screens
*    - Allow uploading images to be used while parsing image visuals
*    - Hide some input from camera pane
*    - Add parsing to all visual classes
*    - Add parsing to all component classes
*    - Add animation builder page
*    - Add code export
*    - Add scene properties
*    - Only allow certain components for chosen scene type
*/





type ID = string;

// Wrapper Classes for Properties
export type PropertyValue = NumberValue | StringValue | BooleanValue | ColorValue | ChoiceValue;

export class NumberValue {
    property: string;
    value: number;
    name: string;
    propType: string = "number";
    disabled: boolean = false;

    constructor(property: string = "size", value: number = 0, name: string = "Size", disabled: boolean = false) {
        this.property = property;
        this.value = value;
        this.name = name;
        this.disabled = disabled;
    }

    toJSON() {
        return parseFloat(this.value.toString());
    }
}

export class StringValue {
    property: string;
    value: string;
    name: string;
    propType: string = "string";
    disabled: boolean = false;

    constructor(property: string = "", value: string = "", name: string = "", disabled: boolean = false) {
        this.property = property;
        this.value = value;
        this.name = name;
        this.disabled = disabled;
    }

    toJSON() {
        return this.value;
    }
}

export class BooleanValue {
    property: string;
    value: boolean;
    name: string;
    propType: string = "boolean";
    disabled: boolean = false;

    constructor(property: string = "", value: boolean = false, name: string = "", disabled: boolean = false) {
        this.property = property;
        this.value = value;
        this.name = name;
        this.disabled = disabled;
    }

    toJSON() {
        return this.value;
    }
}

export class ColorValue {
    property: string;
    value: string;
    name: string;
    propType: string = "color";
    disabled: boolean = false;

    constructor(property: string = "", value: string = "", name: string = "", disabled: boolean = false) {
        this.property = property;
        this.value = value;
        this.name = name;
        this.disabled = disabled;
    }

    toJSON() {
        return this.value;
    }
}

export class ChoiceValue {
    property: string;
    value: string;
    name: string;
    possibleValues: string[];
    propType: string = "choice";
    disabled: boolean = false;

    constructor(property: string = "", value: string = "",  possible: string[] = [], name: string = "", disabled: boolean = false) {
        this.property = property;
        this.value = value;
        this.possibleValues = possible;
        this.name = name;
        this.disabled = disabled;
    }

    toJSON() {
        return this.value.toLowerCase();
    }
}









function randomHexColor() {
    const alph = "0123456789ABCDEF";
    return "#" + Array.from({length: 6}, () => alph[Math.floor(Math.random() * 16)]).join("");
}

export function generateId() {
    return Math.random().toString(36).substr(2, 12);
}

// Base Class for Data
class Data {
    objectType : string[] = ["Data"];
    type: string = "Data";
}







// Scene Data
/* 🆘 Todo */
class SceneData extends Data {
    /* 🆘 Todo */   width: NumberValue = new NumberValue("width", 0, "Width");
    /* 🆘 Todo */   height: NumberValue = new NumberValue("height", 0, "Height");
    /* 🆘 Todo */   background: VisualData | null = null;
    /* ✅ Done */   components: ComponentViewData[] = [];

    constructor() {
        super();
        this.objectType.push("SceneData");
        this.type = "SceneData";
    }
}






export class PropData extends Data {
    /* ✅ Done */   id: ID = generateId();
    /* ✅ Done */   name: StringValue = new StringValue("name", "", "Name");
}

/* 🆘 Todo */
export class ComponentViewData extends PropData {
    /* ✅ Done */   posX: NumberValue = new NumberValue("posX", 0, "Position X");
    /* ✅ Done */   posY: NumberValue = new NumberValue("posY", 0, "Position Y");
    /* ✅ Done */   width: NumberValue = new NumberValue("width", 150, "Width");
    /* ✅ Done */   height: NumberValue = new NumberValue("height", 150, "Height");
    /* ✅ Done */   visual: CompoundVisualData = new CompoundVisualData();
    /* 🆔 BGW  */   zIndex: NumberValue = new NumberValue("zIndex", 0, "Z Index");
    /* ✅ Done */   opacity: NumberValue = new NumberValue("opacity", 1.0, "Opacity");
    /* ✅ Done */   isVisible: BooleanValue = new BooleanValue("isVisible", true, "Visible");
    /* ✅ Done */   isDisabled: BooleanValue = new BooleanValue("isDisabled", false, "Disabled");
    /* 🆔 BGW  */   isFocusable: BooleanValue = new BooleanValue("isFocusable", true, "Focusable");
    /* ✅ Done */   scaleX: NumberValue = new NumberValue("scaleX", 1.0, "Scale X");
    /* ✅ Done */   scaleY: NumberValue = new NumberValue("scaleY", 1.0, "Scale Y");
    /* ✅ Done */   rotation: NumberValue = new NumberValue("rotation", 0.0, "Rotation");
    /* ✅ Done */   layoutFromCenter: BooleanValue = new BooleanValue("isLayoutFromCenter", false, "Layout from Center");
    /* ✅ Done */   isDraggable: BooleanValue = new BooleanValue("isDraggable", false, "Draggable");

    constructor() {
        super();
        this.objectType.push("ComponentViewData");
        this.type = "ComponentViewData";
        this.layoutFromCenter.disabled = true;
    }
}





// UI Components
/* 🟨 Done */
class UIComponentData extends ComponentViewData {
    /* ✅ Done */   font: FontData = new FontData();

    constructor() {
        super();
        this.objectType.push("UIComponentData");
        this.type = "UIComponentData";
        this.isDraggable.disabled = true;
    }
}

/* 🟨 Done */
export class LabeledUIComponentData extends UIComponentData {
    /* ✅ Done */   text: StringValue = new StringValue("text", "", "Text");
    /* ✅ Done */   alignment: {  first: string, second: string } = { first: "center", second: "center" };
    /* ✅ Done */   horizontalAlignment: ChoiceValue = new ChoiceValue("horizontalAlignment", "Center", ["Left", "Center", "Right"], "Horizontal Alignment");
    /* ✅ Done */   verticalAlignment: ChoiceValue = new ChoiceValue("verticalAlignment", "Center", ["Top", "Center", "Bottom"], "Vertical Alignment");
    /* ✅ Done */   isWrapText: BooleanValue = new BooleanValue("isWrapText", false, "Wrap Text");

    constructor() {
        super();
        this.objectType.push("LabeledUIComponentData");
        this.type = "LabeledUIComponentData";
    }
}

/* 🟨 Done */
class TextInputUIComponentData extends UIComponentData {
    /* ✅ Done */   text: StringValue = new StringValue("text", "", "Text");
    /* ✅ Done */   prompt: StringValue = new StringValue("prompt", "", "Prompt");

    constructor() {
        super();
        this.objectType.push("TextInputUIComponentData");
        this.type = "TextInputUIComponentData";
    }
}

/* 🟨 Done */
class BinaryStateButtonData extends LabeledUIComponentData {
    /* ✅ Done */   isSelected: BooleanValue = new BooleanValue("isSelected", false, "Selected");
    /* ✅ Done */   group: StringValue = new StringValue("group", generateId(), "Group");

    constructor() {
        super();
        this.objectType.push("BinaryStateButtonData");
        this.type = "BinaryStateButtonData";
    }
}

/* 🟨 Done */
class ButtonData extends LabeledUIComponentData {
    constructor() {
        super();
        this.objectType.push("ButtonData");
        this.type = "ButtonData";
    }
}

/* 🟨 Done */
class CheckBoxData extends LabeledUIComponentData {
    /* ✅ Done */   isChecked: BooleanValue = new BooleanValue("isChecked", false, "Checked");
    /* ✅ Done */   allowIndeterminate: BooleanValue = new BooleanValue("allowIndeterminate", false, "Allow Indeterminate");
    /* ✅ Done */   isIndeterminate: BooleanValue = new BooleanValue("isIndeterminate", false, "Indeterminate");

    constructor() {
        super();
        this.objectType.push("CheckBoxData");
        this.type = "CheckBoxData";
    }
}

/* 🟨 Done */
// Can only display strings but use all classes
// -> Only allow adding strings to items array
class ComboBoxData extends UIComponentData {
    /* ✅ Done */   prompt: StringValue = new StringValue("prompt", "", "Prompt");
    /* ✅ Done */   items: [number, string][] = [];
    /* ✅ Done */   selectedItem: [number, string] | null = null;

    constructor() {
        super();
        this.objectType.push("ComboBoxData");
        this.type = "ComboBoxData";
    }
}

/* 🟨 Done */
class LabelData extends LabeledUIComponentData {
    constructor() {
        super();
        this.objectType.push("LabelData");
        this.type = "LabelData";
    }
}

/* 🟨 Done */
class RadioButtonData extends BinaryStateButtonData {

    constructor() {
        super();
        this.objectType.push("RadioButtonData");
        this.type = "RadioButtonData";
    }
}

/* 🟨 Done */
class ToggleButtonData extends BinaryStateButtonData {

    constructor() {
        super();
        this.objectType.push("ToggleButtonData");
        this.type = "ToggleButtonData";
    }
}

/* 🟨 Done */
class ToggleGroupData extends PropData {
    /* ✅ Done */   group: StringValue = new StringValue("group", generateId(), "Group");

    constructor() {
        super();
        this.objectType.push("ToggleGroupData");
        this.type = "ToggleGroupData";
    }
}

/* 🟨 Done */
class ColorPickerData extends UIComponentData {
    /* ✅ Done */   selectedColor: ColorValue = new ColorValue("initialColor", "#FFFFFF", "Initial Color");

    constructor() {
        super();
        this.objectType.push("ColorPickerData");
        this.type = "ColorPickerData";
    }
}

/* 🟨 Done */
class PasswordFieldData extends TextInputUIComponentData {
    constructor() {
        super();
        this.objectType.push("PasswordFieldData");
        this.type = "PasswordFieldData";
    }
}

/* 🟨 Done */
class ProgressBarData extends UIComponentData {
    /* ✅ Done */   progress: NumberValue = new NumberValue("progress", 0.0, "Progress");
    /* ✅ Done */   barColor: ColorValue = new ColorValue("barColor", "#FF00FF", "Bar Color");

    constructor() {
        super();
        this.objectType.push("ProgressBarData");
        this.type = "ProgressBarData";
    }
}

/* 🟨 Done */
class TextAreaData extends TextInputUIComponentData {
    constructor() {
        super();
        this.objectType.push("TextAreaData");
        this.type = "TextAreaData";
    }
}

/* 🟨 Done */
class TextFieldData extends TextInputUIComponentData {
    constructor() {
        super();
        this.objectType.push("TextFieldData");
        this.type = "TextFieldData";
    }
}

/* 🟨 Done */
class StructuredDataViewData extends UIComponentData {
    /* ✅ Done */   items = ["Value 1", "Selected", "Value 3"]
    /* ✅ Done */   selectionMode: ChoiceValue = new ChoiceValue("selectionMode", "Single", ["Single", "Multiple", "None"], "Selection Mode");
    /* ✅ Done */   selectionBackground: ColorValue = new ColorValue("selectionBackground", "#0090FF", "Selection Background");
    /* ✅ Done */   selectedItems = [1]

    constructor() {
        super();
        this.objectType.push("StructuredDataViewData");
        this.type = "StructuredDataViewData";
    }
}

/* 🟨 Done */
class TableColumnData extends PropData {
    /* ✅ Done */   title: StringValue = new StringValue("title", "", "Title");
    /* ✅ Done */   width: NumberValue = new NumberValue("width", 150, "Width");
    /* ✅ Done */   font: FontData = new FontData();
    /* ✅ Done */   items = ["Value 1", "Selected", "Value 3"]

    constructor() {
        super();
        this.objectType.push("TableColumnData");
        this.type = "TableColumnData";
    }
}

/* 🟨 Done */
class ListViewData extends StructuredDataViewData {
    /* ✅ Done */   orientation: ChoiceValue = new ChoiceValue("orientation", "Vertical", ["Vertical", "Horizontal"], "Orientation");

    constructor() {
        super();
        this.objectType.push("ListViewData");
        this.type = "ListViewData";
    }
}

/* 🟨 Done */
class TableViewData extends StructuredDataViewData {
    /* ✅ Done */   columns: TableColumnData[] = [];

    constructor() {
        super();
        this.objectType.push("TableViewData");
        this.type = "TableViewData";
    }
}



// Layout Views
/* 🟨 Done */
class LayoutViewData extends ComponentViewData {
    constructor() {
        super();
        this.objectType.push("LayoutViewData");
        this.type = "LayoutViewData";
        this.isDraggable.disabled = true;
    }
}

/* 🟨 Done */
class CameraPaneData extends ComponentViewData {
    /* ✅ Done */   target : LayoutViewData = null;
    /* ✅ Done */   zoom : NumberValue = new NumberValue("zoom", 1.0, "Zoom");
    /* ✅ Done */   interactive : BooleanValue = new BooleanValue("interactive", true, "Interactive");
    /* ✅ Done */   scroll : {xCoord : number,  yCoord : number} = {xCoord : 0.0, yCoord : 0.0};

    constructor() {
        super();
        this.objectType.push("CameraPaneData");
        this.type = "CameraPaneData";
        this.zoom.disabled = true;
    }
}

/* 🟨 Done */
class PaneData extends LayoutViewData {
    /* ✅ Done */   components: ComponentViewData[] = [];

    constructor() {
        super();
        this.objectType.push("PaneData");
        this.type = "PaneData";
    }
}

/* 🟨 Done */
class GridPaneData extends LayoutViewData {
    /* ✅ Done */   columns: NumberValue = new NumberValue("columns", 0, "Columns");
    /* ✅ Done */   rows: NumberValue = new NumberValue("rows", 0, "Rows");
    /* ✅ Done */   spacing: NumberValue = new NumberValue("spacing", 0, "Spacing");
    /* ✅ Done */   grid: GridElementData[] = [];

    constructor() {
        super();
        this.objectType.push("GridPaneData");
        this.type = "GridPaneData";
        this.layoutFromCenter.disabled = false;
    }
}

/* 🟨 Done */
class GridElementData extends PropData {
    /* ✅ Done */   row: NumberValue = new NumberValue("row", 0, "Row");
    /* ✅ Done */   column: NumberValue = new NumberValue("column", 0, "Column");
    /* ✅ Done */   component: ComponentViewData | null = null;
    /* ✅ Done */   alignment: {  first: string, second: string } = { first: "center", second: "center" };
    /* ✅ Done */   horizontalAlignment: ChoiceValue = new ChoiceValue("horizontalAlignment", "Center", ["Left", "Center", "Right"], "Horizontal Alignment");
    /* ✅ Done */   verticalAlignment: ChoiceValue = new ChoiceValue("verticalAlignment", "Center", ["Top", "Center", "Bottom"], "Vertical Alignment");

    constructor() {
        super();
        this.objectType.push("GridElementData");
        this.type = "GridElementData";
    }
}





// Game Component Views
/* 🟨 Done */
class GameComponentViewData extends ComponentViewData {
    constructor() {
        super();
        this.objectType.push("GameComponentViewData");
        this.type = "GameComponentViewData";
    }
}

/* 🟨 Done */
class CardViewData extends GameComponentViewData {
    /* ✅ Done */   front: CompoundVisualData = new CompoundVisualData();
    /* ✅ Done */   back: CompoundVisualData = new CompoundVisualData();
    /* ✅ Done */   currentVisual: CompoundVisualData = this.front;
    /* ✅ Done */   current : ChoiceValue = new ChoiceValue("current", "Front", ["Front", "Back"], "Current Visual");

    constructor() {
        super();
        this.objectType.push("CardViewData");
        this.type = "CardViewData";
    }
}

/* 🟨 Done */
export class DiceViewData extends GameComponentViewData {
    /* ✅ Done */   sideCount: NumberValue = new NumberValue("sideCount", 1, "Side Count");
    /* ✅ Done */   currentSide: ChoiceValue = new ChoiceValue("currentSide", "1", ["1"], "Current Side");
    /* ✅ Done */   visuals: VisualData[] = [];
    vis_0: CompoundVisualData = new CompoundVisualData();

    constructor() {
        super();
        this.objectType.push("DiceViewData");
        this.type = "DiceViewData";
    }
}

/* 🟨 Done */
class HexagonViewData extends GameComponentViewData {
    /* ✅ Done */   size: NumberValue = new NumberValue("size", 75, "Size");

    constructor() {
        super();
        this.objectType.push("HexagonViewData");
        this.type = "HexagonViewData";
    }
}

/* 🟨 Done */
class TokenViewData extends GameComponentViewData {
    constructor() {
        super();
        this.objectType.push("TokenViewData");
        this.type = "TokenViewData";
    }
}





// Container
/* 🟨 Done */
class GameComponentContainerData extends ComponentViewData {
    /* ✅ Done */   components: GameComponentViewData[] = [];

    constructor() {
        super();
        this.objectType.push("GameComponentContainerData");
        this.type = "GameComponentContainerData";
    }
}

/* 🟨 Done */
class AreaData extends GameComponentContainerData {

    constructor() {
        super();
        this.objectType.push("AreaData");
        this.type = "AreaData";
    }
}

/* 🟨 Done */
class CardStackData extends GameComponentContainerData {
    /* ✅ Done */   alignment: {  first: string, second: string } = { first: "center", second: "center" };
    /* ✅ Done */   horizontalAlignment: ChoiceValue = new ChoiceValue("horizontalAlignment", "Center", ["Left", "Center", "Right"], "Horizontal Alignment");
    /* ✅ Done */   verticalAlignment: ChoiceValue = new ChoiceValue("verticalAlignment", "Center", ["Top", "Center", "Bottom"], "Vertical Alignment");

    constructor() {
        super();
        this.objectType.push("CardStackData");
        this.type = "CardStackData";
    }
}

/* 🟨 Done */
class HexagonGridData extends GameComponentContainerData {
    /* ✅ Done */   coordinateSystem: ChoiceValue = new ChoiceValue("coordinateSystem", "Axial", ["Axial", "Offset"], "Coordinate System");
    /* ✅ Done */   map: Map<string, HexagonViewData> = new Map();
    /* ✅ Done */   spacing: NumberValue = new NumberValue("spacing", 0, "Spacing");

    constructor() {
        super();
        this.objectType.push("HexagonGridData");
        this.type = "HexagonGridData";
    }
}

/* 🟨 Done */
class HexagonGridElementData extends PropData {
    /* ✅ Done */   row: NumberValue = new NumberValue("row", 0, "Row");
    /* ✅ Done */   column: NumberValue = new NumberValue("column", 0, "Column");
    /* ✅ Done */   hexagon: HexagonViewData | null = null;

    constructor() {
        super();
        this.objectType.push("HexagonGridElementData");
        this.type = "HexagonGridElementData";
    }
}

/* 🟨 Done */
class LinearLayoutData extends GameComponentContainerData {
    /* ✅ Done */   orientation: ChoiceValue = new ChoiceValue("orientation", "Horizontal", ["Horizontal", "Vertical"], "Orientation");
    /* ✅ Done */   alignment: {  first: string, second: string } = { first: "center", second: "center" };
    /* ✅ Done */   horizontalAlignment: ChoiceValue = new ChoiceValue("horizontalAlignment", "Center", ["Left", "Center", "Right"], "Horizontal Alignment");
    /* ✅ Done */   verticalAlignment: ChoiceValue = new ChoiceValue("verticalAlignment", "Center", ["Top", "Center", "Bottom"], "Vertical Alignment");
    /* ✅ Done */   spacing: NumberValue = new NumberValue("spacing", 0, "Spacing");

    constructor() {
        super();
        this.objectType.push("LinearLayoutData");
        this.type = "LinearLayoutData";
    }
}

/* 🟨 Done */
class SatchelData extends GameComponentContainerData {
    constructor() {
        super();
        this.objectType.push("SatchelData");
        this.type = "SatchelData";
        this.isDraggable.disabled = true;
    }
}





// Visuals
/* 🆘 Todo */
class VisualData extends Data {
    /* ✅ Done */   id: ID = generateId();

    constructor() {
        super();
        this.objectType.push("VisualData");
        this.type = "VisualData";
    }
}

/* 🆘 Todo */
export class SingleLayerVisualData extends VisualData {
    /* ✅ Done */   transparency: NumberValue = new NumberValue("transparency", 1.0, "Transparency");
    /* 🆘 Todo */   style: Map<string, string> = new Map();
    /* 🆘 Todo */   filters: Map<string, string | null> = new Map();
    /* ✅ Done */   flipped: ChoiceValue = new ChoiceValue("flipped", "None", ["None", "Horizontal", "Vertical", "Both"], "Flipped");

    constructor() {
        super();
        this.objectType.push("SingleLayerVisualData");
        this.type = "SingleLayerVisualData";
    }
}

/* 🟨 Done */
export class ColorVisualData extends SingleLayerVisualData {
    /* ✅ Done */   color: string = randomHexColor();

    constructor() {
        super();
        this.objectType.push("ColorVisualData");
        this.type = "ColorVisualData";
    }
}

/* 🟨 Done */
export class ImageVisualData extends SingleLayerVisualData {
    /* ✅ Done */   path: string = ""
    /* ✅ Done */   width: NumberValue = new NumberValue("width", -1, "Width");
    /* ✅ Done */   height: NumberValue = new NumberValue("height", -1, "Height");
    /* ✅ Done */   offsetX: NumberValue = new NumberValue("offsetX", 0, "Offset X");
    /* ✅ Done */   offsetY: NumberValue = new NumberValue("offsetY", 0, "Offset Y");

    constructor() {
        super();
        this.objectType.push("ImageVisualData");
        this.type = "ImageVisualData";
    }
}

/* 🟨 Done */
export class TextVisualData extends SingleLayerVisualData {
    /* ✅ Done */   text: string = "Text"
    /* ✅ Done */   font: FontData = new FontData();
    /* ✅ Done */   alignment: {  first: string, second: string } = { first: "center", second: "center" };
    /* ✅ Done */   horizontalAlignment: ChoiceValue = new ChoiceValue("horizontalAlignment", "Center", ["Left", "Center", "Right"], "Horizontal Alignment");
    /* ✅ Done */   verticalAlignment: ChoiceValue = new ChoiceValue("verticalAlignment", "Center", ["Top", "Center", "Bottom"], "Vertical Alignment");
    /* ✅ Done */   offsetX: NumberValue = new NumberValue("offsetX", 0, "Offset X");
    /* ✅ Done */   offsetY: NumberValue = new NumberValue("offsetY", 0, "Offset Y");

    constructor() {
        super();
        this.objectType.push("TextVisualData");
        this.type = "TextVisualData";
    }
}

/* 🟨 Done */
export class CompoundVisualData extends VisualData {
    /* ✅ Done */   children: SingleLayerVisualData[] = [
        new ColorVisualData()
    ];

    constructor() {
        super();
        this.objectType.push("CompoundVisualData");
        this.type = "CompoundVisualData";
    }
}





// Font
/* 🟨 Done */
export class FontData {
    /* ✅ Done */   size: NumberValue = new NumberValue("size", 16, "Size");
    /* ✅ Done */   color: ColorValue = new ColorValue("color", "#000000", "Color");
    /* ✅ Done */   family: StringValue = new StringValue("family", "Arial", "Family");
    /* ✅ Done */   fontWeight: ChoiceValue = new ChoiceValue("fontWeight", "400", ["100", "200", "300", "400", "500", "600", "700", "800", "900"], "Weight");
    /* ✅ Done */   fontStyle: ChoiceValue = new ChoiceValue("fontStyle", "Normal", ["Normal", "Italic", "Oblique"], "Style");
}










export const DataClasses = {
    "ButtonData": ButtonData,
    "CheckBoxData": CheckBoxData,
    "ColorPickerData": ColorPickerData,
    "ComboBoxData": ComboBoxData,
    "LabelData": LabelData,
    "PasswordFieldData": PasswordFieldData,
    "ProgressBarData": ProgressBarData,
    "RadioButtonData": RadioButtonData,
    "ToggleGroupData": ToggleGroupData,
    "TextAreaData": TextAreaData,
    "TextFieldData": TextFieldData,
    "ToggleButtonData": ToggleButtonData,
    "ListViewData": ListViewData,
    "TableViewData": TableViewData,
    "CameraPaneData": CameraPaneData,
    "PaneData": PaneData,
    "GridPaneData": GridPaneData,
    "CardViewData": CardViewData,
    "DiceViewData": DiceViewData,
    "HexagonViewData": HexagonViewData,
    "TokenViewData": TokenViewData,
    "AreaData": AreaData,
    "CardStackData": CardStackData,
    "HexagonGridData": HexagonGridData,
    "LinearLayoutData": LinearLayoutData,
    "SatchelData": SatchelData,
    "GridElementData": GridElementData,
    "HexagonGridElementData": HexagonGridElementData,
    "TableColumnData": TableColumnData
}

export type DataClass = ButtonData | CheckBoxData | ColorPickerData | ComboBoxData | LabelData | PasswordFieldData | ProgressBarData | RadioButtonData | ToggleGroupData | TextAreaData | TextFieldData | ToggleButtonData | ListViewData | TableViewData | CameraPaneData | PaneData | GridPaneData | CardViewData | DiceViewData | HexagonViewData | TokenViewData | AreaData | CardStackData | HexagonGridData | LinearLayoutData | SatchelData | GridElementData | HexagonGridElementData | TableColumnData;

export const Instantiable = {
    "UIElements": {
        "Button": {
            "cls": "ButtonData",
            "name": "Button",
            "icon": "highlight_mouse_cursor",
            "description": "A button can be clicked to perform an action.",
            "disabled": false
        },
        "CheckBox": {
            "cls": "CheckBoxData",
            "name": "CheckBox",
            "icon": "check_box",
            "description": "A checkbox can be checked or unchecked.",
            "disabled": false
        },
        "ColorPicker": {
            "cls": "ColorPickerData",
            "name": "ColorPicker",
            "icon": "palette",
            "description": "A color picker allows the selection of a color.",
            "disabled": false
        },
        "ComboBox": {
            "cls": "ComboBoxData",
            "name": "ComboBox",
            "icon": "top_panel_open",
            "description": "A combo box allows the selection of an item from a list.",
            "disabled": false
        },
        "Label": {
            "cls": "LabelData",
            "name": "Label",
            "icon": "chips",
            "description": "A label displays text.",
            "disabled": false
        },
        "PasswordField": {
            "cls": "PasswordFieldData",
            "name": "PasswordField",
            "icon": "password",
            "description": "A password field displays text input as dots.",
            "disabled": false
        },
        "ProgressBar": {
            "cls": "ProgressBarData",
            "name": "ProgressBar",
            "icon": "sliders",
            "description": "A progress bar displays a progress percentage.",
            "disabled": false
        },
        "RadioButton": {
            "cls": "RadioButtonData",
            "name": "RadioButton",
            "icon": "radio_button_checked",
            "description": "A radio button can be selected or deselected.",
            "disabled": false
        },
        "TextArea": {
            "cls": "TextAreaData",
            "name": "TextArea",
            "icon": "view_headline",
            "description": "A text area displays multiple lines of text input.",
            "disabled": false
        },
        "TextField": {
            "cls": "TextFieldData",
            "name": "TextField",
            "icon": "short_text",
            "description": "A text field displays a single line of text input.",
            "disabled": false
        },
        "ToggleButton": {
            "cls": "ToggleButtonData",
            "name": "ToggleButton",
            "icon": "toggle_on",
            "description": "A toggle button can be selected or deselected.",
            "disabled": false
        },
        "ListView": {
            "cls": "ListViewData",
            "name": "ListView",
            "icon": "lists",
            "description": "A list view displays items in a list.",
            "disabled": false
        },
        "TableView": {
            "cls": "TableViewData",
            "name": "TableView",
            "icon": "table",
            "description": "A table view displays items in a table.",
            "disabled": false
        }
    },
    "LayoutElements": {
        "CameraPane": {
            "cls": "CameraPaneData",
            "name": "CameraPane",
            "icon": "select_all",
            "description": "A camera pane is a view with pan and zoom.",
            "disabled": false
        },
        "GridPane": {
            "cls": "GridPaneData",
            "name": "GridPane",
            "icon": "border_clear",
            "description": "A grid pane places components in a grid layout.",
            "disabled": false
        },
        "Pane": {
            "cls": "PaneData",
            "name": "Pane",
            "icon": "select",
            "description": "A pane contains other components.",
            "disabled": false
        }
    },
    "GameElements": {
        "Card": {
            "cls": "CardViewData",
            "name": "Card",
            "icon": "playing_cards",
            "description": "A card can be flipped to reveal different visuals.",
            "disabled": false
        },
        "Dice": {
            "cls": "DiceViewData",
            "name": "Dice",
            "icon": "ifl",
            "description": "A dice can be rolled to reveal different sides.",
            "disabled": false
        },
        "Hexagon": {
            "cls": "HexagonViewData",
            "name": "Hexagon",
            "icon": "hexagon",
            "description": "A hexagon can be used as game tiles.",
            "disabled": false
        },
        "Token": {
            "cls": "TokenViewData",
            "name": "Token",
            "icon": "circle",
            "description": "A token can be used as game pieces.",
            "disabled": false
        }
    },
    "ContainerElements": {
        "Area": {
            "cls": "AreaData",
            "name": "Area",
            "icon": "crop_landscape",
            "description": "An area contains various game components.",
            "disabled": false
        },
        "CardStack": {
            "cls": "CardStackData",
            "name": "CardStack",
            "icon": "content_copy",
            "description": "A card stack stacks card components.",
            "disabled": false
        },
        "HexagonGrid": {
            "cls": "HexagonGridData",
            "name": "HexagonGrid",
            "icon": "hive",
            "description": "A hexagon grid places hexagons in a grid.",
            "disabled": false
        },
        "LinearLayout": {
            "cls": "LinearLayoutData",
            "name": "LinearLayout",
            "icon": "view_column",
            "description": "A linear layout places game components in a line.",
            "disabled": false
        },
        "Satchel": {
            "cls": "SatchelData",
            "name": "Satchel",
            "icon": "background_dot_small",
            "description": "A satchel hides draggable game components inside.",
            "disabled": false
        }
    },
    "SpecialElements": {
        "GridElement": {
            "cls": "GridElementData",
            "name": "GridElement",
            "icon": "grid_view",
            "description": "A grid element places a component in a grid cell.",
            "disabled": false
        },
        "HexagonGridElement": {
            "cls": "HexagonGridElementData",
            "name": "HexagonGridElement",
            "icon": "deployed_code",
            "description": "A hexagon grid element places a hexagon in a grid cell.",
            "disabled": false
        },
        "TableColumn": {
            "cls": "TableColumnData",
            "name": "TableColumn",
            "icon": "splitscreen_right",
            "description": "A table column defines a column in a table view.",
            "disabled": false
        },
        "ToggleGroup": {
            "cls": "ToggleGroupData",
            "name": "ToggleGroup",
            "icon": "page_info",
            "description": "A toggle group groups binary state buttons.",
            "disabled": false
        }
    }
}


export const Droppable = {
    "CameraPaneData": {
        whitelist: ["LayoutViewData"],
    },
    "GridPaneData": {
        whitelist: ["GridElementData"],
    },
    "PaneData": {
        whitelist: ["ComponentViewData"],
    },
    "AreaData": {
        whitelist: ["GameComponentViewData", "GameComponentContainerData"],
    },
    "CardStackData": {
        whitelist: ["CardViewData"],
    },
    "HexagonGridData": {
        whitelist: ["HexagonGridElementData"],
    },
    "LinearLayoutData": {
        whitelist: ["GameComponentViewData"],
    },
    "SatchelData": {
        whitelist: ["GameComponentViewData"],
    },
    "GridElementData": {
        whitelist: ["ComponentViewData"],
    },
    "HexagonGridElementData": {
        whitelist: ["HexagonViewData"],
    },
    "ToggleGroupData": {
        whitelist: ["BinaryStateButtonData"],
    },
    "TableViewData": {
        whitelist: ["TableColumnData"],
    }
}