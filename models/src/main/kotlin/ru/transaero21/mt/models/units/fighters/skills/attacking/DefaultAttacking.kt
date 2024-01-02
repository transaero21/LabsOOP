package ru.transaero21.mt.models.units.fighters.skills.attacking

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultAttacking(private val attackingRange: Float) : Attacking() {
    override val timeout: Float = 0F

    override fun useSkill(delta: Float, self: Fighter, fWrapper: FighterWrapper): Boolean {
        if (!update(delta = delta)) return false

        var nearest: Fighter? = null
        var moreHp: Float? = null

        val fIterator = fWrapper.iterator.efIterator()
        while (fIterator.hasNext()) {
            val fighter = fIterator.next().value
            val distance = sqrt(x = (self.x - fighter.x).pow(n = 2) + (self.y - fighter.y).pow(n = 2))
            if (distance <= attackingRange && (moreHp == null || fighter.healthPercentage >= moreHp)) {
                moreHp = distance
                nearest = fighter
            }
        }

        if (nearest != null)
            self.weapon.fire(x = self.x, y = self.x, angle = getAngle(x = self.x, y = self.y, nearest = nearest))
        return nearest != null
    }

    private fun getAngle(x: Float, y: Float, nearest: Fighter): Float {
        return cos(
            x = getLength(x1 = x, y1 = y, x2 = nearest.x, y2 = y) /
                    getLength(x1 = x, y1 = y, x2 = nearest.x, y2 = nearest.y)
        )
    }

    private fun getLength(x1: Float, y1: Float, x2: Float, y2: Float): Float {
        return sqrt(x = (x1 - x2).pow(n = 2) + (y1 - y2).pow(n = 2))
    }
}
