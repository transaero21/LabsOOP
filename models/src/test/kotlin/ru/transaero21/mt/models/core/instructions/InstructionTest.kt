package ru.transaero21.mt.models.core.instructions

import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.models.units.fighters.skills.Skill.Companion.getAndUseSkill
import ru.transaero21.mt.models.units.fighters.skills.attacking.Attacking
import ru.transaero21.mt.models.units.fighters.skills.defusing.Defusing
import ru.transaero21.mt.models.units.fighters.skills.healing.Healing
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InstructionTest {
    @Test
    fun testDefaultDecisionTrue() {
        val instruction = TestInstruction()
        instruction.defaultDecision(true)

        assert(instruction.status == InstructionStatus.Started)
    }

    @Test
    fun testDefaultDecisionFalseStarted() {
        val instruction = TestInstruction()
        instruction.status = InstructionStatus.Started
        instruction.defaultDecision(false)

        assert(instruction.status == InstructionStatus.Done)
    }

    @Test
    fun testDefaultDecisionFalseNotStarted() {
        val instruction = TestInstruction()
        instruction.defaultDecision(false)

        assert(instruction.status == InstructionStatus.Failed)
    }

    @Test
    fun testDefaultDecisionNull() {
        val instruction = TestInstruction()
        instruction.defaultDecision(null)

        assert(instruction.status == InstructionStatus.Failed)
    }

    @Test
    fun testExecuteWithSkill() {
        val delta = 0.1f
        val self = mock<Fighter>()
        val fWrapper = mock<FighterWrapper>()
        Heal.execute(delta = delta, self = self, fWrapper = fWrapper)
        assertFalse(Heal.checkCompatability(self = self))
        Attack.execute(delta = delta, self = self, fWrapper = fWrapper)
        assertFalse(Attack.checkCompatability(self = self))
        Defuse.execute(delta = delta, self = self, fWrapper = fWrapper)
        assertFalse(Defuse.checkCompatability(self = self))
        Move(100f, 100f).let {
            it.execute(delta = delta, self = self, fWrapper = fWrapper)
            assertTrue(it.checkCompatability(self = self))
        }
    }

    private class TestInstruction : Instruction() {
        override fun execute(delta: Float, self: Fighter, fWrapper: FighterWrapper) {}

        override fun checkCompatability(self: Fighter): Boolean { return true }
    }
}