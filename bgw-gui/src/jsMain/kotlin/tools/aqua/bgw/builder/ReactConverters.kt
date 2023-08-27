package tools.aqua.bgw.builder

import ID
import data.event.KeyEventAction
import data.event.KeyEventData
import data.event.MouseEventData
import org.w3c.dom.events.Event
import react.dom.events.KeyboardEvent
import tools.aqua.bgw.event.KeyCode
import tools.aqua.bgw.event.MouseButtonType
import  react.dom.events.MouseEvent as ReactMouseEvent
import  react.dom.events.KeyboardEvent as ReactKeyEvent
import tools.aqua.bgw.event.MouseEvent

object ReactConverters {
    fun ReactMouseEvent<*, *>.toMouseEventData(targetID: ID?): MouseEventData {
        return MouseEventData(
            when (button) {
                0 -> MouseButtonType.LEFT_BUTTON
                1 -> MouseButtonType.MOUSE_WHEEL
                2 -> MouseButtonType.RIGHT_BUTTON
                3, 4 -> MouseButtonType.OTHER
                else -> MouseButtonType.UNSPECIFIED
            },
            clientX,
            clientY
        ).apply { this.id = targetID }
    }

    fun ReactKeyEvent<*>.toKeyEventData(targetID: ID?, action: KeyEventAction): KeyEventData {
        return KeyEventData(
            toKeyCode(),
            key,
            ctrlKey,
            shiftKey,
            altKey,
            action
        ).apply { this.id = targetID }
    }

    private fun ReactKeyEvent<*>.toKeyCode(): KeyCode {
        KeyCode.values().forEach {
            if (it.name == this.key) return it
        }
        return KeyCode.UNDEFINED
    }
}

