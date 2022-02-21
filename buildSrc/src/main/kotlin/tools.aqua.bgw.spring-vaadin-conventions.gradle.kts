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

import org.gradle.accessors.dm.LibrariesForLibs
import org.gradle.kotlin.dsl.extra
import org.gradle.kotlin.dsl.the

/*
 * Copyright 2021 The BoardGameWork Authors
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

plugins {
  id("tools.aqua.bgw.kotlin-conventions")
  id("tools.aqua.bgw.publish-conventions")

  kotlin("plugin.jpa")
  kotlin("plugin.spring")
  id("com.vaadin")
  id("io.spring.dependency-management")
  id("org.springframework.boot")
}

// black magic from https://github.com/gradle/gradle/issues/15383
val libs = the<LibrariesForLibs>()

extra["vaadinVersion"] = libs.versions.vaadin.get()

dependencyManagement { imports { mavenBom("com.vaadin:vaadin-bom:${libs.versions.vaadin.get()}") } }

dependencies {
  implementation(kotlin("reflect"))
  implementation("com.vaadin", "vaadin-spring-boot-starter")

  developmentOnly("org.springframework.boot", "spring-boot-devtools")

  testImplementation("org.springframework.boot", "spring-boot-starter-test")
}
