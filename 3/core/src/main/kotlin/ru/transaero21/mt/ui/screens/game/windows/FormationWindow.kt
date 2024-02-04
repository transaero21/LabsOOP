package ru.transaero21.mt.ui.screens.game.windows

import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.ui.Window
import ktx.scene2d.Scene2DSkin
import ru.transaero21.mt.models.units.fighters.FieldCommander
import ru.transaero21.mt.utils.clickListener

class FormationWindow(
    private val fieldCommanders: MutableMap<Int, FieldCommander>,
    private var commanderSelectedListener: ((Int) -> Unit),
    private val skin: Skin = Scene2DSkin.defaultSkin
): Window(WINDOW_NAME, skin) {
    private val table: Table = Table(skin)
    private val buttonsMap: MutableMap<FieldCommander, TextButton> = mutableMapOf()

    init {
        table.defaults().pad(10f)

        add(table).expand().fill().colspan(2)
        row()

        val closeButton = TextButton("Close", skin)
        closeButton.addListener(clickListener { isVisible = false })
        add(closeButton).padBottom(10f).colspan(2).right().row()

        isMovable = false
        isVisible = false
    }

    fun render() {
        updateButtons()
        pack()
        setSize(400f, 300f)
    }

    private fun updateButtons() {
        buttonsMap.keys.filter { it !in fieldCommanders.values }.forEach { removeButton(it) }

        for ((id, fieldCommander) in fieldCommanders) {
            buttonsMap[fieldCommander]?.setText(fieldCommander.fullName)
                ?: addButton(fieldCommander = fieldCommander, id = id)
        }
    }

    private fun removeButton(fieldCommander: FieldCommander) {
        buttonsMap.remove(fieldCommander)?.remove()
    }

    private fun addButton(fieldCommander: FieldCommander, id: Int) {
        val button = TextButton(fieldCommander.fullName, skin)
        button.addListener(clickListener {
            commanderSelectedListener(id)
            this@FormationWindow.isVisible = false
        })
        buttonsMap[fieldCommander] = button
        table.add(button).left().row()
    }

    companion object {
        private const val WINDOW_NAME = "Select Formation"
    }
}