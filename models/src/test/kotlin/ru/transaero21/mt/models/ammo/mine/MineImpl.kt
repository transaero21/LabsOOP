package ru.transaero21.mt.models.ammo.mine

class MineImpl(
    x: Float, y: Float, angle: Float, velocity: Float
): Mine(x = x, y = y, angle = angle, velocity = velocity) {
    override val hitRange: Float = HIT_RANGE
    override val defuseTime: Float = DEFUSE_TIME
    override val distanceMax: Float = DISTANCE_MAX
    override val damage: Float = DAMAGE

    companion object {
        const val HIT_RANGE = 10F
        const val DEFUSE_TIME = 4F
        const val DISTANCE_MAX = 100F
        const val DAMAGE = 10F
    }
}
