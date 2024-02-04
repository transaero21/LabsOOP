package ru.transaero21.mt.models.weapon

import ru.transaero21.mt.models.ammo.Ammunition

abstract class Weapon {
    abstract val reloadTime: Float
    abstract val shotTime: Float

    abstract val capacity: Int
    abstract val velocity: Float

    var capacityCurrent: Int = 0
        private set
    private var timePassed: Float = 0F
    private var timeOver: Float = 0F
    var state: WeaponState = WeaponState.Ready
        private set

    fun update(delta: Float) {
        timeOver = 0F
        when (state) {
            WeaponState.Ready -> {
                if (capacityCurrent <= 0) {
                    state = WeaponState.Reloading
                    update(delta = delta)
                }
            }

            WeaponState.Reloading -> {
                timePassed += delta
                if (capacityCurrent > 0) {
                    if (timePassed >= shotTime) finalizeReloading()
                } else {
                    if (timePassed >= reloadTime) finalizeReloading()
                }
            }
        }
    }


    private fun finalizeReloading() {
        if (capacityCurrent <= 0) {
            capacityCurrent = capacity
            timeOver = timePassed - reloadTime
        } else timeOver = timePassed - shotTime
        timePassed = 0F
        state = WeaponState.Ready
    }

    fun fire(x: Float, y: Float, angle: Float): Ammunition? {
        val isReady = state == WeaponState.Ready && capacityCurrent > 0
        return if (isReady) shoot(x = x, y = y, angle = angle) else null
    }

    private fun shoot(x: Float, y: Float, angle: Float): Ammunition {
        capacityCurrent--
        state = WeaponState.Reloading
        timePassed = timeOver
        timeOver = 0F
        return createAmmo(x = x, y = y, angle = angle)
    }

    abstract fun createAmmo(x: Float, y: Float, angle: Float): Ammunition
}
