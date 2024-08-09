import okhttp3.internal.notifyAll
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilerExecutionStrategy
import org.jetbrains.kotlin.gradle.tasks.throwExceptionIfCompilationFailed
import java.lang.management.ManagementFactory
import java.io.File

import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
  val kotlinVersion = "2.0.0"
  kotlin("multiplatform") //version kotlinVersion
  kotlin("plugin.serialization") version kotlinVersion
  application
  //id("org.openjfx.javafxplugin") version "0.0.14"
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
  jvmToolchain(11)
  jvm {
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
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.0")
      }
    }
    val commonTest by getting {
      dependencies {
        implementation(kotlin("test"))
      }
    }
    val jvmMain by getting {
      dependencies {
        implementation("io.ktor:ktor-server-core:2.3.11")
        implementation("io.ktor:ktor-server-netty:2.3.11")
        implementation("io.ktor:ktor-server-websockets:2.3.11")
        implementation("io.ktor:ktor-server-html-builder-jvm:2.3.11")
        implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.2")
        implementation("me.friwi:jcefmaven:122.1.10")
        //implementation("com.jfoenix:jfoenix:9.0.1")
      }
    }
    val jvmTest by getting
    val jsMain by getting {
      dependencies {
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react:18.3.1-pre.758")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom:18.3.1-pre.758")
        implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion:11.11.4-pre.758")
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
  doFirst {

  }

  dependsOn(tasks.named<Jar>("jvmJar"))
  classpath(tasks.named<Jar>("jvmJar"))

  doLast {

  }
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

gradle.buildFinished {
//  println("Message: ${this.failure?.message}")
//  println("Exception: ${this.failure?.cause}")
//  val stacktrace = this.failure?.stackTraceToString()
//  val wasCancelled = stacktrace?.indexOf("Build cancelled") != -1
//  println("Build cancelled: ${wasCancelled}")

  if (wasCancelled) {
//    ProcessHandle.allProcesses().forEach {
//      if (it.info().command().orElse("").contains("jcef_helper.exe")) {
//        println("Chrome Process: ${it.pid()}")
//        it.destroy()
//      }
//    }
    ExitCode.OK
  }
  ExitCode.OK
}

//javafx {
//  version = "18.0.2"
//  modules = listOf("javafx.controls", "javafx.web")
//}