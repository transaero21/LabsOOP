package ru.transaero21.mt.utils

import com.badlogic.gdx.Gdx
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import ru.transaero21.mt.models.units.Rank
import kotlin.math.abs
import kotlin.random.Random

object RandomizerUtils {
    private const val NAMES_FILE = "data/names.json"
    private const val RANKS_FILE = "data/ranks.json"

    private val namesList: List<LName> = readFromFile(file = NAMES_FILE)
    private val ranksList: List<LRank> = readFromFile(file = RANKS_FILE)
    private var random: Random? = null

    fun setupNewGame(timestamp: Long) {
        random = Random(seed = timestamp)
    }

    fun getNextName(): String {
        return "${namesList[nextInt() % namesList.size].firstName} ${namesList[nextInt() % namesList.size].lastName}"
    }

    fun getNextRank(): Rank {
        namesList[0].lastName
        return Rank(
            title = ranksList[nextInt() % ranksList.size].rank,
            weight = random!!.nextDouble(from = 0.01, until = 1.0).toFloat()
        )
    }

    private fun nextInt() = abs(random!!.nextInt())

    private inline fun <reified T> readFromFile(file: String): T {
        return Json.decodeFromString(Gdx.files.internal(file).readString())
    }

    @Serializable data class LName(val firstName: String, val lastName: String)
    @Serializable data class LRank(val rank: String)
}
