import { PlaygroundData } from "./playground";

// Public primary constructors in bgw-gui/src/jvmMain. Dokka presentation
// signatures also contain secondary constructors and are unsuitable for export.
const bounds = "posX posY width height";
const labeled = `${bounds} text font alignment isWrapText visual`;
const input = `${bounds} text prompt font visual`;
const constructors: Record<string, string> = {
  Button: labeled, Label: labeled,
  CheckBox: `${labeled} isChecked allowIndeterminate isIndeterminate`,
  ToggleButton: `${labeled} isSelected toggleGroup`,
  RadioButton: `${bounds} text font alignment isSelected toggleGroup visual`,
  TextField: input, TextArea: input, PasswordField: input,
  ColorPicker: `${bounds} initialColor visual`, ProgressBar: `${bounds} visual progress barVisual`,
  Slider: `${bounds} visual minValue maxValue value step orientation`,
  ComboBox: `${bounds} font visual prompt items disallowUnselect itemVisuals`,
  ListView: `${bounds} items font visual orientation selectionMode selectionBackground`,
  TableView: `${bounds} columns items visual selectionMode selectionBackground`,
  TableColumn: "title width font formatFunction",
  Pane: `${bounds} visual`, GridPane: "posX posY columns rows spacing layoutFromCenter visual",
  CameraPane: `${bounds} visual limitBounds target`,
  CardView: `${bounds} front back`, DiceView: `${bounds} visuals`,
  HexagonView: "posX posY size visual orientation", TokenView: `${bounds} visual`,
  Area: `${bounds} visual`, CardStack: `${bounds} alignment visual`,
  HexagonGrid: `${bounds} visual coordinateSystem orientation`,
  LinearLayout: `${bounds} spacing visual orientation alignment`, Satchel: `${bounds} visual`,
  ColorVisual: "color", ImageVisual: "path width height offsetX offsetY rotation",
  TextVisual: "text font alignment offsetX offsetY rotation",
  Font: "size color family fontWeight fontStyle",
};
const writable = "posX posY width height zIndex opacity isVisible isDisabled isFocusable scaleX scaleY rotation layoutFromCenter isDraggable text font alignment isWrapText isSelected isChecked allowIndeterminate isIndeterminate isReadonly selectedColor progress barVisual orientation spacing interactive limitBounds isVerticalLocked isHorizontalLocked isZoomLocked panButton currentSide transparency flipped".split(" ");
const doubles = new Set("posX posY width height opacity scaleX scaleY rotation progress transparency spacing".split(" "));
const propertyNames: Record<string, string> = { layoutFromCenter: "isLayoutFromCenter", allowIndeterminate: "isIndeterminateAllowed", panButton: "panMouseButton" };
const sourceNames: Record<string, string> = { initialColor: "selectedColor", toggleGroup: "group" };
const enums: Record<string, string> = { orientation: "Orientation", coordinateSystem: "HexagonGrid.CoordinateSystem", selectionMode: "SelectionMode", flipped: "Flip", fontStyle: "Font.FontStyle", panButton: "MouseButtonType" };
const genericTypes: Record<string, string> = { Pane: "ComponentView", GridPane: "ComponentView", Area: "GameComponentView", CardStack: "CardView", LinearLayout: "GameComponentView", Satchel: "GameComponentView", HexagonGrid: "HexagonView", ComboBox: "String", ListView: "String", TableView: "Int", TableColumn: "Int" };
const indent = (text: string) => text.split("\n").map(line => `    ${line}`).join("\n");
const quote = (value: any) => '"' + String(value).replace(/[\\"$\u0000-\u001f\u2028\u2029]/g, character => {
  const escapes: Record<string, string> = { "\\": "\\\\", '"': '\\"', "$": "\\$", "\n": "\\n", "\r": "\\r", "\t": "\\t" };
  return escapes[character] ?? `\\u${character.charCodeAt(0).toString(16).padStart(4, "0")}`;
}) + '"';
const number = (value: number, double = false) => Number.isInteger(value) && double ? `${value}.0` : String(value);

function color(value: string): string {
  const hex = /^#([\da-f]{6})([\da-f]{2})?$/i.exec(value);
  if (hex) return hex[2]
    ? `Color(${parseInt(hex[1].slice(0, 2), 16)}, ${parseInt(hex[1].slice(2, 4), 16)}, ${parseInt(hex[1].slice(4, 6), 16)}, ${parseInt(hex[2], 16)})`
    : `Color(${quote(value)})`;
  const rgba = /^rgba?\(([^)]+)\)$/.exec(value);
  if (rgba) {
    const parts = rgba[1].split(",").map(Number);
    return `Color(${parts.slice(0, 3).join(", ")}${parts.length === 4 ? `, ${number(parts[3], true)}` : ""})`;
  }
  throw new Error(`Unsupported color: ${value}`);
}
function alignment(value: { first: string; second: string }) {
  return `Alignment.${value.first === "center" && value.second === "center" ? "CENTER" : `${value.second}_${value.first}`.toUpperCase()}`;
}
function call(name: string, args: string[]) {
  return args.length ? `${name}(\n${indent(args.join(",\n"))}\n)` : `${name}()`;
}

function createExporter() {
  const groups = new Map<string, string>();
  function value(data: any, key = "", owner = ""): string {
    if (data === null) return "null";
    if (key === "toggleGroup") {
      if (!groups.has(data)) groups.set(data, `toggleGroup${groups.size + 1}`);
      return groups.get(data)!;
    }
    if (["color", "initialColor", "selectedColor"].includes(key)) return color(data);
    if (key === "selectionBackground") return `ColorVisual(${color(data)})`;
    if (key === "alignment") return alignment(data);
    if (key === "fontWeight") {
      const names = ["THIN", "EXTRA_LIGHT", "LIGHT", "NORMAL", "MEDIUM", "SEMI_BOLD", "BOLD", "EXTRA_BOLD", "BLACK"];
      return `Font.FontWeight.${names[Number(data) / 100 - 1]}`;
    }
    if (key in enums) {
      const type = key === "orientation" && ["HexagonView", "HexagonGrid"].includes(owner) ? "HexOrientation" : enums[key];
      return `${type}.${String(data).toUpperCase().replaceAll(" ", "_")}`;
    }
    if (Array.isArray(data)) {
      if (key === "items" && owner === "ComboBox") return call("listOf", data.map(item => quote(item.second)));
      if (key === "items" && owner === "TableView") return call("listOf", data.map((_, i) => String(i)));
      return call("listOf", data.map(item => value(item)));
    }
    if (typeof data === "string") return quote(data);
    if (typeof data === "boolean") return String(data);
    if (typeof data === "number") return number(data);
    if (data?.type) return expression(data);
    throw new Error(`Cannot export ${owner}.${key}`);
  }

  function expression(data: PlaygroundData): string {
    const type = data.type.replace(/Data$/, "");
    if (type === "CompoundVisual") return data.children.length ? call("CompoundVisual", data.children.map((child: PlaygroundData) => expression(child))) : "Visual.EMPTY";
    if (type === "ToggleGroup") {
      value(data.group, "toggleGroup");
      return groups.get(data.group)!;
    }
    if (type === "GridElement" || type === "HexagonGridElement") {
      const child = data.component ?? data.hexagon;
      if (!child) throw new Error("Add a component to the grid cell before exporting it.");
      return expression(child);
    }
    const signature = constructors[type];
    if (signature === undefined) throw new Error(`No Kotlin exporter for ${type}.`);
    if (type === "ImageVisual" && /^https?:\/\//i.test(data.path)) throw new Error("Use a resource path or PNG data URI for Kotlin image export.");
    const params = signature.split(" ");
    const args = params.flatMap(key => {
      if (key === "formatFunction") return [`formatFunction = { row -> ${call("listOf", data.items.map(quote))}[row] }`];
      const source = sourceNames[key] ?? key;
      if (data[source] === undefined) return [];
      if (key === "target" && !data.target) throw new Error("Add a layout to the camera pane before exporting it.");
      const double = key === "progress" || (type === "Slider" && ["minValue", "maxValue", "value", "step"].includes(key));
      return [`${key} = ${double ? number(data[source], true) : value(data[source], key, type)}`];
    });
    const generic = genericTypes[type] ? `<${genericTypes[type]}>` : "";
    const code = call(type + generic, args);
    const apply: string[] = [];
    for (const key of writable) {
      if (data[key] === undefined || params.includes(key) || params.some(param => sourceNames[param] === key)) continue;
      if (key === "currentSide" && type !== "DiceView") continue;
      if (key === "isDraggable" && !data.objectType?.includes("GameComponentViewData") && !data.objectType?.includes("GameComponentContainerData")) continue;
      if (key === "spacing" && type === "HexagonGrid") continue;
      const rendered = typeof data[key] === "number" ? number(data[key], doubles.has(key)) : value(data[key], key, type);
      apply.push(`${propertyNames[key] ?? key} = ${rendered}`);
    }
    if (type === "CardView") apply.push(data.current === "back" ? "showBack()" : "showFront()");
    if (type === "ComboBox") apply.push(`selectedItem = ${data.selectedItem ? quote(data.selectedItem.second) : "null"}`);
    if (["ListView", "TableView"].includes(type) && data.selectionMode !== "none") {
      for (const index of data.selectedItems ?? []) apply.push(`selectIndex(${index})`);
    }
    for (const child of data.components ?? []) apply.push(`add(${expression(child)})`);
    for (const cell of data.grid ?? []) {
      if (!cell.component) continue;
      apply.push(`this[${cell.column}, ${cell.row}] = ${expression(cell.component)}`);
      apply.push(`setCellCenterMode(${cell.column}, ${cell.row}, ${alignment(cell.alignment)})`);
    }
    for (const [coordinate, child] of Object.entries(data.map ?? {})) apply.push(`this[${coordinate.split("/").join(", ")}] = ${expression(child as PlaygroundData)}`);
    return apply.length ? `${code}.apply {\n${indent(apply.join("\n"))}\n}` : code;
  }
  return { expression, declarations: () => [...groups.values()].map(name => `val ${name} = ToggleGroup()`).join("\n") };
}

const imports = [
  "tools.aqua.bgw.core.*", "tools.aqua.bgw.components.ComponentView",
  "tools.aqua.bgw.components.container.*", "tools.aqua.bgw.components.gamecomponentviews.*",
  "tools.aqua.bgw.components.layoutviews.*", "tools.aqua.bgw.components.uicomponents.*",
  "tools.aqua.bgw.util.Font", "tools.aqua.bgw.visual.*", "tools.aqua.bgw.style.Flip", "tools.aqua.bgw.event.MouseButtonType",
].map(pkg => `import ${pkg}`).join("\n");
function identifier(name: string, type: string) {
  let result = name.trim().replace(/[^a-zA-Z0-9_]/g, "_") || type.replace(/Data$/, "");
  result = result.charAt(0).toLowerCase() + result.slice(1);
  if (/^\d/.test(result)) result = `component_${result}`;
  return `\`${result}\``;
}
export function exportComponent(component: PlaygroundData): string {
  const exporter = createExporter();
  const code = `val ${identifier(component.name ?? "", component.type)} = ${exporter.expression(component)}`;
  return [imports, exporter.declarations(), code].filter(Boolean).join("\n\n");
}
export function exportScene(components: PlaygroundData[], width: number, height: number, background: string) {
  const exporter = createExporter();
  const children = components.map(component => exporter.expression(component));
  const scene = `val scene = object : BoardGameScene(\n${indent(`width = ${width},\nheight = ${height},\nbackground = ColorVisual(${color(background)})`)}\n) {\n${indent(`init {${children.length ? `\n${indent(call("addComponents", children))}\n` : ""}}`)}\n}`;
  return [imports, exporter.declarations(), scene].filter(Boolean).join("\n\n");
}
