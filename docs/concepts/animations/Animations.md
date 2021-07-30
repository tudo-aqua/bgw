---
parent: Concepts
title: Animations
nav_order: 1
layout: default
---

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

In this section we are going to showcase the differend types of animations in the BGW framework.
Visuals are mostly used to move game elements or change their [Visual](https://tudo-aqua.github.io/bgw/concepts/visual/visual.html).
Additionally the DelayAnimation can be used to delay code execution to enable the player to see what is happening on the table.

![](animations.gif)

The full example can be found [here](/bgw-docs-examples/src/main/kotlin/examples/concepts/animation/AnimationExample.kt).

Each [Animation](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-animation/) has a duration, a running attrtibute and an *onFinished* EventHandler that gets invoked after the animation has finished.

## [DelayAnimation](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-delay-animation/)
A DelayAnimation does nothing in the application window besides calling *onFinished* after the given amount of time (duration parameter). This timer runs asynchronously so it can run while the player is playing. To add a delay between moves in which the user should not be able to interact with the scene use [BoardgameScene#lock](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-scene/lock.html) before plaing the animation and [BoardgameScene#unlock](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-scene/unlock.html) in *onAnimationFinished*.
````kotlin
gameScene.lock()
gameScene.playAnimation(DelayAnimation(duration = 2000).apply {
  onFinished = {
    //Do stuff here
    gameScene.unlock()
  }
})
````

## [MovementAnimation](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-movement-animation/)
A movement animation moves a [GameComponentView](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/). 
The Movement can be passed as fromX/toX, fromY,toY or relative to the current position with byX/byY.

**NOTE**: The Anmiation only moves the component in your Scene and ***does not update it's position***. The Component will snap back upon next refresh if the new position is not set in *onAnimationFinished*, which is the suggested way of usage.

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

Additionally you can move a componentt to another component's location. This is for example useful to animate cards onto a card stack:

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

## [RotationAnimation](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-rotation-animation/)

## [FlipAnimation](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-flip-animation/)

## [RandomizeAnimation](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-randomize-animation/)

## [DiceAnimation](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.animation/-dice-animation/)
````kotlin

````
