package ru.transaero21.mt.ui.screens.menu.windows

import ktx.scene2d.*
import ru.transaero21.mt.ui.screens.menu.MenuWindow
import ru.transaero21.mt.utils.*

fun RootWidget.joinGameWindow(
    onBackClick: () -> Unit,
    onJoinClick: (ipAddress: String, port: String) -> Unit
) = window(title = MenuWindow.JoinGame.title) {
    var ipAddress = NetworkUtils.DEFAULT_IP_ADDRESS
    var port = NetworkUtils.DEFAULT_PORT
    verticalGroup {
        space(12F)
        table {
            columnDefaults(2)
            label(text = "IP Address:") { cell ->
                cell.colspan(1).right().padRight(16f)
            }
            textField(text = ipAddress) { cell ->
                cell.colspan(1).left()
                setTextFieldListener { _, _ -> ipAddress = this.text }
            }
            row()
            label(text = "Port:") { cell ->
                cell.colspan(1).right().padRight(16f)
            }
            textField(text = port) { cell ->
                cell.colspan(1).left()
                setTextFieldListener { _, _ -> port = this.text }
            }
        }
        horizontalGroup {
            space(16F)
            textButton(text = "Back") {
                onClick { onBackClick() }
            }
            textButton(text = "Join") {
                onClick { onJoinClick(ipAddress, port) }
            }
        }
    }


    pack()
    this.isMovable = false
    this.setSize(280F, 152F)
}