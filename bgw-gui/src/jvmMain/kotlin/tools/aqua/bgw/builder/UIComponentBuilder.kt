package tools.aqua.bgw.builder

import tools.aqua.bgw.components.uicomponents.*
import tools.aqua.bgw.core.Frontend

internal object UIComponentBuilder {
    @Suppress("DuplicatedCode")
    fun build(uiComponent: UIComponent) {
        uiComponent.internalCSSProperty.guiListener = { _, _ -> Frontend.updateComponent(uiComponent) }
        uiComponent.fontProperty.guiListener = { _, _ -> Frontend.updateComponent(uiComponent) }
        uiComponent.backgroundStyleProperty.guiListener = { _, _ -> Frontend.updateComponent(uiComponent) }
        uiComponent.componentStyleProperty.guiListener = { _, _ -> Frontend.updateComponent(uiComponent) }
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
        labeledUIComponent.isWrapTextProperty.guiListener = { _, _ -> Frontend.updateComponent(labeledUIComponent) }
        labeledUIComponent.textProperty.guiListener = { _, _ -> Frontend.updateComponent(labeledUIComponent) }
        labeledUIComponent.alignmentProperty.guiListener = { _, _ -> Frontend.updateComponent(labeledUIComponent) }
        when (labeledUIComponent) {
            is Button -> buildButton(labeledUIComponent)
            is CheckBox -> buildCheckBox(labeledUIComponent)
            is Label -> buildLabel(labeledUIComponent)
            is BinaryStateButton -> buildBinaryStateButton(labeledUIComponent)
        }
    }

    private fun buildTextInputUIComponent(textInputUIComponent: TextInputUIComponent) {
        textInputUIComponent.textProperty.guiListener = { _, _ -> Frontend.updateComponent(textInputUIComponent) }
        textInputUIComponent.promptProperty.guiListener = { _, _ -> Frontend.updateComponent(textInputUIComponent) }
        when (textInputUIComponent) {
            is TextArea -> buildTextArea(textInputUIComponent)
            is TextField -> buildTextField(textInputUIComponent)
            is PasswordField -> buildPasswordField(textInputUIComponent)
        }
    }

    private fun buildBinaryStateButton(binaryStateButton: BinaryStateButton) {
        //TODO: Add property for toggle group
        binaryStateButton.selectedProperty.guiListener = { _, _ -> Frontend.updateComponent(binaryStateButton) }
        when (binaryStateButton) {
            is ToggleButton -> buildToggleButton(binaryStateButton)
            is RadioButton -> buildRadioButton(binaryStateButton)
        }
    }

    private fun buildButton(button: Button) {}

    private fun buildCheckBox(checkBox: CheckBox) {
        checkBox.isCheckedProperty.guiListener = { _, _ -> Frontend.updateComponent(checkBox) }
        checkBox.isIndeterminateAllowedProperty.guiListener = { _, _ -> Frontend.updateComponent(checkBox) }
        checkBox.isIndeterminateProperty.guiListener = { _, _ -> Frontend.updateComponent(checkBox) }
    }

    private fun buildLabel(label: Label) {}

    private fun buildComboBox(comboBox: ComboBox<*>) {
        comboBox.observableItemsList.guiListener = { _, _ -> Frontend.updateComponent(comboBox) }
        comboBox.selectedItemProperty.guiListener = { _, _ -> Frontend.updateComponent(comboBox) }
        comboBox.formatFunctionProperty.guiListener = { _, _ -> Frontend.updateComponent(comboBox) }
    }

    private fun buildTextArea(textArea: TextArea) {}

    private fun buildTextField(textField: TextField) {}

    private fun buildPasswordField(passwordField: PasswordField) {}

    private fun buildToggleButton(toggleButton: ToggleButton) {}

    private fun buildRadioButton(radioButton: RadioButton) {}

    private fun buildColorPicker(colorPicker: ColorPicker) {
        colorPicker.selectedColorProperty.guiListener = { _, _ -> Frontend.updateComponent(colorPicker) }
    }

    private fun buildProgressBar(progressBar: ProgressBar) {
        progressBar.progressProperty.guiListener = { _, _ -> Frontend.updateComponent(progressBar) }
        progressBar.barColorProperty.guiListener = { _, _ -> Frontend.updateComponent(progressBar) }
    }

    @Suppress("DuplicatedCode")
    private fun buildStructuredDataView(structuredDataView: StructuredDataView<*>) {
        structuredDataView.items.guiListener = { _, _ -> Frontend.updateComponent(structuredDataView) }
        structuredDataView.selectionModeProperty.guiListener = { _, _ -> Frontend.updateComponent(structuredDataView) }
        structuredDataView.selectionBackgroundProperty.guiListener = { _, _ -> Frontend.updateComponent(structuredDataView) }
        structuredDataView.selectionStyleProperty.guiListener = { _, _ -> Frontend.updateComponent(structuredDataView) }
        /* Internal Properties */
        structuredDataView.selectedItemsList.guiListener = { _, _ -> Frontend.updateComponent(structuredDataView) }
        structuredDataView.selectedIndicesList.guiListener = { _, _ -> Frontend.updateComponent(structuredDataView) }
    }
}

