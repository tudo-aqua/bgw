package tools.aqua.bgw.components

import tools.aqua.bgw.observable.lists.ObservableList


/**
 * An interface that describes a container which can hold [ComponentView] that can be layered.
 */
interface  LayeredContainer <T : ComponentView>{

    /**
     * an [ObservableList] that is contained in the [LayeredContainer]
     */
    val children : ObservableList<T>

    /**
     * Puts the [component] to the front inside the [LayeredContainer].
     *
     * @param component Child that is moved to the front.
     */
    fun toFront(component: T){
        if (children.last()  != component && children.contains(component)) {
            children.removeSilent(component)
            children.add(component)
        }
    }

    /**
     * Puts the [component] to the back inside the [LayeredContainer].
     *
     * @param component Child that is moved to the back.
     */
    fun toBack(component: T) {
        if (children.first() != component && children.contains(component)) {
            children.removeSilent(component)
            children.add(0,component)
        }
    }

}