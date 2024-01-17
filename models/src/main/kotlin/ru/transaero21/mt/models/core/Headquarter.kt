package ru.transaero21.mt.models.core

import ru.transaero21.map.OrderedMap
import ru.transaero21.mt.models.ammo.Ammunition
import ru.transaero21.mt.models.ammo.bullet.Bullet
import ru.transaero21.mt.models.ammo.bullet.BulletState
import ru.transaero21.mt.models.ammo.mine.Mine
import ru.transaero21.mt.models.ammo.mine.MineState
import ru.transaero21.mt.models.core.iterators.FighterIterator
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.core.orders.OrderStatus
import ru.transaero21.mt.models.units.fighters.skills.Skill.Companion.haveSkill
import ru.transaero21.mt.models.units.fighters.skills.defusing.DefaultDefusing
import ru.transaero21.mt.models.units.managers.Commander

class Headquarter(val x: Float, val y: Float, val commander: Commander) {
    val ammunition: OrderedMap<Int, Ammunition> = OrderedMap()

    fun update(deltaTime: Float, orders: List<Order>, iWrapper: IteratorWrapper) {
        commander.staff.forEach { (_, staff) ->
            for (i in staff.current - 1 downTo 0) {
                val order = staff.orders[i]
                when (order.status) {
                    OrderStatus.Processed -> {
                        commander.fieldCommanders[order.fcId]?.conveyOrder(order = order)
                        order.status = OrderStatus.Assigned
                    }
                    OrderStatus.Assigned -> break
                    else -> { /* Do nothing */ }
                }
            }
        }

        orders.forEach { order ->
            commander.handleNewOrder(order = order)
        }

        commander.update(
            deltaTime = deltaTime,
            fWrapper = FighterWrapper(iterator = iWrapper, populateAmmo = { ammunition[it.hashCode()] = it })
        )
    }

    fun finalizePositions() {
        FighterIterator(headquarter = this).forEachRemaining { (_, fighter) ->
            fighter.finalizePosition()
        }
    }

    fun checkAmmoHit(enemy: Headquarter) {
        val fIterator = FighterIterator(headquarter = enemy)
        for ((_, fighter) in fIterator) {
            for ((_, ammo) in ammunition) {
                val (rw, rh) = fighter.uniform.width to fighter.uniform.length
                val (rx, ry) = fighter.x - rw / 2 to fighter.y - rh / 2
                when (ammo) {
                    is Bullet -> {
                        if (ammo.x >= rx && ammo.x <= rx + rw && ammo.y >= ry && ammo.y <= ry + rh) {
                            fighter.applyHit(damage = ammo.damage)
                            ammo.state = BulletState.Dispose
                            break
                        }
                    }
                    is Mine -> {
                        if (ammo.state == MineState.Ready && !fighter.haveSkill<DefaultDefusing>()) {
                            if (Calculations.circleIntersectRect(
                                    x1 = fighter.x, y1 = fighter.y,
                                    x2 = fighter.x + rw, y2 = fighter.y + rh,
                                    x = ammo.x, y = ammo.y, r = ammo.hitRange
                                )
                            ) {
                                fighter.applyHit(damage = ammo.damage)
                                ammo.state = MineState.Exploded
                                break
                            }
                        }
                    }
                }
            }
        }
    }

    fun cleanUpFighters() {
        for ((fcId, fc) in commander.fieldCommanders) {
            if (fc.healthPercentage <= 0F)
                commander.fieldCommanders.remove(key = fcId)
            for ((fId, f) in fc.formation.fighters) {
                if (f.healthPercentage <= 0F)
                    fc.formation.fighters.remove(key = fId)
            }
        }
    }
}
