package ru.transaero21.mt.models.units.fighters

import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.weapon.Weapon

abstract class FieldCommander(var formation: Formation, initX: Float, initY: Float) : Fighter(initX, initY)  {
    private val incomingOrders: MutableList<Order> = mutableListOf()

    override fun update(delta: Float, fWrapper: FighterWrapper) {
        super.update(delta = delta, fWrapper = fWrapper)
        handleIncomingOrders()
        formation.update(deltaTime = delta, fWrapper = fWrapper)
    }

    private fun handleIncomingOrders() {
        incomingOrders.forEach { order ->
            order.getInstructions(fc = this).forEach { (k, v) ->
                val fighter = if (k != -1) formation.fighters[k] else this
                fighter?.conveyInstructions(v)
            }
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
