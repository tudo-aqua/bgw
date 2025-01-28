import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { cn } from '../../lib/utils'
import {HexColorPicker} from "react-colorful";
import {Separator} from "@/components/ui/separator.tsx";
import {
    Dialog,
    DialogContent,
    DialogDescription,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import React from "react";
import {AspectRatio} from "@/components/ui/aspect-ratio.tsx";

export function ImagePicker({
    image,
    setImage,
    className,
    children
}: {
    image: string
    setImage: (image: string) => void
    className?: string
    children?: React.ReactNode
}) {
    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button
                    variant={'secondary'}
                    className={cn(
                        'w-[647px] justify-start text-left font-normal',
                        !image && 'text-muted-foreground',
                        className
                    )}
                >
                    <div className="w-full flex items-center gap-4">
                        {/**/}
                        {image ? (
                            <div
                                className="h-4 w-4 rounded !bg-center !bg-cover transition-all"
                                style={{ backgroundImage: `url('${image}')` }}
                            ></div>
                        ) : (
                            <i className="material-symbols-rounded text-lg text-muted-foreground">image</i>
                        )}
                        <div className="truncate flex-1 font-medium text-white">
                        Image
                        </div>
                    </div>
                </Button>
            </DialogTrigger>
            <DialogContent className="w-[647px] max-w-full">
                <DialogHeader className="pb-4">
                    <DialogTitle>Adjust ImageVisual</DialogTitle>
                    <DialogDescription>
                        Here you can individually adjust attributes of the visual.
                    </DialogDescription>
                </DialogHeader>
                <div className="flex flex-row gap-4">
                    <div className="min-w-[250px]">
                        <AspectRatio ratio={5 / 4} className="rounded-md flex items-center justify-center !bg-muted/50 overflow-hidden">
                            { image && 
                                <img src={image} alt="Image" className="object-cover max-w-full max-h-full" />
                            }
                        </AspectRatio>
                        <Input
                            id="custom"
                            placeholder="Image URL"
                            value={image}
                            className="col-span-2 h-8 mt-4"
                            onChange={(e) => setImage(e.currentTarget.value)}
                        />
                    </div>
                    <Separator orientation={"vertical"} className="h-full" />
                    <div className="flex flex-col gap-6 w-full">
                        {children}
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    )
}