package ru.transaero21.mt.models.units.fighters.skills.healing

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultHealing(private val healingRate: Float, healingRange: Float) : Healing() {
    override val timeout: Float = HEALING_TIMEOUT
    override val range: Float = healingRange

    override fun useSkill(delta: Float, self: Fighter, fWrapper: FighterWrapper): Boolean {
        if (!update(delta = delta)) return true

        var nearest: Fighter? = null
        var lessHp = 1f

        val fIterator = fWrapper.iterator.sfIterator()
        while (fIterator.hasNext()) {
            val fighter = fIterator.next().value
            val distance = sqrt(x = (self.x - fighter.x).pow(n = 2) + (self.y - fighter.y).pow(n = 2))
            if (distance <= range && fighter.healthPercentage < lessHp) {
                lessHp = fighter.healthPercentage
                nearest = fighter
            }
        }

        nearest?.applyHealing(healingRate)
        return nearest != null
    }

    companion object {
        private const val HEALING_TIMEOUT = 0.5f
    }
}
