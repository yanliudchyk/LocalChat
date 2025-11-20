package com.ubdjshdb.localchat.core.prefs

data class AppConfig(
    val appMode: AppMode,
    val username: String,
    val serverIp: String,
    val serverPort: Int,
)