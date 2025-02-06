/*
 * Copyright 2021-2025 The BoardGameWork Authors
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

@file:Suppress("unused")

package tools.aqua.bgw.visual

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import java.util.*
import javax.imageio.ImageIO
import tools.aqua.bgw.observable.properties.StringProperty

/**
 * Visual showing an [Image].
 *
 * The image gets stretched to the size of the component that this visual is embedded in.
 *
 * A sub-image can be loaded by passing offsetX/offsetY for the TOP_LEFT corner and width/height for
 * the sub-image size. If width or height is passed as -1, the remaining image from offsetX/offsetY
 * will be loaded.
 *
 * @constructor Loads an [ImageVisual] from a path in resources.
 *
 * @param path Location of image file relative to /resources.
 * @property width Width of sub-image. Pass -1 to use full width. Default: -1.
 * @property height Height of sub-image. Pass -1 to use full height. Default: -1.
 * @property offsetX Left bound of sub-image. Default: 0.
 * @property offsetY Top bound of sub-image. Default: 0.
 *
 * @throws IllegalArgumentException If [path] is not a valid path or empty or if [path] was not
 * found in resources.
 *
 * @since 0.10
 */
open class ImageVisual(
    path: String,
    val width: Int = -1,
    val height: Int = -1,
    val offsetX: Int = 0,
    val offsetY: Int = 0
) : SingleLayerVisual() {

  init {
    require(!path.startsWith("http://") && !path.startsWith("https://")) {
      "${path} is not a valid path for ImageVisual. Images must be loaded from local resources."
    }

    require(path.isNotEmpty() && path.isNotBlank()) { "ImageVisual path must not be empty." }

    require(this::class.java.classLoader.getResourceAsStream(path) != null) {
      "ImageVisual path '$path' was not found in resources (on Linux and MacOS, file names are case-sensitive)."
    }
  }

  internal val pathProperty = StringProperty(path)

  var path: String
    get() = pathProperty.value
    set(value) {
      pathProperty.value = value
    }

  /** Load image from [BufferedImage]. */
  constructor(
      image: BufferedImage,
      width: Int = -1,
      height: Int = -1,
      offsetX: Int = 0,
      offsetY: Int = 0,
  ) : this(toDataURI(image), width, height, offsetX, offsetY)

  private companion object {
    fun toDataURI(image: BufferedImage): String {
      val baos = ByteArrayOutputStream()
      ImageIO.write(image, "png", baos)
      val base64 = Base64.getEncoder().encodeToString(baos.toByteArray())
      return "data:image/png;base64,$base64"
    }
  }

  override fun copy(): ImageVisual {
    return ImageVisual(path, width, height, offsetX, offsetY).apply {
      transparency = this@ImageVisual.transparency
      style.applyDeclarations(this@ImageVisual.style)
      filters.applyDeclarations(this@ImageVisual.filters)
      flipped = this@ImageVisual.flipped
    }
  }
}
