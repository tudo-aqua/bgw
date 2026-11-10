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

package tools.aqua.bgw.frontend

import kotlin.math.abs
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.openqa.selenium.JavascriptExecutor
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.animation.FadeAnimation
import tools.aqua.bgw.animation.MovementAnimation
import tools.aqua.bgw.animation.ParallelAnimation
import tools.aqua.bgw.animation.RotationAnimation
import tools.aqua.bgw.animation.ScaleAnimation
import tools.aqua.bgw.animation.SequentialAnimation
import tools.aqua.bgw.components.gamecomponentviews.TokenView
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.visual.ColorVisual

/**
 * Samples the rendered state of a component on every frame while an animation plays. Unlike the
 * final-state checks in [AnimationBrowserTest], these tests catch animations that jump, restart
 * from their initial value or cancel each other, which happened when an animation tree contained
 * several animations of the same kind on the same component.
 */
class AnimationContinuityBrowserTest : BrowserTestBase() {

  /** One rendered frame: time, center x/y in scene units, rotation, scale and opacity. */
  private data class Frame(
      val time: Double,
      val x: Double,
      val y: Double,
      val rotation: Double,
      val scale: Double,
      val opacity: Double
  )

  @Test
  fun sequentialMovementsContinueFromThePreviousOne() {
    val frames = record { token ->
      SequentialAnimation(
          MovementAnimation(token, byX = 200, duration = 400),
          MovementAnimation(token, byX = 200, duration = 400))
    }
    assertContinuous(frames, Frame::x, maxStep = 40.0)
    assertMonotonic(frames, Frame::x)
    assertReached(frames, Frame::x, 300.0, "x after the first movement")
    assertEquals(500.0, frames.last().x, 1.0)
  }

  @Test
  fun sequentialRotationsContinueFromThePreviousOne() {
    val frames = record { token ->
      SequentialAnimation(
          RotationAnimation(token, byAngle = 80.0, duration = 400),
          RotationAnimation(token, byAngle = 80.0, duration = 400))
    }
    assertContinuous(frames, Frame::rotation, maxStep = 20.0)
    assertMonotonic(frames, Frame::rotation)
    assertEquals(160.0, frames.last().rotation, 0.5)
  }

  @Test
  fun sequentialFadesAreBothVisible() {
    val frames = record { token ->
      SequentialAnimation(
          FadeAnimation(token, toOpacity = 0.0, duration = 400),
          FadeAnimation(token, toOpacity = 1.0, duration = 400))
    }
    assertContinuous(frames, Frame::opacity, maxStep = 0.2)
    assertTrue(frames.minOf { it.opacity } < 0.1, "The component never faded out")
    assertEquals(1.0, frames.last().opacity, 0.01)
  }

  @Test
  fun sequentialScalesMultiply() {
    val frames = record { token ->
      SequentialAnimation(
          ScaleAnimation(token, toScaleX = 1.5, toScaleY = 1.5, duration = 400),
          ScaleAnimation(token, toScaleX = 1.5, toScaleY = 1.5, duration = 400))
    }
    assertContinuous(frames, Frame::scale, maxStep = 0.1)
    assertMonotonic(frames, Frame::scale)
    assertEquals(2.25, frames.last().scale, 0.01)
  }

  @Test
  fun parallelMovementsOfTheSameComponentAreCombined() {
    val frames = record { token ->
      ParallelAnimation(
          MovementAnimation(token, byX = 200, duration = 500),
          MovementAnimation(token, byY = 200, duration = 500))
    }
    assertContinuous(frames, Frame::x, maxStep = 40.0)
    assertContinuous(frames, Frame::y, maxStep = 40.0)
    // Both movements progress together, i.e. the component moves diagonally.
    frames.forEach { assertEquals(it.x, it.y, 2.0, "Movement is not diagonal at t=${it.time}") }
    assertEquals(300.0, frames.last().x, 1.0)
    assertEquals(300.0, frames.last().y, 1.0)
  }

  /** The docs playground pauses and scrubs animations through `window.bgwAnimator`. */
  @Test
  fun playgroundControlsSeekSequentialAnimations() {
    val token = TokenView(posX = 50, posY = 50, width = 100, height = 100, visual = ColorVisual.RED)
    val scene = BoardGameScene(width = 1000, height = 1000).apply { addComponents(token) }
    show(scene)
    tester.awaitBGWComp(token.id)
    val js = tester.requireDriver() as JavascriptExecutor

    var finished = false
    val animation =
        SequentialAnimation(
                MovementAnimation(token, byX = 200, duration = 1000),
                MovementAnimation(token, byX = 200, duration = 1000))
            .apply { onFinished = { finished = true } }
    scene.playAnimation(animation)
    tester.await("animation controls") {
      js.executeScript("return !!window.bgwAnimator && window.bgwAnimator.active()") == true
    }

    fun centerAfterSeek(time: Int): Double {
      js.executeScript("window.bgwAnimator.pause(); window.bgwAnimator.seek(arguments[0]);", time)
      Thread.sleep(100)
      return (js.executeScript(
              """
              const r = document.getElementById(arguments[0]).getBoundingClientRect();
              const s = document.querySelector('bgw_game_scene bgw_scene').getBoundingClientRect();
              return (r.left + r.width / 2 - s.left) / (s.width / 1000);
              """,
              token.id) as Number)
          .toDouble()
    }

    assertEquals(300.0, centerAfterSeek(1000), 2.0, "end of the first movement")
    assertEquals(100.0, centerAfterSeek(0), 2.0, "start, after seeking backwards")
    val middle = centerAfterSeek(1500)
    assertTrue(middle in 310.0..490.0, "middle of the second movement was $middle")
    assertTrue(centerAfterSeek(1950) > 480.0, "close to the end of the second movement")

    js.executeScript("window.bgwAnimator.resume();")
    tester.await("animation completion") { finished }
    assertEquals(450.0, token.posX)
  }

  private fun record(build: (TokenView) -> Animation): List<Frame> {
    val token = TokenView(posX = 50, posY = 50, width = 100, height = 100, visual = ColorVisual.RED)
    val scene = BoardGameScene(width = 1000, height = 1000).apply { addComponents(token) }
    show(scene)
    tester.awaitBGWComp(token.id)
    val js = tester.requireDriver() as JavascriptExecutor
    js.executeScript(
        """
        const id = arguments[0];
        window.__frames = [];
        window.__recording = true;
        const t0 = performance.now();
        function frame() {
          const el = document.getElementById(id);
          const sc = document.querySelector('bgw_game_scene bgw_scene');
          if (el && sc) {
            const sceneRect = sc.getBoundingClientRect();
            const unit = sceneRect.width / 1000;
            const r = el.getBoundingClientRect();
            const cs = getComputedStyle(el);
            const m = new DOMMatrixReadOnly(cs.transform === 'none' ? undefined : cs.transform);
            window.__frames.push([performance.now() - t0,
              (r.left + r.width / 2 - sceneRect.left) / unit,
              (r.top + r.height / 2 - sceneRect.top) / unit,
              Math.atan2(m.b, m.a) * 180 / Math.PI, Math.hypot(m.a, m.b), parseFloat(cs.opacity)]);
          }
          if (window.__recording) requestAnimationFrame(frame);
        }
        requestAnimationFrame(frame);
        """,
        token.id)

    var finished = false
    val animation = build(token).apply { onFinished = { finished = true } }
    scene.playAnimation(animation)
    tester.await("animation completion") { finished }
    // Wait for the final state of the JVM to be rendered, i.e. the animated values were replaced.
    Thread.sleep(400)
    js.executeScript("window.__recording = false;")

    @Suppress("UNCHECKED_CAST")
    val raw = js.executeScript("return window.__frames") as List<List<Number>>
    return raw.map { values ->
      val v = values.map { it.toDouble() }
      Frame(v[0], v[1], v[2], v[3], v[4], v[5])
    }
  }

  private fun assertContinuous(frames: List<Frame>, value: (Frame) -> Double, maxStep: Double) {
    frames.zipWithNext().forEach { (a, b) ->
      assertTrue(
          abs(value(b) - value(a)) <= maxStep,
          "Rendered value jumped from ${value(a)} to ${value(b)} at t=${b.time.toInt()}ms")
    }
  }

  private fun assertMonotonic(frames: List<Frame>, value: (Frame) -> Double) {
    frames.zipWithNext().forEach { (a, b) ->
      assertTrue(
          value(b) >= value(a) - 0.5,
          "Rendered value went back from ${value(a)} to ${value(b)} at t=${b.time.toInt()}ms")
    }
  }

  private fun assertReached(
      frames: List<Frame>,
      value: (Frame) -> Double,
      target: Double,
      what: String
  ) {
    assertTrue(frames.any { abs(value(it) - target) < 15.0 }, "Never rendered $what ($target)")
  }
}
