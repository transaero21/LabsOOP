package ru.transaero21.mt.network.connector.host

import com.badlogic.gdx.net.Socket
import kotlinx.coroutines.*
import ktx.async.newSingleThreadAsyncContext
import ru.transaero21.mt.network.Command
import ru.transaero21.mt.network.ConnectionState
import ru.transaero21.mt.network.connector.ConnectorExecutor

class HostConnectorExecutor(override val socket: Socket): ConnectorExecutor() {
    private val context = newSingleThreadAsyncContext(threadName = THREAD_NAME)

    private var _state = ConnectionState.Connected
    override val state get() = _state

    override val job: Job = launchListener(context = context)

    override fun parseCommand(command: Command) {
        when (_state) {
            ConnectionState.Connecting -> {
                _state = ConnectionState.Connected
            }
            ConnectionState.Connected -> {

            }
            else -> { /* Do Nothing */ }
        }
    }

    override fun setDisconnected() {
        _state = ConnectionState.Disconnected
    }

    companion object {
        private const val THREAD_NAME = "HostConnectorExecutor-Thread"
    }
}