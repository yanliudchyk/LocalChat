package com.ubdjshdb.localchat.core.models

import kotlin.io.encoding.Base64

data class Message(
    val timestamp: Long,
    val username: String,
    val text: String,
    val isOutgoing: Boolean = false,
) {
    fun serialize(): String {
        val encodedUsername = Base64.encode(username.toByteArray())
        val encodedText = Base64.encode(text.toByteArray())
        return "$timestamp:$encodedUsername:$encodedText"
    }

    companion object {
        fun parse(line: String): Message {
            val parts = line.split(":", limit = 3)
            val timestamp = parts[0].toLong()
            val username = String(Base64.decode(parts[1]))
            val text = String(Base64.decode(parts[2]))
            return Message(timestamp, username, text)
        }
    }
}