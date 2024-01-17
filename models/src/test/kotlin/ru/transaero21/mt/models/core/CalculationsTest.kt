package ru.transaero21.mt.models.core

import org.junit.jupiter.api.Test
import kotlin.math.PI
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CalculationsTest {
    @Test
    fun isReachedTest() {
        // Goal not achieved
        assertFalse(Calculations.isReached(cx = -1.41f, cy = 0f, fx = -1f, fy = 5f, tx = 0f, ty = 17.2f))

        // Goal achieved
        assertTrue(Calculations.isReached(cx = 0f, cy = 3.25f, fx = 0.58f, fy = 0f, tx = 0.3f, ty = 0.157f))
    }

    @Test
    fun getAngleTest() {
        val floatTolerance = 0.001f

        // Random movement perpendicular to the horizon
        assertEquals(
            expected = PI.toFloat() / 2,
            actual = Calculations.getAngle(x1 = 2f, y1 = 2f, x2 = 2f, y2 = 4f),
            absoluteTolerance = floatTolerance
        )

        // No movement
        assertEquals(
            expected = 0f,
            actual = Calculations.getAngle(x1 = 2f, y1 = 2f, x2 = 2f, y2 = 2f),
            absoluteTolerance = floatTolerance
        )
    }

    @Test
    fun getLengthTest() {
        val floatTolerance = 0.001f

        // From smaller coordinates to larger ones
        assertEquals(
            expected = 18.246f,
            actual = Calculations.getLength(x1 = 3.2f, y1 = 1f, x2 = 4.5f, y2 = 19.2f),
            absoluteTolerance = floatTolerance
        )

        // From bigger coordinates to smaller ones
        assertEquals(
            expected = 8.695f,
            actual = Calculations.getLength(x1 = 6.7f, y1 = 0f, x2 = 2.3f, y2 = -7.5f),
            absoluteTolerance = floatTolerance
        )

        // From random to random
        assertEquals(
            expected = 10.435f,
            actual = Calculations.getLength(x1 = 10.9f, y1 = 5.64f, x2 = 1.3f, y2 = 9.73f),
            absoluteTolerance = floatTolerance
        )

        // With the default parameter, from zero coordinates
        assertEquals(
            expected = 52.571f,
            actual = Calculations.getLength(x2 = 5.98f, y2 = 52.23f),
            absoluteTolerance = floatTolerance
        )
    }

    @Test
    fun circleIntersectRectTest() {
        // The circle is far from the square
        assertFalse(Calculations.circleIntersectRect(1F, 1F, 5F, 3F, 0F, 5F, 1F))

        // A circle is smaller than a square and inside it
        assertTrue(Calculations.circleIntersectRect(0F, 0F, 6F, 4F, 3F, 2F, 1F))

        // A square is smaller than a circle and inside it
        assertTrue(Calculations.circleIntersectRect(-1F, -1F, 1F, 1F, 0F, 0F, 4F))

        // Square and circle intersect on the left up
        assertTrue(Calculations.circleIntersectRect(4F, 2F, 10F, 5F, 4F, 4F, 1F))

        // Square and circle don't intersect on the left center
        assertFalse(Calculations.circleIntersectRect(-1F, -1F, 1F, 1F, -2F, 0F, 0F))

        // Square and circle intersect on the left down
        assertTrue(Calculations.circleIntersectRect(2F, 2F, 7F, 6F, 1F, 1F, 4F))

        // Square and circle intersect on the right up
        assertTrue(Calculations.circleIntersectRect(0F, 0F, 2F, 2F, 3F, 3F, 2F))

        // Square and circle don't intersect on the right center
        assertFalse(Calculations.circleIntersectRect(0F, 0F, 2F, 2F, 3F, 1F, 0F))

        // Square and circle intersect on the right down
        assertTrue(Calculations.circleIntersectRect(-6F, 5F, -4F, 7F, -3F, 4F, 2F))

        // Square and circle intersect at the bottom
        assertTrue(Calculations.circleIntersectRect(2F, 2F, 4F, 4F, 3F, 1F, 2F))

        // Square and circle don't intersect at the top
        assertFalse(Calculations.circleIntersectRect(1F, 3F, 6F, 3F, 2F, 10F, 1F))
    }
}
