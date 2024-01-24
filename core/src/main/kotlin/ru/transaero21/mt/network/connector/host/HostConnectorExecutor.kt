package ru.transaero21.mt.network.connector.host

import com.badlogic.gdx.net.Socket
import kotlinx.coroutines.Job
import ktx.async.newSingleThreadAsyncContext
import ru.transaero21.mt.network.Command
import ru.transaero21.mt.network.ConnectionState
import ru.transaero21.mt.network.NetworkManager
import ru.transaero21.mt.network.connector.ConnectorExecutor
import ru.transaero21.mt.ui.screens.game.helpers.FrameHelper

class HostConnectorExecutor(override val socket: Socket): ConnectorExecutor() {
    private val context = newSingleThreadAsyncContext(threadName = THREAD_NAME)

    private var _state = ConnectionState.Connected
    override val state get() = _state

    override val job: Job = launchListener(context = context)

    override fun parseCommand(command: Command) {
        println(command.toString())
        when (_state) {
            ConnectionState.Connecting -> {
                _state = ConnectionState.Connected
            }
            ConnectionState.Connected -> {
                when (command) {
                    is Command.SuggestOrder -> {
                            NetworkManager.sendCommandEveryone(
                                    command = Command.ApplyOrder(
                                        order = command.order,
                                        isLeft = command.isLeft,
                                        frame = FrameHelper.currentFrame + FRAME_DIFF
                                    )
                            )
                    }
                    else -> { /* Do Nothing */ }
                }
            }
            else -> { /* Do Nothing */ }
        }
    }

    override fun setDisconnected() {
        _state = ConnectionState.Disconnected
    }

    companion object {
        private const val THREAD_NAME = "HostConnectorExecutor-Thread"
        private const val FRAME_DIFF = 6
    }
}
