package com.ubdjshdb.localchat.ui.screens.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.launch

@Composable
internal fun ChatBottomBar(
    onSendClicked: (String) -> Unit,
    scrollToBottom: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }
    val sendButtonEnabled by remember { derivedStateOf { text.isNotBlank() } }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainer,
        contentColor = MaterialTheme.colorScheme.onSurface,
    ) {
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            maxLines = 5,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier
                .navigationBarsPadding(),
            trailingIcon = {
                IconButton(
                    onClick = {
                        onSendClicked(text)
                        text = ""
                        scrollToBottom()
                    },
                    enabled = sendButtonEnabled
                ) {
                    Icon(Icons.AutoMirrored.Default.Send, contentDescription = "Send")
                }
            },
            placeholder = {
                Text("Type a message...")
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    vm: ChatViewModel,
    onSettingsButtonClicked: () -> Unit
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val messages by vm.messages.collectAsState()

    Scaffold(
        modifier = Modifier.imePadding(),
        topBar = {
            TopAppBar(
                title = { Text("Chat") },
                actions = {
                    IconButton(onClick = onSettingsButtonClicked) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            ChatBottomBar(
                onSendClicked = vm::sendMessage,
                scrollToBottom = {
                    scope.launch { listState.animateScrollToItem(messages.size - 1) }
                }
            )
        },
    ) {
        MessagesList(
            modifier = Modifier.padding(it),
            messages = messages,
            state = listState
        )
    }
}