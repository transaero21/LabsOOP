package ru.transaero21.mt.ui.screens.game.windows

import com.badlogic.gdx.scenes.scene2d.ui.*
import ktx.scene2d.*
import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.units.managers.Staff

class StaffStatusWindow(
    private val staffArray: OrderedMap<Int, Staff>,
    private val skin: Skin = Scene2DSkin.defaultSkin
) : Window("Staff Status", skin) {
    private val staffTable: Table = Table(skin)

    init {
        staffTable.defaults().pad(10f)

        for ((_, staff) in staffArray) {
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
        for ((_, staff) in staffArray) {
            addStaffStatus(staff)
        }
        pack()
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

        staffTable.add(statusTable).expandX().fillX().row()
    }
}
