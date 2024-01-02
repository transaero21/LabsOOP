package ru.transaero21.mt.models.core.orders

import ru.transaero21.mt.core.orders.OrderStatus
import ru.transaero21.mt.models.core.IteratorWrapper
import ru.transaero21.mt.models.core.instructions.Instruction

abstract class Order(val fcId: Int) {
    var status: OrderStatus = OrderStatus.Created

    abstract fun getInstructions(iWrapper: IteratorWrapper): List<Instruction>
}
