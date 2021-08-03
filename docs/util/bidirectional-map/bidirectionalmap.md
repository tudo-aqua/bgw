---
parent: Util
layout: default
title: BiDirectionalMap
nav_order: 2
---

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
BiDirectionalMaps can be used to model relationships where every key has exactly one value and vice versa.
It may for example be used to map domain model objects to the corresponding view components. The map enables mapping in both directions.

## Creation
The map takes two generic parameters for the type of the domain and co-domain objects. 
The Constructor takes initial elements as varargs.
````kotlin
val map: Bidirectionalmap<Int, String> = Bidirectionalmap(
  Tuple<Int, String>(1, "ONE"),
  Tuple<Int, String>(6, "SIX"),
  Tuple<Int, String>(10, "TEN")
)
````
Note that an [IllegalArgumentException](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/IllegalArgumentException.html) is thrown if the parameters contain duplicated entries in the domain or co-domain.

## Modification
Adding and removing elements from the map can be accomplished by [add](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/add.html) and [remove](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/remove.html).
Adding elements with already contained key or value will result in an unchanged map and return value ``false``.

If an element is already contained can be checked with 
* [contains](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/contains.html)
* [containsForward](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/contains-forward.html)
* [containsBackward](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/contains-backward.html)

## Lookup elements
Looking up elements can be accomplished by
* [forward](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/forward.html) / [forwardOrNull](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/forward-or-null.html)
* [backward](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/backward.html) / [backwardOrNull](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/backward-or-null.html)

The set of entries gets returned by
* [getDomain](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/get-domain.html)
* [getCoDomain](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-bidirectional-map/get-co-domain.html)