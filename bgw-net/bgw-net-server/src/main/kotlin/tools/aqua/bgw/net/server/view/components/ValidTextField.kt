/*
 * Copyright 2022-2023 The BoardGameWork Authors
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

@file:Suppress("MemberVisibilityCanBePrivate")

package tools.aqua.bgw.net.server.view.components

import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.data.binder.Binder
import com.vaadin.flow.data.binder.Binder.BindingBuilder
import com.vaadin.flow.data.binder.Validator
import com.vaadin.flow.function.SerializablePredicate

/** A [TextField] that also validates the content. */
class ValidTextField(label: String) : TextField(label) {
  internal class Content {
    var value: String? = null
  }

  private val content: Content = Content()
  private val binder: Binder<Content> = Binder()
  private val validators: MutableList<Validator<String?>> = mutableListOf()

  init {
    binder.bean = content
  }

  /** Clears the input fields content. */
  fun reset() {
    this.binder.fields.forEach { field -> field.clear() }
  }

  /** Adds a validator for the user input. */
  fun addValidator(predicate: SerializablePredicate<String?>?, errorMessage: String?) {
    addValidator(Validator.from(predicate, errorMessage))
  }

  /** Adds a validator for the user input. */
  fun addValidator(validator: Validator<String?>) {
    validators.add(validator)
    build()
  }

  private fun build() {
    val builder: BindingBuilder<Content, String?> = binder.forField(this)
    for (v in validators) {
      builder.withValidator(v)
    }
    builder.bind({ obj: Content -> obj.value }) { obj: Content, content: String? ->
      obj.value = content
    }
  }
}
