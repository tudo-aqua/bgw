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
  dirs,
  allSamples,
}: {
  location: any;
  dirs: any;
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
      console.log(comp.details.samples);
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
          <div className="border-none rounded-xl w-full">
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
              <CollapsibleTrigger className="flex items-center relative gap-4 mb-7 w-full">
                <i className="material-symbols-rounded text-3xl text-primary-foreground mt-0.5">
                  data_object
                </i>
                <h2 className="font-bold text-3xl">Examples</h2>
                {/* <i className="material-symbols-rounded text-2xl text-primary-foreground right-1 absolute">
                {openConstructors ? "expand_less" : "expand_more"}
              </i> */}
              </CollapsibleTrigger>
              <CollapsibleContent className="grid grid-cols-2 gap-5">
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

    if (
      comp &&
      comp.members &&
      comp.members.constructors &&
      (comp.members.constructors.length > 0 || extraConstructors.length > 0)
    ) {
      return (
        <div className="mb-16">
          <Collapsible open={openConstructors}>
            <CollapsibleTrigger className="flex items-center relative gap-4 mb-5 w-full">
              <i className="material-symbols-rounded text-3xl text-primary-foreground mt-0.5">
                handyman
              </i>
              <h2 className="font-bold text-3xl">Constructors</h2>
              {/* <i className="material-symbols-rounded text-2xl text-primary-foreground right-1 absolute">
                {openConstructors ? "expand_less" : "expand_more"}
              </i> */}
            </CollapsibleTrigger>
            <CollapsibleContent className="flex flex-col gap-4">
              {/* {comp.members.constructors.map((c: any) => (
              <fieldset className="bg-background border-none rounded-xl p-3">
                <CodeTab onLoad={() => { console.log("loaded") }}
                  code={createKotlinCodeLinebreaks(c.signature)}
                  autoIndent={false}
                />
                {c.brief && (
                  <p className="text-muted-foreground">
                    {parseMarkdownLinks(c.doc)}
                  </p>
                )}
                {c.parameters && (
                  <fieldset className="">{getParameters(c.parameters)}</fieldset>
                )}
              </fieldset>
            ))} */}
              {extraConstructors.map((c: any, index: number) => (
                <fieldset
                  className="bg-background border-none rounded-xl p-3 relative"
                  id={`constructor_${index}`}
                >
                  <div className="absolute top-8 right-10 z-10 flex justify-center gap-2">
                    {c.primary && <Badge variant="class">Primary</Badge>}
                    {c.since && buildSince(c.since)}
                  </div>
                  <CodeTab
                    code={createKotlinCodeLinebreaks(c.info.signature, 80)}
                    autoIndent={false}
                  />
                  {c.parameters && (
                    <fieldset className="mt-3">
                      {getClassParameters(c.parameters)}
                    </fieldset>
                  )}
                  {c.info.doc && (
                    <p className="text-muted-foreground mt-3 py-1 px-2">
                      {parseMarkdownLinks(c.info.doc)}
                    </p>
                  )}
                  {c.throws && (
                    <>
                      <h4 className="font-bold text-xs mt-5 mb-3 px-2">
                        Throws:
                      </h4>
                      {getClassThrows(c.throws)}
                    </>
                  )}
                </fieldset>
              ))}
            </CollapsibleContent>
          </Collapsible>
        </div>
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
        <div className="mb-16">
          <Collapsible open={openFunctions}>
            <CollapsibleTrigger className="flex items-center gap-4 mb-5 relative w-full">
              <i className="material-symbols-rounded text-3xl text-primary-foreground mt-0.5">
                function
              </i>
              <h2 className="font-bold text-3xl">Functions</h2>
              {/* <i className="material-symbols-rounded text-2xl text-primary-foreground right-1 absolute">
                {openFunctions ? "expand_less" : "expand_more"}
              </i> */}
            </CollapsibleTrigger>
            <CollapsibleContent className="flex flex-col gap-4">
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

                if (
                  last !== propLast &&
                  !propLast.startsWith("tools.aqua") &&
                  !["ordinal", "name", "values", "entries", "valueOf"].includes(
                    c.name
                  )
                ) {
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

                return (
                  <fieldset
                    className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 ${
                      isActive ? "active__elem" : " "
                    }`}
                    id={`func_${c.name}`}
                  >
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
                        className="group relative"
                      >
                        <CodeTab
                          code={createKotlinCodeLinebreaks(c.signature)}
                          autoIndent={false}
                        />
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
                      <CodeTab
                        code={createKotlinCodeLinebreaks(c.signature)}
                        autoIndent={false}
                      />
                    )}
                    {c.parameters && (
                      <fieldset className="">
                        {getParameters(c.parameters, c, inheritedFunc)}
                      </fieldset>
                    )}
                    {c.doc && (
                      <p className="text-muted-foreground mt-3 py-1 px-2">
                        {parseMarkdownLinks(c.doc)}
                      </p>
                    )}
                    {allThrows.length > 0 && (
                      <>
                        <h4 className="font-bold text-xs mt-3 mb-3 px-2">
                          Throws:
                        </h4>
                        {getClassThrows(allThrows)}
                      </>
                    )}
                    {inherited.length > 0 && (
                      <div className="flex gap-2 items-center mt-3 relative h-6 ml-2">
                        <h4 className="font-bold text-xs absolute left-0">
                          Inherited from:
                        </h4>
                        <ul className="flex gap-2 items-center absolute left-28">
                          {inherited}
                        </ul>
                      </div>
                    )}
                  </fieldset>
                );
              })}
            </CollapsibleContent>
          </Collapsible>
        </div>
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
        <div className="mb-16">
          <Collapsible open={openProperties}>
            <CollapsibleTrigger className="flex items-center gap-4 mb-5 relative w-full">
              <i className="material-symbols-rounded text-3xl text-primary-foreground mt-0.5">
                action_key
              </i>
              <h2 className="font-bold text-3xl">Properties</h2>
              {/* <i className="material-symbols-rounded text-2xl text-primary-foreground right-1 absolute">
                {openProperties ? "expand_less" : "expand_more"}
              </i> */}
            </CollapsibleTrigger>
            <CollapsibleContent className="flex flex-col gap-4">
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

                  if (
                    last !== propLast &&
                    !propLast.startsWith("tools.aqua") &&
                    ![
                      "ordinal",
                      "name",
                      "values",
                      "entries",
                      "valueOf",
                    ].includes(c.name)
                  ) {
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

                  // if (inherited.length > 0) {
                  //   return null;
                  // }

                  return (
                    // <fieldset className={`bg-background rounded-xl p-3 ${isActive ? "border-l-4 border-purple-500 border-solid" : "border-none "}`} id={`prop_${c.name}`}>
                    // <fieldset className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 ${isActive ? "w-[calc(100%-20px)] ml-[20px]" : " "}`} id={`prop_${c.name}`}>
                    //   <div className="h-5 absolute w-1 rounded-xl left-[-20px] bg-purple-500" style={{display: isActive ? "block" : "none"}}></div>
                    <fieldset
                      className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 ${
                        isActive ? "active__elem" : " "
                      }`}
                      id={`prop_${c.name}`}
                    >
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
                          className="group relative"
                        >
                          <CodeTab
                            code={createKotlinCodeLinebreaks(c.signature)}
                            autoIndent={false}
                          />
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
                        <CodeTab
                          code={createKotlinCodeLinebreaks(c.signature)}
                          autoIndent={false}
                        />
                      )}
                      {c.doc && (
                        <p className="text-muted-foreground mt-3 py-1 px-2">
                          {parseMarkdownLinks(c.doc)}
                        </p>
                      )}
                      {inherited.length > 0 && (
                        <div className="flex gap-2 items-center mt-3 relative h-6 ml-2">
                          <h4 className="font-bold text-xs absolute left-0">
                            Inherited from:
                          </h4>
                          <ul className="flex gap-2 items-center absolute left-28">
                            {inherited}
                          </ul>
                        </div>
                      )}
                    </fieldset>
                  );
                })}
            </CollapsibleContent>
          </Collapsible>
        </div>
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
        <div className="mb-16">
          <Collapsible open={openProperties}>
            <CollapsibleTrigger className="flex items-center gap-4 mb-5 relative w-full">
              <i className="material-symbols-rounded text-3xl text-primary-foreground mt-0.5">
                notifications_active
              </i>
              <h2 className="font-bold text-3xl">Listeners</h2>
              {/* <i className="material-symbols-rounded text-2xl text-primary-foreground right-1 absolute">
                {openProperties ? "expand_less" : "expand_more"}
              </i> */}
            </CollapsibleTrigger>
            <CollapsibleContent className="flex flex-col gap-4">
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

                  if (
                    last !== propLast &&
                    !propLast.startsWith("tools.aqua") &&
                    ![
                      "ordinal",
                      "name",
                      "values",
                      "entries",
                      "valueOf",
                    ].includes(c.name)
                  ) {
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

                  // if (inherited.length > 0) {
                  //   return null;
                  // }

                  return (
                    // <fieldset className={`bg-background rounded-xl p-3 ${isActive ? "border-l-4 border-purple-500 border-solid" : "border-none "}`} id={`prop_${c.name}`}>
                    // <fieldset className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 ${isActive ? "w-[calc(100%-20px)] ml-[20px]" : " "}`} id={`prop_${c.name}`}>
                    //   <div className="h-5 absolute w-1 rounded-xl left-[-20px] bg-purple-500" style={{display: isActive ? "block" : "none"}}></div>
                    <fieldset
                      className={`flex flex-col justify-center bg-background rounded-xl border-none relative p-3 ${
                        isActive ? "active__elem" : " "
                      }`}
                      id={`prop_${c.name}`}
                    >
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
                          className="group relative"
                        >
                          <CodeTab
                            code={createKotlinCodeLinebreaks(c.signature)}
                            autoIndent={false}
                          />
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
                        <CodeTab
                          code={createKotlinCodeLinebreaks(c.signature)}
                          autoIndent={false}
                        />
                      )}
                      {c.doc && (
                        <p className="text-muted-foreground mt-3 py-1 px-2">
                          {parseMarkdownLinks(c.doc)}
                        </p>
                      )}
                      {inherited.length > 0 && (
                        <div className="flex gap-2 items-center mt-3 relative h-6 ml-2">
                          <h4 className="font-bold text-xs absolute left-0">
                            Inherited from:
                          </h4>
                          <ul className="flex gap-2 items-center absolute left-28">
                            {inherited}
                          </ul>
                        </div>
                      )}
                    </fieldset>
                  );
                })}
            </CollapsibleContent>
          </Collapsible>
        </div>
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
        <div className="mb-16">
          <div className="flex items-center gap-4 mb-5">
            <i className="material-symbols-rounded text-3xl text-primary-foreground mt-0.5">
              clear_all
            </i>
            <h2 className="font-bold text-3xl">Values</h2>
          </div>
          <ul className="grid grid-cols-3 gap-4">
            {enumValues.map((c: any) => {
              let isActive =
                window.location.hash.replace("#", "") ===
                `enum_${c.details.info.name}`;
              return (
                <fieldset
                  className={`bg-background border-none p-3 rounded-xl h-full relative ${
                    isActive ? "active__elem" : " "
                  }`}
                  id={`enum_${c.details.info.name}`}
                >
                  {c.details &&
                    c.details.since &&
                    buildSince(
                      moreDetails.since,
                      "absolute top-8 right-10 z-10"
                    )}
                  <Link
                    to={"/docs/" + c.breadcrumbs.join("/")}
                    key={c.details.info.name}
                  >
                    <div className="enum__values bg-muted/50 p-5 rounded-xl flex">
                      <span className="enum__value pl-2">
                        {c.details.info.name}
                      </span>
                      {/* <span className="enum__value">{enumComp.breadcrumbs.includes("Companion") ? c.breadcrumbs.fromRight(2) : c.breadcrumbs.fromRight(1)}</span>
                    <p className="">.</p>
                    <span className="enum__value">{c.details.info.name}</span> */}
                    </div>
                  </Link>
                  {c.details.info.doc && (
                    <p className="text-muted-foreground mt-3 px-2">
                      {parseMarkdownLinks(c.details.info.doc)}
                    </p>
                  )}
                </fieldset>
              );
            })}
          </ul>
        </div>
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
      <Table className="rounded-md overflow-hidden bg-muted/50 rounded-xl mt-3">
        <TableHeader>
          <TableRow>
            <TableHead className="w-[200px] pl-6">Parameter</TableHead>
            <TableHead className="w-[200px]">Type</TableHead>
            <TableHead className="w-[200px]">Default</TableHead>
            <TableHead>Description</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {parameters.map((p: any) => {
            return (
              <TableRow key={p.name}>
                <TableCell>
                  <code className="relative rounded font-mono pl-2">
                    {p.name}
                  </code>
                </TableCell>
                <TableCell>
                  <code className="relative rounded font-mono">
                    {parseMarkdownLinksNoCode(p.type)}
                  </code>
                </TableCell>
                <TableCell>
                  {p.defaultValue ? (
                    <code className="relative rounded font-mono">
                      {parseMarkdownLinksNoCode(p.defaultValue)}
                    </code>
                  ) : (
                    "-"
                  )}
                </TableCell>
                <TableCell>{p.doc ? parseMarkdownLinks(p.doc) : "-"}</TableCell>
              </TableRow>
            );
          })}
        </TableBody>
      </Table>
    );
  }

  function parseMarkdownLinks(text: string): JSX.Element[] {
    return parseMarkdownLinksNoCode(text);
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
      const linkTarget = match[2] ? `/docs/${match[2]}` : `#${match[1]}`;
      if (linkTarget.indexOf("#") != -1) {
        elements.push(
          <code className="relative rounded font-mono">{linkText}</code>
        );
      } else if (linkTarget.startsWith("https")) {
        elements.push(
          <code className="relative rounded font-mono">
            <Link
              className="text-primary"
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
          <code className="relative rounded font-mono">
            <Link
              className="text-primary"
              key={match.index}
              to={window.location.host + `/docs/${linkTarget}`}
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
                className="text-primary font-mono"
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
              <Link className="text-primary font-mono" to={linkTarget}>
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

  function parseMarkdownLinksNoCode(text: string): JSX.Element[] {
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
              className="text-primary font-mono"
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
              className="text-primary font-mono"
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

  function buildBreadcrumbs(path: string) {
    let paths = path.split("/");
    let currentPath = "";

    let breadcrumbs = paths.map((b: string, index: number) => {
      currentPath += "/" + b;
      return (
        <>
          <BreadcrumbItem>
            <BreadcrumbLink to={`${currentPath}`}>
              {b === "docs" ? "Reference" : b}
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

  function getClassParameters(parameters: any) {
    return (
      <fieldset className="flex flex-col gap-4 w-full bg-muted/50 rounded-xl overflow-hidden">
        <Table>
          <TableBody>
            {parameters.map((p: any) => {
              return (
                <TableRow key={p.name} className="relative">
                  <TableCell className="w-[250px]">
                    <code className="relative rounded pl-3 font-mono">
                      {p.name}
                    </code>
                  </TableCell>
                  <TableCell className="text-sm">
                    {p.doc ? parseMarkdownLinksNoCode(p.doc) : "-"}
                  </TableCell>
                  <TableCell className="pr-5">
                    {currentComponent[p.name] &&
                      currentComponent[p.name].details &&
                      currentComponent[p.name].details[0] &&
                      currentComponent[p.name].details[0].since &&
                      buildSince(
                        currentComponent[p.name].details[0].since,
                        "float-right"
                      )}
                  </TableCell>
                </TableRow>
              );
            })}
          </TableBody>
        </Table>
      </fieldset>
    );
  }

  function getClassThrows(throws: any) {
    return (
      <fieldset className="flex flex-col gap-4 w-full bg-muted/50 rounded-xl overflow-hidden throws__table">
        <Table>
          <TableBody>
            {throws.map((p: any) => (
              <TableRow
                key={p.name}
                className="border-b-red-500/5 bg-red-500/5 hover:bg-red-500/10 flex items-center"
              >
                <TableCell className="min-w-[350px] text-red-500 items-center inline-flex gap-1 pl-6 py-2">
                  <i className="material-symbols-rounded text-lg text-red-500">
                    {exceptionIcons[p.name] || "warning"}
                  </i>
                  <code className="relative rounded pl-3 font-mono">
                    {p.name}
                  </code>
                </TableCell>
                <TableCell className="text-sm w-full text-red-500 py-2">
                  {p.doc ? parseMarkdownLinksNoCode(p.doc) : "-"}
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </fieldset>
    );
  }

  function getPackageComponent(c: any) {
    return (
      <div>
        <h2 className="font-bold text-4xl mb-4">{c.info.name}</h2>
        <p className="text-muted-foreground text-justify mb-4">
          {parseMarkdownLinksNoCode(c.info.doc)}
        </p>
        <Separator className="my-4" />
        <h4 className="font-bold text-xl mb-4 indent-1">API</h4>
        <CodeTab
          code={createKotlinCodeLinebreaks(c.info.signature)}
          autoIndent={false}
        />
        {c.parameters && (
          <>
            <h4 className="font-bold text-xl mb-4 mt-10 indent-1">
              Parameters
            </h4>
            {getClassParameters(c.parameters)}
          </>
        )}
        {c.throws && (
          <>
            <h4 className="font-bold text-xl mb-4 mt-10 indent-1">Throws</h4>
            {getClassThrows(c.throws)}
          </>
        )}
      </div>
    );
  }

  function buildComponentBanners(c: any, full: any, current: any) {
    let bannerCount = 0;
    let banners = [];

    if (c.deprecated)
      banners.push(
        <div className="bg-destructive/10 text-red-500 px-4 py-2 rounded-xl flex gap-8 items-center docs-banner">
          <div className="flex items-center gap-3">
            <i className="material-symbols-rounded text-lg text-red-500">
              running_with_errors
            </i>
            <h4 className="font-bold">Deprecated</h4>
          </div>
          <p>
            This API-Endpoint will be removed in future versions.{" "}
            {c.deprecated.description}.
          </p>
        </div>
      );

    if (current && current["Companion"] && current["Companion"]._index)
      banners.push(
        <Link to={"/docs/" + current["Companion"]._index.breadcrumbs.join("/")}>
          <div className="bg-bgw-blue/10 text-bgw-blue px-4 py-2 rounded-xl flex gap-8 items-center relative docs-banner">
            <div className="flex items-center gap-3">
              <i className="material-symbols-rounded text-lg text-bgw-blue">
                note_stack_add
              </i>
              <h4 className="font-bold">Companion</h4>
            </div>
            <p>
              This API-Endpoint may have additional properties in it's Companion
              object.
            </p>
            <i className="material-symbols-rounded text-xl text-bgw-blue right-4 absolute">
              chevron_right
            </i>
          </div>
        </Link>
      );

    if (full && full.breadcrumbs.includes("Companion"))
      banners.push(
        <Link to={"/docs/" + full.breadcrumbs.excludeLast().join("/")}>
          <div className="bg-bgw-green/10 text-bgw-green px-4 py-2 rounded-xl flex gap-8 items-center relative docs-banner">
            <div className="flex items-center gap-3">
              <i className="material-symbols-rounded text-lg text-bgw-green">
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

    if (current && !full && current.breadcrumbs.includes("Companion"))
      banners.push(
        <Link to={"/docs/" + current.breadcrumbs.excludeLast(2).join("/")}>
          <div className="bg-bgw-green/10 text-bgw-green px-4 py-2 rounded-xl flex gap-8 items-center relative docs-banner">
            <div className="flex items-center gap-3">
              <i className="material-symbols-rounded text-lg text-bgw-green">
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

    if (
      !full &&
      current.breadcrumbs &&
      current.breadcrumbs.length > 2 &&
      current.breadcrumbs.fromRight(0) === current.breadcrumbs.fromRight(1)
    )
      banners.push(
        <Link to={"/docs/" + current.breadcrumbs.excludeLast(1).join("/")}>
          <div className="bg-bgw-green/10 text-bgw-green px-4 py-2 rounded-xl flex gap-8 items-center relative docs-banner">
            <div className="flex items-center gap-3">
              <i className="material-symbols-rounded text-lg text-bgw-green">
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

    // TODO: Add a "back" button if this is type of other class
    // if (current && !full && current.breadcrumbs.includes("Companion"))
    //   banners.push(
    //     <Link to={"/docs/" + current.breadcrumbs.excludeLast(2).join("/")}>
    //       <div className="bg-green-400/20 text-green-400 px-4 py-2 rounded-xl flex gap-8 items-center relative">
    //         <div className="flex items-center gap-3">
    //           <i className="material-symbols-rounded text-lg text-green-400">
    //             reply
    //           </i>
    //           <h4 className="font-bold">Context</h4>
    //         </div>
    //         <p>
    //           This property is part of the Companion object for{" "}
    //           {current.breadcrumbs.fromRight(2)}.
    //         </p>
    //       </div>
    //     </Link>
    //   );

    if (
      full &&
      full.members &&
      full.members.types &&
      full.members.types.length > 0
    ) {
      full.members.types.forEach((type) => {
        if (type.name === "Companion") return null;
        banners.push(
          <Link to={"/docs/" + type.link}>
            <div className="bg-bgw-blue/10 text-bgw-blue px-4 py-2 rounded-xl flex gap-8 items-center relative docs-banner">
              <div className="flex items-center gap-3">
                <i className="material-symbols-rounded text-lg text-bgw-blue">
                  note_stack
                </i>
                <h4 className="font-bold">
                  {c.info.name}.{type.name}
                </h4>
              </div>
              <p>{parseMarkdownNoLinks(type.doc)}</p>
              <i className="material-symbols-rounded text-xl text-bgw-blue right-4 absolute">
                chevron_right
              </i>
            </div>
          </Link>
        );
      });
    }

    return banners;
  }

  function buildSince(since: string, className: string = "") {
    return (
      <Link
        to={`https://github.com/tudo-aqua/bgw/releases/tag/v${since}`}
        target="_blank"
        className={className}
      >
        <Badge variant="mutedLink">Since {since}</Badge>
      </Link>
    );
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
      <div>
        {banners.length > 0 && (
          <div className="flex flex-col gap-4 mb-8">{banners}</div>
        )}
        <div className="flex gap-8 items-center mb-5">
          <h2 className={`font-bold text-4xl`}>
            {c.info.name === "Companion"
              ? `${c.info.name} (${full.breadcrumbs.fromRight(1)})`
              : c.info.name}
          </h2>
          <div className="flex gap-2 items-center mt-1">{tags}</div>
        </div>
        {c.info.doc.trim() !== "" && (
          <p className="text-muted-foreground text-justify mb-4 whitespace-pre-wrap flex flex-col gap-1">
            {parseClassDoc(c.info.doc)}
          </p>
        )}
        {inheritors.length > 0 && (
          <div className="flex gap-2 items-center mt-2 relative h-6">
            <h4 className="font-bold text-xs absolute left-0">Inheritors:</h4>
            <ul className="flex gap-2 absolute items-center left-20">
              {inheritors}
            </ul>
          </div>
        )}
        {seeAlso.length > 0 && (
          <div className="flex gap-2 items-center mt-2 relative h-6">
            <h4 className="font-bold text-xs absolute left-0">See also:</h4>
            <ul className="flex gap-2 absolute items-center left-20">
              {seeAlso}
            </ul>
          </div>
        )}
        <div className="mt-8"></div>
        <CodeTab
          code={createKotlinCodeLinebreaks(c.info.signature)}
          autoIndent={false}
        />
        {c.parameters && full === null && (
          <>
            <h4 className="font-bold text-xl mb-4 mt-10 indent-1">
              Parameters
            </h4>
            {getClassParameters(c.parameters)}
          </>
        )}
        {c.throws && (
          <>
            <h4 className="font-bold text-xl mb-4 mt-10 indent-1">Throws</h4>
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
      <div className="px-10 max-w-[1600px] pt-5 mb-10 w-full component">
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
