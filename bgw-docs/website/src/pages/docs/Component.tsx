import {
  Breadcrumb,
  BreadcrumbItem,
  BreadcrumbLink,
  BreadcrumbList,
  BreadcrumbSeparator,
} from "@/components/ui/breadcrumb";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import React, { useEffect, useState } from "react";
import LoadingBar from "react-top-loading-bar";

import { Badge } from "@/components/ui/badge";
import { Separator } from "@/components/ui/separator";
import { Link } from "react-router-dom";
import CodeTab from "./CodeTab";
import { Collapsible } from "@radix-ui/react-collapsible";
import {
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import { set } from "date-fns";
import { useDocsStore } from "@/stores/docsStore";
import { createKotlinCodeLinebreaks, isListener } from "@/lib/utils";
import PreviewTab from "./PreviewTab";
import Elements from "./Elements";
import { Banner } from "@/components/ui/banner";
import {
  ClassParametersTable,
  CodeDisplay,
  CollapsibleSection,
  ComponentBanner,
  ComponentBreadcrumbs,
  EnumValueCard,
  ParametersTable,
  SinceBadge,
  ThrowsTable,
} from "@/components/docs/DocComponents";
import { dirs } from "@/main";
import { parseMarkdownLinks, parseMarkdownNoLinks } from "@/lib/docUtils";

const exceptionIcons = {
  IllegalArgumentException: "data_alert",
  IllegalStateException: "nearby_error",
  UnsupportedOperationException: "gpp_maybe",
  NullPointerException: "partner_reports",
  IndexOutOfBoundsException: "partner_reports",
  NumberFormatException: "warning",
  ConcurrentModificationException: "warning",
  NoSuchElementException: "warning",
  ClassCastException: "warning",
  AssertionError: "warning",
  Error: "warning",
  Exception: "warning",
  Throwable: "warning",
  IllegalInheritanceException: "deployed_code_alert",
};

function Component({
  location,
  allSamples,
}: {
  location: any;
  allSamples: any;
}) {
  const {
    currentComponent,
    setSecondarySidebar,
    setCodeBlocksToLoad,
    resetCodeBlocks,
  } = useDocsStore();

  useEffect(() => {
    if (location.hash) {
      document
        .querySelectorAll(location.hash)[0]
        ?.scrollIntoView({ block: "center", inline: "nearest" });
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
  }, [location, currentComponent]);

  React.useLayoutEffect(() => {
    if (currentComponent?._index) {
      const sidebarItems = buildSecondaryNav(currentComponent);
      setSecondarySidebar(sidebarItems);
    }
  }, [currentComponent, setSecondarySidebar]);

  const [openConstructors, setOpenConstructors] = React.useState(true);
  const [openProperties, setOpenProperties] = React.useState(true);
  const [openFunctions, setOpenFunctions] = React.useState(true);

  function buildSecondaryNav(comp: any): any[] {
    if (comp) {
      if (comp._index) {
        let main = comp._index;
        let base = [];
        resetCodeBlocks();
        let allBlockCount = 0;

        let constructors = {
          title: "Constructors",
          url: "#",
          items: [],
        };

        let info = getConstructorsInfo(main);
        info.forEach((c: any, index: number) => {
          constructors.items.push({
            title: c.info.name,
            url: "#constructor_" + index,
            items: [],
            type: "class",
          });
        });

        if (info.length > 0) {
          base.push(constructors);
          allBlockCount += constructors.items.length;
        }

        let enumValues = {
          title: "Values",
          url: "#",
          items: [],
        };

        let enumInfo = getEnumValuesInfo(main);
        enumInfo.forEach((c: any, index: number) => {
          enumValues.items.push({
            title: c.details.info.name,
            url: "#enum_" + c.details.info.name,
            items: [],
            type: "class",
          });
        });

        if (enumInfo.length > 0) {
          base.push(enumValues);
          allBlockCount += enumValues.items.length;
        }

        if (
          main.members &&
          main.members.properties &&
          main.members.properties.filter((c: any) => {
            return !(c.name.startsWith("on") && c.signature.indexOf("->") > 0);
          }).length > 0
        ) {
          let properties = {
            title: "Properties",
            url: "#",
            items: [],
          };

          main.members.properties
            .filter((c: any) => {
              return !(
                c.name.startsWith("on") && c.signature.indexOf("->") > 0
              );
            })
            .forEach((c: any) => {
              properties.items.push({
                title: c.name,
                url: `#prop_${c.name}`,
                type: "property",
              });
            });

          base.push(properties);
          allBlockCount += properties.items.length;
        }

        if (
          main.members &&
          main.members.properties &&
          main.members.properties.filter((c: any) => {
            return c.name.startsWith("on") && c.signature.indexOf("->") > 0;
          }).length > 0
        ) {
          let listeners = {
            title: "Listeners",
            url: "#",
            items: [],
          };

          main.members.properties
            .filter((c: any) => {
              return c.name.startsWith("on") && c.signature.indexOf("->") > 0;
            })
            .forEach((c: any) => {
              listeners.items.push({
                title: c.name,
                url: `#prop_${c.name}`,
                type: "listener",
              });
            });

          base.push(listeners);
          allBlockCount += listeners.items.length;
        }

        if (
          main.members &&
          main.members.functions &&
          main.members.functions.length > 0
        ) {
          let functions = {
            title: "Functions",
            url: "#",
            items: [],
          };

          main.members.functions.forEach((c: any) => {
            functions.items.push({
              title: c.name,
              url: `#func_${c.name}`,
              type: "function",
            });
          });

          base.push(functions);
          allBlockCount += functions.items.length;
        }

        // setCodeBlocksToLoad(allBlockCount);
        return base;
      } else {
        return [];
      }
    } else {
      return [];
    }
  }

  function getExamples(comp: any) {
    let extraExamples = [];
    if (comp && comp.details && comp.details.samples) {
      extraExamples = [...comp.details.samples];
    }

    if (
      comp.breadcrumbs &&
      currentComponent[comp.breadcrumbs[comp.breadcrumbs.length - 1]] &&
      currentComponent[comp.breadcrumbs[comp.breadcrumbs.length - 1]].details
    ) {
      currentComponent[
        comp.breadcrumbs[comp.breadcrumbs.length - 1]
      ].details.forEach((d: any) => {
        if (d.samples) {
          extraExamples.push(...d.samples);
        }
      });
    }

    let samples = extraExamples.map((s: any) => {
      if (allSamples && allSamples[s.codepoint[0]]) {
        return (
          <div className="w-full border-none rounded-xl">
            <PreviewTab data={s} info={allSamples}></PreviewTab>
          </div>
        );
      }
    });

    if (samples.length > 0) {
      return (
        <>
          <div>
            <Collapsible open={openConstructors}>
              <CollapsibleTrigger className="relative flex items-center w-full gap-4 mb-7">
                <i className="material-symbols-rounded text-3xl text-primary-foreground mt-0.5 max-xl:text-2xl">
                  data_object
                </i>
                <h2 className="text-3xl font-bold max-xl:text-2xl">Examples</h2>
                {/* <i className="absolute text-2xl material-symbols-rounded text-primary-foreground right-1">
                {openConstructors ? "expand_less" : "expand_more"}
              </i> */}
              </CollapsibleTrigger>
              <CollapsibleContent className="grid grid-cols-2 gap-5 max-xl:grid-cols-1">
                {samples}
              </CollapsibleContent>
            </Collapsible>
          </div>
          <Separator className="my-8 mt-10" />
        </>
      );
    }
    return null;
  }

  function getConstructorsInfo(comp: any) {
    let extraConstructors = [];
    if (
      comp.breadcrumbs &&
      currentComponent[comp.breadcrumbs[comp.breadcrumbs.length - 1]]
    ) {
      extraConstructors =
        currentComponent[comp.breadcrumbs[comp.breadcrumbs.length - 1]].details;
    }

    if (
      comp &&
      comp.members &&
      comp.members.constructors &&
      (comp.members.constructors.length > 0 || extraConstructors.length > 0)
    ) {
      return extraConstructors;
    }

    return [];
  }

  function getConstructors(comp: any) {
    let extraConstructors = [];
    if (
      comp.breadcrumbs &&
      currentComponent[comp.breadcrumbs[comp.breadcrumbs.length - 1]]
    ) {
      extraConstructors =
        currentComponent[comp.breadcrumbs[comp.breadcrumbs.length - 1]].details;
    }

    extraConstructors.forEach((c: any) => {
      if (!c.since && comp.details && comp.details.since) {
        c.since = comp.details.since;
      }
    });

    extraConstructors = extraConstructors.moveLastToFirst();
    if (extraConstructors.length > 0) {
      extraConstructors[0].primary = true;
    }

    extraConstructors.sort((a: any, b: any) => {
      if (a.since && b.since) {
        return a.since >= b.since ? 1 : -1;
      }
      return 0;
    });

    // TODO: Find primary constructor based on signature

    if (
      comp &&
      comp.members &&
      comp.members.constructors &&
      (comp.members.constructors.length > 0 || extraConstructors.length > 0)
    ) {
      return (
        <CollapsibleSection title="Constructors" icon={"handyman"}>
          {extraConstructors.map((c: any, index: number) => {
            return (
              <div
                className="relative w-full p-3 border-none bg-background rounded-xl max-xl:px-7 max-xl:w-screen max-xl:-ml-7 max-xl:rounded-none max-xl:bg-transparent"
                id={`constructor_${index}`}
              >
                <div className="absolute z-10 flex justify-center gap-2 top-8 right-10">
                  {/* {c.primary && <Badge variant="class">Primary</Badge>} */}
                  {c.since && buildSince(c.since)}
                </div>
                <CodeDisplay code={c.info.signature} lineLength={80} />
                {c.parameters && (
                  <div className="mt-3">
                    {getClassParameters(c.parameters, comp.members.properties)}
                  </div>
                )}
                {c.info.doc && (
                  <p className="px-2 py-1 mt-3 text-muted-foreground max-2xl:text-justify">
                    {parseMarkdownLinks(c.info.doc)}
                  </p>
                )}
                {c.throws && (
                  <>
                    <h4 className="px-2 mt-5 mb-3 text-xs font-bold">
                      Throws:
                    </h4>
                    {getClassThrows(c.throws)}
                  </>
                )}
              </div>
            );
          })}
        </CollapsibleSection>
      );
    }
    return null;
  }

  function getFunctions(comp: any) {
    if (
      comp &&
      comp.members &&
      comp.members.functions &&
      comp.members.functions.length > 0
    ) {
      return (
        <CollapsibleSection title="Functions" icon={"function"}>
          {comp.members.functions.map((c: any) => {
            let isActive =
              window.location.hash.replace("#", "") === `func_${c.name}`;

            let matchingFunction = currentComponent[c.name];
            let allThrows = [];

            if (matchingFunction && matchingFunction.details) {
              matchingFunction.details.forEach((d: any) => {
                if (d.throws && d.info.signature === c.signature) {
                  allThrows.push(...d.throws);
                }
              });
            }

            let inherited = [];
            let breadcrumbs = c.link.split("/");
            let last = comp.breadcrumbs.fromRight(0);
            let propLast = breadcrumbs.fromRight(1);
            let inheritedFunc = null;

            let inheritedDetails = null;

            if (
              last !== propLast &&
              !propLast.startsWith("tools.aqua") &&
              !["ordinal", "name", "values", "entries", "valueOf"].includes(
                c.name
              )
            ) {
              if (!c.link.startsWith("https")) {
                let inheritedComp = c.link
                  .split("/")
                  .reduce((o, i) => o[i], dirs);
                if (inheritedComp) {
                  inheritedComp.details.forEach((d: any) => {
                    if (d.info.signature === c.signature) {
                      inheritedDetails = d;
                    }
                  });
                }

                inherited.push(
                  <li>
                    <Link
                      to={
                        "/docs/" +
                        breadcrumbs.excludeLast().join("/") +
                        "#func_" +
                        c.name
                      }
                      key={last}
                    >
                      <Badge variant="muted">{propLast}</Badge>
                    </Link>
                  </li>
                );

                inheritedFunc = breadcrumbs.reduce((o, i) => o[i], dirs);
                if (inheritedFunc) {
                  inheritedFunc.details.forEach((d: any) => {
                    if (d.throws && d.info.signature === c.signature) {
                      allThrows.push(...d.throws);
                    }
                  });
                }
              }
            }

            if (
              !inheritedFunc &&
              matchingFunction &&
              matchingFunction.details
            ) {
              inheritedFunc = matchingFunction;
            }

            // TODO - Show also for inherited
            let moreDetails = currentComponent[c.name];
            if (
              moreDetails &&
              moreDetails.details &&
              moreDetails.details.length > 0
            ) {
              moreDetails = moreDetails.details.find((d: any) => {
                return d.info.signature === c.signature;
              });
            }

            if (inheritedDetails && moreDetails === undefined) {
              moreDetails = inheritedDetails;
            }

            return (
              <div
                className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 max-xl:px-7 max-xl:w-screen max-xl:-ml-7 max-xl:rounded-none max-xl:bg-transparent ${
                  isActive ? "active__elem" : " "
                }`}
                id={`func_${c.name}`}
              >
                {moreDetails && moreDetails.deprecated && (
                  <Banner
                    variant="deprecated"
                    className="h-12 mt-0 mb-3 text-sm max-2xl:h-fit"
                    icon={"running_with_errors"}
                    subText={`This API-Endpoint will be removed in future versions.  ${moreDetails.deprecated.description}`}
                  />
                )}
                {moreDetails &&
                  moreDetails.since &&
                  buildSince(moreDetails.since, "absolute top-8 right-10 z-10")}
                {!["ordinal", "name", "values", "entries", "valueOf"].includes(
                  c.name
                ) ? (
                  <Link
                    to={"/docs/" + c.link}
                    key={c.name}
                    className="relative group"
                  >
                    <CodeDisplay code={c.signature} />
                    <i
                      className={`material-symbols-rounded text-xl text-muted-foreground/80 absolute top-[16px] group-hover:opacity-100 opacity-0 transition-opacity ${
                        moreDetails && moreDetails.since
                          ? "right-28"
                          : "right-5"
                      }`}
                    >
                      open_in_new
                    </i>
                  </Link>
                ) : (
                  <CodeDisplay code={c.signature} />
                )}
                {c.parameters && (
                  <div className="">
                    {getParameters(c.parameters, c, inheritedFunc)}
                  </div>
                )}
                {c.doc && (
                  <p className="px-2 py-1 mt-3 text-muted-foreground max-2xl:text-justify">
                    {parseMarkdownLinks(c.doc)}
                  </p>
                )}
                {allThrows.length > 0 && (
                  <>
                    <h4 className="px-2 mt-3 mb-3 text-xs font-bold">
                      Throws:
                    </h4>
                    {getClassThrows(allThrows)}
                  </>
                )}
                {inherited.length > 0 && (
                  <div className="relative flex items-center h-6 gap-2 mt-3 ml-2">
                    <h4 className="absolute left-0 text-xs font-bold">
                      Inherited from:
                    </h4>
                    <ul className="absolute flex items-center gap-2 left-28">
                      {inherited}
                    </ul>
                  </div>
                )}
              </div>
            );
          })}
        </CollapsibleSection>
      );
    }
    return null;
  }

  function getProperties(comp: any) {
    if (
      comp &&
      comp.members &&
      comp.members.properties &&
      comp.members.properties.filter((c: any) => {
        return !(c.name.startsWith("on") && c.signature.indexOf("->") > 0);
      }).length > 0
    ) {
      return (
        <CollapsibleSection title="Properties" icon={"action_key"}>
          {comp.members.properties
            .filter((c: any) => {
              return !isListener(c);
            })
            .map((c: any) => {
              let isActive =
                window.location.hash.replace("#", "") === `prop_${c.name}`;

              let inherited = [];
              let breadcrumbs = c.link.split("/");
              let last = comp.breadcrumbs.fromRight(0);
              let propLast = breadcrumbs.fromRight(1);

              let inheritedDetails = null;

              if (
                last !== propLast &&
                !propLast.startsWith("tools.aqua") &&
                !["ordinal", "name", "values", "entries", "valueOf"].includes(
                  c.name
                )
              ) {
                let inheritedComp = c.link
                  .split("/")
                  .reduce((o, i) => o[i], dirs);
                if (inheritedComp) {
                  inheritedComp.details.forEach((d: any) => {
                    if (d.info.signature === c.signature) {
                      inheritedDetails = d;
                    }
                  });
                }

                inherited.push(
                  <li>
                    <Link
                      to={"/docs/" + breadcrumbs.excludeLast().join("/")}
                      key={last}
                    >
                      <Badge variant="muted">{propLast}</Badge>
                    </Link>
                  </li>
                );
              }

              // TODO - Show also for inherited
              let moreDetails = currentComponent[c.name];
              if (
                moreDetails &&
                moreDetails.details &&
                moreDetails.details[0]
              ) {
                moreDetails = moreDetails.details[0];
              }

              if (inheritedDetails && moreDetails === undefined) {
                moreDetails = inheritedDetails;
              }

              // if (inherited.length > 0) {
              //   return null;
              // }

              return (
                // <div className={`bg-background rounded-xl p-3 ${isActive ? "border-l-4 border-purple-500 border-solid" : "border-none "}`} id={`prop_${c.name}`}>
                // <div className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 ${isActive ? "w-[calc(100%-20px)] ml-[20px]" : " "}`} id={`prop_${c.name}`}>
                //   <div className="h-5 absolute w-1 rounded-xl left-[-20px] bg-purple-500" style={{display: isActive ? "block" : "none"}}></div>
                <div
                  className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 max-xl:px-7 max-xl:w-screen max-xl:-ml-7 max-xl:rounded-none max-xl:bg-transparent ${
                    isActive ? "active__elem" : " "
                  }`}
                  id={`prop_${c.name}`}
                >
                  {moreDetails && moreDetails.deprecated && (
                    <Banner
                      variant="deprecated"
                      className="h-12 mt-0 mb-3 text-sm max-2xl:h-fit"
                      icon={"running_with_errors"}
                      subText={`This API-Endpoint will be removed in future versions.  ${moreDetails.deprecated.description}`}
                    />
                  )}
                  {moreDetails &&
                    moreDetails.since &&
                    buildSince(
                      moreDetails.since,
                      "absolute top-8 right-10 z-10"
                    )}
                  {![
                    "ordinal",
                    "name",
                    "values",
                    "entries",
                    "valueOf",
                  ].includes(c.name) ? (
                    <Link
                      to={"/docs/" + c.link}
                      key={c.name}
                      className="relative group"
                    >
                      <CodeDisplay code={c.signature} />
                      <i
                        className={`material-symbols-rounded text-xl text-muted-foreground/80 absolute top-[16px] group-hover:opacity-100 opacity-0 transition-opacity ${
                          moreDetails && moreDetails.since
                            ? "right-28"
                            : "right-5"
                        }`}
                      >
                        open_in_new
                      </i>
                    </Link>
                  ) : (
                    <CodeDisplay code={c.signature} />
                  )}
                  {c.doc && (
                    <p className="px-2 py-1 mt-3 text-muted-foreground max-2xl:text-justify">
                      {parseMarkdownLinks(c.doc)}
                    </p>
                  )}
                  {inherited.length > 0 && (
                    <div className="relative flex items-center h-6 gap-2 mt-3 ml-2">
                      <h4 className="absolute left-0 text-xs font-bold">
                        Inherited from:
                      </h4>
                      <ul className="absolute flex items-center gap-2 left-28">
                        {inherited}
                      </ul>
                    </div>
                  )}
                </div>
              );
            })}
        </CollapsibleSection>
      );
    }
    return null;
  }

  function getEvents(comp: any) {
    if (
      comp &&
      comp.members &&
      comp.members.properties &&
      comp.members.properties.filter((c: any) => {
        return c.name.startsWith("on") && c.signature.indexOf("->") > 0;
      }).length > 0
    ) {
      return (
        <CollapsibleSection title="Listeners" icon={"notifications_active"}>
          {comp.members.properties
            .filter((c: any) => {
              return isListener(c);
            })
            .map((c: any) => {
              let isActive =
                window.location.hash.replace("#", "") === `prop_${c.name}`;

              let inherited = [];
              let breadcrumbs = c.link.split("/");
              let last = comp.breadcrumbs.fromRight(0);
              let propLast = breadcrumbs.fromRight(1);

              let inheritedDetails = null;

              if (
                last !== propLast &&
                !propLast.startsWith("tools.aqua") &&
                !["ordinal", "name", "values", "entries", "valueOf"].includes(
                  c.name
                )
              ) {
                let inheritedComp = c.link
                  .split("/")
                  .reduce((o, i) => o[i], dirs);
                if (inheritedComp) {
                  inheritedComp.details.forEach((d: any) => {
                    if (d.info.signature === c.signature) {
                      inheritedDetails = d;
                    }
                  });
                }

                inherited.push(
                  <li>
                    <Link
                      to={"/docs/" + breadcrumbs.excludeLast().join("/")}
                      key={last}
                    >
                      <Badge variant="muted">{propLast}</Badge>
                    </Link>
                  </li>
                );
              }

              // TODO - Show also for inherited
              let moreDetails = currentComponent[c.name];
              if (
                moreDetails &&
                moreDetails.details &&
                moreDetails.details[0]
              ) {
                moreDetails = moreDetails.details[0];
              }

              if (inheritedDetails && moreDetails === undefined) {
                moreDetails = inheritedDetails;
              }

              // if (inherited.length > 0) {
              //   return null;
              // }

              return (
                // <div className={`bg-background rounded-xl p-3 ${isActive ? "border-l-4 border-purple-500 border-solid" : "border-none "}`} id={`prop_${c.name}`}>
                // <div className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 ${isActive ? "w-[calc(100%-20px)] ml-[20px]" : " "}`} id={`prop_${c.name}`}>
                //   <div className="h-5 absolute w-1 rounded-xl left-[-20px] bg-purple-500" style={{display: isActive ? "block" : "none"}}></div>
                <div
                  className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 max-xl:px-7 max-xl:w-screen max-xl:-ml-7 max-xl:rounded-none max-xl:bg-transparent ${
                    isActive ? "active__elem" : " "
                  }`}
                  id={`prop_${c.name}`}
                >
                  {moreDetails && moreDetails.deprecated && (
                    <Banner
                      variant="deprecated"
                      className="h-12 mt-0 mb-3 text-sm max-2xl:h-fit"
                      icon={"running_with_errors"}
                      subText={`This API-Endpoint will be removed in future versions.  ${moreDetails.deprecated.description}`}
                    />
                  )}
                  {moreDetails &&
                    moreDetails.since &&
                    buildSince(
                      moreDetails.since,
                      "absolute top-8 right-10 z-10"
                    )}
                  {![
                    "ordinal",
                    "name",
                    "values",
                    "entries",
                    "valueOf",
                  ].includes(c.name) ? (
                    <Link
                      to={"/docs/" + c.link}
                      key={c.name}
                      className="relative group"
                    >
                      <CodeDisplay code={c.signature} />
                      <i
                        className={`material-symbols-rounded text-xl text-muted-foreground/80 absolute top-[16px] group-hover:opacity-100 opacity-0 transition-opacity ${
                          moreDetails && moreDetails.since
                            ? "right-28"
                            : "right-5"
                        }`}
                      >
                        open_in_new
                      </i>
                    </Link>
                  ) : (
                    <CodeDisplay code={c.signature} />
                  )}
                  {c.doc && (
                    <p className="px-2 py-1 mt-3 text-muted-foreground max-2xl:text-justify">
                      {parseMarkdownLinks(c.doc)}
                    </p>
                  )}
                  {inherited.length > 0 && (
                    <div className="relative flex items-center h-6 gap-2 mt-3 ml-2">
                      <h4 className="absolute left-0 text-xs font-bold">
                        Inherited from:
                      </h4>
                      <ul className="absolute flex items-center gap-2 left-28">
                        {inherited}
                      </ul>
                    </div>
                  )}
                </div>
              );
            })}
        </CollapsibleSection>
      );
    }
    return null;
  }

  function getEnumValuesInfo(comp: any) {
    if (
      comp &&
      currentComponent &&
      (comp.details.info.tags.type === "enum" || currentComponent["Companion"])
    ) {
      let enumKeys = Object.keys(currentComponent).filter((key) => {
        return key.toUpperCase() === key && currentComponent[key]._index;
      });
      let enumValues = enumKeys.map((key) => {
        return currentComponent[key]._index;
      });

      if (currentComponent["Companion"] && enumValues.length === 0) {
        enumKeys = Object.keys(currentComponent["Companion"]).filter((key) => {
          return (
            key.toUpperCase() === key && currentComponent["Companion"][key]
          );
        });
        enumValues = enumKeys.map((key) => {
          return JSON.parse(JSON.stringify(currentComponent["Companion"][key]));
        });

        enumValues.forEach((v: any) => {
          if (v.details.length > 0) v.details = v.details[0];
        });
      }

      return enumValues;
    }

    return [];
  }

  function getEnumValues(comp: any) {
    let enumComp = comp;

    if (
      comp &&
      currentComponent &&
      (comp.details.info.tags.type === "enum" || currentComponent["Companion"])
    ) {
      let enumKeys = Object.keys(currentComponent).filter((key) => {
        return key.toUpperCase() === key && currentComponent[key]._index;
      });
      let enumValues = enumKeys.map((key) => {
        return currentComponent[key]._index;
      });

      if (currentComponent["Companion"] && enumValues.length === 0) {
        enumKeys = Object.keys(currentComponent["Companion"]).filter((key) => {
          return (
            key.toUpperCase() === key && currentComponent["Companion"][key]
          );
        });
        enumValues = enumKeys.map((key) => {
          return JSON.parse(JSON.stringify(currentComponent["Companion"][key]));
        });

        enumValues.forEach((v: any) => {
          if (v.details.length > 0) v.details = v.details[0];
        });
        enumComp = currentComponent["Companion"]._index;
      }

      if (enumValues.length <= 0) {
        return null;
      }

      return (
        <CollapsibleSection title="Values" icon={"clear_all"}>
          <ul className="grid grid-cols-3 gap-4 max-xl:grid-cols-2 max-md:grid-cols-1">
            {enumValues.map((c: any) => {
              let isActive =
                window.location.hash.replace("#", "") ===
                `enum_${c.details.info.name}`;
              return (
                <EnumValueCard
                  name={c.details.info.name}
                  doc={c.details.info.doc}
                  since={c.details && c.details.since}
                  breadcrumbs={c.breadcrumbs}
                  isActive={isActive}
                />

                // <div
                //   className={`bg-background border-none p-3 rounded-xl h-full relative max-xl:px-7 max-xl:w-screen max-xl:-ml-7 max-xl:rounded-none max-xl:bg-transparent ${
                //     isActive ? "active__elem" : " "
                //   }`}
                //   id={`enum_${c.details.info.name}`}
                // >
                //   {c.details &&
                //     c.details.since &&
                //     buildSince(
                //       moreDetails.since,
                //       "absolute top-8 right-10 z-10"
                //     )}
                //   <Link
                //     to={"/docs/" + c.breadcrumbs.join("/")}
                //     key={c.details.info.name}
                //   >
                //     <div className="flex p-5 enum__values bg-muted/50 rounded-xl">
                //       <span className="pl-2 enum__value">
                //         {c.details.info.name}
                //       </span>
                //     </div>
                //   </Link>
                //   {c.details.info.doc && (
                //     <p className="px-2 mt-3 text-muted-foreground max-2xl:text-justify">
                //       {parseMarkdownLinks(c.details.info.doc)}
                //     </p>
                //   )}
                // </div>
              );
            })}
          </ul>
        </CollapsibleSection>
      );
    }
    return null;
  }

  function getParameters(parameters: any[], func: any, inheritedFunc: any) {
    if (inheritedFunc && inheritedFunc.details) {
      inheritedFunc.details.forEach((d: any) => {
        if (d.info.signature === func.signature && d.parameters) {
          d.parameters.forEach((p: any) => {
            parameters.find((param) => {
              return param.name === p.name;
            }).doc = p.doc;
          });
        }
      });
    }
    if (!parameters || parameters.length === 0) return null;

    return (
      <ParametersTable
        parameters={parameters}
        inheritedFunc={inheritedFunc}
        func={func}
      />
    );
  }

  function parseClassDoc(text: string): JSX.Element[] {
    if (!text) return [];

    // Split text into paragraphs
    const paragraphs = text.split("\n\n").filter((p) => p.trim());
    let previousWasColon = false;

    return paragraphs.map((paragraph, paragraphIndex) => {
      const linkRegex = /\[([^\]]*)\]\(([^)]*)\)/g;
      let lastIndex = 0;
      const elements: JSX.Element[] = [];
      let match;

      while ((match = linkRegex.exec(paragraph)) !== null) {
        // Add text before the link
        if (match.index > lastIndex) {
          elements.push(
            <span key={`${paragraphIndex}-${lastIndex}`}>
              {paragraph.slice(lastIndex, match.index)}
            </span>
          );
        }

        // Add the link, use link text or target if text is empty
        const linkText = match[1] || match[2];
        let linkTarget = match[2] ? `/docs/${match[2]}` : `#${match[1]}`;

        if (paragraph.includes("https://") || paragraph.includes("http://")) {
          linkTarget = match[2];
        }
        if (linkTarget.indexOf("#") != -1) {
          elements.push(linkText);
        } else if (linkTarget.startsWith("https")) {
          elements.push(
            <code key={`${paragraphIndex}-${match.index}`}>
              <Link
                className="font-mono text-primary"
                to={linkTarget}
                target="_blank"
              >
                {linkText}
              </Link>
            </code>
          );
        } else {
          elements.push(
            <code key={`${paragraphIndex}-${match.index}`}>
              <Link className="font-mono text-primary" to={linkTarget}>
                {linkText}
              </Link>
            </code>
          );
        }

        lastIndex = linkRegex.lastIndex;
      }

      // Add remaining text
      if (lastIndex < paragraph.length) {
        elements.push(
          <span key={`${paragraphIndex}-${lastIndex}`}>
            {paragraph.slice(lastIndex)}
          </span>
        );
      }

      // Check if single element ending with colon
      const isSingleElementWithColon =
        elements.length === 1 && paragraph.trim().endsWith(":");

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

  const buildBreadcrumbs = (path: string) => (
    <ComponentBreadcrumbs path={path} />
  );

  function getClassParameters(parameters: any, properties: any) {
    return (
      <ClassParametersTable
        parameters={parameters}
        properties={properties}
        currentComponent={currentComponent}
        dirs={dirs}
      />
    );
  }

  function getClassThrows(throws: any) {
    return <ThrowsTable throws={throws} exceptionIcons={exceptionIcons} />;
  }

  function buildComponentBanners(c: any, full: any, current: any) {
    let bannerCount = 0;
    let banners = [];

    if (c.deprecated)
      banners.push(
        <ComponentBanner
          type={"deprecated"}
          description={`This API-Endpoint will be removed in future versions. ${c.deprecated.description}`}
          title={"Deprecated"}
          icon={"running_with_errors"}
        />
      );

    if (current && current["Companion"] && current["Companion"]._index)
      banners.push(
        <ComponentBanner
          type={"companion"}
          title={"Companion"}
          icon={"note_stack_add"}
          description={
            "This API-Endpoint may have additional properties in it's Companion object."
          }
          link={"/docs/" + current["Companion"]._index.breadcrumbs.join("/")}
        />
      );

    if (full && full.breadcrumbs.includes("Companion"))
      banners.push(
        <ComponentBanner
          type={"context"}
          title={"Context"}
          icon={"reply"}
          description={`This Companion object may contain additional static properties for ${full.breadcrumbs.fromRight(
            1
          )}.`}
          link={"/docs/" + full.breadcrumbs.excludeLast().join("/")}
        />
      );

    if (current && !full && current.breadcrumbs.includes("Companion"))
      banners.push(
        <ComponentBanner
          type={"context"}
          title={"Context"}
          icon={"reply"}
          description={`This property is part of the Companion object for ${current.breadcrumbs.fromRight(
            2
          )}.`}
          link={"/docs/" + current.breadcrumbs.excludeLast(2).join("/")}
        />
      );

    if (
      !full &&
      current.breadcrumbs &&
      current.breadcrumbs.length > 2 &&
      current.breadcrumbs.fromRight(0) === current.breadcrumbs.fromRight(1)
    )
      banners.push(
        <ComponentBanner
          type={"context"}
          title={"Context"}
          icon={"reply"}
          description={`This reference includes additional constructor details for ${current.breadcrumbs.fromRight(
            1
          )}.`}
          link={"/docs/" + current.breadcrumbs.excludeLast(1).join("/")}
        />
      );

    if (
      full &&
      full.members &&
      full.members.types &&
      full.members.types.length > 0
    ) {
      full.members.types.forEach((type) => {
        if (type.name === "Companion") return null;

        banners.push(
          <ComponentBanner
            type={"type"}
            title={c.info.name + "." + type.name}
            icon={"note_stack"}
            description={parseMarkdownNoLinks(type.doc)}
            link={"/docs/" + type.link}
          />
        );
      });
    }

    return banners;
  }

  function buildSince(since: string, className: string = "") {
    return <SinceBadge since={since} className={className} />;
  }

  function getCompleteComponent(c: any, full: any, current: any) {
    let tags = [];

    if (c.info.tags && c.info.tags.type) {
      if (c.info.tags.type === "class") {
        tags.push(
          <Link to={"/docs/" + full.breadcrumbs.join("/") + "/" + c.info.name}>
            <Badge variant={c.info.tags.type}>
              {c.info.tags.type.capitalize()}
            </Badge>
          </Link>
        );
      } else {
        tags.push(
          <Badge variant={c.info.tags.type}>
            {c.info.tags.type.capitalize()}
          </Badge>
        );
      }

      /* c.info.tags.modifiers.forEach((tag) => {
        tags.push(<Badge variant="muted">{tag.capitalize()}</Badge>);
      });
      if (c.info.tags.visibility) {
        tags.push(<Badge variant="muted">{c.info.tags.visibility.capitalize()}</Badge>);
      } */
    } else if (c.type !== "extra") {
      if (c.info.signature.includes("fun ")) {
        tags.push(<Badge variant="function">Function</Badge>);
      } else if (c.info.signature.includes("const ")) {
        tags.push(<Badge variant="constant">Constant</Badge>);
      } else if (
        c.info.signature.includes("val ") ||
        c.info.signature.includes("var ")
      ) {
        if (!isListener(c.info)) {
          tags.push(<Badge variant="property">Property</Badge>);
        } else {
          tags.push(<Badge variant="listener">Listener</Badge>);
        }
      } else if (c.info.name.toUpperCase() === c.info.name) {
        tags.push(<Badge variant="enum">Value</Badge>);
      }
    }

    if (c.since) {
      tags.push(buildSince(c.since));
    }

    let seeAlso = [];

    if (c.seeAlso) {
      c.seeAlso.forEach((see: any) => {
        seeAlso.push(
          <li>
            <Link to={see.link}>
              <Badge variant="muted">{see.name}</Badge>
            </Link>
          </li>
        );
      });
    }

    let inheritors = [];

    if (c.inheritors) {
      c.inheritors.forEach((see: any) => {
        inheritors.push(
          <li>
            <Link to={see.link}>
              <Badge variant="muted">{see.name}</Badge>
            </Link>
          </li>
        );
      });
    }

    let banners = buildComponentBanners(c, full, current);

    return (
      <div className="relative w-full max-xl:w-[calc(100vw-3.5rem)]">
        {banners.length > 0 && (
          <div className="flex flex-col gap-4 mb-8">{banners}</div>
        )}
        <div className="flex items-center gap-8 mb-5">
          <h2 className={`font-bold text-4xl max-xl:text-3xl`}>
            {c.info.name === "Companion"
              ? `${c.info.name} (${full.breadcrumbs.fromRight(1)})`
              : c.info.name}
          </h2>
          <div className="flex items-center gap-2 mt-1">{tags}</div>
        </div>
        {c.info.doc.trim() !== "" && (
          <p className="flex flex-col gap-1 mb-4 text-justify whitespace-pre-wrap text-muted-foreground">
            {parseClassDoc(c.info.doc)}
          </p>
        )}
        {inheritors.length > 0 && (
          <div className="relative flex items-center h-6 gap-2 mt-2">
            <h4 className="absolute left-0 text-xs font-bold">Inheritors:</h4>
            <ul className="absolute flex items-center gap-2 left-20">
              {inheritors}
            </ul>
          </div>
        )}
        {seeAlso.length > 0 && (
          <div className="relative flex items-center h-6 gap-2 mt-2">
            <h4 className="absolute left-0 text-xs font-bold">See also:</h4>
            <ul className="absolute flex items-center gap-2 left-20">
              {seeAlso}
            </ul>
          </div>
        )}
        <div className="mt-8"></div>
        <CodeDisplay code={c.info.signature} />
        {c.parameters && full === null && (
          <>
            <h4 className="mt-10 mb-4 text-xl font-bold indent-1">
              Parameters
            </h4>
            {getClassParameters(c.parameters, null)}
          </>
        )}
        {c.throws && (
          <>
            <h4 className="mt-10 mb-4 text-xl font-bold indent-1">Throws</h4>
            {getClassThrows(c.throws)}
          </>
        )}
        <Separator className="my-8 mt-10" />
        {full && getExamples(full)}
        {full && getConstructors(full)}
        {full && getEnumValues(full)}
        {full && getProperties(full)}
        {full && getEvents(full)}
        {full && getFunctions(full)}
      </div>
    );
  }

  function buildCurrentComponent(comp: any) {
    if (comp) {
      if (
        comp._index &&
        comp._index.breadcrumbs.length === 1 &&
        comp._index.breadcrumbs[0] === comp._index.package
      ) {
        return <Elements comp={comp} pkg={comp._index.package} />;
      }

      if (comp._index) {
        let main = comp._index;
        return getCompleteComponent(main.details, main, comp);
      } else {
        return (
          <div className="flex flex-col gap-4">
            {comp &&
              comp.details &&
              comp.details.map((c: any) => {
                return getCompleteComponent(c, null, comp);
              })}
          </div>
        );
      }
    } else {
      return <h1>Component not found</h1>;
    }
    return null;
  }

  return (
    <div className="flex justify-center w-full">
      <div className="px-10 max-w-[1600px] pt-5 mb-10 w-full component max-2xl:px-7 max-2xl:mb-0">
        {buildBreadcrumbs(location.pathname.replace("/", ""))}
        {buildCurrentComponent(currentComponent)}
        {/* {getTitle(currentComponent)} */}
        {/* <PreviewTab codeId={0} /> */}
        {/* <PreviewTab codeId={1} /> */}
        {/* {getConstructors(currentComponent)} */}
        {/* {getFunctions(currentComponent)} */}
      </div>
    </div>
  );
}

export default Component;
