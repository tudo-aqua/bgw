import { create } from "zustand";

type DocsState = {
  currentComponent: any;
  secondarySidebar: any[];
  setCurrentComponent: (component: any) => void;
  setSecondarySidebar: (items: any[]) => void;
  codeBlocksToLoad: number;
  loadedCodeBlocks: number;
  setCodeBlocksToLoad: (count: number) => void;
  incrementLoadedCodeBlocks: () => void;
  incrementToLoadCodeBlocks: () => void;
  resetCodeBlocks: () => void;
};

export const useDocsStore = create<DocsState>((set) => ({
  currentComponent: null,
  secondarySidebar: [],
  setCurrentComponent: (component) => set({ currentComponent: component }),
  setSecondarySidebar: (items) => set({ secondarySidebar: items }),
  codeBlocksToLoad: 0,
  loadedCodeBlocks: 0,
  setCodeBlocksToLoad: (count) => {
    console.log("Setting code blocks to load", count);
    set({ codeBlocksToLoad: count });
  },
  incrementLoadedCodeBlocks: () => {
    set((state) => ({ loadedCodeBlocks: state.loadedCodeBlocks + 1 }));
  },
  incrementToLoadCodeBlocks: () => {
    set((state) => ({ codeBlocksToLoad: state.codeBlocksToLoad + 1 }));
  },
  resetCodeBlocks: () => {
    console.log("Resetting code blocks");
    set({ codeBlocksToLoad: 0, loadedCodeBlocks: 0 });
  },
}));
