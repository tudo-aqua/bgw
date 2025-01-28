/*
*   Todo:
*    # Fix alignment for buttons (vertical + horizontal)
*    # Fix horizontal alignment for text visuals (horizontal?)
*    # Fix camerapane overflow: "bgw_camera_content { overflow: hidden; }"
*    - Finish combobox
*    # Change website layout for 1920x1080 screens
*    - Allow uploading images to be used while parsing image visuals
*    # Hide some input from camera pane
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
        
        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#f0f0f0"
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
        this.width.value = 120;
        this.height.value = 45;
        
        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
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
        this.width.value = 120;
        this.height.value = 30;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
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
        this.width.value = 120;
        this.height.value = 30;
    }
}

/* 🟨 Done */   
class LabelData extends LabeledUIComponentData {
    constructor() {
        super();
        this.objectType.push("LabelData");
        this.type = "LabelData";
        this.width.value = 120;
        this.height.value = 30;
        
        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
    }
}

/* 🟨 Done */
class RadioButtonData extends BinaryStateButtonData {
    
    constructor() {
        super();
        this.objectType.push("RadioButtonData");
        this.type = "RadioButtonData";
        this.width.value = 120;
        this.height.value = 45;
        
        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
    }
}

/* 🟨 Done */
class ToggleButtonData extends BinaryStateButtonData {
    
    constructor() {
        super();
        this.objectType.push("ToggleButtonData");
        this.type = "ToggleButtonData";
        this.width.value = 120;
        this.height.value = 45;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
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
        this.width.value = 120;
        this.height.value = 30;
        
        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
    }
}

/* 🟨 Done */
class PasswordFieldData extends TextInputUIComponentData {
    constructor() {
        super();
        this.objectType.push("PasswordFieldData");
        this.type = "PasswordFieldData";
        this.width.value = 140;
        this.height.value = 30;
    }
}

/* 🟨 Done */
class ProgressBarData extends UIComponentData {
    /* ✅ Done */   progress: NumberValue = new NumberValue("progress", 0.0, "Progress");
    /* ✅ Done */   barColor: ColorValue = new ColorValue("barColor", "#00FFFF", "Bar Color");
    
    constructor() {
        super();
        this.objectType.push("ProgressBarData");
        this.type = "ProgressBarData";
        this.width.value = 250;
        this.height.value = 20;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
    }
}

/* 🟨 Done */
class TextAreaData extends TextInputUIComponentData {
    constructor() {
        super();
        this.objectType.push("TextAreaData");
        this.type = "TextAreaData";
        this.width.value = 200;
        this.height.value = 100;
    }
}

/* 🟨 Done */
class TextFieldData extends TextInputUIComponentData {
    constructor() {
        super();
        this.objectType.push("TextFieldData");
        this.type = "TextFieldData";
        this.width.value = 140;
        this.height.value = 30;
    }
}

/* 🟨 Done */
class StructuredDataViewData extends UIComponentData {
    /* ✅ Done */   items = []
    /* ✅ Done */   selectionMode: ChoiceValue = new ChoiceValue("selectionMode", "Single", ["Single", "Multiple", "None"], "Selection Mode");
    /* ✅ Done */   selectionBackground: ColorValue = new ColorValue("selectionBackground", "#0000FF", "Selection Background");
    /* ✅ Done */   selectedItems = []
    
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
    /* ✅ Done */   items = []

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
        this.width.value = 500;
        this.height.value = 200;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
    }
}

/* 🟨 Done */
class TableViewData extends StructuredDataViewData {
    /* ✅ Done */   columns: TableColumnData[] = [];
    
    constructor() {
        super();
        this.objectType.push("TableViewData");
        this.type = "TableViewData";
        this.width.value = 400;
        this.height.value = 500;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
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

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
    }
}

/* 🟨 Done */
class PaneData extends LayoutViewData {
    /* ✅ Done */   components: ComponentViewData[] = [];
    
    constructor() {
        super();
        this.objectType.push("PaneData");
        this.type = "PaneData";

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
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
        this.layoutFromCenter.value = true;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
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
        this.width.value = 130;
        this.height.value = 200;
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
        this.width.value = 80;
        this.height.value = 80;
    }
}

/* 🟨 Done */
class HexagonViewData extends GameComponentViewData {
    /* ✅ Done */   size: NumberValue = new NumberValue("size", 75, "Size");
    /* ✅ Done */   orientation: ChoiceValue = new ChoiceValue("orientation", "Pointy Top", ["Pointy Top", "Flat Top"], "Orientation");
    
    constructor() {
        super();
        this.objectType.push("HexagonViewData");
        this.type = "HexagonViewData";
        this.size.value = 100;
    }
}

/* 🟨 Done */
class TokenViewData extends GameComponentViewData {
    constructor() {
        super();
        this.objectType.push("TokenViewData");
        this.type = "TokenViewData";
        this.width.value = 50;
        this.height.value = 50;
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
        this.width.value = 50;
        this.height.value = 50;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
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
        this.width.value = 130;
        this.height.value = 200;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
    }
}

/* 🟨 Done */
class HexagonGridData extends GameComponentContainerData {
    /* ✅ Done */   coordinateSystem: ChoiceValue = new ChoiceValue("coordinateSystem", "Offset", ["Axial", "Offset"], "Coordinate System");
    /* ✅ Done */   map: Map<string, HexagonViewData> = new Map();
    /* ✅ Done */   spacing: NumberValue = new NumberValue("spacing", 0, "Spacing", true);
    /* ✅ Done */   orientation: ChoiceValue = new ChoiceValue("orientation", "Pointy Top", ["Pointy Top", "Flat Top"], "Orientation");
    
    constructor() {
        super();
        this.objectType.push("HexagonGridData");
        this.type = "HexagonGridData";

        this.spacing.disabled = true;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
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
    /* ✅ Done */   alignment: {  first: string, second: string } = { first: "top", second: "left" };
    /* ✅ Done */   horizontalAlignment: ChoiceValue = new ChoiceValue("horizontalAlignment", "Left", ["Left", "Center", "Right"], "Horizontal Alignment");
    /* ✅ Done */   verticalAlignment: ChoiceValue = new ChoiceValue("verticalAlignment", "Top", ["Top", "Center", "Bottom"], "Vertical Alignment");
    /* ✅ Done */   spacing: NumberValue = new NumberValue("spacing", 0, "Spacing");
    
    constructor() {
        super();
        this.objectType.push("LinearLayoutData");
        this.type = "LinearLayoutData";
        this.width.value = 50;
        this.height.value = 50;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
    }
}

/* 🟨 Done */
class SatchelData extends GameComponentContainerData {    
    constructor() {
        super();
        this.objectType.push("SatchelData");
        this.type = "SatchelData";
        this.isDraggable.disabled = true;
        this.width.value = 50;
        this.height.value = 50;

        this.visual = new CompoundVisualData();
        this.visual.children = [new ColorVisualData()];
        (this.visual.children[0] as ColorVisualData).color = "#ffffff"
        this.visual.children[0].transparency.value = 0;
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
    /* ✅ Done */   color: string = "#b4b4b4"
    
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
    /* ✅ Done */   size: NumberValue = new NumberValue("size", 14, "Size");
    /* ✅ Done */   color: ColorValue = new ColorValue("color", "#000000", "Color");
    /* ✅ Done */   family: StringValue = new StringValue("family", "Arial", "Family");
    /* ✅ Done */   fontWeight: ChoiceValue = new ChoiceValue("fontWeight", "400", ["100", "200", "300", "400", "500", "600", "700", "800", "900"], "Weight");
    /* ✅ Done */   fontStyle: ChoiceValue = new ChoiceValue("fontStyle", "Normal", ["Normal", "Italic", "Oblique"], "Style");
    
    type = "FontData";
}








export const NonPrimitiveDataClasses = {
    "VisualData": VisualData,
    "SingleLayerVisualData": SingleLayerVisualData,
    "ColorVisualData": ColorVisualData,
    "ImageVisualData": ImageVisualData,
    "TextVisualData": TextVisualData,
    "CompoundVisualData": CompoundVisualData,
    "FontData": FontData
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
            "icon": "ads_click",
            "color": "#FFFFFFB3",
            "description": "A button can be clicked to perform an action.",
            "disabled": false
        },
        "CheckBox": {
            "cls": "CheckBoxData",
            "name": "CheckBox",
            "icon": "check_box",
            "color": "#FFFFFFB3",
            "description": "A checkbox can be checked or unchecked.",
            "disabled": false
        },
        "ColorPicker": {
            "cls": "ColorPickerData",
            "name": "ColorPicker",
            "icon": "palette",
            "color": "#FFFFFFB3",
            "description": "A color picker allows the selection of a color.",
            "disabled": false
        },
        "ComboBox": {
            "cls": "ComboBoxData",
            "name": "ComboBox",
            "icon": "top_panel_open",
            "color": "#FFFFFFB3",
            "description": "A combo box allows the selection of an item from a list.",
            "disabled": false
        },
        "Label": {
            "cls": "LabelData",
            "name": "Label",
            "icon": "variables",
            "color": "#FFFFFFB3",
            "description": "A label displays text.",
            "disabled": false
        },
        "PasswordField": {
            "cls": "PasswordFieldData",
            "name": "PasswordField",
            "icon": "password",
            "color": "#FFFFFFB3",
            "description": "A password field displays text input as dots.",
            "disabled": false
        },
        "ProgressBar": {
            "cls": "ProgressBarData",
            "name": "ProgressBar",
            "icon": "sliders",
            "color": "#FFFFFFB3",
            "description": "A progress bar displays a progress percentage.",
            "disabled": false
        },
        "RadioButton": {
            "cls": "RadioButtonData",
            "name": "RadioButton",
            "icon": "radio_button_checked",
            "color": "#FFFFFFB3",
            "description": "A radio button can be selected or deselected.",
            "disabled": false
        },
        "TextArea": {
            "cls": "TextAreaData",
            "name": "TextArea",
            "icon": "view_headline",
            "color": "#FFFFFFB3",
            "description": "A text area displays multiple lines of text input.",
            "disabled": false
        },
        "TextField": {
            "cls": "TextFieldData",
            "name": "TextField",
            "icon": "short_text",
            "color": "#FFFFFFB3",
            "description": "A text field displays a single line of text input.",
            "disabled": false
        },
        "ToggleButton": {
            "cls": "ToggleButtonData",
            "name": "ToggleButton",
            "icon": "toggle_on",
            "color": "#FFFFFFB3",
            "description": "A toggle button can be selected or deselected.",
            "disabled": false
        },
        "ListView": {
            "cls": "ListViewData",
            "name": "ListView",
            "icon": "lists",
            "color": "#FFFFFFB3",
            "description": "A list view displays items in a list.",
            "disabled": false
        },
        "TableView": {
            "cls": "TableViewData",
            "name": "TableView",
            "icon": "table",
            "color": "#FFFFFFB3",
            "description": "A table view displays items in a table.",
            "disabled": false
        }
    },
    "LayoutElements": {
        "CameraPane": {
            "cls": "CameraPaneData",
            "name": "CameraPane",
            "icon": "select_all",
            "color": "#BB6DFF",
            "description": "A camera pane is a view with pan and zoom.",
            "disabled": false
        },
        "GridPane": {
            "cls": "GridPaneData",
            "name": "GridPane",
            "icon": "border_clear",
            "color": "#BB6DFF",
            "description": "A grid pane places components in a grid layout.",
            "disabled": false
        },
        "Pane": {
            "cls": "PaneData",
            "name": "Pane",
            "icon": "select",
            "color": "#BB6DFF",
            "description": "A pane contains other components.",
            "disabled": false
        }
    },
    "GameElements": {
        "Card": {
            "cls": "CardViewData",
            "name": "Card",
            "icon": "playing_cards",
            "color": "#FFFFFFB3",
            "description": "A card can be flipped to reveal different visuals.",
            "disabled": false
        },
        "Dice": {
            "cls": "DiceViewData",
            "name": "Dice",
            "icon": "ifl",
            "color": "#FFFFFFB3",
            "description": "A dice can be rolled to reveal different sides.",
            "disabled": false
        },
        "Hexagon": {
            "cls": "HexagonViewData",
            "name": "Hexagon",
            "icon": "hexagon",
            "color": "#FFFFFFB3",
            "description": "A hexagon can be used as game tiles.",
            "disabled": false
        },
        "Token": {
            "cls": "TokenViewData",
            "name": "Token",
            "icon": "circle",
            "color": "#FFFFFFB3",
            "description": "A token can be used as game pieces.",
            "disabled": false
        }
    },
    "ContainerElements": {
        "Area": {
            "cls": "AreaData",
            "name": "Area",
            "icon": "crop_landscape",
            "color": "#6DBEFF",
            "description": "An area contains various game components.",
            "disabled": false
        },
        "CardStack": {
            "cls": "CardStackData",
            "name": "CardStack",
            "icon": "content_copy",
            "color": "#6DBEFF",
            "description": "A card stack stacks card components.",
            "disabled": false
        },
        "HexagonGrid": {
            "cls": "HexagonGridData",
            "name": "HexagonGrid",
            "icon": "hive",
            "color": "#6DBEFF",
            "description": "A hexagon grid places hexagons in a grid.",
            "disabled": false
        },
        "LinearLayout": {
            "cls": "LinearLayoutData",
            "name": "LinearLayout",
            "icon": "view_column",
            "color": "#6DBEFF",
            "description": "A linear layout places game components in a line.",
            "disabled": false
        },
        "Satchel": {
            "cls": "SatchelData",
            "name": "Satchel",
            "icon": "background_dot_small",
            "color": "#6DBEFF",
            "description": "A satchel hides draggable game components inside.",
            "disabled": false
        }
    },
    "SpecialElements": {
        "GridElement": {
            "cls": "GridElementData",
            "name": "GridElement",
            "icon": "grid_view",
            "color": "#FFC656",
            "description": "A grid element places a component in a grid cell.",
            "disabled": false
        },
        "HexagonGridElement": {
            "cls": "HexagonGridElementData",
            "name": "HexagonGridElement",
            "icon": "deployed_code",
            "color": "#FFC656",
            "description": "A hexagon grid element places a hexagon in a grid cell.",
            "disabled": false
        },
        "TableColumn": {
            "cls": "TableColumnData",
            "name": "TableColumn",
            "icon": "splitscreen_right",
            "color": "#FFC656",
            "description": "A table column defines a column in a table view.",
            "disabled": false
        },
        "ToggleGroup": {
            "cls": "ToggleGroupData",
            "name": "ToggleGroup",
            "icon": "page_info",
            "color": "#FFC656",
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
