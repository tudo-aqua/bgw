/*
 * Copyright 2025 The BoardGameWork Authors
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

package tools.aqua.bgw.builder

import DialogData
import org.cef.browser.CefBrowser
import tools.aqua.bgw.dialog.DialogType

internal object DialogBuilder {
  private val css =
      """
        * {
            padding: 0;
            margin: 0;
            box-sizing: border-box;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Oxygen, Ubuntu, Cantarell, "Open Sans", "Helvetica Neue", sans-serif;
        }
        
        *::-webkit-scrollbar {
            display: none;
        }
        
        body {
            background-color: #161d29;
            color: white;
            padding: 1.5rem;
            padding-inline: 2rem;
            width: 100%;
            height: 100vh;
            display: flex;
            flex-direction: column;
        }
        
        .header {
            display: flex;
            align-items: center;
            margin-bottom: 1rem;
            padding-bottom: 0.5rem;
            gap: 1rem;
        }
        
        .header-content {
            display: flex;
            align-items: baseline;
            flex-direction: column;
            gap: 0.25rem;
        }
        
        h1 {
            font-size: 1.25rem;
            font-weight: 700;
            color: white;
        }
        
        .message {
            font-size: 0.85rem;
            color: #9ca3af;
            flex: 1;
        }
        
        .stack-container {
            flex: 1;
            border-radius: 0.75rem;
            background: #121824;
            overflow: hidden;
            display: flex;
            flex-direction: column;
        }
        
        .stack-header {
            background: #0f141f;
            color: #e6e6e6;
            padding: 0.75rem 1rem;
            padding-inline: 1.5rem;
            font-size: 0.9rem;
            font-weight: 500;
        }
        
        .code-scroll {
            padding: 1rem;
            padding-inline: 1.5rem;
            overflow: auto;
            height: 100%;
        }
        
        .code-scroll::-webkit-scrollbar {
            display: none;
        }
        
        code {
            color: #e6e6e6;
            font-family: monospace;
            font-size: 0.9rem;
            line-height: 1.5;
        }
        
        .footer {
            margin-top: 1rem;
            display: flex;
            justify-content: flex-end;
            gap: 0.5rem;
        }
        
        button {
            background-color: #121824;
            color: white;
            border: none;
            border-radius: 0.25rem;
            padding: 0.5rem 1.25rem;
            font-size: 0.9rem;
            font-weight: 500;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        
        button:hover {
            background-color: #0f141f;
        }
        
        /* Syntax highlighting classes */
        .pkg { color: #6dbeff; font-family: monospace; }
        .cls { color: #ffc656; font-family: monospace; }
        .method { color: #ffc656; font-family: monospace; }
        .line { color: #9ca3af; font-family: monospace; }
        .elem { color: #ea7afc; font-family: monospace; }
        .msg { color: #9ca3af; font-style: italic; font-family: monospace; }
    """.trimIndent()

  internal fun setDialogContent(browser: CefBrowser, dialogData: DialogData) {
    browser.executeJavaScript(
        """
        document.write(`
            <html>
                <head>
                    <script>
                      function buttonClicked(buttonIndex) {
                          const data = {
                              dialogId: "${dialogData.id}",
                              buttonIndex: buttonIndex
                          };
                          const encodedData = JSON.stringify(data);
                          
                          window.cefDialogQuery({
                              request: encodedData,
                              onSuccess: function() {
                                  window.close();
                              },
                              onFailure: function() {
                                  window.close();
                              }
                          });
                      }
                    </script>
                    <style>                        
                        $css
                        
                        ${dialogData.buttons.mapIndexed { index, button ->
                            ".button-${index} { background-color: ${escapeHtml(button.backgroundColor)}15; color: ${escapeHtml(button.foregroundColor)}; } .button-${index}:hover { background-color: ${escapeHtml(button.backgroundColor)}20; }"
                        }.joinToString("\n")}
                    </style>
                </head>
                <body>
                    <div class="header">
                        ${getIcon(dialogData.dialogType)}
                        <div class="header-content">
                            <h1 style="margin-top: -0.2rem; padding: 0px;">${escapeHtml(dialogData.header)}</h1>
                        </div>
                    </div>
                    
                    <div class="stack-container">
                        <div class="code-scroll">
                            <code>${escapeHtml(dialogData.message)}</code>
                        </div>
                    </div>
                    
                    <div class="footer">
                        ${
                            dialogData.buttons.mapIndexed { index, button -> 
                                "<button onClick=\"buttonClicked($index)\">${escapeHtml(button.text)}</button>"
                            }.joinToString("\n")
                        }
                    </div>
                </body>
            </html>`);
        """.trimIndent(),
        browser.url,
        0)
  }

  internal fun setExceptionDialogContent(browser: CefBrowser, dialogData: DialogData) {
    browser.executeJavaScript(
        """
        document.write(`
            <html>
                <head>       
                    <script>
                      function buttonClicked(buttonIndex) {
                          const data = {
                              dialogId: "${dialogData.id}",
                              buttonIndex: buttonIndex
                          };
                          const encodedData = JSON.stringify(data);
                          
                          window.cefDialogQuery({
                              request: encodedData,
                              onSuccess: function() {
                                  window.close();
                              },
                              onFailure: function() {
                                  window.close();
                              }
                          });
                      }
                    </script> 
                    <style>
                        $css
                        
                        ${dialogData.buttons.mapIndexed { index, button ->
                            ".button-${index} { background-color: ${button.backgroundColor}15; color: ${button.foregroundColor}; } .button-${index}:hover { background-color: ${button.backgroundColor}20; }"
                        }.joinToString("\n")}
                    </style>
                </head>
                <body>
                    <div class="header">
                        ${getIcon(dialogData.dialogType, 36)}
                        <div class="header-content">
                            <h1>${extractExceptionType(dialogData.exception)}</h1>
                            <div class="message">${escapeHtml(dialogData.message)}</div>
                        </div>
                    </div>
                    
                    <div class="stack-container">
                        <div class="stack-header">Stacktrace</div>
                        <div class="code-scroll">
                            <code>${syntaxHighlightStackTrace(dialogData.exception)}</code>
                        </div>
                    </div>
                    
                    <div class="footer">
                        <button onClick="buttonClicked(0)">Dismiss</button>
                    </div>
                </body>
            </html>`);
        """.trimIndent(),
        browser.url,
        0)
  }

  private fun getIcon(dialogType: DialogType, size: Int = 24, fill: String = "#ffffff"): String {
    var svg =
        """<svg xmlns="http://www.w3.org/2000/svg" height="${size}px" viewBox="0 -960 960 960" width="${size}px" fill="$fill">"""
    svg +=
        when (dialogType) {
          DialogType.NONE ->
              """<path d="M480-80q-33 0-56.5-23.5T400-160h160q0 33-23.5 56.5T480-80ZM360-200q-17 0-28.5-11.5T320-240q0-17 11.5-28.5T360-280h240q17 0 28.5 11.5T640-240q0 17-11.5 28.5T600-200H360Zm-30-120q-69-41-109.5-110T180-580q0-125 87.5-212.5T480-880q125 0 212.5 87.5T780-580q0 81-40.5 150T630-320H330Z"/>"""
          DialogType.INFORMATION ->
              """<path d="M480-280q17 0 28.5-11.5T520-320v-160q0-17-11.5-28.5T480-520q-17 0-28.5 11.5T440-480v160q0 17 11.5 28.5T480-280Zm0-320q17 0 28.5-11.5T520-640q0-17-11.5-28.5T480-680q-17 0-28.5 11.5T440-640q0 17 11.5 28.5T480-600Zm0 520q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Z"/>"""
          DialogType.WARNING ->
              """<path d="M109-120q-11 0-20-5.5T75-140q-5-9-5.5-19.5T75-180l370-640q6-10 15.5-15t19.5-5q10 0 19.5 5t15.5 15l370 640q6 10 5.5 20.5T885-140q-5 9-14 14.5t-20 5.5H109Zm371-120q17 0 28.5-11.5T520-280q0-17-11.5-28.5T480-320q-17 0-28.5 11.5T440-280q0 17 11.5 28.5T480-240Zm0-120q17 0 28.5-11.5T520-400v-120q0-17-11.5-28.5T480-560q-17 0-28.5 11.5T440-520v120q0 17 11.5 28.5T480-360Z"/>"""
          DialogType.CONFIRMATION ->
              """<path d="m508-512-58-57q-11-11-27.5-11T394-568q-11 11-11 28t11 28l86 86q12 12 28 12t28-12l170-170q12-12 11.5-28T706-652q-12-12-28.5-12.5T649-653L508-512ZM320-240q-33 0-56.5-23.5T240-320v-480q0-33 23.5-56.5T320-880h480q33 0 56.5 23.5T880-800v480q0 33-23.5 56.5T800-240H320ZM160-80q-33 0-56.5-23.5T80-160v-520q0-17 11.5-28.5T120-720q17 0 28.5 11.5T160-680v520h520q17 0 28.5 11.5T720-120q0 17-11.5 28.5T680-80H160Z"/>"""
          DialogType.ERROR ->
              """<path d="M363-120q-16 0-30.5-6T307-143L143-307q-11-11-17-25.5t-6-30.5v-234q0-16 6-30.5t17-25.5l164-164q11-11 25.5-17t30.5-6h234q16 0 30.5 6t25.5 17l164 164q11 11 17 25.5t6 30.5v234q0 16-6 30.5T817-307L653-143q-11 11-25.5 17t-30.5 6H363Zm117-304 86 86q11 11 28 11t28-11q11-11 11-28t-11-28l-86-86 86-86q11-11 11-28t-11-28q-11-11-28-11t-28 11l-86 86-86-86q-11-11-28-11t-28 11q-11 11-11 28t11 28l86 86-86 86q-11 11-11 28t11 28q11 11 28 11t28-11l86-86Z"/>"""
          DialogType.EXCEPTION ->
              """<path d="M480-120q-65 0-120.5-32T272-240h-72q-17 0-28.5-11.5T160-280q0-17 11.5-28.5T200-320h44q-3-20-3.5-40t-.5-40h-40q-17 0-28.5-11.5T160-440q0-17 11.5-28.5T200-480h40q0-20 .5-40t3.5-40h-44q-17 0-28.5-11.5T160-600q0-17 11.5-28.5T200-640h72q14-23 31.5-43t40.5-35l-37-38q-11-11-11-27.5t12-28.5q11-11 28-11t28 11l58 58q28-9 57-9t57 9l60-59q11-11 27.5-11t28.5 12q11 11 11 28t-11 28l-38 38q23 15 41.5 34.5T688-640h72q17 0 28.5 11.5T800-600q0 17-11.5 28.5T760-560h-44q3 20 3.5 40t.5 40h40q17 0 28.5 11.5T800-440q0 17-11.5 28.5T760-400h-40q0 20-.5 40t-3.5 40h44q17 0 28.5 11.5T800-280q0 17-11.5 28.5T760-240h-72q-32 56-87.5 88T480-120Zm-40-200h80q17 0 28.5-11.5T560-360q0-17-11.5-28.5T520-400h-80q-17 0-28.5 11.5T400-360q0 17 11.5 28.5T440-320Zm0-160h80q17 0 28.5-11.5T560-520q0-17-11.5-28.5T520-560h-80q-17 0-28.5 11.5T400-520q0 17 11.5 28.5T440-480Z"/>"""
        }
    svg += "</svg>"

    return svg
  }

  private fun extractExceptionType(trace: String?): String {
    if (trace == null) return ""

    val exceptionType = trace.substringBefore(":")
    return exceptionType.replace(":", "").split(".").last().trim()
  }

  private fun syntaxHighlightStackTrace(trace: String?): String {
    if (trace == null) return ""

    return trace
        .replace(Regex("\\\$(\\d+|\\w+)"), "")
        // Match package.Class@hexIdentifier patterns
        .replace(Regex("(\\w+(?:\\.\\w+)+)@([0-9a-f]+)"), "<span class=\"elem\">$1@$2</span>")
        // Match standard stack trace lines with file and line numbers
        .replace(
            Regex("at ((?:[\\w.]+\\.)+)(.+)\\((.*?):(\\d+)\\)"),
            "<span class=\"msg\">at</span> <span class=\"pkg\">$1</span><span class=\"method\">$2</span><span class=\"line\">($3:$4)</span>")
        // Match exception class names with message
        .replace(
            Regex("^([^:\\n]+Exception|[^:\\n]+Error):(.*)", RegexOption.MULTILINE),
            "<span class=\"cls\">$1</span>:<span class=\"msg\">$2</span>")
        // Highlight "Caused by:" sections
        .replace("Caused by: ", "<span class=\"msg\">Caused by: </span>")
        // Replace tabs with proper spacing
        .replace("\t", "<span style=\"display:inline-block;width:20px;\"></span>")
        // Replace newlines with HTML breaks
        .replace("\n", "<br>")
        .trimIndent()
        .trim()
  }

  private fun escapeHtml(text: String): String {
    return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
  }
}
