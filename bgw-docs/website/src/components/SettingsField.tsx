﻿import React from "react";
import {Collapsible, CollapsibleContent, CollapsibleTrigger} from "@/components/ui/collapsible.tsx";

type SettingsFieldProps = {
    title : string;
    children : React.ReactNode;
    icon? : string;
}

function SettingsField({ title, icon, children } : SettingsFieldProps) {
    return (
        <fieldset className="flex flex-col gap-4 p-3 w-full">
            <Collapsible defaultOpen={true}>
                <CollapsibleTrigger className="cursor-pointer" asChild>
                    <div className="flex flex-row justify-between items-center">
                        <div className="flex flex-row items-center gap-3">
                            {icon &&
                                <i className="material-symbols-rounded text-white text-lg cursor-pointer hover:text-white transition-colors">{icon}</i>}
                            <h2 className="text-sm font-bold">
                                {title}
                            </h2>
                        </div>
                        {!icon &&
                            <i className="material-symbols-rounded text-white/70 text-lg -mt-0.5 cursor-pointer hover:text-white transition-colors">question_mark</i>}
                        {icon &&
                            <i className="material-symbols-rounded text-white/70 text-lg cursor-pointer hover:text-white transition-colors">book_2</i>}
                    </div>
                </CollapsibleTrigger>
                <CollapsibleContent className="flex flex-col gap-4">
                    <div className="w-full h-0"></div>
                    {children}
                </CollapsibleContent>
            </Collapsible>
        </fieldset>
    )
        ;
}

export default SettingsField;