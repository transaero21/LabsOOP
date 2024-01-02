package ru.transaero21.mt.models.core.orders

import ru.transaero21.mt.models.core.IteratorWrapper
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.core.instructions.Move

class Move(val x: Float, val y: Float, fcId: Int) : Order(fcId = fcId) {
    override fun getInstructions(iWrapper: IteratorWrapper) : List<Instruction>  {
        return listOf(Move(x = x, y = y))
    }
}
