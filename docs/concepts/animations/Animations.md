---
parent: Concepts
title: Animations
nav_order: 2
layout: default
---

<!-- KDoc -->
[AnimationKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-animation/
[MovementAnimationKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-movement-animation/
[RotationAnimationKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-rotation-animation/
[FlipAnimationKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-flip-animation/
[RandomizeAnimationKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-randomize-animation/
[DiceAnimationKDoc]:https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-dice-animation/
[DelayAnimationKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-delay-animation/

[GameComponentViewKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/
[lockKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-scene/lock.html
[unlockKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-scene/unlock.html

<!-- GH-Pages Doc -->
[VisualDoc]: https://tudo-aqua.github.io/bgw/concepts/visual/visual.html
[DelayAnimationDoc]: https://tudo-aqua.github.io/bgw/concepts/animations/Animations.html#delayanimation

<!-- Start Page -->
## Animations
{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

In this section the different types of animations in the BGW framework are shown. Visuals are mostly used
to move game elements or change their [Visual][VisualDoc].
Additionally, the [DelayAnimation][DelayAnimationDoc]
can be used to delay code execution to enable the player to see what is happening on
the table.

![](animations.gif)

The full example of the gif above can be found here: [View it on GitHub](https://github.com/tudo-aqua/bgw/blob/main/bgw-docs-examples/src/main/kotlin/examples/concepts/animation/AnimationExample.kt){:
.btn }.

Each [Animation][AnimationKDoc] has a
duration, a running attribute and an ``onFinished`` EventHandler that gets invoked after the animation has finished.

## [DelayAnimation][DelayAnimationKDoc]

A DelayAnimation does nothing in the application window besides calling ``onFinished`` after the given amount of time 
(duration parameter). This timer runs asynchronously, so it can run while the player is playing. To add a delay between
moves in which the user should not be able to interact with the scene, the method [BoardGameScene#lock][lockKDoc]
has to be called before playing the animation and the method [BoardGameScene#unlock][unlockKDoc]
in ``onAnimationFinished``.
An example for this locking mechanism can be found below:

````kotlin
gameScene.lock()
gameScene.playAnimation(DelayAnimation(duration = 2000).apply {
  onFinished = {
    //Do stuff here
    gameScene.unlock()
  }
})
````

## [MovementAnimation][MovementAnimationKDoc]
A [MovementAnimation][MovementAnimationKDoc] moves a [GameComponentView][GameComponentViewKDoc].
The movement can be passed as ``fromX``/``toX``, ``fromY``/``toY`` or relative to the current position with ``byX``/``byY``.

**NOTE**: The Animation only moves the component in the current Scene and ***does not update its position***. The 
Component
will snap back upon next refresh if the new position is not set in ``onAnimationFinished``, which is the suggested way of
usage.

````kotlin
gameScene.playAnimation(
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
````

Additionally, a component can be moved to another component's location. This is for example useful to animate moving 
cards onto a card stack:

````kotlin
gameScene.playAnimation(
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
````

## [RotationAnimation][RotationAnimationKDoc]
A [RotationAnimation][RotationAnimationKDoc] rotates a [GameComponentView][GameComponentViewKDoc].
The rotation can be passed as ``fromAngle``/``toAngle`` or relative to the current rotation with ``byAngle``.

**NOTE**: The Animation only rotates the component in the current Scene and ***does not update its rotation***. The 
Component
will snap back upon next refresh if the new rotation is not set in ``onAnimationFinished``, which is the suggested 
way of
usage.

````kotlin
gameScene.playAnimation(
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
````

## [FlipAnimation][FlipAnimationKDoc]
A [FlipAnimation][FlipAnimationKDoc] switches between two visuals in a flipping-like animation. The animation sets the 
``fromVisual`` and then switches to the ``toVisual``.

**NOTE**: The Animation only switches the visuals visually. The Visual will revert to the ``currentVisual`` upon next
refresh if the new visual is not set in ``onAnimationFinished``, which is the suggested way of usage.

````kotlin
gameScene.playAnimation(
  FlipAnimation(
    componentView = card,
    fromVisual = backSide,
    toVisual = frontSide,
    duration = 1000
  ).apply { 
    onFinished = {
      card.visual = frontSide
    }
  }
)
````

## [RandomizeAnimation][RandomizeAnimationKDoc]
A [RandomizeAnimation][RandomizeAnimationKDoc] randomly switches between the given visuals. The time each visual is visible can be set by
passing ``steps``, which is calculated like the following: *steps: time = duration / steps*. The animation sets the 
``toVisual`` as the last step in order to control what the resulting visual of this animation is.

**NOTE**: The Animation only switches the visuals visually. The Visual will revert to the ``currentVisual`` upon next
refresh if the new visual is not set in ``onAnimationFinished``, which is the suggested way of usage.

````kotlin
gameScene.playAnimation(
  RandomizeAnimation(
    componentView = card,
    visual = allCardFaces,
    toVisual = allCardFaces[3],
    duration = 1000
  ).apply { 
    onFinished = {
      card.visual = allCardFaces[3]
    }
  }
)
````
## [DiceAnimation][DiceAnimationKDoc]
A [DiceAnimation][DiceAnimationKDoc] behaves like a [RandomizeAnimation][RandomizeAnimationKDoc] specifically for
dices. As the visuals got passed as parameter on Dice creation only the resulting side as zero-based index needs to be
passed. The animation sets the ``toSide`` as the last step in order to control what the resulting visual of this
animation is.

**NOTE**: The Animation only switches the visuals visually. The Visual will revert to the ``currentVisual`` upon next
refresh if the new visual is not set in ``onAnimationFinished``, which is the suggested way of usage.

````kotlin
gameScene.playAnimation(
  DiceAnimation(
    componentView = dice,
    toSide = 3,
    duration = 1000
  ).apply { 
    onFinished = {
      card.currentSide = 3
    }
  }
)
````
