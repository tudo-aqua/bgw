import { SearchIcon } from "lucide-react";
import * as React from "react";

import { Dialog, DialogContent } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { searchGuides } from "@/main.tsx";
import { ScrollArea } from "@/components/ui/scroll-area";

export default function SearchField() {
  const [open, setOpen] = React.useState(false);
  const [results, setResults] = React.useState([]);

  const handleSearch = (event: React.ChangeEvent<HTMLInputElement>) => {
    const query = event.target.value;
    if (query.trim().length === 0) {
      setResults([]);
    } else {
      const foundGuides = searchGuides(query);
      setResults(foundGuides);
    }
  };

  React.useEffect(() => {
    const down = (e: KeyboardEvent) => {
      if (e.key === "k" && (e.metaKey || e.ctrlKey)) {
        e.preventDefault();
        setOpen((open) => !open);
      }
    };

    document.addEventListener("keydown", down);
    return () => document.removeEventListener("keydown", down);
  }, []);

  return (
    <>
      <button
        className="border-input bg-background text-foreground placeholder:text-muted-foreground/70 focus-visible:border-ring focus-visible:ring-ring/50 inline-flex h-9 w-fit rounded-md border px-3 py-2 text-sm shadow-xs transition-[color,box-shadow] outline-none focus-visible:ring-[3px]"
        onClick={() => setOpen(true)}
      >
        <span className="flex grow items-center">
          <SearchIcon
            className="text-muted-foreground/80 -ms-1 me-3"
            size={16}
            aria-hidden="true"
          />
          <span className="text-muted-foreground/70 font-normal">Search</span>
        </span>
        <kbd className="bg-background text-muted-foreground/70 ms-12 -me-1 inline-flex h-5 max-h-full items-center rounded border px-1 font-[inherit] text-[0.625rem] font-medium">
          ⌘K
        </kbd>
      </button>
      <Dialog open={open} onOpenChange={setOpen}>
        <DialogContent className="gap-0 p-0 outline-none">
          <div className="flex flex-col gap-4 p-4">
            <Input
              placeholder="Search BGW Documentation..."
              onChange={handleSearch}
              className="border-0 focus-visible:ring-0"
              autoFocus
            />
          </div>
          <ScrollArea className="h-[50vh] border-t">
            {results.length === 0 ? (
              <p className="p-4 text-sm text-muted-foreground">
                No results found.
              </p>
            ) : (
              <div className="p-4">
                <div className="text-sm text-muted-foreground mb-2">Guides</div>
                {results.map(([path, content, guideInfo]) => (
                  <div
                    key={path}
                    className="text-sm px-2 py-2 rounded-sm hover:bg-accent hover:text-accent-foreground cursor-pointer"
                  >
                    <div className="font-medium mb-1 flex items-center gap-2">
                      <span className="material-symbols-rounded text-base">
                        {guideInfo?.icon}
                      </span>
                      <span className="text-muted-foreground">
                        {guideInfo?.section}
                      </span>
                      <span className="text-muted-foreground/50 mx-1">/</span>
                      <span>{guideInfo?.title}</span>
                    </div>
                    <div
                      className="text-xs text-muted-foreground line-clamp-2"
                      dangerouslySetInnerHTML={{ __html: content }}
                    />
                  </div>
                ))}
              </div>
            )}
          </ScrollArea>
        </DialogContent>
      </Dialog>
    </>
  );
}
