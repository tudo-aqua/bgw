[BiDirectionalMapKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/index.html
[addKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/add.html
[removeKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/remove.html
[containsKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/contains.html
[containsForwardKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/contains-forward.html
[containsBackwardKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/contains-backward.html
[forwardKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/forward.html
[forwardOrNullKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/forward-or-null.html
[backwardKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/backward.html
[backwardOrNullKDoc]: /docs/tools.aqua.bgw.util/-bidirectional-map/backward-or-null.html
[getDomainKDoc]: /docs/tools.aqua.bgw.util/BidirectionalMap/keysForward
[getCoDomainKDoc]: /docs/tools.aqua.bgw.util/BidirectionalMap/keysBackward
[IllegalArgumentExceptionDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/

> This guide is currently being rewritten. Content may be incomplete, incorrect or subject to change.
> {style="danger"}

# BiDirectionalMap

## Use case

[BiDirectionalMaps][BiDirectionalMapKDoc] can be used to model relationships where every key has exactly one value and
vice versa.
It may for example be used to map domain model objects to the corresponding view /components. The map enables mapping in both directions.

## Creation

The map takes two generic parameters for the type of the domain and co-domain objects.
The constructor takes initial elements as varargs.

```kotlin
val map: BidirectionalMap<Int, String> = BidirectionalMap(
  Pair<Int, String>(1, "ONE"),
  Pair<Int, String>(6, "SIX"),
  Pair<Int, String>(10, "TEN")
)
```

Note that an [IllegalArgumentException][IllegalArgumentExceptionDoc]
is thrown if the parameters contain duplicate entries in the domain or co-domain.

## Modification

Adding and removing elements from the map can be accomplished by [add][addKDoc] and [remove][removeKDoc].
Adding elements with an already contained key or value will result in an unchanged map and return value `false`.

If an element is already contained can be checked with the following methods:

- [contains][containsKDoc]
- [containsForward][containsForwardKDoc]
- [containsBackward][containsBackwardKDoc]

## Lookup elements

Looking up elements can be accomplished by

- [forward][forwardKDoc] / [forwardOrNull][forwardOrNullKDoc]
- [backward][backwardKDoc] / [backwardOrNull][backwardOrNullKDoc]

The set of entries gets returned by

- [keysForward][getDomainKDoc]
- [keysBackward][getCoDomainKDoc]
