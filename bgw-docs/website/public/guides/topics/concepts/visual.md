[VisualKDoc]: /docs/tools.aqua.bgw.visual/-visual/index.html
[SingleLayerVisualKDoc]: /docs/tools.aqua.bgw.visual/-single-layer-visual/index.html
[ColorVisualKDoc]: /docs/tools.aqua.bgw.visual/-color-visual/index.html
[TextVisualKDoc]: /docs/tools.aqua.bgw.visual/-text-visual/index.html
[ImageVisualKDoc]: /docs/tools.aqua.bgw.visual/-image-visual/index.html
[CompoundVisualKDoc]: /docs/tools.aqua.bgw.visual/-compound-visual/index.html
[ColorKDoc]: /docs/tools.aqua.bgw.core/-color/index.html
[FontKDoc]: /docs/tools.aqua.bgw.util/-font/index.html
[AlignmentKDoc]: /docs/tools.aqua.bgw.core/-alignment/index.html
[StyleKDoc]: /docs/tools.aqua.bgw.style/-style/index.html
[BorderRadiusKDoc]: /docs/tools.aqua.bgw.style/-border-radius/index.html
[CursorKDoc]: /docs/tools.aqua.bgw.style/-cursor/index.html
[FilterKDoc]: /docs/tools.aqua.bgw.style/-filter/index.html
[FlipKDoc]: /docs/tools.aqua.bgw.style/-flip/index.html
[ComponentViewKDoc]: /docs/tools.aqua.bgw.components/-component-view/index.html

<!-- GH-Pages Doc -->

[ComponentViewDoc]: /guides/components/componentview
[GameComponentsDoc]: /guides/components/gamecomponents
[AdvancedUsage]: /guides/concepts/advanced-usage
[AnimationDoc]: /guides/concepts/animations

# Visual

<tldr>Style your components with colors, text and images</tldr>

## Introduction

A [Visual][VisualKDoc] describes what a component looks like. Every [ComponentView][ComponentViewKDoc] has a `visual`, and so do scenes and the application itself as `background`. Visuals are the only way to give components an appearance, so it is worth knowing them well.

There are two kinds of visuals:

<table orientation="horizontal">
    <tr>
        <td width="30%"><code>SingleLayerVisual</code></td>
        <td>A single layer, which is either a <code>ColorVisual</code>, a <code>TextVisual</code> or an <code>ImageVisual</code>. All of them share transparency, style, filters and flipping.</td>
    </tr>
    <tr>
        <td><code>CompoundVisual</code></td>
        <td>A stack of single layer visuals that are displayed on top of each other.</td>
    </tr>
</table>

> The previews in this guide show tokens with different visuals. Tokens are the simplest component: they have a size and a `visual`, nothing else.
> {style="note"}

---

## ColorVisual

Fill a component with a solid color.

A [ColorVisual][ColorVisualKDoc] fills the component with a [Color][ColorKDoc]. A color consists of red, green and blue channels between `0` and `255` and an alpha channel between `0.0` (invisible) and `1.0` (opaque). It can be created in several ways:

```kotlin
Color(0xBB6DFF)                  // hex value, alpha = 1.0
Color("#BB6DFF")                 // hex string
Color(187, 109, 255)             // red, green, blue
Color(187, 109, 255, 0.5)        // red, green, blue, alpha
Color.WHITE                      // presets like BLACK, RED, GREEN, BLUE, ...
```

The `ColorVisual` companion object additionally offers visuals for all preset colors, like `ColorVisual.RED`. Transparency can either be part of the color (alpha channel) or be set via the `transparency` property of the visual. If both are used, they are multiplied.

<preview key="tools.aqua.bgw.main.examples.ExampleVisualScene.colorExample">
val colorSolid = TokenView(
    width = 90,
    height = 90,
    visual = ColorVisual(Color(0xBB6DFF))
)
&nbsp;
val colorAlpha = TokenView(
    width = 90,
    height = 90,
    visual = ColorVisual(Color(187, 109, 255, 0.5))
)
&nbsp;
val colorTransparency = TokenView(
    width = 90,
    height = 90,
    visual = ColorVisual(Color(0xBB6DFF)).apply {
        transparency = 0.25
    }
)
&nbsp;
val colorPreset = TokenView(
    width = 90,
    height = 90,
    visual = ColorVisual.RED
)
</preview>

> The constructors that accept a `java.awt.Color` are deprecated. Use BGW's own `Color` class instead.
> {style="warning"}

---

## TextVisual

Display a line of text.

A [TextVisual][TextVisualKDoc] displays text. How it looks is defined by a [Font][FontKDoc] with size, color, family, weight and style.

<signature>
<code-block lang="kotlin" copy="false">
TextVisual(
    text: String,
    font: Font = Font(),
    alignment: Alignment = Alignment.CENTER,
    offsetX: Number = 0,
    offsetY: Number = 0,
    rotation: Number = 0
)
</code-block>

`alignment` places the text inside the component using an [Alignment][AlignmentKDoc]. `offsetX` and `offsetY` shift it from there and `rotation` turns it by the given degrees.

</signature>

A TextVisual has **no background** of its own. To label a component with a colored background, combine a `ColorVisual` and a `TextVisual` in a `CompoundVisual`, which is explained [below](#compoundvisual).

<preview key="tools.aqua.bgw.main.examples.ExampleVisualScene.textExample">
val textDefault = TokenView(
    width = 90,
    height = 90,
    visual = CompoundVisual(
        ColorVisual(Color(0xBB6DFF)),
        TextVisual("Token")
    )
)
&nbsp;
val textStyled = TokenView(
    width = 90,
    height = 90,
    visual = CompoundVisual(
        ColorVisual(Color(0x6DBEFF)),
        TextVisual(
            text = "Token",
            font = Font(
                size = 20,
                color = Color(0x0F141F),
                fontWeight = Font.FontWeight.BOLD,
                fontStyle = Font.FontStyle.ITALIC
            )
        )
    )
)
&nbsp;
val textTopLeft = TokenView(
    width = 90,
    height = 90,
    visual = CompoundVisual(
        ColorVisual(Color(0xFFC656)),
        TextVisual(
            text = "Top left",
            font = Font(
                size = 16,
                color = Color(0x0F141F)
            ),
            alignment = Alignment.TOP_LEFT
        )
    )
)
&nbsp;
val textOffset = TokenView(
    width = 90,
    height = 90,
    visual = CompoundVisual(
        ColorVisual(Color(0xC6FF6E)),
        TextVisual(
            text = "Offset",
            font = Font(
                size = 16,
                color = Color(0x0F141F)
            ),
            offsetX = 10,
            offsetY = -20,
            rotation = -20
        )
    )
)
</preview>

> The default `Font` is Arial in black with size 14. Fonts from your own resources can be registered as described in [Advanced Usage][AdvancedUsage].
> {style="note"}

---

## ImageVisual

Display an image from your resources.

An [ImageVisual][ImageVisualKDoc] displays an image. The image is loaded from the **resources** folder of your project, the `path` is relative to it. The image is stretched to the size of the component.

```kotlin
val visual = ImageVisual("assets/elements/fortify.png")
```

> Images must be bundled with the application. URLs are rejected, and a path that cannot be found in the resources throws an `IllegalArgumentException` immediately. On Linux and macOS file names are case-sensitive.
> {style="warning"}

<signature>
<code-block lang="kotlin" copy="false">
ImageVisual(
    path: String,
    width: Int = -1,
    height: Int = -1,
    offsetX: Int = 0,
    offsetY: Int = 0,
    rotation: Number = 0
)
</code-block>

To use only a part of an image, for example one sprite of a texture atlas, pass the size of the part as `width` and `height` and its top left corner as `offsetX` and `offsetY`. All values are **pixels of the image file**. A `width` or `height` of `-1` uses everything that is left from the offset. `rotation` turns the image by the given degrees.

</signature>

The card image used below is 214 × 345 pixels. From left to right the tokens show the full image, its left half, its right half, its top half, and the image rotated by 90°:

<preview key="tools.aqua.bgw.main.examples.ExampleVisualScene.imageExample">
val imageFull = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    )
)
&nbsp;
val imageLeftHalf = TokenView(
    width = 35,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png",
        width = 107,
        height = 345
    )
)
&nbsp;
val imageRightHalf = TokenView(
    width = 35,
    height = 113,
    visual = ImageVisual(
        path = "assets/elements/fortify.png",
        width = 107,
        height = 345,
        offsetX = 107
    )
)
&nbsp;
val imageTopHalf = TokenView(
    width = 70,
    height = 56,
    visual = ImageVisual(
        "assets/elements/fortify.png",
        width = 214,
        height = 172
    )
)
&nbsp;
val imageRotated = TokenView(
    width = 113,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png",
        rotation = 90
    )
)
</preview>

---

## CompoundVisual

Stack multiple visuals.

A [CompoundVisual][CompoundVisualKDoc] displays several single layer visuals on top of each other. The visuals are rendered in the order they were passed, the first one is at the bottom. Every layer except the lowest should be at least partially transparent to let the layers beneath shine through.

Typical use cases are text on a background, a tint over an image, or a badge:

<preview key="tools.aqua.bgw.main.examples.ExampleVisualScene.compoundExample">
val compoundImageAndColor = TokenView(
    width = 70,
    height = 113,
    visual = CompoundVisual(
        ImageVisual("assets/elements/fortify.png"),
        ColorVisual(Color(0xC6FF6E)).apply {
            transparency = 0.35
        }
    )
)
&nbsp;
val compoundImageAndText = TokenView(
    width = 70,
    height = 113,
    visual = CompoundVisual(
        ImageVisual("assets/elements/fortify.png"),
        TextVisual(
            text = "Fortify",
            font = Font(
                size = 14,
                color = Color.WHITE,
                fontWeight = Font.FontWeight.BOLD
            ),
            alignment = Alignment.BOTTOM_CENTER
        )
    )
)
&nbsp;
val compoundBadge = TokenView(
    width = 100,
    height = 100,
    visual = CompoundVisual(
        ColorVisual(Color(0x161D29)).apply {
            style.borderRadius = BorderRadius.LARGE
        },
        ColorVisual(Color(0xEF4444)).apply {
            style.borderRadius = BorderRadius.FULL
            transparency = 0.8
        },
        TextVisual(
            "7",
            font = Font(
                size = 40,
                color = Color.WHITE
            )
        )
    )
)
</preview>

The `children` of a compound visual can be changed at runtime. The component updates automatically:

```kotlin
val compound = CompoundVisual(ColorVisual.WHITE)
token.visual = compound

compound.children = listOf(ColorVisual.WHITE, TextVisual("Hello"))
```

> `Visual.EMPTY` is an empty compound visual and can be used to display nothing at all.
> {style="note"}

---

## Styling

All single layer visuals share the same additional styling options. They are applied on top of what the visual displays.

> Styles and filters are rendered by the browser engine and are meant for **visual** effects. The API reference marks them as an advanced feature, so avoid relying on exotic combinations.
> {style="warning"}

##### Transparency and flipping

<signature>
<code-block lang="kotlin" copy="false">
transparency: Double
flipped: Flip
</code-block>

`transparency` ranges from `0.0` (fully transparent) to `1.0` (opaque), which is the default. `flipped` mirrors the visual horizontally, vertically or both with a [Flip][FlipKDoc] value: `NONE`, `HORIZONTAL`, `VERTICAL` or `BOTH`.

</signature>

<preview key="tools.aqua.bgw.main.examples.ExampleVisualScene.flipExample">
val flipNone = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    )
)
&nbsp;
val flipHorizontal = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    ).apply {
        flipped = Flip.HORIZONTAL
    }
)
&nbsp;
val flipVertical = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    ).apply {
        flipped = Flip.VERTICAL
    }
)
&nbsp;
val flipBoth = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    ).apply {
        flipped = Flip.BOTH
    }
)
</preview>

##### Style

<signature>
<code-block lang="kotlin" copy="false">
style.borderRadius: BorderRadius
style.cursor: Cursor
</code-block>

The [Style][StyleKDoc] of a visual defines the roundness of its corners and the mouse cursor that is shown above it. A [BorderRadius][BorderRadiusKDoc] is created from a size in virtual pixels, one value for all corners or four values for top left, top right, bottom right and bottom left. The companion object offers the presets `NONE`, `XS`, `SMALL`, `MEDIUM`, `LARGE`, `XL`, `XXL`, `XXXL` (2 to 128 pixels) and `FULL` for circles. The [Cursor][CursorKDoc] companion contains all common cursors like `POINTER` or `GRAB`.

</signature>

<preview key="tools.aqua.bgw.main.examples.ExampleVisualScene.styleExample">
val radiusNone = TokenView(
    width = 70,
    height = 70,
    visual = ColorVisual(Color(0xFA6C56)).apply {
        style.borderRadius = BorderRadius.NONE
    }
)
&nbsp;
val radiusMedium = TokenView(
    width = 70,
    height = 70,
    visual = ColorVisual(Color(0xFA6C56)).apply {
        style.borderRadius = BorderRadius.MEDIUM
    }
)
&nbsp;
val radiusXL = TokenView(
    width = 70,
    height = 70,
    visual = ColorVisual(Color(0xFA6C56)).apply {
        style.borderRadius = BorderRadius.XL
    }
)
&nbsp;
val radiusFull = TokenView(
    width = 70,
    height = 70,
    visual = ColorVisual(Color(0xFA6C56)).apply {
        style.borderRadius = BorderRadius.FULL
    }
)
&nbsp;
val radiusCustom = TokenView(
    width = 70,
    height = 70,
    visual = ColorVisual(Color(0xFA6C56)).apply {
        style.borderRadius = BorderRadius(
            32,
            0,
            32,
            0
        )
        style.cursor = Cursor.POINTER
    }
)
</preview>

##### Filters

<signature>
<code-block lang="kotlin" copy="false">
filters.blur: BlurFilter
filters.saturation: SaturationFilter
filters.sepia: SepiaFilter
</code-block>

[Filters][FilterKDoc] change how a visual is rendered. `BlurFilter` takes a radius (presets `SMALL`, `MEDIUM`, `LARGE`), `SaturationFilter` a factor where `GREYSCALE` removes all color and `SepiaFilter` a value between `0` and `1` (`SEPIA` for the full effect).

</signature>

<preview key="tools.aqua.bgw.main.examples.ExampleVisualScene.filterExample">
val filterNone = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    )
)
&nbsp;
val filterBlur = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    ).apply {
        filters.blur = BlurFilter.SMALL
    }
)
&nbsp;
val filterGreyscale = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    ).apply {
        filters.saturation =
            SaturationFilter.GREYSCALE
    }
)
&nbsp;
val filterSepia = TokenView(
    width = 70,
    height = 113,
    visual = ImageVisual(
        "assets/elements/fortify.png"
    ).apply {
        filters.sepia = SepiaFilter.SEPIA
    }
)
</preview>

---

## Using visuals

Where a component accepts a visual, you can always pass any of the types above. Some components offer more than one visual slot:

- [CardView, DiceView and more][GameComponentsDoc]: a `CardView` has a `frontVisual` and `backVisual`, a `DiceView` one visual per side.
- Text components like `Button` or `Label` take a `visual` for the background and a separate `font` for their text.
- Scenes and the application use a `background` visual.

Visuals are mutable objects, so sharing one instance between components means that changing it changes all of them. To use the same appearance twice, create two instances or call `copy()`, which also copies transparency, style, filters and flipping. Animating between visuals is covered by the [FlipAnimation and RandomizeAnimation][AnimationDoc].
