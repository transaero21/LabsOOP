package ru.transaero21.mt.models.units.fieldCommander

import org.mockito.kotlin.mock
import ru.transaero21.mt.models.core.FighterWrapper
import ru.transaero21.mt.models.core.orders.Heal
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.Uniform
import ru.transaero21.mt.models.units.fighters.FieldCommander.Companion.makeCommander
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.models.units.fighters.Formation
import ru.transaero21.mt.models.units.fighters.skills.Skill
import ru.transaero21.mt.models.weapon.Weapon
import ru.transaero21.mt.models.weapon.WeaponImpl
import kotlin.test.Test
import kotlin.test.assertEquals

class FieldCommanderTest {
    @Test
    fun testConveyOrder() {
        val fieldCommander = getFieldCommander(0f, 0f)
        fieldCommander.conveyOrder(Heal(0f, 0f, 1))

        assertEquals(1, fieldCommander.incomingOrders.size)

        val fighterWrapper = mock<FighterWrapper>()
        fieldCommander.update(1.0f, fighterWrapper)
    }

    @Test
    fun testUpdate() {
        val fieldCommander = getFieldCommander(1f, 1f)

        val fighterWrapper = mock<FighterWrapper>()
        fieldCommander.update(1.0f, fighterWrapper)
    }

    companion object {
        fun getFieldCommander(x: Float, y: Float) = object : Fighter(x, y) {
            override val healthMax: Float = 100f
            override val skills: Set<Skill> = emptySet()
            override val weapon: Weapon = WeaponImpl()
            override val speed: Float = 10f
            override val fullName: String = ""
            override val rank: Rank = Rank("", 1f)
            override val uniform: Uniform = Uniform(1f, 1f)
        }.makeCommander(formation = Formation(approachRadius = 16f))
    }
}