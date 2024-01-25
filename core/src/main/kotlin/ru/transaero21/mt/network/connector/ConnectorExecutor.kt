package ru.transaero21.mt.network.connector

import com.badlogic.gdx.net.Socket
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ru.transaero21.mt.network.Command
import ru.transaero21.mt.network.CommandParser
import ru.transaero21.mt.network.ConnectionState
import ru.transaero21.mt.network.NetworkManager
import kotlin.coroutines.CoroutineContext

abstract class ConnectorExecutor {
    abstract val socket: Socket
    abstract val state: ConnectionState

    abstract val job: Job

    abstract fun parseCommand(command: Command)
    abstract fun setDisconnected()

    fun launchListener(context: CoroutineContext): Job {
        return KtxAsync.launch(context = context) {
            while (true) {
                try {
                    parseCommand(command = CommandParser.readFromSocket(socket))
                } catch (e: Exception) {
                    break
                }
            }
            if (!NetworkManager.killInProgress) {
                NetworkManager.killAll()
            }
        }
    }

    @Synchronized fun sendCommand(command: Command) {
        with(socket.outputStream) {
            write(command.toString().toByteArray())
            flush()
        }
    }

    suspend fun kill() {
        socket.dispose()
        job.cancelAndJoin()
        setDisconnected()
    }
}
