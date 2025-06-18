[CoordinateDoc]: /docs/tools.aqua.bgw.util/-coordinate/index.html
[CoordinatePlain]: /docs/tools.aqua.bgw.util/-coordinate-plain/index.html
[rotatedKDoc]: /docs/tools.aqua.bgw.util/-coordinate/rotated.html
[rotatedPlainKDoc]: /docs/tools.aqua.bgw.util/-coordinate-plain/rotated.html

> This guide is currently being rewritten. Content may be incomplete, incorrect or subject to change.
> {style="danger"}

# Coordinates

## [Coordinate][CoordinateDoc]

The [Coordinate][CoordinateDoc] class is used as a Tuple wrapper for X and Y coordinates of a ComponentView.
It is created with X and Y coordinates accordingly.

The coordinate class implements plus and minus operator to add or subtract coordinates from each other:

```kotlin
val coord1: Coordinate = Coordinate(xCoord = 5, yCoord = 7)
val coord2: Coordinate = Coordinate(xCoord = -1, yCoord = 5)

val addedCoord: Coordinate = coord1 + coord2 //xCoord = 4, yCoord = 13
val subtractedCoord: Coordinate = coord1 - coord2 //xCoord = 6, yCoord =  2
```

The equals function returns true if and only if `coord1.xCoord == coord2.xCoord && coord1.yCoord == coord2.yCoord`.

Additionally, the [rotated][rotatedKDoc] function can be used to rotate the coordinate by a given angle around a center point.

Common Operators **plus, minus, times** and **divide** are defined.

## [CoordinatePlain][CoordinatePlain]

A [CoordinatePlain][CoordinatePlain] represents a right-angled plain with four corners. These can be accessed by:

```kotlin
val topLeft: Coordinate
val topRight: Coordinate
val bottomLeft: Coordinate
val bottomRight: Coordinate
```

Additionally, the dimensions get calculated and can be accessed:

```kotlin
val height: Double
val width: Double
```

The CoordinatePlain can be created by specifying the top-left and bottom-right corner either with Coordinates or X and Y position.

The function [rotated][rotatedPlainKDoc] function can be used to rotate the 2D plain by a given angle around a center point.
