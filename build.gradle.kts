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

import tools.aqua.APACHE_2
import tools.aqua.GlobalMavenMetadataExtension.Developer
import tools.aqua.GlobalMavenMetadataExtension.GithubProject

plugins { id("tools.aqua.bgw.root-conventions") }

group = "tools.aqua"

mavenMetadata {
  developers.addAll(
      Developer("Simon Dierl", "simon.dierl@tu-dortmund.de"),
      Developer("Stefan Naujokat", "stefan.naujokat@tu-dortmund.de"),
      Developer("Till Schallau", "till.schallau@tu-dortmund.de"),
      Developer("Amin Bouzerda", "amin.bouzerda@tu-dortmund.de"),
      Developer("Dominik Mäckel", "dominik.maeckel@tu-dortmund.de"),
      Developer("Fabian Klümpers", "fabian.kluempers@tu-dortmund.de"),
  )
  githubProject.set(GithubProject("tudo-aqua", "bgw"))
  licenses.addAll(APACHE_2)
}

koverMerged {
  filters { projects { excludes += listOf(":bgw-docs", ":bgw-examples", ":bgw-net") } }
}
