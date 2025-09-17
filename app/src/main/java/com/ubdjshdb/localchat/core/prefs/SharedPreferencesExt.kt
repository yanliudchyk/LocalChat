package com.ubdjshdb.localchat.core.prefs

import android.content.SharedPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun SharedPreferences.getStringAsFlow(key: String, defaultValue: String? = null) =
    callbackFlow {
        val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, prefKey ->
            if (key == prefKey) {
                trySend(getString(prefKey, defaultValue))
            }
        }
        if (contains(key)) {
            send(getString(key, defaultValue))
        }
        registerOnSharedPreferenceChangeListener(listener)
        awaitClose { unregisterOnSharedPreferenceChangeListener(listener) }
    }