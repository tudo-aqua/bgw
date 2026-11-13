/*
 * Copyright 2026 The BoardGameWork Authors
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

package tools.aqua.bgw.gradle;

import org.gradle.api.provider.Property;

/** Configures the BoardGameWork dependencies added by the {@link BgwPlugin}. */
public abstract class BgwExtension {

  /**
   * The BoardGameWork version to depend on. Defaults to the version of the plugin.
   *
   * @return the version property.
   */
  public abstract Property<String> getVersion();

  /**
   * Whether the network modules {@code bgw-net-client} and {@code bgw-net-common} are added in
   * addition to {@code bgw-gui}. Defaults to {@code false}.
   *
   * @return the network property.
   */
  public abstract Property<Boolean> getNetwork();
}
