[ObservableKDoc]: /docs/tools.aqua.bgw.observable/-observable/index.html
[ValueObservableKDoc]: /docs/tools.aqua.bgw.observable/-value-observable/index.html
[ValueObserverKDoc]: /docs/tools.aqua.bgw.observable/-value-observer/index.html
[PropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-property/index.html
[BooleanPropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-boolean-property/index.html
[IntegerPropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-integer-property/index.html
[DoublePropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-double-property/index.html
[StringPropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-string-property/index.html
[LimitedDoublePropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-limited-double-property/index.html
[ReadonlyPropertyKDoc]: /docs/tools.aqua.bgw.observable.properties/-readonly-property/index.html
[ObservableListKDoc]: /docs/tools.aqua.bgw.observable.lists/-observable-list/index.html
[ReadonlyObservableListKDoc]: /docs/tools.aqua.bgw.observable.lists/-readonly-observable-list/index.html
[ObservableArrayListKDoc]: /docs/tools.aqua.bgw.observable.lists/-observable-array-list/index.html
[ObservableLinkedListKDoc]: /docs/tools.aqua.bgw.observable.lists/-observable-linked-list/index.html
[ListViewKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-list-view/index.html

<!-- GH-Pages Doc -->

[UIComponentsDoc]: /guides/components/uicomponents
[UserInputDoc]: /guides/concepts/user-input

# Observable

<tldr>Get notified whenever a value or a list changes</tldr>

## Introduction

Observables follow the observer pattern: an object holds a value, and everybody who is interested in changes can register a **listener**. BGW offers these building blocks for your own game state, for example to update a score label whenever the score changes.

You will mostly meet observables in two places:

- **Lists** of UI components like the `items` of a [ListView][ListViewKDoc], which you can listen to or modify.
- **Properties** and **lists** in your own code, for example to update a score label whenever the score changes.

> To observe a component, use its event handlers described in [User Input][UserInputDoc]. Observable lists on components such as `items` can be used directly.
> {style="note"}

---

## Properties

A [Property][PropertyKDoc] wraps a single value and invokes its listeners whenever the value changes.

<signature>
<code-block lang="kotlin" copy="false">
Property&lt;T&gt;(initialValue: T)
value: T
</code-block>

Reading `value` returns the current value, assigning it stores a new one. Listeners are invoked **if and only if** the new value is not equal to the old one.

</signature>

For the common data types there are dedicated classes:

<table orientation="horizontal">
    <tr>
        <td width="35%"><code>BooleanProperty</code></td>
        <td>A <code>Boolean</code>, default <code>false</code></td>
    </tr>
    <tr>
        <td><code>IntegerProperty</code></td>
        <td>An <code>Int</code>, default <code>0</code></td>
    </tr>
    <tr>
        <td><code>DoubleProperty</code></td>
        <td>A <code>Double</code>, default <code>0.0</code></td>
    </tr>
    <tr>
        <td><code>StringProperty</code></td>
        <td>A <code>String</code>, default empty</td>
    </tr>
    <tr>
        <td><code>LimitedDoubleProperty</code></td>
        <td>A <code>Double</code> within fixed bounds. Setting a value outside of <code>lowerBoundInclusive</code> and <code>upperBoundInclusive</code> throws an <code>IllegalArgumentException</code>.</td>
    </tr>
</table>

For any other type use the generic `Property<T>`. The matching [ReadonlyProperty][ReadonlyPropertyKDoc] classes allow reading and listening but not setting the value from the outside. They are used wherever BGW exposes a value that only the framework may change.

### Listeners

Listeners are registered with `addListener`. They receive the old and the new value:

```kotlin
val score = IntegerProperty(0)

score.addListener { oldValue, newValue ->
    println("Score changed from $oldValue to $newValue")
}

score.value = 1 // prints "Score changed from 0 to 1"
score.value = 1 // equal value: the listener is not invoked
```

<signature>
<code-block lang="kotlin" copy="false">
addListener(listener: ValueObserver&lt;T&gt;)
removeListener(listener: ValueObserver&lt;T&gt;): Boolean
clearListeners()
</code-block>

Registers or removes a [ValueObserver][ValueObserverKDoc], or removes all listeners. `removeListener` returns whether the listener was found. To be able to remove a listener later, keep a reference to it.

<br>

<code-block lang="kotlin" copy="false">
addListenerAndInvoke(initialValue: T, listener: (T, T) -&gt; Unit)
</code-block>

Registers the listener and **immediately** invokes it with the given initial value as both old and new value. This is handy to initialize a view and observe it in one step. The initial value is only passed to the listener, it is not stored in the property.

<br>

<code-block lang="kotlin" copy="false">
once(initialValue: T, expectedValue: T, listener: (T, T) -&gt; Unit)
</code-block>

Invokes the listener the first time the property changes to `expectedValue` and removes it afterwards.

<br>

<code-block lang="kotlin" copy="false">
notifyUnchanged()
</code-block>

Invokes all listeners with the current value as old and new value, although nothing changed.

</signature>

```kotlin
val observer = ValueObserver<Int> { _, newValue -> println("Observed $newValue") }
score.addListener(observer)
score.removeListener(observer)

val ready = BooleanProperty(false)
ready.once(initialValue = false, expectedValue = true) { _, _ ->
    println("Ready!") // printed exactly once
}
ready.value = true
```

### A practical example

Properties are a good fit for state that belongs to the GUI itself, and that several components depend on. Here a scene owns a `showHints` property. Every component that depends on it is updated by a single listener, and the button only has to flip the value:

```kotlin
class DiceDuelGameScene : BoardGameScene(/* ... */) {
    private val showHints = BooleanProperty(true)

    val hintLabel = Label(/* ... */)
    val hintArrow = Label(/* ... */)
    val hintButton = Button(/* ... */)

    init {
        showHints.addListenerAndInvoke(true) { _, visible ->
            hintLabel.isVisible = visible
            hintArrow.isVisible = visible
        }

        hintButton.onMouseClicked = { showHints.value = !showHints.value }
    }
}
```

---

## Observable lists

An [ObservableList][ObservableListKDoc] behaves like a Kotlin `MutableList` but informs its listeners about changes. Its listeners receive a snapshot of the list **before** and **after** the change.

```kotlin
val names = ObservableArrayList(listOf("Alice", "Bob"))

names.addListener { oldList, newList ->
    println("List changed from $oldList to $newList")
}

names.add("Carol")  // [Alice, Bob] -> [Alice, Bob, Carol]
names.removeAt(0)   // [Alice, Bob, Carol] -> [Bob, Carol]
```

There are two implementations, which are backed by an array list or a linked list respectively:

- [ObservableArrayList][ObservableArrayListKDoc]
- [ObservableLinkedList][ObservableLinkedListKDoc]

All list operations that modify the list invoke the listeners **once**, also bulk operations like `addAll`, `removeAll` or `sort`. Operations that do not change anything, for example `remove` of an element that is not contained, do not invoke them. A [ReadonlyObservableList][ReadonlyObservableListKDoc] only offers the reading part of the API.

> The same listener functions as for properties (`addListener`, `addListenerAndInvoke`, `removeListener`, `clearListeners`) are available on lists.
> {style="note"}

### Observable lists in components

[ListView][ListViewKDoc] and `TableView` offer their data as `items`, which is an `ObservableList`. Whatever you add or remove is immediately shown, and you can listen to your own changes. `selectedItems` and `selectedIndices` are read-only observable lists that reflect the selection of the player.

<preview key="tools.aqua.bgw.main.examples.ExampleInteractionScene.observableListExample">
val listView = ListView(
    width = 200,
    height = 200,
    items = mutableListOf("Alice", "Bob", "Carol"),
    visual = ColorVisual(Color(0x0F141F)),
    font = Font(20.0, Color.WHITE),
    selectionMode = SelectionMode.SINGLE,
    selectionBackground = ColorVisual(
        Color(0xBB6DFF)
    )
)
</preview>

The `items` can be changed at any time, and the selection can be observed:

```kotlin
listView.items.add("Dave")  // Appears at the end of the list
listView.selectedItems.addListener { _, selected ->
    println("Selected: $selected")
}
```

> Read more about list based UI components in [UI Components][UIComponentsDoc].
> {style="note"}
