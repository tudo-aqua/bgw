/*
 * Copyright 2021-2023 The BoardGameWork Authors
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

package tools.aqua.bgw.observable.property

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import tools.aqua.bgw.observable.properties.*

/** Test base for Properties. */
open class PropertyTestBase {

  /** The initial property value. */
  protected val initialValue: Double = 13.0

  /** The new property value to be set. */
  protected val newValue: Double = 42.0

  /** Lower bound for [LimitedDoubleProperty]. */
  private val lowerBound: Double = -5.0

  /** Upper bound for [LimitedDoubleProperty]. */
  private val upperBound: Double = 50.0

  /** Property field. */
  protected lateinit var property: LimitedDoubleProperty

  /** [TestListener] 1 catching update invocation. */
  protected val listener1: TestListener = TestListener()

  /** [TestListener] 2 catching update invocation. */
  protected val listener2: TestListener = TestListener()

  /** The internal listener. */
  protected val internalListener: TestListener = TestListener()

  /** The gui listener. */
  protected val guiListener: TestListener = TestListener()

  /** Initializes property with given value and bounds before each test. */
  @BeforeEach
  fun setUp() {
    property = LimitedDoubleProperty(lowerBound, upperBound, initialValue)
    property.addListener(listener1)
    property.addListener(listener2)
    property.internalListener = internalListener
    property.guiListener = guiListener
  }

  /** Tests instantiations of all properties. */
  @Test
  @DisplayName("Tests instantiations of all properties")
  fun instantiationTest() {
    BooleanProperty()
    BooleanProperty(true)
    DoubleProperty()
    DoubleProperty(1.0)
    LimitedDoubleProperty()
    LimitedDoubleProperty(lowerBoundInclusive = 0)
    LimitedDoubleProperty(upperBoundInclusive = 1)
    LimitedDoubleProperty(initialValue = 0)
    LimitedDoubleProperty(lowerBoundInclusive = 0, initialValue = 0)
    LimitedDoubleProperty(upperBoundInclusive = 1, initialValue = 0)
    LimitedDoubleProperty(lowerBoundInclusive = 0, upperBoundInclusive = 1)
    LimitedDoubleProperty(lowerBoundInclusive = 0, upperBoundInclusive = 1, initialValue = 0)
    IntegerProperty()
    IntegerProperty(1)
    StringProperty()
    StringProperty("Hello")

    ReadonlyBooleanProperty()
    ReadonlyBooleanProperty(true)
    ReadonlyDoubleProperty()
    ReadonlyDoubleProperty(1.0)
    ReadonlyIntegerProperty()
    ReadonlyIntegerProperty(1)
    ReadonlyStringProperty()
    ReadonlyStringProperty("Hello")
  }

  /** Test listener registering callback invocation. */
  class TestListener : ((Double, Double) -> Unit) {

    /** Tracks invocation count. */
    var invokedCount: Int = 0

    /** Tracks old value. */
    var oldValue: Double? = null

    /** Tracks new value. */
    var newValue: Double? = null

    override fun invoke(oV: Double, nV: Double) {
      invokedCount++
      oldValue = oV
      newValue = nV
    }
  }
}
