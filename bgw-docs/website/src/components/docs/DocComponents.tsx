import React, { useState } from "react";
import { Badge } from "@/components/ui/badge";
import { Link } from "react-router-dom";
import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import CodeTab from "@/pages/docs/CodeTab";
import { createKotlinCodeLinebreaks } from "@/lib/utils";
import { parseMarkdown, parseMarkdownLinks } from "@/lib/docUtils";

// Section header with icon
export const SectionHeader = ({ icon, title, onClick }) => (
  <CollapsibleTrigger
    className="relative flex items-center w-full gap-4 mb-5"
    onClick={onClick}
  >
    <i className="material-symbols-rounded text-3xl text-primary-foreground mt-0.5 max-xl:text-2xl">
      {icon}
    </i>
    <h2 className="text-3xl font-bold max-xl:text-2xl">{title}</h2>
  </CollapsibleTrigger>
);

// Since version badge
export const SinceBadge = ({ since, className = "" }) => (
  <Link
    to={`https://github.com/tudo-aqua/bgw/releases/tag/v${since}`}
    target="_blank"
    className={className}
  >
    <Badge variant="mutedLink">Since {since}</Badge>
  </Link>
);

// Collapsible section component with standardized styling
export const CollapsibleSection = ({
  title,
  icon,
  isOpen = true,
  children,
}) => {
  const [open, setOpen] = useState(isOpen);

  return (
    <div className="mb-16 max-xl:mb-8">
      <Collapsible open={open} className="relative">
        <SectionHeader
          icon={icon}
          title={title}
          open={true}
          // onClick={() => setOpen(!open)}
        />
        <CollapsibleContent className="flex flex-col gap-4">
          {children}
        </CollapsibleContent>
      </Collapsible>
    </div>
  );
};

// Code signature display component
export const CodeDisplay = ({
  code,
  autoIndent = false,
  lineLength = 100,
  defaultHighlighter = false,
}) => {
  if (defaultHighlighter)
    return (
      <CodeTab
        code={createKotlinCodeLinebreaks(code, lineLength)}
        autoIndent={autoIndent}
      />
    );

  return (
    <div className="relative w-full max-w-full px-6 py-5 font-mono text-sm code-tab bg-muted/50 rounded-xl">
      {kotlinTypeHighlighter(createKotlinCodeLinebreaks(code, lineLength))}
    </div>
  );
};

export const kotlinTypeHighlighter = (signature: string) => {
  if (!signature) return null;

  // Define styling classes for different syntax elements
  const styles = {
    keyword: "text-bgw-pink", // Keywords like fun, var
    defaultType: "text-bgw-yellow", // Types like String, Boolean
    type: "text-bgw-blue", // BGW types
    functionName: "text-white", // Function/property names
    paramName: "text-bgw-blue", // Parameter names
    symbol: "text-bgw-gray", // Symbols like : ( ) ,
    operator: "text-bgw-white", // Operators like -> ? =
    generic: "text-bgw-white", // Generic type params
    genericType: "text-bgw-blue", // Generic type references
    space: "", // Spaces
    defaultValue: "text-bgw-red", // Default values
    stringProperty: "text-bgw-green", // String literals
    numberProperty: "text-bgw-orange", // Number literals
    booleanProperty: "text-bgw-orange", // Boolean literals (true, false, null)
    property: "text-bgw-blue", // Property references
    number: "text-bgw-orange", // Number literals
    constant: "text-bgw-yellow", // Constants in capital letters
  };

  // Known Kotlin keywords
  const keywords = [
    "val",
    "var",
    "fun",
    "open",
    "override",
    "abstract",
    "final",
    "internal",
    "private",
    "protected",
    "public",
    "constructor",
    "companion",
    "object",
    "class",
    "interface",
    "enum",
    "suspend",
    "vararg",
    "in",
    "operator",
    "set",
    "get",
  ];

  // Known Kotlin types
  const types = [
    // Basic types
    "Int",
    "Long",
    "Short",
    "Byte",
    "Float",
    "Double",
    "Boolean",
    "Char",
    "String",
    "Number",
    "Any",
    "Unit",
    "Nothing",
    // Unsigned types
    "UInt",
    "ULong",
    "UShort",
    "UByte",
    // Array types
    "Array",
    "ByteArray",
    "ShortArray",
    "IntArray",
    "LongArray",
    "FloatArray",
    "DoubleArray",
    "BooleanArray",
    "CharArray",
    // Collection types
    "List",
    "Set",
    "Map",
    "Collection",
    "MutableList",
    "MutableSet",
    "MutableMap",
    "ArrayList",
    "HashMap",
    "HashSet",
    "LinkedHashMap",
    "LinkedHashSet",
    // Other common types
    "Pair",
    "Triple",
    "Any",
    "Unit",
    "Nothing",
    "Void",
    "Sequence",
    "Iterable",
    // Function types
    "Function",
    "Function0",
    "Function1",
    "Function2",
    "Function3",
    "Function4",
    "Function5",
    // Common Kotlin stdlib
    "Throwable",
    "Exception",
    "Error",
    "Comparable",
    "Lazy",
    "Result",
  ];

  // Tokenize the signature
  const tokens: { text: string; type: string }[] = [];
  let currentToken = "";
  let inDefaultValue = false;

  // Helper to process and add the current token
  const addToken = () => {
    if (currentToken) {
      let type = "default";

      if (keywords.includes(currentToken)) {
        type = "keyword";
      }
      // Identify types (starting with capital letters)
      else if (/^[A-Z][a-zA-Z0-9_]*$/.test(currentToken)) {
        if (types.includes(currentToken)) {
          type = "defaultType";
        } else if (
          /^[A-Z][A-Z0-9_]*$/.test(currentToken) &&
          currentToken === currentToken.toUpperCase()
        ) {
          if (currentToken.length > 1) type = "constant";
          else type = "genericType";
        } else {
          type = "type";
        }
      }
      // Function or property names
      else if (
        tokens.length > 0 &&
        (tokens[tokens.length - 1].text === "fun" ||
          tokens[tokens.length - 1].text === "val" ||
          tokens[tokens.length - 1].text === "var")
      ) {
        type = "functionName";
      }
      // Default value properties
      else if (inDefaultValue) {
        if (/^[0-9]+$/.test(currentToken)) {
          type = "numberProperty";
        } else if (/^".*"$/.test(currentToken) || /^'.*'$/.test(currentToken)) {
          type = "stringProperty";
        } else if (["true", "false", "null"].includes(currentToken)) {
          type = "booleanProperty";
        } else {
          type = "property";
        }
      }

      tokens.push({ text: currentToken, type });
      currentToken = "";
    }
  };

  // First pass: tokenize the signature
  for (let i = 0; i < signature.length; i++) {
    const char = signature[i];

    if (/[\s:(),<>?=.]/.test(char)) {
      addToken();

      // Handle arrow operator
      if (
        char === "-" &&
        i + 1 < signature.length &&
        signature[i + 1] === ">"
      ) {
        tokens.push({ text: "->", type: "operator" });
        i++; // Skip the next character
      }
      // Handle other special characters
      else if (char === ":") {
        tokens.push({ text: char, type: "symbol" });
      } else if (char === "?") {
        tokens.push({ text: char, type: "operator" });
      } else if (char === "=") {
        tokens.push({ text: char, type: "operator" });
        inDefaultValue = true;
      } else if (char === ".") {
        tokens.push({ text: char, type: "symbol" });
      } else if (char === "<" || char === ">") {
        tokens.push({ text: char, type: "generic" });
      } else if (char === "(" || char === ")" || char === ",") {
        tokens.push({ text: char, type: "symbol" });
        // Reset default value flag on closing parenthesis or comma
        if (char === ")" || char === ",") {
          inDefaultValue = false;
        }
      } else if (char === " ") {
        tokens.push({ text: char, type: "space" });
      }
    } else {
      currentToken += char;
    }
  }

  // Add any final token
  addToken();

  // Second pass: identify parameter names
  if (
    tokens.filter((t) => t.type === "keyword" && t.text === "fun").length > 0
  ) {
    for (let i = 0; i < tokens.length; i++) {
      // Parameter names are typically followed by a colon (possibly with spaces in between)
      if (tokens[i].type === "default") {
        // Look ahead for a colon, skipping any space tokens
        let j = i + 1;
        while (j < tokens.length && tokens[j].type === "space") {
          j++;
        }

        // If we found a colon, this is a parameter name
        if (j < tokens.length && tokens[j].text === ":") {
          tokens[i].type = "paramName";
        }
      }
    }
  }

  // Convert to JSX elements
  // TODO - Add links to types
  const elements = tokens.map((token, index) => {
    // if (token.type === "type")
    //   return (
    //     <Link key={index} to={`/docs/`} className={styles[token.type] || ""}>
    //       <span key={index} className={styles[token.type] || ""}>
    //         {token.text}
    //       </span>
    //     </Link>
    //   );
    return (
      <span key={index} className={styles[token.type] || ""}>
        {token.text}
      </span>
    );
  });

  return <>{elements}</>;
};

// Parameter table for functions and constructors
export const ParametersTable = ({ parameters, func, inheritedFunc }) => {
  // Handle function parameters with inherited documentation
  if (func && inheritedFunc && inheritedFunc.details) {
    inheritedFunc.details.forEach((d) => {
      if (d.info.signature === func.signature && d.parameters) {
        d.parameters.forEach((p) => {
          const param = parameters.find((param) => param.name === p.name);
          if (param) param.doc = p.doc;
        });
      }
    });
  }

  if (!parameters || parameters.length === 0) return null;

  return (
    <Table className="mt-3 overflow-hidden rounded-md bg-muted/50 rounded-xl">
      <TableHeader>
        <TableRow>
          <TableHead className="w-[200px] pl-6">Parameter</TableHead>
          <TableHead className="w-[200px]">Type</TableHead>
          <TableHead className="w-[200px]">Default</TableHead>
          <TableHead>Description</TableHead>
        </TableRow>
      </TableHeader>
      <TableBody>
        {parameters.map((p) => (
          <TableRow key={p.name}>
            <TableCell>
              <code className="relative pl-2 font-mono rounded">{p.name}</code>
            </TableCell>
            <TableCell>
              <code className="relative font-mono rounded">
                {parseMarkdown(p.type, false)}
              </code>
            </TableCell>
            <TableCell>
              {p.defaultValue ? (
                <code className="relative font-mono rounded">
                  {parseMarkdown(p.defaultValue, false)}
                </code>
              ) : (
                "-"
              )}
            </TableCell>
            <TableCell>{p.doc ? parseMarkdown(p.doc) : "-"}</TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  );
};

// Class parameters table with since badges and property links
export const ClassParametersTable = ({
  parameters,
  properties,
  currentComponent,
  dirs,
}) => {
  return (
    <div className="flex flex-col w-full gap-4 overflow-hidden bg-muted/50 rounded-xl">
      <Table>
        <TableBody>
          {parameters.map((p) => {
            let addSince = null;
            let foundProperty = properties?.find(
              (prop) => prop.name === p.name
            );
            let link = foundProperty ? foundProperty.link : null;
            let inheritedDetails = null;

            if (link) {
              let inheritedComp = link.split("/").reduce((o, i) => o[i], dirs);
              if (inheritedComp) {
                inheritedComp.details.forEach((d) => {
                  if (d.info.signature === foundProperty.signature) {
                    inheritedDetails = d;
                  }
                });
              }

              if (inheritedDetails && inheritedDetails.since) {
                addSince = inheritedDetails.since;
              }
            }

            return (
              <TableRow key={p.name} className="relative">
                <TableCell className="w-[250px]">
                  <code className="relative pl-3 font-mono rounded">
                    {p.name}
                  </code>
                </TableCell>
                <TableCell className="text-sm">
                  {p.doc ? parseMarkdown(p.doc) : "-"}
                </TableCell>
                <TableCell className="pr-5">
                  {currentComponent &&
                  currentComponent[p.name] &&
                  currentComponent[p.name].details &&
                  currentComponent[p.name].details[0] &&
                  currentComponent[p.name].details[0].since ? (
                    <SinceBadge
                      since={currentComponent[p.name].details[0].since}
                      className="float-right"
                    />
                  ) : addSince ? (
                    <SinceBadge since={addSince} className="float-right" />
                  ) : null}
                </TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    </div>
  );
};

// Throws table for exceptions
export const ThrowsTable = ({ throws, exceptionIcons }) => (
  <div className="flex flex-col w-full gap-4 overflow-hidden rounded-xl throws__table">
    <Table>
      <TableBody>
        {throws.map((p) => (
          <TableRow
            key={p.name}
            className="flex items-center border-b-red-500/10 bg-red-500/10 hover:bg-red-500/15 max-xl:flex-col max-xl:py-2"
          >
            <TableCell className="min-w-[350px] text-red-500 items-center inline-flex gap-1 pl-6 py-2 max-xl:pl-3">
              <i className="text-lg text-red-500 material-symbols-rounded max-xl:hidden">
                {exceptionIcons[p.name] || "warning"}
              </i>
              <code className="relative pl-3 font-mono rounded max-xl:pl-0 max-xl:text-xs">
                {p.name}
              </code>
            </TableCell>
            <TableCell className="w-full py-2 text-sm text-red-500">
              {p.doc ? parseMarkdown(p.doc) : "-"}
            </TableCell>
          </TableRow>
        ))}
      </TableBody>
    </Table>
  </div>
);

// Breadcrumb navigation component
export const ComponentBreadcrumbs = ({ path }) => {
  const paths = path.split("/");
  let currentPath = "";

  return (
    <Breadcrumb className="mb-10">
      <BreadcrumbList>
        {paths.map((b, index) => {
          currentPath += "/" + b;
          return (
            <React.Fragment key={`breadcrumb-${index}`}>
              <BreadcrumbItem>
                <BreadcrumbLink to={`${currentPath}`}>
                  {b === "docs" ? "Reference" : b}
                </BreadcrumbLink>
              </BreadcrumbItem>
              {index < paths.length - 1 && <BreadcrumbSeparator />}
            </React.Fragment>
          );
        })}
      </BreadcrumbList>
    </Breadcrumb>
  );
};

// Component for displaying banner messages
export const ComponentBanner = ({
  type,
  title,
  description,
  icon,
  link,
}: {
  type: string;
  title: string;
  description: any;
  icon: string;
  link?: string;
}) => {
  const bannerStyles = {
    deprecated: "text-red-500 bg-destructive/10",
    companion: "bg-bgw-blue/10 text-bgw-blue",
    context: "bg-bgw-green/10 text-bgw-green",
    type: "bg-bgw-blue/10 text-bgw-blue",
  };

  const BannerContent = () => (
    <div
      className={`relative flex items-center gap-8 px-4 py-2 rounded-xl docs-banner ${bannerStyles[type]}`}
    >
      <div className="flex items-center gap-3">
        <i
          className={`text-lg material-symbols-rounded ${
            type === "deprecated" ? "text-red-500" : ""
          }`}
        >
          {icon}
        </i>
        <h4 className="font-bold">{title}</h4>
      </div>
      <p>{description}</p>
      {link && icon !== "reply" && (
        <i
          className={`absolute text-xl material-symbols-rounded ${
            bannerStyles[type].split(" ")[1]
          } right-4`}
        >
          chevron_right
        </i>
      )}
    </div>
  );

  return link ? (
    <Link to={link}>
      <BannerContent />
    </Link>
  ) : (
    <BannerContent />
  );
};

// Component for displaying inheritance information
export const InheritanceInfo = ({ items, label = "Inherited from:" }) => {
  if (!items || items.length === 0) return null;

  return (
    <div className="relative flex items-center h-6 gap-2 mt-3 ml-2">
      <h4 className="absolute left-0 text-xs font-bold">{label}:</h4>
      <ul className="absolute flex items-center gap-2 left-28">
        {items.map((item, index) => (
          <li key={`inheritance-item-${index}`}>{item}</li>
        ))}
      </ul>
    </div>
  );
};

// Component for displaying tag badges
export const ComponentTags = ({ tags }) => {
  if (!tags || tags.length === 0) return null;

  return (
    <div className="flex items-center gap-2 mt-1">
      {tags.map((tag, index) => (
        <React.Fragment key={`tag-${index}`}>{tag}</React.Fragment>
      ))}
    </div>
  );
};

// Component for displaying enum values
export const EnumValueCard = ({ name, doc, since, breadcrumbs, isActive }) => {
  return (
    <div
      className={`bg-background border-none p-3 rounded-xl h-full relative max-xl:px-7 max-xl:w-screen max-xl:-ml-7 max-xl:rounded-none max-xl:bg-transparent ${
        isActive ? "active__elem" : " "
      }`}
      id={`enum_${name}`}
    >
      {since && (
        <SinceBadge since={since} className="absolute z-10 top-8 right-10" />
      )}
      <Link to={"/docs/" + breadcrumbs.join("/")}>
        <div className="flex p-5 enum__values bg-muted/50 rounded-xl">
          <span className="pl-2 enum__value">{name}</span>
        </div>
      </Link>
      {doc && (
        <p className="px-2 mt-3 text-muted-foreground max-2xl:text-justify">
          {parseMarkdownLinks(doc)}
        </p>
      )}
    </div>
  );
};
