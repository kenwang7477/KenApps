package com.kenwang.kenapps.ui.chatgpt

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenwang.kenapps.data.model.ChatMessage
import com.kenwang.kenapps.data.model.ChatMessageFrom
import com.kenwang.kenapps.utils.TimeUtil
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
object ChatGPTScreen {

    @Composable
    fun ChatGPTUI(
        paddingValues: PaddingValues,
        viewModel: ChatGPTViewModel = hiltViewModel()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            val (keywordFieldId, sendButtonId, messageListId) = createRefs()

            var keyword by remember { mutableStateOf(TextFieldValue("")) }
            val focusManager = LocalFocusManager.current

            fun sendMessage() {
                viewModel.getResult(keyword.text)
                keyword = TextFieldValue("")
                focusManager.clearFocus()
            }

            OutlinedTextField(
                modifier = Modifier.constrainAs(keywordFieldId) {
                    start.linkTo(parent.start, margin = 10.dp)
                    end.linkTo(sendButtonId.start)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                },
                keyboardActions = KeyboardActions(
                    onSend = {
                        sendMessage()
                    }
                ),
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                value = keyword,
                onValueChange = { keyword = it }
            )
            IconButton(
                modifier = Modifier.constrainAs(sendButtonId) {
                    top.linkTo(keywordFieldId.top)
                    end.linkTo(parent.end)
                    bottom.linkTo(keywordFieldId.bottom)
                },
                onClick = {
                    sendMessage()
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }

            val scrollState = rememberLazyListState()
            ChatMessageList(
                modifier = Modifier
                    .constrainAs(messageListId) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(keywordFieldId.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
                    .padding(10.dp),
                scrollState,
                viewModel = viewModel
            )
        }
    }

    @Composable
    fun ChatMessageList(
        modifier: Modifier,
        scrollState: LazyListState,
        viewModel: ChatGPTViewModel,
    ) {
        when(val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
            is ChatGPTViewModel.ChatGPTViewState.ShowResult -> {
                LazyColumn(
                    modifier = modifier,
                    state = scrollState
                ) {
                    items(state.messageList) { chatMessage ->
                        MessageCard(chatMessage = chatMessage)
                    }
                }

                val coroutineScope = rememberCoroutineScope()
                LaunchedEffect(state.messageList) {
                    coroutineScope.launch {
                        scrollState.animateScrollToItem(state.messageList.size - 1)
                    }
                }
            }
            is ChatGPTViewModel.ChatGPTViewState.Loading -> Unit
            is ChatGPTViewModel.ChatGPTViewState.Empty -> Unit
        }
    }

    @Composable
    fun MessageCard(chatMessage: ChatMessage) { // 1
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalAlignment = when(chatMessage.chatMessageFrom) {
                ChatMessageFrom.Me -> Alignment.End
                else -> Alignment.Start
            },
        ) {
            Card(
                modifier = Modifier.widthIn(max = 340.dp),
                shape = cardShapeFor(chatMessage)
            ) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = chatMessage.message
                )
            }
            Text(
                text = TimeUtil.timestampToDate(chatMessage.time),
                fontSize = 12.sp,
            )
        }
    }

    @Composable
    fun cardShapeFor(chatMessage: ChatMessage): Shape {
        val roundedCorners = RoundedCornerShape(16.dp)
        return when(chatMessage.chatMessageFrom) {
            ChatMessageFrom.Me -> roundedCorners.copy(bottomEnd = CornerSize(0))
            else -> roundedCorners.copy(bottomStart = CornerSize(0))
        }
    }
}
