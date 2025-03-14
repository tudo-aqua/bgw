import React from "react";
import {
  Collapsible,
  CollapsibleContent,
  CollapsibleTrigger,
} from "@/components/ui/collapsible.tsx";

type SettingsFieldProps = {
  title: string;
  children: React.ReactNode;
  icon?: string;
  hideRightIcon?: boolean;
};

function SettingsField({
  title,
  icon,
  children,
  hideRightIcon,
}: SettingsFieldProps) {
  return (
    <div className="flex flex-col w-full max-w-full min-w-full gap-4 p-5 px-5 overflow-hidden rounded-lg bg-background">
      <Collapsible defaultOpen={true}>
        <CollapsibleTrigger className="cursor-pointer select-none" asChild>
          <div className="flex flex-row items-center justify-between">
            <div className="flex flex-row items-center gap-3">
              {icon && (
                <i className="text-lg text-white transition-colors cursor-pointer material-symbols-rounded hover:text-white">
                  {icon}
                </i>
              )}
              <h2 className="text-sm font-bold">{title}</h2>
            </div>
            {/* {!icon &&
                            <i className="material-symbols-rounded text-white/70 text-lg -mt-0.5 cursor-pointer hover:text-white transition-colors">question_mark</i>} */}
            {icon && !hideRightIcon && (
              <i className="text-lg transition-colors cursor-pointer material-symbols-rounded text-white/70 hover:text-white">
                book_2
              </i>
            )}
          </div>
        </CollapsibleTrigger>
        <CollapsibleContent className="flex flex-col gap-4">
          <div className="w-full h-0"></div>
          {children}
        </CollapsibleContent>
      </Collapsible>
    </div>
  );
}

export default SettingsField;
