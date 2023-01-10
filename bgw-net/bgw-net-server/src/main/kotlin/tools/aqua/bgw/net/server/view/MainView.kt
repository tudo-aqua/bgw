package tools.aqua.bgw.net.server.view

import com.vaadin.flow.component.orderedlayout.VerticalLayout
import com.vaadin.flow.router.PageTitle
import com.vaadin.flow.router.Route

/** Default view for first login redirect */
@Route(value = "", layout = MainLayout::class)
@PageTitle("BGW-Net")
class MainView() : VerticalLayout() {
}