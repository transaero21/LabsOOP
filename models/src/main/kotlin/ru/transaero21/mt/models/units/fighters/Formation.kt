package ru.transaero21.mt.models.units.fighters

import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.core.FighterWrapper
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/**
 * Represents a formation of fighters in a combat scenario, organized around a field commander.
 *
 * @property approachRadius radius within which individual fighters approach the field commander.
 */
class Formation(private val approachRadius: Float) {
    /**
     * A collection of fighters organized in the formation, mapped by their unique identifiers.
     */
    val fighters: OrderedMap<Int, Fighter> = OrderedMap()

    /**
     * Updates the state of all fighters in the formation based on the elapsed time.
     *
     * @param deltaTime time elapsed since the last update, in seconds.
     * @param fWrapper wrapper containing context-specific information for the fighter.
     */
    fun update(deltaTime: Float, fWrapper: FighterWrapper) {
        fighters.entries.forEach { (_, fighter) ->
            fighter.update(delta = deltaTime, fWrapper = fWrapper)
        }
    }

    /**
     * Gets the expected positions of all fighters in the formation based on the field commander's current position.
     *
     * @param fieldCommander the field commander around which the formation is organized.
     * @return [OrderedMap] of fighter identifiers mapped to their expected positions relative to the commander.
     */
    fun getExpectedPosition(fieldCommander: FieldCommander): OrderedMap<Int, Pair<Float, Float>> {
        return getExpectedPositions(x = fieldCommander.x, y = fieldCommander.y)
    }

    /**
     * Gets the expected positions of all fighters in the formation based on specified coordinates.
     *
     * @param x The x-coordinate around which the formation is centered.
     * @param y The y-coordinate around which the formation is centered.
     * @return [OrderedMap] of fighter identifiers mapped to their expected positions relative to the specified coordinates.
     */
    fun getExpectedPositions(x: Float, y: Float): OrderedMap<Int, Pair<Float, Float>> {
        val coordinates = OrderedMap<Int, Pair<Float, Float>>()
        val step = 2 * PI.toFloat() / fighters.size
        var angle = -PI.toFloat() / 4
        fighters.forEach { (id, _) ->
            coordinates[id] =  x + cos(x = angle) * approachRadius to y + sin(x = angle) * approachRadius
            angle += step
        }
        return coordinates
    }
}
