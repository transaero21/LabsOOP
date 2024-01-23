package ru.transaero21.mt.models.core.orders

import ru.transaero21.mt.models.core.IteratorWrapper
import ru.transaero21.mt.models.core.instructions.Heal
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.core.instructions.Move
import ru.transaero21.mt.models.units.fighters.Fighter

class Heal(x: Float, y: Float, fcId: Int) : Order(x = x, y = y, fcId = fcId) {
    override fun getInstructions(iWrapper: IteratorWrapper) : List<Instruction>  {
        return listOf(Move(x = x, y = y))
    }
}
