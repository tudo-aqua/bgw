package tools.aqua.bgw.elements.layoutviews

import tools.aqua.bgw.elements.ElementView
import tools.aqua.bgw.elements.container.GameElementContainerView
import tools.aqua.bgw.observable.IObservable
import tools.aqua.bgw.observable.ObservableArrayList
import tools.aqua.bgw.util.Coordinate
import tools.aqua.bgw.visual.CompoundVisual
import tools.aqua.bgw.visual.Visual

class ElementPane<T : ElementView>(
    height: Number = 0,
    width: Number = 0,
    posX: Number = 0,
    posY: Number = 0,
    visual: Visual = Visual.EMPTY
) :
    LayoutElement<T>(height = height, width = width, posX = posX, posY = posY, visual = visual) , Iterable<T> {

    internal val observableElements : ObservableArrayList<T> = ObservableArrayList()

    /**
     * [ElementView]s that are contained in this [ElementPane].
     */
    var elements: List<T> = observableElements.toList()
        get() = observableElements.toList()
        private set

    /**
     * Adds a [listener] on the [observableElements] list.
     */
    fun addElementsListener(listener: IObservable) {
        observableElements.addListener(listener)
    }

    /**
     * Removes a [listener] from the [observableElements] list.
     */
    fun removeElementsListener(listener: IObservable) {
        observableElements.removeListener(listener)
    }

    /**
     * Removes all [listeners] from the [observableElements] list.
     */
    fun clearElementsListener() {
        observableElements.clearListeners()
    }

    /**
     * Adds an [ElementView] to this [ElementPane].
     *
     * @param element element to add.
     * @throws IllegalArgumentException if [element] is already contained.
     * @throws IllegalArgumentException if [index] is out of bounds for [elements].
     */
    @Suppress("DuplicatedCode")
    @Synchronized
    fun add(element: T, index: Int = observableElements.size) {
        require(!observableElements.contains(element)) {
            "Element $element is already contained in this $this."
        }
        require(element.parent == null) {
            "Element $element is already contained in another container."
        }
        require(index in 0..observableElements.size) {
            "Index $index is out of list range."
        }

        observableElements.add(index, element.apply { parent = this@ElementPane })
    }

    /**
     * Adds all [ElementView]s passed as varargs to this [ElementPane].
     * Whenever an ElementView is encountered, that is already contained, an
     * [IllegalArgumentException] is thrown and no further ElementView is added.
     *
     * @param elements vararg [ElementView]s to add.
     * @throws IllegalArgumentException if an [ElementView] is already contained.
     */
    fun addAll(vararg elements: T) {
        try {
            addAll(elements.toList())
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(e.message)
        }
    }

    /**
     * Adds all [ElementView]s contained in the passed collection to this [ElementPane].
     * Whenever an ElementView is encountered, that is already contained, an
     * [IllegalArgumentException] is thrown and no further [ElementView] is added.
     *
     * @param collection collection containing the [ElementView]s to add.
     * @throws IllegalArgumentException if an [ElementView] is already contained.
     */
    @Synchronized
    fun addAll(collection: Collection<T>) {
        try {
            collection.forEach { add(it) }
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException(e.message)
        }
    }

    /**
     * Removes the [ElementView] specified by the parameter from this [ElementPane].
     *
     * @param element the [ElementView] to remove.
     */
    @Synchronized
    fun remove(element: T) {
        observableElements.remove(element.apply { parent = null })
    }

    /**
     * Removes all [ElementView]s from this [ElementPane].
     * @return list of all removed Elements
     */
    @Synchronized
    fun removeAll(): List<T> {
        val tmp = observableElements.toList()
        observableElements.forEach { it.parent = null }
        observableElements.clear()
        return tmp
    }

    /**
     * Returns the size of the elements list.
     * @see elements
     */
    fun numberOfElements(): Int = observableElements.size

    /**
     * Returns whether the elements list is empty.
     *
     * @return `true` if this list contains no elements, `false` otherwise.
     *
     * @see isNotEmpty
     * @see elements
     */
    fun isEmpty(): Boolean = observableElements.isEmpty()

    /**
     * Returns whether the elements list is not empty.
     *
     * @return `true` if this list contains elements, `false` otherwise.
     *
     * @see isEmpty
     * @see elements
     */
    fun isNotEmpty(): Boolean = !isEmpty()

    /**
     * Returning a contained child's coordinates within this container.
     *
     * @param child child to find.
     *
     * @return coordinate of given child in this container relative to containers anchor point.
     */
    override fun getChildPosition(child: ElementView): Coordinate? = Coordinate(child.posX, child.posY)

    /**
     * Removes [child] from container's children.
     *
     * @param child child to be removed.
     *
     * @throws RuntimeException if the child's type is incompatible with container's type.
     */
    override fun removeChild(child: ElementView) {
        try {
            @Suppress("UNCHECKED_CAST")
            this.remove(child as T)
        } catch (_: ClassCastException) {
            throw RuntimeException("$child type is incompatible with container's type.")
        }
    }

    override fun iterator(): Iterator<T> = observableElements.iterator()
}