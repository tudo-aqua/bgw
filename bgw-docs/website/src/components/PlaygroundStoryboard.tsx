import { useEffect, useLayoutEffect, useMemo, useRef, useState } from "react";
import { createPortal } from "react-dom";
import { renderToStaticMarkup } from "react-dom/server";
import { Button } from "./ui/button";
import { downloadUrl } from "@/lib/sceneImage";

type Rect = { x: number; y: number; w: number; h: number };
type Point = { x: number; y: number };
type Side = "left" | "right" | "top" | "bottom";
type Card = { id: string; name: string; x: number; y: number; w: number; h: number; scale: number; thumbnail?: string; background: string; links: Link[] };
type Link = { rect: Rect; to: string; label: string };
type Palette = { background: string | null; text: string; muted: string; arrow: string; card: string; cardActive: string; pill: string; placeholder: string };

const CARD_WIDTH = 640;
const TITLE_HEIGHT = 48;
const GAP_X = 300;
const GAP_Y = 220;
const MARGIN = 120;
const EXIT = "exit";
const EXIT_SIZE = { w: 380, h: 90 };
const CLEARANCE = 36; // distance arrows keep from scenes
const SLOT = 40; // spacing between arrows that share a side
const CORNER = 18;
const BEND_COST = 180;
const MIN_SCALE = 0.08;
const MAX_SCALE = 2;
const AUTOSCROLL_EDGE = 56;
const FONT = "'JetBrains Mono', ui-monospace, Consolas, monospace";
const MANAGED_PARENTS = ["LinearLayoutData", "CardStackData", "SatchelData", "GridElementData", "HexagonGridElementData"];

const DARK: Palette = { background: null, text: "#e2e8f0", muted: "#94a3b8", arrow: "#a78bfa", card: "#475569", cardActive: "#8b5cf6", pill: "#1e293b", placeholder: "#1e293b" };
const LIGHT: Palette = { background: "#ffffff", text: "#0f172a", muted: "#64748b", arrow: "#0f172a", card: "#0f172a", cardActive: "#0f172a", pill: "#eef0f2", placeholder: "#e5e7eb" };

const number = (value: any, fallback = 0) => {
  const parsed = Number(value?.value ?? value);
  return Number.isFinite(parsed) ? parsed : fallback;
};

function linksOf(record: any): Link[] {
  const components = new Map<string, any>();
  record.components?.forEach((component: any) => components.set(component.id, component));
  const parentOf = (id: string) => record.treeData?.find((node: any) => node.id === id)?.parent ?? 0;
  const rectOf = (id: string): Rect => {
    const chain: string[] = [];
    for (let current: any = id; current && current !== 0; current = parentOf(current)) chain.unshift(current);
    let x = 0, y = 0;
    let rect: Rect = { x: 0, y: 0, w: 0, h: 0 };
    for (let index = 0; index < chain.length; index++) {
      const component = components.get(chain[index]);
      if (!component) continue;
      const parent = index > 0 ? components.get(chain[index - 1]) : null;
      // Children of layouts have no position of their own: point at the layout instead.
      if (parent && MANAGED_PARENTS.includes(parent.type)) break;
      if (component.posX) { x += number(component.posX); y += number(component.posY); }
      rect = { x, y, w: number(component.width), h: number(component.height) };
    }
    return rect;
  };
  const links: Link[] = [];
  components.forEach(component => {
    if (!component.navigateTo) return;
    const text = component.text?.value;
    links.push({ rect: rectOf(component.id), to: component.navigateTo, label: component.navigateLabel || (text ? `[${text}]` : "[Click]") });
  });
  return links;
}

function sideToward(rect: Rect, toward: Point): Side {
  const center = { x: rect.x + rect.w / 2, y: rect.y + rect.h / 2 };
  const dx = toward.x - center.x, dy = toward.y - center.y;
  if (Math.abs(dx) * rect.h >= Math.abs(dy) * rect.w) return dx >= 0 ? "right" : "left";
  return dy >= 0 ? "bottom" : "top";
}

const NORMAL: Record<Side, Point> = { left: { x: -1, y: 0 }, right: { x: 1, y: 0 }, top: { x: 0, y: -1 }, bottom: { x: 0, y: 1 } };

function anchorOn(rect: Rect, side: Side, slot: number, count: number): Point {
  const shift = (slot - (count - 1) / 2) * SLOT;
  const clamp = (value: number, low: number, high: number) => Math.min(Math.max(value, low + 20), high - 20);
  if (side === "left" || side === "right") return { x: side === "left" ? rect.x : rect.x + rect.w, y: clamp(rect.y + rect.h / 2 + shift, rect.y, rect.y + rect.h) };
  return { x: clamp(rect.x + rect.w / 2 + shift, rect.x, rect.x + rect.w), y: side === "top" ? rect.y : rect.y + rect.h };
}

/** Orthogonal routing on the grid spanned by all obstacle edges, so arrows wrap around scenes instead of crossing them. */
function createRouter(obstacles: Rect[], stubs: Point[], bounds: Rect) {
  const blockers = obstacles.map(o => ({ x: o.x - CLEARANCE, y: o.y - CLEARANCE, w: o.w + CLEARANCE * 2, h: o.h + CLEARANCE * 2 }));
  const coordinates = (values: number[]) => {
    const sorted = [...new Set(values.map(value => Math.round(value * 100) / 100))].sort((a, b) => a - b);
    // Midpoints let arrows run through the middle of the gaps between scenes.
    const middles = sorted.slice(1).map((value, index) => (value + sorted[index]) / 2);
    return [...sorted, ...middles].sort((a, b) => a - b);
  };
  const X = coordinates([bounds.x, bounds.x + bounds.w, ...stubs.map(p => p.x), ...blockers.flatMap(b => [b.x, b.x + b.w])]);
  const Y = coordinates([bounds.y, bounds.y + bounds.h, ...stubs.map(p => p.y), ...blockers.flatMap(b => [b.y, b.y + b.h])]);
  const inside = (x: number, y: number) => blockers.some(b => x > b.x + 0.01 && x < b.x + b.w - 0.01 && y > b.y + 0.01 && y < b.y + b.h - 0.01);
  const columns = X.length, rows = Y.length;
  const free = (i: number, j: number) => !inside(X[i], Y[j]);
  const nodeFree = Array.from({ length: columns * rows }, (_, index) => free(index % columns, Math.floor(index / columns)));
  const stepOk = (i: number, j: number, ni: number, nj: number) => nodeFree[nj * columns + ni] && !inside((X[i] + X[ni]) / 2, (Y[j] + Y[nj]) / 2);
  const nearestIndex = (values: number[], target: number) => values.reduce((best, value, index) => Math.abs(value - target) < Math.abs(values[best] - target) ? index : best, 0);
  const DIRECTIONS = [[1, 0], [-1, 0], [0, 1], [0, -1]];

  return (from: Point, to: Point): Point[] | null => {
    const start = { i: nearestIndex(X, from.x), j: nearestIndex(Y, from.y) };
    const goal = { i: nearestIndex(X, to.x), j: nearestIndex(Y, to.y) };
    const key = (i: number, j: number, d: number) => (j * columns + i) * 5 + d;
    const best = new Map<number, number>();
    const previous = new Map<number, number>();
    const heap: { cost: number; estimate: number; i: number; j: number; d: number }[] = [];
    const push = (item: (typeof heap)[number]) => {
      heap.push(item);
      for (let child = heap.length - 1; child > 0;) {
        const parent = (child - 1) >> 1;
        if (heap[parent].estimate <= heap[child].estimate) break;
        [heap[parent], heap[child]] = [heap[child], heap[parent]];
        child = parent;
      }
    };
    const pop = () => {
      const top = heap[0];
      const last = heap.pop()!;
      if (heap.length) {
        heap[0] = last;
        for (let parent = 0;;) {
          let smallest = parent;
          for (const child of [parent * 2 + 1, parent * 2 + 2]) if (child < heap.length && heap[child].estimate < heap[smallest].estimate) smallest = child;
          if (smallest === parent) break;
          [heap[parent], heap[smallest]] = [heap[smallest], heap[parent]];
          parent = smallest;
        }
      }
      return top;
    };
    const heuristic = (i: number, j: number) => Math.abs(X[i] - X[goal.i]) + Math.abs(Y[j] - Y[goal.j]);
    push({ cost: 0, estimate: heuristic(start.i, start.j), ...start, d: 4 });
    best.set(key(start.i, start.j, 4), 0);
    while (heap.length) {
      const current = pop();
      if (current.cost > (best.get(key(current.i, current.j, current.d)) ?? Infinity)) continue;
      if (current.i === goal.i && current.j === goal.j) {
        const points: Point[] = [];
        for (let state: number | undefined = key(current.i, current.j, current.d); state !== undefined; state = previous.get(state)) {
          const node = Math.floor(state / 5);
          points.unshift({ x: X[node % columns], y: Y[Math.floor(node / columns)] });
        }
        return points;
      }
      DIRECTIONS.forEach(([di, dj], d) => {
        const ni = current.i + di, nj = current.j + dj;
        if (ni < 0 || nj < 0 || ni >= columns || nj >= rows || !stepOk(current.i, current.j, ni, nj)) return;
        const cost = current.cost + Math.abs(X[ni] - X[current.i]) + Math.abs(Y[nj] - Y[current.j]) + (current.d !== 4 && current.d !== d ? BEND_COST : 0);
        const next = key(ni, nj, d);
        if (cost >= (best.get(next) ?? Infinity)) return;
        best.set(next, cost);
        previous.set(next, key(current.i, current.j, current.d));
        push({ cost, estimate: cost + heuristic(ni, nj), i: ni, j: nj, d });
      });
    }
    return null;
  };
}

function simplify(points: Point[]): Point[] {
  const result: Point[] = [];
  for (const point of points) {
    const last = result[result.length - 1];
    if (last && last.x === point.x && last.y === point.y) continue;
    const before = result[result.length - 2];
    if (before && last && ((before.x === last.x && last.x === point.x) || (before.y === last.y && last.y === point.y))) result.pop();
    result.push(point);
  }
  return result;
}

function roundedPath(points: Point[]) {
  let d = `M ${points[0].x} ${points[0].y}`;
  for (let index = 1; index < points.length - 1; index++) {
    const before = points[index - 1], corner = points[index], after = points[index + 1];
    const lengthIn = Math.hypot(corner.x - before.x, corner.y - before.y), lengthOut = Math.hypot(after.x - corner.x, after.y - corner.y);
    const radiusIn = Math.min(CORNER, lengthIn / 2), radiusOut = Math.min(CORNER, lengthOut / 2);
    const a = { x: corner.x - ((corner.x - before.x) / lengthIn) * radiusIn, y: corner.y - ((corner.y - before.y) / lengthIn) * radiusIn };
    const b = { x: corner.x + ((after.x - corner.x) / lengthOut) * radiusOut, y: corner.y + ((after.y - corner.y) / lengthOut) * radiusOut };
    d += ` L ${a.x} ${a.y} Q ${corner.x} ${corner.y} ${b.x} ${b.y}`;
  }
  const last = points[points.length - 1];
  return `${d} L ${last.x} ${last.y}`;
}

type Arrow = { key: string; d: string; label: Point; text: string; rect: Rect; card: Card };

function computeArrows(cards: Card[], exit: Rect, hasExit: boolean, view: Rect): Arrow[] {
  const rects = new Map<string, Rect>(cards.map(card => [card.id, { x: card.x, y: card.y, w: card.w, h: card.h }]));
  if (hasExit) rects.set(EXIT, exit);
  type Draft = { key: string; card: Card; link: Link; from: Rect; to: Rect; fromSide: Side; toSide: Side; fromId: string; toId: string };
  const drafts: Draft[] = [];
  cards.forEach(card => card.links.forEach((link, index) => {
    const target = rects.get(link.to);
    if (!target || link.to === card.id) return;
    const from = rects.get(card.id)!;
    const origin = { x: card.x + (link.rect.x + link.rect.w / 2) * card.scale, y: card.y + (link.rect.y + link.rect.h / 2) * card.scale };
    drafts.push({
      key: `${card.id}-${index}`, card, link, from, to: target, fromId: card.id, toId: link.to,
      fromSide: sideToward(from, { x: target.x + target.w / 2, y: target.y + target.h / 2 }),
      toSide: sideToward(target, origin),
    });
  }));
  // Arrows sharing a side of a scene fan out instead of overlapping.
  const slots = new Map<string, Draft[]>();
  const slotKey = (id: string, side: Side) => `${id}:${side}`;
  drafts.forEach(draft => {
    for (const key of [slotKey(draft.fromId, draft.fromSide), slotKey(draft.toId, draft.toSide)]) slots.set(key, [...(slots.get(key) ?? []), draft]);
  });
  const placed = drafts.map(draft => {
    const outgoing = slots.get(slotKey(draft.fromId, draft.fromSide))!, incoming = slots.get(slotKey(draft.toId, draft.toSide))!;
    const start = anchorOn(draft.from, draft.fromSide, outgoing.indexOf(draft), outgoing.length);
    const end = anchorOn(draft.to, draft.toSide, incoming.indexOf(draft), incoming.length);
    const out = NORMAL[draft.fromSide], into = NORMAL[draft.toSide];
    // A scene's title sits above its top edge, so arrows leave and arrive above it.
    const reach = (id: string, side: Side) => CLEARANCE + (side === "top" && id !== EXIT ? TITLE_HEIGHT : 0);
    const outReach = reach(draft.fromId, draft.fromSide), inReach = reach(draft.toId, draft.toSide);
    return { draft, start, end, stubStart: { x: start.x + out.x * outReach, y: start.y + out.y * outReach }, stubEnd: { x: end.x + into.x * inReach, y: end.y + into.y * inReach } };
  });
  const obstacles: Rect[] = cards.map(card => ({ x: card.x, y: card.y - TITLE_HEIGHT, w: card.w, h: card.h + TITLE_HEIGHT }));
  if (hasExit) obstacles.push(exit);
  const route = createRouter(obstacles, placed.flatMap(item => [item.stubStart, item.stubEnd]), view);
  return placed.map(({ draft, start, end, stubStart, stubEnd }) => {
    const found = route(stubStart, stubEnd);
    const middle = found ?? [stubStart, draft.fromSide === "left" || draft.fromSide === "right" ? { x: (stubStart.x + stubEnd.x) / 2, y: stubStart.y } : { x: stubStart.x, y: (stubStart.y + stubEnd.y) / 2 }, stubEnd];
    const points = simplify([start, ...middle, end]);
    let longest = 0, label = points[0];
    for (let index = 1; index < points.length; index++) {
      const length = Math.hypot(points[index].x - points[index - 1].x, points[index].y - points[index - 1].y);
      if (length > longest) { longest = length; label = { x: (points[index].x + points[index - 1].x) / 2, y: (points[index].y + points[index - 1].y) / 2 }; }
    }
    return { key: draft.key, d: roundedPath(points), label, text: draft.link.label, rect: draft.link.rect, card: draft.card };
  });
}

function Board({ cards, exit, view, palette, currentId, interactive, onDragStart, onOpen, onHide, hasExit, pixelsPerUnit }: {
  cards: Card[];
  exit: Rect;
  hasExit: boolean;
  view: Rect;
  palette: Palette;
  currentId: string;
  interactive: boolean;
  pixelsPerUnit?: number;
  onDragStart?: (id: string, event: React.PointerEvent) => void;
  onOpen?: (id: string) => void;
  onHide?: (id: string) => void;
}) {
  const arrows = computeArrows(cards, exit, hasExit, view);
  const showsExit = hasExit && arrows.length > 0 && cards.some(card => card.links.some(link => link.to === EXIT));
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      xmlnsXlink="http://www.w3.org/1999/xlink"
      viewBox={`${view.x} ${view.y} ${view.w} ${view.h}`}
      width={view.w}
      height={view.h}
      fontFamily={FONT}
      style={pixelsPerUnit ? { width: view.w * pixelsPerUnit, height: view.h * pixelsPerUnit } : undefined}
    >
      <defs>
        <marker id="storyboard-arrow" viewBox="0 0 12 12" refX="10" refY="6" markerWidth="12" markerHeight="12" orient="auto-start-reverse">
          <path d="M 2 1 L 10 6 L 2 11" fill="none" stroke={palette.arrow} strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
        </marker>
        {cards.map(card => <clipPath key={card.id} id={`storyboard-clip-${card.id}`}><rect x={card.x} y={card.y} width={card.w} height={card.h} rx="10" /></clipPath>)}
      </defs>
      {palette.background && <rect x={view.x} y={view.y} width={view.w} height={view.h} fill={palette.background} />}
      {cards.map(card => {
        const active = interactive && card.id === currentId;
        return (
          <g
            key={card.id}
            data-card={card.id}
            style={interactive ? { cursor: "grab" } : undefined}
            onPointerDown={interactive ? event => onDragStart?.(card.id, event) : undefined}
            onDoubleClick={interactive ? () => onOpen?.(card.id) : undefined}
          >
            <text x={card.x} y={card.y - 16} fontSize="26" fontWeight="700" fill={palette.text}>{card.name}</text>
            <rect x={card.x} y={card.y} width={card.w} height={card.h} rx="10" fill={card.thumbnail ? card.background : palette.placeholder} />
            {card.thumbnail
              ? <image href={card.thumbnail} x={card.x} y={card.y} width={card.w} height={card.h} preserveAspectRatio="none" clipPath={`url(#storyboard-clip-${card.id})`} />
              : <text x={card.x + card.w / 2} y={card.y + card.h / 2} textAnchor="middle" fontSize="22" fill={palette.muted}>Rendering preview…</text>}
            <rect x={card.x} y={card.y} width={card.w} height={card.h} rx="10" fill="none" stroke={active ? palette.cardActive : palette.card} strokeWidth={active ? 6 : 5} strokeDasharray={active ? "14 8" : undefined} />
            {interactive && (
              <g
                className="playground-storyboard-hide"
                style={{ cursor: "pointer" }}
                onPointerDown={event => event.stopPropagation()}
                onClick={() => onHide?.(card.id)}
              >
                <title>Hide scene</title>
                <rect x={card.x + card.w - 44} y={card.y - 40} width="44" height="36" rx="8" fill="transparent" />
                <text x={card.x + card.w - 22} y={card.y - 12} textAnchor="middle" fontSize="28" fill={palette.muted} fontFamily="Material Symbols Rounded">visibility_off</text>
              </g>
            )}
          </g>
        );
      })}
      {showsExit && (
        <g data-card={EXIT} style={interactive ? { cursor: "grab" } : undefined} onPointerDown={interactive ? event => onDragStart?.(EXIT, event) : undefined}>
          <rect x={exit.x} y={exit.y} width={exit.w} height={exit.h} rx="14" fill={palette.pill} />
          <text x={exit.x + exit.w / 2} y={exit.y + exit.h / 2 + 8} textAnchor="middle" fontSize="26" fontWeight="700" fill={palette.text}>Program ends</text>
        </g>
      )}
      {arrows.map(arrow => (
        <g key={arrow.key}>
          <rect
            x={arrow.card.x + arrow.rect.x * arrow.card.scale}
            y={arrow.card.y + arrow.rect.y * arrow.card.scale}
            width={arrow.rect.w * arrow.card.scale}
            height={arrow.rect.h * arrow.card.scale}
            fill={palette.arrow}
            fillOpacity="0.18"
            stroke={palette.arrow}
            strokeWidth="2"
            strokeDasharray="6 4"
          />
          <path d={arrow.d} fill="none" stroke={palette.arrow} strokeWidth="4" strokeLinecap="round" strokeLinejoin="round" markerEnd="url(#storyboard-arrow)" />
          <text x={arrow.label.x} y={arrow.label.y - 14} textAnchor="middle" fontSize="26" fontWeight="700" fill={palette.text} stroke={palette.background ?? "#0f172a"} strokeWidth="8" paintOrder="stroke" strokeLinejoin="round">{arrow.text}</text>
        </g>
      ))}
    </svg>
  );
}

async function svgToPng(svg: string, width: number, height: number) {
  const url = URL.createObjectURL(new Blob([svg], { type: "image/svg+xml;charset=utf-8" }));
  try {
    const image = new Image();
    await new Promise((resolve, reject) => { image.onload = resolve; image.onerror = reject; image.src = url; });
    const canvas = document.createElement("canvas");
    canvas.width = width;
    canvas.height = height;
    canvas.getContext("2d")!.drawImage(image, 0, 0, width, height);
    return canvas.toDataURL("image/png");
  } finally {
    URL.revokeObjectURL(url);
  }
}

/** Overview of all scenes with arrows for every component that links to another scene. */
export default function PlaygroundStoryboard({ workspaces, currentId, refreshing, toolbarHost, onRefresh, onOpenScene, onMove, onToggleHidden }: {
  workspaces: any[];
  currentId: string;
  refreshing: boolean;
  /** Element in the page header that receives this view's commands. */
  toolbarHost: HTMLElement | null;
  onRefresh: () => void;
  onOpenScene: (id: string) => void;
  onMove: (id: string, position: Point) => void;
  onToggleHidden: (id: string, hidden: boolean) => void;
}) {
  const [moved, setMoved] = useState<Record<string, Point>>({});
  const [exitPosition, setExitPosition] = useState<Point | null>(null);
  const [dragging, setDragging] = useState<string | null>(null);
  const [hiddenOpen, setHiddenOpen] = useState(false);
  // Pixels per board unit. It only changes through zooming, never because the content grows.
  const [pixelsPerUnit, setPixelsPerUnit] = useState<number | null>(null);
  const [frameWidth, setFrameWidth] = useState(0);
  const boardRef = useRef<HTMLDivElement>(null);
  const startView = useRef<Rect>({ x: 0, y: 0, w: 800, h: 400 });
  const viewRef = useRef<Rect>({ x: 0, y: 0, w: 800, h: 400 });
  const previousOrigin = useRef<Point | null>(null);
  const zoomAnchor = useRef<{ fx: number; fy: number; clientX: number; clientY: number } | null>(null);
  const svg = () => boardRef.current?.querySelector("svg") ?? null;

  const hiddenScenes = workspaces.filter(record => record.hidden);
  const cards: Card[] = useMemo(() => workspaces.flatMap((record, index) => {
    if (record.hidden) return [];
    const width = record.sceneWidth ?? 1920, height = record.sceneHeight ?? 1080;
    const scale = CARD_WIDTH / width;
    const column = index % 3, row = Math.floor(index / 3);
    const home = { x: column * (CARD_WIDTH + GAP_X), y: row * (CARD_WIDTH * 0.5625 + TITLE_HEIGHT + GAP_Y) };
    const position = moved[record.id] ?? record.board ?? home;
    return [{ id: record.id, name: record.name, ...position, w: CARD_WIDTH, h: height * scale, scale, thumbnail: record.thumbnail, background: record.sceneBackground ?? "#ffffff", links: linksOf(record).filter(link => link.to === EXIT || workspaces.some(other => other.id === link.to && !other.hidden)) }];
  }), [workspaces, moved]);

  const hasExit = cards.some(card => card.links.some(link => link.to === EXIT));
  const rightmost = Math.max(0, ...cards.map(card => card.x + card.w));
  const exit: Rect = { ...(exitPosition ?? { x: rightmost + 160, y: Math.min(0, ...cards.map(card => card.y)) + 40 }), ...EXIT_SIZE };

  const boundsOf = (items: Card[], pill: Rect): Rect => {
    if (!items.length) return { x: 0, y: 0, w: 800, h: 400 };
    const xs = items.flatMap(card => [card.x, card.x + card.w]).concat(hasExit ? [pill.x, pill.x + pill.w] : []);
    const ys = items.flatMap(card => [card.y - TITLE_HEIGHT, card.y + card.h]).concat(hasExit ? [pill.y, pill.y + pill.h] : []);
    return { x: Math.min(...xs) - MARGIN, y: Math.min(...ys) - MARGIN, w: Math.max(...xs) - Math.min(...xs) + MARGIN * 2, h: Math.max(...ys) - Math.min(...ys) + MARGIN * 2 };
  };
  // While dragging, the board only grows (never shrinks), so a scene dragged past the edge keeps room around it.
  const liveView = boundsOf(cards, exit);
  if (!dragging) startView.current = liveView;
  const view: Rect = dragging
    ? (() => {
        const left = Math.min(startView.current.x, liveView.x), top = Math.min(startView.current.y, liveView.y);
        const right = Math.max(startView.current.x + startView.current.w, liveView.x + liveView.w);
        const bottom = Math.max(startView.current.y + startView.current.h, liveView.y + liveView.h);
        return { x: left, y: top, w: right - left, h: bottom - top };
      })()
    : liveView;
  viewRef.current = view;
  const fitScale = (content: Rect) => Math.min(MAX_SCALE, Math.max(MIN_SCALE, (frameWidth - 32) / content.w));
  const scale = pixelsPerUnit ?? (frameWidth ? fitScale(liveView) : 0.5);
  const anyLinks = cards.some(card => card.links.length > 0);

  useEffect(() => {
    const board = boardRef.current;
    if (!board) return;
    const observer = new ResizeObserver(() => setFrameWidth(board.clientWidth));
    observer.observe(board);
    setFrameWidth(board.clientWidth);
    return () => observer.disconnect();
  }, []);

  // Start at "fit to window" once the frame is measured.
  useEffect(() => {
    if (pixelsPerUnit === null && frameWidth > 0 && cards.length) setPixelsPerUnit(fitScale(liveView));
  }, [frameWidth, cards.length]);

  // Content that grows or shrinks on the left/top moves the origin; scroll by the same amount so nothing jumps on screen.
  useLayoutEffect(() => {
    const board = boardRef.current, before = previousOrigin.current;
    previousOrigin.current = { x: view.x, y: view.y };
    if (!board || !before) return;
    board.scrollLeft += (before.x - view.x) * scale;
    board.scrollTop += (before.y - view.y) * scale;
  }, [view.x, view.y]);

  // Zooming keeps the point under the cursor in place by adjusting the scroll position once the new size is laid out.
  const zoomTo = (next: number, clientX?: number, clientY?: number) => {
    const board = boardRef.current, element = svg();
    if (!board || !element) return;
    const rect = element.getBoundingClientRect(), frame = board.getBoundingClientRect();
    const cx = clientX ?? frame.left + frame.width / 2, cy = clientY ?? frame.top + frame.height / 2;
    zoomAnchor.current = { fx: (cx - rect.left) / rect.width, fy: (cy - rect.top) / rect.height, clientX: cx, clientY: cy };
    setPixelsPerUnit(Math.min(MAX_SCALE, Math.max(MIN_SCALE, next)));
  };
  const zoomRef = useRef(zoomTo);
  zoomRef.current = zoomTo;
  const scaleRef = useRef(scale);
  scaleRef.current = scale;

  useLayoutEffect(() => {
    const anchor = zoomAnchor.current, board = boardRef.current, element = svg();
    if (!anchor || !board || !element) return;
    zoomAnchor.current = null;
    const rect = element.getBoundingClientRect();
    board.scrollLeft += rect.left + anchor.fx * rect.width - anchor.clientX;
    board.scrollTop += rect.top + anchor.fy * rect.height - anchor.clientY;
  }, [pixelsPerUnit]);

  useEffect(() => {
    const board = boardRef.current;
    if (!board) return;
    const onWheel = (event: WheelEvent) => {
      event.preventDefault();
      zoomRef.current(scaleRef.current * Math.exp(-event.deltaY * 0.0015), event.clientX, event.clientY);
    };
    board.addEventListener("wheel", onWheel, { passive: false });
    return () => board.removeEventListener("wheel", onWheel);
  }, []);

  function startPan(event: React.PointerEvent) {
    const board = boardRef.current;
    if (!board || event.button !== 0 || (event.target as Element).closest("[data-card]")) return;
    const origin = { x: event.clientX, y: event.clientY, left: board.scrollLeft, top: board.scrollTop };
    const move = (next: PointerEvent) => {
      board.scrollLeft = origin.left - (next.clientX - origin.x);
      board.scrollTop = origin.top - (next.clientY - origin.y);
    };
    const up = () => {
      window.removeEventListener("pointermove", move);
      window.removeEventListener("pointerup", up);
    };
    window.addEventListener("pointermove", move);
    window.addEventListener("pointerup", up);
  }

  function startDrag(id: string, event: React.PointerEvent) {
    event.preventDefault();
    const element = svg(), board = boardRef.current;
    if (!element || !board) return;
    // Pointer position in board units, using the view as it is right now (it can grow while dragging).
    const toBoard = (clientX: number, clientY: number): Point => {
      const rect = (svg() ?? element).getBoundingClientRect();
      const current = viewRef.current;
      return { x: current.x + ((clientX - rect.left) / rect.width) * current.w, y: current.y + ((clientY - rect.top) / rect.height) * current.h };
    };
    const card = id === EXIT ? exit : cards.find(candidate => candidate.id === id)!;
    const grab = toBoard(event.clientX, event.clientY);
    const offset = { x: grab.x - card.x, y: grab.y - card.y };
    let latest = { x: card.x, y: card.y };
    let pointer = { x: event.clientX, y: event.clientY };
    startView.current = viewRef.current;
    setDragging(id);
    const place = () => {
      const target = toBoard(pointer.x, pointer.y);
      latest = { x: Math.round(target.x - offset.x), y: Math.round(target.y - offset.y) };
      if (id === EXIT) setExitPosition(latest);
      else setMoved(previous => ({ ...previous, [id]: latest }));
    };
    // Near the edge of the frame the board scrolls on its own, so scenes can be carried to far away places.
    let frame = 0;
    const autoScroll = () => {
      const bounds = board.getBoundingClientRect();
      const speed = (distance: number) => (distance < AUTOSCROLL_EDGE ? Math.ceil(((AUTOSCROLL_EDGE - Math.max(distance, 0)) / AUTOSCROLL_EDGE) * 22) : 0);
      const dx = speed(pointer.x - bounds.left) ? -speed(pointer.x - bounds.left) : speed(bounds.right - pointer.x);
      const dy = speed(pointer.y - bounds.top) ? -speed(pointer.y - bounds.top) : speed(bounds.bottom - pointer.y);
      if (dx || dy) {
        const before = { left: board.scrollLeft, top: board.scrollTop };
        board.scrollLeft += dx;
        board.scrollTop += dy;
        if (board.scrollLeft !== before.left || board.scrollTop !== before.top) place();
      }
      frame = requestAnimationFrame(autoScroll);
    };
    frame = requestAnimationFrame(autoScroll);
    const move = (next: PointerEvent) => {
      pointer = { x: next.clientX, y: next.clientY };
      place();
    };
    const up = () => {
      cancelAnimationFrame(frame);
      window.removeEventListener("pointermove", move);
      window.removeEventListener("pointerup", up);
      setDragging(null);
      if (id !== EXIT) onMove(id, latest);
    };
    window.addEventListener("pointermove", move);
    window.addEventListener("pointerup", up);
  }

  const exportPng = async () => {
    const svgText = renderToStaticMarkup(<Board cards={cards} exit={exit} hasExit={hasExit} view={view} palette={LIGHT} currentId="" interactive={false} />);
    downloadUrl(await svgToPng(svgText, Math.round(view.w), Math.round(view.h)), "storyboard.png");
  };

  const toolbar = (
    <>
      <div className="flex items-center gap-1">
        <Button variant="ghost" size="icon-sm" aria-label="Zoom out" onClick={() => zoomTo(scale / 1.25)}><i className="text-xl material-symbols-rounded" aria-hidden="true">remove</i></Button>
        <button type="button" className="w-14 text-sm tabular-nums text-center text-muted-foreground hover:text-foreground" title="Fit to window" onClick={() => zoomTo(fitScale(liveView))}>{Math.round(scale * 100)}%</button>
        <Button variant="ghost" size="icon-sm" aria-label="Zoom in" onClick={() => zoomTo(scale * 1.25)}><i className="text-xl material-symbols-rounded" aria-hidden="true">add</i></Button>
      </div>
      {hiddenScenes.length > 0 && (
        <div className="relative">
          <Button variant="ghost" size="sm" aria-expanded={hiddenOpen} onClick={() => setHiddenOpen(open => !open)}>
            <i className="mr-2 text-lg material-symbols-rounded" aria-hidden="true">visibility_off</i>Hidden ({hiddenScenes.length})
          </Button>
          {hiddenOpen && (
            <ul className="playground-storyboard-hidden">
              {hiddenScenes.map(record => (
                <li key={record.id}>
                  <button type="button" onClick={() => { onToggleHidden(record.id, false); if (hiddenScenes.length === 1) setHiddenOpen(false); }}>
                    <i className="text-lg material-symbols-rounded" aria-hidden="true">visibility</i>
                    <span>{record.name}</span>
                  </button>
                </li>
              ))}
            </ul>
          )}
        </div>
      )}
      <Button variant="ghost" size="icon-sm" disabled={refreshing} aria-label="Refresh previews" title={refreshing ? "Rendering previews…" : "Refresh previews"} onClick={onRefresh}>
        <i className={`text-xl material-symbols-rounded ${refreshing ? "animate-spin" : ""}`} aria-hidden="true">refresh</i>
      </Button>
      <Button variant="secondary" size="sm" disabled={refreshing || !cards.length} onClick={exportPng}>
        <i className="mr-2 text-lg material-symbols-rounded" aria-hidden="true">image</i>PNG
      </Button>
    </>
  );

  return (
    <div className="playground-storyboard" role="region" aria-label="Storyboard">
      {toolbarHost && createPortal(toolbar, toolbarHost)}
      <div ref={boardRef} className="playground-storyboard-board" onPointerDown={startPan}>
        <Board
          cards={cards}
          exit={exit}
          hasExit={hasExit}
          view={view}
          palette={DARK}
          currentId={currentId}
          interactive
          pixelsPerUnit={scale}
          onDragStart={startDrag}
          onOpen={onOpenScene}
          onHide={id => onToggleHidden(id, true)}
        />
        {!anyLinks && (
          <p className="playground-storyboard-hint">
            Select a button or any component and choose a scene under “Click leads to”. Its arrow shows up here.
          </p>
        )}
      </div>
    </div>
  );
}
