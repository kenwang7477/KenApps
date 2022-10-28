package com.kenwang.kenapps.ui.cctvtool.cctvlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.CctvMonitor
import com.kenwang.kenapps.ui.commonscreen.EmptyView
import com.kenwang.kenapps.ui.commonscreen.LoadingView
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalLifecycleComposeApi::class, ExperimentalMaterial3Api::class)
object CctvListScreen {

    @Composable
    fun CctvListUI(
        paddingValues: PaddingValues,
        toCctvMap: (cctvMonitor: CctvMonitor) -> Unit,
        viewModel: CctvListViewModel = hiltViewModel()
    ) {
        when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
            is CctvListViewModel.CctvListViewState.Success -> {
                Column(modifier = Modifier.padding(paddingValues)) {
                    SearchLayout(viewModel = viewModel)
                    CctvListLayout(
                        toCctvMap = toCctvMap,
                        cctvList = state.cctvList
                    )
                }
            }
            is CctvListViewModel.CctvListViewState.Empty -> {
                EmptyView(
                    modifier = Modifier.padding(paddingValues),
                    text = stringResource(id = R.string.no_data)
                )
            }
            is CctvListViewModel.CctvListViewState.Loading -> {
                LoadingView(
                    modifier = Modifier.padding(paddingValues)
                )
            }
        }
    }

    @Composable
    fun SearchLayout(viewModel: CctvListViewModel) {
        var searchText by remember { mutableStateOf("") }
        OutlinedTextField(
            value = searchText,
            onValueChange = {
                searchText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(5.dp),
            label = { Text(text = stringResource(R.string.please_input_location)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { viewModel.getCctvList(searchText) }),
            trailingIcon = {
                if (searchText.isNotEmpty()) {
                    IconButton(
                        onClick = {
                            searchText = ""
                            viewModel.getCctvList()
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
    fun CctvListLayout(
        toCctvMap: (cctvMonitor: CctvMonitor) -> Unit,
        cctvList: ImmutableList<CctvMonitor>
    ) {
        LazyColumn {
            items(cctvList) { cctvMonitor ->
                CctvViewItem(cctvMonitor = cctvMonitor, toCctvMap = toCctvMap)
            }
        }
    }

    @Composable
    fun CctvViewItem(
        cctvMonitor: CctvMonitor,
        toCctvMap: (cctvMonitor: CctvMonitor) -> Unit,
    ) {
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .padding(5.dp)
                .clickable { toCctvMap(cctvMonitor) }
        ) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = cctvMonitor.roadsection)
            }
        }
    }
}
