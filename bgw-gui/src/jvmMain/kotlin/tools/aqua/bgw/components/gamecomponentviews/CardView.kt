/*
 * Copyright 2021-2024 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package tools.aqua.bgw.components.gamecomponentviews

import tools.aqua.bgw.components.gamecomponentviews.CardView.CardSide.BACK
import tools.aqua.bgw.components.gamecomponentviews.CardView.CardSide.FRONT
import tools.aqua.bgw.core.DEFAULT_CARD_HEIGHT
import tools.aqua.bgw.core.DEFAULT_CARD_WIDTH
import tools.aqua.bgw.visual.Visual

/**
 * A [CardView] may be used to visualize a card.
 *
 * You can inherit from this class if you want to add additional functionality or fields. Inheriting
 * does NOT change how a cardView is visualized by the BGW framework.
 *
 * Visualization:
 *
 * The [Visual] at the [currentSide] value is used to visualize the card. By default, the back side
 * is shown.
 *
 * @constructor Creates a [CardView] with given [Visual]s.
 *
 * @param posX Horizontal coordinate for this [CardView]. Default: 0.
 * @param posY Vertical coordinate for this [CardView]. Default: 0.
 * @param width Width for this [CardView]. Default: [DEFAULT_CARD_WIDTH].
 * @param height Height for this [CardView]. Default: [DEFAULT_CARD_HEIGHT].
 * @param front Visual to represent the front side of the card.
 * @param back Visual to represent the back side of the card. Default: same [Visual] as front.
 */
open class CardView(
    posX: Number = 0,
    posY: Number = 0,
    width: Number = DEFAULT_CARD_WIDTH,
    height: Number = DEFAULT_CARD_HEIGHT,
    front: Visual,
    back: Visual = front
) :
    GameComponentView(
        posX = posX, posY = posY, width = width, height = height, visual = Visual.EMPTY
    ) {
    /**
     * The current [CardSide] that is displayed.
     *
     * @see showFront
     * @see showBack
     */
    var currentSide: CardSide = FRONT
        set(value) {
            if (field != value) {
                field = value

                visualProperty.value = if (value == FRONT) frontVisual else backVisual
            }
        }

    /** Front [Visual] for this [CardView]. */
    var frontVisual: Visual = Visual.EMPTY
        /** Sets front [Visual] for this [CardView] as a copy of given [value]. */
        set(value) {
            field = value.copy()

            if (currentSide == FRONT) super.visual = field
        }

    /** Back [Visual] for this [CardView]. */
    var backVisual: Visual = Visual.EMPTY
        /** Sets back [Visual] for this [CardView] as a copy of given [value]. */
        set(value) {
            field = value.copy()

            if (currentSide == BACK) super.visual = field
        }

    override var visual: Visual
        get() = super.visual
        set(_) {
            throw UnsupportedOperationException(
                "Setting a single Visual for a CardView is not supported. " +
                        "Use `frontVisual` and `backVisual` setter instead."
            )
        }

    init {
        this.frontVisual = front
        this.backVisual = back
        this.currentSide = BACK
    }

    /** Sets the [currentSide] to be displayed to [CardSide.FRONT]. */
    fun showFront() {
        currentSide = FRONT
    }

    /** Sets the [currentSide] to be displayed to [CardSide.BACK]. */
    fun showBack() {
        currentSide = BACK
    }

    /** Sets the [currentSide] to the parameter value. */
    fun showCardSide(side: CardSide) {
        currentSide = side
    }

    /** Flips the [CardView] by seting the [currentSide] to the other value. */
    fun flip() {
        currentSide = if (currentSide == BACK) FRONT else BACK
    }

    /** Enum for the card sides [FRONT] and [BACK] with their visual indices. */
    enum class CardSide {
        /** The [FRONT] side. */
        FRONT,

        /** The [BACK] side. */
        BACK
    }
}
