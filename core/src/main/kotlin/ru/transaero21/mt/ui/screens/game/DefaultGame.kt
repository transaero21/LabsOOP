package ru.transaero21.mt.ui.screens.game

import ru.transaero21.mt.models.core.GameInfo
import ru.transaero21.mt.models.core.Headquarter
import ru.transaero21.mt.models.core.instructions.Move
import ru.transaero21.mt.models.units.fighters.Doctor
import ru.transaero21.mt.models.units.Rank
import ru.transaero21.mt.models.units.fighters.FieldCommander.Companion.makeCommander
import ru.transaero21.mt.models.units.fighters.Formation
import ru.transaero21.mt.models.units.managers.Commander
import ru.transaero21.mt.models.units.managers.CommonStaff
import ru.transaero21.mt.models.units.managers.Staff
import ru.transaero21.mt.ui.screens.game.WorldMap.Companion.MAP_SIZE
import ru.transaero21.mt.ui.screens.game.WorldMap.Companion.TILE_WIDTH
import ru.transaero21.mt.utils.RandomizerUtils

private const val STAFF_COUNT = 2

fun getNewGame(mapWidth: Float, mapLength: Float, timestamp: Long): GameInfo {
    RandomizerUtils.setupNewGame(timestamp = timestamp)

    var id = 0
    val nextId = { id++ }

    val gameInfo = GameInfo(
        headquarters =
        Headquarter(
            x = 0F, y = 0F, commander = Commander(
                fullName = "Blue",
                rank = Rank(title = "Commander", weight = 1f)
            )
        ) to Headquarter(
            x = TILE_WIDTH * MAP_SIZE, y = TILE_WIDTH * MAP_SIZE, commander = Commander(
                fullName = "Red",
                rank = Rank(title = "Commander", weight = 1f)
            )
        ),
        timeMax = 60F * 5F,
        mapWidth = mapWidth,
        mapLength = mapLength
    )

    val list = List(2) { Formation().apply {
        repeat(2) {
            Doctor(fullName = "Doctor $it", initX = 16F, initY = 16F * it).let { doc ->
                doc.conveyInstructions(listOf(Move(x = 1000f, y = 1000f)))
                fighters[doc.hashCode()] = doc
            }
        }
    } }.map { Doctor(fullName = "Doctor ${id++}", initX = 16F, initY = 16F * id).makeCommander(formation = it) }

    gameInfo.headquarters.first.commander.fieldCommanders[id++] = list[0]

    repeat(times = STAFF_COUNT * 2) {
        val hq = if (it % 2 == 0) gameInfo.headquarters.first else gameInfo.headquarters.second
        getNextStaff(hq = hq, nextId = nextId)
    }

    return gameInfo
}

private fun getNextStaff(hq: Headquarter, nextId: () -> Int) {
    hq.commander.staff[nextId()] = CommonStaff(
        rank = RandomizerUtils.getNextRank(),
        fullName = RandomizerUtils.getNextName()
    )
}

