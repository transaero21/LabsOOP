package ru.transaero21.mt.models.core.instructions

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.Fighter

abstract class Instruction {
    var status: InstructionStatus = InstructionStatus.Received

    /**
     * Default decision handling based on the return value.
     *
     * @param ret return value indicating the success or failure of the instruction.
     */
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

    /**
     * Abstract method to be implemented by subclasses to execute the instruction.
     *
     * @param delta time delta for execution.
     * @param self fighter executing the instruction.
     * @param fWrapper fighter wrapper containing additional information.
     */
    abstract fun execute(delta: Float, self: Fighter, fWrapper: FighterWrapper)

    /**
     * Abstract method to be implemented by subclasses to check compatibility with the executing fighter.
     *
     * @param self fighter executing the instruction.
     * @return true if compatible, false otherwise.
     */
    abstract fun checkCompatability(self: Fighter): Boolean
}
