export type ConstructorAPIImportInfo = {
    importName : string;
    fullName : string;
    shortName : string;
    returnType : string;
    parameters : ConstructorAPIPair[];
    constructors : object[];
    properties : ConstructorAPIPair[];
    icon : "class" | "property" | "method";
}

export type ConstructorAPIPair = {
    first: string;
    second: string;
}

export type Guides = {
    id : string,
    color: string, 
    x : number,
    y : number,
    width : number,
    height : number
}