import { useEffect, useRef, useState, ReactNode } from "react";
import { Mosaic, MosaicNode } from "react-mosaic-component";
import "react-mosaic-component/react-mosaic-component.css";

const panels = [
  { id: "left-sidebar", label: "Scene" },
  { id: "preview", label: "Preview" },
  { id: "right-sidebar", label: "Properties" },
  { id: "output", label: "Code" },
] as const;

export default function PlaygroundLayout({ elements, onLayoutChange, labels = {} }: {
  elements: Record<string, ReactNode>;
  onLayoutChange: () => void;
  /** Replaces panel titles while a mode shows something else in a panel. */
  labels?: Partial<Record<string, string>>;
}) {
  const [active, setActive] = useState<string>("preview");
  const refresh = useRef(onLayoutChange);
  const [layout, setLayout] = useState<MosaicNode<string>>({
    direction: "row",
    first: {
      direction: "row",
      first: "left-sidebar",
      second: { direction: "column", first: "preview", second: "output", splitPercentage: 65 },
      splitPercentage: 20,
    },
    second: "right-sidebar",
    splitPercentage: 80,
  });
  useEffect(() => { refresh.current = onLayoutChange; }, [onLayoutChange]);
  useEffect(() => {
    const frame = requestAnimationFrame(() => {
      window.dispatchEvent(new Event("resize"));
      refresh.current();
    });
    return () => cancelAnimationFrame(frame);
  }, [active]);

  return (
    <div className="playground-layout" data-active={active}>
      <nav className="playground-navigation" aria-label="Playground panels">
        {panels.map(panel => (
          <button key={panel.id} type="button" aria-pressed={active === panel.id} aria-controls={`playground-panel-${panel.id}`} onClick={() => setActive(panel.id)}>
            {labels[panel.id] ?? panel.label}
          </button>
        ))}
      </nav>
      <div className="playground-layout-content">
        <Mosaic<string>
          className="playground-mosaic"
          value={layout}
          onChange={value => value && setLayout(value)}
          renderTile={id => (
            <section id={`playground-panel-${id}`} className="playground-panel" data-playground-panel={id} aria-label={labels[id] ?? panels.find(panel => panel.id === id)?.label}>
              {elements[id]}
            </section>
          )}
        />
      </div>
    </div>
  );
}
