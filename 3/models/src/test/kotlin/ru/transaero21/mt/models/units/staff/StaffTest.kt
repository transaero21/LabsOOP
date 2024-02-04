package ru.transaero21.mt.models.units.staff

import org.junit.jupiter.api.Test
import ru.transaero21.mt.models.DELTA_TIME
import ru.transaero21.mt.models.core.orders.Heal
import ru.transaero21.mt.models.core.orders.Move
import ru.transaero21.mt.models.getRealFrames
import ru.transaero21.mt.models.units.managers.Staff
import kotlin.math.ceil
import kotlin.random.Random
import kotlin.test.assertEquals

class StaffTest {
    @Test
    fun processOrdersTest() {
        val staff: Staff = StaffImpl()

        val orders = listOf(Move(x = 0F, y = 0F, fcId = 0), Heal(x = 0f, y = 0f, fcId = 0))
        orders.forEach { order -> staff.addOrderForSignature(order = order) }

        val frames = getRealFrames(
            deltaTime = DELTA_TIME,
            frames = ceil(x = staff.processingTime / DELTA_TIME).toInt()
        )
        repeat(times = frames) {
            staff.update(delta = DELTA_TIME)
        }

        assertEquals(expected = 1, actual = staff.current)
    }

    @Test
    fun increasedEfficiencyTest() {
        val staff: Staff = StaffImpl()

        val requiredOrders = ceil(
            x = (staff.maxEfficiency - staff.defaultEfficiency) / staff.efficiencyIncreaseFactor
        ).toInt()
        val orders = List(requiredOrders) {
            Move(x = Random.nextFloat(), y = Random.nextFloat(), fcId = Random.nextInt())
        }
        orders.forEach { order -> staff.addOrderForSignature(order = order) }

        val frames = getRealFrames(
            deltaTime = DELTA_TIME,
            frames = ceil(x = staff.approximateRemainingTime / DELTA_TIME).toInt()
        )
        repeat(times = frames) {
            staff.update(delta = DELTA_TIME)
        }

        assertEquals(expected = orders.size, actual = staff.current)
        assertEquals(expected = staff.efficiency, actual = staff.maxEfficiency)
    }
}
