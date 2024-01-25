package ru.transaero21.mt.ui.screens.game.helpers

import ru.transaero21.mt.models.core.GameInfo
import ru.transaero21.mt.models.core.Headquarter
import ru.transaero21.mt.models.core.orders.Move
import ru.transaero21.mt.models.units.fighters.*
import ru.transaero21.mt.models.units.fighters.FieldCommander.Companion.makeCommander
import ru.transaero21.mt.models.units.managers.Commander
import ru.transaero21.mt.models.units.managers.CommonStaff
import ru.transaero21.mt.ui.screens.game.WorldMap
import ru.transaero21.mt.utils.RandomizerUtils

object GameHelper {
    private const val STAFF_COUNT = 2
    private const val DEFAULT_TIME = 300f
    private const val DEFAULT_RADIUS = 16f
    private var mapWidth: Float = 0f
    private var mapLength: Float = 0f

    private data class Initializer(val x: Float, val y: Float, val count: Int, val createFighter: (Float, Float) -> Fighter)
    private val initializerList = listOf(
        Initializer(x = 24f, y = 24f, count = 3, createFighter = { x, y -> Doctor(initX = x, initY = y) }),
        Initializer(x = 82f, y = 48f, count = 3, createFighter = { x, y -> Grenadier(initX = x, initY = y) }),
        Initializer(x = 48f, y = 82f, count = 3, createFighter = { x, y -> Infantry(initX = x, initY = y) }),
        Initializer(x = 148f, y = 48f, count = 3, createFighter = { x, y -> Infantry(initX = x, initY = y) }),
        Initializer(x = 48f, y = 148f, count = 3, createFighter = { x, y -> Infantry(initX = x, initY = y) })
    )

    fun getNewGame(mapWidth: Float, mapLength: Float, timestamp: Long): GameInfo {
        RandomizerUtils.setupNewGame(timestamp = timestamp)
        this@GameHelper.mapWidth = mapWidth
        this@GameHelper.mapLength = mapLength

        var id = 0
        val nextId = { id++ }

        val gameInfo = GameInfo(
            headquarters = getHeadquarter(isLeft = true) to getHeadquarter(isLeft = false),
            timeMax = DEFAULT_TIME, mapWidth = mapWidth, mapLength = mapLength
        )

        initializerList.forEach { init ->
            repeat(2) {
                val isLeft = it % 2 == 1
                val fl = createFormation(initializer = init.project(isLeft = isLeft), nextId = nextId)
                val hq = if (isLeft) gameInfo.headquarters.first else gameInfo.headquarters.second
                hq.commander.fieldCommanders[nextId()] = fl
            }
        }

        repeat(times = STAFF_COUNT * 2) {
            val hq = if (it % 2 == 0) gameInfo.headquarters.first else gameInfo.headquarters.second
            getNextStaff(hq = hq, nextId = nextId)
        }

        return gameInfo
    }

    private fun getHeadquarter(isLeft: Boolean): Headquarter {
        return Headquarter(
            x = if (isLeft) 0f else WorldMap.TILE_WIDTH * mapWidth,
            y = if (isLeft) 0f else WorldMap.TILE_LENGTH * mapLength,
            commander = Commander(
                fullName = RandomizerUtils.getNextName(),
                rank = RandomizerUtils.getNextRank()
            )
        )
    }

    private fun getNextStaff(hq: Headquarter, nextId: () -> Int) {
        hq.commander.staff[nextId()] = CommonStaff(
            rank = RandomizerUtils.getNextRank(),
            fullName = RandomizerUtils.getNextName()
        )
    }

    private fun createFormation(initializer: Initializer, nextId: () -> Int): FieldCommander {
        val formation = Formation(DEFAULT_RADIUS)
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
}
