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

plugins { id("tools.aqua.bgw.executable-conventions") }

mavenMetadata {
  name.set("BoardGameWork Network Protocol Client")
  description.set("A framework for board game applications.")
}

dependencies {
  implementation(project(":bgw-gui"))
  implementation(project(":bgw-net:bgw-net-common"))
  implementation(project(":bgw-net:bgw-net-client"))
}

application { mainClass.set("tools.aqua.bgw.net.protocol.client.main.MainKt") }
