package ru.transaero21.mt.models.units.managers

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.units.CombatUnit
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.FieldCommander

/**
 * Represents a commander in charge of managing staff and field commanders.
 *
 * @property fullName full name of the commander.
 * @property rank rank of the commander.
 */
open class Commander(
    override val fullName: String,
    override val rank: Rank
) : CombatUnit() {
    /**
     * Uniform worn by the commander (dimensions are set to zero for simplicity).
     */
    override val uniform: Uniform = Uniform(width = 0F, length = 0F)

    /**
     * Collection of staff members managed by the commander, mapped by their unique identifiers.
     */
    val staff: OrderedMap<Int, Staff> = OrderedMap()

    /**
     * Collection of field commanders managed by the commander, mapped by their unique identifiers.
     */
    val fieldCommanders: OrderedMap<Int, FieldCommander> = OrderedMap()

    /**
     * Handles a new order by assigning it to the most available staff member.
     *
     * @param order order to be handled.
     * @return true if the order is successfully assigned, false otherwise.
     */
    fun handleNewOrder(order: Order): Boolean {
        var mostFree: Staff? = null
        var approximateRemainingTimeMin: Float? = null

        staff.entries.forEach { (_, staff) ->
            val approximateRemainingTime = staff.approximateRemainingTime
            if (approximateRemainingTimeMin == null || approximateRemainingTime < approximateRemainingTimeMin!!) {
                approximateRemainingTimeMin = approximateRemainingTime
                mostFree = staff
            }
        }

        mostFree?.addOrderForSignature(order = order)
        return mostFree != null
    }

    /**
     * Updates the state of staff members and field commanders based on the elapsed time.
     *
     * @param delta time elapsed since the last update, in seconds.
     * @param fWrapper wrapper containing context-specific information for the fighter.
     */
    fun update(delta: Float, fWrapper: FighterWrapper) {
        staff.values.forEach { staff ->
            staff.update(delta = delta)
        }

        runBlocking {
            val deferredList = fieldCommanders.values.map { commanders ->
                async { commanders.update(delta = delta, fWrapper = fWrapper) }
            }
            deferredList.awaitAll()
        }
    }
}
