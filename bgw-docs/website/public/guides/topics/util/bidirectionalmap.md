[BiDirectionalMapKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/index.html
[IllegalArgumentExceptionDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/
[NoSuchElementExceptionKDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-no-such-element-exception/

<!-- GH-Pages Doc -->

[ComponentViewDoc]: /guides/components/componentview

# BidirectionalMap

<tldr>A map that can be searched from both sides</tldr>

## Introduction

A [BidirectionalMap][BiDirectionalMapKDoc] models a one-to-one relationship: every key has exactly one value, and every value belongs to exactly one key. This allows lookups in **both directions**.

In board games this is a perfect fit to connect the two worlds of your application: the **model** (a `Card`) and the **view** ([components][ComponentViewDoc] like a `CardView`). Clicking a view gives you its model, changing the model tells you which view to update:

```kotlin
val cardViews = BidirectionalMap<Card, CardView>()

cardViews.add(queenOfHearts, queenOfHeartsView)

cardView.onMouseClicked = {
    val card = cardViews.backward(cardView)   // view -> model
    play(card)
}

val view = cardViews.forward(queenOfHearts)  // model -> view
```

Because the terms _key_ and _value_ depend on the direction, the map talks about its **domain** (the first type, accessed with `forward`) and its **co-domain** (the second type, accessed with `backward`).

---

## Creating a map

The map takes the type of the domain and the co-domain as type parameters, both must be non-null. Initial elements are passed as vararg pairs:

```kotlin
val map = BidirectionalMap(
    1 to "ONE",
    6 to "SIX",
    10 to "TEN"
)
```

Every element may appear **once** in the domain and **once** in the co-domain. An element may appear once on each side, though:

| Entries | Valid |
| --- | --- |
| `(A → B), (A → C)` | No, `A` is contained twice in the domain. |
| `(A → B), (C → B)` | No, `B` is contained twice in the co-domain. |
| `(A → B), (C → A)` | Yes |

> An [IllegalArgumentException][IllegalArgumentExceptionDoc] is thrown if the initial entries violate this rule.
> {style="warning"}

---

## Adding and changing entries

Add new relations or overwrite existing ones.

<signature>
<code-block lang="kotlin" copy="false">
add(entity: T, value: R): Boolean
add(element: Pair&lt;T, R&gt;): Boolean
addAll(vararg items: Pair&lt;T, R&gt;): Boolean
addAll(elements: Collection&lt;Pair&lt;T, R&gt;&gt;): Boolean
</code-block>

Adds a relation `entity → value`, but only if neither the entity is already in the domain nor the value in the co-domain. Returns `false` if the relation could not be added, in which case the map is unchanged. `addAll` adds many relations at once, entries that already exist are ignored.

</signature>

<signature>
<code-block lang="kotlin" copy="false">
put(key: T, value: R): Pair&lt;Pair&lt;T, R&gt;?, Pair&lt;T, R&gt;?&gt;
putAll(vararg items: Pair&lt;T, R&gt;)
putAll(elements: Collection&lt;Pair&lt;T, R&gt;&gt;)
putAll(bidirectionalMap: BidirectionalMap&lt;T, R&gt;)
map[key] = value
</code-block>

Sets the relation and **overwrites** conflicting entries in the domain and the co-domain. `put` returns the overwritten pair of the domain and the overwritten pair of the co-domain, or `null` for each that did not exist. The index operator `map[key] = value` is a shortcut for `put`.

</signature>

```kotlin
map.add(12, "TWELVE")          // true
map.add(1, "EINS")             // false, 1 is already in the domain
map.addAll(listOf(2 to "TWO", 3 to "THREE"))

map.put(1, "UNO")              // overwrites 1 -> "ONE"
map[6] = "SECHS"               // overwrites 6 -> "SIX"
```

---

## Removing entries

Delete relations from either side.

<signature>
<code-block lang="kotlin" copy="false">
remove(entity: T, value: R): Boolean
remove(element: Pair&lt;T, R&gt;): Boolean
removeForward(entity: T): Boolean
removeBackward(value: R): Boolean
clear()
</code-block>

`remove` deletes exactly the given relation. `removeForward` and `removeBackward` delete the relation that contains the given element in the domain or the co-domain. All return whether something was removed.

</signature>

---

## Looking up entries

Find the partner of an element in both directions.

<signature>
<code-block lang="kotlin" copy="false">
forward(entity: T): R
forwardOrNull(entity: T): R?
backward(value: R): T
backwardOrNull(value: R): T?
map[key]
</code-block>

`forward` returns the value for an entity from the domain, `backward` the entity for a value from the co-domain. They throw a [NoSuchElementException][NoSuchElementExceptionKDoc] if there is no relation, the `OrNull` variants return `null`. The index operator `map[key]` is a shortcut for `forward`.

<br>

<code-block lang="kotlin" copy="false">
contains(entity: T, value: R): Boolean
containsForward(entity: T): Boolean
containsBackward(value: R): Boolean
</code-block>

Checks whether a relation exists, or whether an element is part of the domain or of the co-domain.

<br>

<code-block lang="kotlin" copy="false">
keysForward: Set&lt;T&gt;
keysBackward: Set&lt;R&gt;
entries: Set&lt;Pair&lt;T, R&gt;&gt;
size: Int
isEmpty(): Boolean
isNotEmpty(): Boolean
</code-block>

All elements of the domain, all elements of the co-domain, and all relations as pairs.

</signature>
