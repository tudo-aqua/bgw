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
      try {
        const generatedCode = createKotlinCodeLinebreaks(
          exportComponent(null, getOutputElements(true), selectedComponentId)
        );
        setCode(generatedCode);
      } catch (error) {
        console.error("Failed to generate code:", error);
      }
    }
  }, [selectedComponentId, getOutputElements, autoUpdate]);

  const handleManualUpdate = () => {
    try {
      const generatedCode = createKotlinCodeLinebreaks(
        exportComponent(null, getOutputElements(true), selectedComponentId)
      );
      setCode(generatedCode);
    } catch (error) {
      console.error("Failed to generate code:", error);
    }
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

      <div className="flex flex-row items-center justify-between">
        <div className="flex flex-row items-center gap-3">
          <h2 className="text-sm font-bold">Generated Code</h2>
        </div>
        <i
          className="text-lg transition-colors cursor-pointer material-symbols-rounded text-white/70 hover:text-white"
          onClick={handleManualUpdate}
        >
          refresh
        </i>
      </div>

      <CodeTab code={code} autoIndent={false} copy={true} key="live-code-tab" />
    </>
  );
};

export default LiveCodeTab;
