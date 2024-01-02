package ru.transaero21.mt.units.fighters

import ru.transaero21.mt.core.FighterWrapper
import ru.transaero21.mt.core.IteratorWrapper
import ru.transaero21.mt.core.orders.Order
import ru.transaero21.mt.units.Rank
import ru.transaero21.mt.units.Uniform
import ru.transaero21.mt.units.fighters.skills.*
import ru.transaero21.mt.weapon.Weapon

abstract class FieldCommander(var formation: Formation, initX: Float, initY: Float) : Fighter(initX, initY)  {
    private val incomingOrders: MutableList<Order> = mutableListOf()

    override fun update(delta: Float, fWrapper: FighterWrapper) {
        super.update(delta = delta, fWrapper = fWrapper)

        handleIncomingOrders(iWrapper = fWrapper.iterator)

        formation.update(deltaTime = delta, fWrapper = fWrapper)
    }

    private fun handleIncomingOrders(iWrapper: IteratorWrapper) {
        incomingOrders.forEach { order ->
            val lookUpFighter = { fighter: Fighter ->
                order.getInstructions(iWrapper = iWrapper).let { instructions ->
                    if (instructions.firstOrNull { it.checkCompatability(self = fighter) } != null) {
                        fighter.conveyInstructions(instructions = instructions)
                    }
                }
            }

            for ((_, fighter) in formation.fighters)
                lookUpFighter(fighter)
            lookUpFighter(this)
        }
        incomingOrders.clear()
    }

    fun conveyOrder(order: Order) = incomingOrders.add(order)

    companion object {
        fun Fighter.makeCommander(
            formation: Formation
        ) = object : FieldCommander(formation = formation, initX = x, initY = y) {
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
