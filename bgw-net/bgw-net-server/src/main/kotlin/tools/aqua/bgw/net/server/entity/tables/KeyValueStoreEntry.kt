/*
 * Copyright 2022-2024 The BoardGameWork Authors
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

@file:Suppress("JpaDataSourceORMInspection")

package tools.aqua.bgw.net.server.entity.tables

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

/**
 * Table structure for key value pair entries.
 *
 * @property key The key. ID field.
 * @property value The value.
 */
@Entity
@Table(name = "GENERIC_KEY_VALUE_STORE")
class KeyValueStoreEntry(
    @Id @Column(name = "key_for_entry", nullable = false, updatable = false) val key: String,
    //
    @Column(name = "value_for_entry", nullable = false) var value: String,
)
