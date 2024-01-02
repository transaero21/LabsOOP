package ru.transaero21.mt.core.instructions

import ru.transaero21.mt.core.FighterWrapper
import ru.transaero21.mt.units.fighters.Fighter

abstract class Instruction {
    var status: InstructionStatus = InstructionStatus.Received

    fun defaultDecision(ret: Boolean?) {
        when (ret) {
            true -> {
                if (status == InstructionStatus.Received) {
                    status = InstructionStatus.Started
                }
            }

            false -> {
                status = if (status == InstructionStatus.Started) {
                    InstructionStatus.Done
                } else {
                    InstructionStatus.Failed
                }
            }

            null -> status = InstructionStatus.Failed
        }
    }

    abstract fun execute(delta: Float, self: Fighter, fWrapper: FighterWrapper)
    abstract fun checkCompatability(self: Fighter): Boolean
}
