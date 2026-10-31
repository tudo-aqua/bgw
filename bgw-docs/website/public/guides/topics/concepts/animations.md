[AnimationKDoc]: /docs/tools.aqua.bgw.animation/-animation/
[MovementAnimationKDoc]: /docs/tools.aqua.bgw.animation/-movement-animation/
[RotationAnimationKDoc]: /docs/tools.aqua.bgw.animation/-rotation-animation/
[FlipAnimationKDoc]: /docs/tools.aqua.bgw.animation/-flip-animation/
[FadeAnimationKDoc]: /docs/tools.aqua.bgw.animation/-fade-animation/
[RandomizeAnimationKDoc]: /docs/tools.aqua.bgw.animation/-randomize-animation/
[DiceAnimationKDoc]: /docs/tools.aqua.bgw.animation/-dice-animation/
[DelayAnimationKDoc]: /docs/tools.aqua.bgw.animation/-delay-animation/
[SequentialKDoc]: /docs/tools.aqua.bgw.animation/-sequential-animation/
[ParallelKDoc]: /docs/tools.aqua.bgw.animation/-parallel-animation/
[GameComponentViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/
[lockKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/lock.html
[unlockKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/unlock.html
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/
[runOnGUIThreadKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/-companion/run-on-g-u-i-thread.html
[VisualDoc]: /guides/concepts/visual
[DelayAnimationDoc]: /guides/concepts/animations#delayanimation
[Animations]: /docs/tools.aqua.bgw.animation
[ComponentViewDoc]: /guides/components/componentview
[ScaleAnimationKDoc]: /docs/tools.aqua.bgw.animation/-scale-animation/
[DiceViewKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-dice-view/
[AnimationInterpolationKDoc]: /docs/tools.aqua.bgw.core/-animation-interpolation/
[DragDropDoc]: /guides/concepts/drag-and-drop
[HandlingEvents]: /guides/handling-events

# Animations

<tldr>Animations for smooth visual changes and delayed code execution</tldr>

## Introduction

In this section the different types of animations in BGW are explained. Animations are mostly used to move game elements or change their [Visual][VisualDoc].
Additionally, the [DelayAnimation][DelayAnimationDoc] can be used to delay code execution to enable the player to see what is happening on the table.

## Playing Animations

Animations can be played using the `playAnimation()` method, which is available in all classes that inherit from [BoardGameScene][BoardGameSceneKDoc]. This method takes an [Animation][AnimationKDoc] as a parameter and plays it asynchronously. This means that the code after the `playAnimation()` call will be executed immediately and not wait for the animation to finish. To execute code after the animation has finished, it has to be passed in the `onFinished` listener of the animation, which is explained in more detail in the [Listeners](#listeners) section.

The `playAnimation()` method returns a boolean value indicating whether the animation was successfully started. If the method returns `false`, the animation is not played and no component is changed. This happens when

- The animation, or one of its children, is already running,
- The same animation instance is contained more than once in a compound animation, or
- A component of the animation is already animating. Wait for its `onFinished` or chain the animations with a [SequentialAnimation][SequentialKDoc] or [ParallelAnimation][ParallelKDoc].

Animations are also validated when they are created: durations and `speed` must be greater than zero and compound animations must not be empty.

## Stopping Animations

To stop all running animations, the method `stopAllAnimations()` may be used. It immediately stops everything that is playing and **does not** invoke any `onFinished` handler. Animations that already finished and persisted their result are not reverted.

---

## Properties

All [Animations][AnimationKDoc] share the same base property. For a complete list of all properties refer to each individual [API reference][Animations].

<signature>
<code-block lang="kotlin" copy="false">
isRunning : Boolean
</code-block>

It returns the running state of a single animation. It can be used on all types of animations, including compound animations like [SequentialAnimation][SequentialKDoc] and [ParallelAnimation][ParallelKDoc]. For these compound animations, this property returns `true` if at least one of the contained animations is running.

</signature>

#### ComponentAnimations

ComponentAnimations are animations that affect a [ComponentView][ComponentViewDoc] in some way. They can be used to move, rotate or flip a component, to randomly switch between visuals or to animate a dice. They hold additional properties to control the animation:

<signature>
<code-block lang="kotlin" copy="false">
componentView : T
</code-block>

This property holds the [ComponentView][ComponentViewDoc] that is affected by this animation. It is used in all ComponentAnimations to specify which component should be animated.

> For [DiceAnimation][DiceAnimationKDoc] this property is called `dice` of type `DiceView`, while for the [FlipAnimation][FlipAnimationKDoc] and [RandomizeAnimation][RandomizeAnimationKDoc] it is called `gameComponentView` with type `GameComponentView` instead of `componentView`, because these animations are specifically designed and therefore have a more specific property name and type.
> {style="warning"}

</signature>

<signature>
<code-block lang="kotlin" copy="false">
duration : Int
</code-block>

Specifies the duration of the animation in milliseconds. This property is used in all ComponentAnimations to control how long the animation should run.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
persist : Boolean
</code-block>

This property specifies whether the changed component state should automatically persist after the animation has finished. Default is `true`, if set to `false`, all animated properties of the [ComponentView][ComponentViewDoc] will be reverted to the latest state after the animation finished.

> Changing animating properties during an animation might cause unwanted effects upon finish, because the animation will add the changed values to the latest state of the component after the animation finished, which might not be the intended behavior if the component properties got changed during the animation.
> {style="warning"}

> An alternative to using `persist` is to manually update the component state in the `onFinished` listener of the animation, which gives more control over the final state of the component after the animation finished and also allows to trigger other code after the animation finished.
> {style="note"}

</signature>

#### SteppedComponentAnimations

SteppedComponentAnimations are special cases of ComponentAnimations that change the visual of a component in steps, like the [RandomizeAnimation][RandomizeAnimationKDoc] and the [DiceAnimation][DiceAnimationKDoc]. They have an additional property `speed` that controls how fast the animation should switch between the visuals.

<signature>
<code-block lang="kotlin" copy="false">
speed : Int
</code-block>

This property specifies the amount of steps during the `duration` of the animation and can be used to control how fast the animation should switch between the visuals. The time each visual is visible can be calculated as _timePerStep = duration / speed_.

</signature>

---

## Listeners

The following additional event handler is available for every [Animation][AnimationKDoc].

<signature>
<code-block lang="kotlin" copy="false">
onFinished : ((AnimationFinishedEvent) -&gt; Unit)?
</code-block>

This listener gets invoked when the animation finishes. It should be used to trigger code that should be executed after the animation has finished. For example, if a [DelayAnimation][DelayAnimationKDoc] is used to delay code execution, the code that should be executed after the delay can be passed in this listener.

> Note that playing animations is asynchronous, so the code after the `playAnimation` call will be executed immediately and not wait for the animation to finish. To execute code after the animation has finished, it has to be passed in the `onFinished` listener.
> {style="note"}

</signature>

<br>

---

# ComponentAnimations

<tldr>Animations that affect a ComponentView</tldr>

ComponentAnimations are animations that affect a [ComponentView][ComponentViewDoc] in some way. They can be used to move, rotate, scale or fade a component, to flip it, to randomly switch between visuals or to animate a dice.

> The following examples are accurate representations of BGW components and animations based on the provided code snippets. Press **Animate** to play them and **Reset** to start over. The previews show the animation and its result, the code that follows `playAnimation` in a real application is identical.
> {style="warning"}

## MovementAnimation

Animate the movement of a component.

A [MovementAnimation][MovementAnimationKDoc] moves a [ComponentView][ComponentViewDoc].
The movement can be passed as `fromX`/`toX` and `fromY`/`toY`, or relative to the current position with `byX`/`byY`. Positions are relative to the parent of the component, like `posX` and `posY`.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.movementAnimationTo" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.greenToken,tools.aqua.bgw.main.examples.ExampleAnimationScene.movementAnimationTo">
val greenToken = TokenView(
    width = 100, 
    height = 100, 
    visual = ColorVisual(Color(0xc6ff6e))
)
&nbsp;
playAnimation(
    MovementAnimation(
        componentView = greenToken,
        byX = 100,
        duration = 1000
    )
)
</previews>

To move a component to the position of **another** component, even if they have different parents, use `MovementAnimation.toComponentView()`. It calculates the required offset for you, taking rotation and scaling of the parents into account. Both components have to be part of the scene.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.area" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.area,tools.aqua.bgw.main.examples.ExampleAnimationScene.moveToComponentAnimation">
// token1 and token4 are both inside the same Area
playAnimation(
    MovementAnimation.toComponentView(
        componentView = token1,
        toComponentViewPosition = token4,
        scene = this,
        duration = 1500
    )
)
</previews>

---

## RotationAnimation

Animate the rotation of a component.

A [RotationAnimation][RotationAnimationKDoc] rotates a [ComponentView][ComponentViewDoc].
The rotation can be passed as `fromAngle`/`toAngle` or relative to the current rotation with `byAngle`. Angles are given in degrees.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.rotateAnimationBy" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.blueToken,tools.aqua.bgw.main.examples.ExampleAnimationScene.rotateAnimationBy">
val blueToken = TokenView(
    width = 100, 
    height = 100, 
    visual = ColorVisual(Color(0x6dbeff))
)
&nbsp;
playAnimation(
    RotationAnimation(
        componentView = blueToken,
        byAngle = 225.0,
        duration = 1000
    )
)
</previews>

---

## ScaleAnimation

Animate the size of a component.

A [ScaleAnimation][ScaleAnimationKDoc] scales a [ComponentView][ComponentViewDoc] without changing its `width` and `height`. The scale can be passed as `fromScaleX`/`toScaleX` and `fromScaleY`/`toScaleY`, or as factor relative to the current scale: `byScale` scales both axes, `byScaleX` and `byScaleY` one axis each.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.scaleAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.orangeToken,tools.aqua.bgw.main.examples.ExampleAnimationScene.scaleAnimation">
val orangeToken = TokenView(
    width = 100,
    height = 100,
    visual = ColorVisual(Color(0xfa6c56))
)
&nbsp;
playAnimation(
    ScaleAnimation(
        componentView = orangeToken,
        byScale = 1.5,
        duration = 1000
    )
)
</previews>

> `byScale = 1.5` multiplies the current scale by 1.5. A value of `0` means "no change".
> {style="note"}

---

## FadeAnimation

Animate the opacity of a component.

A [FadeAnimation][FadeAnimationKDoc] changes the opacity of a [ComponentView][ComponentViewDoc] to create a fading effect. The animation can be passed as `fromOpacity`/`toOpacity`.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.fadeAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.purpleToken,tools.aqua.bgw.main.examples.ExampleAnimationScene.fadeAnimation">
val purpleToken = TokenView(
    width = 100, 
    height = 100, 
    visual = ColorVisual(Color(0xbb6dff))
)
&nbsp;
playAnimation(
    FadeAnimation(
        componentView = purpleToken,
        toOpacity = 0.25,
        duration = 1000
    )
)
</previews>

---

## Interpolation

[MovementAnimation][MovementAnimationKDoc], [RotationAnimation][RotationAnimationKDoc], [ScaleAnimation][ScaleAnimationKDoc] and [FadeAnimation][FadeAnimationKDoc] all have an additional property `interpolation` that can be used to control the interpolation of the animation. Interpolation defines how the animation progresses over time, by default this is set to `AnimationInterpolation.SMOOTH`. If a different [interpolation][AnimationInterpolationKDoc] is desired, it can be set to one of the following values:

- `LINEAR`: The animation progresses at a constant speed from start to finish.
- `SMOOTH`: The animation starts slowly, is fastest in the middle and slows down again at the end (ease in and out). This is the default.
- `SPRING`: The animation overshoots the target value and then bounces back to it, creating a spring-like effect.
- `STEPS`: The animation progresses in 10 discrete steps rather than smoothly.

The following preview rotates four tokens at the same time, from left to right with `LINEAR`, `SMOOTH`, `SPRING` and `STEPS`:

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.parallelInterpolationAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.area,tools.aqua.bgw.main.examples.ExampleAnimationScene.parallelInterpolationAnimation">
val greenToken = TokenView(
    width = 50,
    height = 50,
    visual = ColorVisual(Color(0xc6ff6e))
)
&nbsp;
val blueToken = TokenView(
    width = 50,
    height = 50,
    visual = ColorVisual(Color(0x6dbeff))
)
&nbsp;
val purpleToken = TokenView(
    width = 50,
    height = 50,
    visual = ColorVisual(Color(0xbb6dff))
)
&nbsp;
val orangeToken = TokenView(
    width = 50,
    height = 50,
    visual = ColorVisual(Color(0xfa6c56))
)
&nbsp;
playAnimation(
    ParallelAnimation(
        RotationAnimation(
            componentView = greenToken,
            byAngle = 225.0,
            duration = 1000,
            interpolation =
                AnimationInterpolation.LINEAR
        ),
        RotationAnimation(
            componentView = blueToken,
            byAngle = 225.0,
            duration = 1000,
            interpolation =
                AnimationInterpolation.SMOOTH
        ),
        RotationAnimation(
            componentView = purpleToken,
            byAngle = 225.0,
            duration = 1000,
            interpolation =
                AnimationInterpolation.SPRING
        ),
        RotationAnimation(
            componentView = orangeToken,
            byAngle = 225.0,
            duration = 1000,
            interpolation =
                AnimationInterpolation.STEPS
        )
    )
)
</previews>

---

## FlipAnimation

Flip a game component to another visual.

A [FlipAnimation][FlipAnimationKDoc] works on a [GameComponentView][GameComponentViewKDoc]. It shows the `fromVisual`, contracts the component in the first half of the duration, switches to the `toVisual` and expands it again in the second half. `fromVisual` defaults to the current visual of the component.

When using `persist = true` on a `CardView` and `toVisual` is one side of the card, the card will be flipped to this side after the animation finished.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.flipAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.card,tools.aqua.bgw.main.examples.ExampleAnimationScene.flipAnimation">
val card = CardView(
    width = 120,
    height = 200,
    front = ImageVisual("fortify.png"),
    back = ImageVisual("back.png")
)
&nbsp;
playAnimation(
    FlipAnimation(
        gameComponentView = card,
        fromVisual = card.backVisual,
        toVisual = card.frontVisual,
        duration = 1000
    )
)
</previews>

---

## SteppedComponentAnimations

The [RandomizeAnimation][RandomizeAnimationKDoc] and the [DiceAnimation][DiceAnimationKDoc] change the visual of a [GameComponentView][GameComponentViewKDoc] in many small steps to create a shuffling effect. In total `speed` changes happen within the `duration`, so each visual is visible for _duration / speed_ milliseconds. The last step is always the visual that you chose, which gives you full control over the outcome.

### RandomizeAnimation
A [RandomizeAnimation][RandomizeAnimationKDoc] randomly switches between the given `visuals` and ends with `toVisual`.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.randomizeAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.randomizeToken,tools.aqua.bgw.main.examples.ExampleAnimationScene.randomizeAnimation">
val visuals = listOf(
    ColorVisual(Color(0xc6ff6e)),
    ColorVisual(Color(0xffc656)),
    ColorVisual(Color(0xfa6c56)),
    ColorVisual(Color(0xef4444)),
    ColorVisual(Color(0xbb6dff)),
    ColorVisual(Color(0x6dbeff))
)
&nbsp;
val token = TokenView(
    width = 100,
    height = 100,
    visual = visuals.first()
)
&nbsp;
playAnimation(
    RandomizeAnimation(
        gameComponentView = token,
        visuals = visuals,
        toVisual = visuals[3],
        duration = 1500,
        speed = 20
    )
)
</previews>

### DiceAnimation
A [DiceAnimation][DiceAnimationKDoc] is a RandomizeAnimation made for a [DiceView][DiceViewKDoc]. As the visuals were already passed when the dice was created, only the resulting side as zero-based index is needed. After a persisted DiceAnimation, the `currentSide` of the dice is `toSide`.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.diceAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.diceCustomSide,tools.aqua.bgw.main.examples.ExampleAnimationScene.diceAnimation">
val dice = DiceView(
    width = 100,
    height = 100,
    visuals = listOf(
        ColorVisual(Color(0xc6ff6e)),
        ColorVisual(Color(0xffc656)),
        ColorVisual(Color(0xfa6c56)),
        ColorVisual(Color(0xef4444)),
        ColorVisual(Color(0xbb6dff)),
        ColorVisual(Color(0x6dbeff))
    )
)
&nbsp;
playAnimation(
    DiceAnimation(
        dice = dice,
        toSide = 3,
        speed = 20,
        duration = 1000
    )
)
</previews>

---

## Persisting the result

By default, a ComponentAnimation **persists** its result: after it finished, the component keeps the animated state. A token that was moved by 100 units stays 100 units further right, and the `posX` property reflects this. Set `persist = false` to let the component return to its previous state instead.

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.persistAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.orangeToken,tools.aqua.bgw.main.examples.ExampleAnimationScene.persistAnimation">
playAnimation(
    MovementAnimation(
        componentView = orangeToken,
        byX = 100,
        duration = 1000,
        persist = false
    )
)
</previews>

> The previews in this guide always show the final state until you press **Reset**. With `persist = false`, the token jumps back right after the animation, because nothing changed in the component itself.
> {style="note"}

---

## CompoundAnimations

Compound animations combine other animations. They are animations themselves, so they can be played, stopped and listened to like any other animation. They are the **only** way to animate one component with several animations at the same time or one after another: playing a second animation on a component that is already animating is rejected.

### SequentialAnimation
A SequentialAnimation plays its animations one after another. Its `duration` is the sum of all durations. This is useful to build complex movements out of simple parts. The token below first moves, then rotates and finally fades:

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.sequentialAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.sequentialToken,tools.aqua.bgw.main.examples.ExampleAnimationScene.sequentialAnimation">
playAnimation(
    SequentialAnimation(
        MovementAnimation(
            componentView = token,
            byX = 150,
            duration = 800
        ),
        RotationAnimation(
            componentView = token,
            byAngle = 180.0,
            duration = 800
        ),
        FadeAnimation(
            componentView = token,
            toOpacity = 0.25,
            duration = 800
        )
    )
)
</previews>

### ParallelAnimation
A ParallelAnimation plays all its animations at the same time. Its `duration` is the longest of the contained durations. Each animation uses its own duration, so they do not have to end together:

<previews key="tools.aqua.bgw.main.examples.ExampleAnimationScene.parallelAnimation" codepoints="tools.aqua.bgw.main.examples.ExampleAnimationScene.parallelToken,tools.aqua.bgw.main.examples.ExampleAnimationScene.parallelAnimation">
playAnimation(
    ParallelAnimation(
        MovementAnimation(
            componentView = token,
            byX = 150,
            duration = 1500
        ),
        RotationAnimation(
            componentView = token,
            byAngle = 180.0,
            duration = 1000
        ),
        FadeAnimation(
            componentView = token,
            toOpacity = 0.25,
            duration = 500
        )
    )
)
</previews>

Both compound animations can be nested into each other. Every contained animation has its own `onFinished` handler, in addition to the one of the compound animation:

```kotlin
playAnimation(
    SequentialAnimation(
        DelayAnimation(duration = 1000).apply {
            onFinished = { println("First DelayAnimation finished!") }
        },
        DelayAnimation(duration = 2000).apply {
            onFinished = { println("Second DelayAnimation finished!") }
        }
    ).apply {
        onFinished = { println("SequentialAnimation finished!") }
    }
)
```

The resulting SequentialAnimation plays for 3000 ms. Replace it with a ParallelAnimation and it plays for 2000 ms.

---

## DelayAnimation

A [DelayAnimation][DelayAnimationKDoc] does nothing visible. It calls `onFinished` after the given `duration`, which makes it a timer that runs alongside the application. It is useful to give the player time to see what happened before the game continues, for example before the opponent makes a move.

To block the player from interacting during the delay, [lock()][lockKDoc] the scene before playing the animation and [unlock()][unlockKDoc] it in `onFinished`:

```kotlin
lock()
playAnimation(DelayAnimation(duration = 2000).apply {
    onFinished = {
        // Do stuff here
        unlock()
    }
})
```

> A DelayAnimation can also be part of a SequentialAnimation or ParallelAnimation. For example, a delay in front of a MovementAnimation makes it start later.
> {style="note"}

---

## Reacting to animations

The `onFinished` handler is the central tool to run code at the right moment. The [Handling Events][HandlingEvents] guide uses it to update the score of Dice Duel after the dice have stopped. As playing animations is asynchronous, never read properties that an animation is about to change directly after `playAnimation()`: use `onFinished`.

Combine animations with [Drag and Drop][DragDropDoc] to let a component slide into place after it was released.
