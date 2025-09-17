package com.ubdjshdb.localchat.ui.screens.chat

import androidx.lifecycle.ViewModel
import com.ubdjshdb.localchat.core.models.Message
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor() : ViewModel() {
    private val _messages = MutableStateFlow((1..10).map { Message("Message $it", isOutgoing = it % 2 == 0) })
    val messages = _messages.asStateFlow()

    fun sendMessage(msg: String) {
        _messages.value = _messages.value + Message(msg, isOutgoing = true)
    }
}