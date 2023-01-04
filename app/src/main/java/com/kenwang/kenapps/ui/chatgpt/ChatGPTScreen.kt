package com.kenwang.kenapps.ui.chatgpt

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenwang.kenapps.ui.commonscreen.LoadingView

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
            val (keywordFieldId, sendButtonId, resultTextId, loadingId) = createRefs()

            var keyword by remember { mutableStateOf("") }
            val focusManager = LocalFocusManager.current

            fun getResult() {
                viewModel.getResult(keyword)
                focusManager.clearFocus()
            }

            OutlinedTextField(
                modifier = Modifier.constrainAs(keywordFieldId) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(sendButtonId.start)
                    width = Dimension.fillToConstraints
                },
                keyboardActions = KeyboardActions(
                    onSend = {
                        getResult()
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
                    getResult()
                }
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }

            when(val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                is ChatGPTViewModel.ChatGPTViewState.ShowResult -> {
                    Text(
                        modifier = Modifier
                            .constrainAs(resultTextId) {
                                top.linkTo(keywordFieldId.bottom)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                bottom.linkTo(parent.bottom)
                                width = Dimension.fillToConstraints
                                height = Dimension.fillToConstraints
                            }
                            .padding(10.dp),
                        text = state.result
                    )
                }
                is ChatGPTViewModel.ChatGPTViewState.Loading -> {
                    LoadingView(
                        modifier = Modifier.constrainAs(loadingId) {
                            top.linkTo(keywordFieldId.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                        }
                    )
                }
                else -> Unit
            }
        }
    }
}
