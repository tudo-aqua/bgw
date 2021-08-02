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

package observable.property

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.observable.LimitedDoubleProperty

open class PropertyTestBase {
	protected val initialValue: Double = 13.0
	protected val newValue: Double = 42.0
	private val lowerBound: Double = -5.0
	private val upperBound: Double = 50.0
	
	protected lateinit var property: LimitedDoubleProperty
	protected val listener1: TestListener = TestListener()
	protected val listener2: TestListener = TestListener()
	protected val internalListener: TestListener = TestListener()
	protected val guiListener: TestListener = TestListener()
	
	@BeforeEach
	fun setUp() {
		property = LimitedDoubleProperty(lowerBound, upperBound, initialValue)
		property.addListener(listener1)
		property.addListener(listener2)
		property.internalListener = internalListener
		property.guiListener = guiListener
	}
	
	class TestListener : ((Double, Double) -> Unit) {
		var invokedCount: Int = 0
		var oldValue: Double? = null
		var newValue: Double? = null
		
		override fun invoke(oV: Double, nV: Double) {
			invokedCount++
			oldValue = oV
			newValue = nV
		}
	}
}