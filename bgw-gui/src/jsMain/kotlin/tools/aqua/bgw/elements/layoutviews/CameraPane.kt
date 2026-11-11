/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

package tools.aqua.bgw.elements.layoutviews

import CameraPaneData
import csstype.PropertiesBuilder
import data.event.TransformChangedEventData
import emotion.react.css
import js.objects.jso
import react.*
import react.dom.html.HTMLAttributes
import tools.aqua.bgw.ReactZoomPanPinchContentRef
import tools.aqua.bgw.TransformComponent
import tools.aqua.bgw.TransformWrapper
import tools.aqua.bgw.builder.NodeBuilder
import tools.aqua.bgw.builder.VisualBuilder
import tools.aqua.bgw.elements.bgw
import tools.aqua.bgw.elements.bgwVisuals
import tools.aqua.bgw.elements.cssBuilder
import tools.aqua.bgw.elements.useAnimationCleanup
import tools.aqua.bgw.event.JCEFEventDispatcher
import web.cssom.*
import web.dom.Element
import web.dom.document
import web.dom.getComputedStyle

internal external interface CameraPaneProps : Props {
  var data: CameraPaneData
}

internal fun PropertiesBuilder.cssBuilderIntern(componentViewData: CameraPaneData) {
  cssBuilder(componentViewData)
}

internal fun convertToRem(px: Double): Double {
  val containerBounds = document.getElementById("bgw-root")?.getBoundingClientRect()
  val containerWidth = containerBounds?.width ?: 0.0
  val containerHeight = containerBounds?.height ?: 0.0

  val newRem =
      document.getElementById("bgw-root")?.let {
        getComputedStyle(it).getPropertyValue("--bgwUnit")
      }

  val isHeightRelative = newRem?.contains("cqh") ?: false
  val isWidthRelative = newRem?.contains("cqw") ?: false
  val numberValue = newRem?.replace("cqh", "")?.replace("cqw", "")?.toDoubleOrNull() ?: 1.0

  val currentRem =
      if (isHeightRelative) {
        containerHeight * (numberValue / 100.0)
      } else if (isWidthRelative) {
        containerWidth * (numberValue / 100.0)
      } else {
        1.0
      }

  return px * currentRem
}

internal fun convertToPx(rem: Double): Double {
  val newRem =
      document.getElementById("bgw-root")?.let {
        getComputedStyle(it).getPropertyValue("--bgwUnit")
      }

  val containerBounds = document.getElementById("bgw-root")?.getBoundingClientRect()
  val containerWidth = containerBounds?.width ?: 0.0
  val containerHeight = containerBounds?.height ?: 0.0

  val isHeightRelative = newRem?.contains("cqh") ?: false
  val isWidthRelative = newRem?.contains("cqw") ?: false
  val numberValue = newRem?.replace("cqh", "")?.replace("cqw", "")?.toDoubleOrNull() ?: 1.0

  val currentRem =
      if (isHeightRelative) {
        containerHeight * (numberValue / 100.0)
      } else if (isWidthRelative) {
        containerWidth * (numberValue / 100.0)
      } else {
        1.0
      }

  return rem / currentRem
}

internal val CameraPane =
    FC<CameraPaneProps> { props ->
      // Clean up animation CSS when animation finishes
      useAnimationCleanup(props.data)

      val cameraPaneRef = useRef<ReactZoomPanPinchContentRef>(null)

      val targetWidth = props.data.target?.width?.toDouble() ?: 0.0
      val targetHeight = props.data.target?.height?.toDouble() ?: 0.0

      val paneWidth = props.data.width.toDouble()
      val paneHeight = props.data.height.toDouble()

      val snapPaneWidth = paneWidth / targetWidth
      val snapPaneHeight = paneHeight / targetHeight

      val targetAspect = targetWidth / targetHeight
      val paneAspect = paneWidth / paneHeight

      val isTargetWide = targetAspect >= 1.0
      val isPaneWide = paneAspect >= 1.0

      // Smallest zoom at which the target covers the pane.
      val coverZoom =
          when {
            targetWidth <= 0.0 || targetHeight <= 0.0 || paneWidth <= 0.0 || paneHeight <= 0.0 ->
                1.0
            isTargetWide && isPaneWide -> {
              if (targetAspect >= paneAspect) snapPaneHeight else snapPaneWidth
            }
            isTargetWide && !isPaneWide -> snapPaneHeight
            !isTargetWide && isPaneWide -> snapPaneWidth
            else -> { // !isTargetWide && !isPaneWide
              if (targetAspect >= paneAspect) snapPaneHeight else snapPaneWidth
            }
          }

      // The user cannot zoom out further than the cover zoom. Smaller application zooms are kept.
      val userZoomable = props.data.interactive && !props.data.isZoomLocked
      val minZoom = if (userZoomable) coverZoom else minOf(coverZoom, props.data.zoom)

      val initialZoom =
          if (props.data.limitBounds) maxOf(minZoom, props.data.zoom) else props.data.zoom

      // Last pan or zoom command applied by this instance, starting with the one the browser had
      // applied when it last reported the transform.
      val appliedSequence = useRef(props.data.anchorSequence.toDouble())
      val appliedPanByTotalX = useRef(props.data.anchorPanByTotalX)
      val appliedPanByTotalY = useRef(props.data.anchorPanByTotalY)

      fun reportTransform(zoomLevel: Double, x: Double, y: Double) {
        JCEFEventDispatcher.dispatchEvent(
            TransformChangedEventData(
                    zoomLevel = zoomLevel,
                    anchor = Pair(convertToPx(x), convertToPx(y)),
                    sequence = (appliedSequence.current ?: 0.0).toLong(),
                    panByTotalX = appliedPanByTotalX.current ?: 0.0,
                    panByTotalY = appliedPanByTotalY.current ?: 0.0)
                .apply { id = props.data.id })
      }

      useEffectWithCleanup {
        val panData = props.data.internalPanData
        val ctx = cameraPaneRef.current
        val sequence = panData.sequence.toDouble()
        if (ctx == null || sequence == 0.0 || sequence == appliedSequence.current)
            return@useEffectWithCleanup
        if (!panData.zoomOnly && panData.panTo == null) return@useEffectWithCleanup

        val panTime = if (panData.panSmooth) 300 else 0
        val state = ctx.instance.transformState

        var panZoom = panData.zoom ?: state.scale
        if (panZoom < minZoom && props.data.limitBounds) panZoom = minZoom

        // Bounds of the target at the new zoom
        val minX = convertToRem(props.data.width.toDouble() - targetWidth * panZoom)
        val minY = convertToRem(props.data.height.toDouble() - targetHeight * panZoom)

        var newX: Double
        var newY: Double
        val panTo = panData.panTo
        when {
          panData.zoomOnly || panTo == null -> {
            newX = state.positionX
            newY = state.positionY
          }
          panData.panBy -> {
            // All relative pans this instance did not apply yet.
            val offsetX = panData.panByTotalX - (appliedPanByTotalX.current ?: 0.0)
            val offsetY = panData.panByTotalY - (appliedPanByTotalY.current ?: 0.0)
            newX = state.positionX + convertToRem(offsetX) * panZoom
            newY = state.positionY + convertToRem(offsetY) * panZoom
          }
          else -> {
            newX = convertToRem(panTo.first) * panZoom + convertToRem(props.data.width / 2.0)
            newY = convertToRem(panTo.second) * panZoom + convertToRem(props.data.height / 2.0)
          }
        }

        if (props.data.limitBounds) {
          newX = newX.coerceIn(minOf(minX, 0.0), maxOf(minX, 0.0))
          newY = newY.coerceIn(minOf(minY, 0.0), maxOf(minY, 0.0))
        }

        ctx.setTransform(newX, newY, panZoom, panTime)
        appliedSequence.current = sequence
        appliedPanByTotalX.current = panData.panByTotalX
        appliedPanByTotalY.current = panData.panByTotalY

        // Report the target transform. The current state may still be animating towards it.
        reportTransform(panZoom, newX, newY)
      }

      bgwCameraPane {
        tabIndex = if (props.data.isFocusable && !props.data.isDisabled) 0 else -1
        id = props.data.id
        className = ClassName("cameraPane")

        css { cssBuilderIntern(props.data) }

        bgwCameraTarget {
          css {
            width = props.data.width.bgw
            height = props.data.height.bgw
            display = Display.flex
          }
          bgwVisuals {
            className = ClassName("visuals")
            +VisualBuilder.build(props.data.visual)
          }
        }

        TransformWrapper {
          centerZoomedOut =
              props.data.limitBounds || props.data.isHorizontalLocked || props.data.isVerticalLocked
          disablePadding = true
          smooth = false
          limitToBounds = props.data.limitBounds
          centerOnInit = !props.data.hasAnchor
          minScale = if (props.data.limitBounds) minZoom else 0.1
          maxScale = 4.0
          initialScale = initialZoom
          if (props.data.hasAnchor) {
            initialPositionX = convertToRem(props.data.anchorX)
            initialPositionY = convertToRem(props.data.anchorY)
          }
          wheel = jso {
            disabled = !props.data.interactive || props.data.isZoomLocked
            step = 0.1
          }
          panning = jso {
            disabled = !props.data.interactive
            wheelPanning = false
            velocityDisabled = true
            allowLeftClickPan = props.data.panButton == "left_button"
            allowMiddleClickPan = props.data.panButton == "mouse_wheel"
            allowRightClickPan = props.data.panButton == "right_button"
            lockAxisX = props.data.isHorizontalLocked
            lockAxisY = props.data.isVerticalLocked
          }
          pinch = jso { disabled = true }
          doubleClick = jso { disabled = true }

          //        onTransformed = { ctx, state ->
          //            val currentX = state.positionX
          //            val currentY = state.positionY
          //
          //            val actualXOffset = convertToPx(currentX)
          //            val actualYOffset = convertToPx(currentY)
          //
          //            JCEFEventDispatcher.dispatchEvent(TransformChangedEventData(
          //                zoomLevel = state.scale,
          //                anchor = Pair(actualXOffset, actualYOffset)
          //            ).apply { id = props.data.id })
          //        }

          onZoomStop = { ctx ->
            //          if(props.data.isHorizontalLocked || props.data.isVerticalLocked) {
            //            ctx.centerView(ctx.instance.transformState.scale, 0, "ease-out")
            //          }
            val state = ctx.instance.transformState
            reportTransform(state.scale, state.positionX, state.positionY)
          }

          onPanningStop = { ctx ->
            if (!props.data.limitBounds) {
              val currentX = ctx.instance.transformState.positionX
              val currentY = ctx.instance.transformState.positionY

              val targetWidth =
                  props.data.target?.width?.let {
                    convertToRem(it.toDouble()) * ctx.instance.transformState.scale
                  } ?: 0.0
              val targetHeight =
                  props.data.target?.height?.let {
                    convertToRem(it.toDouble()) * ctx.instance.transformState.scale
                  } ?: 0.0

              val paneWidth = convertToRem(props.data.width.toDouble())
              val paneHeight = convertToRem(props.data.height.toDouble())

              val minX = -targetWidth + convertToRem(50.0)
              val minY = -targetHeight + convertToRem(50.0)
              val maxX = paneWidth - convertToRem(50.0)
              val maxY = paneHeight - convertToRem(50.0)

              if (currentX < minX && currentY < minY) {
                ctx.setTransform(minX, minY, ctx.instance.transformState.scale, 300)
              } else if (currentX > maxX && currentY > maxY) {
                ctx.setTransform(maxX, maxY, ctx.instance.transformState.scale, 300)
              } else if (currentX < minX && currentY > maxY) {
                ctx.setTransform(minX, maxY, ctx.instance.transformState.scale, 300)
              } else if (currentX > maxX && currentY < minY) {
                ctx.setTransform(maxX, minY, ctx.instance.transformState.scale, 300)
              } else {
                if (currentX < minX) {
                  ctx.setTransform(minX, currentY, ctx.instance.transformState.scale, 300)
                } else if (currentX > maxX) {
                  ctx.setTransform(maxX, currentY, ctx.instance.transformState.scale, 300)
                }

                if (currentY < minY) {
                  ctx.setTransform(currentX, minY, ctx.instance.transformState.scale, 300)
                } else if (currentY > maxY) {
                  ctx.setTransform(currentX, maxY, ctx.instance.transformState.scale, 300)
                }
              }
            }

            val state = ctx.instance.transformState
            reportTransform(state.scale, state.positionX, state.positionY)
          }

          ref = cameraPaneRef

          TransformComponent {
            wrapperStyle = jso {
              width = props.data.width.bgw
              height = props.data.height.bgw
              // left = props.data.posX.bgw
              // top = props.data.posY.bgw
              position = Position.absolute
              zIndex = integer(props.data.zIndex)
              pointerEvents = if (props.data.isDisabled) None.none else Globals.inherit
            }

            if (props.data.target != null) {
              bgwCameraTarget {
                id = props.data.target?.id + "Target"
                className = ClassName("target")
                +props.data.target?.let { NodeBuilder.build(it) }
              }
            }
          }
        }
      }
    }

internal inline val bgwCameraTarget: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_camera_target".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()

internal inline val bgwCameraPane: IntrinsicType<HTMLAttributes<Element>>
  get() = "bgw_camera_pane".unsafeCast<IntrinsicType<HTMLAttributes<Element>>>()
