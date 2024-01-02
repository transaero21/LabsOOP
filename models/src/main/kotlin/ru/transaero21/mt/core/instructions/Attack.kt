package ru.transaero21.mt.core.instructions

import ru.transaero21.mt.core.FighterWrapper
import ru.transaero21.mt.units.fighters.Fighter
import ru.transaero21.mt.units.fighters.skills.Skill.Companion.getAndUseSkill
import ru.transaero21.mt.units.fighters.skills.Skill.Companion.haveSkill
import ru.transaero21.mt.units.fighters.skills.attacking.Attacking

data object Attack : Instruction() {
    override fun execute(delta: Float, self: Fighter, fWrapper: FighterWrapper) {
        defaultDecision(ret = self.getAndUseSkill<Attacking>(delta = delta, fWrapper = fWrapper))
    }

    override fun checkCompatability(self: Fighter) = self.haveSkill<Attacking>()
}
