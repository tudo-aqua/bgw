/*
 * Copyright 2026 The BoardGameWork Authors
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

package tools.aqua.bgw.util

import kotlin.math.max
import tools.aqua.bgw.application.Constants

internal fun String.black() = "\u001B[30m$this\u001B[0m"

internal fun String.red() = "\u001B[31m$this\u001B[0m"

internal fun String.green() = "\u001B[32m$this\u001B[0m"

internal fun String.yellow() = "\u001B[33m$this\u001B[0m"

internal fun String.blue() = "\u001B[34m$this\u001B[0m"

internal fun String.purple() = "\u001B[35m$this\u001B[0m"

internal fun String.cyan() = "\u001B[36m$this\u001B[0m"

internal fun String.white() = "\u001B[37m$this\u001B[0m"

internal fun String.gray() = "\u001B[90m$this\u001B[0m"

internal fun String.bold() = "\u001B[1m$this"

internal fun String.colorize(color: Colors) =
    when (color) {
      Colors.BLACK -> this.black()
      Colors.RED -> this.red()
      Colors.GREEN -> this.green()
      Colors.YELLOW -> this.yellow()
      Colors.BLUE -> this.blue()
      Colors.PURPLE -> this.purple()
      Colors.CYAN -> this.cyan()
      Colors.WHITE -> this.white()
      Colors.GRAY -> this.gray()
    }

internal object Logger {
  internal var LOGLEVEL = if (Constants.DEBUG) LogType.DEBUG else LogType.INFO

  internal fun log(message: String, type: LogType = LogType.DEBUG) {
    val e = Throwable()
    val clazzFull = Class.forName(e.stackTrace[3].className).name.split(".").last()
    val clazz = if (clazzFull.length > 20) clazzFull.take(19) + "…" else clazzFull
    val emptySpaces = " ".repeat(max(0, 20 - clazz.length))

    when (type) {
      LogType.INFO -> {
        if (LogType.INFO < LOGLEVEL) return
        print("[BGW]".bold().purple())
        if (Constants.DEBUG) print(" $clazz $emptySpaces".purple())
        println(" $message")
      }
      LogType.WARN -> {
        if (LogType.WARN < LOGLEVEL) return
        print("[BGW]".bold().yellow())
        if (Constants.DEBUG) print(" $clazz $emptySpaces".yellow())
        println(" $message".yellow())
      }
      LogType.ERROR -> {
        if (LogType.ERROR < LOGLEVEL) return
        print("[BGW]".bold().red())
        if (Constants.DEBUG) print(" $clazz $emptySpaces".red())
        println(" $message".red())
      }
      LogType.DEBUG -> {
        if (LOGLEVEL != LogType.DEBUG) return
        print("[BGW]".bold().gray())
        if (Constants.DEBUG) print(" $clazz $emptySpaces".gray())
        println(" $message".gray())
      }
    }
  }

  internal fun logSingle(message: String, type: LogType = LogType.DEBUG) {
    val e = Throwable()
    val clazzFull = Class.forName(e.stackTrace[3].className).name.split(".").last()
    val clazz = if (clazzFull.length > 20) clazzFull.take(19) + "…" else clazzFull
    val emptySpaces = " ".repeat(max(0, 20 - clazz.length))

    when (type) {
      LogType.INFO -> {
        if (LogType.INFO < LOGLEVEL) return
        print("[BGW]".green())
        if (Constants.DEBUG) print(" $clazz $emptySpaces".green())
        print(" $message")
      }
      LogType.WARN -> {
        if (LogType.WARN < LOGLEVEL) return
        print("[BGW]".yellow())
        if (Constants.DEBUG) print(" $clazz $emptySpaces".yellow())
        print(" $message".yellow())
      }
      LogType.ERROR -> {
        if (LogType.ERROR < LOGLEVEL) return
        print("[BGW]".red())
        if (Constants.DEBUG) print(" $clazz $emptySpaces".red())
        print(" $message".red())
      }
      LogType.DEBUG -> {
        if (LOGLEVEL != LogType.DEBUG) return
        print("[BGW]".gray())
        if (Constants.DEBUG) print(" $clazz $emptySpaces".gray())
        print(" $message".gray())
      }
    }
  }

  internal fun log(any: Any, type: LogType = LogType.DEBUG) {
    log(any.toString(), type)
  }

  internal fun log(any: Any, color: Colors, type: String) {
    print("[${type.uppercase()}]".colorize(color))
    println(" $any")
  }

  internal fun error(message: Any) {
    log(message.toString(), LogType.ERROR)
  }

  internal fun error(stackTrace: Array<StackTraceElement>) {
    log(stackTrace.map { it.toString() }.joinToString { "\n" }, LogType.ERROR)
  }

  internal fun debug(message: Any) {
    log(message.toString(), LogType.DEBUG)
  }

  internal fun info(message: Any) {
    log(message.toString(), LogType.INFO)
  }

  internal fun warning(message: String) {
    log(message, LogType.WARN)
  }

  internal fun warning(stackTrace: Array<StackTraceElement>) {
    log(stackTrace.map { it.toString() }.joinToString { "\n" }, LogType.WARN)
  }
}

internal enum class LogType {
  DEBUG,
  INFO,
  WARN,
  ERROR
}

internal enum class Colors {
  BLACK,
  RED,
  GREEN,
  YELLOW,
  BLUE,
  PURPLE,
  CYAN,
  WHITE,
  GRAY
}
