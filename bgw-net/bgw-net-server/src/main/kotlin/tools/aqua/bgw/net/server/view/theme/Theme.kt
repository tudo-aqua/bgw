package tools.aqua.bgw.net.server.view.theme

import com.vaadin.flow.component.UI
import com.vaadin.flow.dom.Element
import com.vaadin.flow.dom.ThemeList
import com.vaadin.flow.theme.lumo.Lumo
import tools.aqua.bgw.net.server.entity.observable.Observable

object Theme : Observable() {
    const val DARK_MODE = Lumo.DARK
    const val LIGHT_MODE = Lumo.LIGHT
    private const val DEFAULT = DARK_MODE
    private val root: Element
        get() = UI.getCurrent().element

    fun toggleTheme() {
        when (getCurrent()) {
            DARK_MODE -> setLightMode()
            LIGHT_MODE -> setDarkMode()
        }
        notifyChange()
    }

    fun setDarkMode() = root.themeList.setTheme(DARK_MODE)
    fun setLightMode() = root.themeList.setTheme(LIGHT_MODE)
    fun setDefault() = root.themeList.setTheme(DEFAULT)
    fun getCurrent(): String = root.themeList.getTheme() ?: ""
}

private fun ThemeList.getTheme(): String? = this.firstOrNull()

private fun ThemeList.setTheme(theme: String) {
    clear()
    set(theme, true)
}