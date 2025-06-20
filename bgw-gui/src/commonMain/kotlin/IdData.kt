/*
 * Copyright 2025 The BoardGameWork Authors
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

@file:Suppress("unused")

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable internal sealed class IDData

@Serializable
internal class IDSceneData : IDData() {
  var id: ID = ""
  var components: MutableList<IDComponentViewData> = mutableListOf()
}

@Serializable
internal abstract class IDComponentViewData : IDData() {
  var id: ID = ""
}

// LAYOUT VIEWS
@Serializable internal abstract class IDLayoutViewData : IDComponentViewData() {}

@Serializable
internal class IDPaneData : IDLayoutViewData() {
  var components: MutableList<IDComponentViewData> = mutableListOf()
}

@Serializable
internal class IDGridPaneData : IDLayoutViewData() {
  var grid: List<IDGridElementData> = emptyList()
}

@Serializable
@Polymorphic
internal class IDGridElementData(
    var column: Int,
    var row: Int,
    var component: IDComponentViewData?
)

@Serializable
internal class IDCameraPaneData : IDComponentViewData() {
  var target: IDLayoutViewData? = null
}

// GAME COMPONENT VIEWS
@Serializable internal open class IDGameComponentViewData : IDComponentViewData() {}

// CONTAINER
@Serializable
internal abstract class IDGameComponentContainerData : IDComponentViewData() {
  var components: MutableList<IDGameComponentViewData> = mutableListOf()
}

@Serializable internal class IDAreaData : IDGameComponentContainerData() {}

@Serializable internal class IDCardStackData : IDGameComponentContainerData() {}

@Serializable
internal class IDHexagonGridData : IDGameComponentContainerData() {
  var map: MutableMap<String, IDGameComponentViewData> = mutableMapOf()
}

@Serializable internal class IDLinearLayoutData : IDGameComponentContainerData() {}

@Serializable internal class IDSatchelData : IDGameComponentContainerData() {}
