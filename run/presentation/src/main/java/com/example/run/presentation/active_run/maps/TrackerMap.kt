package com.example.run.presentation.active_run.maps

import android.graphics.Bitmap
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.core.domain.location.Location
import com.example.core.domain.location.LocationTimeStamp
import com.example.core.presentation.designsystem.RunIcon
import com.example.run.presentation.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@Composable
fun TrackerMap(
    modifier: Modifier = Modifier,
    isRunFinished: Boolean,
    currentLocation: Location?,
    locations: List<List<LocationTimeStamp>>,
    onSnapshot: (Bitmap) -> Unit,
) {
    val context = LocalContext.current
    val mapStyle = remember {
        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style)
    }
    val cameraPositionState = rememberCameraPositionState()
    val markerState = rememberMarkerState()

    val markerPositionLat by animateFloatAsState(
        targetValue = currentLocation?.latitude?.toFloat() ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )

    val markerPositionLong by animateFloatAsState(
        targetValue = currentLocation?.longitude?.toFloat() ?: 0f,
        animationSpec = tween(durationMillis = 500),
        label = ""
    )
    val markerPosition = remember(markerPositionLat, markerPositionLong) {
        LatLng(markerPositionLat.toDouble(), markerPositionLong.toDouble())
    }

    LaunchedEffect(markerPosition, isRunFinished) {
        if (!isRunFinished) {
            markerState.position = markerPosition
        }
    }

    LaunchedEffect(currentLocation, isRunFinished) {
        if (currentLocation != null && !isRunFinished) {
            val latlong = LatLng(currentLocation.latitude, currentLocation.longitude)
            cameraPositionState.animate(
                CameraUpdateFactory.newLatLngZoom(latlong, 17f)
            )
        }
    }

    GoogleMap(
        cameraPositionState = cameraPositionState,
        properties = MapProperties(
            mapStyleOptions = mapStyle
        ),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false
        )
    ) {
        MyRuniquePolylines(locations = locations)

        if (!isRunFinished && currentLocation != null) {
            MarkerComposable(
                currentLocation,
                state = markerState
            ) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = RunIcon,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}