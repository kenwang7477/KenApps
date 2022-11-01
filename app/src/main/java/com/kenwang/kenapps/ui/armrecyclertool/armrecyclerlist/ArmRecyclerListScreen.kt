package com.kenwang.kenapps.ui.armrecyclertool.armrecyclerlist

import android.location.Geocoder
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenwang.kenapps.data.model.ArmRecycler
import com.kenwang.kenapps.extensions.isVersionAboveTiramisu
import com.kenwang.kenapps.ui.commonscreen.EmptyView
import com.kenwang.kenapps.ui.commonscreen.LoadingView
import com.kenwang.kenapps.utils.MapUtil
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalLifecycleComposeApi::class)
object ArmRecyclerListScreen {

    @Composable
    fun ArmRecyclerListUI(
        paddingValues: PaddingValues,
        viewModel: ArmRecyclerListViewModel = hiltViewModel()
    ) {
        when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
            is ArmRecyclerListViewModel.ArmRecyclerListViewState.Success -> {
                ArmRecyclerList(
                    modifier = Modifier.padding(paddingValues),
                    armRecyclers = state.armRecyclers
                )
//                var loadedState by remember { mutableStateOf(false) }
//                LoadAddress(armRecyclers = state.armRecyclers) {
//                    loadedState = true
//                }
//                if (loadedState) {
//                    ArmRecyclerList(
//                        modifier = Modifier.padding(paddingValues),
//                        navController = navController,
//                        armRecyclers = state.armRecyclers
//                    )
//                } else {
//                    Column(
//                        modifier = Modifier.padding(paddingValues),
//                        verticalArrangement = Arrangement.Center
//                    ) {
//                        Text(text = stringResource(id = R.string.loading))
//                    }
//                }
            }
            is ArmRecyclerListViewModel.ArmRecyclerListViewState.Empty -> {
                EmptyView(
                    modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                )
            }
            is ArmRecyclerListViewModel.ArmRecyclerListViewState.Loading -> {
                LoadingView(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                )
            }
        }
    }

    @Composable
    fun LoadAddress(
        armRecyclers: List<ArmRecycler>,
        onSuccess: () -> Unit
    ) {
        val context = LocalContext.current
        val successState by rememberUpdatedState(onSuccess)
        LaunchedEffect(true) {
            val geoCoder = Geocoder(context)
            armRecyclers.forEach { armRecycler ->
                if (isVersionAboveTiramisu()) {
                    geoCoder.getFromLocationName(armRecycler.address, 1) {
                        it.getOrNull(0)?.let { address ->
                            armRecycler.longitude = address.longitude ?: 0.0
                            armRecycler.latitude = address.latitude ?: 0.0
                        }
                    }
                } else {
                    val address = try {
                        geoCoder.getFromLocationName(armRecycler.address, 1)?.getOrNull(0)
                    } catch (e: Exception) { null }
                    armRecycler.longitude = address?.longitude ?: 0.0
                    armRecycler.latitude = address?.latitude ?: 0.0
                }
            }

            successState()
        }
    }

    @Composable
    fun ArmRecyclerList(
        modifier: Modifier,
        armRecyclers: ImmutableList<ArmRecycler>
    ) {
        LazyColumn(modifier = modifier) {
            items(armRecyclers) { armRecycler ->
                ArmRecyclerViewItem(armRecycler = armRecycler)
            }
        }
    }

    @Composable
    fun ArmRecyclerViewItem(armRecycler: ArmRecycler) {
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.padding(5.dp)
        ) {
            val context = LocalContext.current
            SelectionContainer(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .clickable {
                            MapUtil.openGoogleMapByAddress(context, armRecycler.address)
                        }
                ) {
                    Text(text = "單位名稱：${armRecycler.name}")
                    Text(text = "地址：${armRecycler.address}")
                    Text(text = "數量：${armRecycler.count}")
                    Text(text = "行政區：${armRecycler.area}")
                    Text(text = "使用時間：${armRecycler.time}")
                    Text(text = "回收項目：${armRecycler.recycleItem}")
                    Text(text = "詳細位置：${armRecycler.position}")
                }
            }
        }
    }
}
