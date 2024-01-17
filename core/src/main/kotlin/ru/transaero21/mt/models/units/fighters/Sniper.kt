package ru.transaero21.mt.models.units.fighters

import ru.transaero21.mt.models.weapon.SniperRifle
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.weapon.Weapon

class Sniper(
    override val fullName: String,
    initX: Float, initY: Float
) : Fighter(x = initX, y = initY) {
    override val healthMax: Float = 120F
    override val skills: Set<Skill> = setOf()
    override val weapon: Weapon = SniperRifle()
    override val speed: Float = 12F
    override val uniform: Uniform = Uniform(width = 16F, length = 16F)
    override val rank: Rank = Rank(title = "Doctor", weight = 1F)
}
