package ru.transaero21.mt.models.core.orders

import kotlinx.serialization.Serializable
import ru.transaero21.mt.models.core.IteratorWrapper
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.core.instructions.Move

@Serializable
class Move: Order {
    constructor(x: Float, y: Float, fcId: Int) : super(x, y, fcId)

    override fun getInstructions(iWrapper: IteratorWrapper): List<Instruction> {
        return listOf(Move(x = x, y = y))
    }
}
