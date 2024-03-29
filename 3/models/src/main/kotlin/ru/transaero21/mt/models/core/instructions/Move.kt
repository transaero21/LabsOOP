package ru.transaero21.mt.models.core.instructions

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter

class Move(val x: Float, val y: Float) : Instruction() {
    override fun execute(delta: Float, self: Fighter, fWrapper: FighterWrapper) {
        if (self.move(delta = delta, x = x, y = y)) {
            status = InstructionStatus.Done
        } else {
            if (status == InstructionStatus.Received) {
                status = InstructionStatus.Started
            }
        }
    }

    override fun checkCompatability(self: Fighter) = true
}
