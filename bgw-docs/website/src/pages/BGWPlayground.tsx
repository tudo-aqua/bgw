import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Drawer, DrawerTrigger } from "@/components/ui/drawer";
import React, { useEffect, useState } from "react";
import * as Components from "@/lib/components.ts";
import { DataClass, DataClasses } from "@/lib/components.ts";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area.tsx";
import useWebSocket from "react-use-websocket";
import { useMap } from "@uidotdev/usehooks";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import ImageViewer from "@/pages/ImageViewer.tsx";
import setupIndexedDB, { useIndexedDBStore } from "use-indexeddb";
import { idbConfig } from "@/lib/utils.ts";
import ReactKotlinPlayground from "@/lib/kotlin-playground/ReactKotlinPlayground";
import { render } from "react-dom";
import { CodeParser } from "@/lib/parser.ts";
import { constructors } from "@/lib/constructors.ts";
import { useLoaderData } from "react-router";
import { ConstructorAPIImportInfo } from "@/lib/types.ts";

function Dashboard() {
  // const { data: constructorData = {} as ConstructorAPIImportInfo[], loading } = useFetch(
  //     `http://localhost:8080/api/2.0.10/compiler/constructors`,
  //     {
  //         method: "GET",
  //         headers: {
  //             Accept: "application/json",
  //             "Content-Type": "application/json",
  //         },
  //     },
  //     []
  // );

  // const { data : result, post } = useFetch(
  //     `http://localhost:8080/api/2.0.10/compiler/run`,
  //     {
  //         method: "POST",
  //         headers: {
  //             Accept: "application/json",
  //             "Content-Type": "application/json",
  //         },
  //     }
  // );

  // useEffect(() => {
  //     console.log(loading, constructorData)
  //     window.conData = constructorData
  // }, [constructorData, loading]);

  useEffect(() => {
    setupIndexedDB(idbConfig)
      .then(() => console.log("success"))
      .catch((e) => console.error("error / unsupported", e));
  }, []);

  //Public API that will echo messages sent to it back to the client
  const [socketUrl, setSocketUrl] = useState("ws://localhost:5173/ws");
  const { sendJsonMessage } = useWebSocket(socketUrl);

  const [selectedClass, setSelectedClass] = useState(null);

  const [selectedComponentId, setSelectedComponentId] = useState("");
  const allComponents = useMap([]);

  const [treeData, setTreeData] = useState([]);

  const getInstantiableByData = (data: string) => {
    const flat = Object.entries(Components.Instantiable).flatMap((e) =>
      Object.entries(e[1])
    );
    return flat.find((e) => e[1].cls === data)[1];
  };

  const [imageGallery, setImageGallery] = useState([]);
  const { getAll, getOneByKey } = useIndexedDBStore("images");

  useEffect(() => {
    getAll().then((images) => {
      setImageGallery(images);
    });
  }, []);

  const refreshImages = () => {
    getAll().then((images) => {
      setImageGallery(images);
    });
  };

  useEffect(() => {
    if (loading) return;
    const code = `
            import tools.aqua.bgw.components.gamecomponentviews.*
            import tools.aqua.bgw.components.layoutviews.*
            import tools.aqua.bgw.components.container.*
            import tools.aqua.bgw.components.uicomponents.*
            import tools.aqua.bgw.core.*
            import tools.aqua.bgw.visual.*
        
            class GameScene() : BoardGameScene(1920, 1080, background = ColorVisual.WHITE) {
            
            }
        `;
    handleCodeChange(code);
  }, [loading]);

  useEffect(() => {
    console.log(selectedClass);
    if (selectedClass === null) return;
    allComponents.set(selectedClass.id, selectedClass);
    sendOutputElements();
  }, [selectedClass, treeData]);

  // Initialize the Kotlin Playground
  useEffect(() => {
    const node = document.getElementById("kotlin__playground");
    const props = {
      autoIndent: 2,
      targetPlatform: "jvm",
      theme: "default",
      "auto-indent": "true",
      server: "http://localhost:8080",
      //"data-autocomplete": "true",
      onChange: (code) => {
        handleCodeChange(code);
      },
    };

    const code = `
            import tools.aqua.bgw.components.gamecomponentviews.*
            import tools.aqua.bgw.components.layoutviews.*
            import tools.aqua.bgw.components.container.*
            import tools.aqua.bgw.components.uicomponents.*
            import tools.aqua.bgw.core.*
            import tools.aqua.bgw.visual.*
        
            class GameScene() : BoardGameScene(1920, 1080, background = ColorVisual.WHITE) {
            
            }
        `;
    render(React.createElement(ReactKotlinPlayground, props, code), node);
  }, []);

  const getElementFromId = (id) => {
    if (id === 0) return null;
    return allComponents.get(id) ?? null;
  };

  const getAllChildren = (id) => {
    const children = [];
    treeData.forEach((node) => {
      if (node.parent === id) {
        children.push(node);
        // children.push(...getAllChildren(node.id))
      }
    });

    return children;
  };

  const handleCodeChange = (code: string) => {
    post("", {
      args: "",
      files: [
        {
          name: "File.kt",
          text: code,
          publicId: "",
        },
        {
          name: "Main.kt",
          text: "import tools.aqua.bgw.components.gamecomponentviews.*\nimport tools.aqua.bgw.components.layoutviews.*\nimport tools.aqua.bgw.components.container.*\nimport tools.aqua.bgw.components.uicomponents.*\nimport tools.aqua.bgw.core.*\nimport tools.aqua.bgw.visual.*\nimport tools.aqua.bgw.builder.*\n\nfun main() {\n    println(SceneBuilder.respondJSON(GameScene()))\n}",
          publicId: "",
        },
      ],
      confType: "java",
    }).then((data) => {
      console.log(data);
      if (data !== null && data.exception === null) {
        let res = JSON.parse(
          data.text.replace("</outStream>", "").replace("<outStream>", "")
        );
        console.log(res);
        sendJsonMessage(res);
      }
    });

    /* let parser = new CodeParser()
        let result = parser.tryParse(code)

        allComponents.clear()
        while(treeData.length > 0) {
            treeData.pop()
        }

        if(result) {
            for (let newObj of result) {
                allComponents.set(newObj.id, newObj)
                treeData.push({
                    "id": newObj.id,
                    "parent": 0,
                    "droppable": newObj.objectType.includes("TableViewData") || newObj.objectType.includes("LayoutViewData") || newObj.objectType.includes("ToggleGroupData") || newObj.objectType.includes("GameComponentContainerData") || newObj.objectType.includes("GridElementData") || newObj.objectType.includes("CameraPaneData") || newObj.objectType.includes("HexagonGridElementData"),
                    "text": newObj.name.value
                })
                setSelectedClass(newObj);
                setSelectedComponentId(newObj.id)
            }
        } */

    const possibleComponents = window.conData
      .filter((c) => {
        return c.icon === "class" || c.icon === "enum";
      })
      .map((c) => {
        return c.shortName;
      });

    document.querySelectorAll(".cm-variable").forEach((e) => {
      if (possibleComponents.find((c) => c === e.innerHTML)) {
        e.classList.add("cm-bgw");
        e.removeEventListener("mouseenter", openTooltip);
        e.removeEventListener("mouseleave", closeTooltip);
        e.addEventListener("mouseenter", openTooltip);
        e.addEventListener("mouseleave", closeTooltip);
      }
    });
  };

  const openTooltip = (e) => {
    let elem = e.target.innerHTML.trim();
    let foundComp = window.conData.find((c) => c.shortName === elem);

    //let component = getInstantiableByData(e.target.innerHTML + "Data")

    e.target.classList.add("hovered");
    if (foundComp !== null && foundComp.constructors.length > 0) {
      let tooltip = document.createElement("div");
      tooltip.classList.add("tooltip");
      tooltip.innerHTML =
        `<b>${foundComp.fullName}</b>` +
        Object.entries(foundComp.constructors[0])
          .map((entry) => {
            return `<div class="flex gap-3">
                    <span>${escapeHtml(entry[0])}</span>
                    :
                    <span>${escapeHtml(entry[1])}</span>
                </div>`;
          })
          .join("");
      tooltip.style.position = "fixed";
      tooltip.style.pointerEvents = "none";
      tooltip.style.top = e.target.getBoundingClientRect().top + 28 + "px";
      tooltip.style.left = e.target.getBoundingClientRect().right - 25 + "px";
      tooltip.style.zIndex = "999999999";
      document.body.appendChild(tooltip);
    } else if (foundComp !== null) {
      let tooltip = document.createElement("div");
      tooltip.classList.add("tooltip");
      tooltip.innerHTML = `<b>${foundComp.fullName}</b>`;
      tooltip.style.position = "fixed";
      tooltip.style.pointerEvents = "none";
      tooltip.style.top = e.target.getBoundingClientRect().top + 28 + "px";
      tooltip.style.left = e.target.getBoundingClientRect().right - 25 + "px";
      tooltip.style.zIndex = "999999999";
      document.body.appendChild(tooltip);
    }
  };

  const escapeHtml = (unsafe: string) => {
    return unsafe
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;")
      .replace(/"/g, "&quot;")
      .replace(/'/g, "&#039;");
  };

  const closeTooltip = (e) => {
    e.target.classList.remove("hovered");
    document.querySelectorAll(".tooltip").forEach((e) => {
      e.remove();
    });
  };

  const checkPossibleErrors = (node) => {
    let own = getElementFromId(node.id);
    if (own == null) return null;

    let parent = getElementFromId(node.parent);
    let children = getAllChildren(node.id);

    if (own.type === "GridElementData") {
      if (parent === null || parent.type !== "GridPaneData") {
        return (node.info = {
          type: "error",
          message: "GridElement must be a child of GridPane",
        });
        //return renderError("GridElement must be a child of GridPane")
      } else if (parent.type === "GridPaneData") {
        let columns = parseInt(parent.columns.value);
        let rows = parseInt(parent.rows.value);

        let col = parseInt(own.column.value);
        let row = parseInt(own.row.value);

        if (isNaN(col) || isNaN(row)) {
          return (node.info = {
            type: "error",
            message: "GridElement has invalid row or column",
          });
        }

        if (col >= columns || row >= rows) {
          return (node.info = {
            type: "error",
            message: "GridElement has out of bounds row or column",
          });
          //return renderError(  "GridElement is out of bounds")
        }

        let allSiblings = treeData
          .filter((n) => n.parent === node.parent && n.id !== node.id)
          .map((n) => getElementFromId(n.id));
        let hasConflict = false;
        allSiblings.forEach((child) => {
          if (
            parseInt(child.column.value) === col &&
            parseInt(child.row.value) === row
          ) {
            hasConflict = true;
          }
        });

        if (hasConflict) {
          return (node.info = {
            type: "error",
            message:
              "GridElement has the same row or column as another GridElement",
          });
          //return renderError("GridElement has a conflict with another GridElement")
        }
      }
    }

    if (own.type === "TableColumnData") {
      if (parent === null || parent.type !== "TableViewData") {
        return (node.info = {
          type: "error",
          message: "TableColumn must be a child of TableView",
        });
        //return renderError("GridElement must be a child of GridPane")
      }
    }

    if (own.type === "HexagonGridElementData") {
      if (parent === null || parent.type !== "HexagonGridData") {
        return (node.info = {
          type: "error",
          message: "HexagonGridElement must be a child of HexagonGrid",
        });
        //return renderError("GridElement must be a child of GridPane")
      } else if (parent.type === "HexagonGridData") {
        let col = parseInt(own.column.value);
        let row = parseInt(own.row.value);

        if (isNaN(col) || isNaN(row)) {
          return (node.info = {
            type: "error",
            message: "HexagonGridElement has invalid row or column",
          });
        }

        let allSiblings = treeData
          .filter((n) => n.parent === node.parent && n.id !== node.id)
          .map((n) => getElementFromId(n.id));
        let hasConflict = false;
        allSiblings.forEach((child) => {
          if (
            parseInt(child.column.value) === col &&
            parseInt(child.row.value) === row
          ) {
            hasConflict = true;
          }
        });

        if (hasConflict) {
          return (node.info = {
            type: "error",
            message:
              "HexagonGridElement has the same row or column as another GridElement",
          });
          //return renderError("GridElement has a conflict with another GridElement")
        }
      }
    }

    if (own.type === "GridPaneData") {
      if (children.length === 0) {
        return (node.info = {
          type: "warning",
          message: "GridPane needs GridElements as children",
        });
        //return renderWarning("GridPane needs GridElements as children")
      } else {
        /* let hasDifferentElement = false
                children.forEach((child) => {
                    if (getElementFromId(child.id).type !== "GridElementData") {
                        hasDifferentElement = true
                    }
                })
                
                if (hasDifferentElement) {
                    return renderWarning("GridPane can only have GridElements as children")
                } */
      }
    }
    if (own.type === "HexagonGridData") {
      if (children.length === 0) {
        return (node.info = {
          type: "warning",
          message: "HexagonGrid needs HexagonGridElements as children",
        });
        //return renderWarning("GridPane needs GridElements as children")
      } else {
        /* let hasDifferentElement = false
                children.forEach((child) => {
                    if (getElementFromId(child.id).type !== "GridElementData") {
                        hasDifferentElement = true
                    }
                })
                
                if (hasDifferentElement) {
                    return renderWarning("GridPane can only have GridElements as children")
                } */
      }
    }

    if (own.type === "CameraPaneData") {
      if (children.length === 0) {
        return (node.info = {
          type: "error",
          message: "CameraPane needs a Layout as child",
        });
        //return renderWarning("CameraPane needs components as children")
      } else if (children.length > 1) {
        return (node.info = {
          type: "error",
          message: "CameraPane can only have one child",
        });
        //return renderError("CameraPane can only have one child")
      }
    }

    if (own.type === "TableViewData") {
      if (children.length === 0) {
        return (node.info = {
          type: "warning",
          message: "TableView needs a TableColumn as child",
        });
        //return renderWarning("CameraPane needs components as children")
      }
    }

    if (own.type === "PaneData") {
      if (children.length === 0)
        return (node.info = {
          type: "info",
          message: "PaneData can have components as children",
        });
    } else if (own.type === "ToggleGroupData") {
      if (children.length === 0)
        return (node.info = {
          type: "info",
          message:
            "ToggleGroup can have RadioButtons and ToggleButtons as children",
        });
    } else if (own.type === "AreaData") {
      if (children.length === 0)
        return (node.info = {
          type: "info",
          message:
            "AreaData can have GameComponents and Containers as children",
        });
    } else if (own.type === "CardStackData") {
      if (children.length === 0)
        return (node.info = {
          type: "info",
          message: "CardStack can have CardViews as children",
        });
    } else if (own.type === "LinearLayoutData") {
      if (children.length === 0)
        return (node.info = {
          type: "info",
          message: "LinearLayout can have GameComponents as children",
        });
    } else if (own.type === "SatchelData") {
      if (children.length === 0)
        return (node.info = {
          type: "info",
          message: "Satchel can have GameComponents as children",
        });
    }

    if (own.objectType.includes("LayoutViewData")) {
      if (children.length === 0) {
        return (node.info = {
          type: "warning",
          message: "LayoutView needs components as children",
        });
        //return renderWarning("LayoutView needs components as children")
      }
    }

    node.info = null;
    return null;
  };

  const getOutputElementsFromTree = () => {
    const components: DataClass[] = [];
    const allComponentsCopy = {};

    Array.from(allComponents.keys()).map((key) => {
      let elem = allComponents.get(key);
      if (elem == null) return;

      allComponentsCopy[elem.id] = JSON.parse(JSON.stringify(elem));

      //if(elem.type === "GridElementData") return

      if (elem.type === "CardViewData") {
        allComponentsCopy[elem.id].currentVisual =
          allComponentsCopy[elem.id].current === "front"
            ? allComponentsCopy[elem.id].front
            : allComponentsCopy[elem.id].back;
      } else if (elem.type === "DiceViewData") {
        let listToDel = [];
        allComponentsCopy[elem.id].visuals = Object.entries(
          allComponentsCopy[elem.id]
        )
          .filter((entry) => entry[0].startsWith("vis_"))
          .map((entry) => {
            listToDel.push(entry[0]);
            return entry[1];
          });

        listToDel.forEach((key) => {
          delete allComponentsCopy[elem.id][key];
        });

        allComponentsCopy[elem.id].currentSide =
          parseInt(allComponentsCopy[elem.id].currentSide) - 1;
      }

      if (elem.alignment) {
        allComponentsCopy[elem.id].alignment = {
          first: allComponentsCopy[elem.id].horizontalAlignment,
          second: allComponentsCopy[elem.id].verticalAlignment,
        };
      }

      Object.entries(allComponentsCopy[elem.id]).forEach((entry) => {
        if (!entry[1]) return;

        if (entry[1].type === "CompoundVisualData") {
          entry[1].children = entry[1].children.map((child) => {
            if (child.type === "TextVisualData") {
              child.alignment = {
                first: child.horizontalAlignment,
                second: child.verticalAlignment,
              };
            }

            return child;
          });
        }
      });

      delete allComponentsCopy[elem.id].horizontalAlignment;
      delete allComponentsCopy[elem.id].verticalAlignment;
    });

    treeData.forEach((node) => {
      checkPossibleErrors(node);
      let elem = allComponentsCopy[node.id];
      if (elem == null) return;
      if (node.info && node.info.type == "error") return;

      if (elem.type === "ToggleGroupData") return;

      if (node.parent === 0) {
        components.push(elem);
        return;
      }

      let parent = allComponentsCopy[node.parent];
      let parentNode = treeData.find((n) => n.id === node.parent);
      if (parent == null) return;

      if (parentNode && parentNode.info && parentNode.info.type == "error")
        return;

      console.log(parent.type, elem.objectType);
      if (
        parent.type === "ToggleGroupData" &&
        elem.objectType.includes("BinaryStateButtonData")
      ) {
        elem.group = parent.group;
      }

      if (parent.type === "CameraPaneData") {
        parent.target = elem;
      } else if (
        (parent.objectType.includes("LayoutViewData") ||
          parent.objectType.includes("GameComponentContainerData")) &&
        parent.type !== "GridPaneData" &&
        parent.type !== "HexagonGridData"
      ) {
        parent.components.push(elem);
      } else if (parent.objectType.includes("GridElementData")) {
        parent.component = elem;
      } else if (parent.objectType.includes("HexagonGridElementData")) {
        parent.hexagon = elem;
        const nextParent = allComponentsCopy[parentNode.parent];
        if (nextParent && nextParent.objectType.includes("HexagonGridData")) {
          nextParent.map[parent.column + "/" + parent.row] = elem;
        }
      } else if (parent.type === "HexagonGridData") {
        parent.map[elem.column + "/" + elem.row] = elem.hexagon;
      } else if (parent.type === "GridPaneData") {
        parent.grid.push(elem);
      } else if (
        parent.type === "TableViewData" &&
        elem.type === "TableColumnData"
      ) {
        parent.columns.push(elem);
      } else {
        components.push(elem);
      }
    });

    return components;
  };

  const sendOutputElements = () => {
    // return;
    if (allComponents.size === 0) return;

    // const components : DataClass[] = []
    // allComponents.forEach((value, key) => {
    //     components.push(value)
    // })

    const components = getOutputElementsFromTree();

    const comps = JSON.parse(JSON.stringify(components));

    sendJsonMessage({
      data: {
        type: "AppData",
        gameScene: {
          width: 1920,
          height: 1080,
          background: {
            type: "CompoundVisualData",
            id: "bgw-vis-2",
            children: [
              {
                type: "ColorVisualData",
                id: "bgw-vis-2",
                transparency: 1.0,
                flipped: "none",
                color: "rgba(255, 255, 255, 1.0)",
              },
            ],
          },
          components: comps,
        },
        width: 1920,
        height: 1080,
      },
    });
  };

  return (
    <>
      {loading ? (
        <div className="fixed w-full h-full bg-muted left-0 top-0 right-0 bottom-0 z-[900000] flex items-center justify-center">
          <img src={"bgw_animated.svg"} className="w-[150px]" />
        </div>
      ) : null}
      <div className="grid h-screen w-full">
        <div className="flex flex-col">
          <header className="sticky top-0 z-10 flex h-[57px] items-center gap-1 border-b bg-background px-4 justify-between">
            <h1 className="text-xl font-semibold">BGW Playground</h1>
            <Drawer>
              <DrawerTrigger asChild>
                <Button variant="ghost" size="icon" className="md:hidden">
                  <i className="material-symbols-rounded text-xl">settings</i>
                  <span className="sr-only">Settings</span>
                </Button>
              </DrawerTrigger>
            </Drawer>
            <div className="flex gap-3">
              <Dialog>
                <DialogTrigger className="w-full" asChild>
                  <Button
                    variant="outline"
                    size="sm"
                    className="ml-auto gap-3 text-sm w-fit"
                  >
                    <i className="material-symbols-rounded text-lg">
                      perm_media
                    </i>
                    Images
                  </Button>
                </DialogTrigger>
                <DialogContent className="max-w-[800px]" forceMount={true}>
                  <DialogHeader>
                    <DialogTitle>Saved Images</DialogTitle>
                    <DialogDescription>
                      This is a list of all images currently saved in the
                      project.
                    </DialogDescription>
                  </DialogHeader>
                  <ImageViewer
                    onAddImage={refreshImages}
                    images={imageGallery}
                  />
                </DialogContent>
              </Dialog>
              <Button
                variant="outline"
                size="sm"
                className="ml-auto gap-3 text-sm"
              >
                <i className="material-symbols-rounded text-lg">publish</i>
                Share
              </Button>
            </div>
          </header>
          <main className="grid flex-1 gap-4 h-full max-h-[calc(100vh-57px)] p-4 md:grid-cols-2 lg:grid-cols-12">
            <ScrollArea
              className="h-full relative pr-5 col-span-5 flex bg-muted/50 rounded-xl"
              x-chunk="dashboard-03-chunk-0"
            >
              <code id={"kotlin__playground"}></code>
              <ScrollBar orientation="horizontal"></ScrollBar>
              <ScrollBar orientation="vertical"></ScrollBar>
            </ScrollArea>
            <div
              className={`relative flex h-full min-h-[50vh] flex-col bg-muted/50 rounded-xl ${
                selectedClass == null ? "col-span-7" : "col-span-7"
              }`}
            >
              <Badge variant="outline" className="absolute right-3 top-3">
                Output
              </Badge>
              <Badge variant="outline" className="absolute left-3 top-3">
                1920x1080
              </Badge>
              <div className="flex flex-1 justify-center items-center">
                <iframe
                  className="w-full h-full rounded-xl bg-transparent"
                  src="../../public/index.html"
                ></iframe>
                {/*{*/}
                {/*    selectedClass !== null ? drawOutputElement(selectedClass) : null*/}
                {/*}*/}
              </div>
            </div>
          </main>
        </div>
      </div>
    </>
  );
}

export default Dashboard;
