package tools.aqua.bgw.net.server.view

import com.vaadin.flow.component.Key
import com.vaadin.flow.component.button.Button
import com.vaadin.flow.component.button.ButtonVariant
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.formlayout.FormLayout
import com.vaadin.flow.component.html.Label
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.textfield.TextField
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

@Route(value = "secret", layout = MainLayout::class)
@PageTitle("BGW-Net | SoPra Secret")
class SoPraSecretForm: FormLayout() {
	private val secretLabel: Label = Label("Old SoPra Secret").apply {
		addClassName("secret-form")
	}
	private val secretField: TextField = TextField("New SoPra Secret").apply {
		addClassName("secret-form")
	}

	private val save: Button = Button("Save").apply {
		addThemeVariants(ButtonVariant.MATERIAL_OUTLINED)
		addClassName("secret-form")
	}

	init {
		addClassName("secret-form")
		width = "400px"
		add(
			secretLabel,
			secretField,
			save
		)
	}
}