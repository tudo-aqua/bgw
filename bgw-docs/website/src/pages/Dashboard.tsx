import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Drawer, DrawerTrigger } from "@/components/ui/drawer";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import React, { useEffect, useState } from "react";
import * as Components from "@/lib/components.ts";
import {
  BooleanValue,
  ChoiceValue,
  ColorValue,
  ColorVisualData,
  ComponentViewData,
  CompoundVisualData,
  DataClass,
  DiceViewData,
  Droppable,
  generateId,
  ImageVisualData,
  NumberValue,
  PropertyValue,
  SingleLayerVisualData,
  StringValue,
  TextVisualData,
} from "@/lib/components.ts";
import { ScrollArea } from "@/components/ui/scroll-area.tsx";
import { Switch } from "@/components/ui/switch.tsx";
import { ColorPicker } from "@/components/ui/color-picker.tsx";
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
import { Toggle } from "@/components/ui/toggle.tsx";
import {
  DndProvider,
  getBackendOptions,
  MultiBackend,
  Tree,
} from "@minoru/react-dnd-treeview";
import { ImagePicker } from "@/components/ui/image-picker.tsx";
import { SimpleColorPicker } from "@/components/ui/gradient-picker.tsx";
import { TextPicker } from "@/components/ui/text-picker.tsx";
import {
  ContextMenu,
  ContextMenuCheckboxItem,
  ContextMenuContent,
  ContextMenuItem,
  ContextMenuLabel,
  ContextMenuRadioGroup,
  ContextMenuRadioItem,
  ContextMenuSeparator,
  ContextMenuShortcut,
  ContextMenuSub,
  ContextMenuSubContent,
  ContextMenuSubTrigger,
  ContextMenuTrigger,
} from "@/components/ui/context-menu.tsx";
import Parser from "@/pages/Parser.tsx";
import ImageViewer from "@/pages/ImageViewer.tsx";
import setupIndexedDB, { useIndexedDBStore } from "use-indexeddb";
import { idbConfig } from "@/lib/utils.ts";
import { ConstructorAPIImportInfo, Guides } from "@/lib/types.ts";
import SettingsField from "@/components/SettingsField.tsx";
import { exportComponent } from "@/lib/exporter.ts";
import Playground from "@/pages/Playground.tsx";

function Dashboard() {
  useEffect(() => {
    setupIndexedDB(idbConfig)
      .then(() => console.log("success"))
      .catch((e) => console.error("error / unsupported", e));
  }, []);

  const [advancedMode, setAdvancedMode] = useState(false);
  const [hidePermanentGuides, setHidePermanentGuides] = useState(true);
  const [loading, setLoading] = useState(true);
  const [fullScreen, setFullScreen] = useState(false);

  const [compileError, setCompileError] = useState(null);
  const [projectError, setProjectError] = useState(null);
  const [projectWarning, setProjectWarning] = useState(null);

  const onError = (error) => {
    if (error.includes("window.cef")) return;
    setCompileError(error);
  };

  useEffect(() => {
    window.onerror = onError;

    return () => {
      window.onerror = null;
    };
  }, []);

  useEffect(() => {
    setTimeout(() => {
      setLoading(false);
    }, 2000);
  }, []);

  const captureKeyEvent = (e) => {
    if (selectedClass == null) return;
    let delta = e.shiftKey ? 10 : 1;
    let bounds = e.altKey;

    if (!bounds) {
      if (e.key === "ArrowRight") {
        if (!selectedClass.posX || selectedClass.posX.disabled) return;
        e.preventDefault();
        setSelectedClass((prev: object) => {
          return {
            ...prev,
            posX: new NumberValue(
              "posX",
              prev.posX.value + delta,
              "Position X"
            ),
          };
        });
      } else if (e.key === "ArrowLeft") {
        if (!selectedClass.posX || selectedClass.posX.disabled) return;
        e.preventDefault();
        setSelectedClass((prev: object) => {
          return {
            ...prev,
            posX: new NumberValue(
              "posX",
              prev.posX.value - delta,
              "Position X"
            ),
          };
        });
      } else if (e.key === "ArrowUp") {
        if (!selectedClass.posY || selectedClass.posY.disabled) return;
        e.preventDefault();
        setSelectedClass((prev: object) => {
          return {
            ...prev,
            posY: new NumberValue(
              "posY",
              prev.posY.value - delta,
              "Position Y"
            ),
          };
        });
      } else if (e.key === "ArrowDown") {
        if (!selectedClass.posY || selectedClass.posY.disabled) return;
        e.preventDefault();
        setSelectedClass((prev: object) => {
          return {
            ...prev,
            posY: new NumberValue(
              "posY",
              prev.posY.value + delta,
              "Position Y"
            ),
          };
        });
      }
    } else {
      if (e.key === "ArrowRight") {
        if (!selectedClass.width || selectedClass.width.disabled) return;
        e.preventDefault();
        setSelectedClass((prev: object) => {
          return {
            ...prev,
            width: new NumberValue("width", prev.width.value + delta, "Width"),
          };
        });
      } else if (e.key === "ArrowLeft") {
        if (!selectedClass.width || selectedClass.width.disabled) return;
        e.preventDefault();
        setSelectedClass((prev: object) => {
          return {
            ...prev,
            width: new NumberValue("width", prev.width.value - delta, "Width"),
          };
        });
      } else if (e.key === "ArrowUp") {
        if (!selectedClass.height || selectedClass.height.disabled) return;
        e.preventDefault();
        setSelectedClass((prev: object) => {
          return {
            ...prev,
            height: new NumberValue(
              "height",
              prev.height.value - delta,
              "Height"
            ),
          };
        });
      } else if (e.key === "ArrowDown") {
        if (!selectedClass.height || selectedClass.height.disabled) return;
        e.preventDefault();
        setSelectedClass((prev: object) => {
          return {
            ...prev,
            height: new NumberValue(
              "height",
              prev.height.value + delta,
              "Height"
            ),
          };
        });
      }
    }
  };

  const modifyElementPosition = (elementId, x: number, y: number) => {
    let preview = document.getElementById("bgw-root");
    let bounds = preview.getBoundingClientRect();

    let scenes = document.getElementById("bgw-root").children[0];
    let sceneBounds = scenes.getBoundingClientRect();

    const scaleX = 1920 / sceneBounds.width;
    const scaleY = 1080 / sceneBounds.height;

    let yDiff = sceneBounds.top - bounds.top;
    let xDiff = sceneBounds.left - bounds.left;

    let actualX = x - xDiff;
    let actualY = y - yDiff;

    const translatedX = actualX * scaleX;
    const translatedY = actualY * scaleY;

    setSelectedClass((prev: object) => {
      return {
        ...prev,
        posX: new NumberValue("posX", Math.round(translatedX), "Position X"),
        posY: new NumberValue("posY", Math.round(translatedY), "Position Y"),
      };
    });
  };

  const sendJsonMessage = (message) => {
    console.log(JSON.stringify(message));
    const event = new CustomEvent("BGW_MSG", {
      detail: JSON.stringify(message),
    });

    document.dispatchEvent(event);
  };

  const [selectedComponentType, setSelectedComponentType] = useState("");
  const [selectedClass, setSelectedClass] = useState(null);

  const [parsedCodeObjects, setParsedCodeObjects] = useState(null);

  const [selectedComponentId, setSelectedComponentId] = useState("");
  const allComponents = useMap([]);

  const [allFonts, setAllFonts] = useState([]);

  const [treeData, setTreeData] = useState([]);
  const handleDrop = (newTreeData) => {
    setTreeData(newTreeData);
  };

  const checkDrop = (source, targetId, target) => {
    if (targetId === 0) return true;

    let sourceElem = getElementFromId(source.id);
    let targetElem = getElementFromId(target.id);

    if (sourceElem == null || targetElem == null) return false;

    if (!Droppable[targetElem.type]) return true;
    let droppable = Droppable[targetElem.type].whitelist;
    let allowed = false;
    droppable.forEach((type) => {
      if (sourceElem.objectType.includes(type)) {
        allowed = true;
      }
    });

    return allowed;
  };

  const getInstantiableByData = (data: string) => {
    const flat = Object.entries(Components.Instantiable).flatMap((e) =>
      Object.entries(e[1])
    );
    return flat.find((e) => e[1].cls === data)[1];
  };

  const getType = (value: PropertyValue | null) => {
    if (value === null) {
      return "null";
    }

    if (value.propType) {
      return value.propType;
    } else {
      return "null";
    }
  };

  const [outputDirty, setOutputDirty] = useState(0);

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
    console.log(selectedClass);
    if (selectedClass === null) return;
    allComponents.set(selectedClass.id, selectedClass);
    sendOutputElements();

    window.addEventListener("keydown", captureKeyEvent);

    if (currentGuide != null) {
      showElementGuides(currentGuide.id);
    }

    return () => {
      window.removeEventListener("keydown", captureKeyEvent);
    };
  }, [selectedClass, treeData]);

  useEffect(() => {
    if (currentGuide != null && hidePermanentGuides) {
      hideElementGuides();
    } else if (currentGuide != null) {
      showElementGuides(currentGuide.id);
    } else if (!hidePermanentGuides && selectedComponentId != "") {
      showElementGuides(selectedComponentId);
    }
  }, [hidePermanentGuides, fullScreen]);

  useEffect(() => {
    setCompileError(null);
    sendOutputElements();
  }, [outputDirty]);

  const getDistinctInputElement = (elementClass: DataClass, attr: string) => {
    if (attr === "visual") return getVisualInputs(elementClass);
    if (attr.startsWith("vis_")) return getVisualInputs(elementClass, attr);
    if (attr === "front") return getVisualInputs(elementClass, "front");
    if (attr === "back") return getVisualInputs(elementClass, "back");
    if (attr === "font") return getFontInputs(elementClass);

    const res = Object.entries(elementClass).find((p) => p[0] === attr);

    if (attr === "sideCount") {
      return getSideCountInput(res, elementClass);
    }

    if (attr === "column" || attr === "row") {
      return getSpecialInputElement(res, elementClass.id);
    }

    return getInputElement(res);
  };

  const getSideCountInput = (
    entry: [string, PropertyValue],
    elementClass: DiceViewData
  ) => {
    return (
      <div className="flex flex-row w-full items-center justify-between gap-4">
        <Label className="shrink-0 w-[43%] text-white/70" htmlFor={entry[0]}>
          {entry[1].name}
        </Label>
        <Input
          id={entry[0]}
          type="number"
          placeholder={entry[1].value}
          value={entry[1].value}
          onChange={(e) => {
            setSelectedClass((prev: object) => {
              return {
                ...prev,
                [entry[0]]: new NumberValue(
                  entry[1].property,
                  e.target.value,
                  entry[1].name
                ),
              };
            });

            const currentVisuals = Object.keys(elementClass).filter((k) =>
              k.startsWith("vis_")
            );

            if (currentVisuals.length < parseInt(e.target.value)) {
              for (
                let i = currentVisuals.length;
                i < parseInt(e.target.value);
                i++
              ) {
                if (elementClass[`vis_${i}`]) continue;
                setSelectedClass((prev: object) => {
                  return {
                    ...prev,
                    [`vis_${i}`]: new CompoundVisualData(),
                  };
                });
              }
            } else if (currentVisuals.length > parseInt(e.target.value)) {
              for (
                let i = currentVisuals.length - 1;
                i >= parseInt(e.target.value);
                i--
              ) {
                if (elementClass[`vis_${i}`]) {
                  setSelectedClass((prev: object) => {
                    const newData = { ...prev };
                    delete newData[`vis_${i}`];
                    return newData;
                  });
                }
              }
            }

            setSelectedClass((prev: object) => {
              return {
                ...prev,
                currentSide: new ChoiceValue(
                  "currentSide",
                  prev.currentSide.value >= e.target.value
                    ? e.target.value
                    : prev.currentSide.value,
                  Array.from(
                    { length: parseInt(e.target.value) },
                    (_, i) => i + 1
                  ).map((i) => i.toString()),
                  "Current Side"
                ),
              };
            });
          }}
        />
      </div>
    );
  };

  const changeValueOfVisual = (
    child: SingleLayerVisualData,
    attr: string,
    value: any,
    visAttr: string = "visual"
  ) => {
    setSelectedClass((prev: DataClass) => {
      return {
        ...prev,
        [visAttr]: {
          ...prev[visAttr],
          children: prev[visAttr].children.map((c) => {
            if (c === child) {
              return {
                ...c,
                [attr]: value,
              };
            }
            return c;
          }),
        },
      };
    });
  };

  const changeValueOfVisualFont = (
    child: TextVisualData,
    attr: string,
    value: any,
    visAttr: string = "visual"
  ) => {
    setSelectedClass((prev: DataClass) => {
      return {
        ...prev,
        [visAttr]: {
          ...prev[visAttr],
          children: prev[visAttr].children.map((c) => {
            if (c === child) {
              return {
                ...c,
                font: {
                  ...c.font,
                  [attr]: value,
                },
              };
            }
            return c;
          }),
        },
      };
    });
  };

  const changeValueOfInherited = (
    attr: string,
    value: any,
    childAttr: string = "visual"
  ) => {
    setSelectedClass((prev: DataClass) => {
      return {
        ...prev,
        [childAttr]: {
          ...prev[childAttr],
          [attr]: value,
        },
      };
    });
  };

  const getFontInputs = (elementClass: DataClass) => {
    return (
      <div className="grid gap-6 w-full">
        <div className="flex gap-3">
          {getDistinctInheritedInputElement("family", "font")}
        </div>
        <div className="flex gap-3">
          {getDistinctInheritedInputElement("color", "font")}
          {getDistinctInheritedInputElement("size", "font")}
          {getDistinctInheritedInputElement("fontStyle", "font")}
          {getDistinctInheritedInputElement("fontWeight", "font")}
        </div>
      </div>
    );
  };

  const getVisualInputs = (
    elementClass: DataClass,
    visAttr: string = "visual"
  ) => {
    return (
      <>
        <div className="grid gap-3 w-full">
          {elementClass[visAttr].children.map((child) => {
            if (child.type === "ColorVisualData") {
              return (
                <div className="flex gap-3">
                  <ColorPicker
                    className="w-full"
                    color={child.color}
                    setColor={(value) => {
                      changeValueOfVisual(child, "color", value, visAttr);
                    }}
                  >
                    {getDistinctVisualInputElement(
                      child,
                      "transparency",
                      visAttr
                    )}
                  </ColorPicker>
                  <Button
                    variant="secondary"
                    onClick={() => removeVisualElement(child, visAttr)}
                  >
                    <i className="material-symbols-rounded text-lg text-white/90">
                      delete
                    </i>
                  </Button>
                </div>
              );
            } else if (child.type === "ImageVisualData") {
              return (
                <div className="flex gap-3">
                  <ImagePicker
                    className="w-full"
                    image={child.path}
                    setImage={(value) => {
                      if (value.startsWith("http")) {
                        changeValueOfVisual(child, "path", value, visAttr);
                      } else {
                        getOneByKey("name", value).then((image) => {
                          if (image) {
                            changeValueOfVisual(
                              child,
                              "path",
                              image.src,
                              visAttr
                            );
                          } else {
                            alert("Image not found");
                          }
                        });
                      }
                    }}
                  >
                    {getDistinctVisualInputElement(
                      child,
                      "transparency",
                      visAttr
                    )}
                    {getDistinctVisualInputElement(child, "flipped", visAttr)}
                    <div className="flex gap-3">
                      {getDistinctVisualInputElement(child, "width", visAttr)}
                      {getDistinctVisualInputElement(child, "height", visAttr)}
                    </div>
                    <div className="flex gap-3">
                      {getDistinctVisualInputElement(child, "offsetX", visAttr)}
                      {getDistinctVisualInputElement(child, "offsetY", visAttr)}
                    </div>
                  </ImagePicker>
                  <Button
                    variant="secondary"
                    onClick={() => removeVisualElement(child, visAttr)}
                  >
                    <i className="material-symbols-rounded text-lg text-white/90">
                      delete
                    </i>
                  </Button>
                </div>
              );
            } else if (child.type === "TextVisualData") {
              return (
                <div className="flex gap-3">
                  <TextPicker
                    className="w-full"
                    text={child.text}
                    setText={(value) => {
                      changeValueOfVisual(child, "text", value, visAttr);
                    }}
                  >
                    <div className="flex gap-3">
                      {getDistinctVisualInputElement(
                        child,
                        "transparency",
                        visAttr
                      )}
                      {getDistinctVisualInputElement(child, "flipped", visAttr)}
                    </div>
                    <div className="grid gap-3 w-full">
                      <div className="flex gap-3">
                        <div className="flex gap-3 w-[45%]">
                          {getDistinctVisualFontInputElement(
                            child,
                            "family",
                            visAttr
                          )}
                          {getDistinctVisualFontInputElement(
                            child,
                            "color",
                            visAttr
                          )}
                        </div>
                        <div className="flex gap-3 w-[15%]">
                          {getDistinctVisualFontInputElement(
                            child,
                            "size",
                            visAttr
                          )}
                        </div>
                        <div className="flex gap-3 w-[40%]">
                          {getDistinctVisualFontInputElement(
                            child,
                            "fontStyle",
                            visAttr
                          )}
                          {getDistinctVisualFontInputElement(
                            child,
                            "fontWeight",
                            visAttr
                          )}
                        </div>
                      </div>
                    </div>
                    <div className="flex gap-3">
                      {getDistinctVisualInputElement(
                        child,
                        "horizontalAlignment",
                        visAttr
                      )}
                      {getDistinctVisualInputElement(
                        child,
                        "verticalAlignment",
                        visAttr
                      )}
                    </div>
                    <div className="flex gap-3">
                      {getDistinctVisualInputElement(child, "offsetX", visAttr)}
                      {getDistinctVisualInputElement(child, "offsetY", visAttr)}
                    </div>
                  </TextPicker>
                  <Button
                    variant="secondary"
                    onClick={() => removeVisualElement(child, visAttr)}
                  >
                    <i className="material-symbols-rounded text-lg text-white/90">
                      delete
                    </i>
                  </Button>
                </div>
              );
            }
          })}
        </div>
        <div className="flex gap-3 w-full">
          <Button
            variant="secondary"
            size="sm"
            className="w-full"
            onClick={() => {
              setSelectedClass((prev: DataClass) => {
                return {
                  ...prev,
                  [visAttr]: {
                    ...prev[visAttr],
                    children: [
                      ...prev[visAttr].children,
                      new ColorVisualData(),
                    ],
                  },
                };
              });
            }}
          >
            Add Color
          </Button>
          <Button
            variant="secondary"
            size="sm"
            className="w-full"
            onClick={() => {
              setSelectedClass((prev: DataClass) => {
                return {
                  ...prev,
                  [visAttr]: {
                    ...prev[visAttr],
                    children: [
                      ...prev[visAttr].children,
                      new ImageVisualData(),
                    ],
                  },
                };
              });
            }}
          >
            Add Image
          </Button>
          <Button
            variant="secondary"
            size="sm"
            className="w-full"
            onClick={() => {
              setSelectedClass((prev: DataClass) => {
                return {
                  ...prev,
                  [visAttr]: {
                    ...prev[visAttr],
                    children: [...prev[visAttr].children, new TextVisualData()],
                  },
                };
              });
            }}
          >
            Add Text
          </Button>
        </div>
      </>
    );
  };

  const getDistinctVisualInputElement = (
    child: SingleLayerVisualData,
    attr: string,
    visAttr: string = "visual"
  ) => {
    const res = Object.entries(child).find((p) => p[0] === attr);
    return getVisualInputElement(res, child, visAttr);
  };

  const getDistinctVisualFontInputElement = (
    child: TextVisualData,
    attr: string,
    visAttr: string = "visual"
  ) => {
    const res = Object.entries(child["font"]).find((p) => p[0] === attr);
    return getVisualFontInputElement(res, child, visAttr);
  };

  const getDistinctInheritedInputElement = (
    attr: string,
    childAttr: string = "font"
  ) => {
    const res = Object.entries(selectedClass[childAttr]).find(
      (p) => p[0] === attr
    );
    return getInheritedInputElement(res, childAttr);
  };

  const removeVisualElement = (
    child: SingleLayerVisualData,
    visAttr: string
  ) => {
    setSelectedClass((prev: DataClass) => {
      return {
        ...prev,
        [visAttr]: {
          ...prev[visAttr],
          children: prev[visAttr].children.filter((c) => c !== child),
        },
      };
    });
  };

  const getVisualInputElement = (
    entry: [string, PropertyValue],
    child: SingleLayerVisualData,
    visAttr: string = "visual"
  ) => {
    if (entry[0] === "id") return null;

    const type = getType(entry[1]);
    if (type === "null") return null;

    if (type === "number") {
      return (
        <div className="grid gap-3 w-full">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="number"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              changeValueOfVisual(
                child,
                entry[1].property,
                new NumberValue(
                  entry[1].property,
                  e.target.value,
                  entry[1].name
                ),
                visAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "string") {
      return (
        <div className="grid gap-3 w-full">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="text"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              changeValueOfVisual(
                child,
                entry[1].property,
                new StringValue(
                  entry[1].property,
                  e.target.value,
                  entry[1].name
                ),
                visAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "boolean") {
      return (
        <div className="flex justify-between w-full items-center h-8">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Switch
            id={entry[0]}
            defaultChecked={entry[1].value}
            checked={entry[1].value}
            onCheckedChange={(e) => {
              changeValueOfVisual(
                child,
                entry[1].property,
                new BooleanValue(entry[1].property, e, entry[1].name),
                visAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "choice") {
      return (
        <div className="flex gap-3 w-full flex-col">
          <Label className="text-white/70">{entry[1].name}</Label>
          <Select
            value={entry[1].value}
            onValueChange={(value) => {
              changeValueOfVisual(
                child,
                entry[1].property,
                new ChoiceValue(
                  entry[1].property,
                  value,
                  entry[1].possibleValues,
                  entry[1].name
                ),
                visAttr
              );
            }}
          >
            <SelectTrigger
              id={entry[0]}
              className="items-start [&_[data-description]]:hidden"
            >
              <SelectValue placeholder={entry[1].value} />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                {entry[1].possibleValues.map((choice) => {
                  return (
                    <SelectItem value={choice}>
                      <div className="flex items-center gap-5 text-muted-foreground">
                        <div className="grid gap-0.5">
                          <span className="font-medium text-foreground">
                            {choice}
                          </span>
                        </div>
                      </div>
                    </SelectItem>
                  );
                })}
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
      );
    }
  };

  const getVisualFontInputElement = (
    entry: [string, PropertyValue],
    child: TextVisualData,
    visAttr: string = "visual"
  ) => {
    if (entry[0] === "id") return null;

    const type = getType(entry[1]);
    if (type === "null") return null;

    if (type === "number") {
      return (
        <div className="grid gap-3 w-full">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="number"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              changeValueOfVisualFont(
                child,
                entry[1].property,
                new NumberValue(
                  entry[1].property,
                  e.target.value,
                  entry[1].name
                ),
                visAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "string") {
      return (
        <div className="grid gap-3 w-full">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="text"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              changeValueOfVisualFont(
                child,
                entry[1].property,
                new StringValue(
                  entry[1].property,
                  e.target.value,
                  entry[1].name
                ),
                visAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "boolean") {
      return (
        <div className="flex justify-between w-full items-center h-8">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Switch
            id={entry[0]}
            defaultChecked={entry[1].value}
            checked={entry[1].value}
            onCheckedChange={(e) => {
              changeValueOfVisualFont(
                child,
                entry[1].property,
                new BooleanValue(entry[1].property, e, entry[1].name),
                visAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "choice") {
      return (
        <div className="flex gap-3 w-full flex-col">
          <Label className="text-white/70">{entry[1].name}</Label>
          <Select
            value={entry[1].value}
            onValueChange={(value) => {
              changeValueOfVisualFont(
                child,
                entry[1].property,
                new ChoiceValue(
                  entry[1].property,
                  value,
                  entry[1].possibleValues,
                  entry[1].name
                ),
                visAttr
              );
            }}
          >
            <SelectTrigger
              id={entry[0]}
              className="items-start [&_[data-description]]:hidden"
            >
              <SelectValue placeholder={entry[1].value} />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                {entry[1].possibleValues.map((choice) => {
                  return (
                    <SelectItem value={choice}>
                      <div className="flex items-center gap-5 text-muted-foreground">
                        <div className="grid gap-0.5">
                          <span className="font-medium text-foreground">
                            {choice}
                          </span>
                        </div>
                      </div>
                    </SelectItem>
                  );
                })}
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
      );
    } else if (type === "color") {
      return (
        <div className="grid gap-3 w-full">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <SimpleColorPicker
            className="w-full"
            background={entry[1].value}
            setBackground={(value) => {
              changeValueOfVisualFont(
                child,
                entry[1].property,
                new ColorValue(entry[1].property, value, entry[1].name),
                visAttr
              );
            }}
          />
        </div>
      );
    }
  };

  const getInheritedInputElement = (
    entry: [string, PropertyValue],
    childAttr: string = "font"
  ) => {
    if (entry[0] === "id") return null;

    const type = getType(entry[1]);
    if (type === "null") return null;

    if (type === "number") {
      return (
        <div className="grid gap-3 w-full">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="number"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              changeValueOfInherited(
                entry[1].property,
                new NumberValue(
                  entry[1].property,
                  e.target.value,
                  entry[1].name
                ),
                childAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "string") {
      return (
        <div className="grid gap-3 w-full">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="text"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              changeValueOfInherited(
                entry[1].property,
                new StringValue(
                  entry[1].property,
                  e.target.value,
                  entry[1].name
                ),
                childAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "boolean") {
      return (
        <div className="flex justify-between w-full items-center h-8">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Switch
            id={entry[0]}
            defaultChecked={entry[1].value}
            checked={entry[1].value}
            onCheckedChange={(e) => {
              changeValueOfInherited(
                entry[1].property,
                new BooleanValue(entry[1].property, e, entry[1].name),
                childAttr
              );
            }}
          />
        </div>
      );
    } else if (type === "choice") {
      return (
        <div className="flex gap-3 w-full flex-col">
          <Label className="text-white/70">{entry[1].name}</Label>
          <Select
            value={entry[1].value}
            onValueChange={(value) => {
              changeValueOfInherited(
                entry[1].property,
                new ChoiceValue(
                  entry[1].property,
                  value,
                  entry[1].possibleValues,
                  entry[1].name
                ),
                childAttr
              );
            }}
          >
            <SelectTrigger
              id={entry[0]}
              className="items-start [&_[data-description]]:hidden"
            >
              <SelectValue placeholder={entry[1].value} />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                {entry[1].possibleValues.map((choice) => {
                  return (
                    <SelectItem value={choice}>
                      <div className="flex items-center gap-5 text-muted-foreground">
                        <div className="grid gap-0.5">
                          <span className="font-medium text-foreground">
                            {choice}
                          </span>
                        </div>
                      </div>
                    </SelectItem>
                  );
                })}
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
      );
    } else if (type === "color") {
      return (
        <div className="grid gap-3 w-full">
          <Label className="text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <SimpleColorPicker
            className="w-full"
            background={entry[1].value}
            setBackground={(value) => {
              changeValueOfInherited(
                entry[1].property,
                new ColorValue(entry[1].property, value, entry[1].name),
                childAttr
              );
            }}
          />
        </div>
      );
    }
  };

  const getSpecialInputElement = (
    entry: [string, PropertyValue],
    id: string
  ) => {
    if (entry[0] === "id") return null;
    const type = getType(entry[1]);
    if (type === "null") return null;
    if (entry[1].disabled) return null;

    if (type === "number") {
      return (
        <div className="flex flex-row w-full items-center justify-between gap-4">
          <Label className="shrink-0 w-[43%] text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="number"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              allComponents.set(id, {
                ...allComponents.get(id),
                [entry[0]]: new NumberValue(
                  entry[1].property,
                  e.target.value,
                  entry[1].name
                ),
              });
              setOutputDirty(outputDirty + 1);
            }}
          />
        </div>
      );
    }
  };

  const getInputElement = (entry: [string, PropertyValue]) => {
    if (entry[0] === "id") return null;
    const type = getType(entry[1]);
    if (type === "null") return null;
    if (entry[1].disabled) return null;

    if (type === "number") {
      return (
        <div className="flex flex-row w-full items-center justify-between gap-4">
          <Label className="shrink-0 w-[43%] text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="number"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              setSelectedClass((prev: object) => {
                return {
                  ...prev,
                  [entry[0]]: new NumberValue(
                    entry[1].property,
                    e.target.value,
                    entry[1].name
                  ),
                };
              });
            }}
          />
        </div>
      );
    } else if (type === "string") {
      return (
        <div className="flex flex-row w-full items-center justify-between gap-4">
          <Label className="shrink-0 w-[43%] text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Input
            id={entry[0]}
            type="text"
            placeholder={entry[1].value}
            value={entry[1].value}
            onChange={(e) => {
              setSelectedClass((prev: object) => {
                return {
                  ...prev,
                  [entry[0]]: new StringValue(
                    entry[1].property,
                    e.target.value,
                    entry[1].name
                  ),
                };
              });
            }}
          />
        </div>
      );
    } else if (type === "boolean") {
      return (
        <div className="flex flex-row w-full items-center justify-between gap-4 h-8">
          <Label className="shrink-0 w-[43%] text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <Switch
            id={entry[0]}
            defaultChecked={entry[1].value}
            checked={entry[1].value}
            onCheckedChange={(e) => {
              setSelectedClass((prev: object) => {
                return {
                  ...prev,
                  [entry[0]]: new BooleanValue(
                    entry[1].property,
                    e,
                    entry[1].name
                  ),
                };
              });
            }}
          />
        </div>
      );
    } else if (type === "color") {
      return (
        <div className="flex flex-row w-full items-center justify-between gap-4">
          <Label className="shrink-0 w-[43%] text-white/70" htmlFor={entry[0]}>
            {entry[1].name}
          </Label>
          <SimpleColorPicker
            className="w-full"
            background={entry[1].value}
            setBackground={(value) => {
              setSelectedClass((prev: object) => {
                return {
                  ...prev,
                  [entry[0]]: new ColorValue(
                    entry[1].property,
                    value,
                    entry[1].name
                  ),
                };
              });
            }}
          />
        </div>
      );
    } else if (type === "choice") {
      return (
        <div className="flex flex-row w-full items-center justify-between gap-4">
          <Label className="shrink-0 w-[43%] text-white/70">
            {entry[1].name}
          </Label>
          <Select
            value={entry[1].value}
            onValueChange={(value) => {
              setSelectedClass((prev: object) => {
                return {
                  ...prev,
                  [entry[0]]: new ChoiceValue(
                    entry[1].property,
                    value,
                    entry[1].possibleValues,
                    entry[1].name
                  ),
                };
              });
            }}
          >
            <SelectTrigger
              id={entry[0]}
              className="items-start [&_[data-description]]:hidden"
            >
              <SelectValue placeholder={entry[1].value} />
            </SelectTrigger>
            <SelectContent>
              <SelectGroup>
                {entry[1].possibleValues.map((choice) => {
                  return (
                    <SelectItem value={choice}>
                      <div className="flex items-center gap-5 text-muted-foreground">
                        <div className="grid gap-0.5">
                          <span className="font-medium text-foreground">
                            {choice}
                          </span>
                        </div>
                      </div>
                    </SelectItem>
                  );
                })}
              </SelectGroup>
            </SelectContent>
          </Select>
        </div>
      );
    }
  };

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

  const renderError = (error: string) => {
    return (
      <TooltipProvider>
        <Tooltip>
          <TooltipTrigger asChild>
            <i
              className={`material-symbols-rounded text-xl text-red-500 right-4 absolute`}
            >
              emergency_home
            </i>
          </TooltipTrigger>
          <TooltipContent>
            <p>{error}</p>
          </TooltipContent>
        </Tooltip>
      </TooltipProvider>
    );
  };

  const renderWarning = (warning: string) => {
    return (
      <TooltipProvider>
        <Tooltip>
          <TooltipTrigger asChild>
            <i
              className={`material-symbols-rounded text-xl text-yellow-500 right-4 absolute`}
            >
              warning
            </i>
          </TooltipTrigger>
          <TooltipContent>
            <p>{warning}</p>
          </TooltipContent>
        </Tooltip>
      </TooltipProvider>
    );
  };

  const renderInfo = (info: string) => {
    return (
      <TooltipProvider>
        <Tooltip>
          <TooltipTrigger asChild>
            <i
              className={`material-symbols-rounded text-xl text-white/70 right-4 absolute`}
            >
              info
            </i>
          </TooltipTrigger>
          <TooltipContent>
            <p>{info}</p>
          </TooltipContent>
        </Tooltip>
      </TooltipProvider>
    );
  };

  const [exportOpened, setExportOpened] = useState(false);

  const renderTreeNode = (
    node,
    depth: number,
    isOpen: boolean,
    onToggle: (e) => void
  ) => {
    let elem = allComponents.get(node.id);
    if (elem == null) return null;

    checkPossibleErrors(node);

    return (
      <ContextMenu>
        <ContextMenuTrigger asChild>
          {getTreeNode(node, depth, isOpen, onToggle, elem)}
        </ContextMenuTrigger>
        <ContextMenuContent className="w-48">
          {/*<ContextMenuLabel className="mb-2 ml-2">{elem.name.value.trim() == "" ? getInstantiableByData(elem.type).name : elem.name.value}</ContextMenuLabel>*/}
          <ContextMenuItem
            className="gap-3 cursor-pointer"
            onClick={() => {
              setSelectedClass(elem);
              setSelectedComponentId(elem.id);
              duplicateComponent(elem.id);
            }}
          >
            <i className="material-symbols-rounded text-lg text-muted-foreground ml-2">
              tab_inactive
            </i>
            <p className="text-muted-foreground font-medium">Duplicate</p>
          </ContextMenuItem>
          <ContextMenuItem
            className="gap-3 cursor-pointer"
            onClick={() => {
              setSelectedClass(elem);
              setSelectedComponentId(elem.id);
              setExportOpened(true);
            }}
          >
            <i className="material-symbols-rounded text-lg text-muted-foreground ml-2">
              code
            </i>
            <p className="text-muted-foreground font-medium">Export</p>
          </ContextMenuItem>
          <ContextMenuSeparator />
          <ContextMenuItem
            className="text-red-500 hover:!bg-red-500/20 gap-3 cursor-pointer"
            onClick={() => {
              deleteComponent(elem.id);
            }}
          >
            <i className="material-symbols-rounded text-lg text-red-500 ml-2">
              delete_forever
            </i>
            <p className="text-red-500 font-medium">Delete</p>
          </ContextMenuItem>
        </ContextMenuContent>
      </ContextMenu>
    );
  };

  const getTreeNode = (
    node,
    depth: number,
    isOpen: boolean,
    onToggle: (e) => void,
    elem
  ) => {
    let selected = elem.id === selectedComponentId;
    let opened = isOpen;
    let data = getInstantiableByData(elem.type);

    /* return (
            <Toggle pressed={selected}
                    defaultPressed={selected}
                    onMouseEnter={() => {showElementGuides(elem.id, data.color)}}
                    onMouseLeave={() => {hideElementGuides()}}
                    className={`flex items-center gap-3 mt-1 mb-1 justify-start cursor-pointer relative main__toggle ${selected ? `!bg-[${data.color}]/20` : ""} hover:!bg-[${data.color}]/20`}
                    style={{
                        marginLeft: depth * 30,
                        width: `calc(100% - ${depth * 30}px)`,
                        maxWidth: `calc(100% - ${depth * 30}px)`
                    }}
                    onPressedChange={() => {
                        if(selected) {
                            setSelectedComponentId(null)
                            setSelectedClass(null)
                            return
                        }
                        setSelectedComponentId(elem.id)
                        setSelectedClass(elem)
                    }}>
                {node.droppable && (
                    <i className={`material-symbols-rounded text-lg`}
                       onClick={(event) => { event.preventDefault(); event.stopPropagation(); onToggle(event) }}>{isOpen ? "expand_more" : "chevron_right"}</i>
                )}
                <i className={`material-symbols-rounded main__toggle__icon text-xl text-[${data.color}]`}>{data.icon}</i>
                <span
                    className={`font-medium ${selected ? `text-[${getInstantiableByData(elem.type).color}]` : "text-white/70"} overflow-hidden text-ellipsis w-7/12 whitespace-nowrap text-left`}>{elem.name.value.trim() == "" ? getInstantiableByData(elem.type).name : elem.name.value}</span>
                { node.info && node.info.type === "error" ? renderError(node.info.message) : node.info && node.info.type === "warning" ? renderWarning(node.info.message) : node.info && node.info.type === "info" ? renderInfo(node.info.message) : null}
            </Toggle>
        ) */

    return (
      <Toggle
        pressed={selected}
        defaultPressed={selected}
        onMouseEnter={() => {
          if (hidePermanentGuides) showElementGuides(elem.id, data.color);
        }}
        onMouseLeave={() => {
          if (hidePermanentGuides) hideElementGuides();
        }}
        className={`flex items-center gap-3 mt-1 mb-1 justify-start cursor-pointer relative main__toggle`}
        style={{
          marginLeft: depth * 30,
          width: `calc(100% - ${depth * 30}px)`,
          maxWidth: `calc(100% - ${depth * 30}px)`,
        }}
        title={elem.name.value.trim() == "" ? data.name : elem.name.value}
        onPressedChange={() => {
          if (selected) {
            setSelectedComponentId(null);
            setSelectedClass(null);
            hideElementGuides();
            return;
          }
          setSelectedComponentId(elem.id);
          setSelectedClass(elem);
          showElementGuides(elem.id);
        }}
      >
        {node.droppable && (
          <i
            className={`material-symbols-rounded text-lg`}
            onClick={(event) => {
              event.preventDefault();
              event.stopPropagation();
              onToggle(event);
            }}
          >
            {isOpen ? "expand_more" : "chevron_right"}
          </i>
        )}
        <i
          className={`material-symbols-rounded main__toggle__icon text-xl text-[${data.color}]`}
        >
          {data.icon}
        </i>
        <span
          className={`font-medium ${
            selected ? `text-white` : "text-white/70"
          } overflow-hidden text-ellipsis w-7/12 whitespace-nowrap text-left`}
        >
          {elem.name.value.trim() == ""
            ? getInstantiableByData(elem.type).name
            : elem.name.value}
        </span>
        {node.info && node.info.type === "error"
          ? renderError(node.info.message)
          : node.info && node.info.type === "warning"
          ? renderWarning(node.info.message)
          : node.info && node.info.type === "info"
          ? renderInfo(node.info.message)
          : null}
      </Toggle>
    );
  };

  const [currentGuide, setCurrentGuide] = useState<Guides | null>(null);

  const showElementGuides = (id: string, color: string = "#ffffff") => {
    let guide: Guides = {
      id: id,
      color: color,
      x: -9999,
      y: -9999,
      width: 0,
      height: 0,
    };

    let guideElement = document.getElementById(id);
    if (guideElement == null) {
      setCurrentGuide(guide);
      return;
    }

    let parentElement = document.getElementById("bgw-preview");
    if (parentElement == null) {
      setCurrentGuide(guide);
      return;
    }

    let parentRect = parentElement.getBoundingClientRect();
    let rect = guideElement.getBoundingClientRect();

    guide = {
      id: id,
      color: color,
      x: rect.x - parentRect.x,
      y: rect.y - parentRect.y,
      width: rect.width,
      height: rect.height,
    };

    setCurrentGuide(guide);
  };

  const setGuidePosition = (guidePos: any) => {
    if (currentGuide == null) return;
    setCurrentGuide({
      ...currentGuide,
      x: guidePos.x,
      y: guidePos.y,
    });
  };

  const hideElementGuides = () => {
    setCurrentGuide(null);
  };

  const duplicateComponent = (id: string, parentId: string | number = 0) => {
    const elem = allComponents.get(id);
    const node = treeData.find((n) => n.id === id);
    const children = getAllChildren(id);

    const newId = generateId();
    const newElem = { ...elem, id: newId };
    newElem.name = new StringValue(
      "name",
      `${
        elem.name.value.trim() == ""
          ? getInstantiableByData(elem.type).name
          : elem.name.value
      } (Copy)`,
      "Name"
    );
    allComponents.set(newId, newElem);

    if (children.length > 0) {
      children.forEach((child) => {
        duplicateComponent(child.id, newId);
      });
    }

    setSelectedClass(newElem);
    treeData.push({
      id: newElem.id,
      parent: parentId,
      droppable:
        newElem.objectType.includes("TableViewData") ||
        newElem.objectType.includes("LayoutViewData") ||
        newElem.objectType.includes("ToggleGroupData") ||
        newElem.objectType.includes("GameComponentContainerData") ||
        newElem.objectType.includes("GridElementData") ||
        newElem.objectType.includes("CameraPaneData") ||
        newElem.objectType.includes("HexagonGridElementData"),
      text: newElem.name.value,
    });

    setSelectedComponentId(newElem.id);
    showElementGuides(newElem.id);
    setOutputDirty(outputDirty + 1);
  };

  const deleteComponent = (id: string) => {
    const children = getAllChildren(id);

    if (children.length > 0) {
      children.forEach((child) => {
        deleteComponent(child.id);
      });
    }

    allComponents.delete(id);
    setTreeData(treeData.filter((n) => n.id !== id));

    setSelectedClass(null);
    setSelectedComponentId(null);
    setOutputDirty(outputDirty + 1);
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

  const getOutputElementsFromTree = (isExport: boolean = false) => {
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
      } else if (
        elem.type === "HexagonViewData" ||
        elem.type === "HexagonGridData"
      ) {
        allComponentsCopy[elem.id].orientation = allComponentsCopy[
          elem.id
        ].orientation
          .replace(" ", "_")
          .toLowerCase();
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

    if (!isExport) {
      setProjectError(null);
      setProjectWarning(null);
    }

    treeData.forEach((node) => {
      let possErrors = checkPossibleErrors(node);
      if (!isExport) {
        if (possErrors && possErrors.type === "error") {
          setProjectError(possErrors.message);
        } else if (possErrors && possErrors.type === "warning") {
          setProjectWarning(possErrors.message);
        }
      }

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
        parent.hexagon.orientation = nextParent.orientation;
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
    if (allComponents.size === 0) return;

    // const components : DataClass[] = []
    // allComponents.forEach((value, key) => {
    //     components.push(value)
    // })

    const components = getOutputElementsFromTree();

    const comps = JSON.parse(JSON.stringify(components));

    sendJsonMessage({
      container: "bgw-root",
      props: {
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
      },
    });
  };

  const getAddComponentSelect = () => {
    return (
      <Select
        onValueChange={(value) => {
          setSelectedComponentType(value);
        }}
      >
        <SelectTrigger
          id="model"
          className="items-start [&_[data-description]]:hidden"
        >
          <SelectValue placeholder="Select a component" />
        </SelectTrigger>
        <SelectContent>
          <SelectGroup>
            <SelectLabel className="text-muted-foreground">
              UIComponents
            </SelectLabel>
            {Object.entries(Components.Instantiable.UIElements).map((entry) => {
              return (
                <SelectItem
                  value={entry[1]["cls"]}
                  disabled={entry[1].disabled}
                >
                  <div className="flex items-center gap-5 text-muted-foreground">
                    <i
                      className={`material-symbols-rounded text-xl text-[${entry[1].color}]`}
                    >
                      {entry[1].icon}
                    </i>
                    <div className="grid gap-0.5">
                      <span className="font-medium text-foreground">
                        {entry[0]}
                      </span>
                      <p className="text-xs" data-description>
                        {entry[1].description}
                      </p>
                    </div>
                    {entry[1].disabled && (
                      <i className="material-symbols-rounded text-xl absolute right-5">
                        emergency_home
                      </i>
                    )}
                  </div>
                </SelectItem>
              );
            })}
          </SelectGroup>
          <div className="pt-2 mb-2"></div>
          <SelectGroup>
            <SelectLabel className="text-muted-foreground">
              GameComponents
            </SelectLabel>
            {Object.entries(Components.Instantiable.GameElements).map(
              (entry) => {
                return (
                  <SelectItem
                    value={entry[1]["cls"]}
                    disabled={entry[1].disabled}
                  >
                    <div className="flex items-center gap-5 text-muted-foreground">
                      <i
                        className={`material-symbols-rounded text-xl text-[${entry[1].color}]`}
                      >
                        {entry[1].icon}
                      </i>
                      <div className="grid gap-0.5">
                        <span className="font-medium text-foreground">
                          {entry[0]}
                        </span>
                        <p className="text-xs" data-description>
                          {entry[1].description}
                        </p>
                      </div>
                      {entry[1].disabled && (
                        <i className="material-symbols-rounded text-xl absolute right-5">
                          emergency_home
                        </i>
                      )}
                    </div>
                  </SelectItem>
                );
              }
            )}
          </SelectGroup>
          <div className="pt-2 mb-2"></div>
          <SelectGroup>
            <SelectLabel className="text-muted-foreground">Layouts</SelectLabel>
            {Object.entries(Components.Instantiable.LayoutElements).map(
              (entry) => {
                return (
                  <SelectItem
                    value={entry[1]["cls"]}
                    disabled={entry[1].disabled}
                  >
                    <div className="flex items-center gap-5 text-muted-foreground">
                      <i
                        className={`material-symbols-rounded text-xl text-[${entry[1].color}]`}
                      >
                        {entry[1].icon}
                      </i>
                      <div className="grid gap-0.5">
                        <span className="font-medium text-foreground">
                          {entry[0]}
                        </span>
                        <p className="text-xs" data-description>
                          {entry[1].description}
                        </p>
                      </div>
                      {entry[1].disabled && (
                        <i className="material-symbols-rounded text-xl absolute right-5">
                          emergency_home
                        </i>
                      )}
                    </div>
                  </SelectItem>
                );
              }
            )}
          </SelectGroup>
          <div className="pt-2 mb-2"></div>
          <SelectGroup>
            <SelectLabel className="text-muted-foreground">
              Container
            </SelectLabel>
            {Object.entries(Components.Instantiable.ContainerElements).map(
              (entry) => {
                return (
                  <SelectItem
                    value={entry[1]["cls"]}
                    disabled={entry[1].disabled}
                  >
                    <div className="flex items-center gap-5 text-muted-foreground">
                      <i
                        className={`material-symbols-rounded text-xl text-[${entry[1].color}]`}
                      >
                        {entry[1].icon}
                      </i>
                      <div className="grid gap-0.5">
                        <span className="font-medium text-foreground">
                          {entry[0]}
                        </span>
                        <p className="text-xs" data-description>
                          {entry[1].description}
                        </p>
                      </div>
                      {entry[1].disabled && (
                        <i className="material-symbols-rounded text-xl absolute right-5">
                          emergency_home
                        </i>
                      )}
                    </div>
                  </SelectItem>
                );
              }
            )}
          </SelectGroup>
          <div className="pt-2 mb-2"></div>
          <SelectGroup>
            <SelectLabel className="text-muted-foreground">Special</SelectLabel>
            {Object.entries(Components.Instantiable.SpecialElements).map(
              (entry) => {
                return (
                  <SelectItem
                    value={entry[1]["cls"]}
                    disabled={entry[1].disabled}
                  >
                    <div className="flex items-center gap-5 text-muted-foreground">
                      <i
                        className={`material-symbols-rounded text-xl text-[${entry[1].color}]`}
                      >
                        {entry[1].icon}
                      </i>
                      <div className="grid gap-0.5">
                        <span className="font-medium text-foreground">
                          {entry[0]}
                        </span>
                        <p className="text-xs" data-description>
                          {entry[1].description}
                        </p>
                      </div>
                      {entry[1].disabled && (
                        <i className="material-symbols-rounded text-xl absolute right-5">
                          emergency_home
                        </i>
                      )}
                    </div>
                  </SelectItem>
                );
              }
            )}
          </SelectGroup>
        </SelectContent>
      </Select>
    );
  };

  const getPropDataInputs = (elementClass: DataClass) => {
    return (
      <>
        {getNameInputs(elementClass)}
        {elementClass.type === "TableColumnData"
          ? getFontsInputs(elementClass)
          : null}
        {getRestSettingsInputs(elementClass)}
      </>
    );
  };

  const getComponentViewDataInputs = (elementClass: DataClass) => {
    return (
      <>
        {getNameInputs(elementClass)}
        {getPositionInputs(elementClass)}
        {getSizeInputs(elementClass)}
        {getVisualsInputs(elementClass)}
        {advancedMode && getDisplayInputs(elementClass)}
        {advancedMode && getTransformInputs(elementClass)}
        {elementClass.font &&
        !elementClass.objectType.includes("StructuredDataViewData") &&
        !elementClass.type === "ColorPickerData"
          ? getFontsInputs(elementClass)
          : null}
        {getRestSettingsInputs(elementClass)}
      </>
    );
  };

  const getNameInputs = (elementClass: DataClass) => {
    return (
      <SettingsField
        title={getInstantiableByData(elementClass.type).name}
        icon={getInstantiableByData(elementClass.type).icon}
      >
        {getDistinctInputElement(elementClass, "name")}
      </SettingsField>
    );
  };

  const tryGetParent = (elementClass: DataClass) => {
    if (elementClass === null) return null;
    const ownNode = treeData.find((node) => node.id === elementClass.id);
    if (ownNode.parent === 0) return null;
    const parentNode = treeData.find((node) => node.id === ownNode.parent);
    return getElementFromId(parentNode.id);
  };

  const getBoundsInputs = (elementClass: DataClass) => {
    if (elementClass === null) return null;
    const parent = tryGetParent(elementClass);

    if (
      elementClass.type === "HexagonViewData" &&
      parent &&
      parent.type === "HexagonGridElementData"
    ) {
      return (
        <SettingsField title={`Bounds`}>
          <div className="flex gap-3">
            {getDistinctInputElement(parent, "row")}
            {getDistinctInputElement(parent, "column")}
          </div>
          <div className="flex gap-3">
            {getDistinctInputElement(elementClass, "size")}
          </div>
        </SettingsField>
      );
    } else if (
      elementClass.objectType.includes("ComponentViewData") &&
      parent &&
      parent.type === "GridElementData"
    ) {
      return (
        <SettingsField title={`Bounds`}>
          <div className="flex gap-3">
            {getDistinctInputElement(parent, "row")}
            {getDistinctInputElement(parent, "column")}
          </div>
          <div className="flex gap-3">
            {getDistinctInputElement(elementClass, "width")}
            {getDistinctInputElement(elementClass, "height")}
          </div>
        </SettingsField>
      );
    }

    return (
      <SettingsField title={`Bounds`}>
        <div className="flex gap-3">
          {getDistinctInputElement(elementClass, "posX")}
          {getDistinctInputElement(elementClass, "posY")}
        </div>
        <div className="flex gap-3">
          {elementClass.type === "HexagonViewData" ? (
            getDistinctInputElement(elementClass, "size")
          ) : (
            <>
              {getDistinctInputElement(elementClass, "width")}
              {getDistinctInputElement(elementClass, "height")}
            </>
          )}
        </div>
      </SettingsField>
    );
  };

  const getPositionInputs = (elementClass: DataClass) => {
    if (elementClass === null) return null;
    const parent = tryGetParent(elementClass);

    if (
      elementClass.type === "HexagonViewData" &&
      parent &&
      parent.type === "HexagonGridElementData"
    ) {
      return (
        <SettingsField title={`Position`}>
          {getDistinctInputElement(parent, "row")}
          {getDistinctInputElement(parent, "column")}
        </SettingsField>
      );
    } else if (
      elementClass.objectType.includes("ComponentViewData") &&
      parent &&
      parent.type === "GridElementData"
    ) {
      return (
        <SettingsField title={`Position`}>
          {getDistinctInputElement(parent, "row")}
          {getDistinctInputElement(parent, "column")}
        </SettingsField>
      );
    }

    return (
      <SettingsField title={`Position`}>
        {getDistinctInputElement(elementClass, "posX")}
        {getDistinctInputElement(elementClass, "posY")}
      </SettingsField>
    );
  };

  const getSizeInputs = (elementClass: DataClass) => {
    if (elementClass === null) return null;
    const parent = tryGetParent(elementClass);

    if (
      elementClass.type === "HexagonViewData" &&
      parent &&
      parent.type === "HexagonGridElementData"
    ) {
      return (
        <SettingsField title={`Size`}>
          {getDistinctInputElement(elementClass, "size")}
        </SettingsField>
      );
    } else if (
      elementClass.objectType.includes("ComponentViewData") &&
      parent &&
      parent.type === "GridElementData"
    ) {
      return (
        <SettingsField title={`Size`}>
          {getDistinctInputElement(elementClass, "width")}
          {getDistinctInputElement(elementClass, "height")}
        </SettingsField>
      );
    }

    return (
      <SettingsField title={`Size`}>
        {elementClass.type === "HexagonViewData" ? (
          getDistinctInputElement(elementClass, "size")
        ) : (
          <>
            {getDistinctInputElement(elementClass, "width")}
            {getDistinctInputElement(elementClass, "height")}
          </>
        )}
      </SettingsField>
    );
  };

  const getVisualsInputs = (elementClass: DataClass) => {
    if (elementClass.type === "CardViewData") {
      return (
        <>
          <SettingsField title={`Visuals Front`}>
            {getDistinctInputElement(elementClass, "front")}
          </SettingsField>
          <SettingsField title={`Visuals Back`}>
            {getDistinctInputElement(elementClass, "back")}
          </SettingsField>
        </>
      );
    } else if (elementClass.type === "DiceViewData") {
      return Object.keys(elementClass)
        .filter((key) => key.startsWith("vis_"))
        .map((key, index) => {
          return (
            <SettingsField title={`Visuals Side ${index + 1}`}>
              {getDistinctInputElement(elementClass, key)}
            </SettingsField>
          );
        });
    } else {
      return (
        <SettingsField title="Visuals">
          {getDistinctInputElement(elementClass, "visual")}
        </SettingsField>
      );
    }
  };

  const getDisplayInputs = (elementClass: DataClass) => {
    return (
      <SettingsField title="Display">
        {getDistinctInputElement(elementClass, "opacity")}
        {/* getDistinctInputElement(elementClass, "zIndex") */}
        {getDistinctInputElement(elementClass, "isVisible")}
        {getDistinctInputElement(elementClass, "isDisabled")}
        {/* getDistinctInputElement(elementClass, "isFocusable") */}
      </SettingsField>
    );
  };

  const getTransformInputs = (elementClass: DataClass) => {
    return (
      <SettingsField title="Transform">
        {getDistinctInputElement(elementClass, "scaleX")}
        {getDistinctInputElement(elementClass, "scaleY")}
        {getDistinctInputElement(elementClass, "rotation")}
      </SettingsField>
    );
  };

  const getFontsInputs = (elementClass: DataClass) => {
    return (
      <SettingsField title="Font">
        {getDistinctInputElement(elementClass, "font")}
      </SettingsField>
    );
  };

  const getRestSettingsInputs = (elementClass: DataClass) => {
    return (
      <SettingsField title={`Settings`}>
        {Object.entries(elementClass).map((entry) => {
          if (entry[0] === "width" && elementClass.type === "TableColumnData")
            return getInputElement(entry);
          if (
            [
              "posX",
              "posY",
              "width",
              "height",
              "visual",
              "front",
              "back",
              "currentVisual",
              "opacity",
              "isVisible",
              "isDisabled",
              "isFocusable",
              "name",
              "id",
              "zIndex",
              "scaleX",
              "scaleY",
              "rotation",
              "size",
              "component",
              "group",
              "items",
            ].includes(entry[0])
          )
            return null;
          else if (entry[0] === "sideCount")
            return getDistinctInputElement(elementClass, "sideCount");
          return getInputElement(entry);
        })}
      </SettingsField>
    );
  };

  const getDataInputs = (elementClass: DataClass | null) => {
    if (elementClass === null) return null;

    if (elementClass.objectType.includes("ComponentViewData")) {
      return getComponentViewDataInputs(elementClass);
    } else {
      return getPropDataInputs(elementClass);
    }

    return null;
  };

  const [draggingGuide, setDraggingGuide] = useState(false);
  const [draggingBounds, setDraggingBounds] = useState({
    x: 0,
    y: 0,
  });
  const [draggingGuidesStart, setDraggingGuidesStart] = useState({
    x: 0,
    y: 0,
  });

  return (
    <>
      {loading ? (
        <div className="fixed w-full h-full bg-muted left-0 top-0 right-0 bottom-0 z-[900000] flex items-center justify-center">
          <img src={"bgw_animated.svg"} className="w-[150px] mt-[136px]" />
        </div>
      ) : null}
      <div className="grid h-screen w-full">
        <div className="flex flex-col">
          <header className="sticky top-0 z-10 flex h-[57px] items-center gap-1 border-b bg-background px-4 justify-between">
            <h1 className="text-xl font-semibold">BGW Playground</h1>
            <Drawer>
              <DrawerTrigger asChild>
                <Button variant="secondary" size="icon" className="md:hidden">
                  <i className="material-symbols-rounded text-xl">settings</i>
                  <span className="sr-only">Settings</span>
                </Button>
              </DrawerTrigger>
            </Drawer>
            <div className="flex gap-3">
              <Toggle
                variant={"default"}
                className="w-fit shrink-0"
                size={"sm"}
                onPressedChange={() => {
                  setHidePermanentGuides(!hidePermanentGuides);
                }}
                defaultPressed={hidePermanentGuides}
              >
                Hide Permanent Guides
              </Toggle>
              <Toggle
                variant={"default"}
                className="w-fit shrink-0"
                size={"sm"}
                onPressedChange={() => {
                  setAdvancedMode(!advancedMode);
                }}
              >
                Expert Mode
              </Toggle>
              <Dialog>
                <DialogTrigger className="w-full" asChild>
                  <Button variant="secondary" size="sm">
                    Add Component
                  </Button>
                </DialogTrigger>
                <DialogContent>
                  <DialogHeader>
                    <DialogTitle>Add Component</DialogTitle>
                    <DialogDescription>
                      This action will add an element to the scene.
                    </DialogDescription>
                  </DialogHeader>
                  <div className="grid gap-3 component__select">
                    {getAddComponentSelect()}
                  </div>
                  <DialogFooter>
                    <DialogClose asChild>
                      <Button
                        variant="secondary"
                        onClick={() => {
                          let newObj = new Components.DataClasses[
                            selectedComponentType
                          ]();
                          setSelectedClass(newObj);
                          treeData.push({
                            id: newObj.id,
                            parent: 0,
                            droppable:
                              newObj.objectType.includes("TableViewData") ||
                              newObj.objectType.includes("LayoutViewData") ||
                              newObj.objectType.includes("ToggleGroupData") ||
                              newObj.objectType.includes(
                                "GameComponentContainerData"
                              ) ||
                              newObj.objectType.includes("GridElementData") ||
                              newObj.objectType.includes("CameraPaneData") ||
                              newObj.objectType.includes(
                                "HexagonGridElementData"
                              ),
                            text: newObj.name.value,
                          });
                          setSelectedComponentId(newObj.id);
                          showElementGuides(newObj.id);
                        }}
                      >
                        Add
                      </Button>
                    </DialogClose>
                  </DialogFooter>
                </DialogContent>
              </Dialog>
              <Dialog
                open={exportOpened}
                onOpenChange={() => {
                  setExportOpened(false);
                }}
              >
                <DialogContent className="max-w-[800px]" forceMount={true}>
                  <DialogHeader>
                    <DialogTitle>Code Export</DialogTitle>
                    <DialogDescription>
                      This action will export the selected component as code.
                    </DialogDescription>
                  </DialogHeader>
                  <div className="grid gap-3 component__select">
                    <Playground
                      code={
                        exportOpened
                          ? exportComponent(
                              null,
                              getOutputElementsFromTree(true),
                              selectedComponentId
                            )
                          : ""
                      }
                      open={exportOpened}
                    />
                  </div>
                </DialogContent>
              </Dialog>
              <Dialog>
                <DialogTrigger className="w-full" asChild>
                  <Button variant="secondary" size="sm">
                    Add Code
                  </Button>
                </DialogTrigger>
                <DialogContent className="max-w-[800px]">
                  <DialogHeader>
                    <DialogTitle>Add Code</DialogTitle>
                    <DialogDescription>
                      This action will add an element to the scene.
                    </DialogDescription>
                  </DialogHeader>
                  <div className="grid gap-3 component__select">
                    <Parser
                      onCodeChange={(result) => {
                        setParsedCodeObjects(result);
                      }}
                    />
                  </div>
                  <DialogFooter>
                    <DialogClose asChild>
                      <Button
                        variant="secondary"
                        onClick={() => {
                          for (let newObj of parsedCodeObjects) {
                            allComponents.set(newObj.id, newObj);
                            treeData.push({
                              id: newObj.id,
                              parent: 0,
                              droppable:
                                newObj.objectType.includes("TableViewData") ||
                                newObj.objectType.includes("LayoutViewData") ||
                                newObj.objectType.includes("ToggleGroupData") ||
                                newObj.objectType.includes(
                                  "GameComponentContainerData"
                                ) ||
                                newObj.objectType.includes("GridElementData") ||
                                newObj.objectType.includes("CameraPaneData") ||
                                newObj.objectType.includes(
                                  "HexagonGridElementData"
                                ),
                              text: newObj.name.value,
                            });
                            setSelectedClass(newObj);
                            setSelectedComponentId(newObj.id);
                            showElementGuides(newObj.id);
                          }
                        }}
                      >
                        Add
                      </Button>
                    </DialogClose>
                  </DialogFooter>
                </DialogContent>
              </Dialog>
              <Dialog>
                <DialogTrigger className="w-full" asChild>
                  <Button
                    variant="secondary"
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
                variant="secondary"
                size="sm"
                className="ml-auto gap-3 text-sm"
              >
                <i className="material-symbols-rounded text-lg">publish</i>
                Share
              </Button>
            </div>
          </header>
          <main className="flex items-center flex-1 h-full max-h-[calc(100vh-57px)]">
            <div
              className="relative w-[17.5%] max-w-[385px] shrink-0 max-h-[calc(100vh-57px)] h-full flex flex-col gap-3"
              x-chunk="dashboard-03-chunk-0"
            >
              <fieldset className="h-full max-h-full grid gap-3 rounded-lg pl-4 pr-4 overflow-hidden">
                <ScrollArea className="component__scroll h-full max-h-full">
                  <DndProvider
                    backend={MultiBackend}
                    options={getBackendOptions()}
                  >
                    <div>
                      <Tree
                        tree={treeData}
                        rootId={0}
                        render={(node, { depth, isOpen, onToggle }) => {
                          return renderTreeNode(node, depth, isOpen, onToggle);
                        }}
                        onDrop={handleDrop}
                        sort={false}
                        insertDroppableFirst={false}
                        canDrop={(
                          tree,
                          { dragSource, dropTargetId, dropTarget }
                        ) => {
                          let valid = checkDrop(
                            dragSource,
                            dropTargetId,
                            dropTarget
                          );
                          if (!valid) return false;

                          if (dragSource?.parent === dropTargetId) {
                            return true;
                          }
                        }}
                        dropTargetOffset={10}
                        placeholderRender={(node, { depth }) => (
                          <div
                            className="tree__placeholder bg-purple-500"
                            style={{ left: `${depth * 30}px` }}
                          ></div>
                        )}
                      />
                    </div>
                  </DndProvider>
                </ScrollArea>
              </fieldset>
            </div>
            <div
              className={`relative flex h-full min-h-[50vh] w-full flex-col overflow-hidden bg-muted/30 ${
                fullScreen ? "preview--fullscreen" : ""
              }`}
              id={"bgw-preview"}
              onMouseMove={(e) => {
                if (draggingGuide) {
                  let shift = e.shiftKey;
                  let ctrl = e.ctrlKey;
                  let x = e.clientX - draggingBounds.x;
                  let y = e.clientY - draggingBounds.y;

                  setGuidePosition({
                    x: draggingGuidesStart.x + x,
                    y: draggingGuidesStart.y + y,
                  });
                  // setDraggingBounds({x: x, y: y})
                  //modifyElementPosition(selectedComponentId, currentGuide.x + x, currentGuide.y + y)
                }
              }}
              onMouseLeave={(e) => {
                if (draggingGuide) {
                  setDraggingGuide(false);
                  let shift = e.shiftKey;
                  let ctrl = e.ctrlKey;
                  let x = e.clientX - draggingBounds.x;
                  let y = e.clientY - draggingBounds.y;

                  setGuidePosition({
                    x: draggingGuidesStart.x + x,
                    y: draggingGuidesStart.y + y,
                  });
                  modifyElementPosition(
                    selectedComponentId,
                    draggingGuidesStart.x + x,
                    draggingGuidesStart.y + y
                  );
                  setDraggingBounds({ x: 0, y: 0 });
                }
              }}
              onMouseUp={(e) => {
                if (draggingGuide) {
                  setDraggingGuide(false);
                  let shift = e.shiftKey;
                  let ctrl = e.ctrlKey;
                  let x = e.clientX - draggingBounds.x;
                  let y = e.clientY - draggingBounds.y;

                  setGuidePosition({
                    x: draggingGuidesStart.x + x,
                    y: draggingGuidesStart.y + y,
                  });
                  modifyElementPosition(
                    selectedComponentId,
                    draggingGuidesStart.x + x,
                    draggingGuidesStart.y + y
                  );
                  setDraggingBounds({ x: 0, y: 0 });
                }
              }}
            >
              <Button
                variant="error"
                className={`absolute left-3 bottom-3 z-40 ${
                  compileError != null || projectError != null
                    ? "flex"
                    : "hidden"
                }`}
                title={compileError}
              >
                <i className="material-symbols-rounded text-lg mr-3">
                  emergency_home
                </i>
                <p>Project contains errors: The output might be incorrect.</p>
              </Button>
              <Button
                variant="warning"
                className={`absolute left-3 bottom-3 z-40 ${
                  compileError == null &&
                  projectError == null &&
                  projectWarning != null
                    ? "flex"
                    : "hidden"
                }`}
              >
                <i className="material-symbols-rounded text-lg mr-3">warning</i>
                <p>Project contains warnings: The output might be incorrect.</p>
              </Button>
              <Button
                variant="secondary"
                className="absolute right-3 bottom-3 z-40"
                onClick={() => setFullScreen(!fullScreen)}
              >
                <i className="material-symbols-rounded text-lg">
                  {fullScreen ? "fullscreen_exit" : "fullscreen"}
                </i>
              </Button>
              <Badge variant="muted" className="absolute right-3 top-3">
                Output
              </Badge>
              <Badge variant="muted" className="absolute left-3 top-3">
                1920x1080
              </Badge>
              <div
                className="flex flex-1 justify-center items-center bgw-root-container"
                id="bgw-root-container"
              >
                <div
                  className="w-full h-full rounded-xl bgw-root"
                  id="bgw-root"
                ></div>
                {/*{*/}
                {/*    selectedClass !== null ? drawOutputElement(selectedClass) : null*/}
                {/*}*/}
              </div>
              {currentGuide !== null ? (
                <div
                  className="guide"
                  onMouseDown={(e) => {
                    return;
                    setDraggingGuide(true);
                    setDraggingBounds({ x: e.clientX, y: e.clientY });
                    setDraggingGuidesStart({
                      x: currentGuide.x,
                      y: currentGuide.y,
                    });
                  }}
                  draggable={true}
                  style={{
                    left: currentGuide.x,
                    top: currentGuide.y,
                    width: currentGuide.width,
                    height: currentGuide.height,
                  }}
                ></div>
              ) : null}
            </div>
            <ScrollArea
              className="h-full relative hidden flex-col items-center gap-8 md:flex pr-5 pl-5 w-[25%] shrink-0 max-w-[400px]"
              x-chunk="dashboard-03-chunk-0"
              style={{
                visibility: selectedClass == null ? "hidden" : "visible",
              }}
            >
              <div className="grid w-full items-start data__inputs">
                {getDataInputs(selectedClass)}
              </div>
            </ScrollArea>
          </main>
        </div>
      </div>
    </>
  );
}

export default Dashboard;
