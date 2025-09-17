package com.ubdjshdb.localchat.core.models

data class Message(
    val text: String,
    val isOutgoing: Boolean,
)