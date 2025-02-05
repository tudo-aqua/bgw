// const processedTypes = new Map();

const kotlinBaseTypes = new Set([
  "Any",
  "Unit",
  "Nothing",
  "Boolean",
  "Byte",
  "Short",
  "Int",
  "Long",
  "Float",
  "Double",
  "Char",
  "String",
  "Array",
  "List",
  "Set",
  "Map",
  "Iterable",
  "Sequence",
  "Collection",
  "Pair",
  "Triple",
  "Range",
  "IntRange",
  "LongRange",
  "CharRange",
  "MutableList",
  "MutableSet",
  "MutableMap",
  "ArrayList",
  "HashMap",
  "HashSet",
]);

function normalizeType(type) {
  return type.replace(/\s+/g, " ").trim();
}

function processTypeLinks($, paramEl, rawType, breadcrumbs = []) {
  const normalizedType = normalizeType(rawType);
  let typeText = rawType;
  const links = new Map();
  const processedRanges = new Set(); // Track regions that have been processed

  // Collect all unique links and their positions
  paramEl.find("a").each((_, link) => {
    const $link = $(link);
    const text = $link.text();
    const href = $link.attr("href");

    // Find all occurrences of this text in typeText
    let pos = 0;
    const regex = new RegExp(`\\b${text}\\b`, "g");
    let match;
    while ((match = regex.exec(typeText)) !== null) {
      if (
        !Array.from(processedRanges).some(
          (range) => match.index >= range[0] && match.index <= range[1]
        )
      ) {
        links.set(`${text}:${match.index}`, {
          text,
          href,
          index: match.index,
          length: text.length,
        });
      }
    }
  });

  // Sort links by position (from end to start to preserve indices)
  const sortedLinks = Array.from(links.values()).sort(
    (a, b) => b.index - a.index
  );

  // Replace links
  for (const link of sortedLinks) {
    const { text, href, index, length } = link;
    const newLink = pathToPackage(convertDashesToCaps(href), breadcrumbs);

    if (newLink.trim().length === 0) {
      continue;
    }

    const before = typeText.substring(0, index);
    const after = typeText.substring(index + length);
    typeText = `${before}[${text}](${newLink})${after}`;

    // Mark this range as processed
    processedRanges.add([index, index + length]);
  }

  return typeText;
}

function convertDashesToCaps(str) {
  if (str.startsWith("http")) {
    return str;
  }

  if (str.endsWith(".html")) {
    str = str.replace(".html", "");
  }

  if (str === "index") {
    str = "_index";
  }

  // Replace patterns like "-x" with uppercase "X"
  let converted = str.replace(/-[a-z]/g, (match) =>
    match.charAt(1).toUpperCase()
  );

  // Remove any remaining dashes
  converted = converted.replace(/-/g, "");

  // if (converted === "Companion") {
  //   converted = "_companion";
  // }

  return converted;
}

function pathToPackage(path, breadcrumbs) {
  if (path.startsWith("http")) {
    console.log("##############", path);
    return path;
  }

  if (breadcrumbs.indexOf("ComponentView") !== -1) {
    console.log("path", path);
  }

  path = path.replace(/\.html.+$/, "");

  if (path.indexOf("/") === -1 && path !== "_index") {
    console.log("breadcrumbs", breadcrumbs);
    return [...breadcrumbs, path]
      .join("/")
      .replace(/(\/|_)?index/g, "")
      .replace(/(\/|\.)$/g, "");
  } else if (path === "_index") {
    return breadcrumbs
      .join("/")
      .replace(/(\/|_)?index/g, "")
      .replace(/(\/|\.)$/g, "");
  }

  if (
    breadcrumbs &&
    breadcrumbs.length > 1 &&
    breadcrumbs[breadcrumbs.length - 1] === breadcrumbs[breadcrumbs.length - 2]
  ) {
    breadcrumbs.pop();
  }

  // path = path.replace(/(\/|_)?index/g, "");

  if (path.indexOf("/") === -1) {
    let withoutLast = breadcrumbs.slice(0, -1);
    let ret = withoutLast.join("/") + "/" + path;
    return ret.replace(/(\/|_)?index/g, "").replace(/(\/|\.)$/g, "");
  }

  let breadcrumbIndex = breadcrumbs.length - 1;
  while (path.startsWith("../") && breadcrumbIndex >= 0) {
    path = path.replace("../", "");
    breadcrumbIndex--;
  }

  if (
    path.startsWith("../") &&
    path.replace(/\.\.\//g, "").startsWith("tools.aqua")
  ) {
    path = path.replace(/\.\.\//g, "");
  }

  // Add remaining breadcrumbs to the path
  for (let i = 0; i <= breadcrumbIndex; i++) {
    path = `${breadcrumbs[i]}/${path}`;
  }

  path = path.replace(/\/$/g, ""); // Remove trailing slash

  if (
    !path.split("/")[0].startsWith("tools.aqua") &&
    path.indexOf("tools.aqua") !== -1
  ) {
    let split = path.split("/");
    let index = split.findIndex((s) => s.startsWith("tools.aqua"));
    let beforeAndIncluding = split.slice(0, index + 1).reverse();
    let after = split.slice(index + 1);
    path = [...beforeAndIncluding, ...after].join("/");
  }

  return path.replace(/(\/|_)?index/g, "").replace(/(\/|\.)$/g, "");
}

module.exports = {
  normalizeType,
  processTypeLinks,
  convertDashesToCaps,
  pathToPackage,
};
