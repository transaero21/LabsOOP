package ru.transaero21.mt.models.ammo.bullet

import org.junit.jupiter.api.Test
import ru.transaero21.mt.models.ammo.bullet.BulletImpl.Companion.DISTANCE_MAX
import ru.transaero21.mt.models.weapon.WeaponImpl.Companion.VELOCITY
import kotlin.math.cos
import kotlin.math.sin
import kotlin.test.assertEquals

class BulletTest {
    @Test
    fun bulletOvershotTest() {
        val angle = 0F
        val (x, y) = 0F to 0F
        val bullet: Bullet = BulletImpl(x = x, y = y, angle = angle, velocity = VELOCITY)

        // Bullet at maximum distance
        bullet.update(deltaTime = DISTANCE_MAX / VELOCITY)
        assertEquals(expected = cos(angle) * DISTANCE_MAX, actual = bullet.x)
        assertEquals(expected = sin(angle) * DISTANCE_MAX, actual = bullet.y)
        assertEquals(expected = BulletState.Transit, actual = bullet.state)
        assertEquals(expected = true, actual = bullet.isHitEnd)

        // Bullet at double maximum distance
        bullet.update(deltaTime = DISTANCE_MAX / VELOCITY)
        assertEquals(expected = cos(angle) * DISTANCE_MAX, actual = bullet.x)
        assertEquals(expected = sin(angle) * DISTANCE_MAX, actual = bullet.y)
        assertEquals(expected = BulletState.Dispose, actual = bullet.state)
        assertEquals(expected = true, actual = bullet.isHitEnd)
    }
}
