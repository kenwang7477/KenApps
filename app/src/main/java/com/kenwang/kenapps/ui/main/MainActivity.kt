package com.kenwang.kenapps.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.CctvMonitor
import com.kenwang.kenapps.data.model.ParkingSpace
import com.kenwang.kenapps.data.model.GarbageTruck
import com.kenwang.kenapps.domain.usecase.darkmode.GetDarkModeUseCase
import com.kenwang.kenapps.domain.usecase.main.MainListItem
import com.kenwang.kenapps.extensions.isVersionAboveTiramisu
import com.kenwang.kenapps.extensions.toArmRecyclerList
import com.kenwang.kenapps.extensions.toCctvList
import com.kenwang.kenapps.extensions.toCctvMap
import com.kenwang.kenapps.extensions.toGarbageTruckList
import com.kenwang.kenapps.extensions.toGarbageTruckMap
import com.kenwang.kenapps.extensions.toParkingList
import com.kenwang.kenapps.extensions.toParkingMap
import com.kenwang.kenapps.extensions.toTvProgramList
import com.kenwang.kenapps.ui.Screens
import com.kenwang.kenapps.ui.armrecyclertool.armrecyclerlist.ArmRecyclerListScreen
import com.kenwang.kenapps.ui.cctvtool.cctvlist.CctvListScreen
import com.kenwang.kenapps.ui.cctvtool.cctvmap.CctvMapScreen
import com.kenwang.kenapps.ui.garbagetrucktool.garbagetrucklist.GarbageTruckListScreen
import com.kenwang.kenapps.ui.garbagetrucktool.garbagetruckmap.GarbageTruckMapScreen
import com.kenwang.kenapps.ui.parkingtool.parkinglist.ParkingListScreen
import com.kenwang.kenapps.ui.parkingtool.parkingmap.ParkingMapScreen
import com.kenwang.kenapps.ui.setting.SettingScreen
import com.kenwang.kenapps.ui.theme.KenAppsTheme
import com.kenwang.kenapps.ui.tvprogramlist.TvProgramListScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
@OptIn(ExperimentalLifecycleComposeApi::class)
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun AppNavHost(
    darkMode: Boolean
) {
    
    val navController: NavHostController = rememberNavController()
    val currentRoute = navController
        .currentBackStackEntryFlow
        .collectAsStateWithLifecycle(initialValue = navController.currentBackStackEntry)

    val showBackButton = when (currentRoute.value?.destination?.route) {
        Screens.Main.route -> false
        else -> true
    }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val topAppBarTitle = when (currentRoute.value?.destination?.route?.split("/")?.getOrNull(0)) {
        Screens.ParkingList.route,
        Screens.ParkingMap.route -> stringResource(id = R.string.kh_parking_space_map_title)
        Screens.GarbageTruckList.route,
        Screens.GarbageTruckMap.route-> stringResource(id = R.string.kh_garbage_truck_map_title)
        Screens.TvProgramList.route -> stringResource(id = R.string.tv_program_list_title)
        Screens.ArmRecyclerList.route -> stringResource(id = R.string.kh_arm_recycler_map_title)
        Screens.CctvList.route,
        Screens.CctvMap.route -> stringResource(id = R.string.kh_cctv_system_title)
        Screens.Setting.route -> stringResource(id = R.string.setting)
        else -> stringResource(id = R.string.app_name)
    }
    KenAppsTheme(
        darkTheme = darkMode
    ) {
        MainDrawer(
            drawerState = drawerState,
            navController = navController
        ) {
            Scaffold(
                topBar = {
                    CenterAlignedTopAppBar(
                        title = { Text(text = topAppBarTitle) },
                        navigationIcon = {
                            if (showBackButton) {
                                IconButton(
                                    onClick = { navController.popBackStack() }
                                ) { Icon(Icons.Default.ArrowBack, "Back") }
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
                    NavHost(navController = navController, startDestination = Screens.Main.route) {
                        addMainGraph(paddingValues, navController)
                        addParkingListGraph(paddingValues, navController)
                        addParkingMapGraph(paddingValues)
                        addGarbageTruckListGraph(paddingValues, navController)
                        addGarbageTruckMapGraph(paddingValues)
                        addTvProgramListGraph(paddingValues)
                        addArmRecyclerListGraph(paddingValues)
                        addCctvMonitorListGraph(paddingValues, navController)
                        addCctvMonitorMapGraph(paddingValues)
                        addSettingGraph(paddingValues)
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
    composable(Screens.Main.route) {
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
                    MainListItem.CctvList -> {
                        navController.toCctvList()
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
    composable(Screens.ParkingList.route) {
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
    composable(
        route = "${Screens.ParkingMap.route}/{${Screens.ParkingMap.argParkingSpace}}",
        arguments = listOf(
            navArgument(Screens.ParkingMap.argParkingSpace){ type = ParkingSpace.NavigationType }
        )
    ) {
        val parkingSpace = if (isVersionAboveTiramisu()) {
            it.arguments?.getParcelable(Screens.ParkingMap.argParkingSpace, ParkingSpace::class.java) ?: ParkingSpace()
        } else {
            it.arguments?.getParcelable(Screens.ParkingMap.argParkingSpace) ?: ParkingSpace()
        }
        ParkingMapScreen.ParkingMapUI(
            paddingValues = paddingValues,
            parkingSpace = parkingSpace
        )
    }
}

private fun NavGraphBuilder.addGarbageTruckListGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {
    composable(Screens.GarbageTruckList.route) {
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
    composable(
        route = "${Screens.GarbageTruckMap.route}/{${Screens.GarbageTruckMap.argGarbageTruck}}",
        arguments = listOf(
            navArgument(Screens.GarbageTruckMap.argGarbageTruck) { type = GarbageTruck.NavigationType }
        )
    ) {
        val truck = if (isVersionAboveTiramisu()) {
            it.arguments?.getParcelable(Screens.GarbageTruckMap.argGarbageTruck, GarbageTruck::class.java) ?: GarbageTruck()
        } else {
            it.arguments?.getParcelable(Screens.GarbageTruckMap.argGarbageTruck) ?: GarbageTruck()
        }
        GarbageTruckMapScreen.TruckMapUI(
            paddingValues,
            garbageTruck = truck
        )
    }
}

private fun NavGraphBuilder.addTvProgramListGraph(
    paddingValues: PaddingValues
) {
    composable(Screens.TvProgramList.route) {
        TvProgramListScreen.TvProgramListUI(paddingValues)
    }
}

private fun NavGraphBuilder.addArmRecyclerListGraph(
    paddingValues: PaddingValues
) {
    composable(Screens.ArmRecyclerList.route) {
        ArmRecyclerListScreen.ArmRecyclerListUI(
            paddingValues = paddingValues
        )
    }
}

private fun NavGraphBuilder.addCctvMonitorListGraph(
    paddingValues: PaddingValues,
    navController: NavController
) {
    composable(Screens.CctvList.route) {
        CctvListScreen.CctvListUI(
            paddingValues = paddingValues,
            toCctvMap = {
                navController.toCctvMap(it)
            }
        )
    }
}

private fun NavGraphBuilder.addCctvMonitorMapGraph(
    paddingValues: PaddingValues
) {
    composable(
        route = "${Screens.CctvMap.route}/{${Screens.CctvMap.argCctvMonitor}}",
        arguments = listOf(
            navArgument(Screens.CctvMap.argCctvMonitor) { type = CctvMonitor.NavigationType }
        )
    ) {
        val cctvMonitor = if (isVersionAboveTiramisu()) {
            it.arguments?.getParcelable(Screens.CctvMap.argCctvMonitor, CctvMonitor::class.java) ?: CctvMonitor()
        } else {
            it.arguments?.getParcelable(Screens.CctvMap.argCctvMonitor) ?: CctvMonitor()
        }
        CctvMapScreen.CctvMapUI(
            paddingValues = paddingValues,
            cctvMonitor = cctvMonitor
        )
    }
}

private fun NavGraphBuilder.addSettingGraph(
    paddingValues: PaddingValues
) {
    composable(Screens.Setting.route) {
        SettingScreen.SettingUI(
            paddingValues = paddingValues
        )
    }
}
