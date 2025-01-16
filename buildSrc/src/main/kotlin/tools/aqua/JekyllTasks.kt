/*
 * Copyright 2022-2025 The BoardGameWork Authors
 * SPDX-License-Identifier: Apache-2.0
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package tools.aqua

import de.gesellix.docker.client.DockerClientImpl
import de.gesellix.docker.remote.api.ContainerCreateRequest
import de.gesellix.docker.remote.api.ContainerCreateResponse
import de.gesellix.docker.remote.api.HostConfig
import de.gesellix.docker.remote.api.core.Frame.StreamType.STDERR
import de.gesellix.docker.remote.api.core.Frame.StreamType.STDOUT
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import java.lang.System.`in` as systemIn
import java.time.Duration
import java.time.temporal.ChronoUnit.MINUTES
import javax.inject.Inject
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream.LONGFILE_POSIX
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.UncheckedIOException
import org.gradle.api.file.ArchiveOperations
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileSystemOperations
import org.gradle.api.file.ProjectLayout
import org.gradle.api.model.ObjectFactory
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import org.gradle.kotlin.dsl.mapProperty
import org.gradle.kotlin.dsl.property

/**
 * A test to execute a Jekyll build inside a Docker container. This requires a running docker
 * instance. File I/O is performed via docker get and push operations, i.e., permission issues
 * should not occur.
 *
 * @property archiveOps a Gradle archive operations provider.
 * @property fileOps a Gradle file operations provider.
 * @param layout a Gradle project layout.
 * @param objects a Gradle object factory.
 */
open class JekyllBuildTask
@Inject
constructor(
    private val archiveOps: ArchiveOperations,
    private val fileOps: FileSystemOperations,
    layout: ProjectLayout,
    objects: ObjectFactory
) : DefaultTask() {

  private companion object {
    /** The container-internal working directory. */
    const val JEKYLL_WORK_DIR = "/srv/jekyll"

    /** The container-internal username */
    const val JEKYLL_USERNAME = "jekyll"
  }

  /** The docker client object to use for communications. */
  private val dockerClient = DockerClientImpl()

  /**
   * The name of the Jekyll docker container to use for building, *without* the version part.
   * Defaults to `jekyll/jekyll`.
   */
  @Input
  @Optional
  val jekyllContainer: Property<String> = objects.property<String>().convention("jekyll/jekyll")

  /**
   * The version of the Jekyll docker container to use for building. Defaults to `latest`, which
   * results in a non-independent build. This should normally be overridden by the build script.
   */
  @Input
  @Optional
  val jekyllContainerVersion: Property<String> = objects.property<String>().convention("latest")

  /** The jekyll source tree. Defaults to `src/main/jekyll` in the project root. */
  @InputDirectory
  @Optional
  val source: DirectoryProperty =
      objects.directoryProperty().convention(layout.projectDirectory.dir("src/main/jekyll"))

  /**
   * Additional resources to be included into the jekyll build. For each entry `(k: v)` in this map,
   * the contents of `zipTree(v)` are copied to the directory `k`, resolved relative to the [source]
   * . `v` is resolved following the rules for [Project.zipTree]. Defaults to none.
   */
  @Input
  @Optional
  val includedResources: MapProperty<String, Any> = objects.mapProperty<String, Any>().empty()

  /**
   * Environment variables to set in the Jekyll build container, e.g. `PAGES_REPO_NWO` for GitHub
   * Pages. Defaults to none.
   */
  @Input
  @Optional
  val buildEnvironment: MapProperty<String, String> = objects.mapProperty<String, String>().empty()

  /** The directory to copy the created page to. Default to `jekyll` in the build directory. */
  @OutputDirectory
  @Optional
  val output: DirectoryProperty =
      objects.directoryProperty().convention(layout.buildDirectory.dir("jekyll"))

  /** Run the Jekyll build process. */
  @TaskAction
  fun runJekyllBuild() {
    checkDocker()
    val container = startJekyllContainer()
    try {
      copySourceFilesToContainer(container)
      runJekyll(container)
      copyFilesFromContainer(container)
    } finally {
      stopJekyllContainer(container)
    }
  }

  /**
   * Check if the docker client can access a docker instance.
   * @throws TaskExecutionException if an I/O exception occurs when reading the docker version.
   */
  private fun checkDocker() {
    try {
      logger.debug("Docker version is ${dockerClient.version().content}")
    } catch (e: UncheckedIOException) {
      logger.error("Could not communicate with Docker. Try (re-)starting the Docker daemon.")
      throw TaskExecutionException(this, e)
    }
  }

  /**
   * Prepare and start a Jekyll Docker container.
   * @return the creation response; contains the container ID.
   */
  private fun startJekyllContainer(): ContainerCreateResponse {
    val containerConfig =
        ContainerCreateRequest(
                attachStdout = true,
                attachStderr = true,
                hostConfig = HostConfig(autoRemove = true))
            .apply {
              // the container is not designed to remain active, work around this
              entrypoint = mutableListOf("sleep", Integer.MAX_VALUE.toString())
              env =
                  buildEnvironment.get().entries.mapTo(mutableListOf()) { (key, value) ->
                    "$key=$value"
                  }
              image = "${jekyllContainer.get()}:${jekyllContainerVersion.get()}"
            }
    logger.debug("Creating Jekyll container")
    val container = dockerClient.createContainer(containerConfig).content
    logger.debug("Starting Jekyll container ${container.id} with sleep process")
    dockerClient.startContainer(container.id)
    return container
  }

  /**
   * Copy all source files to the correct locations in the Jekyll container.
   * @param container the container creation data.
   */
  private fun copySourceFilesToContainer(container: ContainerCreateResponse) {
    val sourceDir = temporaryDir.resolve("source-files")
    val tar = temporaryDir.resolve("source-files.tar")

    logger.debug("Copying Jekyll source files from ${source.get()} into $sourceDir")

    fileOps.sync {
      from(source)
      into(sourceDir)
    }

    includedResources.get().forEach { (subDir, resource) ->
      logger.debug("Adding source files from $resource to ${sourceDir.resolve(subDir)}")
      fileOps.sync {
        from(archiveOps.zipTree(resource))
        into(sourceDir.resolve(subDir))
      }
    }

    logger.debug("Assembling source tar $tar")
    TarArchiveOutputStream(tar.outputStream())
        .apply { setLongFileMode(LONGFILE_POSIX) }
        .use { tarOut ->
          sourceDir.walk().forEach {
            tarOut.apply {
              val entry = createArchiveEntry(it, it.relativeTo(sourceDir).path)
              putArchiveEntry(entry)
              if (it.isFile) {
                it.inputStream().use { data -> data.copyTo(tarOut) }
              }
              closeArchiveEntry()
            }
          }
        }

    logger.debug("Pushing source tar $tar to Jekyll container ${container.id}")
    tar.inputStream().use { dockerClient.putArchive(container.id, JEKYLL_WORK_DIR, it) }

    logger.debug(
        "Changing owner of $JEKYLL_WORK_DIR to $JEKYLL_USERNAME in Jekyll container ${container.id}")
    dockerClient.exec(
        container.id, listOf("chown", "-R", "$JEKYLL_USERNAME:$JEKYLL_USERNAME", JEKYLL_WORK_DIR))
  }

  /**
   * Run Jekyll inside the container and await completion.
   * @param container the container creation data.
   */
  private fun runJekyll(container: ContainerCreateResponse) {
    logger.debug("Running Jekyll in ${container.id}")
    dockerClient.exec(
        container.id,
        listOf("jekyll", "build") + if (logger.isInfoEnabled) listOf("--verbose") else emptyList())
    logger.debug("Jekyll execution finished")
  }

  /**
   * Executes command inside the docker container and forwards the command output to the logger
   * @param containerId id of the container
   * @param command the command with arguments to execute
   */
  private fun DockerClientImpl.exec(containerId: String, command: List<String>) {
    exec(
        containerId,
        command,
        {
          when (it.streamType) {
            STDOUT -> logger.info(it.payloadAsString)
            STDERR -> logger.warn(it.payloadAsString)
            else -> logger.warn("Frame of unhandled type ${it.streamType} received")
          }
        },
        Duration.of(10, MINUTES))
  }

  /**
   * Copy all files from the container's build directory to a temporary location, and extract the
   * built site to its destination.
   * @param container the container creation data.
   */
  private fun copyFilesFromContainer(container: ContainerCreateResponse) {
    val resultDir = temporaryDir.resolve("result-files")
    val tar = temporaryDir.resolve("result-files.tar")

    logger.debug("Reading result tar from Jekyll container ${container.id} into $tar")
    dockerClient.getArchive(container.id, JEKYLL_WORK_DIR).content.use {
      tar.outputStream().use { tarOut -> it.copyTo(tarOut) }
    }

    logger.debug("Extracting result tar from $tar to $resultDir")
    fileOps.sync {
      from(archiveOps.tarTree(tar))
      into(resultDir)
    }

    logger.debug("Copying site from $resultDir/jekyll/_site to ${output.get()}")
    fileOps.sync {
      from(resultDir.resolve("jekyll/_site"))
      into(output)
    }
  }

  /**
   * Stop the Jekyll Docker container. This does not remove the container (it should be set to
   * autoremove).
   * @param container the container creation data.
   */
  private fun stopJekyllContainer(container: ContainerCreateResponse) {
    logger.debug("Stopping Jekyll container ${container.id}")
    dockerClient.stop(container.id)
  }
}

/**
 * A task for running a web server to serve a directories contents statically. This internally uses
 * Ktor with the Netty engine.
 * @param objects a Gradle object factory.
 */
open class KtorServeTask @Inject constructor(objects: ObjectFactory) : DefaultTask() {

  /** The directory to serve. */
  @InputDirectory val serverRoot: DirectoryProperty = objects.directoryProperty()

  /** The port to listen on. Defaults to `8080`. */
  @Input @Optional val port: Property<Int> = objects.property<Int>().convention(8080)

  /** The base URL to serve on. Defaults to `/`. */
  @Input @Optional val baseURL: Property<String> = objects.property<String>().convention("/")

  /**
   * Run the server, wait for user input and shut it down after a line has been read on standard
   * input.
   */
  @TaskAction
  fun serve() {
    val server =
        embeddedServer(Netty, port = port.get(), watchPaths = emptyList()) {
              routing {
                static(baseURL.get()) {
                  staticRootFolder = serverRoot.get().asFile
                  files(".")
                  file("/", "index.html")
                  default("index.html")
                }
              }
            }
            .start()
    try {
      logger.warn(
          "Serving on http://localhost:${port.get()}${baseURL.get()}, press [return] to end.")
      systemIn.bufferedReader().readLine() // do not close system input!
    } finally {
      server.stop(1000, 1000)
    }
  }
}
