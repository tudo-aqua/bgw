/*
 * Copyright 2021-2026 The BoardGameWork Authors
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

import org.gradle.kotlin.dsl.application

plugins {
  id("tools.aqua.bgw.kotlin-conventions")

  application
}

// The JVM arguments required by the browser runtime of bgw-gui. sun.lwawt only exists on macOS and
// Java 11 does not accept --enable-native-access.
val addOpens =
    listOf(
        "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
        "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED",
        "--add-opens=java.desktop/java.awt=ALL-UNNAMED")
val addOpensMacOs =
    listOf(
        "--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED",
        "--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
val isMacOs = System.getProperty("os.name").lowercase().startsWith("mac")

tasks.withType<JavaExec>().configureEach {
  val javaVersion = javaLauncher.map { it.metadata.languageVersion.asInt() }
  jvmArgumentProviders.add(
      CommandLineArgumentProvider {
        addOpens +
            (if (isMacOs) addOpensMacOs else emptyList()) +
            (if (javaVersion.get() >= 17) listOf("--enable-native-access=ALL-UNNAMED")
            else emptyList())
      })
}

tasks.startScripts { defaultJvmOpts = addOpens + addOpensMacOs }
