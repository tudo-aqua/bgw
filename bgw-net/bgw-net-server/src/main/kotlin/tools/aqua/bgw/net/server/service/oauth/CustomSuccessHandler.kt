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

import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.oidc.user.OidcUser
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/** Handles the post authentication process such as storing the users account. */
@Component
class CustomSuccessHandler(
    /** Repository holding information about the oauth login accounts. */
    var accountRepository: AccountRepository,
) : AuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication?
    ) {
        if (authentication != null && authentication.principal is OidcUser) {
            val user = authentication.principal as OidcUser
            val account = accountRepository.findBySub(user.name)
            if (account.isEmpty) {
                user.getAttribute<String>("name")?.let {
                    createAccount(it, user.name, user.getAttribute<ArrayList<String>>("groups"))
                }
            } else {
                user.getAttribute<String>("name")?.let {
                    updateAccount(
                        account.get(), it, user.name, user.getAttribute<ArrayList<String>>("groups")
                    )
                }
            }
        }
        response?.sendRedirect(request?.contextPath + "/")
    }

    /** Update the information about the user account if it might have change since the last login. */
    fun updateAccount(account: Account, name: String, sub: String, groups: ArrayList<String>?) {
        accountRepository.save(
            account.apply {
                this.accountName = name
                this.sub = sub
                this.role = getRoleFromGroups(groups)
            })
    }

    /** Based on the groups the user is in he is assigned to a role. */
    fun getRoleFromGroups(groups: ArrayList<String>?): String =
        if (groups != null && groups.contains("bgw-net-admins")) {
            "admin"
        } else if (groups != null && groups.contains("tutorengruppe")) {
            "tutor"
        } else {
            "user"
        }

    /** Persistently store information account the user as a user account object. */
    fun createAccount(name: String, sub: String, groups: ArrayList<String>?) {
        accountRepository.save(Account(sub = sub, accountName = name, role = getRoleFromGroups(groups)))
    }
}
