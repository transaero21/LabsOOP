package ru.transaero21.mt.network

import com.badlogic.gdx.net.Socket
import java.net.ConnectException
import java.text.ParseException

object CommandParser {
    private val MAGIC_SUFFIX = Command.MAGIC_SUFFIX.toCharArray()[0]

    fun readFromSocket(socket: Socket): Command {
        val sb = StringBuilder()
        socket.inputStream.let {
            while (true) {
                val i = it.read()
                if (i == -1) throw ConnectException()
                val c = i.toChar()
                sb.append(c)
                if (c == MAGIC_SUFFIX) break
            }
        }
        println(sb.toString())

        return try {
            sb.toString().toCommand()
        } catch (e: Exception) {
            throw UnsupportedOperationException()
        }
    }

    private fun String.toCommand(): Command {
        var command = this
        if (!command.startsWith(prefix = Command.MAGIC_PREFIX) || !command.endsWith(suffix = Command.MAGIC_SUFFIX))
            throw ParseException(command, 0)

        command = command.removePrefix(prefix = Command.MAGIC_PREFIX).removeSuffix(suffix = Command.MAGIC_SUFFIX)
        return Command.json.decodeFromString<Command>(command)
    }
}
