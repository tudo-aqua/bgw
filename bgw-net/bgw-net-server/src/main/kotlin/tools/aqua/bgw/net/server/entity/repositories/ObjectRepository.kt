/*
 * Copyright 2022 The BoardGameWork Authors
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

package tools.aqua.bgw.net.server.entity.repositories

/** Repository of objects. */
sealed class ObjectRepository<T> {
  /** All active and orphaned game instances. */
  protected val objectSet: MutableSet<T> = mutableSetOf()

  /**
   * Adds the given object to the [objectSet].
   *
   * @param obj object to add.
   *
   * @return 'true' iff [obj] was successfully added to [objectSet].
   */
  fun add(obj: T): Boolean = objectSet.add(obj)

  /**
   * Removes the given object from the [objectSet].
   *
   * @param obj object to remove.
   *
   * @return 'true' iff [obj] was successfully removed from [objectSet].
   */
  fun remove(obj: T): Boolean = objectSet.remove(obj)

  /**
   * Returns [objectSet] as immutable list.
   *
   * @return [objectSet] as immutable list.
   */
  fun getAll(): List<T> {
    @Suppress("UNNECESSARY_SAFE_CALL", "USELESS_ELVIS") return objectSet?.toList() ?: listOf()
  }
}
