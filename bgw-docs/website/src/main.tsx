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
import { guideStructure } from "./lib/utils";

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1000 * 10,
    },
  },
});

const getConstructors = async () => {
  let c = await fetch(`http://localhost:8080/api/2.0.10/compiler/constructors`);

  if (c.status !== 200) return null;
  return await c.json();
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
        return {
          path,
          content: text,
        };
      } catch (err) {
        console.warn(`Failed to load guide: ${url}`, err);
        return null;
      }
    })
  );

  // Filter out failed fetches and create a map
  const guidesMap = Object.fromEntries(
    guides.filter((g) => g !== null).map((g) => [g.path, g.content])
  );

  return guidesMap;
}

async function docsLoader() {
  const [docsResponse, samplesResponse] = await Promise.all([
    fetch("/bgw/bgw/cleanedStructure.json"),
    fetch("/bgw/bgw/bgwSamples.json"),
  ]);

  const docs = await docsResponse.json();
  const samples = await samplesResponse.json();

  return {
    dirs: docs,
    allSamples: samples,
  };
}

const savedPath = sessionStorage.getItem("redirectPath");
if (savedPath) {
  console.warn("Redirecting to", savedPath);
  sessionStorage.removeItem("redirectPath");
  window.history.replaceState(null, "", savedPath);
}

const router = createBrowserRouter(
  [
    {
      path: "/playground",
      Component: BGWPlayground,
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
