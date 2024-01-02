package ru.transaero21.mt.core.instructions

import ru.transaero21.mt.core.FighterWrapper
import ru.transaero21.mt.units.fighters.Fighter
import ru.transaero21.mt.units.fighters.skills.Skill.Companion.getAndUseSkill
import ru.transaero21.mt.units.fighters.skills.Skill.Companion.haveSkill
import ru.transaero21.mt.units.fighters.skills.healing.Healing

data object Heal : Instruction() {
    override fun execute(delta: Float, self: Fighter, fWrapper: FighterWrapper) {
        defaultDecision(ret = self.getAndUseSkill<Healing>(delta = delta, fWrapper = fWrapper))
    }

    override fun checkCompatability(self: Fighter) = self.haveSkill<Healing>()
}
