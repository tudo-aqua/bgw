import React, {
  useEffect,
  useRef,
  useMemo,
  useState,
  useLayoutEffect,
  Suspense,
} from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import { render, unmountComponentAtNode } from "react-dom";

import "./codePreview.scss";
import { createRoot } from "react-dom/client";
import ReactKotlinPlayground, {
  ReactKotlinPlaygroundProps,
} from "@/lib/kotlin-playground/ReactKotlinPlayground";
import { useDocsStore } from "@/stores/docsStore";
import asyncComponent from "./asyncComponent";
import { useLocation } from "react-router";
import { Button } from "@/components/ui/button";

const AsyncPlayground = asyncComponent(
  () => import("@/lib/kotlin-playground/ReactKotlinPlayground")
);

const CodeTab = React.memo(function CodeTab({
  code,
  autoIndent = false,
  copy = false,
  onLoad,
}: {
  code: string;
  autoIndent: boolean;
  copy?: boolean;
  onLoad?: () => void;
}) {
  const [isLoading, setIsLoading] = useState(true);
  const location = useLocation();
  const { incrementLoadedCodeBlocks, incrementToLoadCodeBlocks } =
    useDocsStore();

  const props: Partial<ReactKotlinPlaygroundProps> = {
    targetPlatform: "jvm",
    theme: "default",
    highlightOnly: true,
    autoIndent: autoIndent,
    onLoaded: (event: any) => {
      // incrementLoadedCodeBlocks();
      setIsLoading(false);
      onLoad?.();
    },
  };

  const handleCopyCode = () => {
    navigator.clipboard.writeText(code);
  };

  return (
    <div className="relative w-full">
      {copy && (
        <Button
          className="absolute right-4 top-4 z-10 h-8 w-8 p-0 hover:bg-muted/80 text-muted-foreground hover:text-white"
          variant="ghost"
          onClick={handleCopyCode}
        >
          <i className="material-symbols-rounded text-xl">content_copy</i>
        </Button>
      )}
      <AsyncPlayground
        className="h-full relative pr-5 pl-3 col-span-5 flex bg-muted/50 rounded-xl pointer-events-none"
        {...props}
        key={code + location.pathname}
      >
        {code}
      </AsyncPlayground>
    </div>
  );
});

export default CodeTab;
