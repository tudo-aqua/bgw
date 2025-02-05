import { FC, useEffect, useRef, useState } from "react";
import KotlinPlayground from "kotlin-playground";
import { Component, ReactNode } from "react";

export interface ReactKotlinPlaygroundProps {
  className?: string;
  children?: ReactNode;

  // Make all event handlers optional
  onChange?: (event: any) => void;
  onConsoleOpen?: (event: any) => void;
  onConsoleClose?: (event: any) => void;
  onLoaded?: (event: any) => void;
  getInstance?: (instance: any) => void;
  getJsCode?: (code: string) => void;
  onRun?: (event: any) => void;
  onError?: (error: any) => void;

  version: string;
  args: string | string[];
  targetPlatform: "junit" | "canvas" | "js" | "java" | "jvm";
  highlightOnly: null | "nocursor" | "readonly" | "true";
  jsLibs: string;
  autoIndent: boolean | number;
  theme: string;
  mode:
    | "kotlin"
    | "js"
    | "java"
    | "groovy"
    | "xml"
    | "c"
    | "shell"
    | "swift"
    | "obj-c";
  minCompilerVersion: string;
  autocomplete: boolean;
  highlightOnFly: boolean;
  indent: number;
  lines: boolean;
  from: number;
  to: number;
  outputHeight: number;
  matchBrackets: boolean;
  shorterHeight: number;
  server: string;
}

const EVENTS = [
  "onChange",
  "onConsoleOpen",
  "onConsoleClose",
  "onLoaded",
  "getInstance",
  "getJsCode",
  "onRun",
  "onError",
];

const DATA_ATTRS = [
  "server",
  "version",
  "targetPlatform",
  "highlightOnly",
  "jsLibs",
  "minCompilerVersion",
  "autocomplete",
  "outputHeight",
  "trackRunId",
  "crosslink",
  "shorterHeight",
];

function upper2dash(str: string): string {
  return str.replace(/[A-Z]/g, "-$&").toLowerCase();
}

function normalizeAttribute(name: string): string {
  let attr = name;
  if (DATA_ATTRS.indexOf(name) !== -1) attr = "data-" + attr;
  return upper2dash(attr);
}

const ReactKotlinPlayground: FC<
  Omit<ReactKotlinPlaygroundProps, "playground">
> = ({ className, children, ...props }) => {
  const codeRef = useRef<HTMLElement>(null);
  const [isPlaygroundLoaded, setIsPlaygroundLoaded] = useState(false);

  const createEventProxy =
    (name: string) =>
    (...args: any[]) => {
      if (props[name]) props[name](...args);
    };

  const initPlayground = () => {
    const isInited =
      codeRef.current &&
      codeRef.current.getAttribute("data-kotlin-playground-initialized") ===
        "true";

    if (!isInited && codeRef.current) {
      // Create event handlers object
      const eventFunctions = EVENTS.reduce((events, name) => {
        // Special handling for onLoaded
        if (name === "onLoaded") {
          events[name] = (...args: any[]) => {
            // setIsPlaygroundLoaded(true);
            // if (props.onLoaded) {
            //   props.onLoaded(...args);
            // }
          };
        } else {
          events[name] = createEventProxy(name);
        }
        return events;
      }, {} as Record<string, Function>);

      // Initialize the playground with event handlers
      KotlinPlayground(codeRef.current, {
        ...eventFunctions,
        server: props.server,
        callback: (targetNode: HTMLElement, mountNode: HTMLElement) => {
          setIsPlaygroundLoaded(true);
          if (props.onLoaded()) {
            props.onLoaded();
          }
        },
      });
    }
  };

  useEffect(() => {
    initPlayground();
  }, []);

  const elementProps = Object.keys(props).reduce((result, name) => {
    if (EVENTS.indexOf(name) === -1)
      result[normalizeAttribute(name)] = props[name];
    return result;
  }, {} as Record<string, any>);

  return (
    <div
      className={`${className} ${!isPlaygroundLoaded ? "animate-pulse" : ""}`}
    >
      <code
        {...elementProps}
        ref={codeRef}
        className="text-sm px-4 py-5 text-background/0"
      >
        {children}
      </code>
    </div>
  );
};

export default ReactKotlinPlayground;
