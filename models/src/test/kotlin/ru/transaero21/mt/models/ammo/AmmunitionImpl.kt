package ru.transaero21.mt.models.ammo

class AmmunitionImpl(x: Float, y: Float, angle: Float, velocity: Float) : Ammunition(x, y, angle, velocity) {
    override val distanceMax: Float = 100f
    override val damage: Float = 2f
}
