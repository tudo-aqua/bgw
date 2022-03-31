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

package tools.aqua.bgw.net.server

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import tools.aqua.bgw.net.server.service.oauth.SecurityConfiguration

/** Spring Boot application test */
@SpringBootTest
internal class SpringBootApplicationTests {
  /**
   * Test the initialization of the application and all configurations e.g. [SecurityConfiguration]
   */
  @Test @DisplayName("Testing the initialization of the application") fun contextLoads() {}
}
