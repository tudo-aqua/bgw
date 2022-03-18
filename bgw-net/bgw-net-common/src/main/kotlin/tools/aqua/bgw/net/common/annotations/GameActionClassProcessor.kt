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

package tools.aqua.bgw.net.common.annotations

import com.google.common.reflect.ClassPath
import tools.aqua.bgw.net.common.GameAction

object GameActionClassProcessor {

  @Suppress("UNCHECKED_CAST")
  fun getAnnotatedClasses(): Set<Class<out GameAction>> =
      ClassPath.from(ClassLoader.getSystemClassLoader())
          .allClasses
          .mapNotNull { clazz -> clazz.loadOrNull() }
          .filter {
            val isGameAction = GameAction::class.java.isAssignableFrom(it)
            val isAnnotationPresent = it.isAnnotationPresent(GameActionClass::class.java)

            if (isGameAction && !isAnnotationPresent) {
              System.err.println(
                  "Found class $it not annotated with @GameActionClass that does inherit from GameAction. " +
                      "Add missing annotation to use class as message type. Ignoring.")
            } else if (!isGameAction && isAnnotationPresent) {
              System.err.println(
                  "Found class $it annotated with @GameActionClass that does not inherit from GameAction. " +
                      "This Class will not be usable as Message type. Ignoring.")
            }

            isGameAction && isAnnotationPresent
          }
          .toSet() as
          Set<Class<out GameAction>>

  private fun ClassPath.ClassInfo.loadOrNull(): Class<*>? =
      try {
        load()
      } catch (_: NoClassDefFoundError) {
        null
      }
}
