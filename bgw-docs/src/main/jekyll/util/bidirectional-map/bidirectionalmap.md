---
parent: Util
layout: default
title: BiDirectionalMap
nav_order: 2
---

<!-- KDoc -->
[BiDirectionalMapKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/index.html
[addKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/add.html
[removeKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/remove.html
[containsKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/contains.html
[containsForwardKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/contains-forward.html
[containsBackwardKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/contains-backward.html
[forwardKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/forward.html
[forwardOrNullKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/forward-or-null.html
[backwardKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/backward.html
[backwardOrNullKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/backward-or-null.html
[getDomainKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/get-domain.html
[getCoDomainKDoc]: https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/get-co-domain.html

<!-- Links -->
[IllegalArgumentExceptionDoc]: https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-illegal-argument-exception/

<!-- Start Page -->
# BiDirectionalMap

{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

## Use case
[BiDirectionalMaps][BiDirectionalMapKDoc] can be used to model relationships where every key has exactly one value and 
vice versa.
It may for example be used to map domain model objects to the corresponding view components. The map enables mapping in both directions.

## Creation
The map takes two generic parameters for the type of the domain and co-domain objects. 
The constructor takes initial elements as varargs.
````kotlin
val map: Bidirectionalmap<Int, String> = Bidirectionalmap(
  Tuple<Int, String>(1, "ONE"),
  Tuple<Int, String>(6, "SIX"),
  Tuple<Int, String>(10, "TEN")
)
````
Note that an [IllegalArgumentException][IllegalArgumentExceptionDoc]
is thrown if the parameters contain duplicate entries in the domain or co-domain.

## Modification
Adding and removing elements from the map can be accomplished by [add][addKDoc] and [remove][removeKDoc].
Adding elements with an already contained key or value will result in an unchanged map and return value ``false``.

If an element is already contained can be checked with the following methods:
* [contains][containsKDoc]
* [containsForward][containsForwardKDoc]
* [containsBackward][containsBackwardKDoc]

## Lookup elements
Looking up elements can be accomplished by
* [forward][forwardKDoc] / [forwardOrNull][forwardOrNullKDoc]
* [backward][backwardKDoc] / [backwardOrNull][backwardOrNullKDoc]

The set of entries gets returned by
* [getDomain][getDomainKDoc]
* [getCoDomain][getCoDomainKDoc]