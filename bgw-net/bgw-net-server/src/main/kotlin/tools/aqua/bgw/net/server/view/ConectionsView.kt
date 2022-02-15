package tools.aqua.bgw.net.server.view

import com.vaadin.flow.component.Unit
import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.menubar.MenuBar
import com.vaadin.flow.component.orderedlayout.FlexComponent
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.component.page.BodySize
import com.vaadin.flow.component.page.Viewport
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route
import com.vaadin.flow.theme.Theme
import com.vaadin.flow.theme.lumo.Lumo
import org.springframework.beans.factory.annotation.Autowired
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.service.FrontendService

@Route(value = "", layout = MainLayout::class)
@PageTitle("BGW-Net | Active Connections")
class ConnectionsView(@Autowired private val frontendService: FrontendService) : VerticalLayout() {
	private val playerGrid = Grid<Player>().apply {
		addColumn(Player::name).setHeader("Name")
		addColumn { it.session.remoteAddress?.address?.hostAddress ?: "n/a" }.setHeader("IP Address")
		setItems(frontendService.activePlayers)
		minWidth = "400px"
	}

	private val gameGrid = Grid<Game>().apply {
		addColumn(Game::gameID).setHeader("Game ID")
		addColumn(Game::sessionID).setHeader("Session ID")
		val playerRenderer = ComponentRenderer<Grid<Player>, Game> { game ->
			Grid<Player>().apply {
				addColumn(Player::name).setHeader("Name")
				addColumn { it.session.remoteAddress?.address?.hostAddress ?: "n/a" }.setHeader("IP Address")
				setItems(game.players)
			}
		}
		setItemDetailsRenderer(playerRenderer)
		setItems(frontendService.activeGames)
		minWidth = "400px"
	}

	init {
		width = "100%"
		height = "100%"
		add(HorizontalLayout(playerGrid, gameGrid))
	}
}