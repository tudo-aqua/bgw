@file:JsModule("react-zoom-pan-pinch")
@file:JsNonModule

package tools.aqua.bgw

import react.*

@JsName("TransformWrapper")
internal external val TransformWrapper: ComponentClass<TransformWrapperProps>

internal external interface TransformWrapperProps : PropsWithStyle, PropsWithChildren, PropsWithClassName {
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

internal external interface WheelProps {
    var step : Double
}

@JsName("TransformComponent")
internal external val TransformComponent: ComponentClass<TransformComponentProps>

internal external interface TransformComponentProps : PropsWithStyle, PropsWithChildren, PropsWithClassName {
    var wrapperStyle : CSSProperties
    var contentStyle : CSSProperties
}