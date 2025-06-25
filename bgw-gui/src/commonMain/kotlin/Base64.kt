/*
 * Copyright 2025 The BoardGameWork Authors
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

@file:Suppress("unused")

internal object Base64 {
  private fun encode(bytes: ByteArray): String {
    return bytes.joinToString("") { it.toUByte().toString(16).padStart(2, '0') }
  }

  private fun decode(string: String): ByteArray {
    return string.chunked(2).map { it.toInt(16).toByte() }.toByteArray()
  }

  fun encode(string: String): String {
    return encode(string.encodeToByteArray())
  }

  fun decode(string: String, charset: String = "UTF-8"): String {
    return decode(string).decodeToString()
  }
}
