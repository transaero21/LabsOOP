package ru.transaero21.mt.models.core

import org.junit.jupiter.api.Test
import ru.transaero21.mt.models.ammo.bullet.BulletImpl
import ru.transaero21.mt.models.core.orders.Move
import ru.transaero21.mt.models.core.orders.Order
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.fighter.FighterTest
import ru.transaero21.mt.models.units.fighters.FieldCommander
import ru.transaero21.mt.models.units.fighters.FieldCommander.Companion.makeCommander
import ru.transaero21.mt.models.units.fighters.Fighter
import ru.transaero21.mt.models.units.fighters.Formation
import ru.transaero21.mt.models.units.managers.Commander
import ru.transaero21.mt.models.units.staff.StaffImpl

class GameInfoTest {
    private val staffCount = 2
    private val defaultTime = 300f
    private val defaultRadius = 16f
    private var mapWidth: Float = 0f
    private var mapLength: Float = 0f

    private data class Initializer(val x: Float, val y: Float, val count: Int, val createFighter: (Float, Float) -> Fighter)
    private val initializerList = listOf(
        Initializer(x = 24f, y = 24f, count = 3, createFighter = { x, y -> FighterTest.ConcreteFighter(x = x, y = y) }),
        Initializer(x = 82f, y = 48f, count = 3, createFighter = { x, y -> FighterTest.ConcreteFighter(x = x, y = y) }),
        Initializer(x = 48f, y = 82f, count = 3, createFighter = { x, y -> FighterTest.ConcreteFighter(x = x, y = y) }),
        Initializer(x = 148f, y = 48f, count = 3, createFighter = { x, y -> FighterTest.ConcreteFighter(x = x, y = y) }),
        Initializer(x = 48f, y = 148f, count = 3, createFighter = { x, y -> FighterTest.ConcreteFighter(x = x, y = y) })
    )

    private fun getNewGame(mapWidth: Float, mapLength: Float): GameInfo {
        this@GameInfoTest.mapWidth = mapWidth
        this@GameInfoTest.mapLength = mapLength

        var id = 0
        val nextId = { id++ }

        val gameInfo = GameInfo(
            headquarters = getHeadquarter(isLeft = true) to getHeadquarter(isLeft = false),
            timeMax = defaultTime, mapWidth = mapWidth, mapLength = mapLength
        )

        gameInfo.headquarters.first.ammunition[0] = BulletImpl(0f, 0f, 0f, 0f)
        initializerList.forEach { init ->
            repeat(2) {
                val isLeft = it % 2 == 1
                val fl = createFormation(initializer = init.project(isLeft = isLeft), nextId = nextId)
                val hq = if (isLeft) gameInfo.headquarters.first else gameInfo.headquarters.second
                hq.commander.fieldCommanders[nextId()] = fl
            }
        }

        repeat(times = staffCount * 2) {
            val hq = if (it % 2 == 0) gameInfo.headquarters.first else gameInfo.headquarters.second
            getNextStaff(hq = hq, nextId = nextId)
        }

        return gameInfo
    }

    private fun getHeadquarter(isLeft: Boolean): Headquarter {
        return Headquarter(
            x = if (isLeft) 0f else mapWidth,
            y = if (isLeft) 0f else mapLength,
            commander = Commander(
                fullName = "Default Name",
                rank = Rank("Default", 0f)
            )
        )
    }

    private fun getNextStaff(hq: Headquarter, nextId: () -> Int) {
        hq.commander.staff[nextId()] = StaffImpl()
    }

    private fun createFormation(initializer: Initializer, nextId: () -> Int): FieldCommander {
        val formation = Formation(defaultRadius)
        repeat(initializer.count) {
            formation.fighters[nextId()] = initializer.createFighter(initializer.x, initializer.y)
        }

        return initializer.createFighter(initializer.x, initializer.y).makeCommander(formation = formation).also {
            it.conveyOrder(Move(x = initializer.x, y = initializer.y, fcId = -1))
        }
    }

    private fun Initializer.project(isLeft: Boolean): Initializer = Initializer(
        x = if (isLeft) this.x else mapWidth - this.x,
        y = if (isLeft) this.y else mapWidth - this.y,
        count = this.count, createFighter = this.createFighter
    )

    @Test
    fun testLaunchGame() {
        val gameInfo = getNewGame(1024f, 1024f)
        gameInfo.update(10f, emptyList<Order>() to emptyList<Order>())
    }
}
