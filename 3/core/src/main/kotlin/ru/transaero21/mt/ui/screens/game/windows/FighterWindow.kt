package ru.transaero21.mt.ui.screens.game.windows

import com.badlogic.gdx.scenes.scene2d.ui.*
import ktx.scene2d.Scene2DSkin
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.utils.clickListener

class FighterWindow(
    private val skin: Skin = Scene2DSkin.defaultSkin
) : Window(WINDOW_NAME, skin) {
    private val table: Table = Table(skin)
    var fighter: Fighter? = null

    init {
        table.defaults().pad(10f)
        add(table).colspan(2).row()

        val closeButton = TextButton("Close", skin)
        closeButton.addListener(clickListener(onClick = { fighter = null }))
        add(closeButton).padTop(10f).colspan(2).row()

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
        setSize(272f, 258f)
    }

    private fun addFighterStatus(fighter: Fighter) {
        val statusTable = Table(skin).apply {
            add(Label("Full Name: ${fighter.fullName}", skin)).left().row()
            add(Label("Type: ${fighter::class.simpleName ?: "FieldCommander"}", skin)).left().row()
            add(Label("Health: ${fighter.healthMax * fighter.healthPercentage}", skin)).left().row()
            add(Label("Speed: ${fighter.speed}", skin)).left().row()
            add(Label("Coordinates: (${fighter.x.toInt()}, ${fighter.y.toInt()})", skin)).left().row()
            add(Label("Skills: ${fighter.skills.joinToString(separator = ", ") { it.skillTag }}", skin)).left().row()
            add().height(6f).row()
            add(Label("Current Instruction: ${fighter.getCurrentInstructions()?.let { it::class.simpleName } ?: "None"}", skin)).left().row()
            add(Label("Instructions Left: ${fighter.instrSize}", skin)).left().row()
            add().height(6f).row()
            add(Label("Weapon: ${fighter.weapon::class.simpleName}", skin)).left().row()
            add(Label("Capacity: ${fighter.weapon.capacityCurrent}/${fighter.weapon.capacity}", skin)).left().row()
        }
        table.add(statusTable).expandX().fillX().row()
    }

    companion object {
        private const val WINDOW_NAME = "Fighter Status"
    }
}
