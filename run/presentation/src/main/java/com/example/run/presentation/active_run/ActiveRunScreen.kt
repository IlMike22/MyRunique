@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.run.presentation.active_run

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.presentation.designsystem.MyRuniqueTheme
import com.example.core.presentation.designsystem.StartIcon
import com.example.core.presentation.designsystem.StopIcon
import com.example.core.presentation.designsystem.components.MyRuniqueDialog
import com.example.core.presentation.designsystem.components.MyRuniqueFloatingActionButton
import com.example.core.presentation.designsystem.components.MyRuniqueOutlinedActionButton
import com.example.core.presentation.designsystem.components.MyRuniqueScaffold
import com.example.core.presentation.designsystem.components.MyRuniqueToolbar
import com.example.run.presentation.R
import com.example.run.presentation.components.RunDataCard
import com.example.run.presentation.util.hasLocationPermission
import com.example.run.presentation.util.hasNotificationPermission
import com.example.run.presentation.util.shouldShowLocationPermissionRationale
import com.example.run.presentation.util.shouldShowNotificationPermissionRationale
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveRunScreenRoot(
    viewModel: ActiveRunViewModel = koinViewModel()
) {

    ActiveRunScreen(
        state = viewModel.state,
        onAction = viewModel::onAction
    )

}

@Composable
fun ActiveRunScreen(
    state: ActiveRunState,
    onAction: (ActiveRunAction) -> Unit
) {
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions -> // this block comes into play when user did sth on the perm dialog
        val hasCourseLocationPermission =
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        val hasFineLocationPermission =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true
        val hasNotificationPermission =
            if (Build.VERSION.SDK_INT >= 33) permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true else true

        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = hasCourseLocationPermission && hasFineLocationPermission,
                showLocationPermissionRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationPermissionRationale = showNotificationRationale
            )
        )
    }

    LaunchedEffect(key1 = true) {/* this will be called only one time at startup when using "true" */
        val activity = context as ComponentActivity
        val showLocationRationale = activity.shouldShowLocationPermissionRationale()
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()

        onAction(
            ActiveRunAction.SubmitLocationPermissionInfo(
                acceptedLocationPermission = context.hasLocationPermission(),
                showLocationPermissionRationale = showLocationRationale
            )
        )

        onAction(
            ActiveRunAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationPermissionRationale = showNotificationRationale
            )
        )

        if (!showLocationRationale && !showNotificationRationale ) {
            permissionLauncher.requestMyRuniquePermissions(context)
        }

    }
    MyRuniqueScaffold(
        widthGradient = false,
        topAppBar = {
            MyRuniqueToolbar(
                showBackButton = true,
                title = stringResource(id = R.string.active_run),
                onBackClick = {
                    onAction(ActiveRunAction.OnBackClick)
                }
            )
        },
        floatingActionButton = {
            MyRuniqueFloatingActionButton(
                icon = if (state.shouldTrack) StopIcon else StartIcon,
                onClick = {
                    onAction(ActiveRunAction.OnToggleRunClick)
                },
                iconSize = 20.dp,
                contentDescription = if (state.shouldTrack) stringResource(id = R.string.pause_run) else stringResource(
                    R.string.start_run
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            RunDataCard(
                elapsedTime = state.elapsedTime,
                runData = state.runData,
                modifier = Modifier
                    .padding(16.dp)
                    .padding(padding)
                    .fillMaxWidth()
            )
        }
    }

    if (state.showLocationPermissionRationale || state.showNotificationPermissionRationale) {
        MyRuniqueDialog(
            title = stringResource(id = R.string.permission_required),
            description = when {
                state.showLocationPermissionRationale && state.showNotificationPermissionRationale -> {
                    stringResource(id = R.string.location_notification_rationale)
                }

                state.showLocationPermissionRationale -> {
                    stringResource(id = R.string.location_rationale)
                }

                else -> {
                    stringResource(id = R.string.notification_rationale)
                }
            },
            onDismiss = {/* Normal dismissing not allowed for permission */ },
            primaryButton = {
                MyRuniqueOutlinedActionButton(
                    text = stringResource(R.string.okay),
                    isLoading = false,
                    onClick = {
                        onAction(ActiveRunAction.DismissRationaleDialog)
                        permissionLauncher.requestMyRuniquePermissions(context)
                    }
                )
            }
        )
    }
}

private fun ActivityResultLauncher<Array<String>>.requestMyRuniquePermissions(
    context: Context
) {
    val hasLocationPermission = context.hasLocationPermission()
    val hasNotificationPermission = context.hasNotificationPermission()
    val locationPermissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        arrayOf(Manifest.permission.POST_NOTIFICATIONS)
    } else arrayOf()

    when {
        !hasLocationPermission && !hasNotificationPermission -> {
            launch(locationPermissions + notificationPermission)
        }

        !hasLocationPermission -> {
            launch(locationPermissions)
        }

        else -> launch(notificationPermission)
    }
}

@Preview
@Composable
private fun ActiveRunScreenPreview() {
    MyRuniqueTheme {
        ActiveRunScreen(
            state = ActiveRunState(

            ),
            onAction = {}
        )
    }
}