package com.kenwang.kenapps.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.domain.usecase.darkmode.GetDarkModeUseCase
import com.kenwang.kenapps.domain.usecase.main.MainListItem
import com.kenwang.kenapps.extensions.toArmRecyclerList
import com.kenwang.kenapps.extensions.toGarbageTruckList
import com.kenwang.kenapps.extensions.toGarbageTruckMap
import com.kenwang.kenapps.extensions.toMapLocationList
import com.kenwang.kenapps.extensions.toMapLocationMap
import com.kenwang.kenapps.extensions.toParkingList
import com.kenwang.kenapps.extensions.toParkingMap
import com.kenwang.kenapps.extensions.toTextToSpeech
import com.kenwang.kenapps.extensions.toTvProgramList
import com.kenwang.kenapps.ui.ArmRecyclerListRoute
import com.kenwang.kenapps.ui.GarbageTruckListRoute
import com.kenwang.kenapps.ui.GarbageTruckMapRoute
import com.kenwang.kenapps.ui.MainRoute
import com.kenwang.kenapps.ui.MapLocationListRoute
import com.kenwang.kenapps.ui.MapLocationMapRoute
import com.kenwang.kenapps.ui.ParkingListRoute
import com.kenwang.kenapps.ui.ParkingMapRoute
import com.kenwang.kenapps.ui.SettingRoute
import com.kenwang.kenapps.ui.TextToSpeechRoute
import com.kenwang.kenapps.ui.TvProgramListRoute
import com.kenwang.kenapps.ui.armrecyclertool.armrecyclerlist.ArmRecyclerListScreen
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
import kotlin.reflect.typeOf

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
    
    val navController: NavHostController = rememberNavController()
    val currentRoute = navController
        .currentBackStackEntryFlow
        .collectAsStateWithLifecycle(initialValue = navController.currentBackStackEntry)

    val destination = currentRoute.value?.destination
    val showBackButton = destination?.hasRoute<MainRoute>() != true
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val topAppBarTitle = when {
        destination?.hasRoute<ParkingListRoute>() == true ||
            destination?.hasRoute<ParkingMapRoute>() == true -> stringResource(R.string.kh_parking_space_map_title)

        destination?.hasRoute<GarbageTruckListRoute>() == true ||
            destination?.hasRoute<GarbageTruckMapRoute>() == true -> stringResource(id = R.string.kh_garbage_truck_map_title)

        destination?.hasRoute<TvProgramListRoute>() == true -> stringResource(id = R.string.tv_program_list_title)
        destination?.hasRoute<ArmRecyclerListRoute>() == true -> stringResource(id = R.string.kh_arm_recycler_map_title)
        destination?.hasRoute<SettingRoute>() == true -> stringResource(id = R.string.setting)
        destination?.hasRoute<MapLocationListRoute>() == true ||
            destination?.hasRoute<MapLocationMapRoute>() == true -> stringResource(id = R.string.map_location_title)
        destination?.hasRoute<TextToSpeechRoute>() == true -> stringResource(id = R.string.text_to_speech_title)
        else -> stringResource(id = R.string.app_name)
    }
    KenAppsTheme(
        darkTheme = darkMode
    ) {
        MainDrawer(
            drawerState = drawerState,
            navController = navController,
            gesturesEnabled = !showBackButton
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = topAppBarTitle) },
                        navigationIcon = {
                            if (showBackButton) {
                                IconButton(
                                    onClick = { navController.navigateUp() }
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
                    NavHost(navController = navController, startDestination = MainRoute) {
                        addMainGraph(paddingValues = paddingValues, navController = navController)
                        addParkingListGraph(
                            paddingValues = paddingValues,
                            navController = navController
                        )
                        addParkingMapGraph(paddingValues = paddingValues)
                        addGarbageTruckListGraph(
                            paddingValues = paddingValues,
                            navController = navController
                        )
                        addGarbageTruckMapGraph(paddingValues = paddingValues)
                        addTvProgramListGraph(paddingValues = paddingValues)
                        addArmRecyclerListGraph(paddingValues = paddingValues)
                        addSettingGraph(paddingValues = paddingValues)
                        addMapLocationGraph(
                            paddingValues = paddingValues,
                            navController = navController
                        )
                        addMapLocationMapGraph(paddingValues = paddingValues)
                        addTextToSpeechGraph(paddingValues = paddingValues)
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.addMainGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {
    composable<MainRoute> {
        MainScreen.MainUI(
            paddingValues = paddingValues,
            navToItem = { listItem ->
                when (listItem) {
                    MainListItem.ParkingMap -> {
                        navController.toParkingList()
                    }

                    MainListItem.GarbageTruckMap -> {
                        navController.toGarbageTruckList()
                    }

                    MainListItem.TvProgramList -> {
                        navController.toTvProgramList()
                    }
                    MainListItem.ArmRecyclerMap -> {
                        navController.toArmRecyclerList()
                    }
                    MainListItem.MapLocation -> {
                        navController.toMapLocationList()
                    }
                    MainListItem.TextToSpeech -> {
                        navController.toTextToSpeech()
                    }
                }
            }
        )
    }
}

private fun NavGraphBuilder.addParkingListGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {
    composable<ParkingListRoute> {
        ParkingListScreen.ParkingListUI(
            paddingValues = paddingValues,
            toParkingMap = {
                navController.toParkingMap(it)
            }
        )
    }
}

private fun NavGraphBuilder.addParkingMapGraph(
    paddingValues: PaddingValues
) {
    composable<ParkingMapRoute>(typeMap = mapOf(typeOf<ParkingSpace>() to ParkingSpace.NavigationType)) {
        val arguments = it.toRoute<ParkingMapRoute>()
        ParkingMapScreen.ParkingMapUI(
            paddingValues = paddingValues,
            parkingSpace = arguments.argParkingSpace
        )
    }
}

private fun NavGraphBuilder.addGarbageTruckListGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {
    composable<GarbageTruckListRoute> {
        GarbageTruckListScreen.GarbageTruckListUI(
            paddingValues = paddingValues,
            toGarbageTruckMap = {
                navController.toGarbageTruckMap(it)
            }
        )
    }
}

private fun NavGraphBuilder.addGarbageTruckMapGraph(
    paddingValues: PaddingValues
) {
    composable<GarbageTruckMapRoute>(typeMap = mapOf(typeOf<GarbageTruck>() to GarbageTruck.NavigationType)) {
        val arguments = it.toRoute<GarbageTruckMapRoute>()
        GarbageTruckMapScreen.TruckMapUI(
            paddingValues,
            garbageTruck = arguments.argGarbageTruck
        )
    }
}

private fun NavGraphBuilder.addTvProgramListGraph(
    paddingValues: PaddingValues
) {
    composable<TvProgramListRoute> {
        TvProgramListScreen.TvProgramListUI(paddingValues = paddingValues)
    }
}

private fun NavGraphBuilder.addArmRecyclerListGraph(
    paddingValues: PaddingValues
) {
    composable<ArmRecyclerListRoute> {
        ArmRecyclerListScreen.ArmRecyclerListUI(paddingValues = paddingValues)
    }
}

private fun NavGraphBuilder.addSettingGraph(
    paddingValues: PaddingValues
) {
    composable<SettingRoute> {
        SettingScreen.SettingUI(paddingValues = paddingValues)
    }
}

private fun NavGraphBuilder.addMapLocationGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {
    composable<MapLocationListRoute> {
        MapLocationListScreen.MapLocationListUI(
            paddingValues = paddingValues,
            toMapLocationMap = { longitude, latitude ->
                navController.toMapLocationMap(longitude, latitude)
            }
        )
    }
}

private fun NavGraphBuilder.addMapLocationMapGraph(
    paddingValues: PaddingValues
) {
    composable<MapLocationMapRoute> {
        val arguments = it.toRoute<MapLocationMapRoute>()
        MapLocationMapScreen.MapLocationMapUI(
            paddingValues = paddingValues,
            targetLongitude = arguments.argLongitude?.toDouble(),
            targetLatitude = arguments.argLatitude?.toDouble()
        )
    }
}

private fun NavGraphBuilder.addTextToSpeechGraph(
    paddingValues: PaddingValues
) {
    composable<TextToSpeechRoute> {
        TextToSpeechScreen.TextToSpeechUI(paddingValues = paddingValues)
    }
}
