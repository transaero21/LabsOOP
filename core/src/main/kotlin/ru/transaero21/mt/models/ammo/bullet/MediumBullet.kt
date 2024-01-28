package ru.transaero21.mt.models.ammo.bullet

class MediumBullet(
    x: Float, y: Float, angle: Float, velocity: Float
) : Bullet(x = x, y = y, angle = angle, velocity = velocity) {
    override val distanceMax: Float = 600F
    override val damage: Float = 1F
}