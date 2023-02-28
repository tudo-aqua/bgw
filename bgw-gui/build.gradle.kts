/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

plugins { id("tools.aqua.bgw.library-conventions") }

mavenMetadata {
  name.set("BoardGameWork GUI Library")
  description.set("A framework for board game applications.")
}

dependencies {
  /* jfoenix - Applies styles to JavaFX controls. */
  implementation(libs.jfoenix)

  /*
   * UNUSED MODULES:
   * javafx.fxml - Defines the FXML APIs for the JavaFX UI toolkit.
   *
   * javafx.media - Defines APIs for playback of media and audio content, as part of the JavaFX UI toolkit, including
   *                  MediaView and MediaPlayer.
   * javafx.swing - Defines APIs for the JavaFX / Swing interop support included with the JavaFX UI toolkit, including
   *                  SwingNode (for embedding Swing inside a JavaFX application) and JFXPanel (for embedding JavaFX
   *                  inside a Swing application).
   * javafx.web - Defines APIs for the WebView functionality contained within the JavaFX UI toolkit.
   */

  listOf("win", "mac", "linux" /*, "mac-aarch64" */).forEach { os ->
    /*
     * javafx.base - Defines the base APIs for the JavaFX UI toolkit, including APIs for bindings, properties,
     *   collections, and events.
     * javafx.controls  - Defines the UI controls, charts, and skins that are available for the JavaFX UI toolkit.
     * javafx.graphics  - Defines the core scenegraph APIs for the JavaFX UI toolkit (such as layout containers,
     *   application lifecycle, shapes, transformations, canvas, input, painting, image handling, and effects), as well
     *   as APIs for animation, css, concurrency, geometry, printing, and windowing.
     */
    libs.bundles.openjfx.small.get().forEach { dep ->
      implementation(
          dep.module.group, dep.module.name, dep.versionConstraint.requiredVersion, classifier = os)
    }
  }
}
