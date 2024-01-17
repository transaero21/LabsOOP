package ru.transaero21.mt.models.core

import ru.transaero21.mt.models.core.orders.Order

class GameInfo(
    val headquarters: Pair<Headquarter, Headquarter>,
    val mapWidth: Float,
    val mapLength: Float,
    val timeMax: Float,
) {
    var timeLeft: Float = timeMax
        private set

    fun update(deltaTime: Float, orders: Pair<List<Order>, List<Order>>): Boolean {
        if (timeLeft <= 0) return false
        timeLeft -= deltaTime

        headquarters.first.let { hq ->
            hq.update(
                deltaTime = deltaTime,
                orders = orders.first,
                iWrapper = hq.getIteratorWrapper(enemy = headquarters.second)
            )
        }

        headquarters.second.let { hq ->
            hq.update(
                deltaTime = deltaTime,
                orders = orders.first,
                iWrapper = hq.getIteratorWrapper(enemy = headquarters.first)
            )
        }

        headquarters.first.finalizePositions()
        headquarters.second.finalizePositions()

//        headquarters.first.checkAmmoHit(enemy = headquarters.second)
//        headquarters.second.checkAmmoHit(enemy = headquarters.first)
//
//        headquarters.first.cleanUpFighters()
//        headquarters.second.cleanUpFighters()

        return true
    }
}
