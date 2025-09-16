package com.kenwang.kenapps.ui.main

import com.kenwang.kenapps.R
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class MainScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)
    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<MainActivity>()

//    lateinit var navController: TestNavHostController

    @Before
    fun setup() {
        hiltRule.inject()
//        composeTestRule.setContent {
//            navController = TestNavHostController(LocalContext.current)
//            navController.navigatorProvider.addNavigator(ComposeNavigator())
//            AppNavHost(navController = navController)
//        }
    }

    @Test
    fun testMainListDisplayed() {
        val garbageTruckString = composeTestRule.activity.getString(R.string.garbage_truck_map_title)
        val parkingMapString = composeTestRule.activity.getString(R.string.parking_space_map_title)
        val tvProgramString = composeTestRule.activity.getString(R.string.tv_program_list_title)

        composeTestRule.onNodeWithText(garbageTruckString).assertIsDisplayed()
        composeTestRule.onNodeWithText(parkingMapString).assertIsDisplayed()
        composeTestRule.onNodeWithText(tvProgramString).assertIsDisplayed()
    }

    @Test
    fun testMenuItemDisplayed() {
        val mainMenu = composeTestRule.onNodeWithTag("mainMenu")
        mainMenu.assertIsDisplayed()

        val settingString = composeTestRule.activity.getString(R.string.setting)
        mainMenu.performClick()
        composeTestRule.onNodeWithText(settingString).assertIsDisplayed()
    }
}
