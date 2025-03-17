import React, { useState, useEffect } from "react";
import { exportComponent } from "@/lib/exporter";
import CodeTab from "@/pages/docs/CodeTab";
import { Button } from "@/components/ui/button";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { createKotlinCodeLinebreaks } from "@/lib/utils";

const LiveCodeTab = ({ getOutputElements, selectedComponentId }) => {
  const [code, setCode] = useState("");
  const [autoUpdate, setAutoUpdate] = useState(true);

  useEffect(() => {
    if (selectedComponentId && autoUpdate) {
      let allComponents = getOutputElements(true).allComponentsCopy;
      let component = allComponents[selectedComponentId];
      if (!component) {
        console.error(
          "Component not found in allComponents",
          selectedComponentId
        );
        return;
      }

      // try {
      const generatedCode = createKotlinCodeLinebreaks(
        exportComponent(
          component,
          getOutputElements(true).components,
          selectedComponentId
        ),
        0
      );
      setCode(generatedCode);
      // } catch (error) {
      //   console.error("Failed to generate code:", error);
      // }
    }
  }, [selectedComponentId, getOutputElements, autoUpdate]);

  const handleManualUpdate = () => {
    try {
      const generatedCode = createKotlinCodeLinebreaks(
        exportComponent(
          null,
          getOutputElements(true).components,
          selectedComponentId
        ),
        0
      );
      setCode(generatedCode);
    } catch (error) {
      console.error("Failed to generate code:", error);
    }
  };

  const copyCode = (code) => () => {
    navigator.clipboard.writeText(code);
  };

  return (
    <>
      {/* <div className="flex items-center justify-between mb-2">
        <div className="flex items-center gap-2">
          <Button
            size="sm"
            variant="outline"
            onClick={handleManualUpdate}
            disabled={autoUpdate}
            className={autoUpdate ? "opacity-50" : ""}
          >
            <i className="mr-1 material-symbols-rounded">refresh</i>
            Update
          </Button>
          <Button
            size="sm"
            variant={autoUpdate ? "secondary" : "ghost"}
            onClick={() => setAutoUpdate(!autoUpdate)}
            className="flex items-center"
          >
            <i className="mr-1 material-symbols-rounded">
              {autoUpdate ? "toggle_on" : "toggle_off"}
            </i>
            Auto
          </Button>
        </div>
      </div> */}

      <div className="flex flex-col max-w-full p-5 px-5 overflow-hidden rounded-lg bg-background live-code">
        <div className="flex flex-row items-center justify-between mb-3">
          <h2 className="text-sm font-bold">Generated Code</h2>
          <div className="flex gap-3">
            <i
              className="text-lg transition-colors cursor-pointer material-symbols-rounded text-white/70 hover:text-white"
              onClick={copyCode(code)}
            >
              content_copy
            </i>
            <i
              className="text-lg transition-colors cursor-pointer material-symbols-rounded text-white/70 hover:text-white"
              onClick={handleManualUpdate}
            >
              refresh
            </i>
          </div>
        </div>

        <CodeTab
          code={
            // "// Generating code is still in alpha.\n" +
            // "// Correctness of the generated output is not guaranteed.\n" +
            code
          }
          autoIndent={false}
          copy={false}
          key="live-code-tab"
        />
        <p className="mt-4 ml-1 text-xs text-justify text-white/70">
          Code generation is still in alpha, correctness of the output is not
          guaranteed.
        </p>
      </div>
    </>
  );
};

export default LiveCodeTab;
