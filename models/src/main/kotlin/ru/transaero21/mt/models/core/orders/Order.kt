package ru.transaero21.mt.models.core.orders

import ru.transaero21.mt.models.core.IteratorWrapper
import ru.transaero21.mt.models.core.instructions.Instruction

abstract class Order(val x: Float, val y: Float, val fcId: Int) {
    var status: OrderStatus = OrderStatus.Created

    abstract fun getInstructions(iWrapper: IteratorWrapper): List<Instruction>
}
