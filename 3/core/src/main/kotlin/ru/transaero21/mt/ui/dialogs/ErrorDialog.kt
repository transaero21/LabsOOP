package ru.transaero21.mt.ui.dialogs

import com.badlogic.gdx.utils.Align
import ktx.scene2d.StageWidget
import ktx.scene2d.dialog
import ktx.scene2d.label

private const val MIN_WIDTH = 190f
private const val MIN_HEIGHT = 140f

fun StageWidget.errorDialog(errorMsg: String = "Terrible Error!") = dialog(title = "Error") {
    val label = label(errorMsg).apply {
        setWrap(true)
        setAlignment(Align.center)
    }
    contentTable.add(label).width(300f).pad(20f).row()
    button("Close")
    setSize(prefWidth.coerceAtLeast(MIN_WIDTH), prefHeight.coerceAtLeast(MIN_HEIGHT))
}
