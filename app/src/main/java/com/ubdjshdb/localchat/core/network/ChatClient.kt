package com.ubdjshdb.localchat.core.network

import com.ubdjshdb.localchat.core.models.Message
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.Closeable
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.InetSocketAddress
import java.net.Socket

class ChatClient(private val scope: CoroutineScope) : ChatMessageSink, ChatMessageSource, Closeable {
    private var socket: Socket? = null
    private var writer: PrintWriter? = null

    private var username: String = "guest"

    private val _incomingMessages = MutableSharedFlow<Message>()
    override val incomingMessages = _incomingMessages.asSharedFlow()

    suspend fun connect(ip: String, port: Int, username: String) = withContext(Dispatchers.IO) {
        close()
        this@ChatClient.username = username

        try {
            val newSocket = Socket()
            socket = newSocket
            newSocket.connect(InetSocketAddress(ip, port), 2500)
            writer = PrintWriter(newSocket.getOutputStream(), true)

            scope.launch(Dispatchers.IO) {
                readLoop(newSocket)
            }

            _incomingMessages.emit(
                Message(System.currentTimeMillis() / 1000, "System", "Подключено к $ip:$port")
            )
        } catch (e: Exception) {
            e.printStackTrace()
            _incomingMessages.emit(
                Message(System.currentTimeMillis() / 1000, "System", "Ошибка подключения: ${e.message}")
            )
            close()
        }
    }

    private suspend fun readLoop(activeSocket: Socket) {
        try {
            val reader = BufferedReader(InputStreamReader(activeSocket.getInputStream()))

            while (currentCoroutineContext().isActive && !activeSocket.isClosed) {
                val line = reader.readLine() ?: break

                try {
                    val msg = Message.parse(line)
                    _incomingMessages.emit(msg)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (_: Exception) { } finally {
            if (socket != null && !activeSocket.isClosed) {
                _incomingMessages.emit(
                    Message(System.currentTimeMillis() / 1000, "System", "Связь с сервером потеряна")
                )
            }
            close()
        }
    }

    override fun sendMessage(text: String) {
        scope.launch(Dispatchers.IO) {
            try {
                val msg = Message(System.currentTimeMillis() / 1000, username, text)
                writer?.println(msg.serialize())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun close() {
        try {
            writer?.close()
            socket?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            writer = null
            socket = null
        }
    }
}