package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A [TextField] is a single line input field.
 * Whenever user input occurs the [label] field gets updated.
 *
 * @param height height for this [TextField]. Default: 0.
 * @param width width for this [TextField]. Default: 0.
 * @param posX horizontal coordinate for this [TextField]. Default: 0.
 * @param posY vertical coordinate for this [TextField]. Default: 0.
 * @param label initial label for this [TextField]. Default: empty string.
 * @param prompt Prompt for this [TextField].
 *        This gets displayed as a prompt to the user whenever the label is an empty string.
 *        Default: empty string.
 */
open class TextField(
    height: Number = 0,
    width: Number = 0,
    posX: Number = 0,
    posY: Number = 0,
    label: String = "",
    font: Font = Font(),
    val prompt: String = "",
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, label = label, font = font)