package ru.transaero21.mt.models.ammo.mine

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class MineTest {
    @Test
    fun testDefuse() {
        val mine: Mine = MineImpl(x = 0F, y = 0F, angle = 0F, velocity = 10f)

        mine.update(delta = 10f)
        mine.finalizePosition()
        assertEquals(expected = MineState.Ready, actual = mine.state)
        
        assertFalse(actual = mine.defuse(deltaTime = 1f))
        assertFalse(actual = mine.defuse(deltaTime = 1f))
        assertTrue(actual = mine.defuse(deltaTime = 1f))
        assertEquals(expected = MineState.Defused, actual = mine.state)

        assertFalse(actual = mine.defuse(deltaTime = 1f))
        assertEquals(expected = MineState.Defused, actual = mine.state)
    }

    @Test
    fun testUpdate() {
        val mine: Mine = MineImpl(x = 0F, y = 0F, angle = 0F, velocity = 10f)

        assertEquals(expected = MineState.Transit, actual = mine.state)

        mine.update(delta = 0.1f)
        mine.finalizePosition()
        assertEquals(expected = MineState.Transit, actual = mine.state)

        mine.update(delta = 1f)
        mine.finalizePosition()
        assertEquals(expected = MineState.Ready, actual = mine.state)

        mine.update(delta = 2f)
        mine.finalizePosition()
        assertEquals(expected = MineState.Ready, actual = mine.state)
    }
}
