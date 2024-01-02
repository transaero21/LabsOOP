package ru.transaero21.mt.ammo.mine

import ru.transaero21.mt.ammo.Ammunition

abstract class Mine(
    x: Float, y: Float, angle: Float, velocity: Float
) : Ammunition(x = x, y = y, angle = angle, velocity = velocity) {
    abstract val hitRange: Float
    abstract val defuseTime: Float
    var timePassed: Float = 0F

    var state: MineState = MineState.TRANSIT

    fun defuse(deltaTime: Float, isGoodStep: Boolean): Boolean {
        if (state != MineState.READY) return false
        timePassed += deltaTime
        return isDefuseTimePassed().also { isOk ->
            if (isOk) state = if (isGoodStep) MineState.DEFUSED else MineState.EXPLODED
        }
    }

    override fun update(deltaTime: Float) {
        super.update(deltaTime)

        if (distanceCurrent >= distanceMax && state == MineState.TRANSIT)
            state = MineState.READY
    }

    private fun isDefuseTimePassed() = timePassed >= defuseTime
}
