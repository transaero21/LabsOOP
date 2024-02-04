package ru.transaero21.mt.models.units.fighters

import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.units.fighters.skills.attacking.DefaultAttacking
import ru.transaero21.mt.models.units.fighters.skills.defusing.DefaultDefusing
import ru.transaero21.mt.models.weapon.Pistol
import ru.transaero21.mt.models.weapon.Weapon
import ru.transaero21.mt.utils.RandomizerUtils

class Sapper(
    override val fullName: String = RandomizerUtils.getNextName(),
    initX: Float, initY: Float
) : Fighter(x = initX, y = initY) {
    override val healthMax: Float = 100F
    override val skills: Set<Skill> = setOf(DefaultAttacking(attackingRange = 128f), DefaultDefusing(defusingRange = 16f))
    override val weapon: Weapon = Pistol()
    override val speed: Float = 12F
    override val uniform: Uniform = Uniform(width = 16F, length = 16F)
    override val rank: Rank = RandomizerUtils.getNextRank()
}