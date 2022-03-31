# Developer Guide for BGW

BGW makes heavy use of “advanced” Gradle features to abstract away most build logic. This is a guide to the Gradle
features and custom extension used (and how to modify them).

## Conventions

Build logic is extracted to *convention plugins*. They are located in `buildSrc/src/main/kotlin` and are compiled from
the `*.gradle.kts` files contained there. These follow the syntax for normal Gradle build files, but are compiled into
Gradle plugins behind the scenes. A convention plugin named `tools.aqua.xyz.gradle.kts` can be applied by using

```kotlin
plugins {
    id("tools.aqua.xyz")
}
```

Further information on convention plugins can be found in
the [Gradle documentation](https://docs.gradle.org/current/userguide/sharing_build_logic_between_subprojects.html).

Each Plugin is designed to contain common configuration for one project facette.

### `base-conventions`

The base conventions apply three plugins:

- [Gradle Task Tree](https://github.com/dorongold/gradle-task-tree) helps with debugging the build performed by Gradle
  (e.g., missing task dependencies).
- [Gradle Version Plugin](https://github.com/ben-manes/gradle-versions-plugin) can be used to check dependencies for
  updates. Since plugins are declared via `buildSrc`, it can unfortunately not detect plugin updates.
- [Spotless](https://github.com/diffplug/spotless) is used for enforcing style and license headers on the Gradle build
  files. The license header templates are located in the `contrib` folder.

It should be “inherited“ by all other conventions.

### `kotlin-conventions`

The Kotlin conventions handle the setup of the Kotlin compiler, documentation generation, testing, and code quality
enforcement. Style and license headers of Kotlin source files are again enforced
via [Spotless](https://github.com/diffplug/spotless). Additionally, [Detekt](https://detekt.dev/) is configured to
perform additional quality checks on Kotlin files, but will not fail the build on violations. The Detekt configuration
(with some rationales) is located in `contrib/detekt-rules.yml`.

### `publish-conventions`

The publish conventions handle publication of artifacts via Maven Central. Most of the process is automated, but each
module must provide a name and description for the generated POM file. This is done using the (custom) `MavenMetadata`
extension:

```kotlin
mavenMetadata {
    name.set("BoardGameWork Thing")
    description.set("A thing for working board games.")
}
```

The extension also enforces signing as soon as a publication task (including `publishToMavenLocal`) is included in the
build process. Each developer *must* generate a GPG key for signing and register the ID in their Gradle system
properties file. See the [Gradle documentation](https://docs.gradle.org/current/userguide/signing_plugin.html) for
details.

### `executable-conventions`

The executable conventions should be applied to every module that contains a main class. They enable the Kotlin and
publishing configurations as well as the
[application plugin](https://docs.gradle.org/current/userguide/application_plugin.html). Each module using these
conventions must set an appropriate main class. See the `bgw-examples` modules for examples.

### `library-conventions`

The library conventions should be applied to every module that is intended to be reused. They enable the Kotlin and
publishing configurations as well as the
[Java library plugin](https://docs.gradle.org/current/userguide/java_library_plugin.html). Each module using this
convention must take care to differentiate `api` and `implementation` dependencies. This convention can be combined with
the executable convention for programs with a defined API. See the `bgw-gui` module for an example.

### `spring-vaadin-conventions`

The Spring Boot + Vaadin conventions should be applied to each module that uses these frameworks. They set up all
relevant plugins and BOMs and include some required dependencies. They enable the Kotlin and publishing configurations.
See the `bgw-net/bgw-net-server` module for an example.

### `root-conventions`

The root conventions must only be applied to the root project and set up plugins that must be applied there. This
includes code coverage (to enable data aggregation across subprojects), style enforcement
via [Spotless](https://github.com/diffplug/spotless) for the `buildSrc`, and automatic version generation using the
[Gradle Git Versioning Plugin](https://github.com/qoomon/gradle-git-versioning-plugin).

The latter generates version numbers from the current Git state and requires a non-sparse Git checkout to work
correctly. The versioning rules are:

- If the HEAD points to a tag beginning with `v`, the remainder of the tag is used as a version (i.e., a release).
- If the branch is `main`, the version is the *last* release, plus a `git describe`-style specified, as a `SNAPSHOT`.
- If the branch is *not* `main`, the version is the *last* release, plus the branch name, plus a `git describe`-style
  specified, as a `SNAPSHOT`.
- Otherwise, the version of last resort is `0.0.0-SNAPSHOT` and a warning is emitted.

### `jekyll-conventions`

The Jekyll conventions are designed for generated documentation sites and mostly emulate the build process provided by
GitHub Pages. They provide the configuration `includedKDoc` for KDoc JARs that are to be included in the generated page.
For example, the dependency

```kotlin
includedKDoc(project(":bgw-thing", "kdoc"))
```

will take the artifact associated with the configuration `kdoc` from the project `bgw-thing` and unpack it in the
directory `bgw-thing` relative to the site root. The `kdoc` configuration is provided by every project using
the `kotlin-conventions`.

The Jekyll support is custom developed and uses a Jekyll Docker container. If Docker is not running, the `jekyllBuild`
task defined by this convention will fail. Additionally, a pure-Java server is provided to test the generated site
locally. See `bgw-docs` for an example.

## Custom Extensions, Tasks and Shorthands

Some custom build code is provided in `buildSrc/src/main/kotlin/tools/aqua`. This includes some shorthand notation used
inside the conventions, the `MavenMetadata` extension, and the Jekyll-related tasks. They contain explanatory KDoc
comments. Note that all code provided there is visible from *all* build scripts, both in `buildSrc` and regular modules.

## Declarative Dependencies

The BGW project utilizes *version catalogs* provided by the `gradle/libs.versions.toml` file. All dependencies used by
BGW projects should be defined there and included into projects using Gradle's type-safe accessor syntax. See any
project or convention for examples.

The file uses some shared version declarations in the `[versions]` block. These are required for versions shared by
multiple dependencies (e.g., Kotlin) and versions of non-Java-dependencies (i.e., the Jekyll Docker image). Depencencies
themselves are declared in the `[libraries]` block. Since Gradle plugins are applies via `buildSrc` and are therefore *
regular* dependencies of that project, they are declared here. Their aliases are prefixed by `gradle-`. Bundles are used
to group some common dependencies, while plugin aliases cannot be used in combination with `buildSrc`-applied plugins.

The [Gradle documentation](https://docs.gradle.org/current/userguide/platforms.html) should be consulted for further
information on catalogs and the TOML syntax.

## Style & Quality

As mentioned above, consistent styling is enforced via [Spotless](https://github.com/diffplug/spotless). A consistent
style can therefore be enforced by executing the `spotlessApply` task on the project. Note that Spotless is somewhat
memory-hungry, Gradle is therefore configured to use up to 1GiB of heap memory to avoid crashes. Since the Kotlin style
is applied using [Ktfmt](https://github.com/facebookincubator/ktfmt), developers may want to configure their IDEs to use
that style, but should still reformat their code.

Additionally, [Detekt](https://detekt.dev/) is used to flag code smells. Developers should consult the reports (and CLI
output) created by `detekt` and attempt to rectify the issues. Finally, the Kotlin compiler is instructed to treat
warnings as errors.