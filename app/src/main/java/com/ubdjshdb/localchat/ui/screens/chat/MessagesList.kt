package com.ubdjshdb.localchat.ui.screens.chat

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubdjshdb.localchat.core.models.Message

@Composable
fun MessagesList(
    modifier: Modifier = Modifier,
    messages: List<Message>,
    state: LazyListState = rememberLazyListState()
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 8.dp),
        state = state
    ) {
        item { Spacer(Modifier.height(8.dp)) }
        items(messages) {
            MessageView(it)
            Spacer(Modifier.height(8.dp))
        }
    }
}

@Composable
fun MessageView(message: Message) {
    Box(Modifier.fillMaxWidth()) {
        Surface(
            shape = RoundedCornerShape(32.dp),
            color = if (message.isOutgoing) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainer,
            contentColor = if (message.isOutgoing) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.align(if (message.isOutgoing) Alignment.CenterEnd else Alignment.CenterStart)
        ) {
            Text(text = message.text, modifier = Modifier.padding(12.dp))
        }
    }
}