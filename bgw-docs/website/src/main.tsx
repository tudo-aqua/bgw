import { QueryClient, QueryClientProvider } from "react-query";
import ReactDOM from "react-dom/client";
import {
  createBrowserRouter,
  redirect,
  RouterProvider,
} from "react-router-dom";
import { ErrorBoundary } from "react-error-boundary";
import "./index.scss";
import React, { ErrorInfo } from "react";
import { TooltipProvider } from "@radix-ui/react-tooltip";
import { ThemeProvider } from "@/components/theme-provider.tsx";
import BGWPlayground from "@/pages/BGWPlayground.tsx";
import BGWDocsLayout from "./pages/BGWDocsLayout";
import { convertMarkdownToHtml, guideStructure } from "./lib/utils";
import Test from "./pages/Test";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 10,
    },
  },
});

const getConstructors = async () => {
  let c = await fetch("/bgw/bgw/constructors.json");
  if (!c.ok) return null;

  c = await c.json();
  return c;
};

const getDirs = async () => {
  let c = await fetch("/bgw/bgw/cleanedStructure.json");
  if (!c.ok) return null;

  c = await c.json();
  return c;
};

const loaderPlayground = async ({ params }) => {
  const c = await getConstructors();
  if (!c) return redirect("/parser");
  return c;
};

const myErrorHandler = (error: Error, errorInfo: ErrorInfo) => {
  // ...
};

async function loadAllGuides() {
  // Create a flat array of all guide URLs from the guideStructure
  const guideUrls = Object.values(guideStructure).flatMap((section) =>
    section.items.map((item) => ({
      url: item.url.replace("/guides", "").replace(".md", "").toLowerCase(),
      path: item.url,
    }))
  );

  // Add the installation guide which is the default
  guideUrls.unshift({
    url: "installation",
    path: "/guides/installation",
  });

  // Fetch all guides in parallel
  const guides = await Promise.all(
    guideUrls.map(async ({ url, path }) => {
      try {
        const response = await fetch(`/bgw/guides/topics/${url}.md`);
        if (!response.ok) {
          console.warn(`Failed to load guide: ${url}`);
          return null;
        }
        const text = await response.text();
        const html = await convertMarkdownToHtml(text);
        const cleanedHtml = html.replace(
          /{\s?style="(note|danger|warning|info)"\s?}/g,
          ""
        );
        return {
          path,
          content: text,
          html: cleanedHtml,
          searchText: cleanedHtml.replace(/<[^>]*>/g, ""),
        };
      } catch (err) {
        console.warn(`Failed to load guide: ${url}`, err);
        return null;
      }
    })
  );

  // Filter out failed fetches and create a map
  const guidesMap = Object.fromEntries(
    guides.filter((g) => g !== null).map((g) => [g.path, g])
  );

  return guidesMap;
}

// Declare variables to store the loaded data
export let guidesMap = {};
export let constructors = null;
export let dirs = null;

function getGuideInfo(path: string) {
  for (const [section, data] of Object.entries(guideStructure)) {
    const guide = data.items.find((item) => item.url === path);
    if (guide) {
      return {
        section,
        title: guide.title,
        icon: data.icon,
      };
    }
  }
  return null;
}

export function searchGuides(query: string) {
  const results = [];
  const searchRegex = new RegExp(`(${query})`, "gi");

  for (const [path, guide] of Object.entries(guidesMap)) {
    if (
      path.toLowerCase().includes(query.toLowerCase()) ||
      guide.searchText.toLowerCase().includes(query.toLowerCase())
    ) {
      let snippet = "";
      const match = searchRegex.exec(guide.searchText);
      if (match) {
        const start = Math.max(0, match.index - 50);
        const end = Math.min(
          guide.searchText.length,
          match.index + query.length + 50
        );
        snippet =
          (start > 0 ? "..." : "") +
          guide.searchText
            .slice(start, end)
            .trim()
            .replace(
              searchRegex,
              '<mark class="bg-transparent font-bold text-purple-400">$1</mark>'
            ) +
          (end < guide.searchText.length ? "..." : "");
      }
      const guideInfo = getGuideInfo(path);
      results.push([
        path,
        snippet || guide.searchText.slice(0, 100) + "...",
        guideInfo,
      ]);
    }
  }
  return results;
}

export function searchDirs(query: string) {
  if (!query || query.trim() === "") return [];

  const results = [];
  const searchQuery = query.toLowerCase();
  const seenPaths = new Set(); // Track unique paths

  // Iterate through each package
  Object.entries(dirs).forEach(([packageName, packageContent]) => {
    // Iterate through each class in the package
    Object.entries(packageContent).forEach(([className, classContent]) => {
      if (
        ["_index", "breadcrumbs", "name", "ordinal", "entries"].includes(
          className
        )
      )
        return;
      const searchRegex = new RegExp(`(${searchQuery})`, "gi");

      // Check if class name matches
      if (className.toLowerCase().includes(searchQuery)) {
        const indexData = classContent._index || {};
        const details = indexData.details || {};
        const info = details.info || {};

        const path = `${packageName}.${className}`;
        if (!seenPaths.has(path)) {
          seenPaths.add(path);
          results.push([
            packageName,
            {
              type: "class",
              className,
              signature: info.signature || className,
              doc: info.doc || "",
              path,
              highlight: className.replace(
                searchRegex,
                '<mark class="bg-transparent font-bold text-purple-400">$1</mark>'
              ),
            },
          ]);
        }
      }

      // Get class members from _index if available
      const indexMembers = classContent._index?.members || {};

      // Search in properties
      const properties = indexMembers.properties || [];
      properties.forEach((prop) => {
        if (
          ["_index", "breadcrumbs", "name", "ordinal", "entries"].includes(
            prop.name
          )
        )
          return;
        if (prop.name.toLowerCase().includes(searchQuery)) {
          const path = `${packageName}.${className}.${prop.name}`;
          if (!seenPaths.has(path)) {
            seenPaths.add(path);
            results.push([
              packageName,
              {
                type: "property",
                className,
                name: prop.name,
                signature: prop.signature || "",
                doc: prop.doc || "",
                path,
                highlight: prop.name.replace(
                  searchRegex,
                  '<mark class="bg-transparent font-bold text-purple-400">$1</mark>'
                ),
              },
            ]);
          }
        }
      });

      // Search in constructors by name only
      const constructors = indexMembers.constructors || [];
      if (
        className.toLowerCase().includes(searchQuery) &&
        constructors.length > 0
      ) {
        const path = `${packageName}.${className}.${className}`;
        if (!seenPaths.has(path)) {
          seenPaths.add(path);
          results.push([
            packageName,
            {
              type: "constructor",
              className,
              signature: constructors[0].signature || "",
              doc: constructors[0].doc || "",
              path,
              highlight: className.replace(
                searchRegex,
                '<mark class="bg-transparent font-bold text-purple-400">$1</mark>'
              ),
            },
          ]);
        }
      }

      // Search in other members by name (functions, etc.)
      Object.entries(classContent).forEach(([memberName, memberContent]) => {
        // Skip _index as we've already processed it
        if (
          [
            "_index",
            "breadcrumbs",
            "name",
            "ordinal",
            "entries",
            "package",
            "path",
            "type",
            "details",
            "members",
          ].includes(memberName)
        )
          return;

        if (memberName.toLowerCase().includes(searchQuery)) {
          const path = `${packageName}.${className}.${memberName}`;
          if (!seenPaths.has(path)) {
            seenPaths.add(path);
            results.push([
              packageName,
              {
                type: "member",
                className,
                memberName,
                signature: memberContent.details?.[0]?.info?.signature || "",
                doc: memberContent.details?.[0]?.info?.doc || "",
                path,
                highlight: memberName.replace(
                  searchRegex,
                  '<mark class="bg-transparent font-bold text-purple-400">$1</mark>'
                ),
              },
            ]);
          }
        }
      });
    });
  });

  let pushedPathClasses = new Set();

  let uniqueResults = results.filter((e) => {
    if (!pushedPathClasses.has(e[0] + "-" + e[1].className)) {
      pushedPathClasses.add(e[0] + "-" + e[1].className);
      return true;
    }

    return false;
  });

  uniqueResults.sort((a, b) => {
    const aPath = a[0].split(".").pop();
    const bPath = b[0].split(".").pop();

    return aPath.localeCompare(bPath);
  });

  return uniqueResults.slice(0, 30); // Limit results to 30 entries
}

async function docsLoader() {
  const [samplesResponse] = await Promise.all([
    fetch("/bgw/bgw/bgwSamples.json"),
  ]);

  const samples = await samplesResponse.json();

  return {
    allSamples: samples,
  };
}

async function exportLoader() {
  const [constructorsResponse] = await Promise.all([
    fetch("/bgw/bgw/constructors.json"),
  ]);

  const constructors = await constructorsResponse.json();

  return {
    constructors: constructors,
  };
}

async function playgroundLoader() {
  const [constructorsResponse] = await Promise.all([
    fetch("/bgw/bgw/constructors.json"),
  ]);

  const constructors = await constructorsResponse.json();

  return {
    constructors,
  };
}

const savedPath = sessionStorage.getItem("redirectPath");
if (savedPath) {
  console.warn("Redirecting to", savedPath);
  sessionStorage.removeItem("redirectPath");
  window.history.replaceState(null, "", savedPath);
}

// Create a function to initialize the app
async function initializeApp() {
  // Load all the data
  guidesMap = await loadAllGuides();
  constructors = await getConstructors();
  dirs = await getDirs();

  // Create the router after data is loaded
  const router = createBrowserRouter(
    [
      {
        path: "/playground",
        Component: BGWPlayground,
        loader: exportLoader,
      },
      {
        path: "/docs",
        Component: BGWDocsLayout,
        loader: docsLoader,
        children: [
          {
            path: "*",
          },
        ],
      },
      {
        path: "/guides",
        Component: BGWDocsLayout,
        loader: docsLoader,
        children: [
          {
            path: "*",
          },
        ],
      },
      // {
      //   path: "/tests",
      //   Component: Test,
      // },
      {
        path: "/",
        Component: BGWDocsLayout,
        loader: docsLoader,
        children: [
          {
            path: "*",
          },
        ],
      },
    ],
    {
      basename: "/bgw",
    }
  );

  ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
    //<React.StrictMode>
    <>
      <div className="hidden text-[#BB6DFF] text-[#ef4444] text-[#c6ff6e] text-[#FFFFFFB3] text-[#6DBEFF] text-[#FFC656] bg-[#BB6DFF]/20 bg-[#6DBEFF]/20 bg-[#FFC656]/20 !bg-[#BB6DFF]/20 !bg-[#6DBEFF]/20 !bg-[#FFC656]/20 hover:!bg-[#BB6DFF]/20 hover:!bg-[#6DBEFF]/20 hover:!bg-[#FFC656]/20"></div>
      <ThemeProvider defaultTheme="dark" storageKey="vite-ui-theme">
        <TooltipProvider>
          <QueryClientProvider client={queryClient}>
            <RouterProvider router={router} />
          </QueryClientProvider>
        </TooltipProvider>
      </ThemeProvider>
    </>
    //</React.StrictMode>,
  );
}

// Start the application
initializeApp().catch((error) => {
  console.error("Failed to initialize the application:", error);
  // Show some error UI if initialization fails
  ReactDOM.createRoot(document.getElementById("root") as HTMLElement).render(
    <div className="p-8 text-center">
      <h1 className="mb-4 text-2xl font-bold text-red-500">
        Application Error
      </h1>
      <p>
        Failed to load required data. Please refresh the page and try again.
      </p>
    </div>
  );
});
