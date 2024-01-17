package ru.transaero21.mt.ui.screens.game.helpers

import ru.transaero21.mt.models.core.GameInfo
import ru.transaero21.mt.models.core.orders.Order

object FrameHelper {
    private const val FIXED_DELTA = 1f / 30

    private var timePassed = 0f
    private var frameCount = 0L

    fun update(delta: Float, gameInfo: GameInfo) {
        timePassed += delta
        if (timePassed >= FIXED_DELTA) {
            timePassed -= FIXED_DELTA
            frameCount++
            gameInfo.update(deltaTime = delta, orders = emptyList<Order>() to emptyList())
        }
    }

    fun resetGame() {
        timePassed = 0f
        frameCount = 0L
    }
}