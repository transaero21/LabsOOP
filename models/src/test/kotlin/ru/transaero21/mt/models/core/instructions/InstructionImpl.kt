package ru.transaero21.mt.models.core.instructions

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter

class InstructionImpl: Instruction() {
    override fun execute(delta: Float, self: Fighter, fWrapper: FighterWrapper) {
        TODO("Not yet implemented")
    }

    override fun checkCompatability(self: Fighter): Boolean {
        TODO("Not yet implemented")
    }
}