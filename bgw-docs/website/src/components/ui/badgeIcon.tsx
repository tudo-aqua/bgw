import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";

import { cn } from "@/lib/utils";

const badgeIconVariants = cva(
  "flex justify-center items-center rounded-full py-0.5 text-xs font-semibold transition-colors w-2 h-2 text-center shrink-0 font-mono indent-[-0.1px] ml-1",
  {
    variants: {
      variant: {
        default:
          "border-none bg-primary text-primary-foreground hover:bg-primary/80 badge",
        secondary:
          "border-none bg-secondary text-secondary-foreground hover:bg-secondary/80 badge",
        destructive:
          "border-none bg-destructive text-destructive-foreground hover:bg-destructive/80 badge",
        muted: "border-none bg-secondary text-secondary-foreground badge",
        mutedLink:
          "border-none bg-secondary text-secondary-foreground hover:bg-muted-foreground/10 badge",
        outline: "text-foreground badge",
        class:
          "border-none bg-purple-500 text-background hover:bg-purple-500/80 badge",
        interface:
          "border-none bg-muted-foreground text-background badge rounded-[2px]",
        object:
          "border-none bg-bgw-green text-background hover:bg-bgw-green/80 badge",
        enum: "border-none bg-[#ffc656] text-background hover:bg-[#ffc656]/80 badge",
        constant:
          "border-none bg-[#fa6c56] text-background hover:bg-[#fa6c56]/80 badge",
        function:
          "border-none bg-[#ea7afc] text-background hover:bg-[#ea7afc]/80 badge",
        property:
          "border-none bg-[#c6ff6e] text-background hover:bg-[#c6ff6e]/80 badge",
        listener:
          "border-none bg-[#6dbeff] text-background hover:bg-[#6dbeff]/80 badge",
        purpleTransparent:
          "border-none bg-primary/10 text-primary hover:bg-primary/15 badge",
      },
    },
    defaultVariants: {
      variant: "default",
    },
  }
);

export interface BadgeIconProps
  extends React.HTMLAttributes<HTMLDivElement>,
    VariantProps<typeof badgeIconVariants> {}

function BadgeIcon({ className, variant, ...props }: BadgeIconProps) {
  return (
    <div className={cn(badgeIconVariants({ variant }), className)} {...props} />
  );
}

export { BadgeIcon, badgeIconVariants };
