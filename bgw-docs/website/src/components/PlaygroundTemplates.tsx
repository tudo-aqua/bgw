import { Dialog, DialogContent, DialogDescription, DialogHeader, DialogTitle } from "./ui/dialog";
import { PlaygroundTemplate, templates } from "@/lib/templates";

export default function PlaygroundTemplates({ open, onOpenChange, onSelect }: {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  onSelect: (template: PlaygroundTemplate) => void;
}) {
  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="w-[calc(100%_-_24px)] max-w-2xl max-h-[90dvh] overflow-y-auto">
        <DialogHeader>
          <DialogTitle>Start from a template</DialogTitle>
          <DialogDescription>Opens as a new scene. Your current scene stays in Saved Scenes.</DialogDescription>
        </DialogHeader>
        <div className="grid gap-3 sm:grid-cols-2">
          {templates.map(template => (
            <button
              key={template.id}
              type="button"
              onClick={() => onSelect(template)}
              className="playground-template flex flex-col gap-3 p-4 text-left transition rounded-lg bg-secondary/60 hover:bg-bgw-green/10 focus-visible:outline focus-visible:outline-2 focus-visible:outline-bgw-green"
            >
              <span className="flex items-center gap-3">
                <i className="text-2xl material-symbols-rounded text-bgw-green" aria-hidden="true">{template.icon}</i>
                <span className="font-semibold">{template.name}</span>
              </span>
              <span className="text-sm text-muted-foreground">{template.description}</span>
              <span className="text-xs text-muted-foreground/70">{template.width}×{template.height}</span>
            </button>
          ))}
        </div>
      </DialogContent>
    </Dialog>
  );
}
