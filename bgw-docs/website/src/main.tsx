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
import Dashboard from "@/pages/Dashboard.tsx";
import { TooltipProvider } from "@radix-ui/react-tooltip";
import { ThemeProvider } from "@/components/theme-provider.tsx";
import Parser from "@/pages/Parser.tsx";
import BGWPlayground from "@/pages/BGWPlayground.tsx";
import ComponentDocs from "./pages/docs/Layout";
import { DndContext } from "@dnd-kit/core";
import Test from "./pages/Test";
import Layout from "./pages/docs/Layout";

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

async function docsLoader() {
  const [docsResponse, samplesResponse] = await Promise.all([
    fetch("/bgw/bgw/cleanedStructure.json"),
    fetch("/bgw/bgw/bgwSamples.json"),
  ]);

  const docs = await docsResponse.json();
  const samples = await samplesResponse.json();

  return { dirs: docs, allSamples: samples };
}

const router = createBrowserRouter(
  [
    {
      path: "/playground",
      Component: Dashboard,
    },
    {
      path: "/docs",
      Component: Layout,
      loader: docsLoader,
      children: [
        {
          path: "*",
        },
      ],
    },
    {
      path: "/guides",
      Component: Layout,
      loader: docsLoader,
      children: [
        {
          path: "*",
        },
      ],
    },
    {
      path: "/",
      Component: Layout,
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
    <div className="hidden text-[#BB6DFF] text-[#FFFFFFB3] text-[#6DBEFF] text-[#FFC656] bg-[#BB6DFF]/20 bg-[#6DBEFF]/20 bg-[#FFC656]/20 !bg-[#BB6DFF]/20 !bg-[#6DBEFF]/20 !bg-[#FFC656]/20 hover:!bg-[#BB6DFF]/20 hover:!bg-[#6DBEFF]/20 hover:!bg-[#FFC656]/20"></div>
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
