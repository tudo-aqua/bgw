[CoordinateKDoc]: /docs/tools.aqua.bgw.util/-coordinate/index.html
[CoordinatePlainKDoc]: /docs/tools.aqua.bgw.util/-coordinate-plain/index.html

<!-- GH-Pages Doc -->

[ComponentViewDoc]: /guides/components/componentview
[UsingComponents]: /guides/using-components
[AnimationDoc]: /guides/concepts/animations

# Coordinates

<tldr>Positions and areas in a scene</tldr>

## Introduction

Positions in BGW are expressed as `x` and `y` values in the coordinate space of the scene or the parent of a [component][ComponentViewDoc], see [Using Components][UsingComponents]. The `x` axis points to the right and the `y` axis points **down**. Two small classes help to calculate with them.

---

## Coordinate

A point with x and y.

A [Coordinate][CoordinateKDoc] is an immutable pair of an `xCoord` and a `yCoord`. Both are `Double` values, but any `Number` can be passed to the constructor. It is used by the framework, for example by `MovementAnimation.toComponentView` or by containers that return the position of their children, and is handy for your own geometry calculations.

```kotlin
val coord1 = Coordinate(xCoord = 5, yCoord = 7)
val coord2 = Coordinate(xCoord = -1, yCoord = 5)
```

### Calculating

Combine and scale coordinates.

<signature>
<code-block lang="kotlin" copy="false">
operator plus(other: Coordinate): Coordinate
operator minus(other: Coordinate): Coordinate
operator times(factor: Number): Coordinate
operator div(denominator: Number): Coordinate
</code-block>

The common operators work on both axes. `div` throws an `IllegalArgumentException` when dividing by zero.

</signature>

```kotlin
val added = coord1 + coord2        // X = 4.0, Y = 12.0
val subtracted = coord1 - coord2   // X = 6.0, Y = 2.0
val scaled = coord1 * 2            // X = 10.0, Y = 14.0
val divided = coord1 / 2           // X = 2.5, Y = 3.5
```

Two coordinates are equal if and only if both axes are equal.

### Rotating

Turn a coordinate around a center point.

<signature>
<code-block lang="kotlin" copy="false">
rotated(angle: Number, center: Coordinate): Coordinate
rotated(angle: Double): Coordinate
</code-block>

Returns the coordinate rotated by `angle` **degrees** around a center point, or around the origin `(0, 0)` if no center is passed. Positive angles rotate **clockwise** on the screen, like the `rotation` of a component.

</signature>

```kotlin
// A point 100 units to the right of the center, turned by 90 degrees
val center = Coordinate(200, 200)
val point = Coordinate(300, 200)

point.rotated(90, center)   // X = 200.0, Y = 300.0 (now below the center)
```

---

## CoordinatePlain

A rectangle in the coordinate space.

A [CoordinatePlain][CoordinatePlainKDoc] describes a rectangular area, defined by its top left and its bottom right corner. It can be rotated, which makes it a useful tool to find out which space a rotated component occupies.

<signature>
<code-block lang="kotlin" copy="false">
CoordinatePlain(topLeftX: Number, topLeftY: Number, bottomRightX: Number, bottomRightY: Number)
CoordinatePlain(topLeft: Coordinate, bottomRight: Coordinate)
</code-block>

The top left corner must be smaller than or equal to the bottom right corner in both directions, otherwise an `IllegalArgumentException` is thrown.

</signature>

```kotlin
val plain = CoordinatePlain(topLeftX = 0, topLeftY = 0, bottomRightX = 100, bottomRightY = 50)
```

### Properties

The corners and the size of the bounding box.

<signature>
<code-block lang="kotlin" copy="false">
topLeftBound: Coordinate
topRightBound: Coordinate
bottomLeftBound: Coordinate
bottomRightBound: Coordinate
width: Double
height: Double
</code-block>

The **bounds** are the corners of the smallest axis-aligned rectangle that contains the plain. For an unrotated plain they are the corners themselves. After a rotation, the bounds enclose the rotated rectangle, and `width` and `height` describe this enclosing rectangle.

</signature>

### Rotating

Turn the whole rectangle around a center point.

<signature>
<code-block lang="kotlin" copy="false">
rotated(angle: Number, center: Coordinate): CoordinatePlain
</code-block>

Rotates all four corners by `angle` degrees around the `center` and returns the new plain.

</signature>

```kotlin
val rotated = plain.rotated(45, Coordinate(50, 25))

rotated.width    // about 106.07, the width of the bounding box
rotated.height   // about 106.07
```

> The animation of a rotation is explained in [Animations][AnimationDoc].
> {style="note"}
