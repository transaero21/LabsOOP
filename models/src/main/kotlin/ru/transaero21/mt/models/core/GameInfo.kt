package ru.transaero21.mt.models.core

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import ru.transaero21.mt.models.core.iterators.FighterIterator
import ru.transaero21.mt.models.core.orders.Order

/**
 * Represents game information.
 *
 * @property headquarters pair of Headquarter instances representing the two opposing sides in the game.
 * @property mapWidth width of the game map.
 * @property mapLength length of the game map.
 * @property timeMax maximum time allowed for the game, in seconds.
 */
class GameInfo(
    val headquarters: Pair<Headquarter, Headquarter>,
    val mapWidth: Float,
    val mapLength: Float,
    val timeMax: Float,
) {
    /**
     * The remaining time left in the game, updated during each call to [update].
     */
    var timeLeft: Float = timeMax
        private set

    /**
     * Updates the game state based on elapsed time and provided orders for each headquarters.
     *
     * @param deltaTime time elapsed since the last update, in seconds.
     * @param orders pair of lists of orders, where the first list corresponds to the first headquarters, and the second to the second headquarters.
     * @return true if the game is still in progress, `false` if the time limit is reached.
     */
    fun update(deltaTime: Float, orders: Pair<List<Order>, List<Order>>): Boolean {
        if (timeLeft <= 0) return false
        timeLeft -= deltaTime
        if (timeLeft <= 0) timeLeft = 0f

        performOnBoth { hq ->
            hq.update(
                delta = deltaTime,
                orders = if (hq == headquarters.first) orders.first else orders.second,
                iWrapper = hq.getIteratorWrapper(enemy = getEnemy(headquarter = hq))
            )
        }
        performOnBoth { hq -> hq.finalizePositions() }
        performOnBoth { hq -> hq.checkAmmoHit(enemy = getEnemy(headquarter = hq)) }
        performOnBoth { hq -> hq.makeClean() }

        return getWinner() == null
    }

    /**
     * Determines the winner of the game based on the state of fighters in both headquarters.
     *
     * @return `true` if the first headquarters has no remaining fighters, `false` if the second headquarters has no remaining fighters, `null` if the game is still ongoing.
     */
    fun getWinner(): Boolean? {
        return when {
            !FighterIterator(headquarter = headquarters.first).hasNext() -> false
            !FighterIterator(headquarter = headquarters.second).hasNext() -> true
            else -> null
        }
    }

    /**
     * Performs a given action on both headquarters concurrently.
     *
     * @param perform the action to perform on each headquarters.
     */
    private fun performOnBoth(perform: (Headquarter) -> Unit) {
        runBlocking {
            val deferredList = listOf(
                async { perform(headquarters.first) },
                async { perform(headquarters.second) },
            )
            deferredList.awaitAll()
        }
    }

    /**
     * Gets the enemy headquarters for a given headquarters.
     *
     * @param headquarter the current headquarters.
     * @return the enemy headquarters.
     */
    private fun getEnemy(headquarter: Headquarter): Headquarter {
        return if (headquarter == headquarters.first) headquarters.second else headquarters.first
    }
}
