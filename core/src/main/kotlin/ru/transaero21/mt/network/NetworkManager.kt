package ru.transaero21.mt.network

import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.network.connector.guest.GuestConnectorExecutor
import ru.transaero21.mt.utils.logError
import java.lang.Exception

object NetworkManager {
    private var hostExecutor: HostExecutor? = null
    private var connectorExecutor: GuestConnectorExecutor? = null

    val isConnecting get() = connectorExecutor?.let { it.state == ConnectionState.Connecting } ?: false
    val isConnected get() = connectorExecutor?.let { it.state == ConnectionState.Connected } ?: false

    var gameRunning = false

    var isHost = false

    fun initializeAsHost(port: String, size: WorldSize) {
        isHost = true
        try {
            hostExecutor = HostExecutor(port = port.toInt(), size = size)
            connectorExecutor = GuestConnectorExecutor(ipAddress = "localhost", port = port.toInt())
        } catch (e: Exception) {
            killAll()
            throw e
        }
    }

    fun initializeAsGuest(ipAddress: String, port: String) {
        try {
            connectorExecutor = GuestConnectorExecutor(ipAddress = ipAddress, port = port.toInt())
        } catch (e: Exception) {
            killAll()
            throw e
        }
    }

    var killInProgress = false

    fun killAll() {
        gameRunning = false
        killInProgress = true
        KtxAsync.launch {
            connectorExecutor?.kill()?.also { connectorExecutor = null }
            hostExecutor?.kill()?.also {
                hostExecutor = null
                isHost = false
            }
            killInProgress = false
        }

    }

    fun sendCommandEveryone(command: Command) {

    }
}
