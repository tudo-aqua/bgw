# BoardGameWork
<!--[![Code Style](https://github.com/tudo-aqua/bgw-core/actions/workflows/code-style.yml/badge.svg)](https://github.com/tudo-aqua/bgw/security/code-scanning)-->
[![Build](https://github.com/tudo-aqua/bgw-core/actions/workflows/analyze-build-deploy.yml/badge.svg)](https://github.com/tudo-aqua/bgw-core/actions)
[![GitHub-Pages](https://github.com/tudo-aqua/bgw-core/actions/workflows/github-pages.yml/badge.svg)](https://tudo-aqua.github.io/bgw/)

[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-gui?label=MavenCentral%20bgw-gui&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-gui)
[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-gui?label=MavenCentral%20bgw-net-common&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-net-common)
[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-gui?label=MavenCentral%20bgw-net-client&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-net-client)
[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-gui?label=MavenCentral%20bgw-net-server&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-net-server)

[![KDocs BGW-Gui](https://img.shields.io/static/v1?label=kDoc%20bgw-gui&message=overview&color=blue)](https://tudo-aqua.github.io/bgw/bgw-gui-kdoc/index.html)
[![KDocs BGW-Gui](https://img.shields.io/static/v1?label=kDoc%20bgw-net-common&message=overview&color=blue)](https://tudo-aqua.github.io/bgw/bgw-net-common-kdoc/index.html)
[![KDocs BGW-Gui](https://img.shields.io/static/v1?label=kDoc%20bgw-net-client&message=overview&color=blue)](https://tudo-aqua.github.io/bgw/bgw-net-client-kdoc/index.html)
------------

BoardGameWork is a framework for creating 2D board game applications.

Read on [how to get started](https://tudo-aqua.github.io/bgw/), or take a look at the complete API documentation for [bgw-gui](https://tudo-aqua.github.io/bgw/bgw-gui-kdoc/index.html), [bgw-net-common](https://tudo-aqua.github.io/bgw/bgw-net-common-kdoc/index.html), or [bgw-net-client](https://tudo-aqua.github.io/bgw/bgw-net-client-kdoc/index.html).

Examples are available for [MauMau](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-maumau-example), [Sudoku](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-sudoku-example) and [Tetris](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-tetris-example).

<!-- GETTING STARTED -->

## Getting Started


### Prerequisites

<!-- https://www.azul.com/downloads/?version=java-11-lts&package=jdk-fx#download-openjdk -->

BoardGameWork is built on top of [JavaFX 17](https://openjfx.io/openjfx-docs/) and therefore requires at least JDK 11. Since JavaFX was decoupled from the JavaJDK as of JDK 11, BoardGameWork comes with JavaFX dependencies including their native libraries for various platforms like Windows, Linux and Mac.

We recommend installing a JDK build that already includes JavaFX to ensure your platform is supported. [Azul Zulu Builds of OpenJDK](https://www.azul.com/downloads/?version=java-11-lts&package=jdk-fx#download-openjdk) support a wide range of platforms and architectures including [Mac M1](https://www.azul.com/downloads/?version=java-11-lts&os=macos&architecture=arm-64-bit&package=jdk-fx#download-openjdk).

### Setup

Start by adding the latest version of BGW as a dependency to your project.

#### Gradle
```gradle
implementation("tools.aqua:bgw-gui:0.8")
```

#### Maven
```xml
<dependency>
  <groupId>tools.aqua</groupId>
  <artifactId>bgw-gui</artifactId>
  <version>0.8</version>
</dependency>
``` 

You also need to specify the correct JVM target in your build system.

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
