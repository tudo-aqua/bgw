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

function DraggableDND() {
  const { attributes, listeners, setNodeRef, transform } = useDraggable({
    id: "unique-id",
  });

  const style = {
    transform: CSS.Translate.toString(transform),
  };

  return (
    <button
      id="unique-id"
      className="w-[100px] h-[50px] bg-red-400"
      style={style}
      ref={setNodeRef}
      {...attributes}
      {...listeners}
      onClick={() => {}}
    ></button>
  );
}

function DroppableDND(props) {
  const { isOver, setNodeRef } = useDroppable({
    id: "droppable",
  });

  return (
    <div
      ref={setNodeRef}
      className={`w-[300px] h-[300px] ${isOver ? "bg-green-400" : "bg-white"}`}
      style={{
        zoom: 2,
        position: "absolute",
        top: "50%",
      }}
    >
      {props.children}
    </div>
  );
}

function Test() {
  let pointerSensor = useSensor(PointerSensor, {
    activationConstraint: {
      distance: 10,
    },
  });

  let allSensors = useSensors(pointerSensor);

  return (
    <>
      <PreviewTab codeId={0}></PreviewTab>
      <PreviewTab codeId={1}></PreviewTab>
    </>
  );
}

export default Test;
