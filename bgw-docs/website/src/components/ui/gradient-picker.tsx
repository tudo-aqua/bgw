import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from "@/components/ui/popover";
import { cn } from "../../lib/utils";
import { HexColorPicker } from "react-colorful";
import { Separator } from "@/components/ui/separator.tsx";

export function SimpleColorPicker({
  background,
  setBackground,
  className,
  children,
}: {
  background: string;
  setBackground: (background: string) => void;
  className?: string;
  children?: React.ReactNode;
}) {
  return (
    <Popover>
      <PopoverTrigger asChild>
        <Button
          variant={"secondary"}
          className={cn(
            "w-[150px] justify-start text-left font-normal",
            !background && "text-muted-foreground",
            className
          )}
        >
          <div className="flex items-center w-full gap-4">
            {/**/}
            {background ? (
              <div
                className="h-4 w-4 rounded !bg-center !bg-cover transition-all"
                style={{ background }}
              ></div>
            ) : (
              <i className="text-lg material-symbols-rounded text-muted-foreground">
                water_drop
              </i>
            )}
            <div className="flex-1 font-medium truncate">Color</div>
          </div>
        </Button>
      </PopoverTrigger>
      <PopoverContent
        asChild
        side={"top"}
        align={"end"}
        sideOffset={8}
        className="w-[250px]"
      >
        <div className="flex flex-col">
          <HexColorPicker color={background} onChange={setBackground} />
          <Input
            id="custom"
            value={background}
            className="h-10 col-span-2 mt-3"
            onChange={(e) => setBackground(e.currentTarget.value)}
          />
        </div>
      </PopoverContent>
    </Popover>
  );
}
