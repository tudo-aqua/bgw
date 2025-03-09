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
import { getParamsFromSignature } from "./utils";

const applyBlacklist = [
  "id",
  "type",
  "name",
  "components",
  "alignment",
  "visual",
  "selectedColor",
  "font",
  "selectedItems",
  "selectedItem",
  "style",
  "filters",
  "flipped",
  "scroll",
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
  "selectedItems",
  "selectedItem",
];

const mappingNames = {
  front: "frontVisual",
  back: "backVisual",
  layoutFromCenter: "isLayoutFromCenter",
  allowIndeterminate: "isIndeterminateAllowed",
  initialColor: "selectedColor",
};

const getInstantiableByData = (data: string) => {
  const flat = Object.entries(Components.Instantiable).flatMap((e) =>
    Object.entries(e[1])
  );
  return flat.find((e) => e[1].cls === data)[1];
};

const getNonPrimitiveByType = (type: string) => {
  return Object.entries(Components.NonPrimitiveDataClasses).find(
    (e) => e[0] === type
  )[1];
};

const isInstantiable = (type: string) => {
  return Object.keys(Components.Instantiable).some((e) => e === type);
};

const isNonPrimitive = (type: string) => {
  return Object.keys(Components.NonPrimitiveDataClasses).some(
    (e) => e === type
  );
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
    if (component.objectType.includes("StructuredDataViewData")) {
      genericType = "Int";
    }
    if (component.objectType.includes("ComboBoxData")) {
      genericType = "String";
    }
    code = `val ${name} = ${compName}<${genericType}>(`;
  }

  let computedParams = [];
  let computedApply = [];

  for (let param of mainConstructorParams) {
    let pName = param.name;
    let newName = param.propertyName;

    let actualName = newName;

    if (component[newName] === undefined && component[pName] === undefined)
      continue;

    if (component[newName] === undefined) {
      actualName = pName;
    } else if (component[pName] === undefined) {
      actualName = newName;
    }

    if (
      ["String", "Number", "Boolean", "Double", "Int", "Float"].includes(
        param.type
      )
    ) {
      if (param.type === "String")
        computedParams.push(`${pName} = "${component[actualName]}"`);
      else if (param.type === "Double")
        computedParams.push(
          `${pName} = ${
            parseFloat(component[actualName]) ===
            parseInt(component[actualName])
              ? parseFloat(component[actualName]).toFixed(1)
              : parseFloat(component[actualName])
          }`
        );
      else computedParams.push(`${pName} = ${component[actualName]}`);
    } else {
      if (actualName === "target") {
        computedParams.push(`${pName} = Pane() // Replace with your target`);
        continue;
      }
      let exp = exportNonPrimitive(
        component[actualName],
        param.type,
        actualName
      );

      if (!exp) continue;
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
  let actualType = type.replace("Data", "");
  let code = "";

  if (type === "List<Visual>") {
    return `listOf(
      ${data
        .map((child) => {
          return exportNonPrimitive(child);
        })
        .join(",\n")}
      )`;
  } else if (type === "ObservableList<T>" && name === "items") {
    return `listOf(
      ${data.join(", ")}
      )`;
  } else if (type === "List<T>" && name === "items") {
    return `listOf(
      ${data
        .map((child) => {
          return '"' + child.second + '"';
        })
        .join(", ")}
      )`;
  } else if (type === "Color") {
    return `Color(0x${data.replace("#", "").toUpperCase()})`;
  } else if (type === "ColorVisual" && name === "selectionBackground") {
    return `ColorVisual(Color(0x${data.replace("#", "").toUpperCase()}))`;
  } else if (type === "Alignment") {
    if (data.first === "center" && data.second === "center") {
      return "Alignment.CENTER";
    } else {
      const found = Object.keys(getFolderForComponent("Alignment")).find(
        (e) => e.toLowerCase() === `${data.second}_${data.first}`.toLowerCase()
      );
      if (!found) return `Alignment.CENTER`;

      return `Alignment.${data.second.toUpperCase()}_${data.first.toUpperCase()}`;
    }
  } else if (type === "Orientation") {
    const found = Object.keys(getFolderForComponent("Orientation")).find(
      (e) => e.toLowerCase() === data.toLowerCase()
    );
    if (!found) return `Orientation.VERTICAL`;

    return `Orientation.${data.toUpperCase()}`;
  } else if (type === "HexOrientation") {
    const found = Object.keys(getFolderForComponent("HexOrientation")).find(
      (e) => e.toLowerCase() === data.toLowerCase()
    );
    if (!found) return `HexOrientation.POINTY_TOP`;

    return `HexOrientation.${data.toUpperCase()}`;
  } else if (type === "HexagonGrid.CoordinateSystem") {
    const folder = getFolderForComponent("HexagonGrid");
    if (!folder) return `HexagonGrid.CoordinateSystem.OFFSET`;

    const found = Object.keys(folder["CoordinateSystem"]).find(
      (e) => e.toLowerCase() === data.toLowerCase()
    );
    if (!found) return `HexagonGrid.CoordinateSystem.OFFSET`;

    return `HexagonGrid.CoordinateSystem.${data.toUpperCase()}`;
  } else if (type === "SelectionMode") {
    const found = Object.keys(getFolderForComponent("SelectionMode")).find(
      (e) => e.toLowerCase() === data.toLowerCase()
    );
    if (!found) return `SelectionMode.SINGLE`;

    return `SelectionMode.${data.toUpperCase()}`;
  } else if (type === "Font.FontStyle") {
    const folder = getFolderForComponent("Font");
    if (!folder) return `Font.FontStyle.NORMAL`;

    const found = Object.keys(folder["FontStyle"]).find(
      (e) => e.toLowerCase() === data.toLowerCase()
    );

    if (!found) return `Font.FontStyle.NORMAL`;
    return `Font.FontStyle.${data.toUpperCase()}`;
  } else if (type === "Font.FontWeight") {
    const folder = getFolderForComponent("Font");
    if (!folder) return `Font.FontWeight.NORMAL`;

    const valueToIndex = (parseInt(data) - 100) / 100;
    const possible = [
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

    if (valueToIndex >= 0 && valueToIndex < possible.length) {
      return `Font.FontWeight.${possible[valueToIndex].toUpperCase()}`;
    }

    return `Font.FontWeight.NORMAL`;
  } else if (type === "Flip") {
    const folder = getFolderForComponent("Flip");
    if (!folder) return `Flip.NONE`;

    const found = Object.keys(folder).find(
      (e) => e.toLowerCase() === data.toLowerCase()
    );
    if (!found) return `Flip.NONE`;

    return `Flip.${data.toUpperCase()}`;
  } else if (data.type) {
    if (data.type === "CompoundVisualData") {
      if (data.children.length === 1) {
        return exportNonPrimitive(data.children[0], data.children[0].type);
      } else {
        return `CompoundVisual(
                  ${data.children
                    .map((child) => {
                      return exportNonPrimitive(child, child.type);
                    })
                    .join(",\n")}
                )`;
      }
    }
    // else if (data.type === "ColorVisualData") {
    //   let code = `ColorVisual(Color(0x${data.color
    //     .replace("#", "")
    //     .toUpperCase()}))`;

    //   return code;
    // }
    else {
      const {
        index,
        comp,
        mainConstructor,
        mainConstructorParams,
        properties,
      } = getConstructorAndProperties(data.type.replace("Data", ""));
      if (!index || !comp || !mainConstructorParams || !properties)
        return `NON`;

      let code = `${actualType}(`;
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
        code = `${actualType}<${genericType}>(`;
      }

      let computedParams = [];
      let computedApply = [];

      for (let param of mainConstructorParams) {
        let pName = param.name;

        if (
          ["String", "Number", "Boolean", "Double", "Int", "Float"].includes(
            param.type
          )
        ) {
          if (param.type === "String")
            computedParams.push(`${pName} = "${data[pName]}"`);
          else if (param.type === "Double")
            computedParams.push(`${pName} = ${parseFloat(data[pName])}`);
          else computedParams.push(`${pName} = ${data[pName]}`);
        } else {
          let exp = exportNonPrimitive(data[pName], param.type, pName);

          if (exp.trim() === "") continue;
          computedParams.push(`${pName} = ${exp}`);
        }
      }

      if (isNonPrimitive(type)) {
        let reference = new Components.NonPrimitiveDataClasses[type]();
        let referenceProperties = Object.entries(reference)
          .map((e) => e[0])
          .filter((e) => properties.find((prop) => prop.name === e));

        for (let prop of referenceProperties) {
          if (data[prop] === undefined) continue;
          if (applyBlacklist.includes(prop)) continue;
          if (mainConstructorParams.find((param) => param.name === prop))
            continue;
          if (data[prop] === reference[prop].value) continue;

          computedApply.push(`${prop} = ${data[prop]}`);
        }
      }

      if (computedApply.length === 0) {
        return `${code}${computedParams.join(", ")})`;
      }

      return `${code}${computedParams.join(
        ", "
      )}).apply {\n${computedApply.join(",\n")}\n}`;
    }
  }
}

function getFolderForComponent(compName: string) {
  let matchingPkg = Object.keys(dirs).filter((e) => {
    let pkg = dirs[e];
    return pkg[compName] !== undefined;
  });

  if (!matchingPkg || matchingPkg.length === 0) return null;

  let element = dirs[matchingPkg[0]][compName];
  if (!element) return null;

  return element;
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

  // Get main constructor from API-Reference
  constructors.forEach((c: any) => {
    let own = getParamsFromSignature(c.info.signature);
    let global = getParamsFromSignature(index.details.info.signature);

    if (own.length === global.length) {
      c.primary = own.every(
        (param, i) =>
          param.name === global[i].name && param.type === global[i].type
      );
    }
  });

  let mainConstructor = constructors.find((c: any) => c.primary);
  if (!mainConstructor) {
    mainConstructor = constructors.fromRight(0);
  }

  let extractedTypes = getParamsFromSignature(mainConstructor.info.signature);

  let mainConstructorParams = mainConstructor.parameters
    .filter((param) => param.name.length > 1)
    .map((param) => {
      let newName = param.name;
      if (!properties.find((prop) => prop.name === param.name)) {
        newName = mappingNames[param.name] || param.name;
      }

      let type1 = extractedTypes.find((e) => e.name === param.name);
      let type2 = properties.find((e) => e.name === newName);

      return {
        name: param.name,
        propertyName: newName,
        type: type1 ? type1.type : type2 ? type2.type : "String",
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
