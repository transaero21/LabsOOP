package ru.transaero21.mt.models.core.orders

import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.core.IteratorWrapper
import ru.transaero21.mt.models.core.instructions.Defuse
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.core.instructions.Move

class Defuse(x: Float, y: Float, private val defusableId: Int, fcId: Int) : Order(x = x, y = y, fcId = fcId) {
    override fun getInstructions(iWrapper: IteratorWrapper) : List<Instruction>  {
        var ammo: Ammunition? = null
        iWrapper.eaIterator().forEachRemaining {
            if (it.key == defusableId) ammo = it.value
        }
        return ammo?.let { listOf(Move(x = it.x, y = it.y), Defuse) } ?: emptyList()
    }
}
