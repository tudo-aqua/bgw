const fs = require("fs");
const path = require("path");
const cheerio = require("cheerio");
const { deprecate } = require("util");
const {
  normalizeType,
  processTypeLinks,
  convertDashesToCaps,
  pathToPackage,
} = require("./utils");

let rootDir = "./example/htmlPartial";

function loadPackageList(filename) {
  const packageList = fs.readFileSync(
    "./example/htmlPartial/" + filename,
    "utf8"
  );

  let lines = packageList.split("\n").map((line) => line.trim());
  lines = lines.filter((line) => line.length > 0 && !line.startsWith("$"));

  return lines;
}

function loadAllPackages() {
  const bgwGui = loadPackageList("package-list");
  const bgwClient = loadPackageList("package-list-client");
  const bgwCommon = loadPackageList("package-list-common");

  return [...bgwGui, ...bgwClient, ...bgwCommon];
}

let validPackages = loadAllPackages();

function getDirectoryStructure(dir) {
  let results = {};
  const list = fs.readdirSync(dir);

  list.forEach((file) => {
    const filePath = path.join(dir, file);
    const stat = fs.statSync(filePath);

    if (stat && stat.isDirectory()) {
      results[file] = getDirectoryStructure(filePath);
    } else {
      const content = fs.readFileSync(filePath, "utf8");
      const $ = cheerio.load(content);
      const breadcrumbs = $(".breadcrumbs").text().trim().split("/");
      breadcrumbs.shift();
      const tags = [];

      $("button.platform-tag").each((i, elem) => {
        tags.push($(elem).text());
      });

      let isClass = file.startsWith("-");
      if (
        tags.length > 0 &&
        (tags.includes("jvm") || tags.includes("common"))
      ) {
        if (breadcrumbs.some((crumb) => validPackages.includes(crumb))) {
          results[file] = {
            _dirtype: "file",
            path: filePath,
            breadcrumbs: breadcrumbs,
            package: breadcrumbs.join("."),
            tags: tags.length > 0 ? tags : [],
            isClass: isClass,
            path: filePath,
          };
        } else {
          results[file] = null;
        }
      }
    }
  });

  return results;
}

Array.prototype.last = function () {
  return this[this.length - 1];
};

Array.prototype.fromRight = function (index) {
  if (index > this.length) return null;
  return this[this.length - index];
};

function extractLinksFromSignature($, signature, breadcrumbs = []) {
  const links = {};

  $(signature)
    .find("a")
    .each((i, el) => {
      const link = $(el).attr("href");
      const text = $(el).text().trim();

      let finalLink = pathToPackage(convertDashesToCaps(link), breadcrumbs);
      let splitFinal = finalLink.split("/");
      if (splitFinal.last() === splitFinal.fromRight(2)) {
        splitFinal.pop();
      }
      finalLink = splitFinal.join("/");

      if (finalLink.trim().length === 0) {
        return;
      }

      links[text] = finalLink;
    });

  return links;
}

// ---

function parseConstructor($, el, tree) {
  let constructorText = $(el).text().trim();
  let params = [];
  let briefElement = $(el).closest(".table-row").find(".brief");
  let brief = briefElement.text().trim();

  // Detect if this is a secondary constructor
  const isSecondaryConstructor = constructorText.startsWith("constructor");

  // Remove 'constructor' keyword for secondary constructors
  const constructor = isSecondaryConstructor
    ? constructorText.replace("constructor", "").trim()
    : constructorText;

  function findConstructorParams($, param) {
    let result = "";
    $(".platform-hinted.with-platform-tabs").each((i, el) => {
      $(el)
        .find(".table")
        .first()
        .find(".table-row")
        .each((i, row) => {
          const paramName = $(row).find(".main-subrow.keyValue div").first();
          const paramDesc = $(row).find(".main-subrow.keyValue div").last();
          if (paramName.text().trim() == param) {
            result = paramDesc;
          }
        });
    });

    return result;
  }

  const paramMatch = constructor.match(/\((.*)\)/);
  if (paramMatch && paramMatch[1]) {
    params = paramMatch[1].split(",").map((param) => {
      const paramEl = $(el).find(`*:contains("${param.trim()}")`).first();
      const parts = param.trim().split(":");
      const nameAndModifier = parts[0].trim();

      // For secondary constructors, we don't expect val/var modifiers
      const isVal =
        !isSecondaryConstructor && nameAndModifier.startsWith("val ");
      const isVar =
        !isSecondaryConstructor && nameAndModifier.startsWith("var ");
      const modifier = isVal ? "val" : isVar ? "var" : "";

      const name = nameAndModifier.replace(/^(val|var)\s+/, "");

      let brief = findConstructorParams($, name);
      let processedBrief = processTypeLinks(
        $,
        brief,
        brief.text().trim(),
        tree.breadcrumbs
      );

      let type = parts[1] ? parts[1].trim() : "";
      let defaultValue = "";

      if (type) {
        type = processTypeLinks($, paramEl, type, tree.breadcrumbs);
      }

      const defaultMatch = type.match(/\s*=\s*(.+)$/);
      if (defaultMatch) {
        defaultValue = defaultMatch[1];
        type = type.replace(/\s*=\s*.+$/, "").trim();
      }

      return {
        modifier,
        name,
        type,
        defaultValue,
        doc: processedBrief || "",
      };
    });
  }

  const processedBrief = processTypeLinks(
    $,
    briefElement,
    brief,
    tree.breadcrumbs
  );

  return {
    signature: constructorText, // Use original text to preserve 'constructor' keyword
    signatureLinks: extractLinksFromSignature($, el, tree.breadcrumbs),
    parameters: params,
    doc: processedBrief || "",
    isSecondaryConstructor, // Add flag to indicate constructor type
  };
}

function parseType($, el, tree) {
  const type = $(el).find("a").first().text().trim();
  const link = $(el).find("a").first().attr("href");

  const doc = $(el).closest(".table-row").find(".brief").text().trim();

  return {
    name: type,
    doc: processTypeLinks($, $(el), doc, tree.breadcrumbs),
    link: pathToPackage(convertDashesToCaps(link), tree.breadcrumbs),
  };
}

function parseProperty($, el, tree) {
  try {
    if (!$ || !el) {
      console.warn("Invalid input to parseProperty");
      return null;
    }

    const property = $(el).text().trim();
    const briefElement = $(el).closest(".table-row").find(".brief");
    const link = $(el).find("a").first().attr("href");
    const parsedLink = pathToPackage(
      convertDashesToCaps(link),
      tree.breadcrumbs
    );
    const brief = briefElement.text().trim();

    // Extract modifier (val/var), name and type (including possible default value)
    const match = property.match(
      /^(?:(?:abstract|override|open|private|protected|public|internal|const)\s+)*(val|var)?\s*(\w+):\s*(.+)$/
    );
    if (!match) {
      console.warn(`Could not parse Kotlin property: ${property}`);
      return null;
    }

    const [, modifier, name, rawType] = match;

    // Extract default value if present
    const defaultMatch = rawType.match(/\s*=\s*(.+)$/);
    const defaultValue = defaultMatch ? defaultMatch[1] : "";
    const type = rawType.replace(/\s*=\s*.+$/, "").trim();

    const result = {
      signature: property,
      signatureLinks: extractLinksFromSignature($, el, tree.breadcrumbs),
      modifier: modifier || "",
      name,
      link: parsedLink,
      type: processTypeLinks($, $(el), type, tree.breadcrumbs),
      defaultValue,
      doc: processTypeLinks($, briefElement, brief, tree.breadcrumbs),
    };

    if (!result.name || !result.type) {
      console.warn("Invalid Kotlin property parsing result:", result);
      return null;
    }

    return result;
  } catch (error) {
    console.error("Error parsing Kotlin property:", error);
    return null;
  }
}

function parseFunction($, el, tree) {
  try {
    if (!$ || !el) {
      console.warn("Invalid input to parseFunction");
      return null;
    }

    const func = $(el).text().trim();
    const briefElement = $(el).next(".brief");
    const link = $(el).find("a").first().attr("href");
    const parsedLink = pathToPackage(
      convertDashesToCaps(link),
      tree.breadcrumbs
    );
    const brief = briefElement.length ? briefElement.text().trim() : "";

    // const functionPattern =
    //   /^(?:(?:suspend|private|protected|public|internal|inline|open|abstract|override|operator)\s+)*fun\s+(?:[\w<>]+\.)?(\w+)\s*(?:<[^>]+>)?\s*\((.*?)\)(?:\s*:\s*(.+))?$/s;
    const functionPattern =
      /^(?:(?:suspend|private|protected|public|internal|inline|abstract|override|operator|open)\s+)*fun\s+(\w+)\s*(?:<[^>]+>)?\s*\((.*?)\)(?:\s*:\s*(.+))?$/s;
    const match = func.replace(/^@Synchronized/, "").match(functionPattern);

    if (!match) {
      console.warn(`Could not parse Kotlin function: ${func}`);
      return null;
    }

    if (
      func.includes("toFront") ||
      func.includes("toBack") ||
      func.includes("setZIndex")
    ) {
      console.warn(`Could not parse Kotlin function: ${func}`);
      return null;
    }

    const [, name, params, returnType] = match;

    if (!params.trim()) {
      return {
        signature: func,
        signatureLinks: extractLinksFromSignature($, el, tree.breadcrumbs),
        name,
        link: parsedLink,
        parameters: [],
        returnType: returnType
          ? processTypeLinks($, $(el), returnType.trim(), tree.breadcrumbs)
          : "Unit",
        doc: processTypeLinks($, briefElement, brief, tree.breadcrumbs),
      };
    }

    // Enhanced Kotlin parameter parsing
    function splitTopLevelParams(paramsStr) {
      const result = [];
      let current = "";
      let depth = 0;

      for (let char of paramsStr) {
        if (char === "(" || char === "<") depth++;
        else if (char === ")" || char === ">") depth--;
        else if (char === "," && depth === 0) {
          result.push(current.trim());
          current = "";
          continue;
        }
        current += char;
      }
      if (current.trim()) result.push(current.trim());
      return result;
    }

    const parameters = splitTopLevelParams(params)
      .map((param) => {
        const paramPattern =
          /^(?:(val|var|noinline|crossinline|vararg)\s+)?(\w+)\s*:\s*(.+?)(?:\s*=\s*(.+))?$/;
        const paramMatch = param.trim().match(paramPattern);

        if (!paramMatch) {
          // Try parsing as lambda parameter
          const lambdaPattern = /^(\w+)\s*:\s*\((.*?)\)\s*->\s*(.+)$/;
          const lambdaMatch = param.trim().match(lambdaPattern);

          if (lambdaMatch) {
            const [, paramName, lambdaParams, lambdaReturn] = lambdaMatch;
            return {
              modifier: "",
              name: paramName,
              link: parsedLink,
              type: `(${lambdaParams}) -> ${lambdaReturn}`,
              defaultValue: "",
              isLambda: true,
            };
          }

          console.info(`Could not parse Kotlin parameter: ${param}`);
          return null;
        }

        const [, modifier, paramName, type, defaultValue] = paramMatch;

        return {
          modifier: modifier || "",
          name: paramName,
          link: parsedLink,
          type: processTypeLinks($, $(el), type.trim(), tree.breadcrumbs),
          defaultValue: defaultValue ? defaultValue.trim() : "",
          isLambda: false,
        };
      })
      .filter(Boolean);

    const result = {
      signature: func,
      signatureLinks: extractLinksFromSignature($, el, tree.breadcrumbs),
      name,
      parameters,
      link: parsedLink,
      returnType: returnType
        ? processTypeLinks($, $(el), returnType.trim(), tree.breadcrumbs)
        : "Unit",
      doc: processTypeLinks($, briefElement, brief, tree.breadcrumbs),
      isSuspend: func.startsWith("suspend"),
    };

    if (!result.name || !Array.isArray(result.parameters)) {
      console.warn("Invalid Kotlin function parsing result:", result);
      return null;
    }

    return result;
  } catch (error) {
    console.error("Error parsing Kotlin function:", error);
    return null;
  }
}

// ----

function extractMembers(content, type, tree) {
  const $ = cheerio.load(content);
  const members = {
    constructors: [],
    types: [],
    properties: [],
    functions: [],
  };

  try {
    $('div[data-togglable="CONSTRUCTOR"] .symbol.monospace').each((i, el) => {
      const constructor = parseConstructor($, el, tree);
      if (constructor) members.constructors.push(constructor);
    });
  } catch (error) {}

  if ($('div[data-togglable="CONSTRUCTOR"] .symbol.monospace').length == 0) {
    delete members.constructors;
  }

  try {
    $('div[data-togglable="TYPE"] .symbol.monospace').each((i, el) => {
      const type = parseType($, el, tree);
      if (type) members.types.push(type);
    });
  } catch (error) {}

  if ($('div[data-togglable="TYPE"] .symbol.monospace').length == 0) {
    delete members.types;
  }

  try {
    $('div[data-togglable="PROPERTY"] .symbol.monospace').each((i, el) => {
      const property = parseProperty($, el, tree);
      if (property) members.properties.push(property);
    });
  } catch (error) {}

  if ($('div[data-togglable="PROPERTY"] .symbol.monospace').length == 0) {
    delete members.properties;
  }

  try {
    $('div[data-togglable="FUNCTION"] .symbol.monospace').each((i, el) => {
      const func = parseFunction($, el, tree);
      if (func) members.functions.push(func);
    });
  } catch (error) {}

  if ($('div[data-togglable="FUNCTION"] .symbol.monospace').length == 0) {
    delete members.functions;
  }

  return members;
}

function getClassAttribute($, start, title, tree, extraCrumbs) {
  let elements = [];
  start.each((i, el) => {
    if (
      $(el).is("h4") &&
      $(el).text().trim().toLowerCase() === title.toLowerCase()
    ) {
      $(el)
        .next()
        .find("a")
        .each((i, link) => {
          elements.push({
            name: $(link).text().trim(),
            link: pathToPackage(
              convertDashesToCaps($(link).attr("href")),
              extraCrumbs ?? tree.breadcrumbs
            ),
          });
        });
    }
  });

  return elements;
}

function getClassTableAttribute($, start, title, tree, extraCrumbs) {
  let elements = [];
  start.each((i, el) => {
    if (
      $(el).is("h4") &&
      $(el).text().trim().toLowerCase() === title.toLowerCase()
    ) {
      $(el)
        .next()
        .find(".table-row")
        .each((i, row) => {
          const doc = $(row).find(".keyValue div").last();
          const rawType = doc.text().trim();
          const processedDoc = processTypeLinks(
            $,
            doc,
            rawType,
            extraCrumbs ?? tree.breadcrumbs
          );
          // console.log(rawType);

          elements.push({
            name: $(row).find(".keyValue div").first().text().trim(),
            doc: processedDoc,
          });
        });
    }
  });

  return elements;
}

function getAttributeDeprecated($, start, tree) {
  let deprecated = null;

  start.each((i, el) => {
    if ($(el).is(".deprecation-content")) {
      let desc = $(el).find(".paragraph").text().trim();
      let replaceWith = $(el).find(".sample-container>pre").text().trim();

      deprecated = {
        description: desc,
        replaceWith: replaceWith,
      };
    }
  });

  return deprecated;
}

function getKDocSince($, start, tree) {
  let since = "";
  start.each((i, el) => {
    if (
      $(el).is(".kdoc-tag") &&
      $(el).children().first().text().trim().toLowerCase() === "since"
    ) {
      since = $(el).children().first().next().text().trim();
    }
  });

  return since.length > 0 ? since : null;
}

function getClassDetails($, tree) {
  let name = $("h1.cover").text().trim();
  let signature = $(".content.sourceset-dependent-content")
    .find(".symbol.monospace")
    .first()
    .text()
    .replace("@Serializable", "")
    .trim();

  let docElements = $(".content.sourceset-dependent-content > .paragraph");
  let docArray = [];
  let realElements = [];
  docElements.each((i, el) => {
    if ($(el).next().is(".paragraph")) {
      docArray.push($(el).text());
      realElements.push(el);
    } else {
      docArray.push($(el).text());
      realElements.push(el);
      return false; // Break the loop when next element is not a paragraph
    }
  });

  let samples = $(".sample-container").children();
  let sampleArray = [];
  samples.each((i, el) => {
    let text = $(el).text();
    let importMatch = text.match(
      /import.*?tools\.aqua\.bgw\.main\.examples.*?\n/
    );
    let sampleMatch = text.split("//sampleStart");

    if (importMatch && sampleMatch.length > 1) {
      // Extract variable name from val/var declaration
      let sampleCode = sampleMatch[1].split("//sampleEnd")[0];
      let varMatch = sampleCode.match(/\b(val|var)\s+(\w+)\s*=/);
      let variableName = varMatch ? varMatch[2] : null;

      sampleArray.push({
        codepoint: [
          importMatch[0]
            .replace(/\s?as.+/, "")
            .replace(/\n/gm, "")
            .replace(/^\s?import\s?/gm, "") +
            "." +
            variableName,
        ],
        sample: sampleCode
          .replace(/\/\*\*(.*?)\*\//gs, "")
          .replace(/\n$/gm, "")
          .trim(), // Remove line comments
        doc:
          sampleCode
            .match(/\/\*\*(.*?)\*\//gs)
            ?.map((comment) =>
              comment
                .replace(/\/\*\*|\*\//g, "")
                .replace(/\s*\*\s*/g, " ")
                .trim()
            )
            .join(" ") || "",
      });
    }
  });

  // Special handling for Animation samples
  if (
    sampleArray.length > 0 &&
    sampleArray[0].codepoint[0].includes("Animation")
  ) {
    const mergedSample = {
      codepoint: sampleArray
        .filter((s) => s.codepoint[0].split(".").pop() !== "null")
        .map((s) => s.codepoint[0]),
      sample: sampleArray.map((s) => s.sample).join("\n\n"),
      doc: sampleArray
        .map((s) => s.doc)
        .filter(Boolean)
        .join(" "),
    };
    sampleArray = [mergedSample];
  }

  const doc$ = cheerio.load(
    realElements.flatMap((el) => $(el).html()).join("\n\n")
  );
  const rawDoc = doc$("body");
  const rawType = docArray.join("\n\n");

  const doc = processTypeLinks(doc$, rawDoc, rawType, tree.breadcrumbs);

  let start = $(".content.sourceset-dependent-content").children();

  let classMetadata = {
    visibility: null,
    modifiers: [],
    type: null,
  };

  let signatureLinks = {};

  if (signature) {
    // Split signature into words and filter out empty strings
    const words = signature.split(/\s+/).filter((word) => word.length > 0);

    // Define Kotlin keywords to look for
    const visibilityModifiers = ["public", "private", "protected", "internal"];
    const classModifiers = [
      "abstract",
      "sealed",
      "open",
      "data",
      "inner",
      "value",
    ];
    const types = ["class", "interface", "object", "enum"];

    // Extract metadata from words
    words.forEach((word) => {
      if (visibilityModifiers.includes(word)) classMetadata.visibility = word;
      if (classModifiers.includes(word)) classMetadata.modifiers.push(word);
      if (types.includes(word)) classMetadata.type = word;
    });

    let wholeSignature = $(".content.sourceset-dependent-content")
      .find(".symbol.monospace")
      .first();
    signatureLinks = extractLinksFromSignature(
      $,
      wholeSignature,
      tree.breadcrumbs
    );
  }

  return {
    info: {
      name,
      signature,
      signatureLinks,
      doc,
      tags: classMetadata,
    },
    inheritors: getClassAttribute($, start, "Inheritors", tree),
    seeAlso: getClassAttribute($, start, "See also", tree),
    since: getKDocSince($, start, tree),
    parameters: getClassTableAttribute($, start, "Parameters", tree),
    throws: getClassTableAttribute($, start, "Throws", tree),
    deprecated: getAttributeDeprecated($, start, tree),
    samples: sampleArray,
  };
}

function getExtraDetails($, tree) {
  let constructors = [];

  let elements = $(".content.sourceset-dependent-content")
    .children()
    .toString()
    .split("<hr>");

  elements.forEach((el) => {
    const loaded = cheerio.load(el);

    let name = $("h1.cover").text().trim();
    let signature = loaded("body")
      .find(".symbol.monospace")
      .first()
      .text()
      .trim();

    let wholeSignature = loaded("body").find(".symbol.monospace").first();
    let signatureLinks = extractLinksFromSignature(
      $,
      wholeSignature,
      tree.breadcrumbs
    );
    let docElements = loaded("body > .paragraph");
    let docArray = [];
    let realElements = [];
    docElements.each((i, el) => {
      if (loaded(el).next().is(".paragraph")) {
        docArray.push(loaded(el).text());
        realElements.push(el);
      } else {
        docArray.push(loaded(el).text());
        realElements.push(el);
        return false; // Break the loop when next element is not a paragraph
      }
    });

    let samples = loaded("body").find(".sample-container").children();
    let sampleArray = [];
    samples.each((i, el) => {
      let text = loaded(el).text();
      let importMatch = text.match(
        /import.*?tools\.aqua\.bgw\.main\.examples.*?\n/
      );
      let sampleMatch = text.split("//sampleStart");

      if (importMatch && sampleMatch.length > 1) {
        // Extract variable name from val/var declaration
        let sampleCode = sampleMatch[1].split("//sampleEnd")[0];
        let varMatch = sampleCode.match(/\b(val|var)\s+(\w+)\s*=/);
        let variableName = varMatch ? varMatch[2] : null;

        sampleArray.push({
          codepoint: [
            importMatch[0]
              .replace(/\s?as.+/, "")
              .replace(/\n/gm, "")
              .replace(/^\s?import\s?/gm, "") +
              "." +
              variableName,
          ],
          sample: sampleCode
            .replace(/\/\*\*(.*?)\*\//gs, "")
            .replace(/\n$/gm, "")
            .trim(), // Remove line comments
          doc:
            sampleCode
              .match(/\/\*\*(.*?)\*\//gs)
              ?.map((comment) =>
                comment
                  .replace(/\/\*\*|\*\//g, "")
                  .replace(/\s*\*\s*/g, " ")
                  .trim()
              )
              .join(" ") || "",
        });
      }
    });

    // Special handling for Animation samples
    if (
      sampleArray.length > 0 &&
      sampleArray[0].codepoint[0].includes("Animation")
    ) {
      const mergedSample = {
        codepoint: sampleArray
          .filter((s) => s.codepoint[0].split(".").pop() !== "null")
          .map((s) => s.codepoint[0]),
        sample: sampleArray.map((s) => s.sample).join("\n\n"),
        doc: sampleArray
          .map((s) => s.doc)
          .filter(Boolean)
          .join(" "),
      };
      sampleArray = [mergedSample];
    }

    const doc$ = cheerio.load(
      realElements.flatMap((el) => $(el).html()).join("\n\n")
    );
    const rawDoc = doc$("body");
    const rawType = docArray.join("\n\n");

    const breadcrumbsWithoutLast = tree.breadcrumbs.slice(0, -1);

    const doc = processTypeLinks(doc$, rawDoc, rawType, breadcrumbsWithoutLast);

    const start = loaded("body").children();

    constructors.push({
      info: {
        name,
        signature,
        signatureLinks,
        doc,
      },
      inheritors: getClassAttribute(
        loaded,
        start,
        "Inheritors",
        tree,
        breadcrumbsWithoutLast
      ),
      seeAlso: getClassAttribute(
        loaded,
        start,
        "See also",
        tree,
        breadcrumbsWithoutLast
      ),
      since: getKDocSince(loaded, start, tree, breadcrumbsWithoutLast),
      parameters: getClassTableAttribute(
        loaded,
        start,
        "Parameters",
        tree,
        breadcrumbsWithoutLast
      ),
      throws: getClassTableAttribute(
        loaded,
        start,
        "Throws",
        tree,
        breadcrumbsWithoutLast
      ),
      deprecated: getAttributeDeprecated(
        loaded,
        start,
        tree,
        breadcrumbsWithoutLast
      ),
      samples: sampleArray,
    });
  });

  return constructors;
}

function extractDetails(html, type, tree) {
  const $ = cheerio.load(html);

  if (type === "global") return getClassDetails($, tree);
  else if (type === "none") return getExtraDetails($, tree);
  return getExtraDetails($, tree);
}

function parseFileToJSON(tree) {
  const content = fs.readFileSync(tree.path, "utf8");
  let isClassGlobal = "none";

  if (tree.path.endsWith("\\index.html") || tree.path.endsWith("/index.html")) {
    isClassGlobal = "global";
  } else if (
    tree.breadcrumbs.length > 1 &&
    tree.breadcrumbs[tree.breadcrumbs.length - 1] ==
      tree.breadcrumbs[tree.breadcrumbs.length - 2]
  ) {
    isClassGlobal = "extra";
  }

  let details = extractDetails(content, isClassGlobal, tree);
  let members = extractMembers(content, isClassGlobal, tree);

  return {
    type: isClassGlobal,
    breadcrumbs: tree.breadcrumbs,
    package: tree.package,
    path: tree.path,
    details: details,
    members: members,
  };
}

function traverseTree(tree, file, rebuiltTree = {}) {
  if (!tree || typeof tree !== "object") return null;

  if (tree !== null && tree._dirtype === "file") {
    return parseFileToJSON(tree);
  }

  const result = {};
  for (let key in tree) {
    if (
      key !== "_dirtype" &&
      tree[key] !== null &&
      typeof tree[key] === "object"
    ) {
      result[convertDashesToCaps(key)] = traverseTree(tree[key], key);
    } else {
      result[convertDashesToCaps(key)] = null;
    }
  }

  return result;
}

const directoryStructure = getDirectoryStructure(rootDir);

function removeNullValues(obj) {
  if (!obj || typeof obj !== "object") return obj;

  Object.keys(obj).forEach((key) => {
    const value = obj[key];
    if (value === null) {
      delete obj[key];
    } else if (typeof value === "object") {
      removeNullValues(value);
      if (
        Object.keys(value).length === 0 &&
        ![
          "constructors",
          "types",
          "properties",
          "functions",
          "visibility",
          "modifiers",
          "type",
        ].includes(key)
      ) {
        delete obj[key];
      }
    }
  });

  return obj;
}

const cleanedStructure = removeNullValues(traverseTree(directoryStructure));

// Save the JSON to a file in ./output
fs.writeFileSync(
  "./output/cleanedStructure.json",
  JSON.stringify(cleanedStructure)
);
