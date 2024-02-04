package ru.transaero21.mt.models.ammo.mine

class MineImpl(
    x: Float, y: Float, angle: Float, velocity: Float
): Mine(x = x, y = y, angle = angle, velocity = velocity) {
    override val hitRange: Float = 5f
    override val defuseTime: Float = 3f
    override val distanceMax: Float = 10f
    override val damage: Float = 1f
}
