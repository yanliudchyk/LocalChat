package com.ubdjshdb.localchat.core.network

import com.ubdjshdb.localchat.core.models.Message
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.util.concurrent.ConcurrentHashMap

class ChatServer(private val scope: CoroutineScope) : ChatMessageSink, ChatMessageSource,
    Closeable {
    private var serverSocket: ServerSocket? = null
    private var serverJob: Job? = null

    private data class ClientSession(val socket: Socket, val writer: PrintWriter)

    private val clients = ConcurrentHashMap<Int, ClientSession>()

    private val _incomingMessages = MutableSharedFlow<Message>()
    override val incomingMessages = _incomingMessages.asSharedFlow()

    private var username: String = "host"

    fun startServer(username: String, port: Int) {
        if (serverJob?.isActive == true) return
        this.username = username

        serverJob = scope.launch(Dispatchers.IO) {
            try {
                serverSocket = ServerSocket(port)
                while (isActive) {
                    val clientSocket = serverSocket?.accept() ?: break

                    launch(Dispatchers.IO) {
                        handleClient(clientSocket)
                    }
                }
            } catch (e: Exception) {
                if (isActive) e.printStackTrace()
            }
        }
    }

    override fun sendMessage(text: String) {
        scope.launch(Dispatchers.IO) {
            val msg = Message(
                timestamp = System.currentTimeMillis(),
                username = username,
                text = text
            )
            broadcastToClients(msg, senderId = 0)
        }
    }

    private suspend fun handleClient(socket: Socket) {
        val clientId = socket.hashCode()

        try {
            val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
            val writer = PrintWriter(OutputStreamWriter(socket.getOutputStream()), true)

            clients[clientId] = ClientSession(socket, writer)

            while (currentCoroutineContext().isActive) {
                val line = reader.readLine() ?: break

                try {
                    val message = Message.parse(line)
                    _incomingMessages.emit(message)
                    broadcastToClients(message, senderId = clientId)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
        } finally {
            clients.remove(clientId)
            try {
                socket.close()
            } catch (e: Exception) { /* ignore */
            }
        }
    }

    private fun broadcastToClients(message: Message, senderId: Int) {
        val serializedMsg = message.serialize()

        clients.forEach { (id, session) ->
            if (id != senderId) {
                try {
                    session.writer.println(serializedMsg)
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun close() {
        try {
            serverSocket?.close()
        } catch (e: Exception) {
        }

        clients.values.forEach { session ->
            try {
                session.socket.close()
            } catch (e: Exception) {
            }
        }
        clients.clear()

        serverJob?.cancel()
    }
}