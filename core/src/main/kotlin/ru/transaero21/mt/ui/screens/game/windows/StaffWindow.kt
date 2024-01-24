package ru.transaero21.mt.ui.screens.game.windows

import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Skin
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.Window
import ktx.scene2d.Scene2DSkin
import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.units.managers.Staff

class StaffWindow(
    private val staffMap: OrderedMap<Int, Staff>,
    private val skin: Skin = Scene2DSkin.defaultSkin
) : Window(WINDOW_NAME, skin) {
    private val staffTable: Table = Table(skin)

    init {
        staffTable.defaults().pad(10f)

        for ((_, staff) in staffMap) {
            addStaffStatus(staff)
        }

        add(staffTable).expand().fill().colspan(2)
        row()

        pack()
        isMovable = false
        isVisible = false
    }

    fun render() {
        if (!isVisible) isVisible = true
        staffTable.clear()
        for ((_, staff) in staffMap) {
            addStaffStatus(staff)
        }
        pack()
        width = 200f
    }

    private fun addStaffStatus(staff: Staff) {
        val nameLabel = Label(staff.fullName, skin)
        val completionLabel = Label("Completion: ${staff.current}/${staff.orders.size}", skin)
        val efficiencyLabel = Label("Efficiency: ${staff.efficiency * 100}%", skin)
        val timeLeftLabel = Label("Time Left: ${staff.approximateRemainingTime}", skin)

        val statusTable = Table(skin)
        statusTable.add(nameLabel).left().row()
        statusTable.add(completionLabel).left().row()
        statusTable.add(efficiencyLabel).left().row()
        statusTable.add(timeLeftLabel).left()

        staffTable.add(statusTable).left().row()
    }

    companion object {
        private const val WINDOW_NAME = "Staff Status"
    }
}
