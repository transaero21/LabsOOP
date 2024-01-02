package ru.transaero21.mt.models.core.instructions

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.models.units.fighters.skills.Skill.Companion.getAndUseSkill
import ru.transaero21.mt.models.units.fighters.skills.Skill.Companion.haveSkill
import ru.transaero21.mt.models.units.fighters.skills.attacking.Attacking

data object Attack : Instruction() {
    override fun execute(delta: Float, self: Fighter, fWrapper: FighterWrapper) {
        defaultDecision(ret = self.getAndUseSkill<Attacking>(delta = delta, fWrapper = fWrapper))
    }

    override fun checkCompatability(self: Fighter) = self.haveSkill<Attacking>()
}
