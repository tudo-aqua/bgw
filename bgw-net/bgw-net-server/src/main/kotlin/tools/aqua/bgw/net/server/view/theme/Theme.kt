package tools.aqua.bgw.net.server.view.theme

import com.vaadin.flow.component.UI
import com.vaadin.flow.dom.Element
import com.vaadin.flow.dom.ThemeList
import tools.aqua.bgw.net.server.DARK_THEME
import tools.aqua.bgw.net.server.DEFAULT_THEME
import tools.aqua.bgw.net.server.LIGHT_THEME
import tools.aqua.bgw.net.server.entity.observable.Observable

object Theme : Observable<String>(DEFAULT_THEME) {
    private val root: Element
        get() = UI.getCurrent().element

    init {
        onChange { root.setTheme(it) }
        root.addAttachListener {
            if (root.getTheme().isEmpty()) value = DEFAULT_THEME
        }
    }

    fun toggle() {
        when (value) {
            DARK_THEME -> root.setTheme(LIGHT_THEME)
            LIGHT_THEME -> root.setTheme(DARK_THEME)
        }
    }

    fun getCurrent(): String = value
}

private fun Element.getTheme(): String = themeList.getTheme() ?: ""

private fun Element.setTheme(theme: String) = themeList.setTheme(theme)

private fun ThemeList.getTheme(): String? = this.firstOrNull()

private fun ThemeList.setTheme(theme: String) {
    clear()
    set(theme, true)
}