package ru.transaero21.mt.models.ammo.mine

import ru.transaero21.mt.models.ammo.Ammunition

abstract class Mine(
    x: Float, y: Float, angle: Float, velocity: Float
) : Ammunition(x = x, y = y, angle = angle, velocity = velocity) {
    abstract val hitRange: Float
    abstract val defuseTime: Float
    var timePassed: Float = 0F

    var state: MineState = MineState.Transit

    fun defuse(deltaTime: Float, isGoodStep: Boolean): Boolean {
        if (state != MineState.Ready) return false
        timePassed += deltaTime
        return isDefuseTimePassed().also { isOk ->
            if (isOk) state = if (isGoodStep) MineState.Defused else MineState.Exploded
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (distanceCurrent >= distanceMax && state == MineState.Transit)
            state = MineState.Ready
    }

    private fun isDefuseTimePassed() = timePassed >= defuseTime
}
