package com.kenwang.kenapps.ui.texttospeech

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.kenwang.kenapps.R
import java.math.RoundingMode
import java.text.DecimalFormat

object TextToSpeechScreen {

    @Composable
    fun TextToSpeechUI(
        paddingValues: PaddingValues,
        viewModel: TextToSpeechViewModel = hiltViewModel()
    ) {
        DisposableEffect(key1 = Unit) {
            onDispose {
                viewModel.stop()
                viewModel.shutdown()
                viewModel.reset()
            }
        }

        Column(modifier = Modifier.padding(paddingValues)) {
            val keyword = remember { mutableStateOf("") }

            KeywordLayout(keyword = keyword)

            Row(
                modifier = Modifier
                    .padding(top = 20.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { viewModel.play(keyword.value) }
                ) {
                    Text(text = stringResource(id = R.string.play_speech))
                }
                Button(
                    modifier = Modifier.padding(start = 20.dp),
                    onClick = { viewModel.stop() }
                ) {
                    Text(text = stringResource(id = R.string.stop_speech))
                }
            }

            SpeechRateSlider(viewModel)
        }
    }

    @Composable
    fun KeywordLayout(keyword: MutableState<String>) {
        val focusManager = LocalFocusManager.current
        OutlinedTextField(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth(),
            value = keyword.value,
            onValueChange = { keyword.value = it },
            keyboardActions = KeyboardActions(
                onDone = {
                    focusManager.clearFocus()
                }
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                if (keyword.value.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            keyword.value = ""
                        }
                    ) {
                        Icon(
                            Icons.Default.Clear,
                            contentDescription = "clear"
                        )
                    }
                }
            }
        )
    }

    @Composable
    fun SpeechRateSlider(viewModel: TextToSpeechViewModel) {
        var speechRate by remember { mutableStateOf(1.0f) }
        Text(
            modifier = Modifier.padding(top = 20.dp, start = 10.dp),
            text = "${stringResource(id = R.string.speech_rate)} $speechRate"
        )
        Slider(
            modifier = Modifier.padding(top = 5.dp, start = 10.dp, end = 10.dp),
            value = speechRate,
            onValueChange = {
                val decimalValue = DecimalFormat("#.#").apply {
                    roundingMode = RoundingMode.HALF_UP
                }.format(it).toFloat()
                speechRate = decimalValue
                viewModel.setSpeechRate(decimalValue)
            },
            valueRange = 0.1f..2.1f,
            steps = 19
        )
    }
}
