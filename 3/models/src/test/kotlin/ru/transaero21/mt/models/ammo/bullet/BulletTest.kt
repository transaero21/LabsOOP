package ru.transaero21.mt.models.ammo.bullet

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BulletTest {
    @Test
    fun overshotTest() {
        val floatTolerance = 0.01f
        val bullet: Bullet = BulletImpl(x = 0f, y = 0f, angle = 0f, velocity = 10f)

        bullet.update(delta = 1f)
        bullet.finalizePosition()
        assertEquals(expected = 10f, actual = bullet.x, absoluteTolerance = floatTolerance)
        assertEquals(expected = 0f, actual = bullet.y, absoluteTolerance = floatTolerance)
        assertEquals(expected = 10f, actual = bullet.distanceCurrent, absoluteTolerance = floatTolerance)
        assertEquals(expected = BulletState.Transit, actual = bullet.state)

        bullet.update(delta = 5f)
        bullet.finalizePosition()
        assertEquals(expected = 50f, actual = bullet.x, absoluteTolerance = floatTolerance)
        assertEquals(expected = 0f,actual = bullet.y, absoluteTolerance = floatTolerance)
        assertEquals(expected = 50f, actual = bullet.distanceCurrent,  absoluteTolerance = floatTolerance)
        assertEquals(expected = BulletState.Transit, actual = bullet.state)

        bullet.update(delta = 1f)
        bullet.finalizePosition()
        assertEquals(expected = 50f, actual = bullet.x, absoluteTolerance = floatTolerance)
        assertEquals(expected = 0f, actual = bullet.y, absoluteTolerance = floatTolerance)
        assertEquals(expected = 50f, actual = bullet.distanceCurrent, absoluteTolerance = floatTolerance)
        assertEquals(expected = BulletState.Dispose, actual = bullet.state)
    }

    @Test
    fun testIsHitEnd() {
        val bullet: Bullet = BulletImpl(x = 0f, y = 0f, angle = 0f, velocity = 10f)
        assertFalse(actual = bullet.isHitEnd)

        bullet.update(delta = 6f)
        bullet.finalizePosition()
        assertTrue(actual = bullet.isHitEnd)

        bullet.update(delta = 1f)
        bullet.finalizePosition()
        assertTrue(actual = bullet.isHitEnd)
    }
}
