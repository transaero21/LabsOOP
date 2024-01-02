package ru.transaero21.mt.core.orders

import ru.transaero21.mt.ammo.Ammunition
import ru.transaero21.mt.core.IteratorWrapper
import ru.transaero21.mt.core.instructions.Defuse
import ru.transaero21.mt.core.instructions.Instruction
import ru.transaero21.mt.core.instructions.Move

class Defuse(private val defusableId: Int, fcId: Int) : Order(fcId = fcId) {
    override fun getInstructions(iWrapper: IteratorWrapper) : List<Instruction>  {
        var ammo: Ammunition? = null
        iWrapper.eaIterator().forEachRemaining {
            if (it.key == defusableId) ammo = it.value
        }
        return ammo?.let { listOf(Move(x = it.x, y = it.y), Defuse) } ?: emptyList()
    }
}
