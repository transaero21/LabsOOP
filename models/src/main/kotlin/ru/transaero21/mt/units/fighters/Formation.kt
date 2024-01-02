package ru.transaero21.mt.units.fighters

import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.core.FighterWrapper

class Formation {
    val fighters: OrderedMap<Int, Fighter> = OrderedMap()

    fun update(deltaTime: Float, fWrapper: FighterWrapper) {
        for ((_, fighter) in fighters.entries) {
            fighter.update(delta = deltaTime, fWrapper = fWrapper)
        }
    }
}
