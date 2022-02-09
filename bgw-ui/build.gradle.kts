@file:Suppress("SpellCheckingInspection")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
//	id("org.openjfx.javafxplugin") version "0.0.11"
    id("org.jetbrains.dokka") version "1.4.32"
    id("io.gitlab.arturbosch.detekt") version "1.18.0"
    id("org.cadixdev.licenser") version "0.6.1"
    id("com.dorongold.task-tree") version "2.1.0" // example usage: ./gradlew publish taskTree
    `maven-publish`
    kotlin("jvm")
//	java
    signing
}

group = "tools.aqua"
version = rootProject.version
var javaFxVersion: String = "17.0.2"
extra["isReleaseVersion"] = !version.toString().endsWith("SNAPSHOT")

repositories {
    mavenCentral()
}

dependencies {
//	implementation(kotlin("stdlib"))
    implementation(kotlin("stdlib-jdk8"))
    testImplementation(kotlin("test"))

    dokkaGfmPlugin("org.jetbrains.dokka:kotlin-as-java-plugin:1.6.0")

    /*
     * jfoenix - Applies styles to JavaFX controls.
     */
    implementation(group = "com.jfoenix", name = "jfoenix", version = "9.0.10")
    /*
     * javafx.base - Defines the base APIs for the JavaFX UI toolkit, including APIs for bindings, properties,
     * collections, and events.
     */
    implementation(group = "org.openjfx", name = "javafx-base", version = javaFxVersion, classifier = "win")
    implementation(group = "org.openjfx", name = "javafx-base", version = javaFxVersion, classifier = "mac")
    implementation(group = "org.openjfx", name = "javafx-base", version = javaFxVersion, classifier = "mac-aarch64")
    implementation(group = "org.openjfx", name = "javafx-base", version = javaFxVersion, classifier = "linux")
    /*
     * javafx.controls  - Defines the UI controls, charts, and skins that are available for the JavaFX UI toolkit.
     */
    implementation(group = "org.openjfx", name = "javafx-controls", version = javaFxVersion, classifier = "win")
    implementation(group = "org.openjfx", name = "javafx-controls", version = javaFxVersion, classifier = "mac")
    implementation(group = "org.openjfx", name = "javafx-controls", version = javaFxVersion, classifier = "mac-aarch64")
    implementation(group = "org.openjfx", name = "javafx-controls", version = javaFxVersion, classifier = "linux")

    /*
     * javafx.graphics  - Defines the core scenegraph APIs for the JavaFX UI toolkit (such as layout containers,
     * application lifecycle, shapes, transformations, canvas, input, painting, image handling, and effects), as well
     * as APIs for animation, css, concurrency, geometry, printing, and windowing.
     */
    implementation(group = "org.openjfx", name = "javafx-graphics", version = javaFxVersion, classifier = "win")
    implementation(group = "org.openjfx", name = "javafx-graphics", version = javaFxVersion, classifier = "mac")
    implementation(group = "org.openjfx", name = "javafx-graphics", version = javaFxVersion, classifier = "mac-aarch64")
    implementation(group = "org.openjfx", name = "javafx-graphics", version = javaFxVersion, classifier = "linux")

    /* UNUSED MODULES:
     * javafx.fxml - Defines the FXML APIs for the JavaFX UI toolkit.
     *
     * javafx.media - Defines APIs for playback of media and audio content, as part of the JavaFX UI toolkit, including
                        MediaView and MediaPlayer.
     * javafx.swing - Defines APIs for the JavaFX / Swing interop support included with the JavaFX UI toolkit, including
                        SwingNode (for embedding Swing inside a JavaFX application) and JFXPanel (for embedding JavaFX
                        inside a Swing application).
     * javafx.web - Defines APIs for the WebView functionality contained within the JavaFX UI toolkit.
     */
}

//javafx {
//	version = javaFxVersion
//	modules("javafx.base", "javafx.controls", "javafx.graphics")
////	configuration = "compileOnly"
//}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

detekt {
    source = files("src/main/kotlin/tools/aqua/bgw")
    config = files("detekt-rules.yml")
}

val javadocJar: TaskProvider<Jar> by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
}

java {
    withSourcesJar()
    withJavadocJar()
    modularity.inferModulePath.set(true)
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])

            pom {
                name.set("BoardGameWork Core Library")

                description.set(
                    "A framework for board game applications."
                )

                url.set("https://github.com/tudo-aqua/bgw-core")

                licenses {
                    license {
                        name.set("Apache License, Version 2.0")
                        url.set("https://opensource.org/licenses/Apache-2.0")
                    }
                }

                developers {
                    developer {
                        name.set("Stefan Naujokat")
                        email.set("stefan.naujokat@tu-dortmund.de")
                    }
                    developer {
                        name.set("Till Schallau")
                        email.set("till.schallau@tu-dortmund.de")
                    }
                    developer {
                        name.set("Dominik Mäckel")
                        email.set("dominik.maeckel@tu-dortmund.de")
                    }
                    developer {
                        name.set("Fabian Klümpers")
                        email.set("fabian.kluempers@tu-dortmund.de")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com:tudo-aqua/bgw-core.git")
                    developerConnection.set("scm:git:ssh://git@github.com:tudo-aqua/bgw-core.git")
                    url.set("https://github.com/tudo-aqua/bgw-core/tree/main")
                }
            }
        }
    }
    repositories {
        maven {
            name = "nexusOSS"
            val releasesUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            val snapshotsUrl = uri("https://oss.sonatype.org/content/repositories/snapshots/")
            url = if (version.toString().endsWith("SNAPSHOT")) snapshotsUrl else releasesUrl
            credentials {
                username = properties["nexusUsername"] as? String
                password = properties["nexusPassword"] as? String
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications["maven"])
}

tasks.withType<Sign>().configureEach {
    onlyIf { project.extra["isReleaseVersion"] as Boolean }
}

tasks.named("publishMavenPublicationToNexusOSSRepository") {
    dependsOn(tasks.checkLicenses)
}

license {
    header(rootProject.file("LICENSE-HEADER.txt"))
}

