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

export function ColorPicker({
    color,
    setColor,
    className,
    children
}: {
    color: string
    setColor: (color: string) => void
    className?: string
    children?: React.ReactNode
}) {
    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button
                    variant={'secondary'}
                    className={cn(
                        'w-full min-w-0 justify-start text-left font-normal',
                        !color && 'text-muted-foreground',
                        className
                    )}
                >
                    <div className="w-full flex items-center gap-4">
                        {/**/}
                        {color ? (
                            <div
                                className="h-4 w-4 rounded !bg-center !bg-cover transition-all"
                                style={{ backgroundColor: color }}
                            ></div>
                        ) : (
                            <i className="material-symbols-rounded text-lg text-muted-foreground">water_drop</i>
                        )}
                        <div className="truncate flex-1 font-medium text-white">
                        Color
                        </div>
                    </div>
                </Button>
            </DialogTrigger>
            <DialogContent className="w-[calc(100%_-_24px)] max-w-[647px] max-h-[90dvh] overflow-y-auto">
                <DialogHeader className="pb-4">
                    <DialogTitle>Adjust ColorVisual</DialogTitle>
                    <DialogDescription>
                        Here you can individually adjust attributes of the visual.
                    </DialogDescription>
                </DialogHeader>
                <div className="flex flex-col sm:flex-row gap-4 min-w-0">
                    <div className="w-full sm:w-[250px] shrink-0 min-w-0">
                        <HexColorPicker color={color} onChange={setColor} />
                        <Input
                            id="custom"
                            value={color}
                            className="col-span-2 h-8 mt-4"
                            onChange={(e) => setColor(e.currentTarget.value)}
                        />
                    </div>
                    <Separator orientation={"vertical"} className="hidden sm:block h-[250px]" />
                    <div className="flex flex-col gap-6 w-full min-w-0">
                        {children}
                    </div>
                </div>
            </DialogContent>
        </Dialog>
    )
}