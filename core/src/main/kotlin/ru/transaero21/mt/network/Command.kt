package ru.transaero21.mt.network

import com.badlogic.gdx.utils.Json
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.models.core.orders.Move
import ru.transaero21.mt.models.core.orders.Order

sealed class Command {
    @Transient private val json = Json()

    object PingPongConnection: Command()
    class StartGame(val time: Long = 0L, val size: WorldSize = WorldSize.Medium): Command()
    class SuggestOrder(val order: Order? = null): Command()
    class ApplyOrder(val order: Order? = null, val frame: Long = 0L): Command()

    override fun toString(): String {
        return "$MAGIC_PREFIX${commandsMagic[this::class]}${json.toJson(this)}$MAGIC_SUFFIX"
    }

    companion object {
        const val MAGIC_PREFIX = "MTCMD!"
        const val MAGIC_SUFFIX = "#"

        val commandsMagic = mapOf(
            PingPongConnection::class to "PPC",
            StartGame::class to "SG",
            SuggestOrder::class to "SO",
            ApplyOrder::class to "AO"
        )
    }
}
