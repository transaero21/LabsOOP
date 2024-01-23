package ru.transaero21.mt.ui.screens.game.windows

import com.badlogic.gdx.scenes.scene2d.ui.*
import ktx.scene2d.Scene2DSkin
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.utils.clickListener

class FighterWindow(
    private val skin: Skin = Scene2DSkin.defaultSkin
) : Window("Fighter Status", skin) {
    private val table: Table = Table(skin)
    var fighter: Fighter? = null

    init {
        table.defaults().pad(10f)
        add(table).colspan(2).row()

        val closeButton = TextButton("Close", skin)
        closeButton.addListener(clickListener(onClick = { fighter = null }))
        add(closeButton).padBottom(10f).colspan(2).row()

        isMovable = false
        isVisible = false
    }

    fun render() {
        table.clear()

        fighter?.let { f ->
            if (f.healthPercentage > 0f) {
                addFighterStatus(f)
                isVisible = true
            } else {
                fighter = null
            }
        }
        if (fighter == null) isVisible = false

        pack()
        setSize(272f, 192f)
    }

    private fun addFighterStatus(fighter: Fighter) {
        val currentHealth = fighter.healthMax * fighter.healthPercentage

        val statusTable = Table(skin)
        statusTable.add(Label("Full Name: ${fighter.fullName}", skin)).colspan(2).left().row()
        statusTable.add(Label("Health: $currentHealth", skin)).left().row()
        statusTable.add(Label("Speed: ${fighter.speed}", skin)).left().row()
        statusTable.add(Label("Coordinates: (${fighter.x.toInt()}, ${fighter.y.toInt()})", skin)).left().row()
        statusTable.add(Label("Skills: ${fighter.skills.joinToString(separator = ", ")}", skin)).left().row()

        table.add(statusTable).expandX().fillX().row()
    }
}