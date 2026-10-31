import { useState } from "react";
import { Button } from "./ui/button";
import { Tabs, TabsList, TabsTrigger } from "./ui/tabs";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "./ui/select";
import CodeExport from "./CodeExport";
import { exportComponent, exportScene } from "@/lib/exporter";
import { createPreviewMessage, PlaygroundData, toPreviewData } from "@/lib/playground";

export default function PlaygroundExport({ snapshot, selectedId, width, height, background }: {
  snapshot: { components: PlaygroundData[]; allComponentsCopy: Record<string, PlaygroundData>; errors: Record<string, string> };
  selectedId: string;
  width: number;
  height: number;
  background: string;
}) {
  const [language, setLanguage] = useState("kotlin");
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
    else if (scope === "selected" && language === "json" && selected.type === "ToggleGroupData") error = "Select a button to export its group as JSON.";
    else if (language === "json") code = JSON.stringify(scope === "selected" ? toPreviewData(selected) : createPreviewMessage(snapshot.components, width, height, background), null, 2);
    else code = scope === "selected" ? exportComponent(selected) : exportScene(snapshot.components, width, height, background);
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
    const url = URL.createObjectURL(new Blob([code], { type: language === "json" ? "application/json" : "text/plain" }));
    const anchor = document.createElement("a");
    anchor.href = url;
    anchor.download = `${scope === "scene" ? "PlaygroundScene" : "Component"}.${language === "json" ? "json" : "kt"}`;
    anchor.click();
    URL.revokeObjectURL(url);
  }
  return (
    <div className="playground-export">
      <div className="playground-export-toolbar">
        <Tabs value={language} onValueChange={value => { setLanguage(value); setStatus(""); }}>
          <TabsList aria-label="Export format"><TabsTrigger value="kotlin">Kotlin</TabsTrigger><TabsTrigger value="json">JSON</TabsTrigger></TabsList>
        </Tabs>
        <Select value={scope} onValueChange={value => { setScope(value); setStatus(""); }}>
          <SelectTrigger aria-label="Export scope" className="w-[168px] min-w-0"><SelectValue /></SelectTrigger>
          <SelectContent>
            <SelectItem value="scene">Entire scene</SelectItem>
            <SelectItem value="selected">Selected component</SelectItem>
          </SelectContent>
        </Select>
        <div className="playground-export-actions">
          <Button variant="ghost" size="icon" aria-label="Wrap code" aria-pressed={wrap} onClick={() => setWrap(!wrap)}><i className="material-symbols-rounded" aria-hidden="true">wrap_text</i></Button>
          <Button variant="ghost" size="icon" aria-label="Download export" disabled={!!error} onClick={download}><i className="material-symbols-rounded" aria-hidden="true">download</i></Button>
          <Button variant="secondary" size="sm" disabled={!!error} onClick={copy}><i className="material-symbols-rounded mr-2" aria-hidden="true">{status === "Copied" ? "check" : "content_copy"}</i>{status === "Copied" ? "Copied" : "Copy"}</Button>
        </div>
      </div>
      {error ? <p className="playground-export-empty" role="status">{error}</p> : (
        <pre className={`playground-code ${wrap ? "playground-code-wrap" : ""}`} tabIndex={0} aria-label={`${language} export`}><code><CodeExport code={code} language={language} /></code></pre>
      )}
      <span className="sr-only" role="status">{status}</span>
    </div>
  );
}
