package mapper

import DialogData
import tools.aqua.bgw.animation.Animation
import tools.aqua.bgw.dialog.Dialog

object DialogMapper {
    fun map(dialog: Dialog) : DialogData {
        return DialogData().apply {
            dialogType = dialog.dialogType
            title = dialog.title
            header = dialog.header
            message = dialog.message
            exception = dialog.exception.message ?: ""
        }
    }
}