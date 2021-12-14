package tools.aqua.bgw.net.server.view

import com.vaadin.flow.component.grid.Grid
import com.vaadin.flow.component.orderedlayout.HorizontalLayout
import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.data.renderer.ComponentRenderer
import com.vaadin.flow.router.Route
import org.springframework.beans.factory.annotation.Autowired
import tools.aqua.bgw.net.server.entity.Game
import tools.aqua.bgw.net.server.entity.Player
import tools.aqua.bgw.net.server.service.FrontendService

@Route
class MainView(@Autowired private val frontendService: FrontendService) : VerticalLayout() {
	private val playerGrid = Grid<Player>().apply {
		addColumn(Player::name).setHeader("Name")
		addColumn { it.session.remoteAddress?.address?.hostAddress ?: "n/a" }.setHeader("IP Address")
		setItems(frontendService.activePlayers)
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
	}

	init {
		add(HorizontalLayout(playerGrid, gameGrid).apply { setSizeFull() })
		setSizeFull()
	}
}