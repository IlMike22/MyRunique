@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.run.presentation.overview

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
import org.koin.androidx.compose.koinViewModel

@Composable
fun RunOverviewScreenRoot(
    viewModel: RunOverviewViewModel = koinViewModel()
) {
    RunOverviewScreen(
        onAction = viewModel::onAction
    )
}

@Composable
fun RunOverviewScreen(
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

        }
    )
}

@Preview
@Composable
private fun RunOverviewScreenPreview() {
    MyRuniqueTheme {
        RunOverviewScreen(
            onAction = {}
        )
    }
}