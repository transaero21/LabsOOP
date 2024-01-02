package ru.transaero21.mt.core.orders

import ru.transaero21.mt.core.IteratorWrapper
import ru.transaero21.mt.core.instructions.Instruction
import ru.transaero21.mt.core.instructions.Move

class Move(val x: Float, val y: Float, fcId: Int) : Order(fcId = fcId) {
    override fun getInstructions(iWrapper: IteratorWrapper) : List<Instruction>  {
        return listOf(Move(x = x, y = y))
    }
}
