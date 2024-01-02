package ru.transaero21.mt.models.weapon

import ru.transaero21.mt.models.DELTA_TIME
import ru.transaero21.mt.models.getRealFrames
import ru.transaero21.mt.models.setFieldValue
import kotlin.math.ceil
import kotlin.test.Test
import kotlin.test.assertEquals

internal class WeaponTest {
    @Test
    fun desyncFixTest() {
        val weapon: Weapon = WeaponImpl()
        weapon.setFieldValue(fieldName = "capacityCurrent", value = 0)
        weapon.setFieldValue(fieldName = "state", value = WeaponState.Ready)

        weapon.update(deltaTime = DELTA_TIME)

        if (DELTA_TIME < weapon.reloadTime)
            assertEquals(expected = WeaponState.Reloading, actual = weapon.state)
        else
            assertEquals(expected = WeaponState.Ready, actual = weapon.state)
    }

    @Test
    fun fullReloadTest() {
        val weapon: Weapon = WeaponImpl()

        val frames = getRealFrames(
            deltaTime = DELTA_TIME,
            frames = ceil(x = weapon.reloadTime / DELTA_TIME + 1).toInt()
        )
        repeat(times = frames) { _ ->
            weapon.update(deltaTime = DELTA_TIME)
        }

        assertEquals(expected = weapon.capacity, actual = weapon.capacityCurrent)
        assertEquals(expected = WeaponState.Ready, actual = weapon.state)
    }

    @Test
    fun fireWithShotReloadTest() {
        val weapon: Weapon = WeaponImpl()
        weapon.setFieldValue(fieldName = "capacityCurrent", value = weapon.capacity)

        val frames = getRealFrames(
            deltaTime = DELTA_TIME,
            frames = ceil(x = weapon.shotTime * (weapon.capacity - 1) / DELTA_TIME + 1).toInt()
        )
        var shotsFired = 0
        repeat(times = frames) { _ ->
            weapon.update(deltaTime = DELTA_TIME)
            if (weapon.fire(x = 0F, y = 0F, angle = 0F) != null)
                shotsFired++
        }

        assertEquals(expected = weapon.capacity, actual = shotsFired)
        assertEquals(expected = 0, actual = weapon.capacityCurrent)
        assertEquals(expected = WeaponState.Reloading, actual = weapon.state)
    }
}
