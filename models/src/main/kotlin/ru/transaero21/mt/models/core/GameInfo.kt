package ru.transaero21.mt.models.core

import ru.transaero21.mt.models.core.orders.Order

class GameInfo(
    val headquarters: Pair<Headquarter, Headquarter>,
    private val mapWidth: Float,
    private val mapLength: Float,
    private val timeMax: Float,
) {
    var timeCurrent: Float = timeMax
        private set

    fun update(deltaTime: Float, orders: Pair<List<Order>, List<Order>>): Boolean {
        if (timeCurrent >= timeMax) return false
        timeCurrent += deltaTime

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

        headquarters.first.checkAmmoHit(enemy = headquarters.second)
        headquarters.second.checkAmmoHit(enemy = headquarters.first)

        headquarters.first.cleanUpFighters()
        headquarters.second.cleanUpFighters()

        return true
    }
}
