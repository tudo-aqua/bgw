import React from "react";
import { Link } from "react-router-dom";
import { Badge } from "@/components/ui/badge";

// Parse markdown links and return JSX elements
export function parseMarkdown(
  text: string,
  wrapInCode: boolean = true
): React.ReactNode[] {
  if (!text) return [];

  const linkRegex = /\[([^\]]*)\]\(([^)]*)\)/g;
  let lastIndex = 0;
  const elements: React.ReactNode[] = [];
  let match;

  while ((match = linkRegex.exec(text)) !== null) {
    // Add text before the link
    if (match.index > lastIndex) {
      elements.push(
        <span key={lastIndex}>{text.slice(lastIndex, match.index)}</span>
      );
    }

    // Add the link, use link text or target if text is empty
    const linkText = match[1] || match[2];
    let linkTarget = match[2] ? `/docs/${match[2]}` : `#${match[1]}`;

    if (text.includes("https://") || text.includes("http://")) {
      linkTarget = match[2];
    }

    if (linkTarget.indexOf("#") !== -1) {
      elements.push(
        wrapInCode ? (
          <code className="relative font-mono rounded">{linkText}</code>
        ) : (
          linkText
        )
      );
    } else if (linkTarget.startsWith("https")) {
      elements.push(
        wrapInCode ? (
          <code className="relative font-mono rounded">
            <Link
              className="text-primary"
              key={match.index}
              to={linkTarget}
              target="_blank"
            >
              {linkText}
            </Link>
          </code>
        ) : (
          <Link
            className="font-mono text-primary"
            key={match.index}
            to={linkTarget}
            target="_blank"
          >
            {linkText}
          </Link>
        )
      );
    } else {
      elements.push(
        wrapInCode ? (
          <code className="relative font-mono rounded">
            <Link className="text-primary" key={match.index} to={linkTarget}>
              {linkText}
            </Link>
          </code>
        ) : (
          <Link
            className="font-mono text-primary"
            key={match.index}
            to={linkTarget}
          >
            {linkText}
          </Link>
        )
      );
    }

    lastIndex = linkRegex.lastIndex;
  }

  // Add remaining text
  if (lastIndex < text.length) {
    elements.push(<span key={lastIndex}>{text.slice(lastIndex)}</span>);
  }

  return elements;
}

// Parse class documentation with special formatting for headers
export function parseClassDoc(text: string): React.ReactNode[] {
  if (!text) return [];

  // Split text into paragraphs
  const paragraphs = text.split("\n\n").filter((p) => p.trim());
  let previousWasColon = false;

  return paragraphs.map((paragraph, paragraphIndex) => {
    const elements = parseMarkdown(paragraph, false);

    // Check if single element ending with colon
    const isSingleElementWithColon =
      typeof paragraph === "string" && paragraph.trim().endsWith(":");

    const element = (
      <p
        key={paragraphIndex}
        className={`${
          isSingleElementWithColon ? "font-semibold docs__header -mb-1" : ""
        } ${previousWasColon ? "docs__text" : ""}`}
      >
        {elements}
      </p>
    );

    previousWasColon = isSingleElementWithColon;
    return element;
  });
}

export const parseMarkdownLinks = (text: string): JSX.Element[] => {
  return parseMarkdownLinksNoCode(text);
};

export function parseMarkdownLinksNoCode(text: string): JSX.Element[] {
  if (!text) return [];

  const linkRegex = /\[([^\]]*)\]\(([^)]*)\)/g;
  let lastIndex = 0;
  const elements: JSX.Element[] = [];
  let match;

  while ((match = linkRegex.exec(text)) !== null) {
    // Add text before the link
    if (match.index > lastIndex) {
      elements.push(
        <span key={lastIndex}>{text.slice(lastIndex, match.index)}</span>
      );
    }

    // Add the link, use link text or target if text is empty
    const linkText = match[1] || match[2];
    let linkTarget = match[2] ? `/docs/${match[2]}` : `#${match[1]}`;

    if (text.includes("https://") || text.includes("http://")) {
      linkTarget = match[2];
    }
    if (linkTarget.indexOf("#") != -1) {
      elements.push(linkText);
    } else if (linkTarget.startsWith("https")) {
      elements.push(
        <code>
          <Link
            className="font-mono text-primary"
            key={match.index}
            to={linkTarget}
            target="_blank"
          >
            {linkText}
          </Link>
        </code>
      );
    } else {
      elements.push(
        <code>
          <Link
            className="font-mono text-primary"
            key={match.index}
            to={linkTarget}
          >
            {linkText}
          </Link>
        </code>
      );
    }

    lastIndex = linkRegex.lastIndex;
  }

  // Add remaining text
  if (lastIndex < text.length) {
    elements.push(<span key={lastIndex}>{text.slice(lastIndex)}</span>);
  }

  return elements;
}

export function parseMarkdownNoLinks(text: string): JSX.Element[] {
  if (!text) return [];

  const linkRegex = /\[([^\]]*)\]\(([^)]*)\)/g;
  let lastIndex = 0;
  const elements: JSX.Element[] = [];
  let match;

  while ((match = linkRegex.exec(text)) !== null) {
    // Add text before the link
    if (match.index > lastIndex) {
      elements.push(
        <span key={lastIndex}>{text.slice(lastIndex, match.index)}</span>
      );
    }

    // Add the link, use link text or target if text is empty
    const linkText = match[1] || match[2];
    let linkTarget = match[2] ? `/docs/${match[2]}` : `#${match[1]}`;

    if (text.includes("https://") || text.includes("http://")) {
      linkTarget = match[2];
    }
    if (linkTarget.indexOf("#") != -1) {
      elements.push(linkText);
    } else if (linkTarget.startsWith("https")) {
      elements.push(<span>{linkText}</span>);
    } else {
      elements.push(<span>{linkText}</span>);
    }

    lastIndex = linkRegex.lastIndex;
  }

  // Add remaining text
  if (lastIndex < text.length) {
    elements.push(<span key={lastIndex}>{text.slice(lastIndex)}</span>);
  }

  return elements;
}

// Build banner notifications for components
export function buildBanners(
  c: any,
  full: any,
  current: any,
  dirs: any,
  currentComponent: any
) {
  const banners: React.ReactNode[] = [];

  // Deprecated banner
  if (c.deprecated) {
    banners.push(
      <div
        className="flex items-center gap-8 px-4 py-2 text-red-500 bg-destructive/10 rounded-xl docs-banner"
        key="deprecated-banner"
      >
        <div className="flex items-center gap-3">
          <i className="text-lg text-red-500 material-symbols-rounded">
            running_with_errors
          </i>
          <h4 className="font-bold">Deprecated</h4>
        </div>
        <p>
          This API-Endpoint will be removed in future versions.{" "}
          {c.deprecated.description}
        </p>
      </div>
    );
  }

  // Companion object banner
  if (current && current["Companion"] && current["Companion"]._index) {
    banners.push(
      <Link
        to={"/docs/" + current["Companion"]._index.breadcrumbs.join("/")}
        key="companion-banner"
      >
        <div className="relative flex items-center gap-8 px-4 py-2 bg-bgw-blue/10 text-bgw-blue rounded-xl docs-banner">
          <div className="flex items-center gap-3">
            <i className="text-lg material-symbols-rounded text-bgw-blue">
              note_stack_add
            </i>
            <h4 className="font-bold">Companion</h4>
          </div>
          <p>
            This API-Endpoint may have additional properties in its Companion
            object.
          </p>
          <i className="absolute text-xl material-symbols-rounded text-bgw-blue right-4">
            chevron_right
          </i>
        </div>
      </Link>
    );
  }

  // Companion context banner
  if (full && full.breadcrumbs.includes("Companion")) {
    banners.push(
      <Link
        to={"/docs/" + full.breadcrumbs.excludeLast().join("/")}
        key="companion-context-banner"
      >
        <div className="relative flex items-center gap-8 px-4 py-2 bg-bgw-green/10 text-bgw-green rounded-xl docs-banner">
          <div className="flex items-center gap-3">
            <i className="text-lg material-symbols-rounded text-bgw-green">
              reply
            </i>
            <h4 className="font-bold">Context</h4>
          </div>
          <p>
            This Companion object may contain additional static properties for{" "}
            {full.breadcrumbs.fromRight(1)}.
          </p>
        </div>
      </Link>
    );
  }

  // Property context banner
  if (current && !full && current.breadcrumbs?.includes("Companion")) {
    banners.push(
      <Link
        to={"/docs/" + current.breadcrumbs.excludeLast(2).join("/")}
        key="property-context-banner"
      >
        <div className="relative flex items-center gap-8 px-4 py-2 bg-bgw-green/10 text-bgw-green rounded-xl docs-banner">
          <div className="flex items-center gap-3">
            <i className="text-lg material-symbols-rounded text-bgw-green">
              reply
            </i>
            <h4 className="font-bold">Context</h4>
          </div>
          <p>
            This property is part of the Companion object for{" "}
            {current.breadcrumbs.fromRight(2)}.
          </p>
        </div>
      </Link>
    );
  }

  // Constructor details banner
  if (
    !full &&
    current.breadcrumbs &&
    current.breadcrumbs.length > 2 &&
    current.breadcrumbs.fromRight(0) === current.breadcrumbs.fromRight(1)
  ) {
    banners.push(
      <Link
        to={"/docs/" + current.breadcrumbs.excludeLast(1).join("/")}
        key="constructor-context-banner"
      >
        <div className="relative flex items-center gap-8 px-4 py-2 bg-bgw-green/10 text-bgw-green rounded-xl docs-banner">
          <div className="flex items-center gap-3">
            <i className="text-lg material-symbols-rounded text-bgw-green">
              reply
            </i>
            <h4 className="font-bold">Context</h4>
          </div>
          <p>
            This reference includes additional constructor details for{" "}
            {current.breadcrumbs.fromRight(1)}.
          </p>
        </div>
      </Link>
    );
  }

  // Nested type banners
  if (
    full &&
    full.members &&
    full.members.types &&
    full.members.types.length > 0
  ) {
    full.members.types.forEach((type: any, index: number) => {
      if (type.name === "Companion") return;
      banners.push(
        <Link to={"/docs/" + type.link} key={`type-banner-${index}`}>
          <div className="relative flex items-center gap-8 px-4 py-2 bg-bgw-blue/10 text-bgw-blue rounded-xl docs-banner">
            <div className="flex items-center gap-3">
              <i className="text-lg material-symbols-rounded text-bgw-blue">
                note_stack
              </i>
              <h4 className="font-bold">
                {c.info.name}.{type.name}
              </h4>
            </div>
            <p>{parseMarkdownNoLinks(type.doc)}</p>
            <i className="absolute text-xl material-symbols-rounded text-bgw-blue right-4">
              chevron_right
            </i>
          </div>
        </Link>
      );
    });
  }

  return banners;
}

// Generate badge for component type
export function generateComponentTypeBadge(component: any): React.ReactNode {
  if (!component) return null;

  if (component.info?.tags?.type) {
    return (
      <Badge variant={component.info.tags.type}>
        {component.info.tags.type.capitalize()}
      </Badge>
    );
  } else if (component.type !== "extra") {
    if (component.info.signature.includes("fun ")) {
      return <Badge variant="function">Function</Badge>;
    } else if (component.info.signature.includes("const ")) {
      return <Badge variant="constant">Constant</Badge>;
    } else if (
      component.info.signature.includes("val ") ||
      component.info.signature.includes("var ")
    ) {
      const isEventListener =
        component.info.name.startsWith("on") &&
        component.info.signature.indexOf("->") > 0;

      return isEventListener ? (
        <Badge variant="listener">Listener</Badge>
      ) : (
        <Badge variant="property">Property</Badge>
      );
    } else if (component.info.name.toUpperCase() === component.info.name) {
      return <Badge variant="enum">Value</Badge>;
    }
  }

  return null;
}
