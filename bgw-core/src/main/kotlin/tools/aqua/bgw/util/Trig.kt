package tools.aqua.bgw.util

class Trig {
	companion object{
		/**
		 * Sin function for angles in degrees.
		 */
		@Suppress("GrazieInspection")
		fun sinD(degrees: Number): Double = kotlin.math.sin(Math.toRadians(degrees.toDouble()))
		
		/**
		 * Cos function for angles in degrees.
		 */
		@Suppress("GrazieInspection")
		fun cosD(degrees: Number): Double = kotlin.math.cos(Math.toRadians(degrees.toDouble()))
	}
}