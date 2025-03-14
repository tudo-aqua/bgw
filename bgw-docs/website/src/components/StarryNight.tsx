import React, { useEffect, useState } from "react";
import { common, createStarryNight } from "@wooorm/starry-night";
import { toJsxRuntime } from "hast-util-to-jsx-runtime";
import { Fragment, jsx, jsxs } from "react/jsx-runtime";
import kotlinGrammar from "../lib/source.kotlin.js"; // Import your custom Kotlin grammar

import "./StarryNight.scss";

async function highlight(language: string, code: string) {
  // Initialize starry-night with common grammars plus your custom Kotlin grammar
  // The custom grammar will override the built-in one with the same name
  const starryNight = await createStarryNight([...common, kotlinGrammar]);

  // Get grammar for the specified language
  const scope = starryNight.flagToScope(language) || "kotlin";

  // Highlight the code
  const tree = starryNight.highlight(code, scope);

  // Convert the syntax tree to HTML string
  const reactNode = toJsxRuntime(tree, { Fragment, jsx, jsxs });

  return reactNode;
}

const StarryNight = ({
  code,
  language,
}: {
  code: string;
  language: string;
}) => {
  const [highlighted, setHighlighted] = useState("");

  useEffect(() => {
    highlight(language, code).then((reactNode) => {
      setHighlighted(reactNode);
    });
  }, [code, language]);

  return (
    <div className="starry-night-container" style={{ whiteSpace: "pre" }}>
      {highlighted}
    </div>
  );
};

export default StarryNight;
