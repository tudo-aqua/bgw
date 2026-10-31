import { DataClass, DataClasses, Droppable, generateId, TextVisualData } from "./components";

type Spec = {
  cls: string;
  name?: string;
  props?: Record<string, any>;
  fill?: string;
  transparent?: boolean;
  alpha?: number;
  caption?: string;
  fontColor?: string;
  fontSize?: number;
  face?: { text: string; fill: string; color: string; back: string };
  children?: Spec[];
};

export type PlaygroundTemplate = {
  id: string;
  name: string;
  description: string;
  icon: string;
  width: number;
  height: number;
  background: string;
  tree: Spec[];
};

export type TemplateWorkspace = {
  components: DataClass[];
  treeData: { id: string; parent: string | number; droppable: boolean; text: string }[];
};

// Templates are authored at 1280x720 and scaled up to the 1920x1080 default scene.
const SCALE = 1.5;
const SCALED = new Set(["posX", "posY", "width", "height", "size", "spacing"]);

function instantiate(spec: Spec): DataClass {
  const component: any = new DataClasses[spec.cls]();
  if (spec.name) component.name.value = spec.name;
  for (const [key, value] of Object.entries(spec.props ?? {})) {
    const scaled = typeof value === "number" && SCALED.has(key) ? value * SCALE : value;
    if (component[key]?.propType) component[key].value = scaled;
    else component[key] = value;
  }
  const base = component.visual;
  if (component.vis_0) component.vis_0.children = base.children;
  if (spec.fill) {
    base.children[0].color = spec.fill;
    base.children[0].transparency.value = spec.alpha ?? 1;
  }
  if (spec.transparent) base.children[0].transparency.value = 0;
  if (spec.fontColor) component.font.color.value = spec.fontColor;
  if (spec.fontSize) component.font.size.value = spec.fontSize * SCALE;
  if (spec.caption) {
    const text = new TextVisualData();
    text.text = spec.caption;
    text.font.color.value = "#111827";
    text.font.size.value = 64 * SCALE;
    base.children.push(text);
  }
  if (spec.face) {
    const { text, fill, color, back } = spec.face;
    const front = component.front.children;
    front[0].color = fill;
    const caption = new TextVisualData();
    caption.text = text;
    caption.font.color.value = color;
    caption.font.size.value = 48 * SCALE;
    caption.font.fontWeight.value = "700";
    front.push(caption);
    component.back.children[0].color = back;
  }
  return component;
}

export function buildTemplate(template: PlaygroundTemplate): TemplateWorkspace {
  const components: DataClass[] = [];
  const treeData: TemplateWorkspace["treeData"] = [];
  const visit = (spec: Spec, parent: string | number) => {
    const component = instantiate(spec);
    components.push(component);
    treeData.push({ id: component.id, parent, droppable: spec.cls in Droppable, text: component.name.value });
    spec.children?.forEach(child => visit(child, component.id));
  };
  template.tree.forEach(spec => visit(spec, 0));
  return { components, treeData };
}

export function newWorkspaceId() {
  return generateId();
}

const label = (name: string, text: string, x: number, y: number, w: number, h: number, size = 28, color = "#ffffff"): Spec => ({
  cls: "LabelData", name, props: { text, posX: x, posY: y, width: w, height: h }, transparent: true, fontColor: color, fontSize: size,
});

const card = (name: string, text: string, fill: string, x: number, y: number, color = "#111827"): Spec => ({
  cls: "CardViewData", name, props: { posX: x, posY: y, width: 130, height: 200 }, face: { text, fill, color, back: "#1d4ed8" },
});

const tile = (row: number, column: number, name: string, fill: string): Spec => ({
  cls: "HexagonGridElementData", props: { row, column },
  children: [{ cls: "HexagonViewData", name, fill, props: { size: 90 } }],
});

const cell = (row: number, column: number, text: string): Spec => ({
  cls: "GridElementData", props: { row, column },
  children: [{ cls: "LabelData", name: `Cell ${row},${column}`, fill: (row + column) % 2 ? "#e2e8f0" : "#cbd5e1", fontColor: text === "X" ? "#dc2626" : "#2563eb", fontSize: 72, props: { width: 150, height: 150, text } }],
});

export const templates: PlaygroundTemplate[] = [
  {
    id: "hex-board",
    name: "Hex board",
    description: "A HexagonGrid with terrain tiles, the base of a Catan-style map.",
    icon: "hexagon",
    width: 1920,
    height: 1080,
    background: "#1e3a5f",
    tree: [
      label("Title", "Island of Hexes", 40, 24, 400, 48, 32),
      {
        cls: "HexagonGridData", name: "Map", props: { posX: 380, posY: 120, width: 640, height: 520 },
        children: [
          tile(0, 0, "Forest", "#166534"), tile(0, 1, "Hills", "#b45309"), tile(0, 2, "Fields", "#eab308"),
          tile(1, 0, "Mountains", "#6b7280"), tile(1, 1, "Desert", "#fcd34d"), tile(1, 2, "Pasture", "#84cc16"),
          tile(2, 0, "Fields", "#eab308"), tile(2, 1, "Forest", "#166534"), tile(2, 2, "Hills", "#b45309"),
        ],
      },
    ],
  },
  {
    id: "card-table",
    name: "Card table",
    description: "A hand of cards, a draw pile and a discard pile on a felt table.",
    icon: "playing_cards",
    width: 1920,
    height: 1080,
    background: "#14532d",
    tree: [
      label("Draw label", "Draw", 120, 100, 130, 32, 22),
      { cls: "CardStackData", name: "Draw pile", fill: "#000000", alpha: 0.25, props: { posX: 120, posY: 140, width: 130, height: 200 }, children: [
        { cls: "CardViewData", name: "Top card", props: { current: "Back", width: 130, height: 200 }, face: { text: "", fill: "#ffffff", color: "#111827", back: "#1d4ed8" } },
      ] },
      label("Discard label", "Discard", 1030, 100, 130, 32, 22),
      { cls: "CardStackData", name: "Discard pile", fill: "#000000", alpha: 0.25, props: { posX: 1030, posY: 140, width: 130, height: 200 }, children: [
        card("Seven of hearts", "7♥", "#ffffff", 0, 0, "#dc2626"),
      ] },
      {
        cls: "AreaData", name: "Hand", props: { posX: 290, posY: 450, width: 700, height: 220 },
        children: [
          card("Ace of spades", "A♠", "#ffffff", 0, 10), card("King of hearts", "K♥", "#ffffff", 140, 10, "#dc2626"),
          card("Ten of clubs", "10♣", "#ffffff", 280, 10), card("Queen of diamonds", "Q♦", "#ffffff", 420, 10, "#dc2626"),
          card("Two of spades", "2♠", "#ffffff", 560, 10),
        ],
      },
    ],
  },
  {
    id: "dice-tokens",
    name: "Dice & tokens",
    description: "Two dice and a row of player tokens in LinearLayouts.",
    icon: "casino",
    width: 1920,
    height: 1080,
    background: "#7c2d12",
    tree: [
      label("Caption", "Roll to move!", 440, 60, 400, 56, 40),
      {
        cls: "LinearLayoutData", name: "Dice tray", fill: "#000000", alpha: 0.25, props: { posX: 390, posY: 240, width: 500, height: 140, spacing: 24, orientation: "Horizontal" },
        children: [
          { cls: "DiceViewData", name: "Die 1", caption: "⚄", props: { width: 100, height: 100 }, fill: "#ffffff" },
          { cls: "DiceViewData", name: "Die 2", caption: "⚂", props: { width: 100, height: 100 }, fill: "#ffffff" },
        ],
      },
      {
        cls: "LinearLayoutData", name: "Player tokens", fill: "#000000", alpha: 0.25, props: { posX: 390, posY: 440, width: 500, height: 100, spacing: 24 },
        children: ["#ef4444", "#3b82f6", "#facc15", "#a855f7"].map((fill, index): Spec => ({
          cls: "TokenViewData", name: `Player ${index + 1}`, fill, props: { width: 64, height: 64 },
        })),
      },
    ],
  },
  {
    id: "tic-tac-toe",
    name: "Tic-tac-toe",
    description: "A 3×3 GridPane board: the simplest grid-based game.",
    icon: "grid_3x3",
    width: 1920,
    height: 1080,
    background: "#0f172a",
    tree: [
      label("Turn", "X to move", 440, 40, 400, 56, 36),
      {
        cls: "GridPaneData", name: "Board", props: { posX: 640, posY: 400, width: 470, height: 470, columns: 3, rows: 3, spacing: 10 },
        children: [
          cell(0, 0, "X"), cell(0, 1, "O"), cell(0, 2, ""),
          cell(1, 0, ""), cell(1, 1, "X"), cell(1, 2, "O"),
          cell(2, 0, ""), cell(2, 1, ""), cell(2, 2, "X"),
        ],
      },
    ],
  },
];
