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
export const CodeDisplay = ({ code, autoIndent = false, lineLength = 100 }) => (
  <CodeTab
    code={createKotlinCodeLinebreaks(code, lineLength)}
    autoIndent={autoIndent}
  />
);

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
