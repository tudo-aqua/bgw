import { type ClassValue, clsx } from "clsx";
import { twMerge } from "tailwind-merge";
import {
  BooleanValue,
  ChoiceValue,
  ColorValue,
  NumberValue,
  StringValue,
} from "./components";

export function cn(...inputs: ClassValue[]) {
  return twMerge(clsx(inputs));
}

String.prototype.isEmpty = function () {
  return this.trim().length === 0;
};

export function isNumber(value?: string | number): boolean {
  return value != null && value !== "" && !isNaN(Number(value.toString()));
}

Array.prototype.takeRight = function (n: number) {
  return this.slice(this.length - n, this.length);
};

Array.prototype.fromRight = function (n: number) {
  return this[this.length - n - 1];
};

Array.prototype.excludeLast = function (n: number = 1) {
  return this.slice(0, this.length - n);
};

Array.prototype.moveLastToFirst = function () {
  if (this.length === 0) return this;
  const last = this.fromRight(0);
  return [last, ...this.excludeLast()];
};

String.prototype.capitalize = function () {
  return this.charAt(0).toUpperCase() + this.slice(1);
};

String.prototype.toTitleCase = function () {
  return this.split(" ")
    .map((word) => word.capitalize())
    .join(" ");
};

export function isListener(c: any) {
  return c.name.startsWith("on") && c.signature.indexOf("->") > 0;
}

export function createKotlinCodeLinebreaks(code: string, length: number = 100) {
  if (code.length < length) {
    return code.replace(/(\s+)?:(\s+)?/gm, " : ");
  }

  return formatAll(code);
}

export function normalizeKotlinCode(input: string): string {
  const lines = input.trim().split("\n");

  const normalizedLines: string[] = [];

  for (let i = 0; i < lines.length; i++) {
    // Remove leading/trailing whitespace
    let currentLine = lines[i].trim();

    // Skip truly empty lines
    if (!currentLine) {
      normalizedLines.push("");
      continue;
    }

    // Combine lines until we hit an empty line or semicolon
    let nextIndex = i + 1;
    while (nextIndex < lines.length) {
      const nextLine = lines[nextIndex].trim();
      if (!nextLine || currentLine.endsWith(";")) break;

      currentLine += " " + nextLine;
      i = nextIndex;
      nextIndex++;
    }

    // Normalize all whitespace to single spaces
    currentLine = currentLine.replace(/\s+/g, " ");
    normalizedLines.push(currentLine);
  }

  return normalizedLines.join("\n");
}

export function formatAll(input: string): string {
  let normalized = normalizeKotlinCode(input);
  return normalized.split("\n").map(formatKotlinCode).join("\n");
}

export function formatKotlinCode(input: string): string {
  input = input.trim();
  let formatted = "";
  let inString = false;
  let lastChar = "";
  let indentLevel = 0;
  const indentSize = 4;
  let currentLine = "";

  // Define matching bracket pairs
  const bracketPairs = {
    "(": ")",
    "{": "}",
    "[": "]",
  };

  // Enhanced bracket info including parent context
  const brackets: Array<{
    char: string;
    pos: number;
    context?: string; // Store context like 'apply', 'run', etc.
  }> = [];

  const addLine = (extra: string = "") => {
    if (currentLine.trim()) {
      if (formatted === "") {
        formatted += currentLine.trim() + extra + "\n"; // Do not indent the first line
      } else {
        formatted +=
          " ".repeat(indentLevel * indentSize) +
          currentLine.trim() +
          extra +
          "\n";
      }
    } else {
      formatted += "\n";
    }
    currentLine = "";
  };

  const isColorConstructor = (pos: number): boolean => {
    const beforeContent = input.slice(Math.max(0, pos - 6), pos).trim();
    if (!beforeContent.endsWith("Color")) return false;

    let j = pos + 1;
    let content = "";
    let depth = 1;

    while (j < input.length && depth > 0) {
      if (input[j] === "(") depth++;
      if (input[j] === ")") depth--;
      content += input[j];
      j++;
    }

    // Remove whitespace and check if it's a valid color format
    const params = content
      .slice(0, -1)
      .trim()
      .split(",")
      .map((p) => p.trim());

    // Valid formats:
    // Color(0xFF00FF00) - Single hex parameter
    // Color(255, 255, 255) - RGB
    // Color(255, 255, 255, 255) - RGBA
    return (
      (params.length === 1 && params[0].startsWith("0x")) ||
      (params.length === 3 && params.every((p) => !isNaN(Number(p)))) ||
      (params.length === 4 && params.every((p) => !isNaN(Number(p))))
    );
  };

  const isInsideColorConstructor = (): boolean => {
    if (brackets.length === 0) return false;
    const lastBracket = brackets[brackets.length - 1];
    return isColorConstructor(lastBracket.pos);
  };

  for (let i = 0; i < input.length; i++) {
    const char = input[i];

    // Handle strings
    if (char === '"' && lastChar !== "\\") {
      inString = !inString;
    }

    if (!inString) {
      // Check if next content is a generic or empty
      const isGenericOrEmpty = (pos: number): boolean => {
        let j = pos + 1;
        let content = "";
        while (
          j < input.length &&
          input[j] !== ")" &&
          input[j] !== "}" &&
          input[j] !== "]"
        ) {
          if (!input[j].match(/\s/)) content += input[j];
          j++;
        }
        return (
          content.length === 0 ||
          (content.length === 1 && content.match(/[A-Z]/))
        );
      };

      // Opening brackets
      if (Object.keys(bracketPairs).includes(char)) {
        const context = currentLine.trim().endsWith("apply")
          ? "apply"
          : undefined;
        brackets.push({ char, pos: i, context });

        if (isGenericOrEmpty(i) || isColorConstructor(i)) {
          currentLine += char;
        } else {
          currentLine += char;
          addLine();
          indentLevel++; // Moved after addLine() to increase indent for next line
        }
      }
      // Closing brackets
      else if (Object.values(bracketPairs).includes(char)) {
        if (brackets.length > 0) {
          const lastBracket = brackets[brackets.length - 1];
          if (bracketPairs[lastBracket.char] === char) {
            brackets.pop();
            const isSimpleBracket =
              isGenericOrEmpty(lastBracket.pos) ||
              isColorConstructor(lastBracket.pos);

            if (!isSimpleBracket && currentLine.trim()) {
              addLine();
              indentLevel--;
              currentLine = char;
            } else {
              currentLine += char;
            }
          }
        }
      }
      // Handle commas, comments and other line breaks
      else if (char === "," || char === ";") {
        currentLine += char;
        if (
          !isInsideColorConstructor() &&
          !currentLine.trim().startsWith(") :")
        ) {
          addLine();
        }
      } else if (char === "/" && lastChar === "*") {
        addLine("/");
      } else if (char === "\n") {
        addLine();
      } else {
        currentLine += char;
      }
    } else {
      currentLine += char;
    }

    lastChar = char;
  }

  // Add remaining content
  if (currentLine.trim()) {
    addLine();
  }

  return formatted.trim();
}

function reconstructSingleProperty(prop: any) {
  switch (prop.propType) {
    case "number":
      return NumberValue.fromJSON(prop);
    case "string":
      return StringValue.fromJSON(prop);
    case "boolean":
      return BooleanValue.fromJSON(prop);
    case "color":
      return ColorValue.fromJSON(prop);
    case "choice":
      return ChoiceValue.fromJSON(prop);
    default:
      return prop;
  }
}

export function reconstructProperties(component: any) {
  if (!component || typeof component !== "object") {
    return component;
  }

  Object.keys(component).forEach((key) => {
    const value = component[key];

    if (value && typeof value === "object") {
      if (value.propType) {
        // If it has propType, reconstruct this property
        component[key] = reconstructSingleProperty(value);
      } else {
        // Otherwise recursively process nested object
        component[key] = reconstructProperties(value);
      }
    }
  });

  return component;
}
// Database Configuration
export const idbConfig = {
  databaseName: "bgw-playground",
  version: 1,
  stores: [
    {
      name: "images",
      id: { keyPath: "name" },
      indices: [{ name: "name", keyPath: "name", options: { unique: true } }],
    },
    {
      name: "workspaces",
      id: { keyPath: "id" },
      indices: [{ name: "id", keyPath: "id", options: { unique: true } }],
    },
  ],
};

export const layoutMap = {
  animation: {
    title: "Animation",
    description: "Animations are a powerful tool to bring your app to life.",
    icon: "motion_play",
    package: "tools.aqua.bgw.animation",
    module: "bgw-gui",
  },
  components: {
    title: "Components",
    description: "Components are the building blocks of your BGW app.",
    icon: "shapes",
    package: "tools.aqua.bgw.components",
    module: "bgw-gui",
  },
  container: {
    title: "Container",
    description:
      "Containers are used to layout and align your game components.",
    icon: "stacks",
    package: "tools.aqua.bgw.components.container",
    module: "bgw-gui",
  },
  gamecomponentviews: {
    title: "GameComponentViews",
    description: "GameComponentViews are used to display your game components.",
    icon: "playing_cards",
    package: "tools.aqua.bgw.components.gamecomponentviews",
    module: "bgw-gui",
  },
  layoutviews: {
    title: "LayoutViews",
    description: "LayoutViews are used to layout and align your ui components.",
    icon: "view_carousel",
    package: "tools.aqua.bgw.components.layoutviews",
    module: "bgw-gui",
  },
  uicomponents: {
    title: "UIComponents",
    description: "UIComponents are used to build your UI.",
    icon: "buttons_alt",
    package: "tools.aqua.bgw.components.uicomponents",
    module: "bgw-gui",
  },
  core: {
    title: "Core",
    description: "Includes core classes, functions and constants.",
    icon: "token",
    package: "tools.aqua.bgw.core",
    module: "bgw-gui",
  },
  dialog: {
    title: "Dialog",
    description: "Dialogs are popups that can be used to display information.",
    icon: "wysiwyg",
    package: "tools.aqua.bgw.dialog",
    module: "bgw-gui",
  },
  event: {
    title: "Event",
    description: "Events are used to handle user interactions.",
    icon: "web_traffic",
    package: "tools.aqua.bgw.event",
    module: "bgw-gui",
  },
  io: {
    title: "IO",
    description: "IO classes are used to read and write files.",
    icon: "save",
    package: "tools.aqua.bgw.io",
    module: "bgw-gui",
  },
  observable: {
    title: "Observable",
    description: "Observables are used to listen for changes in your data.",
    icon: "visibility",
    package: "tools.aqua.bgw.observable",
    module: "bgw-gui",
  },
  lists: {
    title: "Lists",
    description:
      "Observable lists are used to store data as a list and observe changes.",
    icon: "format_list_numbered",
    package: "tools.aqua.bgw.observable.lists",
    module: "bgw-gui",
  },
  properties: {
    title: "Properties",
    description: "Properties are used to store data and observe changes.",
    icon: "123",
    package: "tools.aqua.bgw.observable.properties",
    module: "bgw-gui",
  },
  style: {
    title: "Style",
    description: "Style classes are used to style your components.",
    icon: "style",
    package: "tools.aqua.bgw.style",
    module: "bgw-gui",
  },
  util: {
    title: "Util",
    description: "Util classes are used to perform common tasks.",
    icon: "function",
    package: "tools.aqua.bgw.util",
    module: "bgw-gui",
  },
  visual: {
    title: "Visual",
    description: "Visual classes are used to give your components color.",
    icon: "colors",
    package: "tools.aqua.bgw.visual",
    module: "bgw-gui",
  },
  annotations: {
    title: "Annotations",
    description:
      "Network annotations are used to annotate network message receivers.",
    icon: "alternate_email",
    package: "tools.aqua.bgw.net.common.annotations",
    module: "bgw-net",
  },
  client: {
    title: "Client",
    description: "The client is used to communicate with the BGW-Net server.",
    icon: "cast",
    package: "tools.aqua.bgw.net.client",
    module: "bgw-net",
  },
  common: {
    title: "Common",
    description: "Common classes are used to define network messages.",
    icon: "device_hub",
    package: "tools.aqua.bgw.net.common",
    module: "bgw-net",
  },
  message: {
    title: "Message",
    description:
      "Messages are used to send custom game messages between clients.",
    icon: "message",
    package: "tools.aqua.bgw.net.common.message",
    module: "bgw-net",
  },
  notification: {
    title: "Notification",
    description: "Notifications are used to send alerts between clients.",
    icon: "mark_chat_unread",
    package: "tools.aqua.bgw.net.common.notification",
    module: "bgw-net",
  },
  request: {
    title: "Request",
    description:
      "Requests are used to send predefined framework messages to clients.",
    icon: "keyboard_double_arrow_right",
    package: "tools.aqua.bgw.net.common.request",
    module: "bgw-net",
  },
  response: {
    title: "Response",
    description: "Responses are used to receive data from clients.",
    icon: "keyboard_double_arrow_left",
    package: "tools.aqua.bgw.net.common.response",
    module: "bgw-net",
  },
};

export const guideStructure = {
  "Discover BoardGameWork": {
    icon: "school",
    items: [
      { title: "Installation", url: "/guides/installation" },
      { title: "Getting Started", url: "/guides/getting-started" },
      { title: "Declaring Scenes", url: "/guides/declaring-scenes" },
      { title: "Using Components", url: "/guides/using-components" },
      { title: "Handling Events", url: "/guides/handling-events" },
    ],
    dir: "",
  },
  Components: {
    icon: "widgets",
    items: [
      {
        title: "ComponentView",
        url: "/guides/components/componentview",
      },
      {
        title: "DynamicComponentView",
        url: "/guides/components/dynamiccomponentview",
      },
      {
        title: "Game Components",
        url: "/guides/components/gamecomponents",
      },
      {
        title: "UI Components",
        url: "/guides/components/uicomponents",
      },
      { title: "Layouts", url: "/guides/components/layout" },
      { title: "Container", url: "/guides/components/container" },
    ],
    dir: "components",
  },
  Concepts: {
    icon: "lightbulb",
    items: [
      { title: "Animations", url: "/guides/concepts/animations" },
      {
        title: "Drag and Drop",
        url: "/guides/concepts/drag-and-drop",
      },
      { title: "Observable", url: "/guides/concepts/observable" },
      { title: "User Input", url: "/guides/concepts/user-input" },
      { title: "Visual", url: "/guides/concepts/visual" },
      {
        title: "Advanced Usage",
        url: "/guides/concepts/advanced-usage",
      },
    ],
    dir: "concepts",
  },
  Dialogs: {
    icon: "chat",
    items: [
      { title: "Dialog", url: "/guides/dialogs/dialog" },
      { title: "File Dialog", url: "/guides/dialogs/file-dialog" },
    ],
    dir: "dialogs",
  },
  Util: {
    icon: "build",
    items: [
      {
        title: "Bidirectional Map",
        url: "/guides/util/bidirectionalmap",
      },
      { title: "Coordinate", url: "/guides/util/coordinate" },
      { title: "Stack", url: "/guides/util/stack" },
    ],
    dir: "util",
  },
  Network: {
    icon: "lan",
    items: [
      {
        title: "Communication",
        url: "/guides/network/communication",
      },
      { title: "JSON", url: "/guides/network/json" },
    ],
    dir: "network",
  },
};
