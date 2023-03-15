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

package tools.aqua.bgw.util.bidirectionalmap

import org.junit.jupiter.api.BeforeEach
import tools.aqua.bgw.util.BidirectionalMap

/** Test base for in BidirectionalMap. */
open class BidirectionalMapTestBase {

  /** BidirectionalMap initially filled with pairs (0,1) and (2,3). */
  protected lateinit var map: BidirectionalMap<Int, Int>

  /** Fills BidirectionalMap with pairs (0,1) and (2,3) before tests. */
  @BeforeEach
  fun setUp() {
    map = BidirectionalMap(Pair(0, 1), Pair(2, 3))
  }
}
