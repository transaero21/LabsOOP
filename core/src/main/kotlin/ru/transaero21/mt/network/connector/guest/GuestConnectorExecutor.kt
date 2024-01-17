package ru.transaero21.mt.network.connector.guest

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.Socket
import kotlinx.coroutines.*
import ktx.async.newSingleThreadAsyncContext
import ru.transaero21.mt.MainGame
import ru.transaero21.mt.network.Command
import ru.transaero21.mt.network.ConnectionState
import ru.transaero21.mt.network.NetworkManager
import ru.transaero21.mt.network.connector.ConnectorExecutor
import ru.transaero21.mt.ui.screens.Screen
import ru.transaero21.mt.utils.NetworkUtils
import ru.transaero21.mt.utils.logError

class GuestConnectorExecutor(ipAddress: String, port: Int): ConnectorExecutor() {
    private val context = newSingleThreadAsyncContext(threadName = THREAD_NAME)
    override val socket: Socket

    private var _state = ConnectionState.Connecting
    override val state get() = _state

    override val job: Job

    init {
        socket = Gdx.net.newClientSocket(Net.Protocol.TCP, ipAddress, port, NetworkUtils.connectorHints)
        job = launchListener(context = context)
    }

    override fun parseCommand(command: Command) {
        when (_state) {
            ConnectionState.Connecting -> {
                _state = ConnectionState.Connected
                parseCommand(command = command)
            }
            ConnectionState.Connected -> {
                when (command) {
                    is Command.StartGame -> {
                        if (!NetworkManager.gameRunning) {
                            NetworkManager.gameRunning = true
                            MainGame.setScreen(
                                screen = Screen.Gameplay,
                                arguments = mapOf("startGameAt" to command.time, "worldSize" to command.size)
                            )
                        }
                    }
                    else -> {

                    }
                }
            }
            else -> { /* Do Nothing */ }
        }
    }

    override fun setDisconnected() {
        _state = ConnectionState.Disconnected
    }

    companion object {
        private const val THREAD_NAME = "GuestConnectorExecutor-Thread"
    }
}
