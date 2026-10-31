/*
 * Copyright 2025-2026 The BoardGameWork Authors
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

package examples.util

import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.util.CoordinatePlain
import tools.aqua.bgw.util.Stack

/** Compile-checked snippets of the Util guides. */
fun main() {
  // region Stack
  val emptyStack = Stack<String>()
  val collectionStack = Stack(listOf("ONE", "SIX", "TEN"))
  val varargsStack = Stack("ONE", "SIX", "TEN")

  varargsStack.push("TWELVE")
  varargsStack.pushAll("A", "B")
  val top = varargsStack.pop() // "B"
  val peeked = varargsStack.peek() // "A"
  val three = varargsStack.popAll(3) // ["A", "TWELVE", "TEN"]
  val rest = varargsStack.peekAll() // ["SIX", "ONE"]
  val index = varargsStack.indexOf("ONE")
  varargsStack.shuffle()
  varargsStack.sort(compareBy { it.length })
  val removed = varargsStack.clear()
  val safe = emptyStack.popOrNull() // null
  println(listOf(collectionStack.size, top, peeked, three, rest, index, removed, safe))
  // endregion

  // region BidirectionalMap
  val map = BidirectionalMap(1 to "ONE", 6 to "SIX", 10 to "TEN")
  map.add(12, "TWELVE") // true
  map.add(1, "EINS") // false, 1 is already used
  map.addAll(listOf(2 to "TWO", 3 to "THREE")) // true
  val value = map.forward(6) // "SIX"
  val key = map.backward("TEN") // 10
  val missing = map.forwardOrNull(99) // null
  val same = map[6] // "SIX"
  map[6] = "SECHS" // overwrites
  val overwritten = map.put(1, "UNO")
  map.putAll(7 to "SEVEN", 8 to "EIGHT")
  map.remove(12, "TWELVE")
  map.removeForward(1)
  map.removeBackward("TEN")
  println(listOf(value, key, missing, same, overwritten, map.containsForward(6), map.keysForward))
  // endregion

  // region Coordinate
  val coord1 = Coordinate(xCoord = 5, yCoord = 7)
  val coord2 = Coordinate(xCoord = -1, yCoord = 5)
  val added = coord1 + coord2 // X = 4.0, Y = 12.0
  val subtracted = coord1 - coord2 // X = 6.0, Y = 2.0
  val scaled = coord1 * 2 // X = 10.0, Y = 14.0
  val divided = coord1 / 2 // X = 2.5, Y = 3.5
  val rotated = Coordinate(1, 0).rotated(90, Coordinate(0, 0)) // X ~ 0.0, Y = 1.0

  val plain = CoordinatePlain(topLeftX = 0, topLeftY = 0, bottomRightX = 100, bottomRightY = 50)
  val bounds = plain.rotated(45, Coordinate(50, 25))
  println(listOf(added, subtracted, scaled, divided, rotated, plain.width, bounds.topLeftBound))
  // endregion
}
