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

plugins { id("tools.aqua.bgw.spring-vaadin-conventions") }

mavenMetadata {
  name.set("BoardGameWork Server")
  description.set("A framework for board game applications.")
}

repositories {
  mavenCentral()
  maven {
    url = uri("https://maven.vaadin.com/vaadin-addons")
  }
}

dependencies {
  implementation(project(":bgw-net:bgw-net-common"))

  implementation("de.f0rce:ace:3.4.2")

  implementation(libs.kotlinx.serialization.json)
  implementation(libs.spring.boot.devtools)

  // json kotlin schema
  implementation(libs.jackson.kotlin)
  implementation(libs.jsonSchemaValidator)

  implementation(libs.spring.boot.jpa)
  implementation(libs.hibernateTypes)
  runtimeOnly(libs.postgreSQL.jdbc)

  implementation(libs.spring.boot.websocket)
  implementation(libs.spring.boot.oauth2.client)
  implementation(libs.ilay)

  // Integration testing
  testImplementation(libs.h2database.h2)
  testImplementation(libs.testcontainers.junit.jupiter)
}

vaadin { productionMode = false }

tasks.bootBuildImage {
  imageName = System.getenv("DOCKER_IMAGE_NAME")
  environment = mapOf("BP_JVM_VERSION" to "11.*")
  isPublish = true
  docker {
    publishRegistry {
      username = System.getenv("DOCKER_REGISTRY_USER")
      password = System.getenv("DOCKER_REGISTRY_PASSWORD")
      url = System.getenv("DOCKER_REGISTRY_URL")
    }
  }
}
