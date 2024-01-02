package ru.transaero21.mt.models.units.fighters.skills.healing

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultHealing(private val healingRate: Float, private val healingRange: Float) : Healing() {
    override val timeout: Float = HEALING_TIMEOUT

    override fun useSkill(delta: Float, self: Fighter, fWrapper: FighterWrapper): Boolean {
        if (!update(delta = delta)) return false

        var nearest: Fighter? = null
        var lessHp: Float? = null

        val fIterator = fWrapper.iterator.sfIterator()
        while (fIterator.hasNext()) {
            val fighter = fIterator.next().value
            val distance = sqrt(x = (self.x - fighter.x).pow(n = 2) + (self.y - fighter.y).pow(n = 2))
            if (distance <= healingRange && (lessHp == null || fighter.healthPercentage < lessHp)) {
                lessHp = distance
                nearest = fighter
            }
        }

        nearest?.applyHit(-healingRate)
        return nearest != null
    }

    companion object {
        private const val HEALING_TIMEOUT = 0.5F
    }
}
