package ru.transaero21.mt.models.core.orders

import kotlinx.serialization.Serializable
import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.units.fighters.FieldCommander

@Serializable
sealed class Order(val x: Float, val y: Float, val fcId: Int) {
    var status: OrderStatus = OrderStatus.Created

    abstract fun getInstructions(fc: FieldCommander): OrderedMap<Int, List<Instruction>>
}
