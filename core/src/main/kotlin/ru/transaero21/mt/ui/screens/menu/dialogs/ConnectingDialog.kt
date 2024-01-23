package ru.transaero21.mt.ui.screens.menu.dialogs

import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.scene2d.*

private const val MIN_RETRIES = 2
private const val MIN_VALUE = 0.2f
private const val MID_VALUE = 0.4f
private const val MAX_VALUE = 1f

fun StageWidget.connectingDialog(
    isConnecting: () -> Boolean
) = dialog(title = "Connecting") {
    var value = MIN_VALUE
    var retries = 0
    contentTable.add(
        progressBar {
            KtxAsync.launch {
                do {
                    retries++
                    value = if (value != MIN_VALUE) MIN_VALUE else MID_VALUE
                    setValue(value)
                    setAnimateDuration(0.25f)
                    delay(250)
                } while (isConnecting() || retries < MIN_RETRIES)
                setValue(MAX_VALUE)
                setAnimateDuration(0.2f)
                delay(500)
                this@dialog.hide()
            }
        }
    )
    buttonTable.remove()
    setSize(96F, 96F)
}
