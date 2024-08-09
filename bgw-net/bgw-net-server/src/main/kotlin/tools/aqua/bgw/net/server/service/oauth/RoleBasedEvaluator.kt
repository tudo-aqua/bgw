/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

import com.vaadin.flow.router.Location
import org.ilay.Access
import org.ilay.AccessEvaluator
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.core.user.DefaultOAuth2User

/**
 * Checks whether a given user account has enough permission based on their role.
 *
 * @property accountRepository Repository holding information about the oauth login accounts.
 */
open class RoleBasedEvaluator(private var accountRepository: AccountRepository) :
    AccessEvaluator<SecuredByRole> {
  override fun evaluate(
      location: Location,
      navigationTarget: Class<*>,
      annotation: SecuredByRole
  ): Access {
    val principal = SecurityContextHolder.getContext().authentication.principal as DefaultOAuth2User
    val account = accountRepository.findBySub(principal.name)

    return when {
      account.isEmpty -> Access.restricted("/")
      account.get().role in annotation.value -> Access.granted()
      else -> Access.restricted("/")
    }
  }
}
