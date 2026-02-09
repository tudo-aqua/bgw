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
[VisualDoc]: /guides/visual/visual
[DelayAnimationDoc]: concepts/animations/Animations#delayanimation
[Animations]: /docs/tools.aqua.bgw.animation
[ComponentViewDoc]: /guides/components/componentview

# Animations

<tldr>Animations for smooth visual changes and delayed code execution</tldr>

## Introduction

In this section the different types of animations in BGW are explained. Animations are mostly used to move game elements or change their [Visual][VisualDoc].
Additionally, the [DelayAnimation][DelayAnimationDoc] can be used to delay code execution to enable the player to see what is happening on the table.

## Playing Animations

Animations can be played using the `playAnimation()` method, which is available in all classes that inherit from [BoardGameScene][BoardGameSceneKDoc]. This method takes an [Animation][AnimationKDoc] as a parameter and plays it asynchronously. This means that the code after the `playAnimation()` call will be executed immediately and not wait for the animation to finish. To execute code after the animation has finished, it has to be passed in the `onFinished` listener of the animation, which is explained in more detail in the [Listeners](#listeners) section.

The `playAnimation()` method returns a boolean value indicating whether the animation was successfully started. If the method returns `false`, it means that the animation could not be started, for example because a component already has a running animation that conflicts with the new one. In this case, the animation will not be played and the state of the component will not be changed.

## Stopping Animations

To stop all running animations, the method `stopAllAnimations()` may be used. This will immediately stop all running animations and revert all animated properties of the components to their latest state.

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
onFinished : ((AnimationFinishedEvent) -> Unit)?
</code-block>

This listener gets invoked when the animation finishes. It should be used to trigger code that should be executed after the animation has finished. For example, if a [DelayAnimation][DelayAnimationKDoc] is used to delay code execution, the code that should be executed after the delay can be passed in this listener.

> Note that playing animations is asynchronous, so the code after the `playAnimation` call will be executed immediately and not wait for the animation to finish. To execute code after the animation has finished, it has to be passed in the `onFinished` listener.
> {style="note"}
> </signature>

</signature>

<br>

# ComponentAnimations

<tldr>Animations that affect a ComponentView</tldr>

ComponentAnimations are animations that affect a [ComponentView][ComponentViewDoc] in some way. They can be used to move, rotate or flip a component, to randomly switch between visuals or to animate a dice.

> The following examples are accurate representations of BGW components and animations based on the provided code snippets.
> All animations provided in this guide are purely illustrative and persist the final state until reset, for ease of understanding.
> {style="warning"}

## [MovementAnimation][MovementAnimationKDoc]

<tldr>Animate the movement of a component</tldr>

A [MovementAnimation][MovementAnimationKDoc] moves a [ComponentView][ComponentViewDoc].
The movement can be passed as `fromX`/`toX`, `fromY`/`toY` or relative to the current position with `byX`/`byY`.

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

---

## [RotationAnimation][RotationAnimationKDoc]

<tldr>Animate the rotation of a component</tldr>

A [RotationAnimation][RotationAnimationKDoc] rotates a [ComponentView][ComponentViewDoc].
The rotation can be passed as `fromAngle`/`toAngle` or relative to the current rotation with `byAngle`.

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

## [FadeAnimation][FadeAnimationKDoc]

<tldr>Animate the opacity of a component</tldr>

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

#### Interpolation

[MovementAnimation][MovementAnimationKDoc], [RotationAnimation][RotationAnimationKDoc] and [FadeAnimation][FadeAnimationKDoc] all have an additional property `interpolation` that can be used to control the interpolation of the animation. Interpolation defines how the animation progresses over time, by default this is set to `AnimationInterpolation.SMOOTH`, which means that the animation will start and end slowly and be faster in the middle. If a different interpolation is desired, it can be set to one of the following values:

- `LINEAR`: The animation progresses at a constant speed from start to finish.
- `SMOOTH`: The animation starts slowly and then accelerates towards the end.
- `SPRING`: The animation overshoots the target value and then bounces back to it, creating a spring-like effect.
- `STEPS`: The animation progresses in 10 discrete steps rather than smoothly.

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
val linearAnimation = playAnimation(
    RotationAnimation(
        componentView = greenToken,
        byAngle = 225.0,
        duration = 1000,
        interpolation = AnimationInterpolation.LINEAR
    )
)
&nbsp;
val smoothAnimation = playAnimation(
    RotationAnimation(
        componentView = blueToken,
        byAngle = 225.0,
        duration = 1000,
        interpolation = AnimationInterpolation.SMOOTH
    )
)
&nbsp;
val springAnimation = playAnimation(
    RotationAnimation(
        componentView = purpleToken,
        byAngle = 225.0,
        duration = 1000,
        interpolation = AnimationInterpolation.SPRING
    )
)
&nbsp;
val stepsAnimation = playAnimation(
    RotationAnimation(
        componentView = orangeToken,
        byAngle = 225.0,
        duration = 1000,
        interpolation = AnimationInterpolation.STEPS
    )
)
</previews>

---

### [FlipAnimation][FlipAnimationKDoc]

A [FlipAnimation][FlipAnimationKDoc] switches between two visuals in a flipping-like animation. The animation sets the
`fromVisual` and then switches to the `toVisual`. When using `persist = true` on a `CardView` and `toVisual` is one side of the card, the card will be flipped to this side after the animation finished.

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

### [RandomizeAnimation][RandomizeAnimationKDoc]

A [RandomizeAnimation][RandomizeAnimationKDoc] randomly switches between the given visuals. The time each visual is visible can be set by
passing `steps`, which is calculated like the following: _steps: time = duration / steps_. The animation sets the
`toVisual` as the last step in order to control what the resulting visual of this animation is.

```kotlin
playAnimation(
    RandomizeAnimation(
        gameComponentView = card,
        visual = allCardFaces,
        toVisual = allCardFaces[3],
        duration = 1000
    )
)
```

### [DiceAnimation][DiceAnimationKDoc]

A [DiceAnimation][DiceAnimationKDoc] behaves like a [RandomizeAnimation][RandomizeAnimationKDoc] specifically for
dices. As the visuals got passed as parameter on Dice creation only the resulting side as zero-based index needs to be
passed. The animation sets the `toSide` as the last step in order to control what the resulting visual of this
animation is.

```kotlin
playAnimation(
    DiceAnimation(
        dice = dice,
        toSide = 3,
        duration = 1000
    )
)
```

## CompoundAnimations

### [SequentialAnimation][SequentialKDoc]

A SequentialAnimation is an [Animation][AnimationKDoc] that consists of multiple animations, that get played in sequence.
This is useful to combine multiple animations into a single one.
An example on how to create and play a SequentialAnimation can be found below:

```kotlin
playAnimation(
    SequentialAnimation(
        DelayAnimation(duration = 1000).apply {
            onFinished = { println("First DelayAnimation finished!") }
        },
        DelayAnimation(duration = 2000).apply {
            onFinished = { println("Second DelayAnimation finished!") }
        },
    ).apply {
        onFinished = { println("SequentialAnimation finished!") }
    }
)
```

The resulting SequentialAnimation will play for 3000ms and print some information on which Animations have finished playing.

### [ParallelAnimation][ParallelKDoc]

A ParallelAnimation is an [Animation][AnimationKDoc] that consists of multiple animations, that get played in parallel.
This is useful to combine multiple animations into a single one.
An example on how to create and play a ParallelAnimation can be found below:

```kotlin
playAnimation(
    ParallelAnimation(
        DelayAnimation(duration = 1000).apply {
            onFinished = { println("First DelayAnimation finished!") }
        },
        DelayAnimation(duration = 2000).apply {
            onFinished = { println("Second DelayAnimation finished!") }
        },
    ).apply {
        onFinished = { println("ParallelAnimation finished!") }
    }
)
```

The resulting ParallelAnimation will play for 2000 milliseconds and print some information on which animations have finished playing.

## [DelayAnimation][DelayAnimationKDoc]

A DelayAnimation does nothing in the application window besides calling `onFinished` after the given amount of time
(duration parameter). This timer runs asynchronously, so it can run while the player is playing. To add a delay between
moves in which the user should not be able to interact with the scene, the method [BoardGameScene#lock][lockKDoc]
has to be called before playing the animation and the method [BoardGameScene#unlock][unlockKDoc]
in `onFinished`.
An example for this locking mechanism can be found below:

```kotlin
lock()
playAnimation(DelayAnimation(duration = 2000).apply {
    onFinished = {
        // Do stuff here
        unlock()
    }
})
```
