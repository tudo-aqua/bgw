import { Button } from "@/components/ui/button.tsx";
import {
  Tooltip,
  TooltipContent,
  TooltipTrigger,
} from "@/components/ui/tooltip.tsx";
import { Drawer, DrawerTrigger } from "@/components/ui/drawer.tsx";
import { ScrollArea } from "@/components/ui/scroll-area.tsx";
import {
  DndProvider,
  getBackendOptions,
  MultiBackend,
  Tree,
} from "@minoru/react-dnd-treeview";
import { Toggle } from "@/components/ui/toggle.tsx";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog.tsx";
import {
  Select,
  SelectContent,
  SelectGroup,
  SelectItem,
  SelectLabel,
  SelectTrigger,
  SelectValue,
} from "@/components/ui/select.tsx";
import * as Components from "@/lib/components.ts";
import { Badge } from "@/components/ui/badge.tsx";
import React, { useState } from "react";
import { Textarea } from "@/components/ui/textarea.tsx";
import { CharStream, CommonTokenStream } from "antlr4ng";
import { KotlinLexer } from "@/generated/KotlinLexer.ts";
import { TokenSource } from "antlr4/src/antlr4/TokenSource";
import {
  colourNameToHex,
  constructors,
  enums,
  specialConstructors,
} from "@/lib/constructors.ts";
import { isNumber } from "@/lib/utils.ts";
import {
  ColorVisualData,
  CompoundVisualData,
  SingleLayerVisualData,
} from "@/lib/components.ts";

type Token = {
  type: string;
  value: string;
  line: number;
};

type Assignment = {
  property: string | number;
  value: string;
};

type ParserProps = {
  onCodeChange: (result: object) => void;
};

function Parser({ onCodeChange }: ParserProps) {
  const hardKeywords = [
    "as",
    "as?",
    "break",
    "class",
    "continue",
    "do",
    "else",
    "false",
    "for",
    "fun",
    "if",
    "in",
    "!in",
    "interface",
    "is",
    "!is",
    "null",
    "object",
    "package",
    "return",
    "super",
    "this",
    "throw",
    "true",
    "try",
    "typealias",
    "typeof",
    "val",
    "var",
    "when",
    "while",
  ];

  const softKeywords = [
    "by",
    "catch",
    "constructor",
    "delegate",
    "dynamic",
    "field",
    "file",
    "finally",
    "get",
    "import",
    "init",
    "param",
    "property",
    "receiver",
    "set",
    "setparam",
    "where",
  ];

  const modifierKeywords = [
    "abstract",
    "annotation",
    "companion",
    "const",
    "crossinline",
    "data",
    "enum",
    "external",
    "final",
    "infix",
    "inline",
    "inner",
    "internal",
    "lateinit",
    "noinline",
    "open",
    "operator",
    "out",
    "override",
    "private",
    "protected",
    "public",
    "reified",
    "sealed",
    "suspend",
    "tailrec",
    "vararg",
  ];

  const specialModifiers = ["field", "it"];

  const operators = [
    "+",
    "-",
    "*",
    "/",
    "%",
    "=",
    "+=",
    "-=",
    "*=",
    "/=",
    "%=",
    "++",
    "--",
    "&&",
    "||",
    "!",
    "==",
    "!=",
    "===",
    "!==",
    "<",
    ">",
    "<=",
    ">=",
    "!!",
    "?.",
    "?:",
    "::",
    "..",
    "..<",
    ":",
    "?",
    "->",
    "@",
    "$",
    "_",
  ];

  const separators = [",", " ", "\n", "\t", "(", ")", "{", "}", "[", "]"];

  let [parsed, setParsed] = useState(null);

  const tryParse = (value: string) => {
    const tokens = getKotlinTokens(value).filter((token) => {
      let simpleTokenSplit = token.type.split("_");
      let simpleToken =
        simpleTokenSplit[simpleTokenSplit.length - 1].toLowerCase();
      return (
        simpleToken != "ws" &&
        token.type.toLowerCase().indexOf("comment") == -1 &&
        token.type != "Inside_NL"
      );
    });

    let separatedTokens = getTokensStartingWithValOrVar(tokens);
    console.warn("BY VAL SEPERATED TOKENS >>>>>", separatedTokens);

    let result: object[] = [];
    separatedTokens.forEach((tokens) => {
      if (tokens.length == 0) return;
      let allAssignments = getConstructorArguments(tokens);
      let assignments = allAssignments.constructor;
      let applys = allAssignments.apply;

      let assignment = tokens.findIndex((token) => {
        return token.type == "ASSIGNMENT";
      });

      console.info("FOUND CONSTRUCTOR ASSIGNMENTS >>>>>", { ...assignments });
      console.info("FOUND APPLYS >>>>>", { ...applys });

      if (assignment == -1) return;
      if (assignment == 0 || assignment == tokens.length - 1) return;
      let component: Assignment = {
        property: tokens[assignment - 1].value,
        value: tokens[assignment + 1].value,
      };
      const constructor = constructors[component.value];
      if (!constructor) return;

      if (!checkNaming(assignments)) return;

      assignments.forEach((assignment) => {
        if (isNumber(assignment.property)) {
          assignment.property = constructor[assignment.property].property;
        }
      });
      let distinctAssignments = {};
      for (const assignment of assignments) {
        distinctAssignments[assignment.property] = assignment.value;
      }
      if (assignments.length != Object.keys(distinctAssignments).length) return;

      let object = new Components.DataClasses[component.value + "Data"]();
      object.name.value = component.property;

      for (const propertyName in distinctAssignments) {
        let constructorProperty = constructors[component.value].find(
          (constructor) => constructor.property == propertyName
        );
        let typedValue = getTypedValue(
          distinctAssignments[propertyName],
          constructorProperty
        );

        if (constructorProperty && constructorProperty.primitive) {
          object[propertyName].value = typedValue;
        } else if (
          constructorProperty &&
          constructorProperty.type == "Alignment"
        ) {
          let value = distinctAssignments[propertyName]
            .replace("Alignment.", "")
            .toUpperCase();
          let enumValues = enums[constructorProperty.type][value];

          if (enumValues) {
            enumValues.forEach((enumValue) => {
              object[enumValue.property].value = enumValue.value;
            });
          }
        } else if (
          constructorProperty &&
          constructorProperty.type == "Visual"
        ) {
          object[propertyName] = getCompoundVisual(
            getKotlinTokens(distinctAssignments[propertyName])
          );
        } else if (constructorProperty && constructorProperty.type == "Font") {
          object[propertyName] = getFont(
            getKotlinTokens(distinctAssignments[propertyName])
          );
        } else if (!constructorProperty) {
          return;
        }
      }

      let distinctApplys = {};
      for (const apply of applys) {
        distinctApplys[apply.property] = apply.value;
      }

      for (const propertyName in distinctApplys) {
        let constructorProperty = constructors[component.value].find(
          (constructor) => constructor.property == propertyName
        );
        let typedValue = getTypedValue(
          distinctApplys[propertyName],
          constructorProperty
        );

        if (constructorProperty && constructorProperty.primitive) {
          object[propertyName].value = typedValue;
        } else if (
          constructorProperty &&
          constructorProperty.type == "Alignment"
        ) {
          let value = distinctApplys[propertyName]
            .replace("Alignment.", "")
            .toUpperCase();
          let enumValues = enums[constructorProperty.type][value];

          if (enumValues) {
            enumValues.forEach((enumValue) => {
              object[enumValue.property].value = enumValue.value;
            });
          }
        } else if (!constructorProperty) {
          if (object[propertyName] && object[propertyName].propType) {
            let typedValue = getTypedValueApply(
              distinctApplys[propertyName],
              object[propertyName].propType
            );
            object[propertyName].value = typedValue;
          }
        }
      }

      result.push(object);
    });

    setParsed(JSON.stringify(tokens, null, 2));

    onCodeChange(result);
  };

  function getCompoundVisual(tokens: Token[], topLayer: boolean = true) {
    const filteredTokens = tokens.filter((token) => {
      let simpleTokenSplit = token.type.split("_");
      let simpleToken =
        simpleTokenSplit[simpleTokenSplit.length - 1].toLowerCase();
      return (
        simpleToken != "ws" &&
        token.type.toLowerCase().indexOf("comment") == -1 &&
        simpleToken != "nl"
      );
    });

    if (filteredTokens.length == 0) return null;
    if (filteredTokens[0].value == "Visual") {
      let completeString = filteredTokens
        .filter((token) => token.type === "Identifier" || token.type === "DOT")
        .map((token) => token.value)
        .join("");
      if (completeString == "Visual.EMPTY") {
        let comp = new CompoundVisualData();
        comp.children = [new ColorVisualData()];
        (comp.children[0] as ColorVisualData).color = "#ffffff";
        comp.children[0].transparency.value = 0;
        return comp;
      }
      return null;
    } else if (filteredTokens[0].value == "CompoundVisual") {
      const compoundAssignments =
        getConstructorArguments(filteredTokens).constructor;

      const compoundVisuals: SingleLayerVisualData[] = compoundAssignments.map(
        (assignment) => {
          return getCompoundVisual(getKotlinTokens(assignment.value), false);
        }
      );
      const compoundVisual = new CompoundVisualData();
      compoundVisual.children = compoundVisuals;

      return compoundVisual;
    } else if (filteredTokens[0].value == "ColorVisual") {
      let colorVisualArguments =
        getConstructorArguments(filteredTokens).constructor;
      let colorVisual = new ColorVisualData();
      if (colorVisualArguments.length == 1) {
        colorVisual.color = getColor(
          getKotlinTokens(colorVisualArguments[0].value)
        );
      } else if (colorVisualArguments.length == 0) {
        let exists = checkIfConExists(filteredTokens);
        if (!exists) {
          colorVisual.color = "#ffffff";
        }

        let colorEnumIndex = filteredTokens
          .reverse()
          .findIndex((token) => token.type == "Identifier");
        let colorEnum = filteredTokens[colorEnumIndex].value;
        colorVisual.color = colourNameToHex(colorEnum.toLowerCase());
      }

      if (topLayer == true) {
        let comp = new CompoundVisualData();
        comp.children = [colorVisual];
        return comp;
      }
      return colorVisual;
    } else {
      return new CompoundVisualData();
    }
  }

  function getFont(tokens: Token[]) {
    const filteredTokens = tokens.filter((token) => {
      let simpleTokenSplit = token.type.split("_");
      let simpleToken =
        simpleTokenSplit[simpleTokenSplit.length - 1].toLowerCase();
      return (
        simpleToken != "ws" &&
        token.type.toLowerCase().indexOf("comment") == -1 &&
        simpleToken != "nl"
      );
    });

    if (filteredTokens.length == 0) return null;

    if (filteredTokens[0].value != "Font") {
      return new Components.FontData();
    } else if (filteredTokens[0].value == "Font") {
      const fontAssignments =
        getConstructorArguments(filteredTokens).constructor;
      if (!checkNaming(fontAssignments)) return new Components.FontData();

      const constructor = specialConstructors["Font"];
      if (!constructor) return;

      fontAssignments.forEach((assignment) => {
        if (isNumber(assignment.property)) {
          assignment.property = constructor[assignment.property].property;
        }
      });

      const font = new Components.FontData();
      fontAssignments.forEach((assignment) => {
        if (assignment.property == "size") {
          font.size.value = getTypedValue(
            assignment.value,
            constructor.find((con) => con.property == "size")
          );
        } else if (assignment.property == "family") {
          font.family.value = getTypedValue(
            assignment.value,
            constructor.find((con) => con.property == "family")
          );
        } else if (assignment.property == "color") {
          font.color.value = getColor(getKotlinTokens(assignment.value));
        } else if (assignment.property == "fontWeight") {
          font.fontWeight.value = getTypedValue(
            assignment.value,
            constructor.find((con) => con.property == "fontWeight")
          );
        } else if (assignment.property == "fontStyle") {
          font.fontStyle.value = getTypedValue(
            assignment.value,
            constructor.find((con) => con.property == "fontStyle")
          );
        }
      });

      return font;
    }
  }

  function checkIfConExists(tokens: Token[]) {
    return true;
    let completeString = tokens
      .filter((token) => token.type === "Identifier" || token.type === "DOT")
      .map((token) => token.value)
      .join("");
    let availableConstructors = window.conData.filter(
      (con) => con.fullName == completeString
    );
    return availableConstructors.length > 0;
  }

  function getColor(tokens: Token[]) {
    const filteredTokens = tokens.filter((token) => {
      let simpleTokenSplit = token.type.split("_");
      let simpleToken =
        simpleTokenSplit[simpleTokenSplit.length - 1].toLowerCase();
      return (
        simpleToken != "ws" &&
        token.type.toLowerCase().indexOf("comment") == -1 &&
        simpleToken != "nl"
      );
    });

    if (filteredTokens.length == 0) return "#ffffff";

    let exists = checkIfConExists(filteredTokens);
    if (!exists) {
      return "#ffffff";
    }

    if (filteredTokens[0].value == "Color") {
      const colorAssignments =
        getConstructorArguments(filteredTokens).constructor;

      switch (colorAssignments.length) {
        case 0:
          let colorEnumIndex = filteredTokens
            .reverse()
            .findIndex((token) => token.type == "Identifier");
          let colorEnum = filteredTokens[colorEnumIndex].value;

          return colourNameToHex(colorEnum.toLowerCase());
        case 1:
          return colorAssignments[0].value.replace("0x", "#");
        case 3:
          return convertRGBToHex(
            `rgb(${colorAssignments[0].value},${colorAssignments[1].value},${colorAssignments[2].value})`
          );
        case 4:
          return convertRGBAToHex(
            `rgba(${colorAssignments[0].value},${colorAssignments[1].value},${colorAssignments[2].value},${colorAssignments[3].value})`
          );
        default:
          return "#ffffff";
      }
    } else {
      return "#ffffff";
    }
  }

  function convertRGBAToHex(rgba: string) {
    let values = rgba.replace("rgba(", "").replace(")", "").split(",");
    let hex = "#";
    for (let i = 0; i < 3; i++) {
      let value = parseInt(values[i]).toString(16);
      hex += value.length == 1 ? "0" + value : value;
    }
    return hex;
  }

  function convertRGBToHex(rgb: string) {
    let values = rgb.replace("rgb(", "").replace(")", "").split(",");
    let hex = "#";
    for (let i = 0; i < 3; i++) {
      let value = parseInt(values[i]).toString(16);
      hex += value.length == 1 ? "0" + value : value;
    }
    return hex;
  }

  function getTypedValueApply(value: string, type: string) {
    if (type == "string") {
      return eval(value);
      return value.replace(/"/g, "");
    } else if (type == "number") {
      return isNumber(parseFloat(eval(value))) ? parseFloat(eval(value)) : 0;
    } else if (type == "boolean") {
      return value == "true";
    }

    return value;
  }

  function getTypedValue(value: string, con: object) {
    if (!con) return value;

    if (con.primitive) {
      if (con.type == "string") {
        return eval(value);
        //return value.replace(/"/g, "")
      } else if (con.type == "number") {
        return isNumber(parseFloat(eval(value))) ? parseFloat(eval(value)) : 0;
      } else if (con.type == "boolean") {
        return value == "true";
      }
    }
    return value;
  }

  function checkNaming(assignments: Assignment[]): boolean {
    let named = false;
    let result = true;
    assignments.forEach((assignment, index) => {
      if (isNumber(assignment.property)) {
        if (index < assignments.length - 1 && assignment.value.isEmpty()) {
          result = false;
        } else if (
          !(index == assignments.length - 1 && assignment.value.isEmpty()) &&
          named
        ) {
          result = false;
        } else if (
          index == assignments.length - 1 &&
          assignment.value.isEmpty()
        ) {
          assignments.pop();
        }
      } else {
        named = true;
      }
    });
    return result;
  }

  function getConstructorArguments(tokens: Token[]) {
    let constructorResult: Assignment[] = [];
    let applyResult: Assignment[] = [];
    let currentAssignment: Assignment = {
      property: "",
      value: "",
    };

    let startIndex = 0;
    for (let i = 0; i < tokens.length; i++) {
      if (tokens[i].type == "LPAREN") {
        startIndex = i + 1;
        break;
      }
    }

    let parenthesisCount = 1;
    let rightSide = false;
    let constructorEnded = false;

    for (let i = startIndex; i < tokens.length; i++) {
      if (tokens[i].type == "LPAREN") {
        parenthesisCount++;
      } else if (tokens[i].type == "RPAREN") {
        parenthesisCount--;
      }

      if (rightSide == false) {
        rightSide = true;
        if (tokens[i + 1] && tokens[i + 1].type == "ASSIGNMENT") {
          currentAssignment.property = tokens[i].value;
          i++;
          continue;
        } else {
          currentAssignment.property = constructorResult.length;
        }
      }

      let rightSideEnded =
        (tokens[i].type == "COMMA" && parenthesisCount == 1) ||
        parenthesisCount == 0;
      if (rightSideEnded) {
        rightSide = false;
        constructorResult.push(currentAssignment);
        currentAssignment = {
          property: "",
          value: "",
        };
      } else {
        currentAssignment.value += tokens[i].value;
      }

      if (parenthesisCount == 0) {
        return {
          constructor: constructorResult,
          apply: getApplyArguments(tokens, i + 1),
        };
      }
    }

    return {
      constructor: constructorResult,
      apply: applyResult,
    };
  }

  function getApplyArguments(tokens: Token[], start: number) {
    let result: Assignment[] = [];
    let currentAssignment: Assignment = {
      property: "",
      value: "",
    };

    let startIndex;
    for (let i = start; i < tokens.length; i++) {
      if (tokens[i].value == "apply") {
        startIndex = i + 2;
        break;
      }
    }
    if (startIndex == undefined) return result;

    let parenthesisCount = 1;
    let rightSide = false;
    let applyEnded = false;

    for (let i = startIndex; i < tokens.length; i++) {
      if (tokens[i].type == "LCURL") {
        parenthesisCount++;
      } else if (tokens[i].type == "RCURL") {
        parenthesisCount--;
      }

      if (rightSide == false) {
        if (tokens[i].type != "ASSIGNMENT") {
          currentAssignment.property += tokens[i].value;
          continue;
        } else {
          rightSide = true;
          continue;
        }
      }

      let rightSideEnded =
        ((tokens[i].type == "SEMICOLON" || tokens[i].value == "\n") &&
          parenthesisCount == 1) ||
        parenthesisCount == 0;
      if (rightSideEnded) {
        rightSide = false;
        currentAssignment.property = currentAssignment.property
          .replace(/\n/gm, "")
          .replace("this.", "");
        currentAssignment.value = currentAssignment.value.replace(/\n/gm, "");
        result.push(currentAssignment);
        currentAssignment = {
          property: "",
          value: "",
        };
      } else {
        currentAssignment.value += tokens[i].value;
      }

      if (parenthesisCount == 0) {
        return result.concat(getApplyArguments(tokens, i + 1));
      }
    }

    return result;
  }

  const getTokensStartingWithValOrVar = (tokens: Token[]) => {
    const result = [];
    let currentTokens = [];

    for (let i = 0; i < tokens.length; i++) {
      if (tokens[i].type === "VAL" || tokens[i].type === "VAR") {
        if (currentTokens.length > 0) {
          result.push(currentTokens);
          currentTokens = [];
        }
        currentTokens.push(tokens[i]);
      } else if (currentTokens.length > 0) {
        currentTokens.push(tokens[i]);
      }
    }

    if (currentTokens.length > 0) {
      result.push(currentTokens);
    }

    return result;
  };

  const getAssignments = (tokens: Token[]) => {
    const assignments = [];
    for (let i = 0; i < tokens.length; i++) {
      if (tokens[i].type == "ASSIGNMENT") {
        const left = tokens[i - 1];
        const right = [];
        let j = i + 1;

        while (
          j < tokens.length &&
          tokens[j].type !== "SEMICOLON" &&
          tokens[j].type !== "NL" &&
          tokens[j].type !== "EOF" &&
          tokens[j].type !== "COMMA"
        ) {
          right.push(tokens[j]);
          j++;
        }

        if (left && right) {
          assignments.push({
            left: left.value,
            operator: tokens[i].value,
            right: right.map((token) => token.value).join(""),
          });
        }
      }
    }
    return assignments;
  };

  const getKotlinTokens = (code: string) => {
    const chars = CharStream.fromString(code);
    const lexer = new KotlinLexer(chars);
    const tokens = new CommonTokenStream(lexer);

    tokens.fill();
    let tokensArray = tokens.getTokens().map((token) => {
      return {
        type: lexer.vocabulary.getSymbolicName(token.type),
        value: token.text,
        line: token.line,
      };
    });

    return tokensArray;
  };

  return (
    <div className="">
      <Textarea
        className="h-full resize-none p-6 pl-8 pr-8 font-mono relative flex h-full w-full min-w-[600px] min-h-[400px]"
        onChange={(e) => tryParse(e.target.value)}
      ></Textarea>
    </div>
  );
}

export default Parser;
