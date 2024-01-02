package ru.transaero21.mt.ammo

import ru.transaero21.mt.core.Movable
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

abstract class Ammunition(
    x: Float, y: Float,
    val angle: Float,
    val velocity: Float
): Movable(x = x, y = y) {
    abstract val distanceMax: Float
    var distanceCurrent: Float = 0F
        private set

    abstract val damage: Float

    open fun update(deltaTime: Float) {
        if (distanceCurrent < distanceMax) {
            var (newX, newY) = getNewX(deltaTime = deltaTime) to getNewY(deltaTime = deltaTime)
            var distanceDiff = sqrt(x = (x - newX).pow(n = 2) + (y - newY).pow(n = 2))
            val distanceOver = distanceDiff + distanceCurrent - distanceMax
            if (distanceOver > 0) {
                distanceDiff -= distanceOver
                newX -= distanceOver * cos(x = angle)
                newY -= distanceOver * sin(x = angle)
            }
            distanceCurrent += distanceDiff
            x = newX
            y = newY
        }
    }

    private fun getNewX(deltaTime: Float) = cos(x = angle) * velocity * deltaTime + x
    private fun getNewY(deltaTime: Float) = sin(x = angle) * velocity * deltaTime + y
}
