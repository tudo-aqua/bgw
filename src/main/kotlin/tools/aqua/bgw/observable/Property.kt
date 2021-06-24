@file:Suppress("unused")

package tools.aqua.bgw.observable

/**
 * Property superclass providing observable fields.
 *
 * @param initialValue initial value of this property.
 */
sealed class Property<T>(initialValue: T) : ValueObservable<T>() {
	
	/**
	 * Value of this property.
	 */
	protected var boxedValue: T = initialValue
	
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
	 * Overrides value of this property without notifying student listeners.
	 * Only notifies GUI listener.
	 */
	internal open fun setSilent(value: T) {
		val savedValue = boxedValue
		this.boxedValue = value
		notifyGUIListener(savedValue, value)
	}
	
	/**
	 * Notifies all listeners with current value.
	 */
	fun notifyUnchanged() = notifyChange(boxedValue, boxedValue)
}

/**
 * A BooleanProperty.
 *
 * @param initialValue initial Value. Default: false.
 */
class BooleanProperty(initialValue: Boolean = false) : Property<Boolean>(initialValue)

/**
 * An IntegerProperty.
 *
 * @param initialValue initial Value. Default: 0.
 */
class IntegerProperty(initialValue: Int = 0) : Property<Int>(initialValue)

/**
 * A DoubleProperty.
 *
 * @param initialValue initial Value. Default: 0.0.
 */
class DoubleProperty(initialValue: Number = 0.0) : Property<Double>(initialValue.toDouble())

/**
 * A limited DoubleProperty to a value range.
 *
 * @throws IllegalArgumentException if a value out of range is set.
 *
 * @param lowerBoundInclusive lower bound inclusive. Default: -inf.
 * @param upperBoundInclusive lower bound inclusive. Default: +inf.
 * @param initialValue initial Value. Default: 0.0.
 */
class LimitedDoubleProperty(
	private val lowerBoundInclusive: Number = Double.NEGATIVE_INFINITY,
	private val upperBoundInclusive: Number = Double.POSITIVE_INFINITY,
	initialValue: Number = 0
) : Property<Double>(initialValue.toDouble()) {
	/**
	 * Value of this property.
	 */
	override var value: Double
		get() = boxedValue
		set(value) {
			checkBounds(value)
			
			val savedValue = boxedValue
			if (boxedValue != value) {
				boxedValue = value
				notifyChange(savedValue, value)
			}
		}
	
	/**
	 * Overrides value of this property without notifying student listeners.
	 * Only notifies GUI listener.
	 */
	override fun setSilent(value: Double) {
		checkBounds(value)
		
		val savedValue = boxedValue
		this.boxedValue = value
		notifyGUIListener(savedValue, value)
	}
	
	/**
	 * Checks whether the given value is in the valid range.
	 *
	 * @return `true` if the given value is in the valid range, `false` otherwise.
	 */
	private fun checkBounds(value: Double) {
		require(value >= lowerBoundInclusive.toDouble()) {
			"Argument is lower than lower bound for this property."
		}
		require(value <= upperBoundInclusive.toDouble()) {
			"Argument is higher than upper bound for this property."
		}
	}
}

/**
 * A StringProperty.
 *
 * @param initialValue initial Value. Default: Empty string.
 */
class StringProperty(initialValue: String = "") : Property<String>(initialValue)

/**
 * An ObjectProperty with generic type.
 *
 * @param initialValue initial value.
 */
class ObjectProperty<T>(initialValue: T) : Property<T>(initialValue)