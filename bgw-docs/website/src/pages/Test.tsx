import {
  DndContext,
  PointerSensor,
  useDraggable,
  useDroppable,
  useSensor,
  useSensors,
} from "@dnd-kit/core";
import { CSS } from "@dnd-kit/utilities";
import React from "react";
import PreviewTab from "./docs/PreviewTab";
import CodeTab from "./docs/CodeTab";
import { createKotlinCodeLinebreaks } from "@/lib/utils";
import ReactKotlinPlayground from "@/components/ReactKotlinPlayground";

function Test() {
  const [selectedCode, setSelectedCode] = React.useState(0);

  const kotlinSnippets = [
    `fun main() {
      val greeting = "Hello, Kotlin!"
      println(greeting)
    }`,
    `data class Person(
      val name: String,
      val age: Int
    )
    
    fun main() {
      val person = Person("Alice", 30)
      println(person)
    }`,
  ];

  const handleToggle = () => {
    setSelectedCode((prev) => (prev === 0 ? 1 : 0));
  };

  return (
    <div className="p-4">
      <button
        onClick={handleToggle}
        className="px-4 py-2 mb-4 text-white bg-blue-500 rounded"
      >
        Toggle Code
      </button>
      <CodeTab
        code={createKotlinCodeLinebreaks(kotlinSnippets[selectedCode])}
      />
    </div>
  );
}

export default Test;
