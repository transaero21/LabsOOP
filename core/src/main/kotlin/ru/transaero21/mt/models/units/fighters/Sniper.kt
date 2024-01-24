package ru.transaero21.mt.models.units.fighters

import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.units.fighters.skills.attacking.DefaultAttacking
import ru.transaero21.mt.models.weapon.SniperRifle
import ru.transaero21.mt.models.weapon.Weapon
import ru.transaero21.mt.utils.RandomizerUtils

class Sniper(
    override val fullName: String = RandomizerUtils.getNextName(),
    initX: Float, initY: Float
) : Fighter(x = initX, y = initY) {
    override val healthMax: Float = 70F
    override val skills: Set<Skill> = setOf(DefaultAttacking(attackingRange = 128f))
    override val weapon: Weapon = SniperRifle()
    override val speed: Float = 20F
    override val uniform: Uniform = Uniform(width = 16F, length = 16F)
    override val rank: Rank = RandomizerUtils.getNextRank()
}
