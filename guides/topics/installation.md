[MauMauRules]: https://en.wikipedia.org/wiki/Mau_Mau_(card_game)
[BGW]: https://github.com/tudo-aqua/bgw
[AzulZuluOpenJDK]: https://www.azul.com/downloads/?version=java-11-lts&package=jdk-fx#download-openjdk
[Kotlin]: https://kotlinlang.org/
[GettingStarted]: /guides/getting-started

# Welcome to BoardGameWork

[BoardGameWork (BGW)][BGW] is a framework for creating 2D board game applications using [Kotlin][Kotlin].
This guide will introduce you to the essential concepts and features of the framework and guide you through the process
of setting up your first game scene. We will use the popular [MauMau][MauMauRules] card game as our example project.

## Prerequisites

Before you can start using BGW, you will need a Java Development Kit (JDK) installed on your system. BGW requires at least
Java 11 to run. You can download a compatible version of [Azul Zulu OpenJDK][AzulZuluOpenJDK] from the official website.

## Setup

Start by adding the latest version of BGW as a dependency to your project.

<tabs group="gradleMaven">
    <tab title="Gradle" group-key="gradle">
        <code-block lang="apache"> 
        implementation(group = "tools.aqua", name = "bgw-gui", version = "0.10")
        </code-block>
    </tab>
    <tab title="Maven" group-key="maven">
        <code-block lang="xml">
            &lt;dependency&gt;
    &lt;groupId&gt;tools.aqua&lt;/groupId&gt;
    &lt;artifactId&gt;bgw-gui&lt;/artifactId&gt;
    &lt;version&gt;0.10&lt;/version&gt;
&lt;/dependency&gt;
        </code-block>
    </tab>
</tabs>

When running on JDK 16 or later, you need to add the following JVM arguments to your run configuration:

<tabs group="gradleMaven">
    <tab title="Gradle" group-key="gradle">
        <code-block lang="apache">
            application {
    applicationDefaultJvmArgs = listOf(
        "--add-opens", "java.desktop/sun.awt=ALL-UNNAMED",
        "--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED",
        "--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED",
        "--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
}
        </code-block>
    </tab>
    <tab title="Maven" group-key="maven">
        <code-block lang="xml">
            &lt;configuration&gt;
    &lt;jvmArguments&gt;
        --add-opens java.desktop/sun.awt=ALL-UNNAMED 
        --add-opens java.desktop/java.awt.peer=ALL-UNNAMED 
        --add-opens java.desktop/sun.lwawt=ALL-UNNAMED 
        --add-opens java.desktop/sun.lwawt.macosx=ALL-UNNAMED
    &lt;/jvmArguments&gt;
&lt;/configuration&gt;
        </code-block>
    </tab>
</tabs>

It is therefore recommended to specify the correct JVM target in your build system.

<tabs group="gradleMaven">
    <tab title="Gradle" group-key="gradle">
        <code-block lang="apache">
            compileKotlin {
    kotlinOptions.jvmTarget = "11"
}
        </code-block>
    </tab>
    <tab title="Maven" group-key="maven">
        <code-block lang="xml">
            &lt;configuration&gt;
    &lt;jvmTarget&gt;11&lt;/jvmTarget&gt;
&lt;/configuration&gt;
        </code-block>
    </tab>
</tabs>

> The basic setup should now be complete. To learn more about creating your first board game application using BGW, continue with the [Getting Started][GettingStarted] section.
> {style="note"}
