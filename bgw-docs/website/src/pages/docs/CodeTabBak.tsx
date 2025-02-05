import React, { useEffect, useRef, useState } from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import ReactKotlinPlayground from "@/lib/kotlin-playground";
import { createRoot } from "react-dom/client";

import "./codePreview.scss";

function CodeTabBak({
  code,
  autoIndent = false,
  onLoad,
}: {
  code: string;
  autoIndent: boolean;
  onLoad?: () => void;
}) {
  const element = useRef(null);
  const rootRef = useRef(null);
  const [isLoaded, setIsLoaded] = useState(false);

  useEffect(() => {
    const node = element.current;
    const props = {
      targetPlatform: "jvm",
      theme: "default",
      highlightOnly: true,
      autoIndent: autoIndent,
      key: code,
      onLoaded: () => completeLoad(),
    };

    if (node) {
      setIsLoaded(false);
      if (!rootRef.current) {
        rootRef.current = createRoot(node);
      }
      rootRef.current.render(
        React.createElement(ReactKotlinPlayground, props, code)
      );
    }
  }, [code, rootRef.current]);

  function completeLoad() {
    setIsLoaded(true);
    if (onLoad) {
      onLoad();
    }
  }

  return (
    <div
      className={`h-full relative pr-5 pl-3 col-span-5 flex bg-muted/50 rounded-xl pointer-events-none`}
      ref={element}
    ></div>
  );
}

export default CodeTabBak;
