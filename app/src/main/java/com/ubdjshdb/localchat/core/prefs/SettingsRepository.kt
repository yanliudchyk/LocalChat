package com.ubdjshdb.localchat.core.prefs

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    private val _config = MutableStateFlow(AppConfig(
        appMode = AppMode.valueOf(prefs.getString(APP_MODE, AppMode.Client.name)!!),
        username = prefs.getString(USERNAME, "user")!!,
        serverIp = prefs.getString(SERVER_IP, "127.0.0.1")!!,
        serverPort = prefs.getInt(SERVER_PORT, 12345),
    ))

    val config = _config.asStateFlow()

    fun updateConfig(config: AppConfig) {
        prefs.edit {
            putString(APP_MODE, config.appMode.name)
            putString(USERNAME, config.username)
            putString(SERVER_IP, config.serverIp)
            putInt(SERVER_PORT, config.serverPort)
        }

        _config.value = config
    }

    companion object {
        const val APP_MODE = "app_mode"
        const val USERNAME = "username"
        const val SERVER_IP = "server_ip"
        const val SERVER_PORT = "server_port"
    }
}