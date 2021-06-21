@file:Suppress("unused")

package tools.aqua.bgw.observable

/**
 * Property superclass providing observable fields.
 *
 * @param initialValue Initial value of this property.
 */
sealed class Property<T>(initialValue: T) : ValueObservable<T>() {
	protected var realValue: T = initialValue
	
	/**
	 * Value of this property.
	 */
	open var value: T
		get() = realValue
		set(value) {
			if (realValue != value) {
				realValue = value
				notifyChange(realValue)
			}
		}
	
	/**
	 * Overrides value of this property without notifying student listeners.
	 * Only notifies GUI listener.
	 */
	internal open fun setSilent(value: T) {
		this.realValue = value
		notifyGUIListener(value)
	}
	
	/**
	 * Notifies all listeners with current value.
	 */
	fun notifyUnchanged() = notifyChange(realValue)
}

/**
 * A Boolean Property.
 *
 * @param initialValue initial Value. Default: false
 */
class BooleanProperty(initialValue: Boolean = false) : Property<Boolean>(initialValue)

/**
 * An Integer Property.
 *
 * @param initialValue initial Value. Default: 0
 */
class IntegerProperty(initialValue: Int = 0) : Property<Int>(initialValue)

/**
 * A Double Property.
 *
 * @param initialValue initial Value. Default: 0.0
 */
class DoubleProperty(initialValue: Number = 0.0) : Property<Double>(initialValue.toDouble())

/**
 * A limited Double Property to a value range.
 *
 * @throws IllegalArgumentException If a value out of range is set.
 *
 * @param lowerBoundInclusive lower bound inclusive. Default: -inf
 * @param upperBoundInclusive lower bound inclusive. Default: +inf
 * @param initialValue initial Value. Default: 0.0
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
		get() = realValue
		set(value) {
			checkBounds(value)
			
			if (realValue != value) {
				realValue = value
				notifyChange(realValue)
			}
		}
	
	/**
	 * Overrides value of this property without notifying student listeners.
	 * Only notifies GUI listener.
	 */
	override fun setSilent(value: Double) {
		checkBounds(value)
		this.realValue = value
		notifyGUIListener(value)
	}
	
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
 * A String Property.
 *
 * @param initialValue initial Value. Default: Empty string
 */
class StringProperty(initialValue: String = "") : Property<String>(initialValue)

/**
 * A Object Property with generic type.
 *
 * @param initialValue initial Value.
 */
class ObjectProperty<T>(initialValue: T) : Property<T>(initialValue)