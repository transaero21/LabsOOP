package ru.transaero21.mt.models.units.formation

import org.mockito.Mockito.*
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyZeroInteractions
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.units.fighters.FieldCommander
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.models.units.fighters.Formation
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class FormationTest {

    @Test
    fun testUpdate() {
        val approachRadius = 5f
        val formation = Formation(approachRadius)

        val fighter1 = mock<Fighter>()
        val fighter2 = mock<Fighter>()

        formation.fighters[1] = fighter1
        formation.fighters[2] = fighter2

        val deltaTime = 0.1f
        val fWrapper = mock<FighterWrapper>()

        formation.update(deltaTime, fWrapper)

        verify(fighter1).update(deltaTime, fWrapper)
        verify(fighter2).update(deltaTime, fWrapper)
    }

    @Test
    fun testGetExpectedPosition() {
        val approachRadius = 5f
        val formation = Formation(approachRadius)

        val fieldCommander = mock<FieldCommander>()
        `when`(fieldCommander.x).thenReturn(10f)
        `when`(fieldCommander.y).thenReturn(15f)

        val fighter1 = mock(Fighter::class.java)
        val fighter2 = mock(Fighter::class.java)

        formation.fighters[1] = fighter1
        formation.fighters[2] = fighter2

        val expectedPositions = formation.getExpectedPosition(fieldCommander)

        assertNotNull(expectedPositions)
        assertEquals(2, expectedPositions.size)
    }

    @Test
    fun testGetExpectedPositions() {
        val approachRadius = 5f
        val formation = Formation(approachRadius)

        val x = 10f
        val y = 15f

        val fighter1 = mock(Fighter::class.java)
        val fighter2 = mock(Fighter::class.java)

        formation.fighters[1] = fighter1
        formation.fighters[2] = fighter2

        val expectedPositions = formation.getExpectedPositions(x, y)

        assertNotNull(expectedPositions)
        assertEquals(2, expectedPositions.size)

        verifyZeroInteractions(fighter1, fighter2)
    }
}