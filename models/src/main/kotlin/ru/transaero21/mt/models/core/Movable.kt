package ru.transaero21.mt.models.core

/**
 * An abstract class representing a movable object with a position defined by its x and y coordinates.
 */
interface Movable {
    /**
     * Current x-coordinate of the movable object.
     */
    var x: Float

    /**
     * Current y-coordinate of the movable object.
     */
    var y: Float

    /**
     * Future x-coordinate of the movable object.
     */
    var futureX: Float?

    /**
     * Future y-coordinate of the movable object.
     */
    var futureY: Float?

    /**
     * Updates the future position of the movable object to the specified coordinates.
     *
     * @param x future x-coordinate of the movable object.
     * @param y future y-coordinate of the movable object.
     * @throws AssertionError if future coordinates do not become current.
     */
    fun updatePosition(x: Float, y: Float) {
        assert(value = futureX == null && futureY == null) {
            "finalizePosition() must be called before each subsequent coordinate update"
        }
        futureX = x
        futureY = y
    }

    /**
     * Finalizes the position of the movable object by updating its current position to the future position.
     */
    fun finalizePosition() {
        futureX?.let { nx ->
            x = nx
            futureX = null
        }
        futureY?.let { ny ->
            y = ny
            futureY = null
        }
    }

    fun getLength(x: Float, y: Float): Float {
        return Calculations.getLength(x1 = this.x, y1 = this.y, x2 = x, y2 = y)
    }

    fun isReached(fx: Float, fy: Float, tx: Float, ty: Float): Boolean {
        return Calculations.isReached(cx = this.x, cy = this.y, fx = fx, fy = fy, tx = tx, ty = ty)
    }

    fun getAngle(x: Float, y: Float): Float {
        return Calculations.getAngle(x1 = this.x, y1 = this.y, x2 = x, y2 = y)
    }
}
