package ru.transaero21.mt.models.weapon

import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.ammo.AmmunitionImpl

class WeaponImpl: Weapon() {
    override val reloadTime: Float = RELOAD_TIME
    override val shotTime: Float = SHOT_TIME
    override val capacity: Int = CAPACITY
    override val velocity: Float = VELOCITY

    override fun createAmmo(x: Float, y: Float, angle: Float): Ammunition {
        return AmmunitionImpl(x = x, y = y, angle = angle, velocity = velocity)
    }

    companion object {
        const val RELOAD_TIME = 2.5F
        const val SHOT_TIME = 0.2F
        const val CAPACITY = 30
        const val VELOCITY = 1000F
    }
}
