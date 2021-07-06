package tools.aqua.bgw.elements.uielements

import tools.aqua.bgw.util.Font

/**
 * A [TextField] is a single line input field.
 * Whenever user input occurs the [label] field gets updated.
 *
 * @param height height for this [TextField]. Default: [TextField.DEFAULT_TEXTFIELD_HEIGHT].
 * @param width width for this [TextField]. Default: [TextField.DEFAULT_TEXTFIELD_WIDTH].
 * @param posX horizontal coordinate for this [TextField]. Default: 0.
 * @param posY vertical coordinate for this [TextField]. Default: 0.
 * @param label initial label for this [TextField]. Default: empty String.
 * @param prompt Prompt for this [TextField].
 *        This gets displayed as a prompt to the user whenever the label is an empty string.
 *        Default: empty string.
 */
open class TextField(
    height: Number = DEFAULT_TEXTFIELD_HEIGHT,
    width: Number = DEFAULT_TEXTFIELD_WIDTH,
    posX: Number = 0,
    posY: Number = 0,
    label: String = "",
    font: Font = Font(),
    val prompt: String = "",
) : LabeledUIElementView(height = height, width = width, posX = posX, posY = posY, label = label, font = font) {
    /**
     * Defines some static constants that can be used as suggested properties of a [TextField].
     */
    companion object {
        /**
         * Suggested [TextField] [height].
         */
        const val DEFAULT_TEXTFIELD_HEIGHT: Int = 30
    
        /**
         * Suggested [TextField] [width].
         */
        const val DEFAULT_TEXTFIELD_WIDTH: Int = 140
    }
}