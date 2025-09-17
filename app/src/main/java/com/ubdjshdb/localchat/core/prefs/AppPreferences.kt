package com.ubdjshdb.localchat.core.prefs

import android.content.Context
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppPreferences @Inject constructor(@ApplicationContext private val context: Context) {
    private val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    val username = prefs.getStringAsFlow("username", "")
    fun setUsername(username: String) = prefs.edit { putString("username", username) }

    val appMode = prefs.getStringAsFlow("app_mode", AppMode.Client.name)
        .map { AppMode.valueOf(it!!) }
    fun setAppMode(appMode: AppMode) = prefs.edit { putString("app_mode", appMode.name) }

    val serverIp = prefs.getStringAsFlow("server_ip", "")
    fun setServerIp(ip: String) = prefs.edit { putString("server_ip", ip) }
}
