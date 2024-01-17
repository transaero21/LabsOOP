package ru.transaero21.mt.models.core

import kotlin.math.PI
import kotlin.test.Test
import kotlin.test.assertEquals

class MovableTest {
    private val movable: Movable = MovableImpl()

    @Test
    fun updatePositionTest() {
        movable.updatePosition(10f, 20f)
        assertEquals(expected = 10f, actual = movable.futureX)
        assertEquals(expected = 20f, actual = movable.futureY)
    }

    @Test
    fun finalizePositionTest() {
        movable.updatePosition(10f, 20f)
        movable.finalizePosition()
        assertEquals(expected = 10f, actual = movable.x)
        assertEquals(expected = 20f, actual = movable.y)
        assertEquals(expected = null, actual = movable.futureX)
        assertEquals(expected = null, actual = movable.futureY)
    }

    @Test
    fun getLengthTest() {
        val floatTolerance = 0.001f
        assertEquals(expected = 5f, actual = movable.getLength(3f, 4f), absoluteTolerance = floatTolerance)
    }

    @Test
    fun isReachedTest() {
        movable.updatePosition(x = 10f, y = 20f)
        val reached = movable.isReached(fx = 3f, fy = 4f, tx = 5f, ty = 6f)
        assertEquals(false, reached)
    }

    @Test
    fun getAngleTest() {
        val floatTolerance = 0.001f
        assertEquals(expected = PI.toFloat() / 4, actual = movable.getAngle(1f, 1f), absoluteTolerance = floatTolerance)
    }
}
