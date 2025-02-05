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
import {Badge} from "@/components/ui/badge.tsx";

export function TextPicker({
    text,
    setText,
    className,
    children
}: {
    text: string
    setText: (text: string) => void
    className?: string
    children?: React.ReactNode
}) {
    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button
                    variant={'secondary'}
                    className={cn(
                        'w-[839px] justify-start text-left font-normal',
                        !text && 'text-muted-foreground',
                        className
                    )}
                >
                    <div className="w-full flex items-center gap-4">
                        <i className="material-symbols-rounded text-lg text-muted-foreground">text_fields</i>
                        <div className="truncate flex-1 font-medium text-white">
                            { text ?? "Text"}
                        </div>
                    </div>
                </Button>
            </DialogTrigger>
            <DialogContent className="w-[839px] max-w-full">
                <DialogHeader className="pb-4">
                    <DialogTitle>Adjust TextVisual</DialogTitle>
                    <DialogDescription>
                        Here you can individually adjust attributes of the visual.
                    </DialogDescription>
                </DialogHeader>
                <div className="flex flex-row gap-4">
                    <div className="min-w-[250px]">
                        <AspectRatio ratio={5 / 4} className="rounded-md flex items-center justify-center !bg-muted/50 overflow-hidden">
                            {/*{ text && */}
                            {/*    <p className="text-lg text-muted-foreground">{text}</p>*/}
                            {/*}*/}
                            <Badge variant="outline">No preview available</Badge>
                        </AspectRatio>
                        <Input
                            id="custom"
                            placeholder="Text"
                            value={text}
                            className="col-span-2 h-8 mt-4"
                            onChange={(e) => setText(e.currentTarget.value)}
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