package tools.aqua.bgw.net.server.view

import com.vaadin.flow.component.applayout.AppLayout
import com.vaadin.flow.component.applayout.DrawerToggle
import com.vaadin.flow.component.dependency.CssImport
import com.vaadin.flow.component.html.H1
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.HighlightConditions
import com.vaadin.flow.router.RouterLink
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.material.Material

@CssImport("./styles/styles.css")
@Theme(Material::class, variant = Material.LIGHT)
class MainLayout : AppLayout() {
	init {
		createHeader()
		createDrawer()
	}

	private fun createHeader() {
		val logo = H1("BGW Net")
		logo.addClassName("logo")
		val header = HorizontalLayout(DrawerToggle(), logo)
		header.width = "100%"
		header.defaultVerticalComponentAlignment = FlexComponent.Alignment.CENTER
		addToNavbar(header)
	}

	private fun createDrawer() {
		val connectionsLink = RouterLink("Connections and Games", ConnectionsView::class.java).apply {
			highlightCondition = HighlightConditions.sameLocation()
		}
		val secretLink = RouterLink("SoPra Secret", SoPraSecretForm::class.java).apply {
			highlightCondition = HighlightConditions.sameLocation()
		}

		addToDrawer(VerticalLayout(connectionsLink, secretLink))
	}
}