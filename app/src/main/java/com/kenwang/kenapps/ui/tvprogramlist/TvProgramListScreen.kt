package com.kenwang.kenapps.ui.tvprogramlist

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.tvprogramlist.repository.TvProgramRepository
import com.kenwang.kenapps.R
import com.kenwang.kenapps.data.model.TvProgram
import com.kenwang.kenapps.ui.commonscreen.EmptyView
import com.kenwang.kenapps.ui.commonscreen.LoadingView
import com.kenwang.kenapps.utils.ChromeTabUtil

object TvProgramListScreen {

    @Composable
    fun TvProgramListUI(
        paddingValues: PaddingValues,
        viewModel: TvProgramListViewModel = hiltViewModel()
    ) {
        Column(modifier = Modifier.padding(paddingValues)) {
            ProgramSelector(viewModel)
            when (val state = viewModel.viewState.collectAsStateWithLifecycle().value) {
                is TvProgramListViewModel.TvProgramListViewState.Success -> {
                    ProgramList(programList = state.programs)
                }
                is TvProgramListViewModel.TvProgramListViewState.Loading -> {
                    LoadingView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
                is TvProgramListViewModel.TvProgramListViewState.Empty -> {
                    EmptyView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                    )
                }
            }
        }
    }

    @Composable
    fun ProgramSelector(viewModel: TvProgramListViewModel) {
        val menus = TvProgramRepository.TvProgramEnum.entries.map { it.title }
        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(menus[0]) }
        var textFieldSize by remember { mutableStateOf(Size.Zero) }
        val icon = if (expanded) {
            Icons.Filled.KeyboardArrowUp
        } else {
            Icons.Filled.KeyboardArrowDown
        }

        Column(Modifier.padding(20.dp)) {
            OutlinedTextField(
                value = selectedText,
                readOnly = true,
                enabled = false,
                onValueChange = {
                    selectedText = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .onGloballyPositioned { coordinates ->
                        //This value is used to assign to the DropDown the same width
                        textFieldSize = coordinates.size.toSize()
                    },
                colors = TextFieldDefaults.colors(
                    disabledLabelColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.Transparent,
                    disabledIndicatorColor = MaterialTheme.colorScheme.primary,
                    disabledTextColor = MaterialTheme.colorScheme.primary,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.primary
                ),
                label = { Text(text = stringResource(id = R.string.website)) },
                trailingIcon = {
                    Icon(
                        imageVector = icon,
                        contentDescription = "contentDescription"
                    )
                }
            )

            DropdownMenu(
                modifier = Modifier.width(with(LocalDensity.current){ textFieldSize.width.toDp() }),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                menus.forEach { menu ->
                    DropdownMenuItem(
                        text = { Text(text = menu) },
                        onClick = {
                            selectedText = menu
                            expanded = false
                            val eProgram = TvProgramRepository.TvProgramEnum.getTvProgramEnum(menu)
                            viewModel.getProgramList(eProgram)
                        }
                    )
                }
            }
        }
    }

    @Composable
    fun ProgramList(programList: List<TvProgram>) {
        LazyColumn {
            items(programList) { program ->
                ProgramCardItem(program = program)
            }
        }
    }

    @Composable
    fun ProgramCardItem(program: TvProgram) {
        val ctx = LocalContext.current
        Card(
            shape = RoundedCornerShape(5.dp),
            border = BorderStroke(width = 2.dp, color = MaterialTheme.colorScheme.onBackground),
            modifier = Modifier
                .padding(5.dp)
                .clickable { ChromeTabUtil.openTab(ctx, program.link) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 50.dp)
                    .padding(10.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(program.name)
            }
        }
    }
}
