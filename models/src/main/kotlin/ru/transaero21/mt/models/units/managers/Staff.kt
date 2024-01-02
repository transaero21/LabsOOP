package ru.transaero21.mt.models.units.managers

import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.core.orders.OrderStatus
import ru.transaero21.mt.models.units.CombatUnit
import ru.transaero21.mt.models.units.Uniform

abstract class Staff(
    override val fullName: String,
) : CombatUnit() {
    override val uniform: Uniform = Uniform(width = 0F, length = 0F)

    abstract val defaultProcessingTime: Float
    abstract val maxEfficiencyBonus: Float
    abstract val defaultEfficiency: Float
    abstract val maxEfficiency: Float
    abstract val efficiencyIncreaseFactor: Float

    private val _orders: MutableList<Order> = mutableListOf()
    val orders: List<Order> = _orders
    var current: Int = 0
        private set

    val efficiency: Float
        get() = (defaultEfficiency + efficiencyIncreaseFactor * current).let {
            if (it > maxEfficiency) maxEfficiency else it
        }
    val processingTime: Float
        get() = defaultProcessingTime - maxEfficiencyBonus * efficiency

    private var timePassed: Float = 0F
    val approximateRemainingTime get() = (orders.size - current) * processingTime - timePassed

    fun update(deltaTime: Float) {
        if (current >= orders.size) return

        timePassed += deltaTime
        if (timePassed >= processingTime)
            pickNextOrder()
    }

    private fun pickNextOrder() {
        timePassed -= processingTime
        _orders[current].status = OrderStatus.Processed
        current++
        if (current < orders.size)
            _orders[current].status = OrderStatus.Processing

    }

    fun addOrderForSignature(order: Order) = _orders.add(order)
}
