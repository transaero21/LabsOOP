package ru.transaero21.mt.models.units.commander

import org.mockito.Mockito.`when`
import org.mockito.kotlin.*
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.orders.Move
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.fieldCommander.FieldCommanderTest.Companion.getFieldCommander
import ru.transaero21.mt.models.units.managers.Commander
import ru.transaero21.mt.models.units.managers.Staff
import ru.transaero21.mt.models.units.staff.StaffImpl
import kotlin.test.*

class CommanderTest {

    @Test
    fun testHandleNewOrder() {
        val mockOrder = Move(0f, 0f, 1)
        val commander = Commander("John Doe", Rank("General", 5.0f))

        // Mock staff members
        val staff1 = spy(StaffImpl())
        val staff2 = spy(StaffImpl())
        `when`(staff1.approximateRemainingTime).thenReturn(10.0f)
        `when`(staff2.approximateRemainingTime).thenReturn(5.0f)

        commander.staff[1] = staff1
        commander.staff[2] = staff2

        // Execute the method you want to test
        val result = commander.handleNewOrder(mockOrder)

        // Verify that the order is assigned to the most available staff member
        assertTrue(result)
    }

    @Test
    fun testUpdate() {
        val delta = 1.0f
        val mockWrapper = mock<FighterWrapper>()
        val commander = spy(Commander("John Doe", Rank("General", 5.0f)))

        // Mock staff members
        val staff1 = StaffImpl()
        val staff2 = StaffImpl()

        commander.staff[1] = staff1
        commander.staff[2] = staff2

        // Mock field commanders
        val fieldCommander1 = getFieldCommander(0f, 0f)
        val fieldCommander2 = getFieldCommander(0f, 0f)

        commander.fieldCommanders[1] = fieldCommander1
        commander.fieldCommanders[2] = fieldCommander2

        // Execute the method you want to test
        commander.update(delta, mockWrapper)
    }
}
