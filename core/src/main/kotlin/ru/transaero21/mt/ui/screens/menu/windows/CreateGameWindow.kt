package ru.transaero21.mt.ui.screens.menu.windows

import com.badlogic.gdx.scenes.scene2d.Touchable
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.utils.Align
import ktx.scene2d.*
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.ui.screens.menu.MenuWindow
import ru.transaero21.mt.utils.NetworkUtils
import ru.transaero21.mt.utils.onClick

fun RootWidget.createGameWindow(
    onBackClicked: () -> Unit,
    onCreateClicked: (port: String, size: WorldSize) -> Unit
) = window(title = MenuWindow.CreateGame.title) {
    var port = NetworkUtils.DEFAULT_PORT
    var worldSize = WorldSize.Medium
    var worldSizeLabel: Label? = null
    var worldSizeMinus: TextButton? = null
    var worldSizePlus: TextButton? = null
    val updateMapSize = { isMinus: Boolean ->
        var current = WorldSize.entries.indexOf(worldSize)
        if (isMinus) current-- else current++
        worldSize = WorldSize.entries[current]
        worldSizeLabel?.setText(worldSize.name)
        worldSizeMinus?.touchable = if (current > 0) Touchable.enabled else Touchable.disabled
        worldSizePlus?.touchable = if (current < WorldSize.entries.size - 1) Touchable.enabled else Touchable.disabled
    }

    verticalGroup {
        space(12F)
        table {
            columnDefaults(4)

            label(text = "Port:") { cell ->
                cell.colspan(1).right().padRight(16f)
            }
            textField(text = port) { cell ->
                cell.colspan(3)
                setTextFieldListener { _, _ -> port = this.text }
            }
            row()
            label(text = "World size:") { cell ->
                cell.colspan(1).right().padRight(16f)
            }
            worldSizeMinus = textButton("-") { cell ->
                cell.colspan(1).width(cell.prefHeight).align(Align.left)
                onClick { updateMapSize(true) }
            }
            worldSizeLabel = label(worldSize.name) { cell ->
                cell.colspan(1)
            }
            worldSizePlus = textButton("+") { cell ->
                cell.colspan(1).width(cell.prefHeight).align(Align.right)
                onClick { updateMapSize(false) }
            }
        }
        horizontalGroup {
            space(16F)
            textButton(text = "Back") {
                onClick { onBackClicked() }
            }
            textButton(text = "Create") {
                onClick { onCreateClicked(port, worldSize) }
            }
        }
    }

    pack()
    this.isMovable = false
    this.setSize(280F, 152F)
}