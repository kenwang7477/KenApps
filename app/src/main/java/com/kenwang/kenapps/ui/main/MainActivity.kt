package com.kenwang.kenapps.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import com.kenwang.kenapps.R
import com.kenwang.kenapps.domain.usecase.darkmode.GetDarkModeUseCase
import com.kenwang.kenapps.domain.usecase.main.MainListItem
import com.kenwang.kenapps.extensions.addGarbageTruckList
import com.kenwang.kenapps.extensions.addGarbageTruckMap
import com.kenwang.kenapps.extensions.addMapLocationList
import com.kenwang.kenapps.extensions.addMapLocationMap
import com.kenwang.kenapps.extensions.addParkingList
import com.kenwang.kenapps.extensions.addParkingMap
import com.kenwang.kenapps.extensions.addTextToSpeech
import com.kenwang.kenapps.extensions.addTvProgramList
import com.kenwang.kenapps.ui.Screens
import com.kenwang.kenapps.ui.garbagetrucktool.garbagetrucklist.GarbageTruckListScreen
import com.kenwang.kenapps.ui.garbagetrucktool.garbagetruckmap.GarbageTruckMapScreen
import com.kenwang.kenapps.ui.maplocation.maplocationlist.MapLocationListScreen
import com.kenwang.kenapps.ui.maplocation.maplocationmap.MapLocationMapScreen
import com.kenwang.kenapps.ui.parkingtool.parkinglist.ParkingListScreen
import com.kenwang.kenapps.ui.parkingtool.parkingmap.ParkingMapScreen
import com.kenwang.kenapps.ui.setting.SettingScreen
import com.kenwang.kenapps.ui.texttospeech.TextToSpeechScreen
import com.kenwang.kenapps.ui.theme.KenAppsTheme
import com.kenwang.kenapps.ui.tvprogramlist.TvProgramListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var getDarkModeUseCase: Provider<GetDarkModeUseCase>
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val darkMode = getDarkModeUseCase.get().invoke().collectAsStateWithLifecycle(
                initialValue = false
            ).value
            AppNavHost(darkMode = darkMode)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavHost(
    darkMode: Boolean
) {
    val backStack = rememberNavBackStack(Screens.MainRoute)
    val showBackButton = backStack.last() != Screens.MainRoute
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val topAppBarTitle = when(backStack.last()) {
        is Screens.ParkingListRoute,
        is Screens.ParkingMapRoute -> stringResource(R.string.kh_parking_space_map_title)

        is Screens.GarbageTruckListRoute,
        is Screens.GarbageTruckMapRoute -> stringResource(id = R.string.kh_garbage_truck_map_title)

        is Screens.TvProgramListRoute -> stringResource(id = R.string.tv_program_list_title)
        is Screens.SettingRoute -> stringResource(id = R.string.setting)
        is Screens.MapLocationListRoute,
        is Screens.MapLocationMapRoute -> stringResource(id = R.string.map_location_title)
        is Screens.TextToSpeechRoute -> stringResource(id = R.string.text_to_speech_title)
        else -> stringResource(id = R.string.app_name)
    }

    KenAppsTheme(darkTheme = darkMode) {
        MainDrawer(
            drawerState = drawerState,
            backStack = backStack,
            gesturesEnabled = !showBackButton
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = topAppBarTitle) },
                        navigationIcon = {
                            if (showBackButton) {
                                IconButton(
                                    onClick = { backStack.removeLastOrNull() }
                                ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                            } else {
                                IconButton(
                                    modifier = Modifier.testTag("mainMenu"),
                                    onClick = {
                                        scope.launch {
                                            if (drawerState.isOpen) {
                                                drawerState.close()
                                            } else {
                                                drawerState.open()
                                            }
                                        }
                                    }
                                ) { Icon(Icons.Default.Menu, "menu") }
                            }
                        }
                    )
                },
                content = { paddingValues ->
                    NavDisplay(
                        backStack = backStack,
                        onBack = { backStack.removeLastOrNull() },
                        entryProvider = entryProvider {
                            entry<Screens.MainRoute> {
                                MainScreen.MainUI(
                                    paddingValues = paddingValues,
                                    navToItem = { listItem ->
                                        when (listItem) {
                                            MainListItem.ParkingMap -> {
                                                backStack.addParkingList()
                                            }

                                            MainListItem.GarbageTruckMap -> {
                                                backStack.addGarbageTruckList()
                                            }

                                            MainListItem.TvProgramList -> {
                                                backStack.addTvProgramList()
                                            }
                                            MainListItem.MapLocation -> {
                                                backStack.addMapLocationList()
                                            }
                                            MainListItem.TextToSpeech -> {
                                                backStack.addTextToSpeech()
                                            }
                                        }
                                    }
                                )
                            }
                            entry<Screens.ParkingListRoute> {
                                ParkingListScreen.ParkingListUI(
                                    paddingValues = paddingValues,
                                    toParkingMap = { parkingSpace ->
                                        backStack.addParkingMap(parkingSpace = parkingSpace)
                                    }
                                )
                            }
                            entry<Screens.ParkingMapRoute> {
                                ParkingMapScreen.ParkingMapUI(
                                    paddingValues = paddingValues,
                                    parkingSpace = it.argParkingSpace
                                )
                            }
                            entry<Screens.GarbageTruckListRoute> {
                                GarbageTruckListScreen.GarbageTruckListUI(
                                    paddingValues = paddingValues,
                                    toGarbageTruckMap = { garbageTruck ->
                                        backStack.addGarbageTruckMap(garbageTruck = garbageTruck)
                                    }
                                )
                            }
                            entry<Screens.GarbageTruckMapRoute> {
                                GarbageTruckMapScreen.TruckMapUI(
                                    paddingValues = paddingValues,
                                    garbageTruck = it.argGarbageTruck
                                )
                            }
                            entry<Screens.TvProgramListRoute> {
                                TvProgramListScreen.TvProgramListUI(paddingValues = paddingValues)
                            }
                            entry<Screens.SettingRoute> {
                                SettingScreen.SettingUI(paddingValues = paddingValues)
                            }
                            entry<Screens.MapLocationListRoute> {
                                MapLocationListScreen.MapLocationListUI(
                                    paddingValues = paddingValues,
                                    toMapLocationMap = { longitude, latitude ->
                                        backStack.addMapLocationMap(
                                            longitude = longitude,
                                            latitude = latitude
                                        )
                                    }
                                )
                            }
                            entry<Screens.MapLocationMapRoute> {
                                MapLocationMapScreen.MapLocationMapUI(
                                    paddingValues = paddingValues,
                                    targetLongitude = it.argLongitude?.toDouble(),
                                    targetLatitude = it.argLatitude?.toDouble()
                                )
                            }
                            entry<Screens.TextToSpeechRoute> {
                                TextToSpeechScreen.TextToSpeechUI(paddingValues = paddingValues)
                            }
                        }
                    )
                }
            )
        }
    }
}
