import { useState } from "react";
import { Button } from "./ui/button";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import CodeExport from "./CodeExport";
import { exportComponent, exportScene, sceneClassName } from "@/lib/exporter";
import { PlaygroundData } from "@/lib/playground";

export default function PlaygroundExport({ snapshot, selectedId, width, height, background, sceneName, sceneNames }: {
  snapshot: { components: PlaygroundData[]; allComponentsCopy: Record<string, PlaygroundData>; errors: Record<string, string> };
  selectedId: string;
  width: number;
  height: number;
  background: string;
  sceneName: string;
  sceneNames: Record<string, string>;
}) {
  const [scope, setScope] = useState("scene");
  const [wrap, setWrap] = useState(false);
  const [status, setStatus] = useState("");
  const selected = snapshot.allComponentsCopy[selectedId];
  let code = "";
  let error = "";
  try {
    const issue = scope === "scene" ? Object.values(snapshot.errors)[0] : snapshot.errors[selectedId];
    if (issue) error = issue;
    else if (scope === "selected" && !selected) error = "Select a component to export.";
    else code = scope === "selected" ? exportComponent(selected) : exportScene(snapshot.components, width, height, background, sceneName, sceneNames);
  } catch (cause) {
    error = cause instanceof Error ? cause.message : "Could not generate the export.";
  }
  async function copy() {
    try {
      await navigator.clipboard.writeText(code);
      setStatus("Copied");
    } catch {
      setStatus("Copy failed. Select the code to copy it.");
    }
  }
  function download() {
    const url = URL.createObjectURL(new Blob([code], { type: "text/plain" }));
    const anchor = document.createElement("a");
    anchor.href = url;
    anchor.download = `${scope === "scene" ? sceneClassName(sceneName) : "Component"}.kt`;
    anchor.click();
    URL.revokeObjectURL(url);
  }
  return (
    <div className="playground-export">
      <div className="playground-export-toolbar">
        <Select value={scope} onValueChange={value => { setScope(value); setStatus(""); }}>
          <SelectTrigger aria-label="Export scope" className="w-[168px] min-w-0"><SelectValue /></SelectTrigger>
          <SelectContent>
            <SelectItem value="scene">Entire scene</SelectItem>
            <SelectItem value="selected">Selected component</SelectItem>
          </SelectContent>
        </Select>
        <div className="playground-export-actions">
          <Button variant="ghost" size="icon-sm" aria-label="Wrap code" aria-pressed={wrap} onClick={() => setWrap(!wrap)}><i className="material-symbols-rounded" aria-hidden="true">wrap_text</i></Button>
          <Button variant="ghost" size="icon-sm" aria-label="Download export" disabled={!!error} onClick={download}><i className="material-symbols-rounded" aria-hidden="true">download</i></Button>
          <Button variant="secondary" size="sm" disabled={!!error} onClick={copy}><i className="material-symbols-rounded mr-2" aria-hidden="true">{status === "Copied" ? "check" : "content_copy"}</i>{status === "Copied" ? "Copied" : "Copy"}</Button>
        </div>
      </div>
      {error ? <p className="playground-export-empty" role="status">{error}</p> : (
        <pre className={`playground-code ${wrap ? "playground-code-wrap" : ""}`} tabIndex={0} aria-label="Kotlin export"><code><CodeExport code={code} language="kotlin" /></code></pre>
      )}
      <span className="sr-only" role="status">{status}</span>
    </div>
  );
}
