import { SearchIcon } from "lucide-react";
import * as React from "react";

import { Dialog, DialogClose, DialogContent } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { searchDirs, searchGuides } from "@/main.tsx";
import { ScrollArea } from "@/components/ui/scroll-area";
import { Separator } from "@/components/ui/separator";
import { layoutMap } from "@/lib/utils";
import { Button } from "./ui/button";
import { Link } from "react-router-dom";
import { set } from "date-fns";

export default function SearchField() {
  const [open, setOpen] = React.useState(false);
  const [query, setQuery] = React.useState("");
  const [guideResults, setGuideResults] = React.useState([]);
  const [referenceResults, setReferenceResults] = React.useState([]);

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    const searchQuery = event.target.value;
    setQuery(searchQuery);

    if (searchQuery.trim().length === 0) {
      setGuideResults([]);
      setReferenceResults([]);
    } else {
      const foundGuides = searchGuides(searchQuery);
      const foundReferences = searchDirs(searchQuery);
      setGuideResults(foundGuides);
      setReferenceResults(foundReferences);
    }
  };

  // React.useEffect(() => {
  //   const down = (e: KeyboardEvent) => {
  //     if (e.key === "k" && (e.metaKey || e.ctrlKey)) {
  //       e.preventDefault();
  //       setOpen((open) => !open);
  //     }
  //   };

  //   document.addEventListener("keydown", down);
  //   return () => document.removeEventListener("keydown", down);
  // }, []);

  function parseMarkdownNoLinks(text: string): JSX.Element[] {
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

  // Helper function to get icon based on type
  const getTypeIcon = (type: string) => {
    switch (type) {
      case "class":
        return "class";
      case "member":
        return "code";
      case "property":
        return "variables";
      case "constructor":
        return "construction";
      case "parameter":
        return "format_list_bulleted";
      case "constructor-param":
        return "format_list_bulleted";
      case "detail":
        return "info";
      default:
        return "code";
    }
  };

  const getPackageIcon = (packageName: string) => {
    let icon = "code";
    Object.entries(layoutMap).forEach(([key, value]) => {
      if (value.package === packageName) {
        icon = value.icon;
      }
    });
    return icon;
  };

  const getPackageTitle = (packageName: string) => {
    let title = packageName.split(".").pop().capitalize();
    Object.entries(layoutMap).forEach(([key, value]) => {
      if (value.package === packageName) {
        title = value.title;
      }
    });
    return title;
  };

  // Helper function to format reference result title
  const formatReferenceTitle = (result, query) => {
    const data = result[1];
    const type = data.type;

    if (type === "class") {
      return data.highlight;
    } else if (type === "constructor") {
      return `${data.className}`;
    } else if (type === "property") {
      return data.highlight;
    } else if (type === "parameter" || type === "constructor-param") {
      return `${data.paramName}`;
    } else if (type === "member") {
      return data.highlight;
    }

    return data.className;
  };

  // Helper function to format reference result description
  const formatReferenceDescription = (result, query) => {
    const data = result[1];
    const type = data.type;

    if (type === "class") {
      return parseMarkdownNoLinks(data.doc);
    } else if (type === "constructor") {
      return parseMarkdownNoLinks(data.doc);
    } else if (type === "property") {
      return parseMarkdownNoLinks(data.doc);
    } else if (type === "parameter" || type === "constructor-param") {
      return data.signature;
    } else if (type === "member") {
      return parseMarkdownNoLinks(data.doc);
    }

    return data.path;
  };

  return (
    <>
      <Button
        variant="secondary"
        onClick={() => setOpen(true)}
        className="absolute right-3 h-9 max-lg:hidden"
      >
        <span className="flex items-center w-32 grow">
          <i className="mr-4 text-base material-symbols-rounded text-muted-foreground/70">
            search
          </i>
          <span className="font-semibold text-muted-foreground/70">Search</span>
        </span>
      </Button>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="gap-0 p-0 outline-none w-[50vw] max-w-full search-dialog h-fit">
          <div className="flex gap-3 p-4">
            <Input
              placeholder="Search BGW Documentation..."
              onChange={handleSearch}
              className="font-semibold border-0 focus-visible:ring-0 text-muted-foreground placeholder:text-muted-foreground/50"
              autoFocus
            />
            <Button
              variant="secondary"
              className="px-3 aspect-square"
              onClick={handleSearch}
            >
              <i className="text-lg material-symbols-rounded text-muted-foreground/70">
                search
              </i>
            </Button>
            <DialogClose>
              <Button
                variant="secondary"
                className="px-3 aspect-square bg-red-500/10 hover:bg-red-500/15"
              >
                <i className="text-lg text-red-500 material-symbols-rounded">
                  close
                </i>
              </Button>
            </DialogClose>
          </div>
          <ScrollArea className="h-[50vh] border-t">
            {guideResults.length === 0 && referenceResults.length === 0 ? (
              <p className="p-4 pt-8 text-sm font-semibold text-center text-muted-foreground/70">
                No results found.
              </p>
            ) : (
              <div className="p-4">
                {/* Guide Results */}
                {guideResults.length > 0 && (
                  <>
                    <div className="mb-2 ml-4 text-sm font-medium text-muted-foreground">
                      Guides
                    </div>
                    {guideResults.map(([path, content, guideInfo]) => (
                      <Link
                        to={`${path}`}
                        onClick={() => {
                          setOpen(false);
                          setQuery("");
                          setGuideResults([]);
                          setReferenceResults([]);
                        }}
                      >
                        <div
                          key={`guide-${path}`}
                          className="px-4 py-2 text-sm rounded-sm cursor-pointer hover:bg-accent hover:text-accent-foreground"
                        >
                          <div className="flex items-center gap-3 mb-1 font-medium">
                            <span className="text-base material-symbols-rounded">
                              {guideInfo?.icon}
                            </span>
                            <span className="text-muted-foreground">
                              {guideInfo?.section}
                            </span>
                            <span className="mx-1 text-muted-foreground/50">
                              /
                            </span>
                            <span>{guideInfo?.title}</span>
                          </div>
                          <div
                            className="text-xs text-muted-foreground line-clamp-2"
                            dangerouslySetInnerHTML={{ __html: content }}
                          />
                        </div>
                      </Link>
                    ))}
                  </>
                )}

                {/* Separator between guides and references if both exist */}
                {guideResults.length > 0 && referenceResults.length > 0 && (
                  <Separator className="my-4" />
                )}

                {/* Reference Results */}
                {referenceResults.length > 0 && (
                  <>
                    <div className="mb-2 ml-4 text-sm font-medium text-muted-foreground">
                      API Reference
                    </div>
                    {referenceResults.map((result, index) => (
                      <Link
                        to={`/docs/${result[0]}/${result[1].path
                          .replace(result[0], "")
                          .replace(/\./gm, "/")}`}
                        onClick={() => {
                          setOpen(false);
                          setQuery("");
                          setGuideResults([]);
                          setReferenceResults([]);
                        }}
                      >
                        <div
                          key={`ref-${index}-${result[1].path}`}
                          className="px-4 py-2 text-sm rounded-sm cursor-pointer hover:bg-accent hover:text-accent-foreground"
                        >
                          <div className="flex items-center gap-3 font-medium">
                            <span className="text-base material-symbols-rounded">
                              {getPackageIcon(result[0])}
                            </span>
                            <span className="text-muted-foreground">
                              {getPackageTitle(result[0])}
                            </span>
                            <span className="mx-1 text-muted-foreground">
                              /
                            </span>
                            {result[1].type !== "constructor" &&
                            result[1].type !== "class" ? (
                              <>
                                <span className="text-muted-foreground">
                                  {result[1].className}
                                </span>
                                <span className="mx-1 text-muted-foreground">
                                  /
                                </span>
                              </>
                            ) : null}
                            <span
                              className="ml-1"
                              dangerouslySetInnerHTML={{
                                __html: formatReferenceTitle(result, query),
                              }}
                            ></span>
                          </div>
                          <div className="text-xs text-muted-foreground line-clamp-2">
                            {formatReferenceDescription(result, query)}
                          </div>
                        </div>
                      </Link>
                    ))}
                  </>
                )}
              </div>
            )}
          </ScrollArea>
        </DialogContent>
      </Dialog>
    </>
  );
}
