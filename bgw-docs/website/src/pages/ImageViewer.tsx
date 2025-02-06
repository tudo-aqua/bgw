import React, {useEffect, useState} from "react";
import {DialogClose, DialogFooter} from "@/components/ui/dialog.tsx";
import {Button} from "@/components/ui/button.tsx";
import { useLocalStorage } from "@uidotdev/usehooks";
import setupIndexedDB, { useIndexedDBStore } from "use-indexeddb";

type ImageViewerProps = {
    onAddImage: () => void;
    images ?: any[];
}

function ImageViewer({ onAddImage, images }: ImageViewerProps) {
    const { add, deleteAll } = useIndexedDBStore("images");

    const handleFileChange = (e) => {
        const files = e.target.files;
        for(let i = 0; i < files.length; i++) {
            const file = files[i];
            const reader = new FileReader();
            reader.onload = (e) => {
                const image = e.target.result;
                add({name: file.name, src: image}).then(() => {
                    onAddImage()
                })
            };
            reader.readAsDataURL(file);
        }
    }

    const addImage = () => {
        const input = document.createElement("input");
        input.type = "file";
        input.accept = "image/*";
        input.multiple = true;
        input.onchange = handleFileChange;
        input.click();
    }

    const clearAll = () => {
        deleteAll().then(() => {
            onAddImage();
        })
    }


    return (<>
            <div className="grid gap-3 component__select">
                <div className="h-full p-6 pl-8 pr-8 relative flex-row flex-wrap w-full min-w-[600px] min-h-[400px]">
                    <div className="flex flex-col w-full h-full">
                        <div className="flex flex-row flex-wrap w-full h-full">
                            {images.map((image, index) => (
                                <div key={index} className="flex flex-col w-1/4 h-1/4" title={image.name}>
                                    <img src={image.src} alt="image" className="w-full h-full object-cover"/>
                                </div>
                            ))}
                        </div>
                    </div>
                </div>
            </div>
            <DialogFooter>
                <Button variant="outline" onClick={() => {
                    clearAll();
                }}>Clear</Button>
                <Button variant="outline" onClick={() => {
                    addImage();
                }}>Add</Button>
            </DialogFooter>
        </>
    )
}

export default ImageViewer;