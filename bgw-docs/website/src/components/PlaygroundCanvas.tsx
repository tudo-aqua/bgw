import { useEffect, useRef, useState } from "react";

type Rect = { x: number; y: number; w: number; h: number };
type Bounds = { posX: number; posY: number; width: number; height: number };
type Handle = "n" | "s" | "e" | "w" | "ne" | "nw" | "se" | "sw";
type Pointer = { clientX: number; clientY: number; ctrlKey: boolean };

export type CanvasApi = {
  /** Ids of all components that may act as snapping targets or drop targets for `id`. */
  targetIds: (id: string) => string[];
  getBounds: (id: string) => Bounds | null;
  /** Position can be changed by dragging (not controlled by a layout). */
  canMove: (id: string) => boolean;
  canResize: (id: string) => boolean;
  canDrop: (id: string, targetId: string) => boolean;
  parentOf: (id: string) => string | number;
  select: (id: string) => void;
  changeBounds: (id: string, bounds: Partial<Bounds>) => void;
  /** `position` is the new position relative to the target, in scene units. */
  reparent: (id: string, targetId: string | number, position: { posX: number; posY: number }) => void;
};

const SNAP_PIXELS = 8;
const DRAG_THRESHOLD = 3;
const HANDLES: { id: Handle; x: number; y: number; cursor: string }[] = [
  { id: "nw", x: 0, y: 0, cursor: "nwse-resize" }, { id: "n", x: 0.5, y: 0, cursor: "ns-resize" },
  { id: "ne", x: 1, y: 0, cursor: "nesw-resize" }, { id: "e", x: 1, y: 0.5, cursor: "ew-resize" },
  { id: "se", x: 1, y: 1, cursor: "nwse-resize" }, { id: "s", x: 0.5, y: 1, cursor: "ns-resize" },
  { id: "sw", x: 0, y: 1, cursor: "nesw-resize" }, { id: "w", x: 0, y: 0.5, cursor: "ew-resize" },
];

const sceneElement = () => document.getElementById("boardGameScene");

function nearest(values: number[], targets: number[], threshold: number) {
  let best: { delta: number; target: number } | null = null;
  for (const value of values) for (const target of targets) {
    const delta = target - value;
    if (Math.abs(delta) <= threshold && (!best || Math.abs(delta) < Math.abs(best.delta))) best = { delta, target };
  }
  return best;
}

/**
 * Figma-style editing layer for the preview.
 * Move: Shift locks the axis, Alt turns snapping off, Ctrl while dropping moves the component into the container below.
 * Resize: Alt resizes from the center, Shift keeps the aspect ratio, Ctrl turns snapping off.
 */
export default function PlaygroundCanvas({ sceneWidth, sceneHeight, selectedId, api, resetKey }: {
  sceneWidth: number;
  sceneHeight: number;
  selectedId: string;
  api: CanvasApi;
  resetKey: unknown;
}) {
  const [selection, setSelection] = useState<Rect | null>(null);
  const [guides, setGuides] = useState<{ xs: number[]; ys: number[] }>({ xs: [], ys: [] });
  const [dropRect, setDropRect] = useState<Rect | null>(null);
  const [dragging, setDragging] = useState(false);
  const latest = useRef({ sceneWidth, sceneHeight, selectedId, api });
  latest.current = { sceneWidth, sceneHeight, selectedId, api };
  const justDragged = useRef(false);
  const drag = useRef<{ stop: () => void } | null>(null);

  const preview = () => document.getElementById("bgw-preview");
  const relative = (rect: DOMRect): Rect => {
    const origin = preview()!.getBoundingClientRect();
    return { x: rect.x - origin.x, y: rect.y - origin.y, w: rect.width, h: rect.height };
  };

  const measure = () => {
    const { selectedId: id } = latest.current;
    const element = id ? document.getElementById(id) : null;
    if (!element || !preview() || !sceneElement()) return setSelection(null);
    const rect = element.getBoundingClientRect();
    if (rect.width === 0 && rect.height === 0) return setSelection(null);
    const next = relative(rect);
    setSelection(previous => previous && ["x", "y", "w", "h"].every(key => Math.abs(previous[key as keyof Rect] - next[key as keyof Rect]) < 0.25) ? previous : next);
  };

  useEffect(() => {
    let frame = 0;
    const schedule = () => { cancelAnimationFrame(frame); frame = requestAnimationFrame(measure); };
    const container = document.getElementById("bgw-root-container");
    const observer = new MutationObserver(schedule);
    if (container) observer.observe(container, { attributes: true, childList: true, subtree: true });
    const resize = new ResizeObserver(schedule);
    if (preview()) resize.observe(preview()!);
    window.addEventListener("resize", schedule);
    schedule();
    return () => {
      cancelAnimationFrame(frame);
      observer.disconnect();
      resize.disconnect();
      window.removeEventListener("resize", schedule);
    };
  }, [selectedId, resetKey]);

  function startDrag(id: string, kind: "move" | Handle, event: PointerEvent) {
    const { api: tools, sceneWidth: width, sceneHeight: height } = latest.current;
    const scene = sceneElement();
    const element = document.getElementById(id);
    const start = tools.getBounds(id);
    if (!scene || !element || !start) return;
    const previewRect = preview()!.getBoundingClientRect();
    const sceneRect = scene.getBoundingClientRect();
    const scale = sceneRect.width / width;
    const toScene = (rect: DOMRect): Rect => ({ x: (rect.x - sceneRect.x) / scale, y: (rect.y - sceneRect.y) / scale, w: rect.width / scale, h: rect.height / scale });
    const origin = toScene(element.getBoundingClientRect());
    const targets = new Map<string, Rect>();
    for (const target of tools.targetIds(id)) {
      const rect = document.getElementById(target)?.getBoundingClientRect();
      if (rect && rect.width > 0 && rect.height > 0) targets.set(target, toScene(rect));
    }
    const parentId = tools.parentOf(id);
    const parentRect: Rect = (parentId !== 0 && targets.get(parentId as string)) || { x: 0, y: 0, w: width, h: height };
    const snapX = [0, width / 2, width];
    const snapY = [0, height / 2, height];
    targets.forEach(rect => {
      snapX.push(rect.x, rect.x + rect.w / 2, rect.x + rect.w);
      snapY.push(rect.y, rect.y + rect.h / 2, rect.y + rect.h);
    });
    const threshold = SNAP_PIXELS / scale;
    const canMove = tools.canMove(id);
    const toScreen = (value: number, axis: "x" | "y") => (axis === "x" ? sceneRect.x - previewRect.x : sceneRect.y - previewRect.y) + value * scale;
    let moved = false;
    let drop: string | number = parentId;
    let finalOrigin = origin;
    let last: Pointer = event;

    // Moving into a component only happens while Ctrl is held.
    const updateDrop = (pointer: Pointer) => {
      drop = parentId;
      if (!pointer.ctrlKey) return setDropRect(null);
      const point = { x: (pointer.clientX - sceneRect.x) / scale, y: (pointer.clientY - sceneRect.y) / scale };
      let best: { id: string; rect: Rect } | null = null as { id: string; rect: Rect } | null;
      targets.forEach((rect, target) => {
        if (point.x < rect.x || point.y < rect.y || point.x > rect.x + rect.w || point.y > rect.y + rect.h) return;
        if (!tools.canDrop(id, target) || (best && best.rect.w * best.rect.h <= rect.w * rect.h)) return;
        best = { id: target, rect };
      });
      drop = best ? best.id : 0;
      setDropRect(drop !== parentId && best ? { x: toScreen(best.rect.x, "x"), y: toScreen(best.rect.y, "y"), w: best.rect.w * scale, h: best.rect.h * scale } : null);
    };

    // Snaps one axis of a resize; returns the new low/high edges.
    const resizeAxis = (direction: number, delta: number, low: number, high: number, candidates: number[], container: number, snapping: boolean, center: boolean, lines: number[]) => {
      let nextLow = low, nextHigh = high;
      const edge = direction > 0 ? high : low;
      const match = snapping ? nearest([edge + delta], candidates, threshold) : null;
      const shift = (match ? match.target : edge + delta) - edge;
      if (match) lines.push(match.target);
      if (direction > 0) { nextHigh += shift; if (center) nextLow -= shift; } else { nextLow += shift; if (center) nextHigh -= shift; }
      // Also snap to the size of the containing component.
      if (snapping && !match && Math.abs(nextHigh - nextLow - container) <= threshold) {
        const grow = container - (nextHigh - nextLow);
        if (direction > 0) { nextHigh += grow; if (center) nextLow -= grow; } else { nextLow -= grow; if (center) nextHigh += grow; }
      }
      return [nextLow, nextHigh];
    };

    const onMove = (move: PointerEvent) => {
      last = move;
      if (!moved && Math.hypot(move.clientX - event.clientX, move.clientY - event.clientY) < DRAG_THRESHOLD) return;
      if (!moved) { moved = true; setDragging(true); }
      let dx = (move.clientX - event.clientX) / scale;
      let dy = (move.clientY - event.clientY) / scale;
      const lines = { xs: [] as number[], ys: [] as number[] };
      if (kind === "move") {
        if (move.shiftKey) { if (Math.abs(dx) > Math.abs(dy)) dy = 0; else dx = 0; }
        if (!move.altKey) {
          const sx = nearest([origin.x + dx, origin.x + dx + origin.w / 2, origin.x + dx + origin.w], snapX, threshold);
          const sy = nearest([origin.y + dy, origin.y + dy + origin.h / 2, origin.y + dy + origin.h], snapY, threshold);
          if (sx) { dx += sx.delta; lines.xs.push(sx.target); }
          if (sy) { dy += sy.delta; lines.ys.push(sy.target); }
        }
        dx = Math.round(dx);
        dy = Math.round(dy);
        finalOrigin = { ...origin, x: origin.x + dx, y: origin.y + dy };
        if (canMove) tools.changeBounds(id, { posX: Math.round(start.posX + dx), posY: Math.round(start.posY + dy) });
        updateDrop(move);
      } else {
        const snapping = !move.ctrlKey;
        const center = move.altKey;
        const hx = kind.includes("e") ? 1 : kind.includes("w") ? -1 : 0;
        const hy = kind.includes("s") ? 1 : kind.includes("n") ? -1 : 0;
        let L = origin.x, R = origin.x + origin.w, T = origin.y, B = origin.y + origin.h;
        if (hx) [L, R] = resizeAxis(hx, dx, L, R, snapX, parentRect.w, snapping, center, lines.xs);
        if (hy) [T, B] = resizeAxis(hy, dy, T, B, snapY, parentRect.h, snapping, center, lines.ys);
        if (move.shiftKey) {
          const ratio = origin.h / origin.w;
          if (hx) {
            const h = (R - L) * ratio;
            const middle = (T + B) / 2;
            if (center || !hy) { T = middle - h / 2; B = middle + h / 2; }
            else if (hy > 0) B = T + h;
            else T = B - h;
          } else {
            const w = (B - T) / ratio;
            const middle = (L + R) / 2;
            L = middle - w / 2;
            R = middle + w / 2;
          }
        }
        if (R - L < 1) { if (hx < 0) L = R - 1; else R = L + 1; }
        if (B - T < 1) { if (hy < 0) T = B - 1; else B = T + 1; }
        const next: Partial<Bounds> = {
          width: Math.max(1, Math.round(start.width + (R - L) - origin.w)),
          height: Math.max(1, Math.round(start.height + (B - T) - origin.h)),
        };
        // Components arranged by a layout only change their size.
        if (canMove) {
          next.posX = Math.round(start.posX + (L - origin.x));
          next.posY = Math.round(start.posY + (T - origin.y));
        }
        tools.changeBounds(id, next);
      }
      setGuides({ xs: lines.xs.map(value => toScreen(value, "x")), ys: lines.ys.map(value => toScreen(value, "y")) });
    };

    const onKey = (key: KeyboardEvent) => {
      if (key.key !== "Control" || kind !== "move" || !moved) return;
      updateDrop({ clientX: last.clientX, clientY: last.clientY, ctrlKey: key.type === "keydown" });
    };

    const finish = (up?: PointerEvent) => {
      window.removeEventListener("pointermove", onMove);
      window.removeEventListener("pointerup", finish);
      window.removeEventListener("pointercancel", cancel);
      window.removeEventListener("keydown", onKey);
      window.removeEventListener("keyup", onKey);
      drag.current = null;
      if (moved) {
        justDragged.current = true;
        setTimeout(() => { justDragged.current = false; }, 0);
        if (kind === "move" && up?.type === "pointerup") {
          updateDrop(up);
          if (drop !== parentId) {
            const target = drop === 0 ? { x: 0, y: 0 } : targets.get(drop as string) ?? { x: 0, y: 0 };
            tools.reparent(id, drop, { posX: Math.round(finalOrigin.x - target.x), posY: Math.round(finalOrigin.y - target.y) });
          }
        }
      }
      setDragging(false);
      setGuides({ xs: [], ys: [] });
      setDropRect(null);
    };
    const cancel = () => finish();
    window.addEventListener("pointermove", onMove);
    window.addEventListener("pointerup", finish);
    window.addEventListener("pointercancel", cancel);
    window.addEventListener("keydown", onKey);
    window.addEventListener("keyup", onKey);
    drag.current = { stop: cancel };
  }

  useEffect(() => {
    const container = document.getElementById("bgw-root-container");
    if (!container) return;
    const onPointerDown = (event: PointerEvent) => {
      if (event.button !== 0) return;
      const { api: tools } = latest.current;
      let target = event.target as HTMLElement | null;
      while (target && target !== container && !tools.getBounds(target.id)) target = target.parentElement;
      if (!target || target === container) return;
      event.preventDefault();
      tools.select(target.id);
      startDrag(target.id, "move", event);
    };
    // A drag ends with a click on the preview; swallow it so it does not change the selection.
    const onClick = (event: MouseEvent) => {
      if (justDragged.current) { event.stopPropagation(); event.preventDefault(); }
    };
    container.addEventListener("pointerdown", onPointerDown);
    container.addEventListener("click", onClick);
    return () => {
      container.removeEventListener("pointerdown", onPointerDown);
      container.removeEventListener("click", onClick);
      drag.current?.stop();
    };
  }, []);

  const resizable = !!selectedId && api.canResize(selectedId);
  return (
    <div className="playground-canvas" aria-hidden="true">
      {guides.xs.map(x => <div key={`x${x}`} className="playground-snap playground-snap-x" style={{ left: x }} />)}
      {guides.ys.map(y => <div key={`y${y}`} className="playground-snap playground-snap-y" style={{ top: y }} />)}
      {dropRect && <div className="playground-drop-target" style={{ left: dropRect.x, top: dropRect.y, width: dropRect.w, height: dropRect.h }} />}
      {selection && (
        <div className="playground-selection" data-dragging={dragging} style={{ left: selection.x, top: selection.y, width: selection.w, height: selection.h }}>
          {resizable && HANDLES.map(handle => (
            <span
              key={handle.id}
              className="playground-handle"
              style={{ left: `${handle.x * 100}%`, top: `${handle.y * 100}%`, cursor: handle.cursor }}
              onPointerDown={event => { event.preventDefault(); event.stopPropagation(); startDrag(selectedId, handle.id, event.nativeEvent); }}
            />
          ))}
        </div>
      )}
    </div>
  );
}
