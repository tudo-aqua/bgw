import {
  ComponentViewData,
  Droppable,
  Instantiable,
  NonPrimitiveDataClasses,
} from "@/lib/components.ts";
// import constructors from "@/bgw/constructors.json";
import { ConstructorAPIImportInfo } from "@/lib/types.ts";
import * as Components from "@/lib/components.ts";
// import { constructors } from "@/lib/constructors.ts";
import { dirs } from "@/main";

const applyBlacklist = [
  "id",
  "type",
  "name",
  "components",
  "visual",
  "alignment",
  "selectedColor",
  "font",
  "selectedItem",
];
const applyBlacklistNonPrimitive = [
  "id",
  "type",
  "name",
  "components",
  "visual",
  "alignment",
  "color",
  "filters",
  "style",
  "selectedColor",
  "font",
  "selectedItem",
];

const mappingNames = {
  front: "frontVisual",
  back: "backVisual",
  layoutFromCenter: "isLayoutFromCenter",
  allowIndeterminate: "isIndeterminateAllowed",
};

const getInstantiableByData = (data: string) => {
  const flat = Object.entries(Components.Instantiable).flatMap((e) =>
    Object.entries(e[1])
  );
  return flat.find((e) => e[1].cls === data)[1];
};

export function exportComponent(component: any = null, all: any, id: string) {
  let compName = component.type.replace("Data", "");
  let { index, comp, mainConstructor, mainConstructorParams, properties } =
    getConstructorAndProperties(compName);
  if (!index || !comp || !mainConstructorParams || !properties)
    return `// No constructor found for ${compName}`;

  // Take Instatiable class from components.ts
  let referenceClass = getInstantiableByData(component.type).cls;
  let reference = new Components.DataClasses[referenceClass]();
  let referenceProperties = Object.entries(reference)
    .map((e) => e[0])
    .filter((e) => properties.find((prop) => prop.name === e));

  // Process component name to include no special characters or spaces
  let name =
    component.name.trim() === ""
      ? compName.charAt(0).toLowerCase() + compName.slice(1)
      : component.name.charAt(0).toLowerCase() + component.name.slice(1);
  if (name.indexOf(" ") > -1) name = name.split(" ").join("_");
  name = name.replace(/[^a-zA-Z0-9_]/g, "");

  let code = `val ${name} = ${compName}(`;
  if (mainConstructor.parameters.length !== mainConstructorParams.length) {
    let genericType = "ComponentView";
    try {
      genericType = index.details.info.signature
        .split("(")[0]
        .split("<")[1]
        .split(">")[0]
        .split(":")[1]
        .trim();
    } catch (_) {
      /* empty */
    }
    code = `val ${name} = ${compName}<${genericType}>(`;
  }

  let computedParams = [];
  let computedApply = [];

  for (let param of mainConstructorParams) {
    let pName = param.name;
    if (component[pName] === undefined) continue;

    if (
      ["String", "Number", "Boolean", "Double", "Int", "Float"].includes(
        param.type
      )
    ) {
      computedParams.push(`${pName} = ${component[pName]}`);
    } else {
      let exp = exportNonPrimitive(component[pName], param.type, pName);

      if (exp.trim() === "") continue;
      computedParams.push(`${pName} = ${exp}`);
    }
  }

  for (let prop of referenceProperties) {
    if (component[prop] === undefined) continue;
    if (applyBlacklist.includes(prop)) continue;
    if (mainConstructorParams.find((param) => param.name === prop)) continue;
    if (component[prop] === reference[prop].value) continue;

    computedApply.push(`${prop} = ${component[prop]}`);
  }

  if (computedApply.length === 0) {
    return `${code}${computedParams.join(", ")})`;
  }

  return `${code}${computedParams.join(", ")}).apply {\n${computedApply.join(
    ",\n"
  )}\n}`;
}

function exportNonPrimitive(data: any, type: string = "", name: string = "") {
  if (data === undefined) return "";
  let code = "";

  if (type === "List<Visual>") {
    return `PRIM_listOf()`;
  } else if (type === "Color") {
    return `PRIM_Color(0x${data.replace("#", "").toUpperCase()})`;
  } else if (type === "ColorVisual" && name === "selectionBackground") {
    return `PRIM_ColorVisual(Color(0x${data.replace("#", "").toUpperCase()}))`;
  } else if (type === "Alignment") {
    return `PRIM_Alignment.CENTER`;
  } else if (type === "Orientation") {
    return `PRIM_Orientation.VERTICAL`;
  } else if (type === "SelectionMode") {
    return `PRIM_SelectionMode.${data.toUpperCase()}`;
  } else if (type === "Font.FontStyle") {
    return `PRIM_Font.FontStyle.NORMAL`;
  } else if (type === "Flip") {
    return `PRIM_Flip.NONE`;
  } else if (data.type) {
    console.log("[DATA TYPE]", data.type.replace("Data", ""));
    const { index, comp, mainConstructor, mainConstructorParams, properties } =
      getConstructorAndProperties(data.type.replace("Data", ""));
    if (!index || !comp || !mainConstructorParams || !properties) return `NON`;
  }
}

export function exportComponentOld(
  component: any = null,
  all: any,
  id: string
) {
  if (component == null) {
    component = all.find((e) => e.id === id);
  }
  if (!component) return "";

  console.log("[EXPORTING]", component);

  let elements = constructors.filter(
    (con) => con.fullName === component.type.replace("Data", "")
  );
  let referenceClass = getInstantiableByData(component.type).cls;
  let reference = new Components.DataClasses[referenceClass]();
  let referenceProperties = Object.entries(reference).map((e) => e[0]);

  if (elements.length > 0) {
    let found = elements[0] as ConstructorAPIImportInfo;
    let constructor =
      found.constructors.length > 0 ? found.constructors[0] : null;

    if (constructor) {
      let parameters = Object.entries(constructor).map((param) => {
        return {
          name: param[0],
          type: param[1],
          primitive:
            param[1] === "String" ||
            param[1] === "Number" ||
            param[1] === "Boolean" ||
            param[1] === "Double" ||
            param[1] === "Int" ||
            param[1] === "Float",
        };
      });
      let properties = [];
      found.properties
        .map((prop) => {
          return {
            name: prop.first,
            type: prop.second,
            primitive:
              prop.second === "String" ||
              prop.second === "Number" ||
              prop.second === "Boolean" ||
              prop.second === "Double" ||
              prop.second === "Int" ||
              prop.second === "Float",
          };
        })
        .forEach((prop) => {
          if (
            properties.find((p) => p.name === prop.name && p.type === prop.type)
          )
            return;
          if (!referenceProperties.includes(prop.name)) return;
          if (constructor[prop.name]) return;
          properties.push(prop);
        });

      let name =
        component.name.trim() === ""
          ? found.shortName.charAt(0).toLowerCase() + found.shortName.slice(1)
          : component.name.charAt(0).toLowerCase() + component.name.slice(1);
      if (name.indexOf(" ") > -1) name = name.split(" ").join("_");
      name = name.replace(/[^a-zA-Z0-9_]/g, "");

      let apply = properties
        .map((prop) => {
          if (component[prop.name] === undefined) {
            return;
          }
          if (applyBlacklist.includes(prop.name)) {
            return;
          }
          if (
            prop.primitive &&
            referenceProperties.includes(prop.name) &&
            component[prop.name] === reference[prop.name].value
          ) {
            return;
          }
          if (prop.type == "Flip" && component[prop.name] === "none") {
            return;
          }

          if (prop.type == "String") {
            return `${prop.name} = "${component[prop.name]}"`;
          } else if (prop.primitive) {
            return `${prop.name} = ${component[prop.name]}`;
          } else {
            let property = Object.entries(reference).find((e) => {
              if (e[1] !== null && e[1].property !== undefined) {
                return e[1].property === prop.name;
              }
              return false;
            });

            if (property) {
              console.log("[NON PRIM 1]", prop.name);
              let exp = exportNonPrimitive(
                component[property[0]],
                prop.type,
                prop.name
              );
              if (exp.trim() === "") return;
              return `${prop.name} = ${exp}`;
            }

            console.log("[NON PRIM 2]", prop.name);
            let exp = exportNonPrimitive(
              component[prop.name],
              prop.type,
              prop.name
            );

            if (exp.trim() === "") return;
            return `${prop.name} = ${exp}`;
          }
        })
        .filter((e) => e)
        .filter((e) => e !== undefined && e !== null && e !== "");

      let code = `
                val ${name} = ${found.shortName}(
                    ${parameters
                      .map((param) => {
                        if (param.type == "String") {
                          return `${param.name} = "${component[param.name]}"`;
                        } else if (param.primitive) {
                          return `${param.name} = ${component[param.name]}`;
                        } else if (param.name === "formatFunction") {
                          return `${param.name} = { item -> item.toString() }`;
                        } else if (param.name === "items") {
                          return `${param.name} = listOf()`;
                        } else if (param.name === "toggleGroup") {
                          return `${param.name} = `;
                        } else {
                          let property = Object.entries(reference).find((e) => {
                            if (component[param.name] === undefined)
                              return false;

                            if (component[param.name] === undefined)
                              return false;
                            if (e[1] !== null && e[1].property !== undefined) {
                              return e[1].property === param.name;
                            }
                            return false;
                          });

                          if (property) {
                            console.log("[NON PRIM 3]", param.name);
                            let exp = exportNonPrimitive(
                              component[property[0]],
                              param.type,
                              param.name
                            );
                            if (exp.trim() === "") return;
                            return `${param.name} = ${exp}`;
                          }

                          console.log("[NON PRIM 4]", param.name);
                          let exp = exportNonPrimitive(
                            component[param.name],
                            param.type,
                            param.name
                          );
                          if (exp.trim() === "") return;
                          return `${param.name} = ${exp}`;
                        }
                      })
                      .filter((e) => e)
                      .filter((e) => e !== undefined && e !== null && e !== "")
                      .join(",\n")}
                )`;

      if (apply.length > 0) {
        code += `.apply {
                    ${apply.join("\n")}
                }`;
      }

      let childrenCode = "";

      /* if (Droppable[component.type]) {
        if (
          component.type === "CardStackData" ||
          component.type === "PaneData"
        ) {
          childrenCode = component.components
            .map((child) => {
              return exportComponent(child, [], child.id);
            })
            .join("\n");
          // TODO - Fix
        } else if (component.type === "HexagonGridData") {
          childrenCode = component.map.values
            .map((child) => {
              return exportComponent(child, [], child.id);
            })
            .join("\n");
        }
      }

      if (all.length > 0) {
        return (childrenCode + code).trim();
      }
      return (childrenCode + code).trim();*/
      return code;
    }

    //return `// Exporting is not supported yet and will be added soon.`;
    return `// No constructor found for ${found.shortName}`;
  }

  // return `// Exporting is not supported yet and will be added soon.`;
  return `// No constructor found for ${component.type}`;
}

function getConstructorAndProperties(compName: string): {
  index: any;
  comp: any;
  mainConstructor: any;
  mainConstructorParams: any;
  properties: any;
} {
  let matchingPkg = Object.keys(dirs).filter((e) => {
    let pkg = dirs[e];
    return pkg[compName] !== undefined;
  });

  if (!matchingPkg || matchingPkg.length === 0) return null;

  let element = dirs[matchingPkg[0]][compName];
  if (!element) return null;

  let index = element["_index"];
  let comp = element[compName];
  if (!comp || !index) return null;

  let constructors = comp.details;
  let properties = index.members.properties.map((prop) => {
    return {
      name: prop.name,
      type: prop.signature.split(":")[1].trim().replace("?", ""),
    };
  });

  console.log(constructors);

  // Get main constructor from API-Reference
  let mainConstructor = constructors.fromRight(0);
  console.log(
    properties,
    mainConstructor.parameters.map((e) => e.name)
  );
  let mainConstructorParams = mainConstructor.parameters
    .filter((param) => param.name.length > 1)
    .map((param) => {
      let newName = param.name;
      if (!properties.find((prop) => prop.name === param.name)) {
        newName = mappingNames[param.name] || param.name;
      }
      return {
        name: param.name,
        type: properties.find((prop) => prop.name === newName).type,
      };
    });

  return {
    index,
    comp,
    mainConstructor,
    mainConstructorParams,
    properties,
  };
}

function exportNonPrimitiveOld(
  data: any,
  type: string = "",
  name: string = ""
) {
  console.log(data, type, name);
  if (data === undefined) return "";
  let code = "";

  if (type === "List<Visual>") {
    return `listOf(
            ${data
              .map((child) => {
                return exportNonPrimitive(child);
              })
              .join(",\n")}
        )`;
  } else if (type === "Color") {
    return `Color(0x${data.replace("#", "").toUpperCase()})`;
  } else if (type === "ColorVisual" && name === "selectionBackground") {
    return `ColorVisual(Color(0x${data.replace("#", "").toUpperCase()}))`;
  } else if (type === "Alignment") {
    let possibleAlignments = constructors.filter(
      (con) =>
        con.fullName.startsWith("Alignment") && con.fullName !== con.shortName
    );
    if (data.first === "center" && data.second === "center") {
      return "Alignment.CENTER";
    } else {
      let found = possibleAlignments.find((align) => {
        let split = align.returnType.toLowerCase().split("_");
        return split[0] === data.second && split[1] === data.first;
      });
      if (found) {
        return found.fullName;
      }

      return `Alignment.CENTER`;
    }
  } else if (type === "Orientation") {
    let possible = constructors.filter((con) =>
      con.fullName.startsWith("Orientation")
    );
    let found = possible.find(
      (style) => style.returnType.toLowerCase() === data.toLowerCase()
    );
    if (found) {
      return `Orientation.${data.toUpperCase()}`;
    }

    return `Orientation.VERTICAL`;
  } else if (type === "SelectionMode") {
    let possible = constructors.filter((con) =>
      con.fullName.startsWith("SelectionMode")
    );
    let found = possible.find(
      (style) => style.returnType.toLowerCase() === data.toLowerCase()
    );
    if (found) {
      return `SelectionMode.${data.toUpperCase()}`;
    }

    return `SelectionMode.SINGLE`;
  } else if (type === "Font.FontStyle") {
    let possible = ["normal", "italic", "oblique"];
    // TODO - Add enum classes in classes to constructors.json
    // let found = possible.find((style) => style.returnType.toLowerCase() === data.toLowerCase())
    if (possible.includes(data.toLowerCase())) {
      return `Font.FontStyle.${data.toUpperCase()}`;
    }

    return `Font.FontStyle.NORMAL`;
  } else if (type === "Flip") {
    let possible = constructors.filter((con) =>
      con.fullName.startsWith("Flip")
    );
    let found = possible.find(
      (style) => style.returnType.toLowerCase() === data.toLowerCase()
    );
    if (possible.includes(data.toLowerCase())) {
      return `Flip.${data.toUpperCase()}`;
    }

    return `Flip.NONE`;
  } else if (type === "Font.FontWeight") {
    let valueToIndex = (parseInt(data) - 100) / 100;
    let possible = [
      "thin",
      "extra_light",
      "light",
      "normal",
      "medium",
      "semi_bold",
      "bold",
      "extra_bold",
      "black",
    ];
    // let found = possible.find((style) => style.returnType.toLowerCase() === data.toLowerCase())
    if (valueToIndex >= 0 && valueToIndex < possible.length) {
      return `Font.FontWeight.${possible[valueToIndex].toUpperCase()}`;
    }

    return `Font.FontWeight.NORMAL`;
  } else if (data.type) {
    console.warn("[DATA TYPE]", data.type);
    let elements = constructors.filter(
      (con) => con.fullName === data.type.replace("Data", "")
    );
    let reference = new Components.NonPrimitiveDataClasses[data.type]();
    let referenceProperties = Object.entries(reference).map((e) => e[0]);

    if (elements.length > 0) {
      let found = elements[0] as ConstructorAPIImportInfo;
      let constructor =
        found.constructors.length > 0 ? found.constructors[0] : null;

      if (constructor) {
        let parameters = Object.entries(constructor).map((param) => {
          return {
            name: param[0],
            type: param[1],
            primitive:
              param[1] === "String" ||
              param[1] === "Number" ||
              param[1] === "Boolean" ||
              param[1] === "Double" ||
              param[1] === "Int" ||
              param[1] === "Float",
          };
        });
        let properties = [];
        found.properties
          .map((prop) => {
            return {
              name: prop.first,
              type: prop.second,
              primitive:
                prop.second === "String" ||
                prop.second === "Number" ||
                prop.second === "Boolean" ||
                prop.second === "Double" ||
                prop.second === "Int" ||
                prop.second === "Float",
            };
          })
          .forEach((prop) => {
            if (
              properties.find(
                (p) => p.name === prop.name && p.type === prop.type
              )
            )
              return;
            if (!referenceProperties.includes(prop.name)) return;
            if (constructor[prop.name]) return;
            properties.push(prop);
          });

        let apply = properties
          .map((prop) => {
            if (data[prop.name] === undefined) return;
            if (applyBlacklistNonPrimitive.includes(prop.name)) return;
            if (
              prop.primitive &&
              referenceProperties.includes(prop.name) &&
              data[prop.name] === reference[prop.name].value
            )
              return;

            if (prop.type == "Flip" && data[prop.name] === "none") {
              return;
            }
            if (prop.type == "String") {
              return `${prop.name} = "${data[prop.name]}"`;
            } else if (prop.primitive) {
              return `${prop.name} = ${data[prop.name]}`;
            } else {
              return `${prop.name} = ${exportNonPrimitive(
                data[prop.name],
                prop.type
              )}`;
            }
          })
          .filter((e) => e);

        if (data.type === "CompoundVisualData") {
          if (data.children.length === 1) {
            code = exportNonPrimitive(data.children[0], data.children[0].type);
          } else {
            code = `${found.shortName}(
                            ${data.children
                              .map((child) => {
                                return exportNonPrimitive(child, child.type);
                              })
                              .join(",\n")}
                        )`;
          }
        } else if (data.type === "ColorVisualData") {
          code = `${found.shortName}(Color(0x${data.color
            .replace("#", "")
            .toUpperCase()}))`;
        } else {
          code = `${found.shortName}(
                            ${parameters
                              .map((param) => {
                                if (param.type == "String") {
                                  return `${param.name} = "${
                                    data[param.name]
                                  }"`;
                                } else if (param.primitive) {
                                  return `${param.name} = ${data[param.name]}`;
                                } else {
                                  return `${param.name} = ${exportNonPrimitive(
                                    data[param.name],
                                    param.type
                                  )}`;
                                }
                              })
                              .join(",\n")}
                    )`;
        }

        if (apply.length > 0) {
          code += `.apply {
                        ${apply.join("\n")}
                    }`;
        }
      }
    }
  }

  return code;
}
