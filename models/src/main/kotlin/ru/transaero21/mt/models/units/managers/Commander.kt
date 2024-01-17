package ru.transaero21.mt.models.units.managers

import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.units.CombatUnit
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.FieldCommander

class Commander(
    override val fullName: String,
    override val rank: Rank
) : CombatUnit() {
    override val uniform: Uniform = Uniform(width = 0F, length = 0F)
    val staff: OrderedMap<Int, Staff> = OrderedMap()
    val fieldCommanders: OrderedMap<Int, FieldCommander> = OrderedMap()

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

    fun update(deltaTime: Float, fWrapper: FighterWrapper) {
        staff.entries.forEach { (_, staff) ->
            staff.update(deltaTime = deltaTime)
        }

        fieldCommanders.entries.forEach { (_, commanders) ->
            commanders.update(delta = deltaTime, fWrapper = fWrapper)
        }
    }
}
