package ru.transaero21.mt.models.units.fighters

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.Movable
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.core.instructions.InstructionStatus
import ru.transaero21.mt.models.units.CombatUnit
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.weapon.Weapon
import kotlin.math.cos
import kotlin.math.sin

/**
 * Abstract class representing a fighter in a combat scenario.
 */
abstract class Fighter(
    override var x: Float, override var y: Float
): CombatUnit(), Movable {
    override var futureX: Float? = null
    override var futureY: Float? = null

    /**
     * The maximum health of the fighter.
     */
    abstract val healthMax: Float

    /**
     * The current health percentage of the fighter.
     */
    var healthPercentage: Float = 1F
        private set

    /**
     * The set of skills available to the fighter.
     */
    abstract val skills: Set<Skill>

    /**
     * The weapon equipped by the fighter.
     */
    abstract val weapon: Weapon

    /**
     * The speed of the fighter.
     */
    abstract val speed: Float

    /**
     * The angle at which the fighter is facing.
     */
    var angle: Float = 0f
        private set

    /**
     * The list of instructions for the fighter to execute.
     */
    private val instructions: MutableList<Instruction> = mutableListOf()

    /**
     * The current index of the instruction being executed.
     */
    private var current: Int = 0

    open fun update(delta: Float, fWrapper: FighterWrapper) {
        if (!hasUnfulfilledInstructions()) return

        instructions[current].let { instruction ->
            instruction.execute(delta = delta, self = this, fWrapper = fWrapper)
            when (instruction.status) {
                InstructionStatus.Done, InstructionStatus.Failed -> current++
                else -> { /* Do nothing */ }
            }
        }
    }

    /**
     * Moves the fighter towards a specified position.
     *
     * @param delta time elapsed since the last move.
     * @param x target x-coordinate for the movement.
     * @param y target y-coordinate for the movement.
     * @return true if the target position is reached, false otherwise.
     */
    fun move(delta: Float, x: Float, y: Float): Boolean {
        angle = getAngle(x = x, y = y)
        val moveLength = (speed * delta).let { maxLength ->
            val targetLength = getLength(x = x, y = y)
            return@let if (targetLength >= maxLength) maxLength else targetLength
        }
        val (nx, ny) = this.x + cos(x = angle) * moveLength to this.y + sin(x = angle) * moveLength
        updatePosition(x = nx, y = ny)
        return isReached(fx = nx, fy = ny, tx = x, ty = y)
    }

    /**
     * Applies a hit to the fighter, reducing their health based on the damage received.
     * If the damage is greater than 0, it reduces the fighter's health by the specified amount.
     *
     * @param damage the amount of damage to apply to the fighter.
     * @return true if the fighter is still alive after applying the hit, false otherwise.
     */
    fun applyHit(damage: Float): Boolean {
        if (damage > 0)
            healthPercentage = (healthMax * healthPercentage - damage) / healthMax
        return healthPercentage > 0
    }

    /**
     * Applies healing to the fighter, increasing their health based on the healing received.
     * If the heal amount is greater than 0, it increases the fighter's health by the specified amount.
     *
     * @param healing the amount of healing to apply to the fighter.
     * @return true if the fighter is still alive after applying the healing, false otherwise.
     */
    fun applyHealing(healing: Float): Boolean {
        if (healing > 0)
            healthPercentage = (healthMax * healthPercentage + healing) / healthMax
        return healthPercentage > 0
    }

    /**
     * Conveys a list of instructions to the fighter for execution.
     *
     * @param instructions the list of instructions to be conveyed to the fighter.
     */
    fun conveyInstructions(instructions: List<Instruction>) {
        this.instructions.addAll(instructions)
    }

    fun hasUnfulfilledInstructions(): Boolean {
        return current < instructions.size
    }

    fun getCurrentInstructions(): Instruction? {
        return if (hasUnfulfilledInstructions()) instructions[current] else null
    }
}
