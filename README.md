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

Additionally, three examples are available for [MauMau](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-maumau-example), [Sudoku](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-sudoku-example) and [Tetris](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-tetris-example).

Visit the [Playground](https://tudo-aqua.github.io/bgw/playground) to see BGW in action and try out some of its features.

<!-- GETTING STARTED -->

## Getting Started

### Prerequisites

Before you can start using BGW, you will need a Java Development Kit (JDK) installed on your system. BGW requires at least
Java 11 to run. You can download a compatible version of [Azul Zulu OpenJDK][AzulZuluOpenJDK] from the official website.

### Setup

Start by adding the latest version of BGW as a dependency to your project.

#### Gradle
```gradle
implementation(group = "tools.aqua", name = "bgw-gui", version = "0.10")
```

#### Maven
```xml
<dependency>
    <groupId>tools.aqua</groupId>
    <artifactId>bgw-gui</artifactId>
    <version>0.10</version>
</dependency>
``` 

When running on JDK 16 or later, you need to add the following JVM arguments to your run configuration:

#### Gradle
```gradle
application {
    applicationDefaultJvmArgs = listOf(
        "--add-opens", "java.desktop/sun.awt=ALL-UNNAMED",
        "--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED",
        "--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED",
        "--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED"
    )
}
```

#### Maven
```xml
<configuration>
    <jvmArguments>
        --add-opens java.desktop/sun.awt=ALL-UNNAMED
        --add-opens java.desktop/java.awt.peer=ALL-UNNAMED
        --add-opens java.desktop/sun.lwawt=ALL-UNNAMED
        --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED
    </jvmArguments>
</configuration>
```

It is therefore recommended to explicitly specify the correct JVM target (e.g. 11 in this case) in your build system:

#### Gradle
```gradle
compileKotlin {
    kotlinOptions.jvmTarget = "11"
}
```

#### Maven
```xml
<configuration>
    <jvmTarget>11</jvmTarget>
</configuration>
```

The basic setup should now be complete. To learn more about creating your first board game application using BGW, continue with the [Getting Started][GettingStarted] section or visit the [Playground](https://tudo-aqua.github.io/bgw/playground).