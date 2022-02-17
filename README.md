# BoardGameWork

[![Maven Central](https://img.shields.io/maven-central/v/tools.aqua/bgw-core?label=MavenCentral&logo=apache-maven)](https://search.maven.org/artifact/tools.aqua/bgw-core)
[![Build](https://github.com/tudo-aqua/bgw-core/actions/workflows/build.yml/badge.svg)](https://github.com/tudo-aqua/bgw-core/actions/workflows/build.yml)
[![Test](https://github.com/tudo-aqua/bgw-core/actions/workflows/test.yml/badge.svg)](https://github.com/tudo-aqua/bgw-core/actions/workflows/test.yml)
[![GitHub-Pages](https://github.com/tudo-aqua/bgw-core/actions/workflows/github-pages.yml/badge.svg)](https://tudo-aqua.github.io/bgw/)
[![KDocs](https://img.shields.io/badge/KDoc-Overview-yellowgreen)](https://tudo-aqua.github.io/bgw/kotlin-docs/)
[![Detekt-Pages](https://img.shields.io/badge/Detekt-Report-yellowgreen)](https://tudo-aqua.github.io/bgw/detekt)

------------

BoardGameWork is a framework for creating 2D board game applications.

Read on [how to get started](https://tudo-aqua.github.io/bgw/), or take a look at the complete [API documentation](https://tudo-aqua.github.io/bgw/kotlin-docs/).

Examples are available for [MauMau](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-maumau-example), [Sudoku](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-sudoku-example) and [Tetris](https://github.com/tudo-aqua/bgw/tree/main/bgw-examples/bgw-tetris-example).

<!-- GETTING STARTED -->

## Getting Started


### Prerequisites

<!-- https://www.azul.com/downloads/?version=java-11-lts&package=jdk-fx#download-openjdk -->

BoardGameWork is built on top of [JavaFX 17](https://openjfx.io/openjfx-docs/) and therefore requires at least JDK 11. Since JavaFX was decoupled from the JavaJDK as of JDK 11, BoardGameWork comes with JavaFX dependencies including their native libraries for various platforms like Windows, Linux and Mac.

Tho we recommend installing a JDK Build that already includes JavaFX to ensure your platform is supported. [Azul Zulu Builds of OpenJDK](https://www.azul.com/downloads/?version=java-11-lts&package=jdk-fx#download-openjdk) support a wide range of platforms and architectures including [Mac M1](https://www.azul.com/downloads/?version=java-11-lts&os=macos&architecture=arm-64-bit&package=jdk-fx#download-openjdk).

### Setup

Start by adding the latest version of BGW as a dependency to your project.

####Gradle
```gradle
implementation("tools.aqua:bgw-core:0.5")
```

####Maven
```xml
<dependency>
  <groupId>tools.aqua</groupId>
  <artifactId>bgw-core</artifactId>
  <version>0.5</version>
</dependency>
``` 

You also need to specify the correct JVM target in your build system.

####Gradle
```gradle
compileKotlin {
    kotlinOptions.jvmTarget= "11"
}
```

####Maven
```xml
<configuration>
    <jvmTarget>11</jvmTarget>
</configuration>
```