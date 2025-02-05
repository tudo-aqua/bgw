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
} from "@/components/ReactKotlinPlayground";
import { useDocsStore } from "@/stores/docsStore";
import asyncComponent from "./asyncComponent";
import { useLocation } from "react-router";
import { Button } from "@/components/ui/button";

const AsyncPlayground = asyncComponent(
  () => import("@/components/ReactKotlinPlayground")
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
    <div className="relative w-full max-w-full code-tab">
      {copy && (
        <Button
          className="absolute z-10 w-8 h-8 p-0 right-4 top-4 hover:bg-muted/80 text-muted-foreground hover:text-white max-xl:hidden"
          variant="ghost"
          onClick={handleCopyCode}
        >
          <i className="text-xl material-symbols-rounded">content_copy</i>
        </Button>
      )}
      <ScrollArea className="max-w-full overflow-scroll max-xl:w-full bg-muted/50 rounded-xl xl:overflow-hidden">
        <AsyncPlayground
          className="relative flex h-full col-span-5 pl-3 pr-5 pointer-events-none"
          {...props}
          key={code + location.pathname}
        >
          {code}
        </AsyncPlayground>
        <ScrollBar orientation="horizontal" />
      </ScrollArea>
    </div>
  );
});

export default CodeTab;
