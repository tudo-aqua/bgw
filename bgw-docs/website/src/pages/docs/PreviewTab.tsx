import React, { useEffect } from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import { render } from "react-dom";

import "./codePreview.scss";
import { ReactKotlinPlaygroundProps } from "@/components/ReactKotlinPlayground";
import asyncComponent from "./asyncComponent";
import { Button } from "@/components/ui/button";
import { Link } from "react-router-dom";
import { createKotlinCodeLinebreaks } from "@/lib/utils";
import { Badge } from "@/components/ui/badge";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip";

const AsyncPlayground = asyncComponent(
  () => import("@/components/ReactKotlinPlayground")
);

function PreviewTab({
  data,
  info,
  reformat = true,
  sideBySide = false,
  width = 622,
}: {
  data;
  info;
  reformat?: boolean;
  sideBySide?: boolean;
  width?: number;
}) {
  let code = data.sample; //.replace(/\n/g, "").replace(/\s+/g, " ");
  if (reformat) code = createKotlinCodeLinebreaks(code, 0);

  let msg = JSON.parse(info[data.codepoint[0]]);
  let doc = data.doc;

  let isAnimation = data.codepoint.length > 1;

  const replaceImageVisualPathsRecursively = (obj, path) => {
    if (typeof obj === "object") {
      for (let key in obj) {
        if (["currentVisual", "visual", "front", "back"].includes(key)) {
          if (obj[key].children && obj[key].children.length > 0) {
            obj[key].children.forEach((child, index) => {
              if (child.type === "ImageVisualData") {
                obj[key].children[index].path = obj[key].children[
                  index
                ].path.replace(/http.+\/static/gm, path);
              }
            });
          }
        } else {
          replaceImageVisualPathsRecursively(obj[key], path);
        }
      }
    }
  };

  const randomId = Math.random().toString(36).substring(7);
  msg = { container: `bgw-root-${randomId}`, props: msg };
  if (msg.props.data.type === "AppData") {
    msg.props.data.width = width;
    msg.props.data.height = 300;

    replaceImageVisualPathsRecursively(msg.props.data.gameScene, `/bgw`);
  }

  const sendJsonMessage = (message) => {
    const event = new CustomEvent("BGW_MSG", {
      detail: JSON.stringify(message),
    });

    document.dispatchEvent(event);
  };

  const [currentTab, setCurrentTab] = React.useState("preview");
  let anim = null;

  if (data.codepoint.length > 1) {
    anim = JSON.parse(info[data.codepoint[1]]);
    anim.data.componentView = msg.props.data.gameScene.components[0];
    anim = { container: `bgw-root-${randomId}`, props: anim };

    /* let randomContainerId = Math.random().toString(36).substring(7);
    msg.props.data.gameScene.components[0].id = randomContainerId;
    anim.props.data.componentView.id = randomContainerId; */
  }

  useEffect(() => {
    if (currentTab !== "code") {
      console.log(msg);
      sendJsonMessage(msg);
      return;
    }
  }, [randomId, currentTab]);

  const codeRef = React.useRef(null);

  const props: Partial<ReactKotlinPlaygroundProps> = {
    targetPlatform: "jvm",
    theme: "default",
    highlightOnly: "true",
    autoIndent: true,
    onLoaded: (event: any) => {},
  };

  function resetAnimation() {
    document
      .getElementById(
        msg.props.data.gameScene.components[0].id +
          "--" +
          anim.props.data.animationType
      )
      .remove();
    sendJsonMessage(msg);
    // document
    //   .getElementById(msg.props.data.gameScene.components[0].id)
    //   .classList.remove(
    //     msg.props.data.gameScene.components[0].id +
    //       "--" +
    //       anim.props.data.animationType
    //   );
  }

  const handleCopyCode = () => {
    navigator.clipboard.writeText(code);
  };

  if (sideBySide) {
    return (
      <div className="flex gap-4 preview-tabs max-xl:flex-col max-xl:w-[calc(100vw-3.5rem)]">
        <div className="w-2/5 max-xl:w-full">
          <div className="relative">
            {isAnimation && (
              <div className="absolute top-0 right-0 z-10 inline-flex items-center justify-center h-10 gap-2 rounded-xl text-muted-foreground">
                <Button
                  className="inline-flex items-center justify-center h-10 gap-2 transition-all rounded-lg whitespace-nowrap bg-muted/50 hover:text-white text-muted-foreground hover:bg-muted/80 aspect-square"
                  onClick={() => resetAnimation()}
                >
                  <i className="text-xl material-symbols-rounded">
                    rotate_left
                  </i>
                  Reset
                </Button>
                <Button
                  className="inline-flex items-center justify-center h-10 gap-2 transition-all rounded-lg whitespace-nowrap bg-muted/50 hover:text-white text-muted-foreground hover:bg-muted/80 aspect-square"
                  onClick={() => {
                    sendJsonMessage(anim);
                  }}
                >
                  <i className="text-xl material-symbols-rounded">
                    slow_motion_video
                  </i>
                  Animate
                </Button>
              </div>
            )}
            <div className="bgw-root-container flex !h-[300px] justify-center items-center !relative">
              <div
                className="w-full h-full rounded-xl bgw-root border !bg-[#161d29] border-none"
                id={`bgw-root-${randomId}`}
              ></div>
            </div>
            <Tooltip delayDuration={700} align="start" sideOffset={5}>
              <TooltipTrigger asChild>
                <img
                  src="/bgw/logo.svg"
                  alt="Preview"
                  className="absolute w-6 bottom-3 left-4 opacity-20 hover:opacity-100 cursor-help"
                />
              </TooltipTrigger>
              <TooltipContent>
                <p>BGW Live-Preview</p>
              </TooltipContent>
            </Tooltip>
          </div>
          {doc && (
            <fieldset className="p-5 pt-8 -mt-3 bg-background rounded-b-xl text-muted-foreground">
              <p>{doc}</p>
            </fieldset>
          )}
        </div>
        <div className="w-3/5 relative h-[300px] max-xl:w-full">
          <Button
            className="absolute z-10 w-8 h-8 p-0 right-5 top-4 hover:bg-muted/80 text-muted-foreground hover:text-white max-md:hidden"
            variant="ghost"
            onClick={handleCopyCode}
          >
            <i className="text-xl material-symbols-rounded">content_copy</i>
          </Button>
          <ScrollArea
            className="h-full relative pr-5 col-span-5 flex bg-[#161d29] rounded-xl"
            x-chunk="dashboard-03-chunk-0"
          >
            <AsyncPlayground
              className="relative flex h-full py-2 pl-3 pr-5 pointer-events-none rounded-xl"
              id={`kotlin__playground-${randomId}`}
              {...props}
              key={code + location.pathname}
            >
              {code}
            </AsyncPlayground>
            <ScrollBar orientation="horizontal"></ScrollBar>
            <ScrollBar orientation="vertical"></ScrollBar>
          </ScrollArea>
        </div>
      </div>
    );
  }

  return (
    <div>
      <Tabs
        defaultValue="preview"
        className="relative w-full preview-tabs"
        onValueChange={(newValue) => {
          setCurrentTab(newValue);
          if (newValue === "preview") {
            codeRef.current.style.display = "none";
            codeRef.current.scrollTo(0, 0);
          } else {
            codeRef.current.style.display = "block";
          }
        }}
      >
        <TabsList>
          <TabsTrigger value="preview">Preview</TabsTrigger>
          <TabsTrigger value="code">Code</TabsTrigger>
        </TabsList>
        {isAnimation && (
          <div className="absolute right-0 inline-flex items-center justify-center h-10 gap-2 rounded-xl text-muted-foreground">
            <Button
              className="inline-flex items-center justify-center h-10 gap-2 transition-all rounded-lg whitespace-nowrap bg-muted/50 hover:text-white text-muted-foreground hover:bg-muted/80 aspect-square"
              onClick={() => resetAnimation()}
            >
              <i className="text-xl material-symbols-rounded">rotate_left</i>
              Reset
            </Button>
            <Button
              className="inline-flex items-center justify-center h-10 gap-2 transition-all rounded-lg whitespace-nowrap bg-muted/50 hover:text-white text-muted-foreground hover:bg-muted/80 aspect-square"
              onClick={() => {
                sendJsonMessage(anim);
              }}
            >
              <i className="text-xl material-symbols-rounded">
                slow_motion_video
              </i>
              Animate
            </Button>
          </div>
        )}
        <TabsContent value="preview">
          <div className="bgw-root-container flex !h-[300px] justify-center items-center !relative">
            <div
              className="w-full h-full rounded-xl bgw-root border !bg-[#161d29] border-none"
              id={`bgw-root-${randomId}`}
            ></div>
          </div>
        </TabsContent>
        <TabsContent className="h-[300px]" value="code" ref={codeRef}>
          <div className="relative h-[300px]">
            <Button
              className="absolute z-10 w-8 h-8 p-0 right-5 top-4 hover:bg-muted/80 text-muted-foreground hover:text-white"
              variant="ghost"
              onClick={handleCopyCode}
            >
              <i className="text-xl material-symbols-rounded">content_copy</i>
            </Button>
            <ScrollArea
              className="h-full relative pr-5 col-span-5 flex bg-[#161d29] rounded-xl"
              x-chunk="dashboard-03-chunk-0"
            >
              <AsyncPlayground
                className="relative flex h-full py-2 pl-3 pr-5 pointer-events-none rounded-xl"
                id={`kotlin__playground-${randomId}`}
                {...props}
                key={code + location.pathname}
              >
                {code}
              </AsyncPlayground>
              <ScrollBar orientation="horizontal"></ScrollBar>
              <ScrollBar orientation="vertical"></ScrollBar>
            </ScrollArea>
          </div>
        </TabsContent>
      </Tabs>
      {doc && (
        <fieldset className="p-5 pt-8 -mt-3 bg-background rounded-b-xl text-muted-foreground">
          <p>{doc}</p>
        </fieldset>
      )}
    </div>
  );
}

export default PreviewTab;
