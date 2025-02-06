import React, { useEffect, useRef, useState } from "react";
import { render } from "react-dom";
import ReactKotlinPlayground from "@/components/ReactKotlinPlayground";

type PlaygroundProps = {
  code: string;
  open: boolean;
};

function Playground({ code, open }: PlaygroundProps) {
  let constructors = null;

  useEffect(() => {
    const getDocs = async () => {
      fetch("/bgw/constructors.json")
        .then((response) => response.json())
        .then((response) => {
          constructors = response;
        })
        .catch((err) => console.error(err));
    };
  }, []);

  const element = useRef();
  const props = {
    autoIndent: 2,
    targetPlatform: "jvm",
    theme: "default",
    "auto-indent": "true",
    highlightOnly: true,
    onChange: (code) => {
      handleCodeChange();
    },
    onLoaded: () => {
      handleCodeChange();
    },
  };

  const handleCodeChange = () => {
    const possibleComponents = constructors
      .filter((c) => {
        return c.icon === "class" || c.icon === "enum";
      })
      .map((c) => {
        return c.shortName;
      });

    document.querySelectorAll(".cm-variable").forEach((e) => {
      if (possibleComponents.find((c) => c === e.innerHTML)) {
        e.classList.add("cm-bgw");
      }
    });
  };

  useEffect(() => {
    let searchInt = setInterval(() => {
      let mirror = document.querySelectorAll(".CodeMirror-code");
      if (mirror.length > 0) {
        clearInterval(searchInt);
        handleCodeChange();
      }
    }, 20);
  }, [code, open]);

  return (
    <div className="w-full h-fit flex flex-row playground__readonly bg-muted/30 rounded-lg">
      <ReactKotlinPlayground
        {...props}
        className="pointer-events-none pl-2"
        key={"playground"}
      >
        {code}
      </ReactKotlinPlayground>
    </div>
  );
}

export default Playground;
