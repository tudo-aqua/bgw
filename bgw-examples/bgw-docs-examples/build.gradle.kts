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

plugins { id("tools.aqua.bgw.executable-conventions") }

mavenMetadata {
    name.set("BoardGameWork Docs Examples")
    description.set("The BGW documentation examples.")
}

dependencies {
    implementation(project(":bgw-gui", configuration = "jvmRuntimeElements"))
    implementation(project(":bgw-net:bgw-net-client"))
    implementation(project(":bgw-net:bgw-net-common"))
}
