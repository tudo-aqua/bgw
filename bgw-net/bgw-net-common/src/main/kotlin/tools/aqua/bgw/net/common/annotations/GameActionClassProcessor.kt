/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

/** Processor for all classes annotated with [GameActionClass]. */
object GameActionClassProcessor {

  /**
   * Packages excluded from classpath search for @GameAction annotation. Improves performance by
   * filtering illegal packages instead of loading them.
   */
  private val forbiddenPackagePrefix =
      listOf(
          "META-INF",
          "kotlin",
          "javax",
          "javafx",
          "com.jfoenix",
          "tools.aqua.bgw",
          "com.sun",
          "com.google",
          "com.fasterxml",
          "org.java_websocket",
          "org.jetbrains",
          "org.intellij",
          "org.slf4j",
          "org.checkerframework")

  /**
   * Finds all classes annotated with [GameActionClass] and/or extending [GameAction].
   *
   * Prints Error message if only one condition is met. Classes get ignored in this case.
   *
   * @return Set of all Classes annotated with [GameActionClass] and extending [GameAction].
   *
   * @see GameActionClass
   * @see GameActionReceiver
   * @see GameActionClassProcessor
   */
  @Suppress("UNCHECKED_CAST")
  fun getAnnotatedClasses(): Set<Class<out GameAction>> =
      ClassPath.from(ClassLoader.getSystemClassLoader())
          .allClasses
          .filter { it.isCandidate() }
          .mapNotNull { it.loadOrNull() }
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
          .toSet() as Set<Class<out GameAction>>

  /** Filters [ClassPath] against blacklist [forbiddenPackagePrefix]. */
  private fun ClassPath.ClassInfo.isCandidate(): Boolean =
      packageName.startsWith("tools.aqua.bgw.examples") ||
          packageName.startsWith("tools.aqua.bgw.net.protocol.client") ||
          forbiddenPackagePrefix.none { packageName.startsWith(it) }

  /**
   * Loads [Class] from [ClassPath].
   *
   * @return Loaded [Class] or 'null' if class is not instantiable.
   */
  private fun ClassPath.ClassInfo.loadOrNull(): Class<*>? =
      try {
        load()
      } catch (_: NoClassDefFoundError) {
        null
      }
}
