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

package tools.aqua.bgw.net.client

/** Defines how verbose the network traffic should be logged to the console. */
enum class NetworkLogging {
  /** Network traffic will not be logged to the console. */
  NO_LOGGING,

  /** Only log errors to the console. */
  ERRORS,

  /** Simple logging informing about sent and received messages as one-line info. */
  INFO,

  /** Verbose debug output for complete network logging. */
  VERBOSE
}
