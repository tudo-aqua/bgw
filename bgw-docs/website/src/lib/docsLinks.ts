import { constructors } from "@/main";

const guideForType: Record<string, string> = {
  GridElementData: "components/layout",
  HexagonGridElementData: "components/container",
  SliderData: "components/uicomponents",
};

function guideForObjectTypes(objectType: string[] = []) {
  if (objectType.includes("GameComponentContainerData")) return "components/container";
  if (objectType.includes("GameComponentViewData")) return "components/gamecomponents";
  if (objectType.includes("LayoutViewData")) return "components/layout";
  if (objectType.includes("UIComponentData")) return "components/uicomponents";
  return "components/componentview";
}

// Links to the API docs of a playground component, or to the guide covering it when the class is not documented.
export function docsLinkFor(type: string, objectType?: string[]) {
  const name = type.replace(/Data$/, "");
  const entry = (constructors ?? []).find((candidate: { shortName: string; importName: string }) => candidate.shortName === name);
  if (entry) {
    const segments = entry.importName.split(".");
    return `/docs/${segments.slice(0, -1).join(".")}/${segments.at(-1)}`;
  }
  return `/guides/${guideForType[type] ?? guideForObjectTypes(objectType)}`;
}

export const sceneDocsLink = "/docs/tools.aqua.bgw.core/BoardGameScene";
