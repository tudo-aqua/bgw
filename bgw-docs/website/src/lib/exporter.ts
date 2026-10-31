import { PlaygroundData } from "./playground";
import { AnimationClip, animationImports, clipsToKotlin, Snapshot } from "./animations";

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
// Library defaults: properties still holding these values are not written to the exported code.
const defaults: Record<string, any> = {
  zIndex: 0, opacity: 1, isVisible: true, isDisabled: false, isFocusable: true, scaleX: 1, scaleY: 1, rotation: 0,
  layoutFromCenter: false, isDraggable: false, isSelected: false, isChecked: false, isIndeterminate: false, isReadonly: false,
  transparency: 1, flipped: "none", currentSide: 0,
};
// Constructor arguments are omitted when they render to the library default for that type.
const aligned = new Set(["Button", "Label", "CheckBox", "ToggleButton", "RadioButton", "TextVisual", "CardStack"]);
const argDefaults: Record<string, Record<string, string>> = {
  "*": { posX: "0", posY: "0", isWrapText: "false", offsetX: "0", offsetY: "0", rotation: "0", font: "Font()" },
  Font: { size: "14", color: "Color(0x000000)", family: '"Arial"', fontWeight: "Font.FontWeight.NORMAL", fontStyle: "Font.FontStyle.NORMAL" },
};
const isDefaultArg = (type: string, key: string, rendered: string) =>
  (argDefaults[type] ?? argDefaults["*"])[key] === rendered ||
  (key === "alignment" && aligned.has(type) && rendered === "Alignment.CENTER");
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
    : `Color(0x${hex[1].toUpperCase()})`;
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

const keywords = new Set("as break class continue do else false for fun if in interface is null object package return super this throw true try typealias typeof val var when while".split(" "));

function createExporter(hoist = false, sceneNames: Record<string, string> = {}) {
  const groups = new Map<string, string>();
  const members: string[] = [];
  const used = new Set<string>();
  const names: Record<string, string> = {};
  // Hoisting turns every nested component into a named property of the exported class.
  function ref(data: PlaygroundData): string {
    const code = expression(data);
    const base = propertyName(data.name ?? "", data.type);
    let name = base;
    for (let index = 2; used.has(name); index++) name = `${base}${index}`;
    used.add(name);
    if (data.id) names[data.id] = name;
    members.push(`val ${name} = ${code}`);
    return name;
  }
  const child = (data: PlaygroundData) => hoist ? ref(data) : expression(data);
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
    if (data?.type) return key === "target" ? child(data) : expression(data);
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
      const rendered = double ? number(data[source], true) : value(data[source], key, type);
      return isDefaultArg(type, key, rendered) ? [] : [`${key} = ${rendered}`];
    });
    const generic = genericTypes[type] ? `<${genericTypes[type]}>` : "";
    const code = call(type + generic, args);
    const apply: string[] = [];
    for (const key of writable) {
      if (data[key] === undefined || params.includes(key) || params.some(param => sourceNames[param] === key)) continue;
      if (key === "currentSide" && type !== "DiceView") continue;
      if (key === "isDraggable" && !data.objectType?.includes("GameComponentViewData") && !data.objectType?.includes("GameComponentContainerData")) continue;
      if (key === "spacing" && type === "HexagonGrid") continue;
      if (key in defaults && data[key] === defaults[key]) continue;
      const rendered = typeof data[key] === "number" ? number(data[key], doubles.has(key)) : value(data[key], key, type);
      apply.push(`${propertyNames[key] ?? key} = ${rendered}`);
    }
    if (data.navigateTo) apply.push(`onMouseClicked = { /* TODO: ${data.navigateTo === "exit" ? "exit the program" : `show ${sceneNames[data.navigateTo] ?? "another scene"}`} */ }`);
    if (type === "CardView") if (data.current === "back") apply.push("showBack()");
    if (type === "ComboBox" && data.selectedItem) apply.push(`selectedItem = ${quote(data.selectedItem.second)}`);
    if (["ListView", "TableView"].includes(type) && data.selectionMode !== "none") {
      for (const index of data.selectedItems ?? []) apply.push(`selectIndex(${index})`);
    }
    for (const item of data.components ?? []) apply.push(`add(${child(item)})`);
    for (const cell of data.grid ?? []) {
      if (!cell.component) continue;
      apply.push(`this[${cell.column}, ${cell.row}] = ${child(cell.component)}`);
      apply.push(`setCellCenterMode(${cell.column}, ${cell.row}, ${alignment(cell.alignment)})`);
    }
    for (const [coordinate, item] of Object.entries(data.map ?? {})) apply.push(`this[${coordinate.split("/").join(", ")}] = ${child(item as PlaygroundData)}`);
    return apply.length ? `${code}.apply {\n${indent(apply.join("\n"))}\n}` : code;
  }
  return { expression, ref, names, members: () => members, declarations: () => [...groups.values()].map(name => `val ${name} = ToggleGroup()`).join("\n") };
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
function propertyName(name: string, type: string) {
  const words = (name.trim() || type.replace(/Data$/, "")).split(/[^a-zA-Z0-9]+/).filter(Boolean);
  let result = words.map((word, index) => index ? word.charAt(0).toUpperCase() + word.slice(1) : word.charAt(0).toLowerCase() + word.slice(1)).join("") || "component";
  if (/^\d/.test(result)) result = `component${result.charAt(0).toUpperCase()}${result.slice(1)}`;
  return keywords.has(result) ? `\`${result}\`` : result;
}
export function exportComponent(component: PlaygroundData): string {
  const exporter = createExporter();
  const code = `val ${identifier(component.name ?? "", component.type)} = ${exporter.expression(component)}`;
  return [imports, exporter.declarations(), code].filter(Boolean).join("\n\n");
}
export function sceneClassName(name: string) {
  const words = name.split(/[^a-zA-Z0-9]+/).filter(Boolean);
  const result = words.map(word => word.charAt(0).toUpperCase() + word.slice(1)).join("");
  return !result ? "PlaygroundScene" : /^\d/.test(result) ? `Scene${result}` : result;
}
export function exportScene(components: PlaygroundData[], width: number, height: number, background: string, sceneName = "", sceneNames: Record<string, string> = {}, animations?: { clips: AnimationClip[]; snapshot: Snapshot }) {
  const exporter = createExporter(true, sceneNames);
  const refs = components.map(component => exporter.ref(component));
  const clipMembers = animations ? clipsToKotlin(animations.clips, animations.snapshot, exporter.names, name => propertyName(name, "Animation")) : [];
  const members = [exporter.declarations(), ...exporter.members(), ...clipMembers].filter(Boolean);
  const init = refs.length ? `init {\n${indent(call("addComponents", refs))}\n}` : "";
  const body = [members.join("\n"), init].filter(Boolean).map(indent).join("\n\n");
  const settings = `width = ${width},\nheight = ${height},\nbackground = ColorVisual(${color(background)})`;
  const header = `class ${sceneClassName(sceneName)} : BoardGameScene(\n${indent(settings)}\n)`;
  return [clipMembers.length ? `${imports}\n${animationImports}` : imports, body ? `${header} {\n${body}\n}` : header].join("\n\n");
}
