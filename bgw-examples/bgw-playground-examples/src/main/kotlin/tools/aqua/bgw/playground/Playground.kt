/*
 * Copyright 2026 The BoardGameWork Authors
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

package tools.aqua.bgw.playground

import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.components.ComponentView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameApplication
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual

internal const val SCENE_WIDTH = 1200
internal const val SCENE_HEIGHT = 800

internal data class StoryAction(val title: String, val run: (ComponentView) -> Unit)

internal data class ComponentStory(
    val name: String,
    val group: String,
    val description: String,
    val create: () -> ComponentView,
    val actions: List<StoryAction> = emptyList(),
)

internal object PlaygroundApplication :
    BoardGameApplication(
        windowTitle = "BGW Development Playground", width = SCENE_WIDTH, height = SCENE_HEIGHT) {
  fun showIndex() = showGameScene(IndexScene())

  fun showStory(story: ComponentStory) = showGameScene(StoryScene(story))
}

private class IndexScene :
    BoardGameScene(
        width = SCENE_WIDTH, height = SCENE_HEIGHT, background = ColorVisual(245, 247, 250)) {
  init {
    addComponents(
        Label(
            posX = 40,
            posY = 22,
            width = 1120,
            height = 55,
            text = "BGW component zoo",
            font = Font(size = 34, fontWeight = Font.FontWeight.BOLD)),
        Label(
            posX = 40,
            posY = 76,
            width = 1120,
            height = 35,
            text =
                "Every renderer has an isolated scene with live mutation and lifecycle controls.",
            font = Font(size = 18)))

    componentStories.forEachIndexed { index, story ->
      val column = index / 11
      val row = index % 11
      addComponents(
          Button(
                  posX = 45 + column * 375,
                  posY = 125 + row * 57,
                  width = 345,
                  height = 45,
                  text = "${story.group} / ${story.name}",
                  visual = ColorVisual.WHITE)
              .apply { onMouseClicked = { PlaygroundApplication.showStory(story) } })
    }
  }
}

private class StoryScene(private val story: ComponentStory) :
    BoardGameScene(
        width = SCENE_WIDTH, height = SCENE_HEIGHT, background = ColorVisual(245, 247, 250)) {
  private var subject: ComponentView = story.create()
  private var isSubjectPresent = true
  private val status =
      Label(
          posX = 40, posY = 735, width = 1120, height = 32, text = "Ready", font = Font(size = 16))

  init {
    subject.posX = 470.0
    subject.posY = 250.0
    addComponents(
        Label(
            posX = 40,
            posY = 25,
            width = 1120,
            height = 45,
            text = "${story.group} / ${story.name}",
            font = Font(size = 30, fontWeight = Font.FontWeight.BOLD)),
        Label(
            posX = 40,
            posY = 72,
            width = 1120,
            height = 42,
            text = story.description,
            font = Font(size = 17)),
        subject,
        status)

    addActionButton("Back", 40, 135) { PlaygroundApplication.showIndex() }
    addActionButton("Move", 40, 195) {
      subject.posX = if (subject.posX < 600) subject.posX + 180 else 470.0
    }
    addActionButton("Resize", 40, 255) {
      subject.width = if (subject.width < 300) subject.width + 80 else 180.0
      subject.height = if (subject.height < 220) subject.height + 40 else 100.0
    }
    addActionButton("Rotate", 40, 315) { subject.rotation = (subject.rotation + 45) % 360 }
    addActionButton("Fade", 40, 375) { subject.opacity = if (subject.opacity > 0.6) 0.35 else 1.0 }
    addActionButton("Visual", 40, 435) {
      runCatching {
            subject.visual =
                if (subject.visual == ColorVisual.CYAN) ColorVisual.ORANGE else ColorVisual.CYAN
          }
          .onFailure {
            status.text = "${story.name} manages its visual through component-specific state"
          }
    }
    addActionButton("Remove / add", 40, 495) {
      if (isSubjectPresent) removeComponents(subject) else addComponents(subject)
      isSubjectPresent = !isSubjectPresent
    }
    addActionButton("Reset", 40, 555) { resetSubject() }
    addActionButton("Animate", 40, 615) {
      playAnimation(
          MovementAnimation(componentView = subject, byX = 140, duration = 700, persist = false))
    }

    story.actions.forEachIndexed { index, action ->
      addActionButton(action.title, 255, 135 + index * 60) { action.run(subject) }
    }
  }

  private fun addActionButton(text: String, x: Int, y: Int, action: () -> Unit) {
    addComponents(
        Button(
                posX = x,
                posY = y,
                width = 190,
                height = 44,
                text = text,
                visual = ColorVisual.LIGHT_GRAY)
            .apply {
              onMouseClicked = {
                action()
                status.text = "$text applied to ${story.name}"
              }
            })
  }

  private fun resetSubject() {
    if (isSubjectPresent) removeComponents(subject)
    subject =
        story.create().apply {
          posX = 470.0
          posY = 250.0
        }
    addComponents(subject)
    isSubjectPresent = true
  }
}
