package ru.transaero21.mt.models.units.managers

import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.core.orders.OrderStatus
import ru.transaero21.mt.models.units.CombatUnit
import ru.transaero21.mt.models.units.Uniform
import kotlin.math.max

/**
 * Abstract class representing a staff member.
 *
 * @property fullName full name of the staff member.
 */
abstract class Staff(
    override val fullName: String,
) : CombatUnit() {
    /**
     * Uniform worn by the staff member (dimensions are set to zero for simplicity).
     */
    override val uniform: Uniform = Uniform(width = 0F, length = 0F)

    /**
     * Default processing time for an order assigned to the staff member.
     */
    abstract val defaultProcessingTime: Float

    /**
     * Maximum efficiency bonus that can be applied to the staff member.
     */
    abstract val maxEfficiencyBonus: Float

    /**
     * Default efficiency level of the staff member.
     */
    abstract val defaultEfficiency: Float

    /**
     * Maximum efficiency level that the staff member can reach.
     */
    abstract val maxEfficiency: Float

    /**
     * Factor by which efficiency increases with each processed order.
     */
    abstract val efficiencyIncreaseFactor: Float

    /**
     * List of orders assigned to the staff member.
     */
    private val _orders: MutableList<Order> = mutableListOf()
    val orders: List<Order> = _orders
    var current: Int = 0
        private set

    /**
     * Current efficiency level of the staff member.
     */
    val efficiency: Float
        get() = (defaultEfficiency + efficiencyIncreaseFactor * current).let {
            if (it > maxEfficiency) maxEfficiency else it
        }

    /**
     * Processing time for the staff member, considering efficiency and max efficiency bonus.
     */
    val processingTime: Float
        get() = defaultProcessingTime - maxEfficiencyBonus * efficiency

    /**
     * Total time passed for orders being processed by the staff member.
     */
    private var timePassed: Float = 0F

    /**
     * Approximate remaining time for processing the remaining orders.
     */
    val approximateRemainingTime get() = max(a = (orders.size - current) * processingTime - timePassed, b = 0f)

    /**
     * Updates the state of the staff member based on the elapsed time.
     *
     * @param delta Time elapsed since the last update, in seconds.
     */
    fun update(delta: Float) {
        if (current >= orders.size) return

        timePassed += delta
        if (timePassed >= processingTime)
            pickNextOrder()
    }

    /**
     * Picks the next order for processing and updates the state accordingly.
     */
    private fun pickNextOrder() {
        timePassed -= processingTime
        _orders[current].status = OrderStatus.Processed
        current++
        if (current < orders.size)
            _orders[current].status = OrderStatus.Processing

    }

    /**
     * Adds a new order to the list of orders assigned to the staff member.
     *
     * @param order order to be added.
     */
    fun addOrderForSignature(order: Order) = _orders.add(order)
}
