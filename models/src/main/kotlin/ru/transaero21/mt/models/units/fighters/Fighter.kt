package ru.transaero21.mt.models.units.fighters

import ru.transaero21.mt.models.core.Calculations
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.instructions.Instruction
import ru.transaero21.mt.models.core.instructions.InstructionStatus
import ru.transaero21.mt.models.units.CombatUnit
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.weapon.Weapon
import kotlin.math.cos
import kotlin.math.sign
import kotlin.math.sin

/**
 * Abstract class representing a fighter in a combat scenario.
 *
 * @param initX the initial X coordinate of the fighter.
 * @param initY the initial Y coordinate of the fighter.
 */
abstract class Fighter(initX: Float, initY: Float): CombatUnit() {
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


    private val instructions: MutableList<Instruction> = mutableListOf()


    private var current: Int = 0


    var x: Float = initX
        private set


    var y: Float = initY
        private set

    /**
     * Applies a hit to the fighter, reducing their health based on the damage received.
     * If the damage is greater than 0, it reduces the fighter's health by the specified amount.
     *
     * @param damage the amount of damage to apply to the fighter.
     * @return true if the fighter is still alive after applying the hit, false otherwise.
     */
    fun applyHit(damage: Float): Boolean {
        if (damage > 0) {
            healthPercentage = (healthMax * healthPercentage - damage) / healthMax
        }
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
        if (healing > 0) {
            healthPercentage = (healthMax * healthPercentage + healing) / healthMax
        }
        return healthPercentage > 0
    }


    open fun update(delta: Float, fWrapper: FighterWrapper) {
        if (current >= instructions.size) return

        instructions[current].let { instruction ->
            instruction.execute(delta = delta, self = this, fWrapper = fWrapper)
            when (instruction.status) {
                InstructionStatus.Done, InstructionStatus.Failed -> current++
                else -> { /* Do nothing */ }
            }
        }
    }

    /**
     * Moves the fighter towards the specified coordinates.
     *
     * @param x the target X coordinate.
     * @param y the target Y coordinate.
     * @return true if the fighter has reached the target coordinates, false otherwise.
     */
    fun move(x: Float, y: Float): Boolean {
        val targetLength = getLength(x = x, y = y)
        val realLength = targetLength * speed
        val nx = realLength * cos(x = getLength(x = x, y = this.y) / targetLength) * sign(x = x - this.x)
        val ny = realLength * sin(x = getLength(x = this.y, y = x) / targetLength) * sign(x = y - this.y)
        setPosition(x = nx, y = ny)
        return Calculations.isReached(x = this.x, y = this.y, x1 = nx, y1 = ny, x2 = x, y2 = y)
    }

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    private fun getLength(x: Float, y: Float): Float {
        return Calculations.getLength(x1 = this.x, y1 = this.y, x2 = x, y2 = y)
    }

    /**
     * Conveys a list of instructions to the fighter for execution.
     *
     * @param instructions the list of instructions to be conveyed to the fighter.
     */
    fun conveyInstructions(instructions: List<Instruction>) {
        this.instructions.addAll(instructions)
    }
}
