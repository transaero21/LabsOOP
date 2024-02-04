package ru.transaero21.mt.utils

import com.badlogic.gdx.net.ServerSocketHints
import com.badlogic.gdx.net.SocketHints

object NetworkUtils {
    const val DEFAULT_IP_ADDRESS = "localhost"
    const val DEFAULT_PORT = "2106"

    val connectorHints get() = SocketHints().apply {
        connectTimeout = 1000
        tcpNoDelay = true
        keepAlive = false
    }

    val hostHints get() = ServerSocketHints().apply {
        acceptTimeout = 1000
    }
}
