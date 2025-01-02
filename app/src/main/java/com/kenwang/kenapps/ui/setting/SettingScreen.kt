package com.kenwang.kenapps.ui.setting

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenwang.kenapps.R

object SettingScreen {

    @Composable
    fun SettingUI(
        paddingValues: PaddingValues,
        viewModel: SettingViewModel = hiltViewModel()
    ) {
        Column(modifier = Modifier.padding(paddingValues)) {
            DarkModeItem(viewModel)
        }
    }

    @Composable
    private fun DarkModeItem(viewModel: SettingViewModel) {
        val darkModeSwitch = viewModel.darkModeState.collectAsStateWithLifecycle().value

        ItemCardLayout {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = stringResource(id = R.string.dark_mode))
                Switch(
                    checked = darkModeSwitch,
                    onCheckedChange = {
                        viewModel.setDarkMode(it)
                    }
                )
            }
        }

        LaunchedEffect(Unit) {
            viewModel.loadDarkMode()
        }
    }

    @Composable
    private fun ItemCardLayout(content: @Composable () -> Unit) {
        Card(
            modifier = Modifier.padding(5.dp),
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground)
        ) {
            content()
        }
    }
}
