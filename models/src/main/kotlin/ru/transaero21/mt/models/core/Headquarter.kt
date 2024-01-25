package ru.transaero21.mt.models.core

import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.ammo.bullet.Bullet
import ru.transaero21.mt.models.ammo.bullet.BulletState
import ru.transaero21.mt.models.ammo.mine.Mine
import ru.transaero21.mt.models.ammo.mine.MineState
import ru.transaero21.mt.models.core.iterators.AmmoIterator
import ru.transaero21.mt.models.core.iterators.FighterIterator
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.core.orders.OrderStatus
import ru.transaero21.mt.models.units.fighters.skills.Skill.Companion.haveSkill
import ru.transaero21.mt.models.units.fighters.skills.defusing.Defusing
import ru.transaero21.mt.models.units.managers.Commander

/**
 * Represents a headquarters in a combat scenario, managing fighters, ammunition, and orders.
 *
 * @property x x-coordinate of the headquarters.
 * @property y y-coordinate of the headquarters.
 * @property commander commander in charge of managing staff and field commanders.
 */
class Headquarter(val x: Float, val y: Float, val commander: Commander) {
    /**
     * collection of ammunition managed by the headquarters, mapped by their unique identifiers.
     */
    val ammunition: OrderedMap<Int, Ammunition> = OrderedMap()

    /**
     * Updates the state of the headquarters based on the elapsed time and provided orders.
     *
     * @param delta time elapsed since the last update, in seconds.
     * @param orders list of orders received by the headquarters.
     * @param iWrapper [IteratorWrapper] providing access to iterators for fighters and ammunition.
     */
    fun update(delta: Float, orders: List<Order>, iWrapper: IteratorWrapper) {
        // Processing completed orders assigned to field commanders
        commander.staff.forEach { (_, staff) ->
            for (i in staff.current - 1 downTo 0) {
                val order = staff.orders[i]
                when (order.status) {
                    OrderStatus.Processed -> {
                        commander.fieldCommanders[order.fcId]?.conveyOrder(order = order)
                        order.status = OrderStatus.Assigned
                    }

                    OrderStatus.Assigned -> break
                    else -> { /* Do nothing */
                    }
                }
            }
        }

        // Handling new orders and updating staff and field commanders
        orders.forEach { order ->
            commander.handleNewOrder(order = order)
        }

        // Update ammunition
        ammunition.forEach { (_, v) ->
            v.update(delta = delta)
            v.finalizePosition()
        }

        // Updating the commander and populating ammunition
        val ammoList = mutableListOf<Ammunition>()
        commander.update(
            delta = delta,
            fWrapper = FighterWrapper(iterator = iWrapper, populateAmmo = { ammoList.add(it) })
        )
        var ammoId = ammunition.keys.lastOrNull() ?: 0
        ammoList.sorted().forEach { ammunition[ammoId++] = it }
    }

    /**
     * Finalizes the positions of all fighters of the headquarters.
     */
    fun finalizePositions() {
        FighterIterator(headquarter = this).forEachRemaining { (_, fighter) ->
            fighter.finalizePosition()
        }
    }

    /**
     * Checks for ammunition hits on fighters from an enemy headquarters.
     *
     * @param enemy enemy headquarters.
     */
    fun checkAmmoHit(enemy: Headquarter) {
        val fIterator = FighterIterator(headquarter = enemy)
        fIterator.forEach { (_, f) ->
            ammunition.forEach { (_, a) ->
                val (rw, rh) = f.uniform.width to f.uniform.length
                val (rx, ry) = f.x - rw / 2 to f.y - rh / 2
                when (a) {
                    is Bullet -> {
                        if (a.x in rx..rx + rw && a.y in ry..ry + rh) {
                            if (a.state != BulletState.Dispose) {
                                f.applyHit(damage = a.damage)
                                a.state = BulletState.Dispose
                            }
                        }
                    }

                    is Mine -> {
                        if (!f.haveSkill<Defusing>() && Calculations.circleIntersectRect(rx, ry, rx + rw, ry + rh, a.x, a.y, a.hitRange)) {
                            if (a.state != MineState.Transit || a.state != MineState.Defused) {
                                f.applyHit(damage = a.damage)
                                a.state = MineState.Exploded
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Cleans up fighters with zero or negative health percentages from both the fighters and field commanders.
     */
    fun makeClean() {
        commander.fieldCommanders.forEach { (fcId, fc) ->
            if (fc.healthPercentage <= 0f) {
                commander.fieldCommanders.remove(key = fcId)
            }
            fc.formation.fighters.forEach { (fId, f) ->
                if (f.healthPercentage <= 0F) {
                    fc.formation.fighters.remove(key = fId)
                }
            }
        }
        ammunition.keys.filter {
            when (val ammo = ammunition[it]) {
                is Bullet -> ammo.state == BulletState.Dispose
                is Mine -> ammo.state == MineState.Defused || ammo.state == MineState.Exploded
                else -> false
            }
        }.forEach { k -> ammunition.remove(k) }
    }

    /**
     * Extension function to retrieve an IteratorWrapper for the current headquarters and an enemy headquarters.
     *
     * @param enemy enemy headquarters.
     * @return IteratorWrapper providing access to iterators for fighters and ammunition.
     */
    fun getIteratorWrapper(enemy: Headquarter) = IteratorWrapper(
        efIterator = { FighterIterator(headquarter = enemy) },
        sfIterator = { FighterIterator(headquarter = this) },
        eaIterator = { AmmoIterator(headquarter = enemy) },
        saIterator = { AmmoIterator(headquarter = this) }
    )
}
