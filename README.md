[BGW]: https://github.com/tudo-aqua/bgw
[AzulZuluOpenJDK]: https://www.azul.com/downloads/?version=java-11-lts&package=jdk-fx#download-openjdk
[Kotlin]: https://kotlinlang.org/
[GettingStarted]: https://tudo-aqua.github.io/bgw/guides/getting-started

# BoardGameWork
<!--[![Code Style](https://github.com/tudo-aqua/bgw-core/actions/workflows/code-style.yml/badge.svg)](https://github.com/tudo-aqua/bgw/security/code-scanning)-->
[![Build](https://github.com/tudo-aqua/bgw-core/actions/workflows/analyze-build-deploy.yml/badge.svg)](https://github.com/tudo-aqua/bgw-core/actions)
[![GitHub-Pages](https://github.com/tudo-aqua/bgw-core/actions/workflows/github-pages.yml/badge.svg)](https://tudo-aqua.github.io/bgw/)

[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-gui?label=MavenCentral%20bgw-gui&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-gui)
[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-gui?label=MavenCentral%20bgw-net-common&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-net-common)
[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-gui?label=MavenCentral%20bgw-net-client&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-net-client)
[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-gui?label=MavenCentral%20bgw-net-server&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-net-server)
------------

[BoardGameWork (BGW)][BGW] is a framework for creating 2D board game applications using [Kotlin][Kotlin].

Start with BGW using the dedicated [Getting Started](https://tudo-aqua.github.io/bgw/guides/getting-started) guide, take a look at the complete [API Reference](https://tudo-aqua.github.io/bgw/docs/) or learn more concepts in the [Advanced Usage](https://tudo-aqua.github.io/bgw/guides/concepts/advanced-usage) section.

Complete example games, such as [Collapsi](https://github.com/tudo-aqua/bgw-example-collapsi), are maintained in [separate repositories](https://github.com/orgs/tudo-aqua/repositories?q=bgw-example).

Visit the [Playground](https://tudo-aqua.github.io/bgw/playground) to see BGW in action and try out some of its features.

<!-- GETTING STARTED -->

## Getting Started

### Prerequisites

Before you can start using BGW, you will need a Java Development Kit (JDK) installed on your system. BGW requires at least
Java 11 to run. You can download a compatible version of [Azul Zulu OpenJDK][AzulZuluOpenJDK] from the official website.

### Setup

#### Gradle

The BGW Gradle plugin adds BGW as a dependency and passes the JVM arguments required by its browser runtime to the
`run` task, to tests and to the start scripts of the `application` plugin. The plugin is published on Maven Central, so
add it to the plugin repositories in your `settings.gradle.kts`:

```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
```

Then apply it in your `build.gradle.kts`:

```kotlin
plugins {
    kotlin("jvm") version "2.3.0"
    application
    id("tools.aqua.bgw") version "0.11.1"
}

repositories {
    mavenCentral()
}

kotlin {
    jvmToolchain(11)
}

application {
    mainClass = "MainKt"
}
```

The plugin uses the BGW version matching its own version. Use the `bgw` block to choose another version or to add the
network modules:

```kotlin
bgw {
    version = "0.11.1"
    network = true // adds bgw-net-client and bgw-net-common
}
```

#### Maven

```xml
<dependency>
    <groupId>tools.aqua</groupId>
    <artifactId>bgw-gui</artifactId>
    <version>0.11.1</version>
</dependency>
```

Add the following JVM arguments to your run configuration. Leave out `--enable-native-access` on Java 11, which does not
accept it, and the two `sun.lwawt` arguments on Windows and Linux.

```xml
<configuration>
    <jvmArguments>
        --add-opens java.desktop/sun.awt=ALL-UNNAMED
        --add-opens java.desktop/java.awt.peer=ALL-UNNAMED
        --add-opens java.desktop/java.awt=ALL-UNNAMED
        --add-opens java.desktop/sun.lwawt=ALL-UNNAMED
        --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED
        --enable-native-access ALL-UNNAMED
    </jvmArguments>
</configuration>
```

When starting the application from your IDE instead of Gradle or Maven, add the same arguments to the _VM options_ of
the run configuration.

The basic setup should now be complete. To learn more about creating your first board game application using BGW, continue with the [Getting Started][GettingStarted] section or visit the [Playground](https://tudo-aqua.github.io/bgw/playground).