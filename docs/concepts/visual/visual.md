---
parent: Concepts
title: Visual
nav_order: 1
layout: default
---

## Visual
{: .no_toc}
<details open markdown="block">
  <summary>
    Table of contents
  </summary>
  {: .text-delta }
1. TOC
{:toc}
</details>

In this section we are going to showcase the differend types of visuals in the BGW framework.
Visuals are used to style game elements in the application. Fundamentally there are two types of Visuals: 
 * [SingleLayerVisual](bgw/concepts/visual/visual.html#singlelayervisual)
 * [CompoundVisualVisual](bgw/concepts/visual/visual.html#compoundvisual)

## [SingleLayerVisual](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.visual/-single-layer-visual/)
A simple visual displaying it's type of visualization.

### [ColorVisual](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.visual/-color-visual/)
The ColorVisual displays a solid Color. It can either be created with RGB / RGBA values or via java.awt.Color. Additionally it declares static fields for various standard colors. The following statements result in the same *red* visual:
````kotlin
val visual1: ColorVisual = ColorVisual(255, 0, 0)
val visual2: ColorVisual = ColorVisual(java.awt.Color(255, 0, 0))
val visual3: ColorVisual = ColorVisual(java.awt.Color.RED)
val visual4: ColorVisual = ColorVisual.RED
````


### [TextVisual](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.visual/-text-visual/)
The TextVisual displays one line of text. It can be created with the text to display as a string and an optional [Font](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.util/-font/). The following example shows the code for a TextVisual that displays "Token" in white with bold text in Arial 18px size. All parameters in the font constructor are optional, so *fontStyle* for example is redundant as it contains the default value:
````kotlin
val visual: TextVisual = TextVisual("Token", Font(
  size = 18,
  color = Color.WHITE,
  family = "Arial",
  fontWeight = Font.FontWeight.BOLD,
  fontStyle = Font.FontStyle.NORMAL
))
````

### [ImageVisual](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.visual/-image-visual/)
The ImageVisual displays a a given Image. It can be eather be created with a [BufferedImage](https://docs.oracle.com/en/java/javase/11/docs/api/java.desktop/java/awt/image/BufferedImage.html) or by declaring the image file to load as [File](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/io/File.html) or path string relative to the application's ressource folder. Addidtionally the size and position of a sub-image can be passed in order to select from a texture map. The following code example shows how to load an image:
````kotlin
val visual: ImageVisual = ImageVisual("card_deck.png")
````
![image](card_deck.png)

To select for example the **three of diamonds** from this texture map as a sub image, knowing that each card has a height of 200px and width 130px, we can write the following code:
````kotlin
val visual: ImageVisual = ImageVisual(
  path = "card_deck.png",
  width = 130,
  height = 200,
  offsetX = 2 * 130,
  offsetY = 200
)
````
The full example can be found [here](/bgw-docs-examples/src/main/kotlin/examples/concepts/visuals/VisualsExample.kt).

## [CompoundVisual](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.visual/-compound-visual/)
CompundVisuals can be used to stack multiple visuals. Therefore if stacking [ColorVisuals](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.visual/-color-visual/) and [ImageVisuals](/bgw/kotlin-docs/bgw-core/tools.aqua.bgw.visual/-image-visual/) they have to be partially transparent in order to see the Visual beyond. The Visuals get added in the order they get passed from bottom to top. CompundVisuals can for example be used to create a Text with background Color (left), label a GameToken (center), or display valid drop targets (right):

![image](compounds.png)

The right Visual for example has been created by the following code:
````kotlin
visual = CompoundVisual(
  ImageVisual(
    path = "card_deck.png", 
    width = 130, 
    height = 200, 
    offsetX = 260, 
    offsetY = 200
  ),
  ColorVisual.GREEN.apply { 
    transparency = 0.2 
  }
)
````
The full example can be found [here](/bgw-docs-examples/src/main/kotlin/examples/concepts/visuals/CompoundVisualsExample.kt).
