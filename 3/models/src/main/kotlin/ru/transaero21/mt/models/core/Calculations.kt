package ru.transaero21.mt.models.core

import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * A utility object for performing various geometric calculations.
 */
object Calculations {
    /**
     * Checks if something has reached or surpassed the target coordinates while moving from the current
     * coordinates to the future coordinates.
     *
     * @param cx x-coordinate of the current position.
     * @param cy y-coordinate of the current position.
     * @param fx x-coordinate of the future position.
     * @param fy y-coordinate of the future position.
     * @param tx x-coordinate of the target position.
     * @param ty y-coordinate of the target position.
     * @return true if the person has reached or surpassed the target coordinates, false otherwise.
     */
    fun isReached(cx: Float, cy: Float, fx: Float, fy: Float, tx: Float, ty: Float): Boolean {
        return (tx in cx..fx) || (tx in fx..cx) || (ty in cy..fy) || (ty in fy..cy)
    }

    /**
     * Calculates the angle between two vectors defined by (x1, y1) and (x2, y2).
     *
     * @param x1 x-coordinate of the first vector.
     * @param y1 y-coordinate of the first vector.
     * @param x2 x-coordinate of the second vector.
     * @param y2 y-coordinate of the second vector.
     * @return angle between the two vectors in radians.
     */
    fun getAngle(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        var angle = atan2(y = y2 - y1, x = x2 - x1)
        if (angle < 0) angle += 2 * PI.toFloat()
        return angle
    }

    /**
     * Calculates the length of a line segment defined by two points (x1, y1) and (x2, y2).
     *
     * @param x1 x-coordinate of the first point.
     * @param y1 y-coordinate of the first point.
     * @param x2 x-coordinate of the second point.
     * @param y2 y-coordinate of the second point.
     * @return length of the line segment.
     */
    fun getLength(x1: Float = 0f, y1: Float = 0f, x2: Float, y2: Float): Float {
        return sqrt(x = (x1 - x2).pow(n = 2) + (y1 - y2).pow(n = 2))
    }

    /**
     * Checks if a circle with center (x, y) and radius r intersects with a rectangle defined by (x1, y1) and (x2, y2).
     *
     * @param x1 x-coordinate of the first corner of the rectangle.
     * @param y1 y-coordinate of the first corner of the rectangle.
     * @param x2 x-coordinate of the second corner of the rectangle.
     * @param y2 y-coordinate of the second corner of the rectangle.
     * @param x x-coordinate of the center of the circle.
     * @param y y-coordinate of the center of the circle.
     * @param r radius of the circle.
     * @return true if the circle intersects with the rectangle, false otherwise.
     */
    fun circleIntersectRect(x1: Float, y1: Float, x2: Float, y2: Float, x: Float, y: Float, r: Float): Boolean {
        if (y < y1) {
            if (x < x1)
                return ((x - x1) * (x - x1) + (y - y1) * (y - y1)) <= r.pow(n = 2)
            if (x > x2)
                return ((x - x2) * (x - x2) + (y - y1) * (y - y1)) <= r.pow(n = 2)
            return (y1 - y) <= r
        }
        if (y > y2) {
            if (x < x1)
                return ((x - x1) * (x - x1) + (y - y2) * (y - y2)) <= r.pow(n = 2)
            if (x > x2)
                return ((x - x2) * (x - x2) + (y - y2) * (y - y2)) <= r.pow(n = 2)
            return (y - y2) <= r
        }
        if (x <= x1)
            return (x1 - x) <= r
        if (x >= x2)
            return (x - x2) <= r
        return true
    }
}
