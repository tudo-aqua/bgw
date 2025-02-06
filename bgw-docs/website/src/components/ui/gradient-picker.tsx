import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
    Popover,
    PopoverContent,
    PopoverTrigger,
} from '@/components/ui/popover'
import { cn } from '../../lib/utils'
import {HexColorPicker} from "react-colorful";
import {Separator} from "@/components/ui/separator.tsx";

export function SimpleColorPicker({
    background,
    setBackground,
    className,
    children
}: {
    background: string
    setBackground: (background: string) => void
    className?: string
    children?: React.ReactNode
}) {
    return (
        <Popover>
            <PopoverTrigger asChild>
                <Button
                    variant={'secondary'}
                    className={cn(
                        'w-[150px] justify-start text-left font-normal',
                        !background && 'text-muted-foreground',
                        className
                    )}
                >
                    <div className="w-full flex items-center gap-4">
                        {/**/}
                        {background ? (
                            <div
                                className="h-4 w-4 rounded !bg-center !bg-cover transition-all"
                                style={{ background }}
                            ></div>
                        ) : (
                            <i className="material-symbols-rounded text-lg text-muted-foreground">water_drop</i>
                        )}
                        <div className="truncate flex-1 font-medium">
                        Color
                        </div>
                    </div>
                </Button>
            </PopoverTrigger>
            <PopoverContent asChild side={"top"} className="w-[250px]">
                <div className="flex flex-col">
                    <HexColorPicker color={background} onChange={setBackground} />
                    <Input
                        id="custom"
                        value={background}
                        className="col-span-2 h-8 mt-4"
                        onChange={(e) => setBackground(e.currentTarget.value)}
                    />
                </div>
            </PopoverContent>
        </Popover>
    )
}