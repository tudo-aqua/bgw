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

import java.lang.reflect.Method
import tools.aqua.bgw.net.common.GameAction

object GameActionReceiverProcessor {
  fun getAnnotatedReceivers(target: Class<*>): Map<Class<out GameAction>, Method> {
    val map = mutableMapOf<Class<out GameAction>, Method>()
    val annotatedMethods = mutableListOf<Method>()
    var clazz: Class<*> = target

    // Retrieve all annotated methods
    while (clazz != Any::class.java) {
      for (method in clazz.declaredMethods) {
        if (method.isAnnotationPresent(GameActionReceiver::class.java)) {
          annotatedMethods.add(method)
        }
      }
      clazz = clazz.superclass
    }

    // Create mapping
    for (method in annotatedMethods) {
      val params: Array<Class<*>> = method.parameterTypes

      // Check parameter count
      if (params.size != 2) {
        System.err.println(
            "Found function $method annotated with @GameActionReceiver that does not declare the expected parameter count. Ignoring.")
        continue
      }

      // Check first parameter is subclass of GameAction
      if (!GameAction::class.java.isAssignableFrom(params[0])) {
        System.err.println(
            "Found function $method annotated with @GameActionReceiver with first parameter not conforming to GameAction which was expected. Ignoring.")
        continue
      }

      // Check second parameter is String
      if (!String::class.java.isAssignableFrom(params[1])) {
        System.err.println(
            "Found function $method annotated with @GameActionReceiver with second parameter not conforming to String which was expected. Ignoring.")
        continue
      }

      @Suppress("UNCHECKED_CAST")
      map.putIfAbsent(params[0] as Class<out GameAction>, method)?.run {
        System.err.println(
            "Found function $method annotated with @GameActionReceiver that has the same parameter types as $this. Ignoring duplicate.")
      }
    }
    return map
  }
}
