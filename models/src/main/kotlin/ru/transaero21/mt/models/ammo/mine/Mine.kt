package ru.transaero21.mt.models.ammo.mine

import ru.transaero21.mt.models.ammo.Ammunition

abstract class Mine(
    x: Float, y: Float, angle: Float, velocity: Float
) : Ammunition(x = x, y = y, angle = angle, velocity = velocity) {
    abstract val hitRange: Float
    abstract val defuseTime: Float
    private var timePassed: Float = 0f

    @Volatile var state: MineState = MineState.Transit

    fun defuse(deltaTime: Float): Boolean {
        if (state != MineState.Ready) return false
        timePassed += deltaTime
        return isDefuseTimePassed().also { isOk ->
            if (isOk) state = MineState.Defused
        }
    }

    override fun update(delta: Float) {
        super.update(delta)

        if (distanceCurrent >= distanceMax && state == MineState.Transit)
            state = MineState.Ready
    }

    private fun isDefuseTimePassed() = timePassed >= defuseTime
}
