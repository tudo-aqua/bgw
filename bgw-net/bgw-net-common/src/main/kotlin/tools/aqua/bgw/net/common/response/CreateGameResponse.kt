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

package tools.aqua.bgw.net.common.response

/**
 * Response upon [tools.aqua.bgw.net.common.request.CreateGameMessage].
 *
 * @property status Status code.
 * @property sessionID Session ID for this game. ``null`` if creation was not successful.
 */
class CreateGameResponse(val status: CreateGameResponseStatus, val sessionID: String?) :
    Response() {
  constructor(
      status: Pair<CreateGameResponseStatus, String?>
  ) : this(status = status.first, sessionID = status.second)
}
