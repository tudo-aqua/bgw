/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.observable

/**
 * Property baseclass providing observable fields.
 *
 * @param initialValue initial value of this property.
 */
open class Property<T>(initialValue: T) : ValueObservable<T>() {
	
	/**
	 * Value of this property.
	 */
	private var boxedValue: T = initialValue
	
	/**
	 * Value of this property.
	 */
	open var value: T
		get() = boxedValue
		set(value) {
			val savedValue = boxedValue
			if (boxedValue != value) {
				boxedValue = value
				notifyChange(savedValue, value)
			}
		}
	
	/**
	 * Overrides [value] of this property without notifying public listeners.
	 * Only notifies GUI listener.
	 */
	internal open fun setSilent(value: T) {
		val savedValue = boxedValue
		if (boxedValue != value) {
			boxedValue = value
			notifyGUIListener(savedValue, value)
		}
	}
	
	/**
	 * Notifies all listeners with current value.
	 */
	fun notifyUnchanged(): Unit = notifyChange(boxedValue, boxedValue)
}

/**
 * A BooleanProperty.
 *
 * @param initialValue initial Value. Default: `false`.
 */
open class BooleanProperty(initialValue: Boolean = false) : Property<Boolean>(initialValue)

/**
 * An IntegerProperty.
 *
 * @param initialValue initial Value. Default: 0.
 */
open class IntegerProperty(initialValue: Int = 0) : Property<Int>(initialValue)

/**
 * A DoubleProperty.
 *
 * @param initialValue initial Value. Default: 0.0.
 */
open class DoubleProperty(initialValue: Number = 0.0) : Property<Double>(initialValue.toDouble())

/**
 * A limited DoubleProperty to a value range. Value will be checked to be in range
 * [lowerBoundInclusive] to [upperBoundInclusive].
 * Therefore [upperBoundInclusive] must be greater or equal to [lowerBoundInclusive].
 * The Range is constant and cannot be altered after object creation.
 *
 * @throws IllegalArgumentException if a value out of range is set as initialValue.
 *
 * @param lowerBoundInclusive lower bound inclusive. Default: -inf.
 * @param upperBoundInclusive upper bound inclusive. Default: +inf.
 * @param initialValue initial Value. Default: [lowerBoundInclusive].
 */
open class LimitedDoubleProperty(
	lowerBoundInclusive: Number = Double.NEGATIVE_INFINITY,
	upperBoundInclusive: Number = Double.POSITIVE_INFINITY,
	initialValue: Number = lowerBoundInclusive
) : Property<Double>(initialValue.toDouble()) {
	
	private val lowerBoundInclusive: Double
	private val upperBoundInclusive: Double
	
	init {
		require(lowerBoundInclusive.toDouble() <= upperBoundInclusive.toDouble()) {
			"Argument is lower than lower bound for this property."
		}
		this.lowerBoundInclusive = lowerBoundInclusive.toDouble()
		this.upperBoundInclusive = upperBoundInclusive.toDouble()
		
		checkBounds(initialValue.toDouble())
	}
	
	/**
	 * Value of this property.
	 */
	override var value: Double
		get() = super.value
		set(value) {
			checkBounds(value)
			super.value = value
		}
	
	/**
	 * Overrides value of this property without notifying public listeners.
	 * Only notifies [guiListener].
	 */
	override fun setSilent(value: Double) {
		checkBounds(value)
		
		super.setSilent(value)
	}
	
	/**
	 * Checks whether the given value is in the valid range.
	 *
	 * @return `true` if the given value is in the valid range, `false` otherwise.
	 */
	private fun checkBounds(value: Double) {
		require(value >= lowerBoundInclusive) {
			"Argument is lower than lower bound for this property."
		}
		require(value <= upperBoundInclusive) {
			"Argument is higher than upper bound for this property."
		}
	}
}

/**
 * A StringProperty.
 *
 * @param initialValue initial Value. Default: Empty string.
 */
open class StringProperty(initialValue: String = "") : Property<String>(initialValue)