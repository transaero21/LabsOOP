package ru.transaero21.mt.models.units.fighters.skills.attacking

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultAttacking(attackingRange: Float) : Attacking() {
    override val timeout: Float = 0F
    override val range: Float = attackingRange

    override fun useSkill(delta: Float, self: Fighter, fWrapper: FighterWrapper): Boolean {
        if (!update(delta = delta)) return false

        var nearest: Fighter? = null
        var leastDistance: Float? = null

        val fIterator = fWrapper.iterator.efIterator()
        while (fIterator.hasNext()) {
            val fighter = fIterator.next().value
            val distance = sqrt(x = (self.x - fighter.x).pow(n = 2) + (self.y - fighter.y).pow(n = 2))
            if (distance <= range && (leastDistance == null || leastDistance >= distance)) {
                leastDistance = distance
                nearest = fighter
            }
        }

        if (nearest != null) {
            self.weapon.fire(x = self.x, y = self.y, angle = self.getAngle(x = nearest.x, y = nearest.y))?.let { ammo ->
                fWrapper.populateAmmo(ammo)
            }
        }

        return nearest != null
    }
}
