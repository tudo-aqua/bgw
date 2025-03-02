export const constructors = {
  Button: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
    {
      property: "alignment",
      type: "Alignment",
      primitive: null,
      maps: "alignment",
    },
    {
      property: "isWrapText",
      type: "boolean",
      primitive: true,
      maps: "isWrapText",
    },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  CheckBox: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
    {
      property: "alignment",
      type: "Alignment",
      primitive: null,
      maps: "alignment",
    },
    {
      property: "isWrapText",
      type: "boolean",
      primitive: true,
      maps: "isWrapText",
    },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    {
      property: "isChecked",
      type: "boolean",
      primitive: true,
      maps: "isChecked",
    },
    {
      property: "allowIndeterminate",
      type: "boolean",
      primitive: true,
      maps: "allowIndeterminate",
    },
    {
      property: "isIndeterminate",
      type: "boolean",
      primitive: true,
      maps: "isIndeterminate",
    },
  ],
  ColorPicker: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    {
      property: "initialColor",
      type: "string",
      primitive: true,
      maps: "selectedColor",
    },
  ],
  ComboBox: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
    { property: "prompt", type: "string", primitive: true, maps: "prompt" },
    { property: "items", type: "StringArray", primitive: false, maps: null },
    {
      property: "formatFunction",
      type: "Function",
      primitive: false,
      maps: null,
    },
  ],
  Label: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
    {
      property: "alignment",
      type: "Alignment",
      primitive: null,
      maps: "alignment",
    },
    {
      property: "isWrapText",
      type: "boolean",
      primitive: true,
      maps: "isWrapText",
    },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  PasswordField: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "prompt", type: "string", primitive: true, maps: "prompt" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
  ],
  ProgressBar: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "progress", type: "number", primitive: true, maps: "progress" },
    { property: "barColor", type: "string", primitive: true, maps: "barColor" },
  ],
  RadioButton: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
    {
      property: "alignment",
      type: "Alignment",
      primitive: null,
      maps: "alignment",
    },
    {
      property: "isSelected",
      type: "boolean",
      primitive: true,
      maps: "isSelected",
    },
    {
      property: "toggleGroup",
      type: "ToggleGroup",
      primitive: false,
      maps: null,
    },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  TextArea: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "prompt", type: "string", primitive: true, maps: "prompt" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
  ],
  TextField: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "prompt", type: "string", primitive: true, maps: "prompt" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
  ],
  ToggleButton: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
    {
      property: "alignment",
      type: "Alignment",
      primitive: null,
      maps: "alignment",
    },
    {
      property: "isSelected",
      type: "boolean",
      primitive: true,
      maps: "isSelected",
    },
    {
      property: "toggleGroup",
      type: "ToggleGroup",
      primitive: false,
      maps: null,
    },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  ListView: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "items", type: "StringArray", primitive: false, maps: null },
    { property: "font", type: "Font", primitive: false, maps: "font" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    {
      property: "orientation",
      type: "Orientation",
      primitive: false,
      maps: "orientation",
    },
    {
      property: "selectionMode",
      type: "SelectionMode",
      primitive: false,
      maps: "selectionMode",
    },
    {
      property: "selectionBackground",
      type: "Visual",
      primitive: false,
      maps: "selectionBackground",
    },
    {
      property: "formatFunction",
      type: "Function",
      primitive: false,
      maps: null,
    },
  ],
  TableView: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    {
      property: "columns",
      type: "TableColumnsArray",
      primitive: false,
      maps: null,
    },
    { property: "items", type: "StringArray", primitive: false, maps: null },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    {
      property: "selectionMode",
      type: "SelectionMode",
      primitive: false,
      maps: "selectionMode",
    },
    {
      property: "selectionBackground",
      type: "Visual",
      primitive: false,
      maps: "selectionBackground",
    },
  ],
  CameraPane: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    { property: "target", type: "ComponentView", primitive: false, maps: null },
  ],
  Pane: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  GridPane: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "columns", type: "number", primitive: true, maps: "columns" },
    { property: "rows", type: "number", primitive: true, maps: "rows" },
    { property: "spacing", type: "number", primitive: true, maps: "spacing" },
    {
      property: "layoutFromCenter",
      type: "boolean",
      primitive: true,
      maps: "isLayoutFromCenter",
    },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  CardView: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "front", type: "Visual", primitive: false, maps: "front" },
    { property: "back", type: "Visual", primitive: false, maps: "back" },
  ],
  DiceView: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    {
      property: "visuals",
      type: "VisualArray",
      primitive: false,
      maps: "visuals",
    },
  ],
  HexagonView: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "size", type: "number", primitive: true, maps: "size" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  TokenView: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  Area: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  CardStack: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    {
      property: "alignment",
      type: "Alignment",
      primitive: null,
      maps: "alignment",
    },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
  HexagonGrid: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    {
      property: "coordinateSystem",
      type: "CoordinateSystem",
      primitive: false,
      maps: "coordinateSystem",
    },
  ],
  LinearLayout: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "spacing", type: "number", primitive: true, maps: "spacing" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    {
      property: "orientation",
      type: "Orientation",
      primitive: false,
      maps: "orientation",
    },
    {
      property: "alignment",
      type: "Alignment",
      primitive: null,
      maps: "alignment",
    },
  ],
  Satchel: [
    { property: "posX", type: "number", primitive: true, maps: "posX" },
    { property: "posY", type: "number", primitive: true, maps: "posY" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "visual", type: "Visual", primitive: false, maps: "visual" },
  ],
};

export const specialConstructors = {
  Font: [
    { property: "size", type: "number", primitive: true, maps: "size" },
    { property: "color", type: "string", primitive: true, maps: "color" },
    { property: "family", type: "string", primitive: true, maps: "family" },
    {
      property: "fontWeight",
      type: "FontWeight",
      primitive: false,
      maps: "fontWeight",
    },
    {
      property: "fontStyle",
      type: "FontStyle",
      primitive: false,
      maps: "fontStyle",
    },
  ],
  CompoundVisual: [
    {
      property: "children",
      type: "VisualArray",
      primitive: false,
      maps: "children",
    },
  ],
  ColorVisual: [
    { property: "color", type: "string", primitive: true, maps: "color" },
  ],
  ImageVisual: [
    { property: "path", type: "string", primitive: true, maps: "path" },
    { property: "width", type: "number", primitive: true, maps: "width" },
    { property: "height", type: "number", primitive: true, maps: "height" },
    { property: "offsetX", type: "number", primitive: true, maps: "offsetX" },
    { property: "offsetY", type: "number", primitive: true, maps: "offsetY" },
  ],
  TextVisual: [
    { property: "text", type: "string", primitive: true, maps: "text" },
    { property: "font", type: "Font", primitive: false, maps: "font" },
    {
      property: "alignment",
      type: "Alignment",
      primitive: null,
      maps: "alignment",
    },
    { property: "offsetX", type: "number", primitive: true, maps: "offsetX" },
    { property: "offsetY", type: "number", primitive: true, maps: "offsetY" },
  ],
};

export const enums = {
  Alignment: {
    TOP_LEFT: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Left",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Top",
      },
    ],
    TOP_CENTER: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Center",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Top",
      },
    ],
    TOP_RIGHT: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Right",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Top",
      },
    ],
    CENTER_LEFT: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Left",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Center",
      },
    ],
    CENTER: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Center",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Center",
      },
    ],
    CENTER_RIGHT: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Right",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Center",
      },
    ],
    BOTTOM_LEFT: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Left",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Bottom",
      },
    ],
    BOTTOM_CENTER: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Center",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Bottom",
      },
    ],
    BOTTOM_RIGHT: [
      {
        property: "horizontalAlignment",
        type: "string",
        primitive: true,
        value: "Right",
      },
      {
        property: "verticalAlignment",
        type: "string",
        primitive: true,
        value: "Bottom",
      },
    ],
  },
  Orientation: {
    HORIZONTAL: [
      {
        property: "orientation",
        type: "string",
        primitive: true,
        value: "Horizontal",
      },
    ],
    VERTICAL: [
      {
        property: "orientation",
        type: "string",
        primitive: true,
        value: "Vertical",
      },
    ],
  },
  CoordinateSystem: {
    AXIAL: [
      {
        property: "coordinateSystem",
        type: "string",
        primitive: true,
        value: "Axial",
      },
    ],
    OFFSET: [
      {
        property: "coordinateSystem",
        type: "string",
        primitive: true,
        value: "Offset",
      },
    ],
  },
  FontWeight: {
    THIN: [
      { property: "fontWeight", type: "string", primitive: true, value: "100" },
    ],
    EXTRA_LIGHT: [
      { property: "fontWeight", type: "string", primitive: true, value: "200" },
    ],
    LIGHT: [
      { property: "fontWeight", type: "string", primitive: true, value: "300" },
    ],
    NORMAL: [
      { property: "fontWeight", type: "string", primitive: true, value: "400" },
    ],
    MEDIUM: [
      { property: "fontWeight", type: "string", primitive: true, value: "500" },
    ],
    SEMI_BOLD: [
      { property: "fontWeight", type: "string", primitive: true, value: "600" },
    ],
    BOLD: [
      { property: "fontWeight", type: "string", primitive: true, value: "700" },
    ],
    EXTRA_BOLD: [
      { property: "fontWeight", type: "string", primitive: true, value: "800" },
    ],
    BLACK: [
      { property: "fontWeight", type: "string", primitive: true, value: "900" },
    ],
  },
  FontStyle: {
    NORMAL: [
      {
        property: "fontStyle",
        type: "string",
        primitive: true,
        value: "Normal",
      },
    ],
    ITALIC: [
      {
        property: "fontStyle",
        type: "string",
        primitive: true,
        value: "Italic",
      },
    ],
    OBLIQUE: [
      {
        property: "fontStyle",
        type: "string",
        primitive: true,
        value: "Oblique",
      },
    ],
  },
};

export function colourNameToHex(colour) {
  const colours = {
    white: "#ffffff",
    light_gray: "#c0c0c0",
    gray: "#808080",
    dark_gray: "#404040",
    black: "#000000",
    red: "#ff0000",
    pink: "#ffafaf",
    orange: "#ffc800",
    yellow: "#ffff00",
    green: "#00ff00",
    lime: "#c8ff00",
    magenta: "#ff00ff",
    cyan: "#00ffff",
    blue: "#0000ff",
    purple: "#c800ff",
    brown: "#8b4513",
  };

  if (typeof colours[colour.toLowerCase()] != "undefined")
    return colours[colour.toLowerCase()];

  return false;
}
