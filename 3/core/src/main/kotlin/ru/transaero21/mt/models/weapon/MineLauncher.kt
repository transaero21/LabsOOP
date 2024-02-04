package ru.transaero21.mt.models.weapon

import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.ammo.mine.BasicMine

class MineLauncher: Weapon() {
    override val reloadTime: Float = 10F
    override val shotTime: Float = 2F
    override val capacity: Int = 10
    override val velocity: Float = 40F

    override fun createAmmo(x: Float, y: Float, angle: Float): Ammunition {
        return BasicMine(x = x, y = y, angle = angle, velocity = velocity)
    }
}
