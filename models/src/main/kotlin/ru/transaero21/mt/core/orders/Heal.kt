package ru.transaero21.mt.core.orders

import ru.transaero21.mt.core.IteratorWrapper
import ru.transaero21.mt.core.instructions.Heal
import ru.transaero21.mt.core.instructions.Instruction
import ru.transaero21.mt.core.instructions.Move
import ru.transaero21.mt.units.fighters.Fighter

class Heal(private val woundedId: Int, fcId: Int) : Order(fcId = fcId) {
    override fun getInstructions(iWrapper: IteratorWrapper) : List<Instruction>  {
        var ammo: Fighter? = null
        iWrapper.sfIterator().forEachRemaining {
            if (it.key == woundedId) ammo = it.value
        }
        return ammo?.let { listOf(Move(x = it.x, y = it.y), Heal) } ?: emptyList()
    }
}
