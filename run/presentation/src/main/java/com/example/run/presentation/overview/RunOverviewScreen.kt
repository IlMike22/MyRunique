@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.run.presentation.overview

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.AnalyticsIcon
import com.example.core.presentation.designsystem.LogoIcon
import com.example.core.presentation.designsystem.LogoutIcon
import com.example.core.presentation.designsystem.MyRuniqueTheme
import com.example.core.presentation.designsystem.RunIcon
import com.example.core.presentation.designsystem.components.MyRuniqueFloatingActionButton
import com.example.core.presentation.designsystem.components.MyRuniqueScaffold
import com.example.core.presentation.designsystem.components.MyRuniqueToolbar
import com.example.core.presentation.designsystem.components.util.DropDownItem
import com.example.run.presentation.R
import com.example.run.presentation.overview.components.RunListItem
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    onStartRunClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onAnalyticsClick: () -> Unit,
    viewModel: RunOverviewViewModel = koinViewModel()
) {
    RunOverviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                RunOverviewAction.OnStartClick -> onStartRunClick()
                RunOverviewAction.OnLogoutClick -> onLogoutClick()
                RunOverviewAction.OnAnalyticsClick -> onAnalyticsClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RunOverviewScreen(
    state: RunOverviewState,
    onAction: (RunOverviewAction) -> Unit
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior(
        state = topAppBarState
    )

    MyRuniqueScaffold(
        topAppBar = {
            MyRuniqueToolbar(
                showBackButton = false,
                title = stringResource(R.string.my_runique_title),
                menuItems = listOf(
                    DropDownItem(AnalyticsIcon, stringResource(id = R.string.analytics)),
                    DropDownItem(LogoutIcon, stringResource(id = R.string.logout))
                ),
                startContent = {
                    Icon(
                        imageVector = LogoIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(30.dp)
                    )
                },
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> onAction(RunOverviewAction.OnAnalyticsClick)
                        1 -> onAction(RunOverviewAction.OnLogoutClick)
                    }
                },
                scrollBehavior = scrollBehaviour
            )
        },
        floatingActionButton = {
            MyRuniqueFloatingActionButton(
                icon = RunIcon,
                onClick = { onAction(RunOverviewAction.OnStartClick) })
        },
        content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .nestedScroll(scrollBehaviour.nestedScrollConnection)
                    .padding(horizontal = 16.dp),
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = state.runs,
                    key = { it.id }
                ) {
                    RunListItem(
                        runUi = it,
                        onDeleteClick = { onAction(RunOverviewAction.DeleteRun(it)) },
                    modifier = Modifier
                        .animateItemPlacement()
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    MyRuniqueTheme {
        RunOverviewScreen(
            state = RunOverviewState(),
            onAction = {}
        )
    }
}