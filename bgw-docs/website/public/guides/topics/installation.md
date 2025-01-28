[MauMauRules]: https://en.wikipedia.org/wiki/Mau_Mau_(card_game)
[BGW]: https://github.com/tudo-aqua/bgw
[JavaFX 17]: https://openjfx.io/openjfx-docs/
[AzulZuluOpenJDK]: https://www.azul.com/downloads/?version=java-11-lts&package=jdk-fx#download-openjdk
[Mac M1]: https://www.azul.com/downloads/?version=java-11-lts&os=macos&architecture=arm-64-bit&package=jdk-fx#download-openjdk
[BoardGameApplicationKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/index.html
[BoardGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-scene/index.html
[MenuSceneKDoc]: /docs/tools.aqua.bgw.core/-menu-scene/index.html
[GameComponentKDoc]: /docs/tools.aqua.bgw.components.gamecomponentviews/-game-component-view/index.html
[StaticComponentViewKDoc]: /docs/tools.aqua.bgw.components/-static-component-view/index.html
[LabelKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-label/index.html
[ButtonKDoc]: /docs/tools.aqua.bgw.components.uicomponents/-button/index.html
[ContainerKDoc]: /docs/tools.aqua.bgw.components.container/-game-component-container/index.html
[CardStackKDoc]: /docs/tools.aqua.bgw.components.container/-card-stack/index.html
[LinearLayoutKDoc]: /docs/tools.aqua.bgw.components.container/-linear-layout/index.html
[showGameSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-game-scene.html
[showMenuSceneKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show-menu-scene.html
[showKDoc]: /docs/tools.aqua.bgw.core/-board-game-application/show.html
[addComponentsKDoc]: /docs/tools.aqua.bgw.core/Scene/addComponents
[UIComponentDoc]: /guides/components/uicomponents
[LayoutViewDoc]: /guides/components/layout
[VisualsDoc]: /guides/concepts/visual
[GettingStarted]: /guides/getting-started
[Kotlin]: https://kotlinlang.org/

# Welcome to BoardGameWork

[BoardGameWork (BGW)][BGW] is a framework for creating 2D board game applications using [Kotlin][Kotlin].
This guide will introduce you to the essential concepts and features of the framework and guide you through the process
of setting up your first game scene. We will use the popular [MauMau][MauMauRules] card game as our example project.

## Prerequisites

The BoardGameWork is built on top of [JavaFX 17][JavaFX 17] and therefore requires at least JDK 11.
Since JavaFX was decoupled from the JavaJDK as of JDK 11, BoardGameWork comes with JavaFX dependencies including their
native libraries for various platforms like Windows, Linux and Mac.

We recommend installing a JDK build that already includes JavaFX to ensure your platform is supported.
[Azul Zulu Builds of OpenJDK][AzulZuluOpenJDK] support a wide range of platforms and architectures including [Mac M1][Mac M1].

## Setup

Start by adding the latest version of BGW as a dependency to your project.

<tabs group="gradleMaven">
    <tab title="Gradle" group-key="gradle">
        <code-block lang="apache"> 
        implementation(group = "tools.aqua", name = "bgw-gui", version = "0.9")
        </code-block>
    </tab>
    <tab title="Maven" group-key="maven">
        <code-block lang="xml">
            &lt;dependency&gt;
    &lt;groupId&gt;tools.aqua&lt;/groupId&gt;
    &lt;artifactId&gt;bgw-gui&lt;/artifactId&gt;
    &lt;version&gt;0.9&lt;/version&gt;
&lt;/dependency&gt;
        </code-block>
    </tab>
</tabs>

You also need to specify the correct JVM target in your build system.

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

> To learn more about creating your first board game application using BGW, continue with the [Getting Started][GettingStarted] section.
> {style="note"}
