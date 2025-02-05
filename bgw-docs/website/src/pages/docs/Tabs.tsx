import React, { useEffect, useLayoutEffect } from "react";
import { Tabs, TabsContent, TabsList, TabsTrigger } from "@/components/ui/tabs";
import { ScrollArea, ScrollBar } from "@/components/ui/scroll-area";
import ReactKotlinPlayground from "@/lib/kotlin-playground";
import { render } from "react-dom";

import "./codePreview.scss";

function CodeTabs({ values, children }: { values: string[]; children: any }) {
  const [currentTab, setCurrentTab] = React.useState(values[0]);

  const changeVisibilityOfTabs = (activeTab: string) => {
    children.forEach((child: any, index: number) => {
      const element = document.getElementById(child.props.id);
      if (element) {
        if (child.props.value === activeTab) {
          element.style.position = "relative";
          element.style.visibility = "visible";
          element.style.opacity = "1";
        } else {
          element.style.position = "absolute";
          element.style.visibility = "hidden";
          element.style.opacity = "0";
        }
      }
    });
  };

  useLayoutEffect(() => {
    changeVisibilityOfTabs(currentTab);
  }, []);

  return (
    <Tabs
      defaultValue={values[0]}
      className="w-full mt-4 mb-4 code-tabs"
      onValueChange={(newValue) => {
        changeVisibilityOfTabs(newValue);
        setCurrentTab(newValue);
      }}
    >
      <TabsList>
        {values.map((value) => (
          <TabsTrigger key={value} value={value}>
            {value.capitalize()}
          </TabsTrigger>
        ))}
      </TabsList>
      {children}
    </Tabs>
  );
}

export default CodeTabs;
