package com.kenwang.kenapps.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenwang.kenapps.R
import com.kenwang.kenapps.domain.usecase.main.MainListItem

object MainScreen {

    @Composable
    fun MainUI(
        paddingValues: PaddingValues,
        navToItem: (item: MainListItem) -> Unit,
        mainViewModel: MainViewModel = hiltViewModel()
    ) {
        when (val state = mainViewModel.viewState.collectAsStateWithLifecycle().value) {
            is MainViewModel.MainViewState.Success -> {
                GridListLayout(paddingValues, navToItem, state.items)
            }
            is MainViewModel.MainViewState.Empty -> Unit
        }
    }

    @Composable
    private fun GridListLayout(
        paddingValues: PaddingValues,
        navToItem: (item: MainListItem) -> Unit,
        itemList: List<MainListItem>
    ) {
        LazyVerticalGrid(
            modifier = Modifier.padding(paddingValues),
            columns = GridCells.Fixed(2)
        ) {
            items(itemList) {
                ItemCardLayout(it, navToItem)
            }
        }
    }

    @Composable
    private fun ItemCardLayout(
        item: MainListItem,
        navToItem: (item: MainListItem) -> Unit
    ) {
        Card(
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
                .height(100.dp)
                .clickable(
                    onClick = {
                        navToItem(item)
                    }
                )
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val title = when (item) {
                    MainListItem.ParkingMap -> {
                        stringResource(id = R.string.parking_space_map_title)
                    }
                    MainListItem.GarbageTruckMap -> {
                        stringResource(id = R.string.garbage_truck_map_title)
                    }
                    MainListItem.TvProgramList -> {
                        stringResource(id = R.string.tv_program_list_title)
                    }
                    MainListItem.MapLocation -> {
                        stringResource(id = R.string.map_location_title)
                    }
                    MainListItem.TextToSpeech -> {
                        stringResource(id = R.string.text_to_speech_title)
                    }
                }
                Text(text = title)
            }
        }
    }
}
