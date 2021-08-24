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

package observable.observablelist

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.observable.IValueObservable
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.observable.ObservableList

open class ObservableListTestBase {
	
	protected lateinit var list: ObservableList<Int>
	protected val unordered: List<Int> = listOf(13, 25, 17, 13, -4)
	protected val ordered: List<Int> = listOf(-4, 13, 13, 17, 25)
	
	private val listener: TestListener<List<Int>> = TestListener()
	
	@BeforeEach
	fun setUp() {
		list = ObservableArrayList(unordered)
		list.addListener(listener)
	}
	
	protected fun checkNotNotified(): Boolean = listener.invokedCount == 0
	
	protected fun checkNotified(count: Int = 1): Boolean = listener.invokedCount == count
	
	class TestListener<T> : IValueObservable<T> {
		var invokedCount: Int = 0

		override fun update(oldValue: T, newValue: T) {
			invokedCount++
		}
	}
}