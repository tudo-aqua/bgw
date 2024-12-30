@file:JsModule("react-zoom-pan-pinch")
@file:JsNonModule

package tools.aqua.bgw

import react.*

@JsName("TransformWrapper")
internal external val TransformWrapper: ComponentClass<TransformWrapperProps>

internal external interface TransformWrapperProps : PropsWithStyle, PropsWithChildren, PropsWithClassName, PropsWithRef<ReactZoomPanPinchContentRef> {
    var centerZoomedOut : Boolean
    var disablePadding : Boolean
    var smooth : Boolean
    var limitToBounds : Boolean
    var disabled : Boolean
    var wheel : WheelProps
    var pinch : PinchProps
    var doubleClick : DoubleClickProps
    var velocityAnimation : VelocityAnimation
    var panning : PanningProps
    var centerOnInit : Boolean
    var minScale : Double
    var maxScale : Double
    var initialScale : Double
    var onTransformed : (ref : ReactZoomPanPinchContentRef, state : ReactZoomPanPinchState) -> Unit
    var onZoomStop : (ref : ReactZoomPanPinchContentRef) -> Unit
    var onPanningStart : (ref : ReactZoomPanPinchContentRef) -> Unit
    var onPanningStop : (ref : ReactZoomPanPinchContentRef) -> Unit
}

internal external interface DoubleClickProps {
    var disabled : Boolean
}

internal external interface PinchProps {
    var disabled : Boolean
}

internal external interface PanningProps {
    var disabled : Boolean
    var wheelPanning : Boolean
    var velocityDisabled : Boolean
    var allowLeftClickPan : Boolean
    var allowMiddleClickPan : Boolean
    var allowRightClickPan : Boolean
}

internal external interface ReactZoomPanPinchContentRef {
    var instance : ReactZoomPanPinchContext
    var setTransform : (x : Number, y : Number, scale : Number, animationTime : Number) -> Unit
}

internal external interface ReactZoomPanPinchContext {
    var transformState : ReactZoomPanPinchState
    var maxBounds : BoundsType?
}

internal external interface ReactZoomPanPinchState {
    var previousScale : Double
    var scale : Double
    var positionX : Double
    var positionY : Double
}

internal external interface BoundsType {
    var minPositionX: Number
    var maxPositionX: Number
    var minPositionY: Number
    var maxPositionY: Number
}

internal external interface WheelProps {
    var step : Double
}

internal external interface VelocityAnimation {
    var disabled : Boolean
}

@JsName("TransformComponent")
internal external val TransformComponent: ComponentClass<TransformComponentProps>

internal external interface TransformComponentProps : PropsWithStyle, PropsWithChildren, PropsWithClassName {
    var wrapperStyle : CSSProperties
    var contentStyle : CSSProperties
}