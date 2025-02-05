import * as React from "react";
import { Slot } from "@radix-ui/react-slot";
import { cva, type VariantProps } from "class-variance-authority";

import { cn } from "@/lib/utils";
import { warn } from "console";

const buttonVariants = cva(
  "inline-flex items-center justify-center whitespace-nowrap rounded-md text-sm font-medium ring-offset-background transition-colors focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring focus-visible:ring-offset-2 disabled:pointer-events-none disabled:opacity-50",
  {
    variants: {
      variant: {
        default: "bg-primary text-primary-foreground hover:bg-primary/90",
        destructive:
          "bg-destructive text-destructive-foreground hover:bg-destructive/90",
        error: "bg-red-600/10 text-red-600",
        warning: "bg-yellow-500/10 text-yellow-500",
        outline:
          "border border-input bg-background hover:bg-accent hover:text-accent-foreground",
        secondary:
          "bg-secondary text-secondary-foreground hover:bg-secondary/80",
        ghost: "hover:bg-accent hover:text-accent-foreground",
        link: "text-primary underline-offset-4 hover:underline",
        class:
          "border-none bg-purple-500/10 text-purple-500 hover:bg-purple-500/20",
        interface:
          "border-none bg-secondary text-white hover:bg-muted-foreground/10",
        object:
          "border-none bg-bgw-green/10 text-bgw-green hover:bg-bgw-green/20",
        enum: "border-none bg-bgw-yellow/10 text-bgw-yellow hover:bg-bgw-yellow/20",
        constant: "border-none bg-red-500/10 text-red-500 hover:bg-red-500/20",
        function:
          "border-none bg-bgw-blue/10 text-bgw-blue hover:bg-bgw-blue/20",
        property:
          "border-none bg-bgw-green/10 text-bgw-green hover:bg-bgw-green/20",
        listener:
          "border-none bg-[#6dbeff] text-background hover:bg-[#6dbeff]/80",
        purpleTransparent:
          "border-none bg-primary/10 text-primary hover:bg-primary/15",
      },
      size: {
        default: "h-10 px-4 py-2",
        sm: "h-9 rounded-md px-3",
        lg: "h-11 rounded-md px-8",
        icon: "h-10 w-10",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  }
);

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : "button";
    return (
      <Comp
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    );
  }
);
Button.displayName = "Button";

export { Button, buttonVariants };
