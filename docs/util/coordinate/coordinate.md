---
parent: Util
layout: default
title: Coordinate
nav_order: 1
---

# Coordinates

{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

## [Coordinate](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-coordinate/index.html)

The Coordinate class is used as a Tuple wrapper for X and Y coordinates of a ComponentView.
It is created with X and Y coordinates accordingly.

The coordinate class implements plus and minus operator to add or subtract coordinates from each other:

````kotlin
val coord1: Coordinate = Coordinate(xCoord = 5, yCoord = 7)
val coord2: Coordinate = Coordinate(xCoord = -1, yCoord = 5)

val addedCoord: Coordinate = coord1 + coord2 //xCoord = 4, yCoord = 13
val subtractedCoord: Coordinate = coord1 - coord2 //xCoord = 6, yCoord =  2
````

The equals function returns true if and only if ````coord1.xCoord == coord2.xCoord && coord1.yCoord == coord2.yCoord````.

Additionally, the [rotated](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-coordinate/rotated.html) function can be used to rotate the coordinate by a given angle around a center point.

## [CoordinatePlain](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-coordinate-plain/index.html)
A CoordinatePlain represents a right-angled plain with four its four corners. These can be accessed by
````kotlin
val topLeft: Coordinate
val topRight: Coordinate
val bottomLeft: Coordinate
val bottomRight: Coordinate
````

Additionally, the dimensions get calculated
````kotlin
val height: Double
val width: Double
````

The CoordinatePlain can be created by specifying the top-left and bottom-right corner either with Coordinates or X and Y position.

The function [isIn](https://tudo-aqua.github.io/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-coordinate-plain/is-in.html) can be used to check whether a coordinate is inside this 2D plain.



