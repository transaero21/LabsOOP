package ru.transaero21.mt.models.ammo.bullet

class BulletImpl(
    x: Float, y: Float, angle: Float, velocity: Float
) : Bullet(x = x, y = y, angle = angle, velocity = velocity) {
    override val distanceMax: Float = 50f
    override val damage: Float = 100f
}