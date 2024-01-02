package ru.transaero21.mt.units.managers

import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.core.FighterWrapper
import ru.transaero21.mt.core.orders.Order
import ru.transaero21.mt.units.CombatUnit
import ru.transaero21.mt.units.Rank
import ru.transaero21.mt.units.Uniform
import ru.transaero21.mt.units.fighters.FieldCommander

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

        for ((_, staff) in staff.entries) {
            val approximateRemainingTime = staff.approximateRemainingTime
            if (approximateRemainingTimeMin == null || approximateRemainingTime < approximateRemainingTimeMin) {
                approximateRemainingTimeMin = approximateRemainingTime
                mostFree = staff
            }
        }

        mostFree?.addOrderForSignature(order = order)
        return mostFree != null
    }

    fun update(deltaTime: Float, fWrapper: FighterWrapper) {
        for ((_, staff) in staff.entries) {
            staff.update(deltaTime = deltaTime)
        }

        for ((_, commanders) in fieldCommanders.entries) {
            commanders.update(delta = deltaTime, fWrapper = fWrapper)
        }
    }
}
