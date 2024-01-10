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

import java.util.*
import org.springframework.data.jpa.repository.JpaRepository

/** Holds information about user accounts. Each user represents an oauth login. */
interface AccountRepository : JpaRepository<Account, Long> {
  /**
   * Finds a user account by their unique oauth subject identifier. May return an [Optional.EMPTY]
   */
  fun findBySub(sub: String?): Optional<Account>
}
