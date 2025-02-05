import React, { useEffect, useState, useMemo } from "react";
import ReactMarkdown from "react-markdown";
import { Prism as SyntaxHighlighter } from "react-syntax-highlighter";
import { atomOneDark } from "react-syntax-highlighter/dist/esm/styles/hljs";
import remarkGfm from "remark-gfm";
import rehypeRaw from "rehype-raw";
import Markdown from "react-markdown";

import "./markdown.scss";
import CodeTab from "./CodeTab";
import { Banner } from "@/components/ui/banner";
import { Link, useLocation } from "react-router-dom";
import CodeTabs from "./Tabs";
import { TabsContent } from "@/components/ui/tabs";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import { Collapsible } from "@/components/ui/collapsible";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import { useDocsStore } from "@/stores/docsStore";
import { guideStructure } from "@/lib/utils";
import { Separator } from "@/components/ui/separator";
import PreviewTab from "./PreviewTab";

const Guide = React.memo(({ allSamples }: { allSamples: any }) => {
  const [text, setText] = useState("");
  const [currentLocation, setCurrentLocation] = useState(null);
  const [error, setError] = useState<string | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  const { setSecondarySidebar } = useDocsStore();
  const location = useLocation();

  const fetchMarkdown = async () => {
    try {
      setIsLoading(true);
      setError(null);

      let path = location.pathname
        .replace("/guides", "")
        .replace(".md", "")
        .toLowerCase();
      if (path.trim() === "") path = "installation";

      // Use relative path from public directory
      const response = await fetch(`/bgw/guides/topics/${path}.md`);

      if (!response.ok) {
        throw new Error(`Failed to load guide: ${response.statusText}`);
      }

      const md = await response.text();
      setText(md);
      setCurrentLocation(location.pathname);
      setTimeout(() => {
        scrollToHash(location.hash);
      }, 100);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Failed to load guide");
    } finally {
      setIsLoading(false);
    }
  };

  function scrollToHash(hash) {
    if (hash) {
      document
        .querySelectorAll(hash)[0]
        ?.scrollIntoView({ block: "start", inline: "nearest" });
    } else {
      document
        .querySelectorAll(
          "#comp-scroll-area div[data-radix-scroll-area-viewport]"
        )[0]
        ?.scrollTo(0, 0);
      document
        .querySelectorAll(
          "#sidebar-scroll-area div[data-radix-scroll-area-viewport]"
        )[0]
        ?.scrollTo(0, 0);
    }
  }

  function scrollToTop() {
    document
      .querySelectorAll(
        "#comp-scroll-area div[data-radix-scroll-area-viewport]"
      )[0]
      ?.scrollTo(0, 0);
    document
      .querySelectorAll(
        "#sidebar-scroll-area div[data-radix-scroll-area-viewport]"
      )[0]
      ?.scrollTo(0, 0);
  }

  useEffect(() => {
    scrollToTop();
    if (location.pathname === currentLocation) return;

    fetchMarkdown();
  }, [location.pathname]);

  useEffect(() => {
    scrollToHash(location.hash);
  }, [location.hash]);

  // useEffect(() => {
  //   const extractHeadings = (markdown: string) => {
  //     const headingRegex = /^(#{1,6})\s+(.+)$/gm;
  //     const headings = [];
  //     let match;

  //     while ((match = headingRegex.exec(markdown)) !== null) {
  //       const level = match[1].length;
  //       const title = match[2];
  //       const id = title.toLowerCase().replace(/[^\w]+/g, "-");

  //       headings.push({
  //         title,
  //         url: `#${id}`,
  //         isActive: false,
  //       });
  //     }

  //     const sidebarItems = [
  //       {
  //         title: "Page Content",
  //         url: location.pathname,
  //         items: headings,
  //       },
  //     ];

  //     setSecondarySidebar(sidebarItems);
  //   };

  //   if (text) {
  //     extractHeadings(text);
  //   }
  // }, [text, location.pathname]);

  const renderBlockquote = (props: any) => {
    const { children, className, node, ...rest } = props;
    const text = React.Children.toArray(children)
      .map((child) => {
        if (typeof child === "string") return child;
        if (React.isValidElement(child)) return child.props.children;
        return "";
      })
      .join("");
    const match = /{\s?style=\W+(\w+)\W+\s?}/.exec(String(text));

    const cleanedChildren = React.Children.map(children, (child) => {
      if (typeof child === "string") {
        return child.replace(/{\s?style=\W+(\w+)\W+\s?}/g, "");
      }
      if (React.isValidElement(child)) {
        return React.cloneElement(child, {
          children: React.Children.map(child.props.children, (grandChild) => {
            if (typeof grandChild === "string") {
              return grandChild.replace(/{\s?style=\W+(\w+)\W+\s?}/g, "");
            }
            return grandChild;
          }),
        });
      }
      return child;
    });

    if (match && match.length == 2) {
      return <Banner subText={cleanedChildren} variant={match[1]} />;
    } else {
      return <Banner subText={cleanedChildren} />;
    }
  };

  const renderCode = (props: any) => {
    const { children, className, node, ...rest } = props;
    const match = /language-(\w+)/.exec(className || "");
    if (match && match.length == 2 && match[1] == "kotlin") {
      return (
        <CodeTab
          code={String(children).replace(/\n$/, "")}
          autoIndent={true}
          copy={true}
        />
      );
    } else {
      return (
        <code {...rest} className={className}>
          {children}
        </code>
      );
    }
  };

  const renderCodeBlock = (props: any) => {
    const { node, inline, className, children, ...rest } = props;
    const match = node.properties?.lang || "";
    const text = React.Children.toArray(children)
      .map((child) => {
        if (typeof child === "string") return child;
        if (React.isValidElement(child)) return child.props.children;
        return "";
      })
      .join("");
    const copy =
      node.properties?.copy !== undefined
        ? node.properties?.copy === "true"
        : false;
    return match ? (
      <CodeTab code={text} autoIndent={true} copy={copy} />
    ) : (
      <code className={className} {...rest}>
        {children}
      </code>
    );
  };

  const renderCodeBlockTab = (props: any) => {
    const { node, inline, className, children, ...rest } = props;
    const match = node.properties?.lang || "";
    const text = React.Children.toArray(children)
      .map((child) => {
        if (typeof child === "string") return child;
        if (React.isValidElement(child)) return child.props.children;
        return "";
      })
      .join("");
    return match ? (
      <CodeTab code={text} autoIndent={true} copy={true} />
    ) : (
      <code className={className} {...rest}>
        {children}
      </code>
    );
  };

  function convertDashesToCaps(str) {
    if (str.startsWith("http")) {
      return str;
    }

    if (str.endsWith(".html")) {
      str = str.replace(".html", "");
    }

    if (str === "index") {
      str = "_index";
    }

    // Replace patterns like "-x" with uppercase "X"
    let converted = str.replace(/-[a-z]/g, (match) =>
      match.charAt(1).toUpperCase()
    );

    // Remove any remaining dashes
    converted = converted.replace(/-/g, "");

    // if (converted === "Companion") {
    //   converted = "_companion";
    // }

    return converted;
  }

  const renderLink = (props: any) => {
    const { children, className, node, ...rest } = props;

    if (props.href.startsWith("http"))
      return (
        <Link to={props.href} target="_blank">
          {children}
        </Link>
      );

    let href = props.href;
    if (href.startsWith("/docs")) {
      let withoutIndex = href.replace("/index.html", "");
      let withoutHTML = convertDashesToCaps(withoutIndex);

      return (
        <Link className="link__ref" to={withoutHTML}>
          {children}
        </Link>
      );
    }

    if (href.startsWith("/guides")) {
      return (
        <Link className="link__guide" to={href}>
          {children}
        </Link>
      );
    }

    return <Link to={props.href}>{children}</Link>;
  };

  const generateId: () => string = () => {
    return Math.random().toString(36).substr(2, 9);
  };

  const renderTab = (props: any, actual: any) => {
    const { children, ...rest } = props;
    const ownValue = props["group-key"];
    const filteredChildren = React.Children.map(children, (child) => {
      if (child.props && child.props["lang"]) return child;
    });
    return (
      <TabsContent value={ownValue} key={ownValue} forceMount id={generateId()}>
        {renderCodeBlockTab(filteredChildren[0].props)}
      </TabsContent>
    );
  };

  const renderChapter = (props: any) => {
    const { children, title, collapsible } = props;

    let icon = "book_5";
    if (props.icon) {
      icon = props.icon;
    }
    return (
      <Collapsible defaultOpen={true} className="mt-8 mb-4 space-y-2">
        <CollapsibleTrigger className="flex items-center gap-3 text-lg font-bold">
          <i className="text-xl material-symbols-rounded">{icon}</i>
          {title}
        </CollapsibleTrigger>
        <CollapsibleContent>{children}</CollapsibleContent>
      </Collapsible>
    );
  };

  const renderTable = (props: any) => {
    const { children } = props;
    let orientation = props.orientation || "vertical";
    return (
      <Table
        className="mt-3 overflow-hidden rounded-xl bg-muted/50"
        orientation={orientation}
      >
        {children}
      </Table>
    );
  };

  const renderTableBody = (props: any) => {
    const { children } = props;
    return <TableBody>{children}</TableBody>;
  };

  const renderTableRow = (props: any) => {
    const { children } = props;
    return <TableRow>{children}</TableRow>;
  };

  const renderTableCell = (props: any) => {
    const { children, width } = props;
    const style = width ? { width } : {};
    return <TableCell style={style}>{children}</TableCell>;
  };

  const renderImage = (props: any) => {
    if (!props.src) return null;
    if (props.src.startsWith("http")) {
      return <img src={props.src} className="w-full h-auto rounded-xl" />;
    }

    if (location.pathname.split("/").length === 3) {
      return (
        <img
          src={`/bgw/guides/images/assets/${props.src}`}
          className="w-full h-auto rounded-xl"
        />
      );
    } else {
      return (
        <img
          src={`${location.pathname.replace(
            "/guides/",
            "/bgw/guides/images/"
          )}/${props.src}`}
          className="w-full h-auto rounded-xl"
        />
      );
    }
  };

  function buildBreadcrumbs(path: string) {
    let paths = path.split("/");
    let currentPath = "";

    let breadcrumbs = paths.map((b: string, index: number) => {
      currentPath += "/" + b;
      return (
        <>
          <BreadcrumbItem>
            <BreadcrumbLink
              to={`${currentPath}`}
              className="pointer-events-none"
            >
              {b === "guides"
                ? "Guide"
                : b
                    .replace(/-/g, " ")
                    .replace(".md", "")
                    .split(" ")
                    .map((s) => s.charAt(0).toUpperCase() + s.slice(1))
                    .join(" ")}
            </BreadcrumbLink>
          </BreadcrumbItem>
          {index < paths.length - 1 && <BreadcrumbSeparator />}
        </>
      );
    });

    return (
      <Breadcrumb className="mb-10">
        <BreadcrumbList>{breadcrumbs}</BreadcrumbList>
      </Breadcrumb>
    );
  }

  const renderMarkdown = useMemo(() => {
    return (
      <Markdown
        className="markdown"
        children={text}
        remarkPlugins={[remarkGfm]}
        rehypePlugins={[rehypeRaw]}
        components={{
          i: (props) => {
            let color = props.color || "#ffffff";
            return (
              <i className={`material-symbols-rounded text-[${color}]`}>
                {props.children}
              </i>
            );
          },
          signature: (props) => {
            const { children, node, ...rest } = props;
            return (
              <div className="p-3 fieldset rounded-xl" {...rest}>
                {children}
              </div>
            );
          },
          pre: (props) => {
            const { node, inline, className, children, ...rest } = props;
            return <CodeTab code={children.props.children} copy={true} />;
          },
          code: renderCode,
          "code-block": renderCodeBlock,
          tabs: (props) => {
            const { children, ...rest } = props;
            const values = React.Children.map(children, (child) => {
              if (child.props && child.props["group-key"])
                return child.props["group-key"];
            });
            let filteredChildren = children.filter(
              (child) => child.props && child.props["group-key"]
            );
            let setValues = new Set(values);
            return (
              <CodeTabs values={Array.from(setValues) as string[]}>
                {filteredChildren.map((child) => {
                  return renderTab(child.props, child);
                })}
              </CodeTabs>
            );
          },
          a: renderLink,
          blockquote: renderBlockquote,
          chapter: renderChapter,
          table: renderTable,
          tbody: renderTableBody,
          thead: (props) => (
            <TableHead className="w-full px-0 bg-secondary text-muted-foreground">
              {props.children}
            </TableHead>
          ),
          tr: renderTableRow,
          td: renderTableCell,
          th: renderTableCell,
          h1: (props) => {
            const id = props.children
              ?.toString()
              .toLowerCase()
              .replace(/[^\w]+/g, "-");
            return (
              <h1
                id={id}
                className="relative cursor-pointer group"
                onClick={() => {
                  navigator.clipboard.writeText(
                    `${window.location.origin}${window.location.pathname}#${id}`
                  );
                }}
              >
                {props.children}
                <span className="absolute invisible group-hover:visible -left-8 max-2xl:!hidden">
                  #
                </span>
              </h1>
            );
          },
          h2: (props) => {
            const id = props.children
              ?.toString()
              .toLowerCase()
              .replace(/[^\w]+/g, "-");
            return (
              <h2 id={id} className="relative cursor-pointer group">
                <Link to={`${location.pathname}#${id}`} className="link__none">
                  {props.children}
                  <span className="absolute invisible group-hover:visible -left-8 max-2xl:!hidden">
                    #
                  </span>
                </Link>
              </h2>
            );
          },
          h3: (props) => {
            const id = props.children
              ?.toString()
              .toLowerCase()
              .replace(/[^\w]+/g, "-");
            return (
              <h3
                id={id}
                className="relative cursor-pointer group"
                onClick={() => {
                  navigator.clipboard.writeText(
                    `${window.location.origin}${window.location.pathname}#${id}`
                  );
                }}
              >
                {props.children}
                <span className="absolute invisible group-hover:visible -left-8 max-2xl:!hidden">
                  #
                </span>
              </h3>
            );
          },
          h4: (props) => {
            const id = props.children
              ?.toString()
              .toLowerCase()
              .replace(/[^\w]+/g, "-");
            return (
              <h4
                id={id}
                className="relative cursor-pointer group"
                onClick={() => {
                  navigator.clipboard.writeText(
                    `${window.location.origin}${window.location.pathname}#${id}`
                  );
                }}
              >
                {props.children}
                <span className="absolute invisible group-hover:visible -left-8 max-2xl:!hidden">
                  #
                </span>
              </h4>
            );
          },
          h5: (props) => {
            const id = props.children
              ?.toString()
              .toLowerCase()
              .replace(/[^\w]+/g, "-");
            return (
              <h5 id={id} className="relative group">
                {props.children}
              </h5>
            );
          },
          h6: (props) => {
            const id = props.children
              ?.toString()
              .toLowerCase()
              .replace(/[^\w]+/g, "-");
            return (
              <h6 id={id} className="relative group">
                {props.children}
              </h6>
            );
          },
          img: renderImage,
          tldr: (props) => (
            <h6 className="-mt-5 text-base italic">{props.children}</h6>
          ),
          hr: (props) => <Separator className="my-10 ruler" />,
          preview: (props) => {
            return (
              <PreviewTab
                data={{
                  sample: props.children,
                  codepoint: [props.node.properties.key],
                  doc: null,
                }}
                info={allSamples}
                reformat={false}
                sideBySide={true}
                width={445}
              />
            );
          },
        }}
      ></Markdown>
    );
  }, [text, location.pathname]);

  if (isLoading) return <div></div>;
  if (error) return <div>Error: {error}</div>;

  return (
    <div className="flex justify-center w-full">
      <div className="px-10 2xl:max-w-[1200px] pt-5 mb-10 xl:w-full max-xl:px-7">
        {buildBreadcrumbs(location.pathname.replace("/", ""))}
        {renderMarkdown}
      </div>
    </div>
  );
});

export default Guide;
