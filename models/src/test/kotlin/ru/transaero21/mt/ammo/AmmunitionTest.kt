package ru.transaero21.mt.ammo

import org.junit.jupiter.api.Test
import ru.transaero21.mt.DELTA_TIME
import ru.transaero21.mt.getRealFloatSum
import ru.transaero21.mt.getRealFrames
import ru.transaero21.mt.weapon.WeaponImpl
import kotlin.math.*
import kotlin.test.assertEquals

class AmmunitionTest {
    @Test
    fun ammunitionMovementTest() {
        val angle = 1F / 2F
        val (x, y) = 0F to 0F
        val ammo: Ammunition = AmmunitionImpl(x = x, y = y, angle = angle, velocity = WeaponImpl.VELOCITY)

        val time = ammo.distanceMax / ammo.velocity / 2F
        val frames = getRealFrames(deltaTime = DELTA_TIME, frames = floor(x = time / DELTA_TIME).toInt())
        repeat(times = frames) { _ ->
            ammo.update(deltaTime = DELTA_TIME)
        }
        assertEquals(
            expected = getRealFloatSum(float = x + cos(angle) * WeaponImpl.VELOCITY * DELTA_TIME, count = frames),
            actual = ammo.x
        )
        assertEquals(
            expected = getRealFloatSum(float = y + sin(angle) * WeaponImpl.VELOCITY * DELTA_TIME, count = frames),
            actual = ammo.y
        )
    }

    @Test
    fun ammunitionOvershotTest() {
        val angle = 1F / 2F
        val (x, y) = 0F to 0F
        val ammo: Ammunition = AmmunitionImpl(x = x, y = y, angle = angle, velocity = WeaponImpl.VELOCITY)

        val time = ammo.distanceMax / ammo.velocity
        val frames = getRealFrames(deltaTime = DELTA_TIME, frames = ceil(x = time / DELTA_TIME + 1).toInt())
        repeat(times = frames) { _ ->
            ammo.update(deltaTime = DELTA_TIME)
        }
        val (eX, eY) = x + cos(angle) * ammo.distanceMax to y + sin(angle) * ammo.distanceMax
        assert(value = eX >= ammo.x)
        assertEquals(expected = round(x = eX), actual = round(x = ammo.x))
        assert(value = eY >= ammo.y)
        assertEquals(expected = round(x = eY), actual = round(x = ammo.y))

        // Nothing changed
        val (pX, pY) = ammo.x to ammo.y
        ammo.update(deltaTime = DELTA_TIME)
        assertEquals(expected = pX, actual = ammo.x)
        assertEquals(expected = pY, actual = ammo.y)
    }
}
