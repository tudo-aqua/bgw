import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";

import { cn } from "@/lib/utils";

const badgeVariants = cva(
  "inline-flex items-center rounded-full border px-2.5 py-0.5 text-xs font-semibold transition-colors focus:outline-none focus:ring-2 focus:ring-ring focus:ring-offset-2",
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
          "border-none bg-purple-500/10 text-purple-500 hover:bg-purple-500/20 badge",
        interface:
          "border-none bg-secondary text-white hover:bg-muted-foreground/10 badge",
        object:
          "border-none bg-bgw-green/10 text-bgw-green hover:bg-bgw-green/20 badge",
        enum: "border-none bg-bgw-yellow/10 text-bgw-yellow hover:bg-bgw-yellow/20 badge",
        constant:
          "border-none bg-red-500/10 text-red-500 hover:bg-red-500/20 badge",
        function:
          "border-none bg-bgw-blue/10 text-bgw-blue hover:bg-bgw-blue/20 badge",
        property:
          "border-none bg-bgw-green/10 text-bgw-green hover:bg-bgw-green/20 badge",
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

export interface BadgeProps
  extends React.HTMLAttributes<HTMLDivElement>,
    VariantProps<typeof badgeVariants> {}

function Badge({ className, variant, ...props }: BadgeProps) {
  return (
    <div className={cn(badgeVariants({ variant }), className)} {...props} />
  );
}

export { Badge, badgeVariants };
