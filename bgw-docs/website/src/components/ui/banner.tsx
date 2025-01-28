import * as React from "react";
import { cva, type VariantProps } from "class-variance-authority";

import { cn } from "@/lib/utils";

const bannerVariants = cva(
  "px-3 py-3 rounded-xl flex gap-6 items-center relative my-4 guide-banner",
  {
    variants: {
      variant: {
        default: "bg-bgw-green/10 text-bgw-green",
        // default: "bg-sidebar-accent text-sidebar-accent-foreground",
        primary: "bg-bgw-blue/10 text-bgw-blue",
        muted: "bg-sidebar-accent text-sidebar-accent-foreground",
        success: "bg-bgw-green/10 text-bgw-green",
        danger: "bg-red-500/10 text-red-500",
        warning: "bg-bgw-orange/10 text-bgw-orange",
        note: "bg-bgw-blue/10 text-bgw-blue",
        more: "bg-purple-500/10 text-purple-500",
      },
    },
    defaultVariants: {
      variant: "default",
    },
  }
);

const icons = {
  default: "school",
  primary: "info",
  muted: "info",
  success: "check_circle",
  danger: "error",
  warning: "emergency_home",
  note: "tooltip",
  more: "note_stack",
};

export interface BannerProps
  extends React.HTMLAttributes<HTMLDivElement>,
    VariantProps<typeof bannerVariants> {
  infoText?: string;
  icon: string;
  subText: any;
  hasLink?: boolean;
}

function Banner({ className, variant, ...props }: BannerProps) {
  variant = variant || "default";
  return (
    <div className={cn(bannerVariants({ variant }), className)}>
      <div className="flex items-center gap-3 pl-3">
        <i className="material-symbols-rounded text-lg">{icons[variant]}</i>
        {props.infoText && <h4 className="font-bold">{props.infoText}</h4>}
      </div>
      <div className="">{props.subText}</div>
      {props.hasLink && (
        <i className="material-symbols-rounded text-xl right-4 absolute">
          chevron_right
        </i>
      )}
    </div>
  );
}

export { Banner, bannerVariants };
