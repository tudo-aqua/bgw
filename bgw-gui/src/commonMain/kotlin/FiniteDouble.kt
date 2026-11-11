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

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/** Serializes [Double]s, writing `NaN` and infinite values as `0.0`. */
internal object FiniteDoubleSerializer : KSerializer<Double> {
  override val descriptor: SerialDescriptor =
      PrimitiveSerialDescriptor("tools.aqua.bgw.FiniteDouble", PrimitiveKind.DOUBLE)

  override fun serialize(encoder: Encoder, value: Double) {
    encoder.encodeDouble(if (value.isFinite()) value else 0.0)
  }

  override fun deserialize(decoder: Decoder): Double = decoder.decodeDouble()
}

/** A [Double] that is always serialized as a finite number, see [FiniteDoubleSerializer]. */
internal typealias FiniteDouble = @Serializable(with = FiniteDoubleSerializer::class) Double
