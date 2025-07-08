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

@file:Suppress("DuplicatedCode")

package tools.aqua.bgw.builder

import tools.aqua.bgw.application.Constants
import tools.aqua.bgw.components.layoutviews.CameraPane
import tools.aqua.bgw.components.layoutviews.LayoutView

internal object CameraPaneBuilder {
  fun build(cameraPane: CameraPane<out LayoutView<*>>) {
    cameraPane.zoomProperty.guiListener = { _, _ -> Constants.FRONTEND.updateComponent(cameraPane) }
    cameraPane.interactiveProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(cameraPane)
    }
    cameraPane.anchorPointProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(cameraPane)
    }
    cameraPane.panDataProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(cameraPane)
    }
    cameraPane.panMouseButtonProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(cameraPane)
    }
    cameraPane.limitBoundsProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(cameraPane)
    }
    cameraPane.isVerticalLockedProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(cameraPane)
    }
    cameraPane.isHorizontalLockedProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(cameraPane)
    }
    cameraPane.isZoomLockedProperty.guiListener = { _, _ ->
      Constants.FRONTEND.updateComponent(cameraPane)
    }
    ComponentViewBuilder.build(cameraPane.target)
  }
}
