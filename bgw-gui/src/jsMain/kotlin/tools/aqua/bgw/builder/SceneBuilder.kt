package tools.aqua.bgw.builder

import SceneData
import react.ReactElement
import react.create
import tools.aqua.bgw.elements.Scene as ReactScene

object SceneBuilder {

    fun build(scene: SceneData): ReactElement<*> {
            return ReactScene.create { data = scene }
    }
}