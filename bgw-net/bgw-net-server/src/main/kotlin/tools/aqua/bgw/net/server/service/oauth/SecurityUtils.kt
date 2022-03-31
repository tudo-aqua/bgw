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

package tools.aqua.bgw.net.server.service.oauth

import com.vaadin.flow.server.HandlerHelper
import com.vaadin.flow.shared.ApplicationConstants.REQUEST_TYPE_PARAMETER
import javax.servlet.http.HttpServletRequest

/**
 * Tests if the request is an internal framework request. The test consists of checking if the
 * request parameter is present and if its value is consistent with any of the request types know.
 *
 * @param request the request under examination.
 * @return true if [request] is an internal framework request.
 */
fun isFrameworkInternalRequest(request: HttpServletRequest): Boolean {
  val parameterValue = request.getParameter(REQUEST_TYPE_PARAMETER)
  return parameterValue != null &&
      HandlerHelper.RequestType.values().any { it.identifier == parameterValue }
}
