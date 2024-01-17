package ru.transaero21.mt.models.ammo

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AmmunitionTest {
    @Test
    fun updateTest() {
        val floatTolerance = 0.01f
        val ammunition = AmmunitionImpl(x = 0f, y = 0f, angle = 0f, velocity = 10f)

        ammunition.update(delta = 1F)
        ammunition.finalizePosition()
        assertEquals(expected = ammunition.x, actual = 10f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.y, actual = 0f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.distanceCurrent, actual = 10f, absoluteTolerance = floatTolerance)

        ammunition.update(delta = 1F)
        ammunition.finalizePosition()
        assertEquals(expected = ammunition.x, actual = 20f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.y, actual = 0f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.distanceCurrent, actual = 20f, absoluteTolerance = floatTolerance)

        ammunition.update(delta = 1F)
        ammunition.finalizePosition()
        assertEquals(expected = ammunition.x, actual = 30f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.y, actual = 0f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.distanceCurrent, actual = 30f, absoluteTolerance = floatTolerance)

        ammunition.update(delta = 2F)
        ammunition.finalizePosition()
        assertEquals(expected = ammunition.x, actual = 50f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.y, actual = 0f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.distanceCurrent, actual = 50f, absoluteTolerance = floatTolerance)

        ammunition.update(delta = 10F)
        ammunition.finalizePosition()
        assertEquals(expected = ammunition.x, actual = 100f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.y, actual = 0f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.distanceCurrent, actual = 100f, absoluteTolerance = floatTolerance)

        ammunition.update(delta = 1F)
        ammunition.finalizePosition()
        assertEquals(expected = ammunition.x, actual = 100f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.y, actual = 0f, absoluteTolerance = floatTolerance)
        assertEquals(expected = ammunition.distanceCurrent, actual = 100f, absoluteTolerance = floatTolerance)
    }

    @Test
    @Suppress(names = ["KotlinConstantConditions"])
    fun testCompareTo() {
        val ammunition1 = AmmunitionImpl(x = 0f, y = 0f, angle = 0f, velocity = 10f)
        val ammunition2 = AmmunitionImpl(x = 5f, y = 0f, angle = 0f, velocity = 10f)
        val ammunition3 = AmmunitionImpl(x = 0f, y = 5f, angle = 0f, velocity = 10f)
        val ammunition4 = AmmunitionImpl(x = 0f, y = 0f, angle = 45f, velocity = 10f)
        val ammunition5 = AmmunitionImpl(x = 0f, y = 0f, angle = 0f, velocity = 20f)

        assertTrue(actual = ammunition1 < ammunition2)
        assertTrue(actual = ammunition1 < ammunition3)
        assertTrue(actual = ammunition1 < ammunition4)
        assertTrue(actual = ammunition1 < ammunition5)

        assertFalse(actual = ammunition1 > ammunition2)
        assertFalse(actual = ammunition1 > ammunition3)
        assertFalse(actual = ammunition1 > ammunition4)
        assertFalse(actual = ammunition1 > ammunition5)

        assertTrue(actual = ammunition2 > ammunition1)
        assertTrue(actual = ammunition3 > ammunition1)
        assertTrue(actual = ammunition4 > ammunition1)
        assertTrue(actual = ammunition5 > ammunition1)

        assertFalse(actual = ammunition2 < ammunition1)
        assertFalse(actual = ammunition3 < ammunition1)
        assertFalse(actual = ammunition4 < ammunition1)
        assertFalse(actual = ammunition5 < ammunition1)

        assertEquals(expected = ammunition1, actual = ammunition1)
        assertEquals(expected = 0, actual = ammunition1.compareTo(ammunition1))
    }
}
