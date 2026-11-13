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

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.gradle.api.JavaVersion;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.dsl.DependencyHandler;
import org.gradle.api.provider.Property;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.JavaExec;
import org.gradle.api.tasks.application.CreateStartScripts;
import org.gradle.api.tasks.testing.Test;
import org.gradle.jvm.toolchain.JavaLauncher;

/**
 * Sets up a project that uses BoardGameWork.
 *
 * <p>The plugin adds the BoardGameWork dependencies to the {@code implementation} configuration and
 * passes the JVM arguments required by the browser runtime to all {@link JavaExec} and {@link Test}
 * tasks and to the start scripts of the {@code application} plugin.
 */
public class BgwPlugin implements Plugin<Project> {

  /** The Maven group of all BoardGameWork artifacts. */
  static final String GROUP = "tools.aqua";

  @Override
  public void apply(Project project) {
    BgwExtension extension = project.getExtensions().create("bgw", BgwExtension.class);
    extension.getVersion().convention(pluginVersion());
    extension.getNetwork().convention(false);

    project.getPluginManager().withPlugin("java", plugin -> addDependencies(project, extension));

    project
        .getTasks()
        .withType(JavaExec.class)
        .configureEach(
            task ->
                task.getJvmArgumentProviders()
                    .add(new BgwJvmArguments(javaVersion(project, task.getJavaLauncher()))));
    project
        .getTasks()
        .withType(Test.class)
        .configureEach(
            task ->
                task.getJvmArgumentProviders()
                    .add(new BgwJvmArguments(javaVersion(project, task.getJavaLauncher()))));

    // The Java version and operating system that run the start scripts are unknown, so only
    // arguments accepted by every Java version are added, for every operating system. They are
    // appended after the build script was evaluated to keep the
    // arguments configured with applicationDefaultJvmArgs.
    project
        .getPluginManager()
        .withPlugin(
            "application",
            plugin ->
                project.afterEvaluate(
                    evaluated ->
                        evaluated
                            .getTasks()
                            .withType(CreateStartScripts.class)
                            .configureEach(BgwPlugin::addStartScriptArguments)));
  }

  private static void addDependencies(Project project, BgwExtension extension) {
    DependencyHandler dependencies = project.getDependencies();
    Provider<String> version = extension.getVersion();
    project
        .getConfigurations()
        .named("implementation")
        .configure(
            configuration -> {
              configuration
                  .getDependencies()
                  .addLater(version.map(it -> dependencies.create(GROUP + ":bgw-gui:" + it)));
              configuration
                  .getDependencies()
                  .addAllLater(
                      extension
                          .getNetwork()
                          .zip(
                              version,
                              (network, it) ->
                                  network
                                      ? List.of(
                                          dependencies.create(GROUP + ":bgw-net-client:" + it),
                                          dependencies.create(GROUP + ":bgw-net-common:" + it))
                                      : List.<Dependency>of()));
            });
  }

  private static void addStartScriptArguments(CreateStartScripts task) {
    List<String> options = new ArrayList<>();
    task.getDefaultJvmOpts().forEach(options::add);
    options.addAll(BgwJvmArguments.forAnyPlatform());
    task.setDefaultJvmOpts(options);
  }

  private static Provider<Integer> javaVersion(
      Project project, Property<JavaLauncher> javaLauncher) {
    return javaLauncher
        .map(launcher -> launcher.getMetadata().getLanguageVersion().asInt())
        .orElse(project.provider(() -> Integer.parseInt(JavaVersion.current().getMajorVersion())));
  }

  /**
   * Returns the version of this plugin, which is also the default BoardGameWork version.
   *
   * @return the plugin version.
   */
  static String pluginVersion() {
    Properties properties = new Properties();
    try (InputStream stream = BgwPlugin.class.getResourceAsStream("bgw-gradle-plugin.properties")) {
      if (stream == null) {
        throw new IllegalStateException("The version of the BoardGameWork plugin is missing.");
      }
      properties.load(stream);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
    return properties.getProperty("version");
  }
}
