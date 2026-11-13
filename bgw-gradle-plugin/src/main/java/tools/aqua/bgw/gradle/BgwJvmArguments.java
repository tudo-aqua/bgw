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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Input;
import org.gradle.process.CommandLineArgumentProvider;

/**
 * Provides the JVM arguments required by the browser runtime of BoardGameWork for a JVM of the
 * given Java version that runs on the current operating system.
 */
public final class BgwJvmArguments implements CommandLineArgumentProvider {

  /** Opens the AWT internals accessed by the browser runtime on every operating system. */
  static final List<String> ADD_OPENS =
      List.of(
          "--add-opens=java.desktop/sun.awt=ALL-UNNAMED",
          "--add-opens=java.desktop/java.awt.peer=ALL-UNNAMED",
          "--add-opens=java.desktop/java.awt=ALL-UNNAMED");

  /** Opens the AWT internals accessed by the browser runtime on macOS, where they exist. */
  static final List<String> ADD_OPENS_MACOS =
      List.of(
          "--add-opens=java.desktop/sun.lwawt=ALL-UNNAMED",
          "--add-opens=java.desktop/sun.lwawt.macosx=ALL-UNNAMED");

  /** Allows the browser runtime to load its native libraries. */
  static final String ENABLE_NATIVE_ACCESS = "--enable-native-access=ALL-UNNAMED";

  /** The first Java version that accepts {@link #ENABLE_NATIVE_ACCESS}. */
  static final int ENABLE_NATIVE_ACCESS_SINCE = 17;

  private final Provider<Integer> javaVersion;

  private final boolean macOs;

  /**
   * Creates the arguments for the JVM of the given Java version on the current operating system.
   *
   * @param javaVersion the feature version of the JVM the arguments are passed to.
   */
  public BgwJvmArguments(Provider<Integer> javaVersion) {
    this.javaVersion = javaVersion;
    this.macOs = System.getProperty("os.name", "").toLowerCase(Locale.ROOT).startsWith("mac");
  }

  /**
   * The feature version of the JVM the arguments are passed to.
   *
   * @return the Java version.
   */
  @Input
  public Provider<Integer> getJavaVersion() {
    return javaVersion;
  }

  /**
   * Whether the JVM runs on macOS.
   *
   * @return {@code true} on macOS.
   */
  @Input
  public boolean isMacOs() {
    return macOs;
  }

  @Override
  public Iterable<String> asArguments() {
    return forPlatform(javaVersion.get(), macOs);
  }

  /**
   * Returns the arguments that open the AWT internals on every operating system, for JVMs whose
   * operating system is unknown, for example the ones started by start scripts.
   *
   * @return the JVM arguments.
   */
  static List<String> forAnyPlatform() {
    List<String> arguments = new ArrayList<>(ADD_OPENS);
    arguments.addAll(ADD_OPENS_MACOS);
    return arguments;
  }

  /**
   * Returns the JVM arguments for a JVM of the given Java version.
   *
   * @param javaVersion the feature version of the JVM.
   * @param macOs whether the JVM runs on macOS.
   * @return the JVM arguments.
   */
  static List<String> forPlatform(int javaVersion, boolean macOs) {
    List<String> arguments = new ArrayList<>(ADD_OPENS);
    if (macOs) {
      arguments.addAll(ADD_OPENS_MACOS);
    }
    if (javaVersion >= ENABLE_NATIVE_ACCESS_SINCE) {
      arguments.add(ENABLE_NATIVE_ACCESS);
    }
    return arguments;
  }
}
