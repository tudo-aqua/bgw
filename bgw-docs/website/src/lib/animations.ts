import type { CSSProperties } from "react";
import { PlaygroundData, toPreviewData } from "./playground";

export type Interpolation = "LINEAR" | "SMOOTH" | "SPRING" | "STEPS";
export type AnimationKind = "movement" | "rotation" | "scale" | "fade" | "flip" | "randomize" | "dice" | "delay" | "sequence" | "parallel";
/** A visual an animation switches to: the component's own one, one side of a card, or a plain color. */
export type VisualSpec = { source: "own" | "front" | "back" | "color"; color?: string };

export type AnimationNode = {
  id: string;
  kind: AnimationKind;
  /** Id of the animated component. Groups and delays have none. */
  target?: string;
  duration: number;
  interpolation?: Interpolation;
  byX?: number;
  byY?: number;
  /** Rotation, scale and fade start at the component's current value when `from*` is left empty. */
  fromAngle?: number;
  toAngle?: number;
  fromScaleX?: number;
  fromScaleY?: number;
  toScaleX?: number;
  toScaleY?: number;
  fromOpacity?: number;
  toOpacity?: number;
  fromVisual?: VisualSpec;
  toVisual?: VisualSpec;
  visuals?: VisualSpec[];
  toSide?: number;
  speed?: number;
  children?: AnimationNode[];
};
export type AnimationClip = { id: string; name: string; root: AnimationNode };
export type Snapshot = Record<string, PlaygroundData>;

export const INTERPOLATIONS: Interpolation[] = ["LINEAR", "SMOOTH", "SPRING", "STEPS"];
export const DEFAULT_SPEED = 50;

export type AnimationCategory = "transform" | "visual" | "timing" | "composition";
/** Groups of the Add menu. Each has its own color in the timeline (a `--bgw-*` theme token). */
export const CATEGORIES: Record<AnimationCategory, { label: string; color: string }> = {
  transform: { label: "Transform", color: "--bgw-blue" },
  visual: { label: "Visual", color: "--bgw-purple" },
  timing: { label: "Timing", color: "--bgw-gray" },
  composition: { label: "Composition", color: "--bgw-green" },
};

/** `label` is the BGW class name the node exports to. */
type Meta = { label: string; icon: string; description: string; category: AnimationCategory };
export const KINDS: Record<AnimationKind, Meta> = {
  movement: { label: "MovementAnimation", icon: "open_with", description: "Moves a component by a relative offset.", category: "transform" },
  rotation: { label: "RotationAnimation", icon: "rotate_right", description: "Rotates a component.", category: "transform" },
  scale: { label: "ScaleAnimation", icon: "zoom_out_map", description: "Scales a component.", category: "transform" },
  fade: { label: "FadeAnimation", icon: "opacity", description: "Changes the opacity of a component.", category: "visual" },
  flip: { label: "FlipAnimation", icon: "flip", description: "Flips a card, token or hexagon from one visual to another.", category: "visual" },
  randomize: { label: "RandomizeAnimation", icon: "shuffle", description: "Shuffles through visuals before settling on one.", category: "visual" },
  dice: { label: "DiceAnimation", icon: "casino", description: "Rolls a dice view to a side.", category: "visual" },
  delay: { label: "DelayAnimation", icon: "timer", description: "Waits without changing anything.", category: "timing" },
  sequence: { label: "SequentialAnimation", icon: "playlist_play", description: "Plays its animations one after another.", category: "composition" },
  parallel: { label: "ParallelAnimation", icon: "view_column", description: "Plays its animations at the same time.", category: "composition" },
};
/** Sets `--kind` to the kind's theme token, so CSS can tint with `hsl(var(--kind) / alpha)`. */
export const kindStyle = (kind: AnimationKind) => ({ "--kind": `var(${CATEGORIES[KINDS[kind].category].color})` }) as CSSProperties;
/** The theme color of a kind, e.g. `hsl(var(--bgw-blue))`, optionally with an alpha. */
export const colorOf = (kind: AnimationKind, alpha?: number) => `hsl(var(${CATEGORIES[KINDS[kind].category].color})${alpha === undefined ? "" : ` / ${alpha}`})`;
export const isGroup = (node: AnimationNode) => node.kind === "sequence" || node.kind === "parallel";
export const needsTarget = (kind: AnimationKind) => kind !== "delay" && kind !== "sequence" && kind !== "parallel";
export const hasInterpolation = (kind: AnimationKind) => ["movement", "rotation", "scale", "fade"].includes(kind);
export const isGameComponent = (data?: PlaygroundData) => !!data && (data.objectType?.includes("GameComponentViewData") ?? false);

let counter = 0;
export const newId = (prefix = "anim") => `${prefix}-${Date.now().toString(36)}-${(counter++).toString(36)}-${Math.random().toString(36).slice(2, 6)}`;

export function createNode(kind: AnimationKind, target?: string): AnimationNode {
  const node: AnimationNode = { id: newId(), kind, duration: 1000 };
  if (needsTarget(kind)) node.target = target;
  if (hasInterpolation(kind)) node.interpolation = "SMOOTH";
  if (kind === "movement") { node.byX = 100; node.byY = 0; }
  if (kind === "rotation") node.toAngle = 90;
  if (kind === "scale") { node.toScaleX = 1.5; node.toScaleY = 1.5; }
  if (kind === "fade") node.toOpacity = 0.25;
  if (kind === "flip") { node.fromVisual = { source: "own" }; node.toVisual = { source: "color", color: "#3b82f6" }; node.duration = 600; }
  if (kind === "randomize") { node.visuals = [{ source: "color", color: "#ef4444" }, { source: "color", color: "#22c55e" }, { source: "color", color: "#3b82f6" }]; node.toVisual = { source: "own" }; node.speed = DEFAULT_SPEED; }
  if (kind === "dice") { node.toSide = 0; node.speed = DEFAULT_SPEED; }
  if (isGroup(node)) node.children = [];
  return node;
}

export function createClip(name: string): AnimationClip {
  return { id: newId("clip"), name, root: createNode("sequence") };
}

export function mapNode(node: AnimationNode, id: string, change: (node: AnimationNode) => AnimationNode | null): AnimationNode | null {
  if (node.id === id) return change(node);
  if (!node.children) return node;
  return { ...node, children: node.children.flatMap(child => { const next = mapNode(child, id, change); return next ? [next] : []; }) };
}
export function findNode(node: AnimationNode, id: string): AnimationNode | null {
  if (node.id === id) return node;
  for (const child of node.children ?? []) { const found = findNode(child, id); if (found) return found; }
  return null;
}
export function findParent(node: AnimationNode, id: string): AnimationNode | null {
  for (const child of node.children ?? []) {
    if (child.id === id) return node;
    const found = findParent(child, id);
    if (found) return found;
  }
  return null;
}
export function cloneNode(node: AnimationNode): AnimationNode {
  return { ...structuredClone(node), id: newId(), children: node.children?.map(cloneNode) };
}

export type Timing = { start: number; end: number };
/** Start and end of every node in ms, following how sequences and parallel groups play. */
export function layout(root: AnimationNode): { timings: Map<string, Timing>; total: number } {
  const timings = new Map<string, Timing>();
  function place(node: AnimationNode, start: number): number {
    let end = start + (isGroup(node) ? 0 : Math.max(0, node.duration));
    if (node.kind === "sequence") { let cursor = start; for (const child of node.children ?? []) cursor = place(child, cursor); end = cursor; }
    if (node.kind === "parallel") { end = start; for (const child of node.children ?? []) end = Math.max(end, place(child, start)); }
    timings.set(node.id, { start, end });
    return end;
  }
  const total = place(root, 0);
  return { timings, total };
}

/** Why a node cannot be played or exported, if so. */
export function problemOf(node: AnimationNode, snapshot: Snapshot): string | null {
  if (!needsTarget(node.kind)) return null;
  const data = node.target ? snapshot[node.target] : undefined;
  if (!node.target) return "Pick a component to animate.";
  if (!data) return "The animated component is no longer in the scene.";
  if (["flip", "randomize"].includes(node.kind) && !isGameComponent(data)) return "Only card, dice, hexagon and token views can change their visual.";
  if (node.kind === "dice" && data.type !== "DiceViewData") return "Pick a dice view.";
  if (node.kind === "dice" && (node.toSide ?? 0) >= (data.visuals?.length ?? 0)) return `The dice only has ${data.visuals?.length ?? 0} sides.`;
  return null;
}

export function problemsOf(root: AnimationNode, snapshot: Snapshot): string[] {
  const problems: string[] = [];
  (function walk(node: AnimationNode) {
    const problem = problemOf(node, snapshot);
    if (problem) problems.push(`${KINDS[node.kind].label}: ${problem}`);
    node.children?.forEach(walk);
  })(root);
  return problems;
}

/** Component targets an animation of this kind can use. */
export function validTarget(kind: AnimationKind, data: PlaygroundData) {
  if (kind === "flip" || kind === "randomize") return isGameComponent(data);
  if (kind === "dice") return data.type === "DiceViewData";
  return true;
}

function resolveVisual(spec: VisualSpec | undefined, data: PlaygroundData): any {
  const color = (value: string) => ({ type: "CompoundVisualData", id: newId("vis"), children: [{ type: "ColorVisualData", id: newId("vis"), transparency: 1, color: value }] });
  if (!spec || spec.source === "own") return toPreviewData(data.type === "CardViewData" ? data.currentVisual ?? data.front : data.visual) ?? color("#00000000");
  if (spec.source === "front" && data.front) return toPreviewData(data.front);
  if (spec.source === "back" && data.back) return toPreviewData(data.back);
  if (spec.source === "front" || spec.source === "back") return resolveVisual({ source: "own" }, data);
  return color(spec.color ?? "#000000");
}

const DATA_TYPES: Record<string, string> = {
  movement: "MovementAnimationData", rotation: "RotationAnimationData", scale: "ScaleAnimationData", fade: "FadeAnimationData",
  flip: "FlipAnimationData", randomize: "RandomizeAnimationData", dice: "DiceAnimationData", delay: "DelayAnimationData",
  sequence: "SequentialAnimationData", parallel: "ParallelAnimationData",
};
const ANIMATION_TYPE: Record<string, string> = { movement: "move", rotation: "rotate", scale: "scale", fade: "fade", flip: "flip", randomize: "stepped", dice: "stepped", delay: "delay" };

/** The JSON message the BGW renderer reads for this animation (see the Animations guide). Nodes with problems are left out. */
export function toWire(node: AnimationNode, snapshot: Snapshot): any | null {
  if (problemOf(node, snapshot)) return null;
  const data = node.target ? snapshot[node.target] : undefined;
  const wire: any = { type: `data.animation.${DATA_TYPES[node.kind]}`, id: node.id, duration: Math.max(0, Math.round(node.duration)) };
  if (isGroup(node)) {
    wire.animations = (node.children ?? []).flatMap(child => { const item = toWire(child, snapshot); return item ? [item] : []; });
    return wire;
  }
  wire.animationType = ANIMATION_TYPE[node.kind];
  if (!data) return wire;
  wire.componentView = toPreviewData(data);
  if (node.interpolation && hasInterpolation(node.kind)) wire.interpolation = node.interpolation;
  const current = (key: string, fallback: number) => Number.isFinite(Number(data[key])) ? Number(data[key]) : fallback;
  switch (node.kind) {
    case "movement": wire.byX = Math.round(node.byX ?? 0); wire.byY = Math.round(node.byY ?? 0); break;
    case "rotation": wire.fromAngle = node.fromAngle ?? current("rotation", 0); wire.toAngle = node.toAngle ?? current("rotation", 0); break;
    case "scale":
      wire.fromScaleX = node.fromScaleX ?? current("scaleX", 1); wire.toScaleX = node.toScaleX ?? current("scaleX", 1);
      wire.fromScaleY = node.fromScaleY ?? current("scaleY", 1); wire.toScaleY = node.toScaleY ?? current("scaleY", 1); break;
    case "fade": wire.fromOpacity = node.fromOpacity ?? current("opacity", 1); wire.toOpacity = node.toOpacity ?? current("opacity", 1); break;
    case "flip": wire.fromVisual = resolveVisual(node.fromVisual, data); wire.toVisual = resolveVisual(node.toVisual, data); break;
    case "randomize":
      wire.visuals = (node.visuals?.length ? node.visuals : [{ source: "own" as const }]).map(spec => resolveVisual(spec, data));
      wire.toVisual = resolveVisual(node.toVisual, data); wire.speed = node.speed ?? DEFAULT_SPEED; break;
    case "dice": wire.toSide = node.toSide ?? 0; wire.speed = node.speed ?? DEFAULT_SPEED; break;
  }
  return wire;
}

/** `{ "data": … }` messages exactly like the ones the documentation previews send. */
export function toMessage(clip: AnimationClip, snapshot: Snapshot, container = "bgw-root") {
  const data = toWire(clip.root, snapshot);
  return data ? { container, props: { data } } : null;
}

export function exportJson(clips: AnimationClip[], snapshot: Snapshot): string {
  return JSON.stringify(Object.fromEntries(clips.map(clip => [clip.name, { data: toWire(clip.root, snapshot) }])), null, 2);
}

// ---- Kotlin ----

const number = (value: number, double = false) => Number.isInteger(value) && double ? `${value}.0` : String(value);
const hexColor = (value: string) => {
  const hex = /^#([\da-f]{6})([\da-f]{2})?$/i.exec(value);
  if (!hex) return "Color(0x000000)";
  return hex[2]
    ? `Color(${parseInt(hex[1].slice(0, 2), 16)}, ${parseInt(hex[1].slice(2, 4), 16)}, ${parseInt(hex[1].slice(4, 6), 16)}, ${number(parseInt(hex[2], 16) / 255 , true)})`
    : `Color(0x${hex[1].toUpperCase()})`;
};
const indent = (text: string) => text.split("\n").map(line => `    ${line}`).join("\n");
const call = (name: string, args: string[]) => args.length ? `${name}(\n${indent(args.join(",\n"))}\n)` : `${name}()`;
const DURATION_DEFAULTS: Record<string, number> = { movement: 1000, rotation: 1000, randomize: 1000, dice: 1000, delay: 1000 };

function visualCode(spec: VisualSpec | undefined, name: string, data: PlaygroundData) {
  if (spec?.source === "color") return `ColorVisual(${hexColor(spec.color ?? "#000000")})`;
  if (data.type === "CardViewData" && spec?.source === "front") return `${name}.frontVisual`;
  if (data.type === "CardViewData" && spec?.source === "back") return `${name}.backVisual`;
  return `${name}.visual`;
}

/** Kotlin expression for an animation. `names` maps component ids to their property names in the exported scene. */
export function nodeToKotlin(node: AnimationNode, snapshot: Snapshot, names: Record<string, string>): string | null {
  if (problemOf(node, snapshot)) return null;
  const data = node.target ? snapshot[node.target] : undefined;
  if (isGroup(node)) {
    const children = (node.children ?? []).flatMap(child => { const code = nodeToKotlin(child, snapshot, names); return code ? [code] : []; });
    return call(node.kind === "sequence" ? "SequentialAnimation" : "ParallelAnimation", children);
  }
  const args: string[] = [];
  const name = node.target ? names[node.target] ?? "component" : "";
  const duration = Math.max(0, Math.round(node.duration));
  const addDuration = () => { if (duration !== DURATION_DEFAULTS[node.kind]) args.push(`duration = ${duration}`); };
  const addInterpolation = () => { if (node.interpolation && node.interpolation !== "SMOOTH") args.push(`interpolation = AnimationInterpolation.${node.interpolation}`); };
  const addValue = (key: string, value: number | undefined, current: unknown) => {
    if (value === undefined || !Number.isFinite(value)) return;
    if (data && Number(current) === value) return;
    args.push(`${key} = ${number(value, true)}`);
  };
  switch (node.kind) {
    case "delay": if (duration !== 1000) args.push(`duration = ${duration}`); return call("DelayAnimation", args);
    case "movement":
      args.push(`componentView = ${name}`);
      if (node.byX) args.push(`byX = ${Math.round(node.byX)}`);
      if (node.byY) args.push(`byY = ${Math.round(node.byY)}`);
      addDuration(); addInterpolation(); return call("MovementAnimation", args);
    case "rotation":
      args.push(`componentView = ${name}`);
      addValue("fromAngle", node.fromAngle, data!.rotation); addValue("toAngle", node.toAngle, data!.rotation);
      addDuration(); addInterpolation(); return call("RotationAnimation", args);
    case "scale":
      args.push(`componentView = ${name}`);
      addValue("fromScaleX", node.fromScaleX, data!.scaleX); addValue("toScaleX", node.toScaleX, data!.scaleX);
      addValue("fromScaleY", node.fromScaleY, data!.scaleY); addValue("toScaleY", node.toScaleY, data!.scaleY);
      args.push(`duration = ${duration}`); addInterpolation(); return call("ScaleAnimation", args);
    case "fade":
      args.push(`componentView = ${name}`);
      addValue("fromOpacity", node.fromOpacity, data!.opacity); addValue("toOpacity", node.toOpacity, data!.opacity);
      args.push(`duration = ${duration}`); addInterpolation(); return call("FadeAnimation", args);
    case "flip":
      args.push(`gameComponentView = ${name}`, `fromVisual = ${visualCode(node.fromVisual, name, data!)}`, `toVisual = ${visualCode(node.toVisual, name, data!)}`, `duration = ${duration}`);
      return call("FlipAnimation", args);
    case "randomize":
      args.push(`gameComponentView = ${name}`, `visuals = ${call("listOf", (node.visuals ?? []).map(spec => visualCode(spec, name, data!)))}`, `toVisual = ${visualCode(node.toVisual, name, data!)}`);
      addDuration(); if ((node.speed ?? DEFAULT_SPEED) !== DEFAULT_SPEED) args.push(`speed = ${node.speed}`); return call("RandomizeAnimation", args);
    case "dice":
      args.push(`dice = ${name}`, `toSide = ${node.toSide ?? 0}`);
      addDuration(); if ((node.speed ?? DEFAULT_SPEED) !== DEFAULT_SPEED) args.push(`speed = ${node.speed}`); return call("DiceAnimation", args);
  }
  return null;
}

export const animationImports = ["tools.aqua.bgw.animation.*", "tools.aqua.bgw.core.AnimationInterpolation"].map(pkg => `import ${pkg}`).join("\n");

/** One `val` per clip, to be placed in the exported scene class after the component properties. */
export function clipsToKotlin(clips: AnimationClip[], snapshot: Snapshot, names: Record<string, string>, propertyName: (name: string) => string): string[] {
  const used = new Set<string>();
  return clips.flatMap(clip => {
    const code = nodeToKotlin(clip.root, snapshot, names);
    if (!code) return [];
    const base = propertyName(clip.name);
    let name = base;
    for (let index = 2; used.has(name); index++) name = `${base}${index}`;
    used.add(name);
    return [`val ${name} = ${code}`];
  });
}
