package com.kenwang.kenapps.ui.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.kenwang.kenapps.R
import com.kenwang.kenapps.extensions.toSetting
import kotlinx.coroutines.launch

@Composable
fun MainDrawer(
    drawerState: DrawerState,
    navController: NavController,
    gesturesEnabled: Boolean,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = gesturesEnabled,
        drawerContent = {
            ModalDrawerSheet(modifier = Modifier.width(220.dp)) {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 48.dp, start = 10.dp)) {
                    SettingMenu {
                        scope.launch {
                            drawerState.close()
                            navController.toSetting()
                        }
                    }
                }
            }
        },
        content = content
    )
}

@Composable
private fun SettingMenu(onClick: () -> Unit) {
    NavigationDrawerItem(
        label = {
            Text(
                text = stringResource(id = R.string.setting)
            )
        },
        icon = {
            Icon(imageVector = Icons.Default.Settings, contentDescription = "Setting")
        },
        selected = false,
        onClick = onClick
    )
}
