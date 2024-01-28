package ru.transaero21.mt.models.core.orders

import org.mockito.kotlin.mock
import ru.transaero21.mt.models.units.fighters.FieldCommander
import ru.transaero21.mt.models.units.fighters.Formation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OrderTest {
    @Test
    fun testGetInstructionsForAttackOrder() {
        val fc = mock<FieldCommander>().also { fc -> fc.formation = Formation(approachRadius = 0f).also { it.fighters[0] = mock() } }
        val attackOrder = Attack(x = 1.0f, y = 2.0f, fcId = 3)
        assertEquals(OrderStatus.Created, attackOrder.status)
        val instructions = attackOrder.getInstructions(fc)
        assertTrue { instructions.isNotEmpty() }
    }

    @Test
    fun testGetInstructionsForDefuseOrder() {
        val fc = mock<FieldCommander>().also { fc -> fc.formation = Formation(approachRadius = 0f).also { it.fighters[0] = mock() } }
        val defuseOrder = Defuse(x = 1.0f, y = 2.0f, fcId = 3)
        assertEquals(OrderStatus.Created, defuseOrder.status)
        val instructions = defuseOrder.getInstructions(fc)
        assertTrue { instructions.isNotEmpty() }
    }

    @Test
    fun testGetInstructionsForHealOrder() {
        val fc = mock<FieldCommander>().also { fc -> fc.formation = Formation(approachRadius = 0f).also { it.fighters[0] = mock() } }
        val healOrder = Heal(x = 1.0f, y = 2.0f, fcId = 3)
        assertEquals(OrderStatus.Created, healOrder.status)
        val instructions = healOrder.getInstructions(fc)
        assertTrue { instructions.isNotEmpty() }
    }

    @Test
    fun testGetInstructionsForMoveOrder() {
        val fc = mock<FieldCommander>().also { fc -> fc.formation = Formation(approachRadius = 0f).also { it.fighters[0] = mock() } }
        val moveOrder = Move(x = 1.0f, y = 2.0f, fcId = 3)
        assertEquals(OrderStatus.Created, moveOrder.status)
        val instructions = moveOrder.getInstructions(fc)
        assertTrue { instructions.isNotEmpty() }
    }
}
