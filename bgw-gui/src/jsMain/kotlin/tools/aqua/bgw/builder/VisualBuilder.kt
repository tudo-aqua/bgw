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

package tools.aqua.bgw.builder

import ColorVisualData
import CompoundVisualData
import ImageVisualData
import TextVisualData
import VisualData
import react.*
import react.dom.html.ReactHTML.div
import tools.aqua.bgw.elements.visual.ColorVisual as ReactColorVisual
import tools.aqua.bgw.elements.visual.CompoundVisual
import tools.aqua.bgw.elements.visual.ImageVisual as ReactImageVisual
import tools.aqua.bgw.elements.visual.TextVisual as ReactTextVisual

internal external interface VisualProps : Props {
  var data: VisualData
}

internal object VisualBuilder {
  fun build(visual: VisualData?): ReactElement<*> {
    when (visual) {
      is ColorVisualData -> {
        return ReactColorVisual.create { data = visual }
      }
      is ImageVisualData -> {
        return ReactImageVisual.create { data = visual }
      }
      is TextVisualData -> {
        return ReactTextVisual.create { data = visual }
      }
      is CompoundVisualData -> {
        return CompoundVisual.create { data = visual }
      }
      else -> {
        return div.create()
      }
    }
  }
}
