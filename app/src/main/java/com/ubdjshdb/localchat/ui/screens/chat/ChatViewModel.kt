package com.ubdjshdb.localchat.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubdjshdb.localchat.core.models.Message
import com.ubdjshdb.localchat.core.network.ChatClient
import com.ubdjshdb.localchat.core.network.ChatMessageSink
import com.ubdjshdb.localchat.core.network.ChatMessageSource
import com.ubdjshdb.localchat.core.network.ChatServer
import com.ubdjshdb.localchat.core.prefs.AppConfig
import com.ubdjshdb.localchat.core.prefs.AppMode
import com.ubdjshdb.localchat.core.prefs.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.Closeable
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private var currentSession: Closeable? = null
    private var messageSink: ChatMessageSink? = null

    init {
        viewModelScope.launch {
            settingsRepository.config.collectLatest { config ->
                startSession(config)
            }
        }
    }

    private suspend fun startSession(config: AppConfig) {
        closeCurrentSession()
        _messages.value = emptyList()

        try {
            val session: Any

            if (config.appMode == AppMode.Server) {
                val server = ChatServer(viewModelScope)
                server.startServer(config.username, config.serverPort)
                session = server

                addSystemMessage("Сервер запущен на порту ${config.serverPort}")
            } else {
                val client = ChatClient(viewModelScope)
                client.connect(config.serverIp, config.serverPort, config.username)
                session = client
            }

            currentSession = session as Closeable
            messageSink = session as ChatMessageSink
            val messageSource = session as ChatMessageSource
            messageSource.incomingMessages.collect { message ->
                _messages.update { it + message }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            addSystemMessage("Ошибка инициализации: ${e.message}")
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank()) return

        val currentConfig = settingsRepository.config.value
        val timestamp = System.currentTimeMillis() / 1000

        val myMessage = Message(
            timestamp = timestamp,
            username = currentConfig.username,
            text = text,
            isOutgoing = true
        )
        _messages.update { it + myMessage }

        messageSink?.sendMessage(text)
    }

    private fun addSystemMessage(text: String) {
        val sysMsg = Message(
            timestamp = System.currentTimeMillis() / 1000,
            username = "System",
            text = text
        )
        _messages.update { it + sysMsg }
    }

    private fun closeCurrentSession() {
        try {
            currentSession?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            currentSession = null
            messageSink = null
        }
    }

    override fun onCleared() {
        super.onCleared()
        closeCurrentSession()
    }
}