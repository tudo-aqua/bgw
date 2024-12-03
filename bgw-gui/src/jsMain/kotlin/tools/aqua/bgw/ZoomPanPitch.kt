@file:JsModule("react-zoom-pan-pinch")
@file:JsNonModule

package tools.aqua.bgw

import react.*

@JsName("TransformWrapper")
external val TransformWrapper: ComponentClass<TransformWrapperProps>

external interface TransformWrapperProps : PropsWithStyle, PropsWithChildren, PropsWithClassName {
    var centerZoomedOut : Boolean
    var disablePadding : Boolean
    var smooth : Boolean
    var limitToBounds : Boolean
    var disabled : Boolean
    var wheel : WheelProps
    var centerOnInit : Boolean
    var minScale : Double
    var maxScale : Double
    var initialScale : Double
}

external interface WheelProps {
    var step : Double
}

@JsName("TransformComponent")
external val TransformComponent: ComponentClass<TransformComponentProps>

external interface TransformComponentProps : PropsWithStyle, PropsWithChildren, PropsWithClassName {
    var wrapperStyle : CSSProperties
    var contentStyle : CSSProperties
}