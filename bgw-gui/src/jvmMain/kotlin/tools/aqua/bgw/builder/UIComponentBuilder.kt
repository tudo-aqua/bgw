package tools.aqua.bgw.builder

import tools.aqua.bgw.components.uicomponents.*

object UIComponentBuilder {
    @Suppress("DuplicatedCode")
    fun build(uiComponent: UIComponent) {
        uiComponent.internalCSSProperty.guiListener = { _, _ -> Frontend.updateScene() }
        uiComponent.fontProperty.guiListener = { _, _ -> Frontend.updateScene() }
        uiComponent.backgroundStyleProperty.guiListener = { _, _ -> Frontend.updateScene() }
        uiComponent.componentStyleProperty.guiListener = { _, _ -> Frontend.updateScene() }
        when (uiComponent) {
            is LabeledUIComponent -> buildLabeledUIComponent(uiComponent)
            is TextInputUIComponent -> buildTextInputUIComponent(uiComponent)
            is ComboBox<*> -> buildComboBox(uiComponent)
            is ColorPicker -> buildColorPicker(uiComponent)
            is ProgressBar -> buildProgressBar(uiComponent)
            is StructuredDataView<*> -> buildStructuredDataView(uiComponent)
        }
    }

    private fun buildLabeledUIComponent(labeledUIComponent: LabeledUIComponent) {
        labeledUIComponent.isWrapTextProperty.guiListener = { _, _ -> Frontend.updateScene() }
        labeledUIComponent.textProperty.guiListener = { _, _ -> Frontend.updateScene() }
        labeledUIComponent.alignmentProperty.guiListener = { _, _ -> Frontend.updateScene() }
        when (labeledUIComponent) {
            is Button -> buildButton(labeledUIComponent)
            is CheckBox -> buildCheckBox(labeledUIComponent)
            is Label -> buildLabel(labeledUIComponent)
            is BinaryStateButton -> buildBinaryStateButton(labeledUIComponent)
        }
    }

    private fun buildTextInputUIComponent(uiComponent: TextInputUIComponent) {
        when (uiComponent) {
            is TextArea -> buildTextArea(uiComponent)
            is TextField -> buildTextField(uiComponent)
            is PasswordField -> buildPasswordField(uiComponent)
        }
    }

    private fun buildBinaryStateButton(binaryStateButton: BinaryStateButton) {
        //TODO: Add property for toggle group
        binaryStateButton.selectedProperty.guiListener = { _, _ -> Frontend.updateScene() }
        when (binaryStateButton) {
            is ToggleButton -> buildToggleButton(binaryStateButton)
            is RadioButton -> buildRadioButton(binaryStateButton)
        }
    }

    private fun buildButton(button: Button) {}

    private fun buildCheckBox(checkBox: CheckBox) {
        checkBox.isCheckedProperty.guiListener = { _, _ -> Frontend.updateScene() }
        checkBox.isIndeterminateAllowedProperty.guiListener = { _, _ -> Frontend.updateScene() }
        checkBox.isIndeterminateProperty.guiListener = { _, _ -> Frontend.updateScene() }
    }

    private fun buildLabel(label: Label) {}

    private fun buildComboBox(comboBox: ComboBox<*>) {
        comboBox.observableItemsList.guiListener = { _, _ -> Frontend.updateScene() }
        comboBox.selectedItemProperty.guiListener = { _, _ -> Frontend.updateScene() }
        comboBox.formatFunctionProperty.guiListener = { _, _ -> Frontend.updateScene() }
    }

    private fun buildTextArea(textArea: TextArea) {}

    private fun buildTextField(textField: TextField) {}

    private fun buildPasswordField(passwordField: PasswordField) {}

    private fun buildToggleButton(toggleButton: ToggleButton) {}

    private fun buildRadioButton(radioButton: RadioButton) {}

    private fun buildColorPicker(colorPicker: ColorPicker) {
        colorPicker.selectedColorProperty.guiListener = { _, _ -> Frontend.updateScene() }
    }

    private fun buildProgressBar(progressBar: ProgressBar) {
        progressBar.progressProperty.guiListener = { _, _ -> Frontend.updateScene() }
        progressBar.barColorProperty.guiListener = { _, _ -> Frontend.updateScene() }
    }

    @Suppress("DuplicatedCode")
    private fun buildStructuredDataView(structuredDataView: StructuredDataView<*>) {
        structuredDataView.items.guiListener = { _, _ -> Frontend.updateScene() }
        structuredDataView.selectionModeProperty.guiListener = { _, _ -> Frontend.updateScene() }
        structuredDataView.selectionBackgroundProperty.guiListener = { _, _ -> Frontend.updateScene() }
        structuredDataView.selectionStyleProperty.guiListener = { _, _ -> Frontend.updateScene() }
        /* Internal Properties */
        structuredDataView.selectedItemsList.guiListener = { _, _ -> Frontend.updateScene() }
        structuredDataView.selectedIndicesList.guiListener = { _, _ -> Frontend.updateScene() }
    }
}

