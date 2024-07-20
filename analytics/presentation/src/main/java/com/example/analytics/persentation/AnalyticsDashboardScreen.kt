package com.example.analytics.persentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.analytics.persentation.components.AnalyticsCard
import com.example.core.presentation.designsystem.MyRuniqueTheme
import com.example.core.presentation.designsystem.components.MyRuniqueScaffold
import com.example.core.presentation.designsystem.components.MyRuniqueToolbar
import org.koin.androidx.compose.koinViewModel

@Composable
fun AnalyticsDashboardScreenRoot(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: AnalyticsDashboardViewModel = koinViewModel(),
) {
    AnalyticsDashboardScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                AnalyticsAction.OnBackClick -> onBack()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsDashboardScreen(
    modifier: Modifier = Modifier,
    state: AnalyticsDashboardState?,
    onAction: (AnalyticsAction) -> Unit
) {
    MyRuniqueScaffold(
        topAppBar = {
            MyRuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.analytics),
                onBackClick = {
                    onAction(AnalyticsAction.OnBackClick)
                }
            )
        }
    ) { padding ->
        if (state == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.total_distance_run),
                        value = state.totalDistanceRun,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AnalyticsCard(
                        title = stringResource(id = R.string.total_time_run),
                        value = state.totalTimeRun,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.total_distance_run),
                        value = state.totalDistanceRun,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    AnalyticsCard(
                        title = stringResource(id = R.string.total_time_run),
                        value = state.totalTimeRun,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.fastest_ever_run),
                        value = state.fastestEverRun,
                        modifier = Modifier.weight(1f)
                    )
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    AnalyticsCard(
                        title = stringResource(id = R.string.average_distance_per_run),
                        value = state.averageDistance,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    AnalyticsCard(
                        title = stringResource(id = R.string.average_pace_per_run),
                        value = state.averagePace,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AnalyticsDashboardScreenPreview() {
    MyRuniqueTheme {
        AnalyticsDashboardScreen(
            state = AnalyticsDashboardState(
                totalDistanceRun = "2.0 km",
                totalTimeRun = "2h50",
                fastestEverRun = "13km/h",
                averageDistance = "15km",
                averagePace = "07:10"
            ),
            onAction = {}
        )
    }
}