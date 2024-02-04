package ru.transaero21.mt.network

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.models.core.orders.Order

@Serializable
sealed class Command {
    @Serializable object PingPongConnection: Command()
    @Serializable class StartGame(val time: Long, val size: WorldSize): Command()
    @Serializable class SuggestOrder(val order: Order, val isLeft: Boolean): Command()
    @Serializable class ApplyOrder(val order: Order,  val isLeft: Boolean, val frame: Long): Command()

    override fun toString(): String = "$MAGIC_PREFIX${json.encodeToString(this)}$MAGIC_SUFFIX"

    companion object {
        const val MAGIC_PREFIX = "MTCMD!"
        const val MAGIC_SUFFIX = "#"

        val json = Json {
            classDiscriminator = "class"
        }
    }
}
