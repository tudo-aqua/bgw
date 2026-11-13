import { DataClass } from "./components";

export type PlaygroundNode = { id: string; parent: string | number };
export type PlaygroundData = Record<string, any>;

// Keep editor state separate from the JSON consumed by the Kotlin renderer.
function unwrap(value: any): any {
  if (value == null || typeof value !== "object") return value;
  if ("propType" in value) {
    if (value.propType === "number") {
      const number = Number(value.value);
      return Number.isFinite(number) ? number : 0;
    }
    return value.propType === "choice"
      ? String(value.value).toLowerCase().replaceAll(" ", "_")
      : value.value;
  }
  if (value instanceof Map) return Object.fromEntries([...value].map(([k, v]) => [k, unwrap(v)]));
  if (Array.isArray(value)) return value.map(unwrap);
  const data = Object.fromEntries(Object.entries(value).map(([k, v]) => [k, unwrap(v)]));
  if (data.horizontalAlignment !== undefined) {
    data.alignment = { first: data.horizontalAlignment, second: data.verticalAlignment };
    delete data.horizontalAlignment;
    delete data.verticalAlignment;
  }
  if (data.type === "FontData") data.fontWeight = Number(data.fontWeight);
  if (data.type === "ProgressBarData") data.progress = Math.min(1, Math.max(0, data.progress));
  if (data.type === "SliderData") {
    data.maxValue = Math.max(data.minValue, data.maxValue);
    data.value = Math.min(data.maxValue, Math.max(data.minValue, data.value));
    if (data.step <= 0) throw new Error("Slider step must be greater than zero.");
  }
  if (data.type === "CardViewData") data.currentVisual = data[data.current];
  if (data.type === "DiceViewData") {
    data.visuals = Object.keys(data).filter(k => k.startsWith("vis_")).sort((a, b) => Number(a.slice(4)) - Number(b.slice(4))).map(k => data[k]);
    data.currentSide = Math.min(data.visuals.length - 1, Math.max(0, Number(data.currentSide) - 1));
    Object.keys(data).filter(k => k.startsWith("vis_")).forEach(k => delete data[k]);
  }
  return data;
}

export function buildPlaygroundSnapshot(components: Map<string, DataClass>, tree: PlaygroundNode[], excluded = new Set<string>()) {
  const allComponentsCopy: Record<string, PlaygroundData> = {};
  components.forEach((component, id) => {
    const data = unwrap(component);
    if ("components" in data) data.components = [];
    if ("grid" in data) {
      data.grid = [];
      data.columnWidths = Array.from({ length: data.columns }, () => -1);
      data.rowHeights = Array.from({ length: data.rows }, () => -1);
    }
    if (data.type === "HexagonGridData") data.map = {};
    if (data.type === "CameraPaneData") data.target = null;
    if (data.type === "GridElementData") data.component = null;
    if (data.type === "HexagonGridElementData") data.hexagon = null;
    if (data.type === "TableViewData") data.columns = [];
    allComponentsCopy[id] = data;
  });
  const children = new Map<string | number, PlaygroundNode[]>();
  tree.forEach(node => children.set(node.parent, [...(children.get(node.parent) ?? []), node]));
  const visiting = new Set<string>();
  function build(node: PlaygroundNode): PlaygroundData | null {
    const data = allComponentsCopy[node.id];
    if (!data || excluded.has(node.id) || visiting.has(node.id)) return null;
    visiting.add(node.id);
    for (const child of children.get(node.id) ?? []) {
      const item = build(child);
      if (!item) continue;
      if (data.type === "ToggleGroupData") item.group = data.group;
      else if (data.type === "CameraPaneData") data.target = item;
      else if (data.type === "GridElementData") data.component = item;
      else if (data.type === "HexagonGridElementData") data.hexagon = item;
      else if (data.type === "GridPaneData") {
        item.width = -1;
        item.height = -1;
        data.grid.push(item);
      } else if (data.type === "HexagonGridData") {
        if (item.hexagon) {
          item.hexagon.orientation = data.orientation;
          data.map[`${item.column}/${item.row}`] = item.hexagon;
        }
      } else if (data.type === "TableViewData") data.columns.push(item);
      else if (Array.isArray(data.components)) data.components.push(item);
    }
    visiting.delete(node.id);
    return data;
  }
  const roots = (children.get(0) ?? []).flatMap(node => {
    const data = build(node);
    return data ? data.type === "ToggleGroupData"
      ? (children.get(node.id) ?? []).map(child => allComponentsCopy[child.id]).filter(child => child && !excluded.has(child.id))
      : [data] : [];
  });
  return { components: roots, allComponentsCopy };
}

const componentFields = "id posX posY width height visual zIndex opacity isVisible isDisabled isFocusable scaleX scaleY rotation layoutFromCenter isDraggable";
const labeledFields = "text alignment isWrapText";
const inputFields = "text prompt isReadonly";
const structuredFields = "items selectionMode selectionBackground selectedItems";
const fields: Record<string, string> = {
  ButtonData: labeledFields, LabelData: labeledFields,
  CheckBoxData: `${labeledFields} isChecked allowIndeterminate isIndeterminate`,
  RadioButtonData: `${labeledFields} isSelected group`, ToggleButtonData: `${labeledFields} isSelected group`,
  TextFieldData: inputFields, TextAreaData: inputFields, PasswordFieldData: inputFields,
  ComboBoxData: "prompt items itemVisuals selectedItem disallowUnselect", ColorPickerData: "selectedColor",
  ProgressBarData: "progress barVisual", ListViewData: `${structuredFields} orientation`,
  SliderData: "value minValue maxValue step orientation thumbVisual",
  TableViewData: `${structuredFields} columns`, PaneData: "components",
  GridPaneData: "columns rows spacing grid columnWidths rowHeights",
  CameraPaneData: "target interactive panButton limitBounds isVerticalLocked isHorizontalLocked isZoomLocked",
  CardViewData: "front back currentVisual", DiceViewData: "currentSide visuals",
  HexagonViewData: "size orientation", TokenViewData: "", AreaData: "components",
  CardStackData: "components alignment", LinearLayoutData: "components orientation alignment spacing",
  HexagonGridData: "components coordinateSystem map spacing orientation", SatchelData: "components",
  GridElementData: "column row width height component alignment", TableColumnData: "title width font items",
  FontData: "size color family fontWeight fontStyle",
  CompoundVisualData: "id children",
  ColorVisualData: "color", ImageVisualData: "path width height offsetX offsetY", TextVisualData: "text font alignment offsetX offsetY",
};

export function toPreviewData(data: any): any {
  if (data == null || typeof data !== "object") return data;
  if (Array.isArray(data)) return data.map(toPreviewData);
  if (!data.type) return Object.fromEntries(Object.entries(data).map(([k, v]) => [k, toPreviewData(v)]));
  const allowed = new Set((fields[data.type] ?? "").split(" "));
  if (data.objectType?.includes("ComponentViewData")) componentFields.split(" ").forEach(k => allowed.add(k));
  if (data.objectType?.includes("UIComponentData")) allowed.add("font");
  if (data.objectType?.includes("SingleLayerVisualData")) "id transparency style filters flipped rotation".split(" ").forEach(k => allowed.add(k));
  const result = Object.fromEntries(Object.entries(data).filter(([key]) => allowed.has(key)).map(([k, v]) => [k, toPreviewData(v)]));
  if (!["FontData", "GridElementData", "TableColumnData"].includes(data.type)) result.type = data.type;
  // Kotlin's wire format stores integral geometry even though the public API accepts Number.
  for (const key of ["posX", "posY", "width", "height", "size", "offsetX", "offsetY", "spacing", "columns", "rows", "zIndex"]) {
    if (typeof result[key] === "number" && data.type !== "GridElementData") result[key] = Math.round(result[key]);
  }
  return result;
}

export function createPreviewMessage(components: PlaygroundData[], width: number, height: number, color: string) {
  return {
    container: "bgw-root",
    props: { data: {
      type: "AppData", width, height,
      gameScene: {
        type: "SceneData", id: "playground-scene", width, height,
        background: { type: "ColorVisualData", id: "scene-background", transparency: 1, color },
        components: components.map(toPreviewData),
      },
    } },
  };
}
