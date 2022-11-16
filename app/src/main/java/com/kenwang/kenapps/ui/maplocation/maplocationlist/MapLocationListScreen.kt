package com.kenwang.kenapps.ui.maplocation.maplocationlist

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.MapLocation
import com.kenwang.kenapps.ui.commonscreen.CustomDialog
import com.kenwang.kenapps.ui.commonscreen.EmptyView
import com.kenwang.kenapps.ui.commonscreen.LoadingView
import com.kenwang.kenapps.utils.TimeUtil
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLifecycleComposeApi::class)
object MapLocationListScreen {

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @Composable
    fun MapLocationListUI(
        paddingValues: PaddingValues,
        viewModel: MapLocationListViewModel = hiltViewModel(),
        toMapLocationMap: (longitude: Double?, latitude: Double?) -> Unit
    ) {
        Scaffold(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        toMapLocationMap(null, null)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
                }
            },
            content = {
                when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                    is MapLocationListViewModel.MapLocationListViewState.Success -> {
                        MapLocationList(
                            onDeleteMapLocation = {
                                viewModel.deleteMapLocation(it)
                            },
                            mapLocationList = state.list,
                            toMapLocationMap = toMapLocationMap
                        )
                    }
                    is MapLocationListViewModel.MapLocationListViewState.Empty -> {
                        EmptyView(modifier = Modifier.fillMaxSize())
                    }
                    is MapLocationListViewModel.MapLocationListViewState.Loading -> {
                        LoadingView(modifier = Modifier.fillMaxSize())
                    }
                }
            }
        )
    }

    @Composable
    private fun MapLocationList(
        onDeleteMapLocation: (mapLocation: MapLocation) -> Unit,
        mapLocationList: ImmutableList<MapLocation>,
        toMapLocationMap: (longitude: Double, latitude: Double) -> Unit
    ) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(mapLocationList) {
                MapLocationListItem(
                    onDeleteMapLocation = onDeleteMapLocation,
                    mapLocation = it,
                    toMapLocationMap = toMapLocationMap
                )
            }
        }
    }

    @Composable
    private fun MapLocationListItem(
        onDeleteMapLocation: (mapLocation: MapLocation) -> Unit,
        mapLocation: MapLocation,
        toMapLocationMap: (longitude: Double, latitude: Double) -> Unit
    ) {
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .clickable { toMapLocationMap(mapLocation.longitude, mapLocation.latitude) }
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                val (titleId, descriptionId, timestampId, deleteIconId) = createRefs()

                Text(
                    text = "${stringResource(id = R.string.title)}: ${mapLocation.title}",
                    modifier = Modifier.constrainAs(titleId) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(anchor = deleteIconId.start, margin = 10.dp)
                        width = Dimension.fillToConstraints
                    }
                )
                Text(
                    text = "${stringResource(id = R.string.description)}: ${mapLocation.description}",
                    modifier = Modifier.constrainAs(descriptionId) {
                        start.linkTo(parent.start)
                        top.linkTo(anchor = titleId.bottom, margin = 5.dp)
                        end.linkTo(anchor = titleId.end)
                        width = Dimension.fillToConstraints
                    }
                )
                Text(
                    text = "${stringResource(id = R.string.date)}: ${TimeUtil.timestampToDate(mapLocation.timestamp)}",
                    modifier = Modifier.constrainAs(timestampId) {
                        start.linkTo(parent.start)
                        top.linkTo(anchor = descriptionId.bottom, margin = 5.dp)
                        end.linkTo(anchor = titleId.end)
                        width = Dimension.fillToConstraints
                    }
                )

                var dialogState by remember { mutableStateOf(false) }
                IconButton(
                    onClick = { dialogState = true },
                    modifier = Modifier.constrainAs(deleteIconId) {
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "delete")
                }
                if (dialogState) {
                    ConfirmDeleteDialog(
                        onConfirm = {
                            onDeleteMapLocation(mapLocation)
                            dialogState = false
                        },
                        onDismiss = { dialogState = false }
                    )
                }
            }
        }
    }

    @Composable
    private fun ConfirmDeleteDialog(
        onConfirm: () -> Unit,
        onDismiss: () -> Unit
    ) {
        CustomDialog(
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = stringResource(id = R.string.confirm))
                }
            },
            cancelButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            onDismiss = onDismiss
        ) {
            Text(text = stringResource(id = R.string.confirm_to_delete_record))
        }
    }
}
