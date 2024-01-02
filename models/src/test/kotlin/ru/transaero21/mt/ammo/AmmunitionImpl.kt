package ru.transaero21.mt.ammo

class AmmunitionImpl(x: Float, y: Float, angle: Float, velocity: Float) : Ammunition(x, y, angle, velocity) {
    override val distanceMax: Float = DISTANCE_MAX
    override val damage: Float = DAMAGE

    companion object {
        const val DISTANCE_MAX = 10000F
        const val DAMAGE = 100F
    }
}
