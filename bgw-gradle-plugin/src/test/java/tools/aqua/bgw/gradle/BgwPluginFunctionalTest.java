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

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.gradle.testkit.runner.GradleRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BgwPluginFunctionalTest {

  private static final String PRINT_TASK =
      String.join(
          "\n",
          "tasks.register('printBgw') {",
          "  def dependencyList = provider {",
          "    configurations.implementation.allDependencies.collect {",
          "      it.group + ':' + it.name + ':' + it.version",
          "    }.join(',')",
          "  }",
          "  def runArguments = provider {",
          "    (tasks.run.jvmArguments.get()",
          "        + tasks.run.jvmArgumentProviders.collectMany { it.asArguments() }).join(',')",
          "  }",
          "  def startScriptOptions = provider { tasks.startScripts.defaultJvmOpts.join(',') }",
          "  def runJavaVersion = provider {",
          "    tasks.run.javaLauncher.get().metadata.languageVersion.asInt()",
          "  }",
          "  doLast {",
          "    println 'DEPENDENCIES=' + dependencyList.get()",
          "    println 'RUN_ARGUMENTS=' + runArguments.get()",
          "    println 'RUN_JAVA_VERSION=' + runJavaVersion.get()",
          "    println 'START_SCRIPT_OPTIONS=' + startScriptOptions.get()",
          "  }",
          "}");

  @TempDir Path projectDir;

  @Test
  void addsBgwGuiAndJvmArguments() throws IOException {
    writeBuild(
        "plugins { id 'java'; id 'application'; id 'tools.aqua.bgw' }",
        "application {",
        "  mainClass = 'Main'",
        "  applicationDefaultJvmArgs = ['-Dcustom=true']",
        "}",
        PRINT_TASK);

    String output = run("printBgw");

    assertThat(value(output, "DEPENDENCIES"))
        .isEqualTo(BgwPlugin.GROUP + ":bgw-gui:" + BgwPlugin.pluginVersion());

    int javaVersion = Integer.parseInt(value(output, "RUN_JAVA_VERSION"));
    assertThat(Arrays.asList(value(output, "RUN_ARGUMENTS").split(",")))
        .contains("-Dcustom=true")
        .containsAll(BgwJvmArguments.forPlatform(javaVersion, isMacOs()));

    assertThat(Arrays.asList(value(output, "START_SCRIPT_OPTIONS").split(",")))
        .containsExactlyElementsOf(
            Stream.concat(Stream.of("-Dcustom=true"), BgwJvmArguments.forAnyPlatform().stream())
                .collect(Collectors.toList()));
  }

  @Test
  void usesTheConfiguredVersionAndNetworkModules() throws IOException {
    writeBuild(
        "plugins { id 'java'; id 'application'; id 'tools.aqua.bgw' }",
        "application { mainClass = 'Main' }",
        "bgw {",
        "  version = '1.2.3'",
        "  network = true",
        "}",
        PRINT_TASK);

    String output = run("printBgw");

    assertThat(value(output, "DEPENDENCIES").split(","))
        .containsExactlyInAnyOrder(
            "tools.aqua:bgw-gui:1.2.3",
            "tools.aqua:bgw-net-client:1.2.3",
            "tools.aqua:bgw-net-common:1.2.3");
  }

  private static boolean isMacOs() {
    return System.getProperty("os.name").toLowerCase(Locale.ROOT).startsWith("mac");
  }

  private void writeBuild(String... lines) throws IOException {
    Files.writeString(projectDir.resolve("settings.gradle"), "");
    Files.write(projectDir.resolve("build.gradle"), List.of(lines), StandardCharsets.UTF_8);
  }

  private String run(String... arguments) {
    return GradleRunner.create()
        .withProjectDir(projectDir.toFile())
        .withPluginClasspath()
        .withArguments(arguments)
        .build()
        .getOutput();
  }

  private static String value(String output, String key) {
    return output
        .lines()
        .filter(line -> line.startsWith(key + "="))
        .map(line -> line.substring(key.length() + 1))
        .findFirst()
        .orElseThrow(() -> new AssertionError(key + " missing in output:\n" + output));
  }
}
