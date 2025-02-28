import {
  ComponentViewData,
  Droppable,
  Instantiable,
  NonPrimitiveDataClasses,
} from "@/lib/components.ts";
// import constructors from "@/bgw/constructors.json";
import { ConstructorAPIImportInfo } from "@/lib/types.ts";
import * as Components from "@/lib/components.ts";
import { constructors } from "@/main";

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

const getInstantiableByData = (data: string) => {
  const flat = Object.entries(Components.Instantiable).flatMap((e) =>
    Object.entries(e[1])
  );
  return flat.find((e) => e[1].cls === data)[1];
};

export function exportComponent(component: any = null, all: any, id: string) {
  if (component == null) {
    component = all.find((e) => e.id === id);
  }
  if (!component) return "";

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
              return `${prop.name} = ${exportNonPrimitive(
                component[property[0]],
                prop.type,
                prop.name
              )}`;
            }
            return `${prop.name} = ${exportNonPrimitive(
              component[prop.name],
              prop.type,
              prop.name
            )}`;
          }
        })
        .filter((e) => e);

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
                            if (e[1] !== null && e[1].property !== undefined) {
                              return e[1].property === param.name;
                            }
                            return false;
                          });

                          if (property) {
                            return `${param.name} = ${exportNonPrimitive(
                              component[property[0]],
                              param.type,
                              param.name
                            )}`;
                          }
                          return `${param.name} = ${exportNonPrimitive(
                            component[param.name],
                            param.type,
                            param.name
                          )}`;
                        }
                      })
                      .join(",\n")}
                )`;

      if (apply.length > 0) {
        code += `.apply {
                    ${apply.join("\n")}
                }`;
      }

      let childrenCode = "";

      if (Droppable[component.type]) {
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
      return (childrenCode + code).trim();
    }

    //return `// Exporting is not supported yet and will be added soon.`;
    return `// No constructor found for ${found.shortName}`;
  }

  // return `// Exporting is not supported yet and will be added soon.`;
  return `// No constructor found for ${component.type}`;
}

function exportNonPrimitive(data: any, type: string = "", name: string = "") {
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
