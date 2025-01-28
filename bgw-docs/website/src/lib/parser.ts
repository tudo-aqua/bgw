import * as Components from "@/lib/components.ts";
import {Badge} from "@/components/ui/badge.tsx";
import React, {useState} from "react";
import {Textarea} from "@/components/ui/textarea.tsx";
import { CharStream, CommonTokenStream } from "antlr4ng";
import { KotlinLexer } from "@/generated/KotlinLexer.ts";
import {TokenSource} from "antlr4/src/antlr4/TokenSource";
import {colourNameToHex, constructors, enums} from "@/lib/constructors.ts";
import {isNumber} from "@/lib/utils.ts";
import {ColorValue, ColorVisualData, CompoundVisualData, SingleLayerVisualData} from "@/lib/components.ts";

type Token = {
    type: string,
    value: string,
    line: number
}

type Assignment = {
    property: string|number,
    value: string
}

type ParserProps = {
    onCodeChange: (result : object) => void
}

export class CodeParser {

    constructor() {
    }

    tryParse(value: string) {
        const tokens = this.getKotlinTokens(value).filter((token) => {
            let simpleTokenSplit = token.type.split("_")
            let simpleToken = simpleTokenSplit[simpleTokenSplit.length - 1].toLowerCase()
            return simpleToken != "ws" && token.type.toLowerCase().indexOf("comment") == -1 && token.type != "Inside_NL"
        });

        let separatedTokens = this.getTokensStartingWithValOrVar(tokens);
        console.warn("BY VAL SEPERATED TOKENS >>>>>", separatedTokens)

        let result: object[] = [];
        separatedTokens.forEach((tokens) => {
            if (tokens.length == 0) return;
            let allAssignments = this.getConstructorArguments(tokens)
            let assignments = allAssignments.constructor;
            let applys = allAssignments.apply;

            let assignment = tokens.findIndex((token) => {
                return token.type == "ASSIGNMENT"
            });

            console.info("FOUND CONSTRUCTOR ASSIGNMENTS >>>>>", {...assignments})
            console.info("FOUND APPLYS >>>>>", {...applys})

            if (assignment == -1) return;
            if (assignment == 0 || assignment == tokens.length - 1) return;
            let component: Assignment = {
                property: tokens[assignment - 1].value,
                value: tokens[assignment + 1].value
            };
            const constructor = constructors[component.value]
            if (!constructor) return;

            if (!this.checkNaming(assignments)) return;
            console.log("AFTER CHECK NAMING >>>>>", {...assignments})

            assignments.forEach((assignment) => {
                if (isNumber(assignment.property)) {
                    assignment.property = constructor[assignment.property].property
                }
            })
            let distinctAssignments = {}
            for (const assignment of assignments) {
                distinctAssignments[assignment.property] = assignment.value;
            }
            if (assignments.length != Object.keys(distinctAssignments).length) return

            let object = new Components.DataClasses[component.value + "Data"]()
            object.name.value = component.property

            for (const propertyName in distinctAssignments) {
                let constructorProperty = constructors[component.value].find((constructor) => constructor.property == propertyName)
                let typedValue = this.getTypedValue(distinctAssignments[propertyName], constructorProperty)

                if (constructorProperty && constructorProperty.primitive) {
                    object[propertyName].value = typedValue
                } else if (constructorProperty && constructorProperty.type == "Alignment") {
                    let value = distinctAssignments[propertyName].replace("Alignment.", "").toUpperCase()
                    let enumValues = enums[constructorProperty.type][value]

                    if (enumValues) {
                        enumValues.forEach((enumValue) => {
                            object[enumValue.property].value = enumValue.value
                        })
                    }
                } else if (constructorProperty && constructorProperty.type == "Visual") {
                    object[propertyName] = this.getCompoundVisual(this.getKotlinTokens(distinctAssignments[propertyName]), true)
                } else if (!constructorProperty) {
                    return
                }
            }

            let distinctApplys = {}
            for (const apply of applys) {
                distinctApplys[apply.property] = apply.value;
            }

            for (const propertyName in distinctApplys) {
                let constructorProperty = constructors[component.value].find((constructor) => constructor.property == propertyName)
                let typedValue = this.getTypedValue(distinctApplys[propertyName], constructorProperty)

                if (constructorProperty && constructorProperty.primitive) {
                    object[propertyName].value = typedValue
                } else if (constructorProperty && constructorProperty.type == "Alignment") {
                    let value = distinctApplys[propertyName].replace("Alignment.", "").toUpperCase()
                    let enumValues = enums[constructorProperty.type][value]

                    if (enumValues) {
                        enumValues.forEach((enumValue) => {
                            object[enumValue.property].value = enumValue.value
                        })
                    }
                } else if (!constructorProperty) {
                    if (object[propertyName] && object[propertyName].propType) {
                        let typedValue = this.getTypedValueApply(distinctApplys[propertyName], object[propertyName].propType)
                        object[propertyName].value = typedValue
                    }
                }
            }


            result.push(object)
        })

        console.log("ENDRESULT >>>>>", result)
        return result
    }

    getCompoundVisual(tokens: Token[], topLayer : boolean = true) {
        const filteredTokens = tokens.filter((token) => {
            let simpleTokenSplit = token.type.split("_")
            let simpleToken = simpleTokenSplit[simpleTokenSplit.length - 1].toLowerCase()
            return simpleToken != "ws" && token.type.toLowerCase().indexOf("comment") == -1 && simpleToken != "nl"
        });

        if (filteredTokens.length == 0) return null;
        if(filteredTokens[0].value == "Visual") {
            let completeString = filteredTokens.filter((token) => token.type === "Identifier" || token.type === "DOT").map((token) => token.value).join("")
            if(completeString == "Visual.EMPTY") {
                let comp = new CompoundVisualData()
                comp.children = [new ColorVisualData()];
                (comp.children[0] as ColorVisualData).color = "#ffffff"
                comp.children[0].transparency.value = 0
                return comp
            }
            return null
        } else if (filteredTokens[0].value == "CompoundVisual") {
            const compoundAssignments = this.getConstructorArguments(filteredTokens).constructor
            console.log("COMPOUND VISUAL >>>>>", compoundAssignments)
            const compoundVisuals: SingleLayerVisualData[] = compoundAssignments.map((assignment) => {
                return this.getCompoundVisual(this.getKotlinTokens(assignment.value), false)
            })
            const compoundVisual = new CompoundVisualData()
            compoundVisual.children = compoundVisuals
            console.log("COMPOUND VISUAL OBJECT >>>>>", compoundVisual)
            return compoundVisual
        } else if (filteredTokens[0].value == "ColorVisual") {
            console.log("COLOR VISUAL >>>>>", this.getConstructorArguments(filteredTokens).constructor)
            let colorVisualArguments = this.getConstructorArguments(filteredTokens).constructor
            let colorVisual = new ColorVisualData()
            if (colorVisualArguments.length == 1) {
                colorVisual.color = this.getColor(this.getKotlinTokens(colorVisualArguments[0].value))
            } else if (colorVisualArguments.length == 0) {
                let exists = this.checkIfConExists(filteredTokens)
                if(!exists) {
                    colorVisual.color = "#ffffff"
                }
                
                let colorEnumIndex = filteredTokens.reverse().findIndex((token) => token.type == "Identifier")
                let colorEnum = filteredTokens[colorEnumIndex].value
                colorVisual.color = colourNameToHex(colorEnum.toLowerCase())
            }

            console.log("COLOR VISUAL OBJECT >>>>>", colorVisual, topLayer)
            if (topLayer == true) {
                let comp = new CompoundVisualData()
                comp.children = [colorVisual]
                return comp
            }
            return colorVisual
        }
    }
    
    checkIfConExists(tokens: Token[]) {
        let completeString = tokens.filter((token) => token.type === "Identifier" || token.type === "DOT").map((token) => token.value).join("")
        let availableConstructors = window.conData.filter((con) => con.fullName == completeString)
        return availableConstructors.length > 0
    }

    getColor(tokens: Token[]) {
        const filteredTokens = tokens.filter((token) => {
            let simpleTokenSplit = token.type.split("_")
            let simpleToken = simpleTokenSplit[simpleTokenSplit.length - 1].toLowerCase()
            return simpleToken != "ws" && token.type.toLowerCase().indexOf("comment") == -1 && simpleToken != "nl"
        });

        if (filteredTokens.length == 0) return "#ffffff";

        let exists = this.checkIfConExists(filteredTokens)
        if(!exists) {
            return "#ffffff"
        }
        
        if (filteredTokens[0].value == "Color") {
            const colorAssignments = this.getConstructorArguments(filteredTokens).constructor
            console.log("COLOR >>>>>", colorAssignments)
            switch (colorAssignments.length) {
                case 0:
                    let colorEnumIndex = filteredTokens.reverse().findIndex((token) => token.type == "Identifier")
                    let colorEnum = filteredTokens[colorEnumIndex].value
                    console.log("COLOR ENUM >>>>>", colorEnum)
                    return colourNameToHex(colorEnum.toLowerCase())
                case 1:
                    return colorAssignments[0].value.replace("0x", "#")
                case 3:
                    return this.convertRGBToHex(`rgb(${colorAssignments[0].value},${colorAssignments[1].value},${colorAssignments[2].value})`)
                case 4:
                    return this.convertRGBAToHex(`rgba(${colorAssignments[0].value},${colorAssignments[1].value},${colorAssignments[2].value},${colorAssignments[3].value})`)
                default:
                    return "#ffffff"
            }
        } else {
            return "#ffffff";
        }
    }

    convertRGBAToHex(rgba: string) {
        let values = rgba.replace("rgba(", "").replace(")", "").split(",")
        let hex = "#"
        for (let i = 0; i < 3; i++) {
            let value = parseInt(values[i]).toString(16)
            hex += value.length == 1 ? "0" + value : value
        }
        return hex
    }

    convertRGBToHex(rgb: string) {
        let values = rgb.replace("rgb(", "").replace(")", "").split(",")
        let hex = "#"
        for (let i = 0; i < 3; i++) {
            let value = parseInt(values[i]).toString(16)
            hex += value.length == 1 ? "0" + value : value
        }
        return hex
    }


    getTypedValueApply(value: string, type: string) {
        if (type == "string") {
            return eval(value)
            return value.replace(/"/g, "")
        } else if (type == "number") {
            return isNumber(parseFloat(eval(value))) ? parseFloat(eval(value)) : 0
        } else if (type == "boolean") {
            return value == "true"
        }

        return value
    }

    getTypedValue(value: string, con: object) {
        if (!con) return value

        if (con.primitive) {
            if (con.type == "string") {
                return eval(value)
                //return value.replace(/"/g, "")
            } else if (con.type == "number") {
                return isNumber(parseFloat(eval(value))) ? parseFloat(eval(value)) : 0
            } else if (con.type == "boolean") {
                return value == "true"
            }
        }
        return value
    }

    checkNaming(assignments: Assignment[]): boolean {
        let named = false
        let result = true
        assignments.forEach((assignment, index) => {
            if (isNumber(assignment.property)) {
                if (index < assignments.length - 1 && assignment.value.isEmpty()) {
                    result = false
                } else if (!(index == assignments.length - 1 && assignment.value.isEmpty()) && named) {
                    result = false
                } else if (index == assignments.length - 1 && assignment.value.isEmpty()) {
                    assignments.pop()
                }
            } else {
                named = true
            }
        })
        return result
    }

    getConstructorArguments(tokens: Token[]) {
        let constructorResult: Assignment[] = [];
        let applyResult: Assignment[] = [];
        let currentAssignment: Assignment = {
            property: "",
            value: ""
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
                    currentAssignment.property = constructorResult.length
                }
            }

            let rightSideEnded = (tokens[i].type == "COMMA" && parenthesisCount == 1) || parenthesisCount == 0;
            if (rightSideEnded) {
                rightSide = false
                constructorResult.push(currentAssignment);
                currentAssignment = {
                    property: "",
                    value: ""
                };
            } else {
                currentAssignment.value += tokens[i].value;
            }

            if (parenthesisCount == 0) {
                return {
                    constructor: constructorResult,
                    apply: this.getApplyArguments(tokens, i + 1)
                }
            }
        }

        return {
            constructor: constructorResult,
            apply: applyResult
        }
    }

    getApplyArguments(tokens: Token[], start: number) {
        let result: Assignment[] = [];
        let currentAssignment: Assignment = {
            property: "",
            value: ""
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

            let rightSideEnded = ((tokens[i].type == "SEMICOLON" || tokens[i].value == "\n") && parenthesisCount == 1) || parenthesisCount == 0;
            if (rightSideEnded) {
                rightSide = false;
                currentAssignment.property = currentAssignment.property.replace(/\n/gm, "").replace("this.", "")
                currentAssignment.value = currentAssignment.value.replace(/\n/gm, "")
                result.push(currentAssignment);
                currentAssignment = {
                    property: "",
                    value: ""
                };
            } else {
                currentAssignment.value += tokens[i].value;
            }

            if (parenthesisCount == 0) {
                return result.concat(this.getApplyArguments(tokens, i + 1))
            }
        }

        return result;
    }

    getTokensStartingWithValOrVar(tokens: Token[]) {
        const result = [];
        let currentTokens = [];

        for (let i = 0; i < tokens.length; i++) {
            if (tokens[i].type === 'VAL' || tokens[i].type === 'VAR') {
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
    }

    getAssignments(tokens: Token[]) {
        const assignments = [];
        for (let i = 0; i < tokens.length; i++) {
            if (tokens[i].type == 'ASSIGNMENT') {
                const left = tokens[i - 1];
                const right = []
                let j = i + 1;

                while (j < tokens.length && tokens[j].type !== 'SEMICOLON' && tokens[j].type !== 'NL' && tokens[j].type !== 'EOF' && tokens[j].type !== 'COMMA') {
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
    }

    getKotlinTokens(code: string) {
        const chars = CharStream.fromString(code);
        const lexer = new KotlinLexer(chars);
        const tokens = new CommonTokenStream(lexer);

        tokens.fill()
        let tokensArray = tokens.getTokens().map((token) => {
            return {
                type: lexer.vocabulary.getSymbolicName(token.type),
                value: token.text,
                line: token.line
            }
        })

        console.log(tokensArray);

        return tokensArray;
    }
}