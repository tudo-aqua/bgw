/*
 * Copyright 2022-2025 The BoardGameWork Authors
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

import com.vaadin.flow.server.HandlerHelper
import com.vaadin.flow.shared.ApplicationConstants
import javax.servlet.http.HttpServletRequest
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.web.util.matcher.RequestMatcher
import tools.aqua.bgw.net.common.SERVER_ENDPOINT
import tools.aqua.bgw.net.server.LOGOUT_SUCCESS_URL

/**
 * SecurityConfiguration.
 *
 * Configures spring security, doing the following:
 * - Bypass security checks for static resources,
 * - Restrict access to the application, allowing only logged-in users,
 * - Set up the login form
 */
@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    /** Repository holding information about the oauth login accounts. */
    var accountRepository: AccountRepository
) : WebSecurityConfigurerAdapter() {
  /** Restrict access to the application, allowing only logged-in users. */
  override fun configure(http: HttpSecurity) {
    http.httpBasic().disable()
    // Not using Spring CSRF here to be able to use plain HTML for the login page
    http.csrf().disable()
    http.authorizeRequests().apply {
      requestMatchers(RequestMatcher(::isFrameworkInternalRequest))
          .permitAll()
          .anyRequest()
          .authenticated()
    }
    http.oauth2Login { it.successHandler(CustomSuccessHandler(accountRepository)) }
    http.logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL)
  }

  /** Allows access to static resources, bypassing Spring security. */
  override fun configure(web: WebSecurity) {
    web.ignoring()
        .antMatchers(
            "/$SERVER_ENDPOINT", // The server endpoint route
            "/VAADIN/**", // Vaadin Flow static resources
            "/favicon.ico", // the standard favicon URI
            "/robots.txt", // the robots.txt exclusion standard
            "/manifest.webmanifest",
            "/sw.js",
            "/offline-page.html", // web application manifest
            "/icons/**",
            "/images/**", // icons and images
            "/frontend/**", // (development mode) static resources
            "/webjars/**", // (development mode) webjars
            "/h2-console/**", // (development mode) H2 debugging console
            "/frontend-es5/**",
            "/frontend-es6/**", // (production mode) static resources
        )
  }

  /**
   * Tests if the request is an internal framework request. The test consists of checking if the
   * request parameter is present and if its value is consistent with any of the request types know.
   *
   * @param request the request under examination.
   * @return true iff [request] is an internal framework request.
   */
  private fun isFrameworkInternalRequest(request: HttpServletRequest): Boolean {
    val parameterValue =
        request.getParameter(ApplicationConstants.REQUEST_TYPE_PARAMETER) ?: return false

    return HandlerHelper.RequestType.values().any { it.identifier == parameterValue }
  }
}
