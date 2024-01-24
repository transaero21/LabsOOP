package ru.transaero21.mt.models.ammo

import ru.transaero21.mt.models.core.Movable
import kotlin.math.cos
import kotlin.math.sin

/**
 * An abstract class representing an ammunition with properties and methods for movement and updating.
 *
 * @property angle firing angle of the ammunition.
 * @property velocity velocity of the ammunition.
 */
abstract class Ammunition(
    override var x: Float,
    override var y: Float,
    val angle: Float,
    val velocity: Float,
): Movable, Comparable<Ammunition> {
    override var futureX: Float? = null

    override var futureY: Float? = null

    /**
     * Maximum distance the ammunition can travel.
     */
    abstract val distanceMax: Float

    /**
     * Current distance traveled by the ammunition.
     */
    var distanceCurrent: Float = 0F
        private set

    /**
     * Damage caused by the ammunition.
     */
    abstract val damage: Float

    /**
     * Updates the position of the ammunition based on the time passed.
     *
     * @param delta time passed since the last update, in seconds.
     */
    open fun update(delta: Float) {
        if (distanceCurrent < distanceMax) {
            var (newX, newY) = getNewX(delta = delta) to getNewY(delta = delta)
            var distanceDiff = getLength(x = newX, y = newY)
            val distanceOver = distanceDiff + distanceCurrent - distanceMax
            if (distanceOver > 0) {
                distanceDiff -= distanceOver
                newX -= distanceOver * cos(x = angle)
                newY -= distanceOver * sin(x = angle)
            }
            distanceCurrent += distanceDiff
            updatePosition(x = newX, y = newY)
        }
    }

    /**
     * Calculates the new x-coordinate based on the time passed.
     *
     * @param delta time passed since the last update, in seconds.
     * @return new x-coordinate of the ammunition.
     */
    private fun getNewX(delta: Float) = cos(x = angle) * velocity * delta + x

    /**
     * Calculates the new y-coordinate based on the time passed.
     *
     * @param delta time passed since the last update, in seconds.
     * @return new y-coordinate of the ammunition.
     */
    private fun getNewY(delta: Float) = sin(x = angle) * velocity * delta + y

    /**
     * Compares this ammunition with another based on their position, angle, and velocity.
     *
     * @param other the other ammunition to compare to.
     * @return -1 if this ammunition is "less than" the other, 0 if they are "equal", and 1 if this ammunition is "greater than" the other.
     */
    override fun compareTo(other: Ammunition): Int {
        return when {
            x < other.x -> -1
            x > other.x -> 1
            y < other.y -> -1
            y > other.y -> 1
            angle < other.angle -> -1
            angle > other.angle -> 1
            velocity < other.velocity -> -1
            velocity > other.velocity -> 1
            else -> 0
        }
    }
}
