package ru.transaero21.mt.ui.screens.game.helpers

import ru.transaero21.mt.models.core.GameInfo
import ru.transaero21.mt.models.core.Headquarter
import ru.transaero21.mt.models.core.instructions.Move
import ru.transaero21.mt.models.units.fighters.Doctor
import ru.transaero21.mt.models.units.fighters.FieldCommander.Companion.makeCommander
import ru.transaero21.mt.models.units.fighters.Formation
import ru.transaero21.mt.models.units.managers.Commander
import ru.transaero21.mt.models.units.managers.CommonStaff
import ru.transaero21.mt.ui.screens.game.WorldMap
import ru.transaero21.mt.utils.RandomizerUtils

object GameHelper {
    private const val STAFF_COUNT = 2
    private const val DEFAULT_TIME = 300f
    private var mapWidth: Float = 0f
    private var mapLength: Float = 0f

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

        val list = List(2) { Formation().apply {
            repeat(2) {
                id++
                Doctor(fullName = RandomizerUtils.getNextName(), initX = 0f, initY = 0f * id).let { doc ->
                    if (it == 1) doc.conveyInstructions(listOf(Move(1000f, 100f)))
                    fighters[id] = doc
                }
            }
            id++
        } }.map { Doctor(fullName = RandomizerUtils.getNextName(), initX = 16F, initY = 16F * id).makeCommander(formation = it).also {
//            it.conveyOrder(ru.transaero21.mt.models.core.orders.Move(1000f, 100f, id))
        } }

        gameInfo.headquarters.first.commander.fieldCommanders[id] = list[0]

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
}
