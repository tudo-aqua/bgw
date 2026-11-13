[BGW]: https://github.com/tudo-aqua/bgw
[AzulZuluOpenJDK]: https://www.azul.com/downloads/?version=java-11-lts&package=jdk#zulu
[Kotlin]: https://kotlinlang.org/
[MavenCentral]: https://central.sonatype.com/artifact/tools.aqua/bgw-gui
[JCEF]: https://github.com/chromiumembedded/java-cef

<!-- GH-Pages Doc -->

[GettingStarted]: /guides/getting-started
[Playground]: /playground
[NetworkDoc]: /guides/network/communication

# Welcome to BoardGameWork

<tldr>Set up your project and run it for the first time</tldr>

[BoardGameWork (BGW)][BGW] is a framework for creating 2D board game applications using [Kotlin][Kotlin]. You describe your game with scenes, components and event handlers, BGW takes care of rendering, scaling, animations and user input.

These guides walk you through the essentials. Over the next pages we build **Dice Duel**, a tiny two-player dice game with a menu, a game board, animated dice and a score. Along the way, every concept is shown with a live preview that is rendered by BGW itself.

<preview key="tools.aqua.bgw.main.examples.ExampleDiscoverScene.gameScene" fit nocode></preview>

> Just want to try things out? The [Playground][Playground] lets you compose scenes in the browser and exports the matching Kotlin code.
> {style="note"}

---

## Prerequisites

You need a **Java Development Kit (JDK) 11 or newer** and a build tool, for example Gradle or Maven. A compatible JDK is available from [Azul Zulu][AzulZuluOpenJDK].

BGW renders its user interface with a Chromium-based browser engine ([JCEF][JCEF]). The required native binaries are downloaded automatically the first time an application is started and cached in your user directory, so **an internet connection is needed for the first launch**.

---

## Setup

All artifacts are published on [Maven Central][MavenCentral]. With Gradle, the BGW Gradle plugin adds BGW as a dependency and passes the JVM arguments required by the browser engine to the `run` task, to tests and to the start scripts of the `application` plugin.

The plugin is published on Maven Central as well, so add Maven Central to the plugin repositories in your `settings.gradle.kts`:

<code-block lang="apache">
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}
</code-block>

Then apply the plugin in your `build.gradle.kts`, or add the dependency when using Maven:

<tabs group="gradleMaven">
    <tab title="Gradle" group-key="gradle">
        <code-block lang="apache">
plugins {
    kotlin("jvm") version "2.3.0"
    application
    id("tools.aqua.bgw") version "0.11.1"
}
&#13;
repositories {
    mavenCentral()
}
&#13;
kotlin {
    jvmToolchain(11)
}
&#13;
application {
    mainClass = "MainKt"
}
        </code-block>
    </tab>
    <tab title="Maven" group-key="maven">
        <code-block lang="xml">
            &lt;dependency&gt;
    &lt;groupId&gt;tools.aqua&lt;/groupId&gt;
    &lt;artifactId&gt;bgw-gui&lt;/artifactId&gt;
    &lt;version&gt;0.11.1&lt;/version&gt;
&lt;/dependency&gt;
        </code-block>
    </tab>
</tabs>

The plugin uses the BGW version matching its own version. Use the `bgw` block to choose another version or to add the [network modules][NetworkDoc]:

<code-block lang="apache">
bgw {
    version = "0.11.1"
    network = true // adds bgw-net-client and bgw-net-common
}
</code-block>

### JVM arguments

BGW embeds a native browser window, which needs access to some internals of the JDK. The Gradle plugin adds the required JVM arguments automatically. With Maven, add them to your run configuration yourself. Leave out `--enable-native-access` on Java 11, which does not accept it, and the two `sun.lwawt` arguments on Windows and Linux.

<code-block lang="xml">
            &lt;configuration&gt;
    &lt;jvmArguments&gt;
        --add-opens java.desktop/sun.awt=ALL-UNNAMED
        --add-opens java.desktop/java.awt.peer=ALL-UNNAMED
        --add-opens java.desktop/java.awt=ALL-UNNAMED
        --add-opens java.desktop/sun.lwawt=ALL-UNNAMED
        --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED
        --enable-native-access ALL-UNNAMED
    &lt;/jvmArguments&gt;
&lt;/configuration&gt;
</code-block>

> If you start the application from your IDE instead of Gradle or Maven, add the same arguments to the run configuration's _VM options_ field.
> {style="note"}

---

## Modules

BGW is split into several artifacts. Most games only need the first one.

<table orientation="horizontal">
    <tr>
        <td width="22%"><code>bgw-gui</code></td>
        <td>The framework itself: application, scenes, components, animations and dialogs.</td>
    </tr>
    <tr>
        <td><code>bgw-net-client</code></td>
        <td>Client library for online multiplayer. Requires <code>bgw-net-common</code>. The Gradle plugin adds both with <code>bgw { network = true }</code>. See Network communication.</td>
    </tr>
    <tr>
        <td><code>bgw-net-common</code></td>
        <td>Messages and annotations shared between clients and the server.</td>
    </tr>
</table>
