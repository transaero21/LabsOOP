package ru.transaero21.mt.models.units.fighter

import org.junit.jupiter.api.Assertions.assertFalse
import org.mockito.Mockito.mock
import org.mockito.kotlin.mock
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.weapon.Weapon
import ru.transaero21.mt.models.weapon.WeaponImpl
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FighterTest {
    class ConcreteFighter(x: Float, y: Float) : Fighter(x, y) {
        override val healthMax: Float = 100f
        override val skills: Set<Skill> = emptySet()
        override val weapon: Weapon = WeaponImpl()
        override val speed: Float = 10f
        override val fullName: String = ""
        override val rank: Rank = Rank("", 1f)
        override val uniform: Uniform = Uniform(1f, 1f)
    }

    @Test
    fun testApplyHit() {
        val fighter = ConcreteFighter(x = 0f, y = 0f)

        // Apply hit with damage
        assertTrue(fighter.applyHit(20f))
        assertEquals(0.8f, fighter.healthPercentage, 0.001f)

        // Apply hit with zero damage
        assertTrue(fighter.applyHit(0f))
        assertEquals(0.8f, fighter.healthPercentage, 0.001f)

        // Apply hit with negative damage (should have no effect)
        assertTrue(fighter.applyHit(-10f))
        assertEquals(0.8f, fighter.healthPercentage, 0.001f)

        // Apply hit to reduce health to zero
        assertTrue(fighter.applyHit(80f))
        assertEquals(0f, fighter.healthPercentage, 0.001f)
    }

    @Test
    fun testApplyHealing() {
        val fighter = ConcreteFighter(x = 0f, y = 0f)
        fighter.applyHit(100f)

        // Apply healing with positive value
        assertTrue(fighter.applyHealing(100f))
        assertEquals(1f, fighter.healthPercentage, 0.001f)

        // Apply healing with zero healing (should have no effect)
        assertTrue(fighter.applyHealing(0f))
        assertEquals(1f, fighter.healthPercentage, 0.001f)

        // Apply healing with negative value (should have no effect)
        assertTrue(fighter.applyHealing(-10f))
        assertEquals(1f, fighter.healthPercentage, 0.001f)

        // Apply healing to a fighter with full health (should have no effect)
        assertTrue(fighter.applyHealing(20f))
        assertEquals(1f, fighter.healthPercentage, 0.001f)
    }

    @Test
    fun testMove() {
        val fighter = ConcreteFighter(x = 0f, y = 0f)

        // Move to the same position
        assertTrue(fighter.move(delta = 1f, x = 0f, y = 0f))
        fighter.finalizePosition()
        assertEquals(0f, fighter.x, 0.001f)
        assertEquals(0f, fighter.y, 0.001f)

        // Move to a new position
        assertTrue(fighter.move(delta = 1f, x = 0f, y = 10f))
        fighter.finalizePosition()
        assertEquals(0f, fighter.x, 0.001f)
        assertEquals(10f, fighter.y, 0.001f)

        // Move with a speed that can't reach the target in one frame
        assertTrue(fighter.move(delta = 0.5f, x = 5f, y = 10f))
        fighter.finalizePosition()
        assertEquals(5f, fighter.x, 0.001f)
        assertEquals(10f, fighter.y, 0.001f)

        // Move to a new position with zero speed (should have no effect)
        assertFalse(fighter.move(delta = 1f, x = 100f, y = 100f))
        fighter.finalizePosition()
    }

    @Test
    fun testConveyInstructions() {
        val fighter = ConcreteFighter(x = 0f, y = 0f)
        val instructions = listOf(mock<Instruction>(), mock<Instruction>())

        fighter.conveyInstructions(instructions)

        assertEquals(2, fighter.instrSize)
    }

    @Test
    fun testHasUnfulfilledInstructions() {
        val fighter = ConcreteFighter(x = 0f, y = 0f)
        val instructions = listOf(mock(Instruction::class.java))

        assertFalse(fighter.hasUnfulfilledInstructions())

        fighter.conveyInstructions(instructions)

        assertTrue(fighter.hasUnfulfilledInstructions())
    }

    @Test
    fun testGetCurrentInstructions() {
        val fighter = ConcreteFighter(x = 0f, y = 0f)
        val instructions = listOf(mock(Instruction::class.java))

        assertNull(fighter.getCurrentInstructions())

        fighter.conveyInstructions(instructions)

        assertEquals(instructions[0], fighter.getCurrentInstructions())
    }

    @Test
    fun testAbortInstructions() {
        val fighter = ConcreteFighter(x = 0f, y = 0f)
        val instructions = listOf(mock(Instruction::class.java))

        fighter.conveyInstructions(instructions)

        assertEquals(1, fighter.instrSize)

        fighter.abortInstructions()

        assertEquals(0, fighter.instrSize)
    }
}
