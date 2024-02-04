package ru.transaero21.mt.models.ammo.bullet

class BigBullet(
    x: Float, y: Float, angle: Float, velocity: Float
) : Bullet(x = x, y = y, angle = angle, velocity = velocity) {
    override val distanceMax: Float = 300F
    override val damage: Float = 20F
}