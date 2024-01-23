package ru.transaero21.mt.network

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Net
import com.badlogic.gdx.net.ServerSocketHints
import com.badlogic.gdx.net.Socket
import com.badlogic.gdx.utils.GdxRuntimeException
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import ktx.async.KtxAsync
import ktx.async.newSingleThreadAsyncContext
import ru.transaero21.mt.models.core.WorldSize
import ru.transaero21.mt.network.connector.host.HostConnectorExecutor
import ru.transaero21.mt.utils.NetworkUtils
import java.net.SocketException
import java.sql.Timestamp
import java.time.Instant

class HostExecutor(port: Int, val size: WorldSize) {
    private var serverSocket = Gdx.net.newServerSocket(Net.Protocol.TCP, port, NetworkUtils.hostHints)
    private val connections = mutableListOf<Pair<Socket, HostConnectorExecutor>>()

    private var isRunning = true
    private val context = newSingleThreadAsyncContext(threadName = THREAD_NAME)
    private val job = KtxAsync.launch(context = context) {
        while (isRunning) {
            try {
                val socket = serverSocket.accept(NetworkUtils.connectorHints)
                setupConnection(socket)
            } catch (_: GdxRuntimeException) {
                /* Do nothing */
            } catch (e: Exception) {
                isRunning = false
            }
        }
    }

    private fun setupConnection(socket: Socket) {
        if (connections.size >= MAX_CONNECTIONS) socket.dispose()
        else {
            val connector = HostConnectorExecutor(socket = socket)
            connector.sendCommand(command = Command.PingPongConnection)
            connections.add(socket to connector)
            if (connections.size == 2) {
                val cmd = Command.StartGame(time = Timestamp.from(Instant.now()).time + 2000L, size = size)
                connections.forEach {
                    it.second.sendCommand(command = cmd)
                }
            }
        }
    }

    suspend fun kill() {
        serverSocket.dispose()
        for (connection in connections) {
            connection.second.kill()
        }
        isRunning = false
        job.cancelAndJoin()
    }

    companion object {
        private const val THREAD_NAME = "HostExecutor-Thread"
        private const val MAX_CONNECTIONS = 2
    }
}
