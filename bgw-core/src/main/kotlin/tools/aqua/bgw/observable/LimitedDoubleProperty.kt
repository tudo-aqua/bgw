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

package tools.aqua.bgw.observable

/**
 * A limited DoubleProperty to a value range. Value will be checked to be in range
 * [lowerBoundInclusive] to [upperBoundInclusive].
 *
 * Therefore [upperBoundInclusive] must be greater or equal to [lowerBoundInclusive].
 * The Range is constant and cannot be altered after object creation.
 *
 * @constructor Creates a [LimitedDoubleProperty] with given bounds and initial value.
 *
 * @param lowerBoundInclusive Lower bound inclusive. Default: -inf.
 * @param upperBoundInclusive Upper bound inclusive. Default: +inf.
 * @param initialValue Initial Value. Default: [lowerBoundInclusive].
 *
 * @throws IllegalArgumentException If a value out of range is set as initialValue.
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
	 *
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