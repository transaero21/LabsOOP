package ru.transaero21.mt.models.units.fighters

import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.units.fighters.skills.attacking.DefaultAttacking
import ru.transaero21.mt.models.units.fighters.skills.healing.DefaultHealing
import ru.transaero21.mt.models.weapon.Pistol
import ru.transaero21.mt.models.weapon.Weapon
import ru.transaero21.mt.utils.RandomizerUtils

class Doctor(
    override val fullName: String = RandomizerUtils.getNextName(),
    initX: Float, initY: Float
) : Fighter(x = initX, y = initY) {
    override val healthMax: Float = 120F
    override val skills: Set<Skill> = setOf(DefaultAttacking(attackingRange = 128f), DefaultHealing(healingRange = 16f, healingRate = 10f))
    override val weapon: Weapon = Pistol()
    override val speed: Float = 24F
    override val uniform: Uniform = Uniform(width = 16F, length = 16F)
    override val rank: Rank = RandomizerUtils.getNextRank()
}
