package ru.transaero21.mt.ui.screens.menu.windows

import com.badlogic.gdx.Gdx
import ktx.scene2d.RootWidget
import ktx.scene2d.table
import ktx.scene2d.textButton
import ktx.scene2d.window
import ru.transaero21.mt.ui.screens.menu.MenuWindow
import ru.transaero21.mt.utils.onClick

fun RootWidget.mainMenuWindow(
    onCreateRoomClicked: () -> Unit,
    onJoinRoomClicked: () -> Unit
) = window(title = MenuWindow.MainMenu.title) {
    table {
        row()
        textButton(text = MenuWindow.CreateGame.title) {
            onClick { onCreateRoomClicked() }
        }
        row()
        textButton(text = MenuWindow.JoinGame.title) {
            onClick { onJoinRoomClicked() }
        }
        row()
        textButton(text = "Exit") {
            onClick { Gdx.app.exit() }
        }
    }

    pack()
    this.isMovable = false
    this.setSize(150F, 150F)
}