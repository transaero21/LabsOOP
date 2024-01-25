package ru.transaero21.mt.models.units.fighters.skills.defusing

import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.ammo.mine.Mine
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter
import kotlin.math.pow
import kotlin.math.sqrt

class DefaultDefusing(defusingRange: Float) : Defusing() {
    override val timeout: Float = DEFUSING_TIMEOUT
    override val range: Float = defusingRange

    override fun useSkill(delta: Float, self: Fighter, fWrapper: FighterWrapper): Boolean {
        if (!update(delta = delta)) return false

        var nearest: Ammunition? = null
        var distanceMin: Float? = null

        val aIterator = fWrapper.iterator.eaIterator()
        while (aIterator.hasNext()) {
            val ammo = aIterator.next().value
            if (ammo !is Mine) continue
            val distance = sqrt(x = (self.x - ammo.x).pow(n = 2) + (self.y - ammo.y).pow(n = 2))
            if (distance <= range && (distanceMin == null || distance < distanceMin)) {
                distanceMin = distance
                nearest = ammo
            }
        }

        if (nearest is Mine)
            return !nearest.defuse(deltaTime = delta)
        return false
    }


    companion object {
        private const val DEFUSING_TIMEOUT = 1000F
    }
}
