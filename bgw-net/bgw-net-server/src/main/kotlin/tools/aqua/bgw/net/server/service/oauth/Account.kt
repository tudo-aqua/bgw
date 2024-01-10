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

package tools.aqua.bgw.net.server.service.oauth

import java.io.Serializable
import javax.persistence.*

/** Represents a user account login in with an oauth provider. */
@Entity
class Account(
    /** unique identifier for each user. */
    @GeneratedValue @Id var id: Long? = null,
    /** Stands for subject and is a unique identifier from the oath protocol. */
    var sub: String = "",
    /** A clear name used for displaying the user. */
    var accountName: String = "",
    /** Roles the user has. for example admin user etc. */
    var role: String = "",
) : Serializable {
  /** Checks whether this user is an admin. Meaning if he has that role associated to him. */
  fun isAdmin(): Boolean = role.contains("admin")

  companion object {
    private const val serialVersionUID: Long = 1L
  }
}
