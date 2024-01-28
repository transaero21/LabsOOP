package ru.transaero21.mt.models.units.fighters

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.weapon.Weapon

/**
 * Abstract class representing a field commander in a combat.
 *
 * @param formation formation associated with the field commander.
 * @param initX initial x-coordinate of the field commander.
 * @param initY initial y-coordinate of the field commander.
 */
abstract class FieldCommander(var formation: Formation, initX: Float, initY: Float) : Fighter(initX, initY)  {
    /**
     * List of incoming orders for the field commander.
     */
    val incomingOrders: MutableList<Order> = mutableListOf()

    /**
     * Updates the field commander, handles incoming orders, and updates the formation.
     *
     * @param delta time elapsed since the last update, in seconds.
     * @param fWrapper wrapper containing context-specific information for the field commander.
     */
    override fun update(delta: Float, fWrapper: FighterWrapper) {
        super.update(delta = delta, fWrapper = fWrapper)
        handleIncomingOrders()
        formation.update(deltaTime = delta, fWrapper = fWrapper)
    }

    /**
     * Handles incoming orders by conveying instructions to the relevant fighters in the formation.
     */
    private fun handleIncomingOrders() {
        incomingOrders.forEach { order ->
            order.getInstructions(fc = this).forEach { (k, v) ->
                (if (k != -1) formation.fighters[k] else this)?.let { fighter ->
                    fighter.abortInstructions()
                    fighter.conveyInstructions(v)
                }
            }
        }
        incomingOrders.clear()
    }

    /**
     * Conveys an order to the field commander, which will be executed by the formation's fighters.
     *
     * @param order order to be conveyed to the field commander.
     */
    fun conveyOrder(order: Order) = incomingOrders.add(order)

    companion object {
        /**
         * Extension function for creating a [FieldCommander] from an existing [Fighter].
         * Converts the provided [Fighter] instance into a field commander, inheriting its properties.
         *
         * @receiver base [Fighter] instance to be converted into a [FieldCommander].
         * @param formation formation to be assigned to the created field commander.
         * @return [FieldCommander] instance inheriting properties from the original [Fighter].
         */
        fun Fighter.makeCommander(formation: Formation): FieldCommander {
            return object : FieldCommander(formation = formation, initX = x, initY = y) {
                override val healthMax: Float = this@makeCommander.healthMax
                override val skills: Set<Skill> = this@makeCommander.skills
                override val weapon: Weapon = this@makeCommander.weapon
                override val speed: Float = this@makeCommander.speed
                override val fullName: String = this@makeCommander.fullName
                override val rank: Rank = this@makeCommander.rank
                override val uniform: Uniform = this@makeCommander.uniform
            }
        }
    }
}
