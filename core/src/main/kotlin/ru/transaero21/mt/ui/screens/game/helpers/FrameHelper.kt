package ru.transaero21.mt.ui.screens.game.helpers

import ru.transaero21.mt.models.core.GameInfo
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.network.Command
import java.util.concurrent.ConcurrentLinkedQueue

object FrameHelper {
    val queue: ConcurrentLinkedQueue<Command.ApplyOrder> = ConcurrentLinkedQueue()

    private const val FIXED_DELTA = 1f / 30

    private var timePassed = 0f
    private var frameCount = 0L
    val currentFrame get() = frameCount

    fun update(delta: Float, gameInfo: GameInfo) {
        timePassed += delta
        if (timePassed >= FIXED_DELTA) {
            timePassed -= FIXED_DELTA
            frameCount++
            gameInfo.update(deltaTime = delta, orders = getOrdersPair())
        }
    }

    private fun getOrdersPair(): Pair<MutableList<Order>, MutableList<Order>> {
        val (left, right) = mutableListOf<Order>() to mutableListOf<Order>()
        while (!queue.isEmpty()) {
            val order = queue.peek()
            println()
            if (order.frame > frameCount) break
            if (order.isLeft) left.add(order.order) else right.add(order.order)
            queue.poll()
        }
        return left to right
    }

    fun resetGame() {
        timePassed = 0f
        frameCount = 0L
    }
}