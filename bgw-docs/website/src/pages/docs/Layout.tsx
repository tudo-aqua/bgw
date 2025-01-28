import React, { useEffect, useMemo, useRef, useState } from "react";

import { BadgeIcon } from "@/components/ui/badgeIcon";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible";
import { ScrollArea } from "@/components/ui/scroll-area";
import {
  Sidebar,
  SidebarContent,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuButton,
  SidebarMenuItem,
  SidebarMenuSub,
  SidebarMenuSubButton,
  SidebarMenuSubItem,
  SidebarProvider,
} from "@/components/ui/sidebar";
import {
  ChevronRight,
  GalleryVerticalEnd,
  NotebookTextIcon,
} from "lucide-react";
import { Link, useLoaderData, useLocation, useParams } from "react-router-dom";
import Component from "./Component";
import LoadingBar from "react-top-loading-bar";
import Guide from "./Guide";

import "./markdown.scss";
import { useDocsStore } from "@/stores/docsStore";
import { Separator } from "@/components/ui/separator";
import Packages from "./Packages";
import { guideStructure, layoutMap } from "@/lib/utils";
import { set } from "date-fns";

function Layout() {
  const { dirs, allSamples } = useLoaderData();

  const { componentPath } = useParams();
  const [currentName, setCurrentName] = useState(componentPath || "");
  const [currentPage, setCurrentPage] = useState<"docs" | "guides" | "main">(
    "main"
  );
  const { currentComponent, setCurrentComponent, secondarySidebar } =
    useDocsStore();

  const location = useLocation();

  const allDirs = useMemo(() => {
    return dirs;
  }, []);

  useEffect(() => {
    if (location.pathname.endsWith("/")) {
      location.pathname = location.pathname.slice(0, -1);
    }

    console.log(location.pathname);
    if (location.pathname === "/" || location.pathname.trim() === "") {
      setCurrentPage("main");
    } else if (location.pathname.startsWith("/docs")) {
      let path = location.pathname.replace("/docs/", "");
      console.log(path);
      if (
        path.replace("/docs", "") === "" ||
        path.replace("/docs", "") === "/"
      ) {
        setCurrentComponent(null);
      } else {
        setCurrentComponent(path.split("/").reduce((o, i) => o[i], allDirs));
      }
      setCurrentPage("docs");
    } else {
      setCurrentPage("guides");
    }
  }, [location, setCurrentComponent, allDirs]);

  function buildNavStructure(data, loc) {
    const navMain = [];

    Object.keys(data).forEach((key) => {
      if (key === "_index") {
        return;
      }

      let currentPath = loc.pathname.split("/").slice(2)[0];

      const section = {
        title: key.split(".").pop(),
        url: "/docs/" + data[key]._index.breadcrumbs.join("/"),
        isActive: data[key]._index.breadcrumbs.includes(currentPath),
        icon: GalleryVerticalEnd,
        items: [],
      };

      Object.keys(data[key]).forEach((subKey) => {
        if (subKey === "_index") {
          return;
        }

        if (subKey.toUpperCase() === subKey) {
          return;
        }

        if (subKey === "findComponent" || subKey === "getRootNode") {
          return;
        }

        let isClass =
          data[key][subKey]._index &&
          (data[key][subKey]._index.details.info.tags.type === "class" ||
            data[key][subKey]._index.details.info.tags.type === "enum" ||
            data[key][subKey]._index.details.info.tags.type === "object");

        let hasModifier =
          data[key][subKey]._index &&
          (data[key][subKey]._index.details.info.tags.modifiers.includes(
            "sealed"
          ) ||
            data[key][subKey]._index.details.info.tags.modifiers.includes(
              "abstract"
            ));

        let actualType = hasModifier
          ? "interface"
          : data[key][subKey]._index
          ? data[key][subKey]._index.details.info.tags.type
          : "extra";

        section.items.push({
          title: subKey,
          url: "/docs/" + key + "/" + subKey,
          isActive: loc.pathname.split("/").includes(subKey),
          isInstantiable: isClass && !hasModifier,
          type: actualType,
          deprecated:
            data[key][subKey]._index &&
            data[key][subKey]._index.details &&
            data[key][subKey]._index.details.deprecated,
          // url: "/docs/" + data[key][subKey]._index ? data[key][subKey]._index.breadcrumbs.join("/") : "#"
        });
      });

      // section.items.sort((a, b) => {
      //   if (a.isInstantiable && !b.isInstantiable) return -1;
      //   if (!a.isInstantiable && b.isInstantiable) return 1;
      //   return a.title.localeCompare(b.title);
      // });

      navMain.push(section);
    });

    return navMain;
  }

  function buildTopSidebar(loc) {
    return (
      <>
        <SidebarHeader>
          <SidebarMenu>
            <SidebarMenuItem>
              <SidebarMenuButton
                size="lg"
                asChild
                className="pointer-events-none"
              >
                {currentPage === "main" ? (
                  <a href="#" className="flex items-center gap-4">
                    <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-sidebar-accent text-sidebar-accent-foreground">
                      <img src="/bgw/logo.svg" className="w-5 h-5" />
                    </div>
                    <div className="flex flex-col gap-1 leading-none">
                      <span className="font-semibold">BGW - v1.0</span>
                      <span className="">Documentation</span>
                    </div>
                  </a>
                ) : currentPage === "docs" ? (
                  <a href="#" className="flex items-center gap-4">
                    <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-purple-500/20  text-purple-500">
                      <i className="material-symbols-rounded text-lg">topic</i>
                    </div>
                    <div className="flex flex-col gap-1 leading-none">
                      <span className="font-semibold">BGW - v1.0</span>
                      <span className="">Reference</span>
                    </div>
                  </a>
                ) : currentPage === "guides" ? (
                  <a href="#" className="flex items-center gap-4">
                    <div className="flex aspect-square size-8 items-center justify-center rounded-lg bg-bgw-blue/20  text-bgw-blue">
                      <i className="material-symbols-rounded text-lg">book_2</i>
                    </div>
                    <div className="flex flex-col gap-1 leading-none">
                      <span className="font-semibold">BGW - v1.0</span>
                      <span className="">Guides</span>
                    </div>
                  </a>
                ) : null}
              </SidebarMenuButton>
            </SidebarMenuItem>
          </SidebarMenu>
        </SidebarHeader>
        <SidebarContent className="h-fit flex-none pb-4">
          <SidebarGroup className="py-0">
            <SidebarMenu>
              <SidebarMenuItem>
                <Link to="/">
                  {currentPage === "main" ? (
                    <SidebarMenuButton className="h-9 indent-1 text-sidebar-accent-foreground bg-sidebar-accent hover:bg-sidebar-accent hover:text-sidebar-accent-foreground">
                      <i className="material-symbols-rounded text-base">home</i>
                      <span className="pl-1 font-medium">Home</span>
                    </SidebarMenuButton>
                  ) : (
                    <SidebarMenuButton className="h-9 indent-1 hover:bg-sidebar-accent hover:text-sidebar-accent-foreground group active:bg-sidebar-accent active:text-sidebar-accent-foreground">
                      <i className="material-symbols-rounded text-base text-[#FFFFFFB3] group-hover:text-sidebar-accent-foreground">
                        home
                      </i>
                      <span className="pl-1 font-medium">Home</span>
                    </SidebarMenuButton>
                  )}
                </Link>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <Link to="/docs/">
                  {currentPage === "docs" ? (
                    <SidebarMenuButton className="h-9 indent-1 text-purple-500 bg-purple-500/10 hover:bg-purple-500/10 hover:text-purple-500">
                      <i className="material-symbols-rounded text-base">
                        topic
                      </i>
                      <span className="pl-1 font-medium">Reference</span>
                    </SidebarMenuButton>
                  ) : (
                    <SidebarMenuButton className="h-9 indent-1 hover:bg-purple-500/10 hover:text-purple-500 group active:bg-purple-500/10 active:text-purple-500">
                      <i className="material-symbols-rounded text-base text-[#FFFFFFB3] group-hover:text-purple-500">
                        topic
                      </i>
                      <span className="pl-1 font-medium">Reference</span>
                    </SidebarMenuButton>
                  )}
                </Link>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <Link to="/guides">
                  {currentPage === "guides" ? (
                    <SidebarMenuButton className="h-9 indent-1 text-bgw-blue bg-bgw-blue/10 hover:bg-bgw-blue/10 hover:text-bgw-blue">
                      <i className="material-symbols-rounded text-base">
                        book_2
                      </i>
                      <span className="pl-1 font-medium">Guides</span>
                    </SidebarMenuButton>
                  ) : (
                    <SidebarMenuButton className="h-9 indent-1 hover:bg-bgw-blue/10 hover:text-bgw-blue group active:bg-bgw-blue/10 active:text-bgw-blue">
                      <i className="material-symbols-rounded text-base text-[#FFFFFFB3] group-hover:text-bgw-blue">
                        book_2
                      </i>
                      <span className="pl-1 font-medium">Guides</span>
                    </SidebarMenuButton>
                  )}
                </Link>
              </SidebarMenuItem>
              <SidebarMenuItem>
                <Link to="/playground">
                  <SidebarMenuButton className="h-9 indent-1 hover:bg-bgw-green/10 hover:text-bgw-green group active:bg-bgw-green/10 active:text-bgw-green group">
                    <i className="material-symbols-rounded text-base text-[#FFFFFFB3] group-hover:text-bgw-green">
                      draw_abstract
                    </i>
                    <span className="pl-1 font-medium">Playground</span>
                  </SidebarMenuButton>
                </Link>
              </SidebarMenuItem>
            </SidebarMenu>
          </SidebarGroup>
        </SidebarContent>
        <Separator />
      </>
    );
  }

  let [openedSections, setOpenedSections] = useState([]);
  let [openedGuideSections, setOpenedGuideSections] = useState([]);
  let [navMain, setNavMain] = useState([]);

  useEffect(() => {
    let temp = buildNavStructure(allDirs, location);
    setNavMain(temp);

    let openSections = [];
    temp.forEach((section) => {
      if (section.isActive) {
        openSections.push(section.title);
      }
    });

    setOpenedSections(openSections);

    let openGuideSections = [];
    Object.keys(guideStructure).forEach((section) => {
      let elem = guideStructure[section];
      if (elem.items.some((item) => item.url === location.pathname)) {
        openGuideSections.push(elem.dir);
      }

      if (location.pathname === "/guides" && elem.dir === "") {
        openGuideSections.push(elem.dir);
      }
    });

    setOpenedGuideSections(openGuideSections);
  }, [location, allDirs]);

  function buildCurrentSidebar(loc) {
    console.log(openedSections);
    return (
      <Sidebar
        variant="sidebar"
        collapsible="none"
        className="w-full bg-background"
      >
        {buildTopSidebar(loc)}
        <SidebarContent>
          <SidebarGroupLabel className="pl-4 h-fit pt-5">
            Packages
          </SidebarGroupLabel>
          <SidebarGroup className="py-0">
            <SidebarMenu>
              {navMain.map((item) => (
                <Collapsible
                  key={item.title}
                  asChild
                  open={openedSections.includes(item.title)}
                  onOpenChange={(isOpen) => {
                    setOpenedSections(
                      isOpen
                        ? [...openedSections, item.title]
                        : openedSections.filter(
                            (section) => section !== item.title
                          )
                    );
                  }}
                  className="group/collapsible"
                >
                  <SidebarMenuItem>
                    <CollapsibleTrigger asChild>
                      <SidebarMenuButton
                        tooltip={item.title}
                        className="h-9 indent-1"
                      >
                        {item.icon && (
                          <i className="material-symbols-rounded text-base text-[#FFFFFFB3]">
                            {layoutMap[item.title].icon}
                          </i>
                        )}
                        <span className="pl-1 font-medium">
                          {layoutMap[item.title].title}
                        </span>
                        <ChevronRight className="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90" />
                      </SidebarMenuButton>
                    </CollapsibleTrigger>
                    <CollapsibleContent>
                      <SidebarMenuSub className="ml-[18px] mr-[7px] pr-0">
                        {item.items?.map((subItem) => (
                          <SidebarMenuSubItem key={subItem.title}>
                            <SidebarMenuSubButton
                              asChild
                              isActive={subItem.isActive}
                              className={`h-9 indent-1 ${
                                subItem.deprecated ? "line-through" : ""
                              }`}
                            >
                              <Link to={subItem.url}>
                                {subItem.type && (
                                  <BadgeIcon variant={subItem.type}></BadgeIcon>
                                )}
                                <span
                                  className={
                                    !subItem.isInstantiable
                                      ? "text-muted-foreground italic"
                                      : ""
                                  }
                                >
                                  {subItem.title}
                                </span>
                              </Link>
                            </SidebarMenuSubButton>
                          </SidebarMenuSubItem>
                        ))}
                      </SidebarMenuSub>
                    </CollapsibleContent>
                  </SidebarMenuItem>
                </Collapsible>
              ))}
            </SidebarMenu>
          </SidebarGroup>
        </SidebarContent>
      </Sidebar>
    );
  }

  function buildGuidesSidebar() {
    let structure = guideStructure;
    return (
      <Sidebar
        variant="sidebar"
        collapsible="none"
        className="w-full bg-background"
      >
        {buildTopSidebar(location)}
        <SidebarContent>
          <SidebarGroupLabel className="pl-4 h-fit pt-5">
            Guides
          </SidebarGroupLabel>
          <SidebarGroup className="py-0">
            <SidebarMenu>
              {Object.entries(structure).map(([section, content]) => (
                <Collapsible
                  key={section}
                  asChild
                  className="group/collapsible"
                  open={openedGuideSections.includes(content.dir)}
                  onOpenChange={(isOpen) => {
                    setOpenedGuideSections(
                      isOpen
                        ? [...openedGuideSections, content.dir]
                        : openedGuideSections.filter(
                            (section) => section !== content.dir
                          )
                    );
                  }}
                >
                  <SidebarMenuItem>
                    <CollapsibleTrigger asChild>
                      <SidebarMenuButton
                        tooltip={section}
                        className="h-9 indent-1"
                      >
                        <i className="material-symbols-rounded text-base text-[#FFFFFFB3]">
                          {content.icon}
                        </i>
                        <span className="pl-1 font-medium">{section}</span>
                        <ChevronRight className="ml-auto transition-transform duration-200 group-data-[state=open]/collapsible:rotate-90" />
                      </SidebarMenuButton>
                    </CollapsibleTrigger>
                    <CollapsibleContent>
                      <SidebarMenuSub className="ml-[18px] mr-[7px] pr-0">
                        {content.items.map((item) => (
                          <SidebarMenuSubItem key={item.url}>
                            <SidebarMenuSubButton
                              asChild
                              className="h-9 indent-1"
                              isActive={
                                item.url === location.pathname ||
                                (location.pathname === "/guides" &&
                                  item.url === "/guides/installation")
                              }
                            >
                              <Link to={item.url}>{item.title}</Link>
                            </SidebarMenuSubButton>
                          </SidebarMenuSubItem>
                        ))}
                      </SidebarMenuSub>
                    </CollapsibleContent>
                  </SidebarMenuItem>
                </Collapsible>
              ))}
            </SidebarMenu>
          </SidebarGroup>
        </SidebarContent>
      </Sidebar>
    );
  }

  function buildSecondarySidebar(sidebar) {
    return (
      <Sidebar
        variant="sidebar"
        collapsible="none"
        className="w-full bg-background"
      >
        <SidebarContent>
          {/* We create a SidebarGroup for each parent. */}
          {sidebar.map((item, index) => (
            <SidebarGroup key={item.url + item.title + index}>
              <SidebarGroupLabel>{item.title}</SidebarGroupLabel>
              <SidebarGroupContent>
                <SidebarMenu className="border-none font-mono p-0 gap-0">
                  {item.items.map((item, i) => (
                    <SidebarMenuItem
                      key={item.url + item.title + i}
                      className="p-0"
                    >
                      <SidebarMenuButton
                        asChild
                        isActive={item.isActive}
                        className="px-2 py-0"
                      >
                        <Link
                          to={item.url}
                          className="text-xs !h-7 w-[98%] flex justify-between items-center"
                        >
                          {item.title}
                          {/* <BadgeIcon
                            variant={item.type}
                            style={{
                              transform: "scale(0.7)",
                              marginRight: "0.25rem",
                            }}
                          ></BadgeIcon> */}
                        </Link>
                      </SidebarMenuButton>
                    </SidebarMenuItem>
                  ))}
                </SidebarMenu>
              </SidebarGroupContent>
            </SidebarGroup>
          ))}
        </SidebarContent>
      </Sidebar>
    );
  }

  const scrollRef = React.useRef(null);

  const [progress, setProgress] = React.useState(0);
  const { codeBlocksToLoad, loadedCodeBlocks } = useDocsStore();

  useEffect(() => {
    if (codeBlocksToLoad === 0) {
      setProgress(0);
      return;
    }
    console.log(loadedCodeBlocks + " / " + codeBlocksToLoad);
    const percentage = (loadedCodeBlocks / codeBlocksToLoad) * 100;
    setProgress(percentage);
  }, [codeBlocksToLoad, loadedCodeBlocks]);

  return (
    <SidebarProvider>
      <LoadingBar
        color="#bb6dff"
        progress={progress}
        onLoaderFinished={() => {
          setProgress(0);
        }}
      />
      <div className="grid h-screen w-full">
        <div className="flex flex-col">
          <header className="sticky top-0 z-10 flex h-[57px] items-center gap-1 border-b bg-background px-4 justify-between">
            <h1 className="text-xl font-semibold">BGW Docs</h1>
          </header>
          <main className="flex items-center flex-1 h-full max-h-[calc(100vh-57px)]">
            <div
              className="relative w-[15%] max-w-[300px] shrink-0 max-h-[calc(100vh-57px)] h-full flex flex-col gap-3"
              x-chunk="dashboard-03-chunk-0"
            >
              {(currentPage === "docs" || currentPage === "main") &&
                buildCurrentSidebar(location)}
              {currentPage === "guides" && buildGuidesSidebar()}
            </div>
            <ScrollArea
              className="relative flex h-full min-h-[50vh] w-full flex-col overflow-hidden bg-muted/30"
              x-chunk="dashboard-03-chunk-0"
              id="comp-scroll-area"
              ref={scrollRef}
            >
              {currentPage === "main" && (
                <div className="flex flex-col items-center justify-center h-full">
                  <h1 className="text-3xl font-semibold">
                    Welcome to BGW Docs
                  </h1>
                  <p className="text-lg text-center mt-4">
                    Select a component from the sidebar to get started.
                  </p>
                </div>
              )}
              {currentPage === "docs" && currentComponent !== null && (
                <Component
                  location={location}
                  dirs={allDirs}
                  allSamples={allSamples}
                />
              )}
              {currentPage === "docs" && currentComponent === null && (
                <Packages packages={layoutMap} />
              )}
              {currentPage === "guides" && (
                <Guide location={location} allSamples={allSamples} />
              )}
            </ScrollArea>
            {currentPage === "docs" && (
              <ScrollArea
                className="h-full relative hidden flex-col items-center gap-8 md:flex px-1 w-[15%] shrink-0 max-w-[300px]"
                x-chunk="dashboard-03-chunk-0"
                id="sidebar-scroll-area"
              >
                {buildSecondarySidebar(secondarySidebar)}
              </ScrollArea>
            )}
          </main>
        </div>
      </div>
    </SidebarProvider>
  );
}

export default Layout;
