plugins {
  val kotlinVersion = "1.8.21"
  kotlin("multiplatform") version kotlinVersion
  kotlin("plugin.serialization") version kotlinVersion
  application
  id("org.openjfx.javafxplugin") version "0.0.14"
  `maven-publish`
}

group = "tools.aqua"
version = "1.0-SNAPSHOT"

repositories {
  jcenter()
  mavenCentral()
  maven("https://maven.pkg.jetbrains.space/public/p/kotlinx-html/maven")
}

kotlin {
  jvm {
    jvmToolchain(17)
    withJava()
    testRuns["test"].executionTask.configure {
      useJUnitPlatform()
    }
  }
  js(IR) {
    binaries.executable()
    browser {
      commonWebpackConfig {
        cssSupport {
          enabled.set(true)
        }
      }
    }
  }
  sourceSets {
    val commonMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation("io.ktor:ktor-server-core:2.0.2")
        implementation("io.ktor:ktor-server-netty:2.0.2")
        implementation("io.ktor:ktor-server-websockets:2.0.2")
        implementation("io.ktor:ktor-server-html-builder-jvm:2.0.2")
        implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
        implementation("me.friwi:jcefmaven:110.0.25.1")
        implementation("com.jfoenix:jfoenix:9.0.1")
      }
    }
    val jvmTest by getting
    val jsMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.2.0-pre.346")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.2.0-pre.346")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.9.3-pre.346")
      }
    }
    val jsTest by getting
  }
}

application {
  mainClass.set("tools.aqua.bgw.main.MainKt")
}

tasks.named<Copy>("jvmProcessResources") {
  val jsBrowserDistribution = tasks.named("jsBrowserDistribution")
  from(jsBrowserDistribution)
}

tasks.named<JavaExec>("run") {
  dependsOn(tasks.named<Jar>("jvmJar"))
  classpath(tasks.named<Jar>("jvmJar"))
}

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "tools.aqua"
      artifactId = "bgw-gui"
      version = "1.0-SNAPSHOT"
      from(components["kotlin"])
    }
  }
}

javafx {
  version = "20"
  modules = listOf("javafx.controls", "javafx.web")
}