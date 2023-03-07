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

import kotlin.annotation.AnnotationRetention.RUNTIME
import org.ilay.NavigationAnnotation

/** Checks whether a given user account satisfies the provided role. */
@NavigationAnnotation(RoleBasedEvaluator::class)
@Retention(RUNTIME)
annotation class SecuredByRole(
    /** Space separated string of roles the user has to statisfy. */
    vararg val value: String
)
