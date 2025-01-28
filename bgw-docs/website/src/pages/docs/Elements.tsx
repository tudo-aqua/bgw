import { Badge } from "@/components/ui/badge";
import { Banner } from "@/components/ui/banner";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
} from "@/components/ui/breadcrumb";
import { Separator } from "@/components/ui/separator";
import { layoutMap } from "@/lib/utils";
import { Link } from "react-router-dom";

function Elements({ comp, pkg }: { comp: any; pkg: string }) {
  function parseMarkdownNoLinks(text: string): JSX.Element {
    if (!text) return <span></span>;

    const linkRegex = /\[([^\]]*)\]\(([^)]*)\)/g;
    let resultText = "";
    let lastIndex = 0;
    let match;

    while ((match = linkRegex.exec(text)) !== null) {
      // Add text before the link
      resultText += text.slice(lastIndex, match.index);

      // Add the link text
      const linkText = match[1] || match[2];
      resultText += linkText;

      lastIndex = linkRegex.lastIndex;
    }

    // Add remaining text
    if (lastIndex < text.length) {
      resultText += text.slice(lastIndex);
    }

    return <span>{resultText}</span>;
  }

  function getDetails(elem: any, key = "") {
    let isClass =
      elem._index &&
      (elem._index.details.info.tags.type === "class" ||
        elem._index.details.info.tags.type === "enum" ||
        elem._index.details.info.tags.type === "object");

    let hasModifier =
      elem._index &&
      (elem._index.details.info.tags.modifiers.includes("sealed") ||
        elem._index.details.info.tags.modifiers.includes("abstract"));

    let actualType = hasModifier
      ? "interface"
      : elem._index
      ? elem._index.details.info.tags.type
      : "extra";

    if (actualType === "extra") {
      let more = elem.details[0];
      if (more.info.signature.includes("fun ")) {
        actualType = "function";
      } else if (more.info.signature.includes("const ")) {
        actualType = "constant";
      } else if (
        more.info.signature.includes("val ") ||
        more.info.signature.includes("var ")
      ) {
        actualType = "Property";
      } else if (more.info.name.toUpperCase() === more.info.name) {
        actualType = "Value";
      }
    }

    return {
      title: key,
      url: "/docs/" + pkg + "/" + key,
      isInstantiable: isClass && !hasModifier,
      type: actualType,
      deprecated:
        elem._index && elem._index.details && elem._index.details.deprecated,
    };
  }

  const groupAndSortElements = (elements) => {
    // Group by type
    const grouped = elements.reduce((acc, [key, elem]) => {
      const type = elem._index?.details?.type || "";
      if (!acc[type]) acc[type] = [];
      acc[type].push([key, elem]);
      return acc;
    }, {});

    // Sort groups to put interfaces last
    const sortedTypes = Object.keys(grouped).sort((a, b) => {
      if (a.toLowerCase() === "interface") return 1;
      if (b.toLowerCase() === "interface") return -1;
      return a.localeCompare(b);
    });

    return sortedTypes.map((type) => ({
      type,
      elements: grouped[type],
    }));
  };

  let allPkg = Object.keys(layoutMap).find(
    (key) => layoutMap[key].package === pkg
  );
  let icon = layoutMap[allPkg].icon || "note_stack";

  const groupedElements = Object.keys(comp).reduce((groups, key) => {
    if (key === "_index") return groups;
    const elem = comp[key];
    const details = getDetails(elem, key);

    if (!groups[details.type]) {
      groups[details.type] = [];
    }
    groups[details.type].push({ key, elem, details });
    return groups;
  }, {});

  // Sort types to ensure interfaces are last
  const sortedTypes = Object.keys(groupedElements).sort((a, b) => {
    if (a.toLowerCase() === "interface") return 1;
    if (b.toLowerCase() === "interface") return -1;
    return a.localeCompare(b);
  });

  return (
    <div className="flex flex-col gap-10">
      {sortedTypes.map((type) => (
        <div key={type} className="flex flex-col gap-5">
          <div className="flex flex-col gap-5">
            {groupedElements[type].map(({ key, elem, details }) => (
              <Link key={key} to={details.url} className="group">
                <div className="bg-muted/50 border-none px-4 py-4 rounded-xl flex gap-5 items-start relative docs-banner w-full">
                  <div className="flex items-center gap-5">
                    <Badge
                      variant={details.type}
                      className="material-symbols-rounded rounded-lg aspect-square w-14 h-14 p-4 flex items-center justify-center text-2xl"
                    >
                      {icon}
                    </Badge>
                  </div>
                  <div className="w-full min-h-[56px] flex items-center justify-between">
                    <div className="w-[calc(100%-120px)]">
                      <h4
                        className={`font-bold text-lg ${
                          details.deprecated ? "line-through" : ""
                        } ${
                          !details.isInstantiable &&
                          details.type.toLowerCase() !== "constant" &&
                          details.type.toLowerCase() !== "function"
                            ? "text-muted-foreground italic"
                            : ""
                        } group-hover:translate-x-1  transition-all`}
                      >
                        {key}
                      </h4>
                      <h1 className="text-base text-muted-foreground group-hover:translate-x-1 transition-all duration-300">
                        {elem._index &&
                        elem._index.details &&
                        elem._index.details.info &&
                        elem._index.details.info.doc
                          ? parseMarkdownNoLinks(
                              elem._index.details.info.doc
                                .split(/\n.+:/)[0]
                                .replace(/\s+/gm, " ")
                            )
                          : ""}
                      </h1>
                    </div>
                    <Badge variant={details.type} className="mr-2 w-fit h-fit">
                      {details.type.capitalize()}
                    </Badge>
                  </div>
                </div>
              </Link>
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}

export default Elements;
