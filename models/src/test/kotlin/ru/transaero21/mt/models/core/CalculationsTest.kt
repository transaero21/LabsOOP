package ru.transaero21.mt.models.core

import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class CalculationsTest {
    @Test
    fun circleIntersectRectTest() {
        // The circle is far from the square
        assertTrue(!Calculations.circleIntersectRect(1F, 1F, 5F, 3F, 0F, 5F, 1F))

        // A circle is smaller than a square and inside it
        assertTrue(Calculations.circleIntersectRect(0F, 0F, 6F, 4F, 3F, 2F, 1F))

        // A square is smaller than a circle and inside it
        assertTrue(Calculations.circleIntersectRect(-1F, -1F, 1F, 1F, 0F, 0F, 4F))

        // Square and circle intersect on the left up
        assertTrue(Calculations.circleIntersectRect(4F, 2F, 10F, 5F, 4F, 4F, 1F))

        // Square and circle don't intersect on the left center
        assertTrue(!Calculations.circleIntersectRect(-1F, -1F, 1F, 1F, -2F, 0F, 0F))

        // Square and circle intersect on the left down
        assertTrue(Calculations.circleIntersectRect(2F, 2F, 7F, 6F, 1F, 1F, 4F))

        // Square and circle intersect on the right up
        assertTrue(Calculations.circleIntersectRect(0F, 0F, 2F, 2F, 3F, 3F, 2F))

        // Square and circle don't intersect on the right center
        assertTrue(!Calculations.circleIntersectRect(0F, 0F, 2F, 2F, 3F, 1F, 0F))

        // Square and circle intersect on the right down
        assertTrue(Calculations.circleIntersectRect(-6F, 5F, -4F, 7F, -3F, 4F, 2F))

        // Square and circle intersect at the bottom
        assertTrue(Calculations.circleIntersectRect(2F, 2F, 4F, 4F, 3F, 1F, 2F))

        // Square and circle don't intersect at the top
        assertTrue(!Calculations.circleIntersectRect(1F, 3F, 6F, 3F, 2F, 10F, 1F))
    }
}
