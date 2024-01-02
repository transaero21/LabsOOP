package ru.transaero21.mt.models.ammo.bullet

class BulletImpl(
    x: Float, y: Float, angle: Float, velocity: Float
) : Bullet(x = x, y = y, angle = angle, velocity = velocity) {
    override val distanceMax: Float = DISTANCE_MAX
    override val damage: Float = DAMAGE

    companion object {
        const val DISTANCE_MAX = 1000F
        const val DAMAGE = 100F
    }
}