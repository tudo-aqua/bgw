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

package tools.aqua.bgw.builder

import javafx.scene.layout.Pane
import javafx.scene.layout.Region
import tools.aqua.bgw.core.Scene
import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.container.*
import tools.aqua.bgw.elements.gameelements.GameElementView

/**
 * ContainerNodeBuilder.
 * Factory for all BGW containers.
 */
internal class ContainerNodeBuilder {
	companion object {
		/**
		 * Switches between Containers.
		 */
		internal fun buildContainer(
			scene: Scene<out ElementView>,
			containerView: GameElementContainerView<out GameElementView>
		): Region =
			when (containerView) {
				is AreaContainerView -> buildAreaContainer(containerView)
				is CardStackView -> buildCardStack(containerView)
				is GameElementPoolView -> buildGameElementPool(containerView)
				is LinearLayoutContainer -> buildLinearLayout(containerView)
			}.apply {
				containerView.observableElements.setGUIListenerAndInvoke {
					refresh(
						scene,
						containerView
					)
				}
			}
		
		/**
		 * Builds [AreaContainerView].
		 */
		@Suppress("UNUSED_PARAMETER")
		private fun buildAreaContainer(containerView: GameElementContainerView<*>): Pane = Pane()
		
		/**
		 * Builds [CardStackView].
		 */
		@Suppress("UNUSED_PARAMETER")
		private fun buildCardStack(containerView: GameElementContainerView<*>): Pane = Pane()
		
		/**
		 * Builds [GameElementPoolView].
		 */
		@Suppress("UNUSED_PARAMETER")
		private fun buildGameElementPool(containerView: GameElementContainerView<*>): Pane = Pane()
		
		/**
		 * Builds [LinearLayoutContainer].
		 */
		@Suppress("UNUSED_PARAMETER")
		private fun buildLinearLayout(containerView: GameElementContainerView<*>): Pane = Pane()
		
		/**
		 * Refreshes children in this container.
		 */
		private fun Pane.refresh(scene: Scene<out ElementView>, gameElementContainerView: GameElementContainerView<*>) {
			children.clear()
			gameElementContainerView.observableElements.forEach {
				children.add(NodeBuilder.build(scene, it))
			}
		}
	}
}