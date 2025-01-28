import React, { useEffect } from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import { render } from "react-dom";

import "./codePreview.scss";
import { ReactKotlinPlaygroundProps } from "@/lib/kotlin-playground/ReactKotlinPlayground";
import asyncComponent from "./asyncComponent";
import { Button } from "@/components/ui/button";
import { Link } from "react-router-dom";
import { createKotlinCodeLinebreaks } from "@/lib/utils";

const AsyncPlayground = asyncComponent(
  () => import("@/lib/kotlin-playground/ReactKotlinPlayground")
);

function PreviewTab({ data, info }: { data; info }) {
  let code = data.sample; //.replace(/\n/g, "").replace(/\s+/g, " ");
  code = createKotlinCodeLinebreaks(code, 0);
  //console.log(code);
  let msg = JSON.parse(info[data.codepoint[0]]);
  let doc = data.doc;

  let isAnimation = data.codepoint.length > 1;

  const randomId = Math.random().toString(36).substring(7);
  msg = { container: `bgw-root-${randomId}`, props: msg };
  if (msg.props.data.type === "AppData") {
    msg.props.data.width = 622;
    msg.props.data.height = 300;
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

  return (
    <div>
      <Tabs
        defaultValue="preview"
        className="w-full preview-tabs relative"
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
          <div className="inline-flex h-10 items-center justify-center rounded-xl text-muted-foreground absolute right-0 gap-2">
            <Button
              className="inline-flex gap-2 items-center justify-center h-10 whitespace-nowrap rounded-lg bg-muted/50 transition-all hover:text-white text-muted-foreground hover:bg-muted/80 aspect-square"
              onClick={() => resetAnimation()}
            >
              <i className="material-symbols-rounded text-xl">rotate_left</i>
              Reset
            </Button>
            <Button
              className="inline-flex gap-2 items-center justify-center h-10 whitespace-nowrap rounded-lg bg-muted/50 transition-all hover:text-white text-muted-foreground hover:bg-muted/80 aspect-square"
              onClick={() => {
                console.log("anim", anim);
                sendJsonMessage(anim);
              }}
            >
              <i className="material-symbols-rounded text-xl">
                slow_motion_video
              </i>
              Animate
            </Button>
          </div>
        )}
        <TabsContent value="preview">
          <div className="bgw-root-container flex !h-[300px] justify-center items-center !relative">
            <div
              className="w-full h-full rounded-xl bgw-root !bg-card border !bg-[#161d29] border-none"
              id={`bgw-root-${randomId}`}
            ></div>
          </div>
        </TabsContent>
        <TabsContent className="h-[300px]" value="code" ref={codeRef}>
          <div className="relative h-[300px]">
            <Button
              className="absolute right-5 top-4 z-10 h-8 w-8 p-0 hover:bg-muted/80 text-muted-foreground hover:text-white"
              variant="ghost"
              onClick={handleCopyCode}
            >
              <i className="material-symbols-rounded text-xl">content_copy</i>
            </Button>
            <ScrollArea
              className="h-full relative pr-5 col-span-5 flex bg-[#161d29] rounded-xl"
              x-chunk="dashboard-03-chunk-0"
            >
              <AsyncPlayground
                className="h-full relative pr-5 pl-3 py-2 flex rounded-xl pointer-events-none"
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
        <fieldset className="-mt-3 bg-background p-5 pt-8 rounded-b-xl text-muted-foreground">
          <p>{doc}</p>
        </fieldset>
      )}
    </div>
  );
}

export default PreviewTab;
