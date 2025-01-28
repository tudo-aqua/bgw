export const constructors = {
    "Button": [
        { property: "posX", type: "number", primitive: true, maps: "posX" }, 
        { property: "posY", type: "number", primitive: true, maps: "posY" }, 
        { property: "width", type: "number", primitive: true, maps: "width" }, 
        { property: "height", type: "number", primitive: true, maps: "height" }, 
        { property: "text", type: "string", primitive: true, maps: "text" }, 
        { property: "font", type: "Font", primitive: false, maps: "font" }, 
        { property: "alignment", type: "Alignment", primitive: null, maps: "alignment" },
        { property: "isWrapText", type: "boolean", primitive: true, maps: "isWrapText" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "CheckBox": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "text", type: "string", primitive: true, maps: "text" },
        { property: "font", type: "Font", primitive: false, maps: "font" },
        { property: "alignment", type: "Alignment", primitive: null, maps: "alignment" },
        { property: "isWrapText", type: "boolean", primitive: true, maps: "isWrapText" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
        { property: "isChecked", type: "boolean", primitive: true, maps: "isChecked" },
        { property: "allowIndeterminate", type: "boolean", primitive: true, maps: "allowIndeterminate" },
        { property: "isIndeterminate", type: "boolean", primitive: true, maps: "isIndeterminate" },
    ],
    "ColorPicker": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "initialColor", type: "string", primitive: true, maps: "selectedColor" },
    ],
    "ComboBox": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "font", type: "Font", primitive: false, maps: "font" },
        { property: "prompt", type: "string", primitive: true, maps: "prompt" },
        { property: "items", type: "StringArray", primitive: false, maps: null },
        { property: "formatFunction", type: "Function", primitive: false, maps: null },
    ],
    "Label": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "text", type: "string", primitive: true, maps: "text" },
        { property: "font", type: "Font", primitive: false, maps: "font" },
        { property: "alignment", type: "Alignment", primitive: null, maps: "alignment" },
        { property: "isWrapText", type: "boolean", primitive: true, maps: "isWrapText" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "PasswordField": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "text", type: "string", primitive: true, maps: "text" },
        { property: "prompt", type: "string", primitive: true, maps: "prompt" },
        { property: "font", type: "Font", primitive: false, maps: "font" },
    ],
    "ProgressBar": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "progress", type: "number", primitive: true, maps: "progress" },
        { property: "barColor", type: "string", primitive: true, maps: "barColor" },
    ],
    "RadioButton": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "text", type: "string", primitive: true, maps: "text" },
        { property: "font", type: "Font", primitive: false, maps: "font" },
        { property: "alignment", type: "Alignment", primitive: null, maps: "alignment" },
        { property: "isSelected", type: "boolean", primitive: true, maps: "isSelected" },
        { property: "toggleGroup", type: "ToggleGroup", primitive: false, maps: null },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "TextArea": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "text", type: "string", primitive: true, maps: "text" },
        { property: "prompt", type: "string", primitive: true, maps: "prompt" },
        { property: "font", type: "Font", primitive: false, maps: "font" },
    ],
    "TextField": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "text", type: "string", primitive: true, maps: "text" },
        { property: "prompt", type: "string", primitive: true, maps: "prompt" },
        { property: "font", type: "Font", primitive: false, maps: "font" },
    ],
    "ToggleButton": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "text", type: "string", primitive: true, maps: "text" },
        { property: "font", type: "Font", primitive: false, maps: "font" },
        { property: "alignment", type: "Alignment", primitive: null, maps: "alignment" },
        { property: "isSelected", type: "boolean", primitive: true, maps: "isSelected" },
        { property: "toggleGroup", type: "ToggleGroup", primitive: false, maps: null },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "ListView": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "items", type: "StringArray", primitive: false, maps: null },
        { property: "font", type: "Font", primitive: false, maps: "font" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
        { property: "orientation", type: "Orientation", primitive: false, maps: "orientation" },
        { property: "selectionMode", type: "SelectionMode", primitive: false, maps: "selectionMode" },
        { property: "selectionBackground", type: "Visual", primitive: false, maps: "selectionBackground" },
        { property: "formatFunction", type: "Function", primitive: false, maps: null },
    ],
    "TableView": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "columns", type: "TableColumnsArray", primitive: false, maps: null },
        { property: "items", type: "StringArray", primitive: false, maps: null },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
        { property: "selectionMode", type: "SelectionMode", primitive: false, maps: "selectionMode" },
        { property: "selectionBackground", type: "Visual", primitive: false, maps: "selectionBackground" },
    ],
    "CameraPane": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
        { property: "target", type: "ComponentView", primitive: false, maps: null },
    ],
    "Pane": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "GridPane": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "columns", type: "number", primitive: true, maps: "columns" },
        { property: "rows", type: "number", primitive: true, maps: "rows" },
        { property: "spacing", type: "number", primitive: true, maps: "spacing" },
        { property: "layoutFromCenter", type: "boolean", primitive: true, maps: "isLayoutFromCenter"},
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "CardView": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "front", type: "Visual", primitive: false, maps: "front" },
        { property: "back", type: "Visual", primitive: false, maps: "back" },
    ],
    "DiceView": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "visuals", type: "VisualArray", primitive: false, maps: "visuals" },
    ],
    "HexagonView": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "size", type: "number", primitive: true, maps: "size" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "TokenView": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "Area": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "CardStack": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "alignment", type: "Alignment", primitive: null, maps: "alignment" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
    "HexagonGrid": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
        { property: "coordinateSystem", type: "CoordinateSystem", primitive: false, maps: "coordinateSystem" },
    ],
    "LinearLayout": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "spacing", type: "number", primitive: true, maps: "spacing" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
        { property: "orientation", type: "Orientation", primitive: false, maps: "orientation" },
        { property: "alignment", type: "Alignment", primitive: null, maps: "alignment" },
    ],
    "Satchel": [
        { property: "posX", type: "number", primitive: true, maps: "posX" },
        { property: "posY", type: "number", primitive: true, maps: "posY" },
        { property: "width", type: "number", primitive: true, maps: "width" },
        { property: "height", type: "number", primitive: true, maps: "height" },
        { property: "visual", type: "Visual", primitive: false, maps: "visual" },
    ],
}

export const specialConstructors = {
    "Font": [
        {property: "size", type: "number", primitive: true, maps: "size"},
        {property: "color", type: "string", primitive: true, maps: "color"},
        {property: "family", type: "string", primitive: true, maps: "family"},
        {property: "fontWeight", type: "FontWeight", primitive: false, maps: "fontWeight"},
        {property: "fontStyle", type: "FontStyle", primitive: false, maps: "fontStyle"},
    ],
    "CompoundVisual": [
        {property: "children", type: "VisualArray", primitive: false, maps: "children"},
    ],
    "ColorVisual": [
        {property: "color", type: "string", primitive: true, maps: "color"},
    ],
    "ImageVisual": [
        {property: "path", type: "string", primitive: true, maps: "path"},
        {property: "width", type: "number", primitive: true, maps: "width"},
        {property: "height", type: "number", primitive: true, maps: "height"},
        {property: "offsetX", type: "number", primitive: true, maps: "offsetX"},
        {property: "offsetY", type: "number", primitive: true, maps: "offsetY"},
    ],
    "TextVisual": [
        {property: "text", type: "string", primitive: true, maps: "text"},
        {property: "font", type: "Font", primitive: false, maps: "font"},
        {property: "alignment", type: "Alignment", primitive: null, maps: "alignment"},
        {property: "offsetX", type: "number", primitive: true, maps: "offsetX"},
        {property: "offsetY", type: "number", primitive: true, maps: "offsetY"},
    ],
}

export const enums = {
    "Alignment": {
        "TOP_LEFT": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Left"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Top"},
        ],
        "TOP_CENTER": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Center"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Top"},
        ],
        "TOP_RIGHT": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Right"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Top"},
        ],
        "CENTER_LEFT": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Left"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Center"},
        ],
        "CENTER": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Center"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Center"},
        ],
        "CENTER_RIGHT": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Right"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Center"},
        ],
        "BOTTOM_LEFT": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Left"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Bottom"},
        ],
        "BOTTOM_CENTER": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Center"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Bottom"},
        ],
        "BOTTOM_RIGHT": [
            {property: "horizontalAlignment", type: "string", primitive: true, value: "Right"},
            {property: "verticalAlignment", type: "string", primitive: true, value: "Bottom"},
        ],
    },
    "Orientation": {
        "HORIZONTAL": [
            {property: "orientation", type: "string", primitive: true, value: "Horizontal"},
        ],
        "VERTICAL": [
            {property: "orientation", type: "string", primitive: true, value: "Vertical"},
        ],
    },
    "CoordinateSystem": {
        "AXIAL": [
            {property: "coordinateSystem", type: "string", primitive: true, value: "Axial"},
        ],
        "OFFSET": [
            {property: "coordinateSystem", type: "string", primitive: true, value: "Offset"},
        ],
    },
    "FontWeight": {
        "THIN": [
            {property: "fontWeight", type: "string", primitive: true, value: "100"},
        ],
        "EXTRA_LIGHT": [
            {property: "fontWeight", type: "string", primitive: true, value: "200"},
        ],
        "LIGHT": [
            {property: "fontWeight", type: "string", primitive: true, value: "300"},
        ],
        "NORMAL": [
            {property: "fontWeight", type: "string", primitive: true, value: "400"},
        ],
        "MEDIUM": [
            {property: "fontWeight", type: "string", primitive: true, value: "500"},
        ],
        "SEMI_BOLD": [
            {property: "fontWeight", type: "string", primitive: true, value: "600"},
        ],
        "BOLD": [
            {property: "fontWeight", type: "string", primitive: true, value: "700"},
        ],
        "EXTRA_BOLD": [
            {property: "fontWeight", type: "string", primitive: true, value: "800"},
        ],
        "BLACK": [
            {property: "fontWeight", type: "string", primitive: true, value: "900"},
        ],
    },
    "FontStyle": {
        "NORMAL": [
            {property: "fontStyle", type: "string", primitive: true, value: "Normal"},
        ],
        "ITALIC": [
            {property: "fontStyle", type: "string", primitive: true, value: "Italic"},
        ],
        "OBLIQUE": [
            {property: "fontStyle", type: "string", primitive: true, value: "Oblique"},
        ],
    }
}

export function colourNameToHex(colour)
{
    var colours = {"aliceblue":"#f0f8ff","antiquewhite":"#faebd7","aqua":"#00ffff","aquamarine":"#7fffd4","azure":"#f0ffff",
        "beige":"#f5f5dc","bisque":"#ffe4c4","black":"#000000","blanchedalmond":"#ffebcd","blue":"#0000ff","blueviolet":"#8a2be2","brown":"#a52a2a","burlywood":"#deb887",
        "cadetblue":"#5f9ea0","chartreuse":"#7fff00","chocolate":"#d2691e","coral":"#ff7f50","cornflowerblue":"#6495ed","cornsilk":"#fff8dc","crimson":"#dc143c","cyan":"#00ffff",
        "darkblue":"#00008b","darkcyan":"#008b8b","darkgoldenrod":"#b8860b","darkgray":"#a9a9a9","darkgreen":"#006400","darkkhaki":"#bdb76b","darkmagenta":"#8b008b","darkolivegreen":"#556b2f",
        "darkorange":"#ff8c00","darkorchid":"#9932cc","darkred":"#8b0000","darksalmon":"#e9967a","darkseagreen":"#8fbc8f","darkslateblue":"#483d8b","darkslategray":"#2f4f4f","darkturquoise":"#00ced1",
        "darkviolet":"#9400d3","deeppink":"#ff1493","deepskyblue":"#00bfff","dimgray":"#696969","dodgerblue":"#1e90ff",
        "firebrick":"#b22222","floralwhite":"#fffaf0","forestgreen":"#228b22","fuchsia":"#ff00ff",
        "gainsboro":"#dcdcdc","ghostwhite":"#f8f8ff","gold":"#ffd700","goldenrod":"#daa520","gray":"#808080","green":"#008000","greenyellow":"#adff2f",
        "honeydew":"#f0fff0","hotpink":"#ff69b4",
        "indianred ":"#cd5c5c","indigo":"#4b0082","ivory":"#fffff0","khaki":"#f0e68c",
        "lavender":"#e6e6fa","lavenderblush":"#fff0f5","lawngreen":"#7cfc00","lemonchiffon":"#fffacd","lightblue":"#add8e6","lightcoral":"#f08080","lightcyan":"#e0ffff","lightgoldenrodyellow":"#fafad2",
        "lightgrey":"#d3d3d3","lightgreen":"#90ee90","lightpink":"#ffb6c1","lightsalmon":"#ffa07a","lightseagreen":"#20b2aa","lightskyblue":"#87cefa","lightslategray":"#778899","lightsteelblue":"#b0c4de",
        "lightyellow":"#ffffe0","lime":"#00ff00","limegreen":"#32cd32","linen":"#faf0e6",
        "magenta":"#ff00ff","maroon":"#800000","mediumaquamarine":"#66cdaa","mediumblue":"#0000cd","mediumorchid":"#ba55d3","mediumpurple":"#9370d8","mediumseagreen":"#3cb371","mediumslateblue":"#7b68ee",
        "mediumspringgreen":"#00fa9a","mediumturquoise":"#48d1cc","mediumvioletred":"#c71585","midnightblue":"#191970","mintcream":"#f5fffa","mistyrose":"#ffe4e1","moccasin":"#ffe4b5",
        "navajowhite":"#ffdead","navy":"#000080",
        "oldlace":"#fdf5e6","olive":"#808000","olivedrab":"#6b8e23","orange":"#ffa500","orangered":"#ff4500","orchid":"#da70d6",
        "palegoldenrod":"#eee8aa","palegreen":"#98fb98","paleturquoise":"#afeeee","palevioletred":"#d87093","papayawhip":"#ffefd5","peachpuff":"#ffdab9","peru":"#cd853f","pink":"#ffc0cb","plum":"#dda0dd","powderblue":"#b0e0e6","purple":"#800080",
        "rebeccapurple":"#663399","red":"#ff0000","rosybrown":"#bc8f8f","royalblue":"#4169e1",
        "saddlebrown":"#8b4513","salmon":"#fa8072","sandybrown":"#f4a460","seagreen":"#2e8b57","seashell":"#fff5ee","sienna":"#a0522d","silver":"#c0c0c0","skyblue":"#87ceeb","slateblue":"#6a5acd","slategray":"#708090","snow":"#fffafa","springgreen":"#00ff7f","steelblue":"#4682b4",
        "tan":"#d2b48c","teal":"#008080","thistle":"#d8bfd8","tomato":"#ff6347","turquoise":"#40e0d0",
        "violet":"#ee82ee",
        "wheat":"#f5deb3","white":"#ffffff","whitesmoke":"#f5f5f5",
        "yellow":"#ffff00","yellowgreen":"#9acd32"};

    if (typeof colours[colour.toLowerCase()] != 'undefined')
        return colours[colour.toLowerCase()];

    return false;
}