package com.ubdjshdb.localchat.core.network

interface ChatMessageSink {
    fun sendMessage(text: String)
}