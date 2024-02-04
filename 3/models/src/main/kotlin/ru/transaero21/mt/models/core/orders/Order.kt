package ru.transaero21.mt.models.core.orders

import kotlinx.serialization.Serializable
import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.units.fighters.FieldCommander

/**
 * A sealed class representing different types of orders that can be issued to field commanders.
 *
 * @property x x-coordinate of the order.
 * @property y y-coordinate of the order.
 * @property fcId id of the field commander associated with the order.
 */
@Serializable
sealed class Order(val x: Float, val y: Float, var fcId: Int) {
    var status: OrderStatus = OrderStatus.Created

    /**
     * Abstract method to be implemented by subclasses to get instructions based on the order.
     *
     * @param fc field commander associated with the order.
     * @return ordered map containing instructions for different units.
     */
    abstract fun getInstructions(fc: FieldCommander): OrderedMap<Int, List<Instruction>>
}
