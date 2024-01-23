package ru.transaero21.mt.models.core.orders

import ru.transaero21.mt.models.core.IteratorWrapper
import ru.transaero21.mt.models.core.instructions.Attack
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.core.instructions.Move

class Attack(x: Float, y: Float, fcId: Int) : Order(x = x, y = y, fcId = fcId) {
    override fun getInstructions(iWrapper: IteratorWrapper) : List<Instruction>  {
        return listOf(Move(x = x, y = y), Attack)
    }
}