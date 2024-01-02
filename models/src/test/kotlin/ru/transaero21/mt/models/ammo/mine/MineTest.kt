package ru.transaero21.mt.models.ammo.mine

import org.junit.jupiter.api.Test
import ru.transaero21.mt.models.DELTA_TIME
import ru.transaero21.mt.models.getRealFrames
import kotlin.math.ceil
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MineTest {
    @Test
    fun mineDoesNotDefuseDuringTransitTest() {
        val mine: Mine = MineImpl(x = 0F, y = 0F, angle = 0F, velocity = 0F)
        mine.state = MineState.Transit
        assertTrue(actual = !mine.defuse(deltaTime = DELTA_TIME, isGoodStep = true))
    }

    @Test
    fun mineIsReadyAfterMaxDistanceTest() {
        val mineVelocity = 100F
        val mine: Mine = MineImpl(x = 0F, y = 0F, angle = 0F, velocity = mineVelocity)

        val frames = getRealFrames(
            deltaTime = DELTA_TIME,
            ceil(x = mine.distanceMax / mine.velocity / DELTA_TIME + 1).toInt()
        )
        repeat(times = frames) {
            mine.update(deltaTime = DELTA_TIME)
        }

        assertTrue(actual = mine.distanceCurrent >= mine.distanceMax)
        assertEquals(expected = MineState.Ready, actual = mine.state)
    }

    @Test
    fun mineDefuseSuccessfulTest() {
        val mine: Mine = MineImpl(x = 0F, y = 0F, angle = 0F, velocity = 0F)
        mine.state = MineState.Ready

        val frames = getRealFrames(
            deltaTime = DELTA_TIME,
            ceil(x = mine.defuseTime / DELTA_TIME + 1).toInt()
        )

        repeat(times = frames) {
            mine.update(deltaTime = DELTA_TIME)
            mine.defuse(deltaTime = DELTA_TIME, isGoodStep = true)
        }

        assertEquals(expected = MineState.Defused, actual = mine.state)
        assertTrue(actual = mine.timePassed >= mine.defuseTime)
    }

    @Test
    fun mineDefuseFailedTest() {
        val mine: Mine = MineImpl(x = 0F, y = 0F, angle = 0F, velocity = 0F)
        mine.state = MineState.Ready

        val frames = getRealFrames(
            deltaTime = DELTA_TIME,
            ceil(x = mine.defuseTime / DELTA_TIME + 1).toInt()
        )

        repeat(times = frames - 1) {
            mine.update(deltaTime = DELTA_TIME)
            mine.defuse(deltaTime = DELTA_TIME, isGoodStep = true)
        }
        // Step is bad on final defuse try
        mine.defuse(deltaTime = DELTA_TIME, isGoodStep = false)

        assertEquals(expected = MineState.Exploded, actual = mine.state)
        assertTrue(actual = mine.timePassed >= mine.defuseTime)
    }
}