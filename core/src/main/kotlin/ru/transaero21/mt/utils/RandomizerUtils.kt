package ru.transaero21.mt.utils

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Json
import ru.transaero21.mt.models.units.Rank
import kotlin.math.abs
import kotlin.random.Random

object RandomizerUtils {
    private const val NAMES_FILE = "data/names.json"
    private const val RANKS_FILE = "data/ranks.json"

    private val json = Json()
    private val namesList: List<LName> = readFromFile(type = NameWrapper::class.java, file = NAMES_FILE).values
    private val ranksList: List<LRank> = readFromFile(type = RankWrapper::class.java, file = RANKS_FILE).values
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

    private fun <T> readFromFile(type: Class<T> , file: String): T {
        return json.fromJson(type, Gdx.files.internal(file))
    }

    class NameWrapper(var values: MutableList<LName> = mutableListOf())
    class LName(var firstName: String = "", var lastName: String = "")
    class RankWrapper(var values: MutableList<LRank> = mutableListOf())
    class LRank(var rank: String = "")
}
