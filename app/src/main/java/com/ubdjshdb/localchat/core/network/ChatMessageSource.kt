package com.ubdjshdb.localchat.core.network

import com.ubdjshdb.localchat.core.models.Message
import kotlinx.coroutines.flow.SharedFlow

interface ChatMessageSource {
    val incomingMessages: SharedFlow<Message>
}