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

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class BgwJvmArgumentsTest {

  @Test
  void java11OnlyOpensTheAwtInternals() {
    assertThat(BgwJvmArguments.forPlatform(11, false))
        .containsExactlyElementsOf(BgwJvmArguments.ADD_OPENS);
  }

  @Test
  void java17AndLaterAlsoEnableNativeAccess() {
    for (int version : new int[] {17, 21, 25}) {
      assertThat(BgwJvmArguments.forPlatform(version, false))
          .containsAll(BgwJvmArguments.ADD_OPENS)
          .contains(BgwJvmArguments.ENABLE_NATIVE_ACCESS)
          .hasSize(BgwJvmArguments.ADD_OPENS.size() + 1);
    }
  }

  @Test
  void macOsAlsoOpensTheMacOsInternals() {
    assertThat(BgwJvmArguments.forPlatform(11, true))
        .containsAll(BgwJvmArguments.ADD_OPENS)
        .containsAll(BgwJvmArguments.ADD_OPENS_MACOS)
        .doesNotContain(BgwJvmArguments.ENABLE_NATIVE_ACCESS);
  }

  @Test
  void startScriptsOpenTheInternalsOfEveryPlatform() {
    assertThat(BgwJvmArguments.forAnyPlatform())
        .containsAll(BgwJvmArguments.ADD_OPENS)
        .containsAll(BgwJvmArguments.ADD_OPENS_MACOS)
        .doesNotContain(BgwJvmArguments.ENABLE_NATIVE_ACCESS);
  }
}
