/*
 * Copyright 2021-2022 The BoardGameWork Authors
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

package tools.aqua

import org.gradle.api.publish.maven.MavenPom
import org.gradle.api.publish.maven.MavenPomDeveloperSpec
import org.gradle.api.publish.maven.MavenPomLicenseSpec
import tools.aqua.GlobalMavenMetadataExtension.License

/**
 * Add metadata for a GitHub project. This includes URL and SCM connection data.
 * @param organization the repository's organization.
 * @param project the project name.
 * @param mainBranch the main branch, used for URL generation. Defaults to `main`.
 */
fun MavenPom.github(organization: String, project: String, mainBranch: String = "main") {
  url.set("https://github.com/$organization/$project")
  scm {
    connection.set("scm:git:git://github.com:$organization/$project.git")
    developerConnection.set("scm:git:ssh://git@github.com:$organization/$project.git")
    url.set("https://github.com/$organization/$project/tree/$mainBranch")
  }
}

/**
 * Add a developer using a succinct notation.
 * @param developerName the developer's name.
 * @param mailAddress the email address.
 */
fun MavenPomDeveloperSpec.developer(developerName: String, mailAddress: String) = developer {
  name.set(developerName)
  email.set(mailAddress)
}

/**
 * Add a license using a succinct notation.
 * @param licenseName the license's name.
 * @param licenseUrl a URL for the license.
 */
fun MavenPomLicenseSpec.license(licenseName: String, licenseUrl: String) = license {
  name.set(licenseName)
  url.set(licenseUrl)
}

/** The Apache 2 license. */
val APACHE_2 = License("Apache License, Version 2.0", "https://opensource.org/licenses/Apache-2.0")
