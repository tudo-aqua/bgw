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

package tools.aqua.bgw.builder

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import tools.aqua.bgw.dialog.Dialog
import tools.aqua.bgw.dialog.DialogType
import tools.aqua.bgw.mapper.DialogMapper

class DialogBuilderTest {
  private class FailingConstructor {
    val exception = IllegalStateException("Could not read C:\\Users\\me\\save.txt <b>`\${x}`</b>")
  }

  @Test
  fun exceptionDialogEscapesMessageAndStackTrace() {
    val exception = FailingConstructor().exception
    val html =
        DialogBuilder.exceptionDialogHtml(
            DialogMapper.map(Dialog("Error", "Error", exception.message!!, exception)))

    // Constructor frames ("<init>") were parsed as HTML tags and disappeared.
    assertTrue(html.contains("&lt;init&gt;"), "Constructor frame is not escaped")
    assertFalse(html.contains("<init>"), "Constructor frame is not escaped")
    assertFalse(html.contains("<b>"), "Message is not escaped")
    assertTrue(html.contains("C:\\Users\\me\\save.txt"), "Windows path was altered")
  }

  @Test
  fun dialogEscapesUserContent() {
    val html =
        DialogBuilder.dialogHtml(
            DialogMapper.map(
                Dialog(DialogType.INFORMATION, "Title", "<script>x</script>", "`\${1+1}` \\n")))
    assertFalse(html.contains("<script>x</script>"), "Header is not escaped")
    assertTrue(html.contains("`\${1+1}` \\n"), "Message was altered")
  }
}
