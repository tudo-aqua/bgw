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

@file:Suppress("MemberVisibilityCanBePrivate")

package tools.aqua.bgw.net.client

/**
 * Network logger. Prints debug info to the console according to verbosity.
 *
 * @property networkLoggingBehavior Verbosity of output.
 */
internal class NetworkLogger(private val networkLoggingBehavior: NetworkLogging) {

  /**
   * Prints [msg] to the error stream ignoring [networkLoggingBehavior].
   *
   * @param msg Message to be printed.
   */
  fun err(msg: String) {
    System.err.println(msg)
  }

  /**
   * Prints [msg] to the out stream ignoring [networkLoggingBehavior].
   *
   * @param msg Message to be printed.
   */
  fun println(msg: String) {
    kotlin.io.println(msg)
  }

  /**
   * Prints an error to the console if verbosity is not [NetworkLogging.NO_LOGGING].
   *
   * @param msg Message to be printed.
   * @param throwable Error that was thrown.
   */
  fun error(msg: String, throwable: Throwable? = null) {
    if (networkLoggingBehavior != NetworkLogging.NO_LOGGING) {
      println("[ERROR] $msg")
      throwable?.printStackTrace()
    }
  }

  /**
   * Prints simple debug information to the console if verbosity is set to [NetworkLogging.INFO] or
   * [NetworkLogging.VERBOSE].
   *
   * @param msg Message to be printed.
   */
  fun info(msg: String) {
    if (networkLoggingBehavior == NetworkLogging.INFO ||
        networkLoggingBehavior == NetworkLogging.VERBOSE) {
      println("[INFO] $msg")
    }
  }

  /**
   * Prints verbose debug information to the console if verbosity is set to [NetworkLogging.VERBOSE]
   * .
   *
   * @param msg Message to be printed.
   */
  fun debug(msg: String) {
    if (networkLoggingBehavior == NetworkLogging.VERBOSE) {
      println("[DEBUG] $msg")
    }
  }
}
