package ru.transaero21.mt.models.units.fighters

import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.core.FighterWrapper
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class Formation(val approachRadius: Float) {
    val fighters: OrderedMap<Int, Fighter> = OrderedMap()

    fun update(deltaTime: Float, fWrapper: FighterWrapper) {
        for ((_, fighter) in fighters.entries) {
            fighter.update(delta = deltaTime, fWrapper = fWrapper)
        }
    }

    fun getExpectedPosition(fieldCommander: FieldCommander): OrderedMap<Int, Pair<Float, Float>> {
        return getExpectedPositions(x = fieldCommander.x, y = fieldCommander.y)
    }

    fun getExpectedPositions(x: Float, y: Float): OrderedMap<Int, Pair<Float, Float>> {
        val coordinates = OrderedMap<Int, Pair<Float, Float>>()
        val step = 2 * PI.toFloat() / fighters.size
        var angle = -PI.toFloat() / 4
        for ((id, _) in fighters) {
            coordinates[id] =  x + cos(x = angle) * approachRadius to y + sin(x = angle) * approachRadius
            angle += step
        }
        return coordinates
    }
}
