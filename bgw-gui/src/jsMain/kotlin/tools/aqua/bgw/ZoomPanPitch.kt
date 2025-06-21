/*
 * Copyright 2025 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JsModule("react-zoom-pan-pinch")
@file:JsNonModule

package tools.aqua.bgw

import react.*

@JsName("TransformWrapper")
internal external val TransformWrapper: ComponentClass<TransformWrapperProps>

internal external interface TransformWrapperProps :
    PropsWithStyle,
    PropsWithChildren,
    PropsWithClassName,
    PropsWithRef<ReactZoomPanPinchContentRef> {
  var centerZoomedOut: Boolean
  var disablePadding: Boolean
  var smooth: Boolean
  var limitToBounds: Boolean
  var disabled: Boolean
  var wheel: WheelProps
  var pinch: PinchProps
  var doubleClick: DoubleClickProps
  var velocityAnimation: VelocityAnimation
  var panning: PanningProps
  var centerOnInit: Boolean
  var minScale: Double
  var maxScale: Double
  var initialScale: Double
  var onTransformed: (ref: ReactZoomPanPinchContentRef, state: ReactZoomPanPinchState) -> Unit
  var onZoomStop: (ref: ReactZoomPanPinchContentRef) -> Unit
  var onPanningStart: (ref: ReactZoomPanPinchContentRef) -> Unit
  var onPanningStop: (ref: ReactZoomPanPinchContentRef) -> Unit
}

internal external interface DoubleClickProps {
  var disabled: Boolean
}

internal external interface PinchProps {
  var disabled: Boolean
}

internal external interface PanningProps {
  var disabled: Boolean
  var wheelPanning: Boolean
  var velocityDisabled: Boolean
  var allowLeftClickPan: Boolean
  var allowMiddleClickPan: Boolean
  var allowRightClickPan: Boolean
  var lockAxisX: Boolean
  var lockAxisY: Boolean
}

internal external interface ReactZoomPanPinchContentRef {
  var instance: ReactZoomPanPinchContext
  var setTransform: (x: Number, y: Number, scale: Number, animationTime: Number) -> Unit
  var centerView: (scale: Number, animationTime: Number, animationName: String) -> Unit
}

internal external interface ReactZoomPanPinchContext {
  var transformState: ReactZoomPanPinchState
  var maxBounds: BoundsType?
}

internal external interface ReactZoomPanPinchState {
  var previousScale: Double
  var scale: Double
  var positionX: Double
  var positionY: Double
}

internal external interface BoundsType {
  var minPositionX: Number
  var maxPositionX: Number
  var minPositionY: Number
  var maxPositionY: Number
}

internal external interface WheelProps {
  var disabled: Boolean
  var step: Double
}

internal external interface VelocityAnimation {
  var disabled: Boolean
}

@JsName("TransformComponent")
internal external val TransformComponent: ComponentClass<TransformComponentProps>

internal external interface TransformComponentProps :
    PropsWithStyle, PropsWithChildren, PropsWithClassName {
  var wrapperStyle: CSSProperties
  var contentStyle: CSSProperties
}
