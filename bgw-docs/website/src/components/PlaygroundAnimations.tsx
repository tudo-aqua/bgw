import { ReactNode, useEffect, useRef, useState } from "react";
import { createPortal } from "react-dom";
import { Button } from "./ui/button";
import { Input } from "./ui/input";
import { Label } from "./ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import { DropdownMenu, DropdownMenuContent, DropdownMenuGroup, DropdownMenuItem, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from "./ui/dropdown-menu";
import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "./ui/dialog";
import { Tabs, TabsList, TabsTrigger } from "./ui/tabs";
import SettingsField from "./SettingsField";
import { closestCenter, DndContext, DragEndEvent, PointerSensor, useSensor, useSensors } from "@dnd-kit/core";
import { arrayMove, SortableContext, useSortable, verticalListSortingStrategy } from "@dnd-kit/sortable";
import { CSS } from "@dnd-kit/utilities";
import { Popover, PopoverContent, PopoverTrigger } from "./ui/popover";
import { HexColorPicker } from "react-colorful";
import CodeExport from "./CodeExport";
import { downloadText } from "@/lib/sceneImage";
import {
  AnimationCategory, AnimationClip, AnimationKind, AnimationNode, CATEGORIES, INTERPOLATIONS, Interpolation, KINDS, Snapshot, VisualSpec,
  cloneNode, colorOf, kindStyle, createClip, createNode, exportJson, findNode, findParent, hasInterpolation, isGroup, layout, mapNode,
  needsTarget, problemOf, toMessage, validTarget,
} from "@/lib/animations";

export type Target = { id: string; name: string; type: string; depth: number };
type Status = "idle" | "playing" | "paused" | "done";
const REMOVED_STYLES = ["--txAnim", "--tyAnim", "--rot", "--sxAnim", "--syAnim", "--opaAnim", "--flipAnim", "--steppedAnim"];
const STOP_MESSAGE = { container: "bgw-root", props: { data: { type: "data.animation.DelayAnimationData", id: "composer-stop", isStop: true } } };
const sendMessage = (message: unknown) => document.dispatchEvent(new CustomEvent("BGW_MSG", { detail: JSON.stringify(message) }));
const controls = () => (window as any).bgwAnimator as { pause(): void; resume(): void; seek(time: number): void; time(): number; active(): boolean } | undefined;
const seconds = (ms: number) => `${(ms / 1000).toFixed(2)}s`;

export type Player = ReturnType<typeof useAnimationPlayer>;

/** Plays a clip through the live preview. The Kotlin renderer exposes pause/seek as `window.bgwAnimator`. */
export function useAnimationPlayer(clip: AnimationClip | undefined, snapshot: Snapshot, restoreScene: () => void) {
  const [status, setStatus] = useState<Status>("idle");
  const [time, setTime] = useState(0);
  const frame = useRef(0);
  const total = clip ? layout(clip.root).total : 0;

  const watch = () => {
    cancelAnimationFrame(frame.current);
    const step = () => {
      const player = controls();
      if (!player) return;
      setTime(player.time());
      if (!player.active()) { setStatus("done"); return; }
      frame.current = requestAnimationFrame(step);
    };
    frame.current = requestAnimationFrame(step);
  };
  useEffect(() => () => cancelAnimationFrame(frame.current), []);

  const clear = () => {
    sendMessage(STOP_MESSAGE);
    document.querySelectorAll<HTMLElement>("#bgw-root [style]").forEach(element => REMOVED_STYLES.forEach(name => element.style.removeProperty(name)));
  };
  const start = (paused = false) => {
    cancelAnimationFrame(frame.current);
    clear();
    const message = clip && toMessage(clip, snapshot);
    if (!message) { setStatus("idle"); setTime(0); return false; }
    sendMessage(message);
    const player = controls();
    if (!player) return false;
    if (paused) { player.pause(); setStatus("paused"); } else { setStatus("playing"); watch(); }
    return true;
  };

  return {
    status, time, total,
    /** Whether the loaded renderer supports pausing and scrubbing. */
    canControl: () => !!controls(),
    play: () => { setTime(0); start(); },
    pause: () => { cancelAnimationFrame(frame.current); controls()?.pause(); setTime(controls()?.time() ?? 0); setStatus("paused"); },
    resume: () => { controls()?.resume(); setStatus("playing"); watch(); },
    reset: () => { cancelAnimationFrame(frame.current); clear(); restoreScene(); setStatus("idle"); setTime(0); },
    seek: (value: number) => {
      if (status === "idle" || status === "done") { if (!start(true)) return; }
      else { cancelAnimationFrame(frame.current); controls()?.pause(); setStatus("paused"); }
      controls()?.seek(value);
      setTime(value);
    },
  };
}

// ---- small inputs ----

function NumberInput({ value, onChange, placeholder, min, step = 1, label, unit }: { value: number | undefined; onChange: (value: number | undefined) => void; placeholder?: string; min?: number; step?: number; label: string; unit?: string }) {
  const [draft, setDraft] = useState(value === undefined ? "" : String(value));
  const focused = useRef(false);
  useEffect(() => { if (!focused.current) setDraft(value === undefined ? "" : String(value)); }, [value]);
  return (
    <div className="relative flex-1 min-w-0">
      <Input
        aria-label={label}
        inputMode="decimal"
        value={draft}
        placeholder={placeholder}
        className={unit ? "min-w-0 pr-9 tabular-nums" : "min-w-0 tabular-nums"}
        onFocus={() => { focused.current = true; }}
        onBlur={() => { focused.current = false; setDraft(value === undefined ? "" : String(value)); }}
        onKeyDown={event => {
          if (event.key !== "ArrowUp" && event.key !== "ArrowDown") return;
          event.preventDefault();
          const next = Math.round(((value ?? 0) + (event.key === "ArrowUp" ? step : -step)) * 1000) / 1000;
          onChange(min === undefined ? next : Math.max(min, next));
        }}
        onChange={event => {
          const text = event.target.value;
          setDraft(text);
          if (text.trim() === "") { onChange(undefined); return; }
          const parsed = Number(text);
          if (Number.isFinite(parsed)) onChange(min === undefined ? parsed : Math.max(min, parsed));
        }}
      />
      {unit && <span className="absolute text-xs -translate-y-1/2 pointer-events-none right-3 top-1/2 text-muted-foreground">{unit}</span>}
    </div>
  );
}

function Section({ title, icon, children, action }: { title: string; icon: string; children: ReactNode; action?: ReactNode }) {
  return (
    <SettingsField title={title} icon={icon} hideRightIcon>
      {action && <div className="flex justify-end -mt-2">{action}</div>}
      {children}
    </SettingsField>
  );
}

function Field({ label, children, hint }: { label: string; children: ReactNode; hint?: string }) {
  return (
    <div className="flex items-center gap-3" title={hint}>
      <Label className="shrink-0 w-[43%] text-white/70">{label}</Label>
      <div className="flex items-center flex-1 min-w-0 gap-2">{children}</div>
    </div>
  );
}

/** A value that animates from one number to another. Leaving "from" empty starts at the component's current value. */
function Range({ label, from, to, onFrom, onTo, unit, step, min }: { label: string; from: number | undefined; to: number | undefined; onFrom: (value: number | undefined) => void; onTo: (value: number | undefined) => void; unit?: string; step?: number; min?: number }) {
  return (
    <div className="flex flex-col gap-2">
      <Label className="text-white/70">{label}</Label>
      <div className="flex items-center gap-2">
        <NumberInput label={`${label} from`} value={from} placeholder="current" onChange={onFrom} unit={unit} step={step} min={min} />
        <i className="text-base material-symbols-rounded text-muted-foreground" aria-hidden="true">arrow_forward</i>
        <NumberInput label={`${label} to`} value={to} placeholder="current" onChange={onTo} unit={unit} step={step} min={min} />
      </div>
    </div>
  );
}

function ColorSwatch({ value, onChange, label }: { value: string; onChange: (value: string) => void; label: string }) {
  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button variant="secondary" size="icon" aria-label={label} title={label} className="shrink-0">
          <span className="block w-5 h-5 border rounded border-white/20" style={{ background: value }} />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="flex flex-col w-56 gap-3 p-3" align="end">
        <HexColorPicker color={value} onChange={onChange} style={{ width: "100%" }} />
        <Input aria-label={`${label} hex`} value={value} onChange={event => /^#[\da-f]{0,6}$/i.test(event.target.value) && onChange(event.target.value)} className="font-mono" />
      </PopoverContent>
    </Popover>
  );
}

function VisualEditor({ value, onChange, isCard, label }: { value: VisualSpec; onChange: (value: VisualSpec) => void; isCard: boolean; label: string }) {
  return (
    <>
      <Select value={value.source} onValueChange={source => onChange({ source: source as VisualSpec["source"], color: value.color ?? "#ef4444" })}>
        <SelectTrigger aria-label={label} className="flex-1 min-w-0"><SelectValue /></SelectTrigger>
        <SelectContent>
          <SelectItem value="own">Current visual</SelectItem>
          {isCard && <SelectItem value="front">Front</SelectItem>}
          {isCard && <SelectItem value="back">Back</SelectItem>}
          <SelectItem value="color">Color</SelectItem>
        </SelectContent>
      </Select>
      {value.source === "color" && <ColorSwatch label={`${label} color`} value={value.color ?? "#ef4444"} onChange={color => onChange({ source: "color", color })} />}
    </>
  );
}


export type ComposerProps = {
  clips: AnimationClip[];
  onChange: (clips: AnimationClip[]) => void;
  clipId: string;
  onClipChange: (id: string) => void;
  nodeId: string;
  onNodeChange: (id: string) => void;
  snapshot: Snapshot;
  targets: Target[];
  selectedComponentId: string;
  player: Player;
};

/** The BGW class name, followed by the animated component in muted text. Only the component name is truncated. */
function NodeLabel({ node, snapshot }: { node: AnimationNode; snapshot: Snapshot }) {
  const target = node.target ? snapshot[node.target] : undefined;
  return (
    <span className="flex items-baseline min-w-0 gap-1.5">
      <span className="truncate shrink-0 max-w-full">{KINDS[node.kind].label}</span>
      {needsTarget(node.kind) && <span className="font-normal truncate text-muted-foreground">{target?.name ?? "no component"}</span>}
    </span>
  );
}

function KindIcon({ kind, className = "text-lg" }: { kind: AnimationKind; className?: string }) {
  return <i className={`material-symbols-rounded ${className}`} style={{ color: colorOf(kind) }} aria-hidden="true">{KINDS[kind].icon}</i>;
}

const KINDS_BY_CATEGORY = (Object.keys(CATEGORIES) as AnimationCategory[]).map(category => ({
  category,
  kinds: (Object.keys(KINDS) as AnimationKind[]).filter(kind => KINDS[kind].category === category),
}));

function useClip({ clips, clipId, onChange }: Pick<ComposerProps, "clips" | "clipId" | "onChange">) {
  const clip = clips.find(item => item.id === clipId) ?? clips[0];
  const update = (change: (root: AnimationNode) => AnimationNode) => clip && onChange(clips.map(item => item.id === clip.id ? { ...item, root: change(item.root) } : item));
  return { clip, update };
}

// ---- timeline (bottom panel) ----

function Row({ node, depth, total, timings, props }: { node: AnimationNode; depth: number; total: number; timings: ReturnType<typeof layout>["timings"]; props: ComposerProps }) {
  const timing = timings.get(node.id)!;
  const problem = problemOf(node, props.snapshot);
  const selected = props.nodeId === node.id;
  const left = total ? (timing.start / total) * 100 : 0;
  const width = total ? Math.max(((timing.end - timing.start) / total) * 100, 0.6) : 0;
  return (
    <>
      <div
        role="treeitem"
        aria-selected={selected}
        aria-level={depth + 1}
        tabIndex={0}
        className="composer-row"
        style={kindStyle(node.kind)}
        data-selected={selected}
        data-kind={node.kind}
        onClick={() => props.onNodeChange(node.id)}
        onKeyDown={event => { if (event.key === "Enter" || event.key === " ") { event.preventDefault(); props.onNodeChange(node.id); } }}
      >
        <div className="composer-row-label" style={{ paddingLeft: 8 + depth * 16 }}>
          <KindIcon kind={node.kind} />
          <NodeLabel node={node} snapshot={props.snapshot} />
          {problem && <i className="text-base text-yellow-500 material-symbols-rounded" title={problem} aria-label={problem}>warning</i>}
        </div>
        <div className="composer-row-track">
          <div className="composer-bar" data-group={isGroup(node)} data-problem={!!problem} style={{ left: `${left}%`, width: `${width}%`, background: problem ? undefined : colorOf(node.kind, isGroup(node) ? 0.3 : undefined) }}>
            {!isGroup(node) && width > 12 && <span>{seconds(node.duration)}</span>}
          </div>
        </div>
      </div>
      {node.children?.map(child => <Row key={child.id} node={child} depth={depth + 1} total={total} timings={timings} props={props} />)}
    </>
  );
}

export function AnimationTimeline(props: ComposerProps) {
  const { clip, update } = useClip(props);
  const { player } = props;
  const selection = clip ? findNode(clip.root, props.nodeId) : null;

  const addClip = () => {
    const created = createClip(`Animation ${props.clips.length + 1}`);
    props.onChange([...props.clips, created]);
    props.onClipChange(created.id);
    props.onNodeChange(created.root.id);
  };
  const add = (kind: AnimationKind) => {
    if (!clip) return;
    const node = createNode(kind, needsTarget(kind) && validTarget(kind, props.snapshot[props.selectedComponentId] ?? {}) ? props.selectedComponentId || undefined : undefined);
    if (kind === "flip" && props.snapshot[node.target ?? ""]?.type === "CardViewData") { node.fromVisual = { source: "back" }; node.toVisual = { source: "front" }; }
    // New animations go into the selected group, or after the selected animation.
    const inside = selection && isGroup(selection) ? selection : selection ? findParent(clip.root, selection.id) ?? clip.root : clip.root;
    update(root => mapNode(root, inside.id, group => {
      const children = [...(group.children ?? [])];
      const index = selection && selection.id !== inside.id ? children.findIndex(child => child.id === selection.id) + 1 : children.length;
      children.splice(index, 0, node);
      return { ...group, children };
    })!);
    props.onNodeChange(node.id);
  };

  if (!clip) {
    return (
      <div className="playground-composer">
        <EmptyState icon="animation" title="Bring this scene to life" className="h-full">
          <Button variant="secondary" size="sm" onClick={addClip} className="w-full max-w-[200px] text-bgw-green bg-bgw-green/5 hover:bg-bgw-green/10"><i className="mr-2 text-lg material-symbols-rounded" aria-hidden="true">add</i>New animation</Button>
        </EmptyState>
      </div>
    );
  }
  const { timings, total } = layout(clip.root);
  const ticks = total ? Array.from({ length: Math.floor(total / niceStep(total)) + 1 }, (_, index) => index * niceStep(total)) : [];
  return (
    <div className="playground-composer">
      <div className="playground-composer-toolbar">
        <Select value={clip.id} onValueChange={id => { props.onClipChange(id); const next = props.clips.find(item => item.id === id); if (next) props.onNodeChange(next.root.id); }}>
          <SelectTrigger aria-label="Animation" className="w-[180px] min-w-0"><SelectValue /></SelectTrigger>
          <SelectContent>{props.clips.map(item => <SelectItem key={item.id} value={item.id}>{item.name}</SelectItem>)}</SelectContent>
        </Select>
        <Input aria-label="Animation name" className="w-[160px] min-w-0" value={clip.name} onChange={event => props.onChange(props.clips.map(item => item.id === clip.id ? { ...item, name: event.target.value } : item))} />
        <Button variant="ghost" size="icon-sm" aria-label="New animation" title="New animation" onClick={addClip}><i className="text-lg material-symbols-rounded" aria-hidden="true">add</i></Button>
        <Button variant="ghost" size="icon-sm" aria-label="Duplicate animation" title="Duplicate animation" onClick={() => { const copy = { ...clip, id: `${clip.id}-copy-${Date.now().toString(36)}`, name: `${clip.name} copy`, root: cloneNode(clip.root) }; props.onChange([...props.clips, copy]); props.onClipChange(copy.id); props.onNodeChange(copy.root.id); }}><i className="text-lg material-symbols-rounded" aria-hidden="true">content_copy</i></Button>
        <Button variant="ghost" size="icon-sm" aria-label="Delete animation" title="Delete animation" onClick={() => { const rest = props.clips.filter(item => item.id !== clip.id); player.reset(); props.onChange(rest); props.onClipChange(rest[0]?.id ?? ""); props.onNodeChange(rest[0]?.root.id ?? ""); }}><i className="text-lg material-symbols-rounded" aria-hidden="true">delete</i></Button>
        <div className="ml-auto">
          <DropdownMenu>
            <DropdownMenuTrigger asChild>
              <Button variant="secondary" size="sm" className="text-bgw-green bg-bgw-green/5 hover:bg-bgw-green/10"><i className="mr-2 text-lg material-symbols-rounded" aria-hidden="true">add</i>Add step</Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className="w-60 max-h-[70vh] overflow-y-auto">
              {KINDS_BY_CATEGORY.map(({ category, kinds }, index) => (
                <DropdownMenuGroup key={category}>
                  {index > 0 && <DropdownMenuSeparator />}
                  <DropdownMenuLabel className="flex items-center gap-2 text-xs font-medium text-muted-foreground">
                    <span className="w-2 h-2 rounded-full" style={{ background: `hsl(var(${CATEGORIES[category].color}))` }} aria-hidden="true" />
                    {CATEGORIES[category].label}
                  </DropdownMenuLabel>
                  {kinds.map(kind => (
                    <DropdownMenuItem key={kind} onSelect={() => add(kind)} title={KINDS[kind].description} className="gap-3">
                      <KindIcon kind={kind} />{KINDS[kind].label}
                    </DropdownMenuItem>
                  ))}
                </DropdownMenuGroup>
              ))}
            </DropdownMenuContent>
          </DropdownMenu>
        </div>
      </div>
      <div className="composer-scrub">
        <div className="composer-row-label">
          <span className="tabular-nums text-muted-foreground">{seconds(Math.min(player.time, total))} / {seconds(total)}</span>
        </div>
        <div className="composer-row-track">
          {/* The renderer exposes its controls once an animation started; seeking starts the clip paused, so no need to wait for that. */}
          <Ruler total={total} time={player.time} disabled={!total} onSeek={player.seek} />
        </div>
      </div>
      <div className="playground-composer-rows" role="tree" aria-label="Animation timeline">
        <div className="relative composer-rows-inner">
          <div className="composer-playhead-area composer-grid-area" aria-hidden="true">
            {ticks.map(tick => <span key={tick} className="composer-grid-line" style={{ left: `${(tick / total) * 100}%` }} />)}
          </div>
          <div className="composer-playhead-area">
            <div className="composer-playhead" style={{ left: `${total ? (Math.min(player.time, total) / total) * 100 : 0}%` }} />
          </div>
          <Row node={clip.root} depth={0} total={total} timings={timings} props={props} />
          {!clip.root.children?.length && (
            <EmptyState icon="add_circle" title="Nothing to play yet" className="composer-empty-inline">
              <div className="flex flex-wrap justify-center gap-2">
                {(["movement", "fade", "rotation", "scale"] as AnimationKind[]).map(kind => (
                  <Button key={kind} variant="secondary" size="sm" onClick={() => add(kind)}><KindIcon kind={kind} className="mr-1.5 text-base" />{KINDS[kind].label}</Button>
                ))}
              </div>
            </EmptyState>
          )}
        </div>
      </div>
    </div>
  );
}

const tickLabel = (ms: number) => `${Number((ms / 1000).toFixed(2))}s`;

/** Time ruler above the tracks. Clicking or dragging anywhere on it scrubs the preview. */
function Ruler({ total, time, disabled, onSeek }: { total: number; time: number; disabled: boolean; onSeek: (time: number) => void }) {
  const ref = useRef<HTMLDivElement>(null);
  const step = total ? niceStep(total) : 0;
  const ticks = step ? Array.from({ length: Math.floor(total / step) + 1 }, (_, index) => index * step) : [];
  const minor = step ? Array.from({ length: Math.floor(total / (step / 5)) + 1 }, (_, index) => index * (step / 5)).filter(value => value % step !== 0) : [];
  const current = Math.min(time, total);
  const at = (value: number) => `${total ? (value / total) * 100 : 0}%`;
  const timeAt = (clientX: number) => {
    const rect = ref.current!.getBoundingClientRect();
    return Math.round((Math.min(Math.max((clientX - rect.left) / rect.width, 0), 1) * total) / 10) * 10;
  };
  return (
    <div
      ref={ref}
      className="composer-ruler"
      role="slider"
      tabIndex={disabled ? -1 : 0}
      aria-label="Playhead"
      aria-valuemin={0}
      aria-valuemax={total}
      aria-valuenow={Math.round(current)}
      aria-valuetext={seconds(current)}
      aria-disabled={disabled}
      data-disabled={disabled}
      onPointerDown={event => {
        if (disabled || event.button !== 0) return;
        event.currentTarget.setPointerCapture(event.pointerId);
        onSeek(timeAt(event.clientX));
      }}
      onPointerMove={event => { if (!disabled && event.currentTarget.hasPointerCapture(event.pointerId)) onSeek(timeAt(event.clientX)); }}
      onKeyDown={event => {
        if (disabled) return;
        const offset = event.shiftKey ? step : 100;
        const next = ({ ArrowLeft: current - offset, ArrowRight: current + offset, Home: 0, End: total } as Record<string, number>)[event.key];
        if (next === undefined) return;
        event.preventDefault();
        onSeek(Math.min(Math.max(next, 0), total));
      }}
    >
      {minor.map(tick => <span key={tick} className="composer-ruler-minor" style={{ left: at(tick) }} />)}
      {ticks.map(tick => <span key={tick} className="composer-ruler-major" data-start={tick === 0 || undefined} style={{ left: at(tick) }}><span>{tickLabel(tick)}</span></span>)}
      <span className="composer-ruler-head" style={{ left: at(current) }} />
    </div>
  );
}

function niceStep(total: number) {
  const target = total / 6;
  for (const step of [100, 250, 500, 1000, 2000, 5000, 10000, 30000, 60000]) if (step >= target) return step;
  return 60000;
}

// ---- inspector (side panel) ----

function ChildItem({ child, props, onRemove }: { child: AnimationNode; props: ComposerProps; onRemove: () => void }) {
  const { attributes, listeners, setNodeRef, transform, transition, isDragging } = useSortable({ id: child.id });
  const problem = problemOf(child, props.snapshot);
  return (
    <div ref={setNodeRef} style={{ ...kindStyle(child.kind), transform: CSS.Transform.toString(transform), transition, zIndex: isDragging ? 1 : undefined }} className={`flex items-center gap-1 rounded-lg bg-secondary hover:bg-[hsl(var(--kind)/.1)] ${isDragging ? "opacity-80 shadow-lg" : ""}`}>
      <button type="button" aria-label="Drag to reorder" title="Drag to reorder" className="grid h-9 cursor-grab w-7 place-items-center text-muted-foreground touch-none" {...attributes} {...listeners}>
        <i className="text-lg material-symbols-rounded" aria-hidden="true">drag_indicator</i>
      </button>
      <button type="button" className="flex items-center flex-1 min-w-0 gap-2 text-sm text-left h-9" onClick={() => props.onNodeChange(child.id)}>
        <KindIcon kind={child.kind} />
        <NodeLabel node={child} snapshot={props.snapshot} />
        {problem && <i className="text-base text-yellow-500 material-symbols-rounded" title={problem}>warning</i>}
      </button>
      <span className="text-xs tabular-nums text-muted-foreground">{seconds(isGroup(child) ? layout(child).total : child.duration)}</span>
      <Button variant="ghost" size="icon-sm" aria-label="Delete" title="Delete" onClick={onRemove}><i className="text-lg material-symbols-rounded" aria-hidden="true">close</i></Button>
    </div>
  );
}

function EmptyState({ icon, title, children, className = "" }: { icon: string; title: string; children?: ReactNode; className?: string }) {
  return (
    <div className={`composer-empty ${className}`}>
      <i className="text-4xl material-symbols-rounded text-bgw-green" aria-hidden="true">{icon}</i>
      <p className="font-semibold">{title}</p>
      {children}
    </div>
  );
}

function GroupContents({ node, props, change }: { node: AnimationNode; props: ComposerProps; change: (patch: Partial<AnimationNode>) => void }) {
  const sensors = useSensors(useSensor(PointerSensor, { activationConstraint: { distance: 4 } }));
  const children = node.children ?? [];
  const onDragEnd = ({ active, over }: DragEndEvent) => {
    if (!over || active.id === over.id) return;
    change({ children: arrayMove(children, children.findIndex(item => item.id === active.id), children.findIndex(item => item.id === over.id)) });
  };
  return (
    <Section title={node.kind === "sequence" ? "Plays in order" : "Plays together"} icon={KINDS[node.kind].icon}>
      {children.length === 0
        ? <p className="text-sm text-muted-foreground">Empty</p>
        : (
          <DndContext sensors={sensors} collisionDetection={closestCenter} onDragEnd={onDragEnd}>
            <SortableContext items={children.map(item => item.id)} strategy={verticalListSortingStrategy}>
              <div className="flex flex-col gap-1.5">
                {children.map(child => <ChildItem key={child.id} child={child} props={props} onRemove={() => change({ children: children.filter(item => item.id !== child.id) })} />)}
              </div>
            </SortableContext>
          </DndContext>
        )}
    </Section>
  );
}

export function AnimationInspector(props: ComposerProps) {
  const { clip, update } = useClip(props);
  const node = clip ? findNode(clip.root, props.nodeId) ?? clip.root : null;
  if (!clip || !node) return <EmptyState icon="tune" title="Nothing selected" className="h-full" />;

  const change = (patch: Partial<AnimationNode>) => update(root => mapNode(root, node.id, current => ({ ...current, ...patch }))!);
  const parent = findParent(clip.root, node.id);
  const siblings = parent?.children ?? [];
  const index = siblings.findIndex(child => child.id === node.id);
  const move = (offset: number) => parent && update(root => mapNode(root, parent.id, group => {
    const children = [...(group.children ?? [])];
    const [item] = children.splice(index, 1);
    children.splice(index + offset, 0, item);
    return { ...group, children };
  })!);
  const target = node.target ? props.snapshot[node.target] : undefined;
  const isCard = target?.type === "CardViewData";
  const problem = problemOf(node, props.snapshot);
  const choices = props.targets.filter(item => validTarget(node.kind, props.snapshot[item.id] ?? {}));

  return (
    <div className="playground-composer-inspector">
      <div className="flex flex-col gap-4 p-5 rounded-lg bg-background">
      <div className="flex items-center gap-3 -my-2">
        <KindIcon kind={node.kind} />
        <div className="flex-1 min-w-0">
          <h2 className="text-sm font-bold truncate">{KINDS[node.kind].label}</h2>
        </div>
        {parent && (
          <div className="flex items-center ml-auto -mr-2 shrink-0">
            <Button variant="ghost" size="icon-sm" aria-label="Move earlier" title="Move earlier" disabled={index <= 0} onClick={() => move(-1)}><i className="text-lg material-symbols-rounded" aria-hidden="true">arrow_upward</i></Button>
            <Button variant="ghost" size="icon-sm" aria-label="Move later" title="Move later" disabled={index >= siblings.length - 1} onClick={() => move(1)}><i className="text-lg material-symbols-rounded" aria-hidden="true">arrow_downward</i></Button>
            <Button variant="ghost" size="icon-sm" aria-label="Duplicate" title="Duplicate" onClick={() => {
              const copy = cloneNode(node);
              update(root => mapNode(root, parent.id, group => { const children = [...(group.children ?? [])]; children.splice(index + 1, 0, copy); return { ...group, children }; })!);
              props.onNodeChange(copy.id);
            }}><i className="text-lg material-symbols-rounded" aria-hidden="true">content_copy</i></Button>
            <Button variant="ghost" size="icon-sm" aria-label="Delete" title="Delete" onClick={() => { update(root => mapNode(root, node.id, () => null)!); props.onNodeChange(parent.id); }}><i className="text-lg material-symbols-rounded" aria-hidden="true">delete</i></Button>
          </div>
        )}
      </div>
      {problem && (
        <p className="text-sm text-yellow-500" role="status">{problem}</p>
      )}
      </div>

      {needsTarget(node.kind) && (
        <Section title="Target" icon="ads_click">
          <div className="flex items-center gap-2">
            <Select value={node.target && choices.some(item => item.id === node.target) ? node.target : undefined} onValueChange={value => change({ target: value })}>
              <SelectTrigger aria-label="Component" className="flex-1 min-w-0"><SelectValue placeholder="Choose a component…" /></SelectTrigger>
              <SelectContent>
                {choices.length === 0 && <div className="px-2 py-1.5 text-sm text-muted-foreground">No suitable component in the scene</div>}
                {choices.map(item => <SelectItem key={item.id} value={item.id} style={{ paddingLeft: 32 + item.depth * 12 }}>{item.name}{item.name !== item.type.replace(/Data$/, "") && <span className="text-muted-foreground"> ({item.type.replace(/Data$/, "")})</span>}</SelectItem>)}
              </SelectContent>
            </Select>
            <Button variant="secondary" size="icon" className="shrink-0" aria-label="Use selected component" title="Use the component selected in the scene" disabled={!props.selectedComponentId || !choices.some(item => item.id === props.selectedComponentId)} onClick={() => change({ target: props.selectedComponentId })}>
              <i className="text-lg material-symbols-rounded" aria-hidden="true">my_location</i>
            </Button>
          </div>
        </Section>
      )}

      {!isGroup(node) && (
        <Section title="Timing" icon="schedule">
          <Field label="Duration"><NumberInput label="Duration" value={node.duration} min={0} step={100} unit="ms" onChange={value => change({ duration: value ?? 0 })} /></Field>
          {hasInterpolation(node.kind) && (
            <Field label="Easing" hint="How the value progresses over the duration">
              <Select value={node.interpolation ?? "SMOOTH"} onValueChange={value => change({ interpolation: value as Interpolation })}>
                <SelectTrigger aria-label="Easing" className="flex-1 min-w-0"><SelectValue /></SelectTrigger>
                <SelectContent>{INTERPOLATIONS.map(value => <SelectItem key={value} value={value}>{value.charAt(0) + value.slice(1).toLowerCase()}</SelectItem>)}</SelectContent>
              </Select>
            </Field>
          )}
        </Section>
      )}

      {node.kind === "movement" && (
        <Section title="Movement" icon="open_with">
          <Field label="Move by X"><NumberInput label="Move by X" value={node.byX} step={10} unit="px" onChange={value => change({ byX: value ?? 0 })} /></Field>
          <Field label="Move by Y"><NumberInput label="Move by Y" value={node.byY} step={10} unit="px" onChange={value => change({ byY: value ?? 0 })} /></Field>
        </Section>
      )}
      {node.kind === "rotation" && (
        <Section title="Rotation" icon="rotate_right">
          <Range label="Angle" from={node.fromAngle} to={node.toAngle} onFrom={fromAngle => change({ fromAngle })} onTo={toAngle => change({ toAngle })} unit="°" step={15} />
        </Section>
      )}
      {node.kind === "scale" && (
        <Section title="Scale" icon="zoom_out_map">
          <Range label="Scale X" from={node.fromScaleX} to={node.toScaleX} onFrom={fromScaleX => change({ fromScaleX })} onTo={toScaleX => change({ toScaleX })} step={0.1} />
          <Range label="Scale Y" from={node.fromScaleY} to={node.toScaleY} onFrom={fromScaleY => change({ fromScaleY })} onTo={toScaleY => change({ toScaleY })} step={0.1} />
        </Section>
      )}
      {node.kind === "fade" && (
        <Section title="Opacity" icon="opacity">
          <Range label="Opacity" from={node.fromOpacity} to={node.toOpacity} onFrom={fromOpacity => change({ fromOpacity })} onTo={toOpacity => change({ toOpacity })} step={0.1} min={0} />
        </Section>
      )}
      {node.kind === "flip" && (
        <Section title="Visuals" icon="flip">
          <Field label="From"><VisualEditor label="From visual" isCard={isCard} value={node.fromVisual ?? { source: "own" }} onChange={fromVisual => change({ fromVisual })} /></Field>
          <Field label="To"><VisualEditor label="To visual" isCard={isCard} value={node.toVisual ?? { source: "own" }} onChange={toVisual => change({ toVisual })} /></Field>
        </Section>
      )}
      {node.kind === "randomize" && (
        <>
          <Section title="Shuffle through" icon="shuffle" action={
            <Button variant="ghost" size="sm" onClick={() => change({ visuals: [...(node.visuals ?? []), { source: "color", color: "#eab308" }] })}><i className="mr-1 text-base material-symbols-rounded" aria-hidden="true">add</i>Add</Button>
          }>
            {(node.visuals ?? []).map((visual, at) => (
              <div key={at} className="flex items-center gap-2">
                <span className="w-5 text-xs tabular-nums text-center text-muted-foreground">{at + 1}</span>
                <VisualEditor label={`Visual ${at + 1}`} isCard={isCard} value={visual} onChange={value => change({ visuals: node.visuals!.map((item, i) => i === at ? value : item) })} />
                <Button variant="ghost" size="icon-sm" aria-label={`Remove visual ${at + 1}`} title="Remove" disabled={(node.visuals?.length ?? 0) <= 1} onClick={() => change({ visuals: node.visuals!.filter((_, i) => i !== at) })}><i className="text-lg material-symbols-rounded" aria-hidden="true">close</i></Button>
              </div>
            ))}
          </Section>
          <Section title="Result" icon="flag">
            <Field label="Ends on"><VisualEditor label="Ends on" isCard={isCard} value={node.toVisual ?? { source: "own" }} onChange={toVisual => change({ toVisual })} /></Field>
            <Field label="Steps" hint="How many visuals are shown during the animation"><NumberInput label="Steps" value={node.speed} min={1} step={5} onChange={value => change({ speed: Math.max(1, Math.trunc(value ?? 1)) })} /></Field>
          </Section>
        </>
      )}
      {node.kind === "dice" && (
        <Section title="Roll" icon="casino">
          <Field label="Result side" hint="Zero-based index of the side the dice lands on">
            <NumberInput label="Result side" value={node.toSide ?? 0} min={0} onChange={value => change({ toSide: Math.max(0, Math.trunc(value ?? 0)) })} />
            <span className="text-xs whitespace-nowrap text-muted-foreground">of {target?.visuals?.length ?? "?"}</span>
          </Field>
          <Field label="Steps" hint="How many sides are shown during the roll"><NumberInput label="Steps" value={node.speed} min={1} step={5} onChange={value => change({ speed: Math.max(1, Math.trunc(value ?? 1)) })} /></Field>
        </Section>
      )}
      {isGroup(node) && <GroupContents node={node} props={props} change={change} />}
    </div>
  );
}


// ---- header toolbar ----

export function AnimationToolbar({ host, player, clips, snapshot, kotlin, sceneName }: { host: HTMLElement | null; player: Player; clips: AnimationClip[]; snapshot: Snapshot; kotlin: { code: string } | { error: string }; sceneName: string }) {
  const [exportOpen, setExportOpen] = useState(false);
  const [format, setFormat] = useState<"kotlin" | "json">("kotlin");
  const [copied, setCopied] = useState(false);
  const playing = player.status === "playing";
  const json = exportJson(clips, snapshot);
  const code = format === "json" ? json : "code" in kotlin ? kotlin.code : "";
  const error = format === "kotlin" && "error" in kotlin ? kotlin.error : "";
  const empty = clips.length === 0;
  const base = (sceneName || "scene").replace(/[^\w-]+/g, "_");

  const toolbar = (
    <>
      <div className="flex items-center gap-1">
        <Button variant="ghost" size="icon-sm" aria-label={playing ? "Pause" : player.status === "paused" ? "Resume" : "Play"} title={playing ? "Pause" : player.status === "paused" ? "Resume" : "Play"} disabled={empty || (playing && !player.canControl())}
          onClick={() => (playing ? player.pause() : player.status === "paused" ? player.resume() : player.play())}>
          <i className="text-xl material-symbols-rounded text-bgw-green" aria-hidden="true">{playing ? "pause" : "play_arrow"}</i>
        </Button>
        <Button variant="ghost" size="icon-sm" aria-label="Replay from start" title="Replay from start" disabled={empty} onClick={player.play}>
          <i className="text-xl material-symbols-rounded" aria-hidden="true">replay</i>
        </Button>
        <Button variant="ghost" size="icon-sm" aria-label="Reset scene" title="Stop and reset the scene" disabled={empty || player.status === "idle"} onClick={player.reset}>
          <i className="text-xl material-symbols-rounded" aria-hidden="true">stop</i>
        </Button>
      </div>
      <Button variant="secondary" size="sm" disabled={empty} onClick={() => { setCopied(false); setExportOpen(true); }}>
        <i className="mr-2 text-lg material-symbols-rounded" aria-hidden="true">code</i>Export
      </Button>
    </>
  );
  return (
    <>
      {host && createPortal(toolbar, host)}
      <Dialog open={exportOpen} onOpenChange={setExportOpen}>
        <DialogContent className="max-w-3xl">
          <DialogHeader>
            <DialogTitle>Export animations</DialogTitle>
            <DialogDescription>{format === "json" ? "The JSON messages the BGW renderer plays, as used by the documentation previews." : "The scene class including all animations as vals. Defaults are omitted."}</DialogDescription>
          </DialogHeader>
          <div className="flex items-center gap-2">
            <Tabs value={format} onValueChange={value => { setFormat(value as "kotlin" | "json"); setCopied(false); }}>
              <TabsList aria-label="Format">
                <TabsTrigger value="kotlin">Kotlin</TabsTrigger>
                <TabsTrigger value="json">JSON</TabsTrigger>
              </TabsList>
            </Tabs>
            <div className="flex items-center gap-1 ml-auto">
              <Button variant="ghost" size="icon-sm" aria-label="Download export" disabled={!!error} onClick={() => downloadText(code, format === "json" ? `${base}.animations.json` : `${base}.kt`, "text/plain")}><i className="text-lg material-symbols-rounded" aria-hidden="true">download</i></Button>
              <Button variant="secondary" size="sm" disabled={!!error} onClick={async () => { try { await navigator.clipboard.writeText(code); setCopied(true); } catch { setCopied(false); } }}>
                <i className="mr-2 text-lg material-symbols-rounded" aria-hidden="true">{copied ? "check" : "content_copy"}</i>{copied ? "Copied" : "Copy"}
              </Button>
            </div>
          </div>
          {error ? <p className="py-6 text-sm text-center text-muted-foreground" role="status">{error}</p> : (
            <pre className="playground-code composer-export" tabIndex={0} aria-label={`${format} export`}><code><CodeExport code={code} language={format === "json" ? "json" : "kotlin"} /></code></pre>
          )}
        </DialogContent>
      </Dialog>
    </>
  );
}
