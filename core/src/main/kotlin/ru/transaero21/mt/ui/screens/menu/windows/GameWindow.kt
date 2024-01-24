package ru.transaero21.mt.ui.screens.menu.windows

import ktx.scene2d.RootWidget
import ktx.scene2d.textButton
import ktx.scene2d.verticalGroup
import ktx.scene2d.window
import ru.transaero21.mt.ui.screens.menu.MenuWindow
import ru.transaero21.mt.utils.onClick

fun RootWidget.gameWindow(
    goBack: () -> Unit
) = window(title = MenuWindow.Game.title) {
    verticalGroup {
        textButton(text = "Back") {
            onClick { goBack() }
        }
    }

    pack()
    this.isMovable = false
    this.setSize(386F, 184F)
}
