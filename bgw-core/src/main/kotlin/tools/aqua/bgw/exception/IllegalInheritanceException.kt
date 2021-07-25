/*
 *    Copyright 2021 The BoardGameWork Authors
 *    SPDX-License-Identifier: Apache-2.0
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

@file:Suppress("unused")

package tools.aqua.bgw.exception

/**
 * A [RuntimeException] that is thrown when trying to inherit from a top-level framework class which is prohibited.
 */
internal class IllegalInheritanceException(inheritance: Any, supertype: Class<*>) :
	RuntimeException(
		"Illegal direct inheritance of "
				+ inheritance::class.java.name
				+ " from base type "
				+ supertype.name
				+ " has no legal rendering."
	)
