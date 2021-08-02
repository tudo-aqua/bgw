---
parent: Components title: Container has_toc: true nav_order: 2 layout: default has_children: true
---

[AreaDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-area/index.html

[ContainerDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.container/-game-component-container/index.html

[GameComponentView]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/

[TokenDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-token-view/index.html

# Container

{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

Containers can be used to group
[GameComponentView][GameComponentView]s.

[GameComponentContainer][ContainerDoc]
is the abstract baseclass for containers. Different implementations support different styles of layouting for the
contained components.

## Container features

The Container features will be demonstrated using an [Area][AreaDoc], since [GameComponentContainer][ContainerDoc]
is abstract and [Area][AreaDoc] is just the discrete implementation.

The complete source code for this example can be
found [here](/bgw/components/container.html#complete-source-code-for-the-example).

To create a running example, the described elements are wrapped in a
[BoardGameApplication](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.core/-board-game-application/).

````kotlin
class AreaExample : BoardGameApplication("Area example") {
	val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)
	
	val numberOfComponentsLabel: Label = Label(width = 400, posX = 50, posY = 50)
	val area: Area<TokenView> = Area(100, 400, 50, 100, ColorVisual.DARK_GRAY)
	
	val greenToken: TokenView = TokenView(visual = ColorVisual.GREEN)
	val redToken: TokenView = TokenView(visual = ColorVisual.RED)
}
````

### Add and remove

The most important feature of a container is to add and remove components.

Adding a Component is as simple as calling the ``add`` function with the component as its argument. Optionally an index
may be supplied. An example on how to add with or without index:

````kotlin
area.add(greenToken)
area.add(redToken, 0)
````

The ``greenToken`` is added to the ``area``. The index parameter was omitted, so it gets added at the end of the
components list. In this case at index 0. Then the
``redToken`` is added explicitly at index 0, therefore ``greenToken`` is pushed back to index 1.

Removing a Component is as simple as calling the ``remove`` function with the component to remove as its argument.

````kotlin
area.remove(redToken)
````

The ``redToken`` is removed from the ``area``, therefore the ``greenToken`` falls back down to index 0.

There are some convenience functions for adding and removing multiple Components at once. Please refer to
the [docs][AreaDoc] for an in-depth overview.

### onAdd and onRemove

It is possible to specify code that gets executed with the component as its receiver, after it gets added or removed
from the container. This is helpful whenever some modifications need to be made to any components, after it is added or
removed.

In this example [TokenView][TokenDoc]s get resized when they are added to ``area``, and rotated by 45° when they are
removed from ``area``. To achieve this behaviour, the ``onAdd`` and ``onRemove`` fields are set.

````kotlin
area.onAdd = {
	this.resize(100, 100)
}
area.onRemove = {
	this.rotation += 45
}
````

### Listeners

Listeners for the components list may be added to a container. They get invoked any time the components list changes its
state. In this example a Label gets updated with the Number of components currently contained in ``area``.

````kotlin
area.addComponentsListener {
	numberOfComponentsLabel.label = "Number of components in this area: ${area.numberOfComponents()}"
}
````

Listeners can be removed via the ``clearComponentsListners`` or ``removeComponentsListner`` functions.

## Useful hints for dealing with containers

- Containers provide an iterator over their components list via
  the [Iterable](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterable/) interface.
- The position of components contained in any containers with automatic layouting should never be modified, since the
  containers handle positioning.

- When using non-automatic layouting containers, do not forget to position the contained components. Especially if they
  get added after a drag and drop gesture.

- Any Component can only ever be contained in one container at a time. Trying to add an already contained component to
  another container will result in a runtime exception.

- Containers can also be draggable and can act as a drag target.

- ComponentListeners can be a great way of exposing dynamic information about a container via
  sufficient [UIComponents](https://tudo-aqua.github.io/bgw/components/ui-elements/ui-elements.html).

## Types of Containers

### [Area](/bgw/components/container/children/area.html)

Area is the simplest form of a container. Its contained components are positioned relative to the top-left corner of the
Area. No further layouting is provided by the Area.

### [CardStack](/bgw/components/container/children/cardstack.html)

CardStack is a special form of container. It can only contain
[CardView](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.components.gamecomponentviews/-card-view/index.html)
. It should be used to visualize card stacks. It provides automatic layouting and alignment features.

### [LinearLayout](https://tudo-aqua.github.io/bgw/components/container/children/linearlayout.html)

LinearLayout spaces its components dynamically based on its dimensions, the components dimensions, and the user defined
spacing. Additionally, an orientation and alignment may be specified.

### [Satchel](https://tudo-aqua.github.io/bgw/components/container/children/satchel.html)

A satchel hides its components and reveals them, when they are removed. This container can be used to visualize an
entity, where the user should not know what might get drawn next, or what is in the container.

## Complete source code for the example

[View it on GitHub](https://github.com/tudo-aqua/bgw/blob/main/bgw-docs-examples/src/main/kotlin/components/container/AreaExample.kt)
{: .btn }

````kotlin
class AreaExample : BoardGameApplication("Area example") {
	private val gameScene: BoardGameScene = BoardGameScene(background = ColorVisual.LIGHT_GRAY)
	
	private val numberOfComponentsLabel: Label = Label(width = 400, posX = 50, posY = 50)
	private val area: Area<TokenView> = Area(100, 400, 50, 100, ColorVisual.DARK_GRAY)
	
	private val greenToken: TokenView = TokenView(visual = ColorVisual.GREEN)
	private val redToken: TokenView = TokenView(visual = ColorVisual.RED)
	
	init {
		area.onAdd = {
			this.resize(100, 100)
		}
		area.onRemove = {
			this.rotation += 45
		}
		
		area.addComponentsListener {
			numberOfComponentsLabel.label = "Number of components in this area: ${area.numberOfComponents()}"
		}
		
		area.add(greenToken)
		area.add(redToken, 0)
		
		area.remove(redToken)
		
		gameScene.addComponents(area, numberOfComponentsLabel)
		showGameScene(gameScene)
		show()
	}
}
````