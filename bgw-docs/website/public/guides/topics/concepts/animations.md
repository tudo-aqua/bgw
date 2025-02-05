[AnimationKDoc]: /docs/tools.aqua.bgw.animation/-animation/
[MovementAnimationKDoc]: /docs/tools.aqua.bgw.animation/-movement-animation/
[RotationAnimationKDoc]: /docs/tools.aqua.bgw.animation/-rotation-animation/
[FlipAnimationKDoc]: /docs/tools.aqua.bgw.animation/-flip-animation/
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

> This guide is currently being rewritten. Content may be incomplete, incorrect or subject to change.
> {style="danger"}

# Animations

<tldr>Animations for moving and changing game elements</tldr>

## Introduction

In this section the different types of animations in the BGW framework are shown. Visuals are mostly used
to move game elements or change their [Visual][VisualDoc].
Additionally, the [DelayAnimation][DelayAnimationDoc]
can be used to delay code execution to enable the player to see what is happening on
the table.

![](animations.gif)

Each [Animation][AnimationKDoc] has a duration, a running attribute and an `onFinished` listener that gets invoked after the animation has finished.

> Animations can only be played in a [BoardGameScene][BoardGameSceneKDoc].
> {style="warning"}

---

## [SequentialAnimation][SequentialKDoc]

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

## [ParallelAnimation][ParallelKDoc]

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

## [MovementAnimation][MovementAnimationKDoc]

A [MovementAnimation][MovementAnimationKDoc] moves a [GameComponentView][GameComponentViewKDoc].
The movement can be passed as `fromX`/`toX`, `fromY`/`toY` or relative to the current position with `byX`/`byY`.

> The Animation only moves the component in the current Scene and **does not update its position**. The Component will snap back upon next refresh if the new position is not set in `onFinished`, which is the suggested way of usage.
> {style="warning"}

```kotlin
playAnimation(
    MovementAnimation(
        componentView = component,
        byX = 0,
        byY = -50,
        duration = 1000
    ).apply {
        onFinished = {
            component.posY -= 50
        }
    }
)
```

Additionally, a component can be moved to another component's location. This is for example useful to animate moving
cards onto a card stack:

```kotlin
playAnimation(
    MovementAnimation.toComponentView(
        componentView = card,
        toComponentViewPosition = cardStack,
        scene = gameScene,
        duration = 1000
    ).apply {
        onFinished = {
            card.removeFromParent()
            cardStack.add(card)
        }
    }
)
```

## [RotationAnimation][RotationAnimationKDoc]

A [RotationAnimation][RotationAnimationKDoc] rotates a [GameComponentView][GameComponentViewKDoc].
The rotation can be passed as `fromAngle`/`toAngle` or relative to the current rotation with `byAngle`.

> The Animation only rotates the component in the current Scene and **does not update its rotation**. The Component will snap back upon next refresh if the new rotation is not set in `onFinished`, which is the suggested way of usage.
> {style="warning"}

```kotlin
playAnimation(
    RotationAnimation(
        componentView = component,
        byAngle = 180,
        duration = 1000
    ).apply {
        onFinished = {
            component.rotation = (component.rotation + 180) % 360
        }
    }
)
```

## [FlipAnimation][FlipAnimationKDoc]

A [FlipAnimation][FlipAnimationKDoc] switches between two visuals in a flipping-like animation. The animation sets the
`fromVisual` and then switches to the `toVisual`.

> The Animation only switches the component visually but **does not update the actual visual property**. The Visual will revert to the `currentVisual` upon next refresh if the new visual is not set in `onFinished`, which is the suggested way of usage.
> {style="warning"}

```kotlin
playAnimation(
    FlipAnimation(
        gameComponentView = card,
        fromVisual = backSide,
        toVisual = frontSide,
        duration = 1000
    ).apply {
        onFinished = {
            card.visual = frontSide
        }
    }
)
```

## [RandomizeAnimation][RandomizeAnimationKDoc]

A [RandomizeAnimation][RandomizeAnimationKDoc] randomly switches between the given visuals. The time each visual is visible can be set by
passing `steps`, which is calculated like the following: _steps: time = duration / steps_. The animation sets the
`toVisual` as the last step in order to control what the resulting visual of this animation is.

> The Animation only switches the component visually but **does not update the actual visual property**. The Visual will revert to the `currentVisual` upon next refresh if the new visual is not set in `onFinished`, which is the suggested way of usage.
> {style="warning"}

```kotlin
playAnimation(
    RandomizeAnimation(
        gameComponentView = card,
        visual = allCardFaces,
        toVisual = allCardFaces[3],
        duration = 1000
    ).apply {
        onFinished = {
            card.visual = allCardFaces[3]
        }
    }
)
```

## [DiceAnimation][DiceAnimationKDoc]

A [DiceAnimation][DiceAnimationKDoc] behaves like a [RandomizeAnimation][RandomizeAnimationKDoc] specifically for
dices. As the visuals got passed as parameter on Dice creation only the resulting side as zero-based index needs to be
passed. The animation sets the `toSide` as the last step in order to control what the resulting visual of this
animation is.

> The Animation only switches the component visually but **does not update the actual visual property**. The Visual will however revert to the `currentVisual` upon next refresh if the new visual is not set in `onFinished`, which is the suggested way of usage.
> {style="warning"}

```kotlin
playAnimation(
    DiceAnimation(
        dice = dice,
        toSide = 3,
        duration = 1000
    ).apply {
        onFinished = {
            card.currentSide = 3
        }
    }
)
```
