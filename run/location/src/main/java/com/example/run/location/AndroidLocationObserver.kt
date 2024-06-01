package com.example.run.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.content.getSystemService
import com.example.core.domain.location.LocationWithAltitude
import com.example.run.domain.LocationObserver
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AndroidLocationObserver(
    private val context: Context
) : LocationObserver {
    private val client = LocationServices.getFusedLocationProviderClient(context)

    override fun observeLocation(interval: Long): Flow<LocationWithAltitude> {
        return callbackFlow {
            val locationManager = context.getSystemService<LocationManager>()!!
            var isGpsEnabled = false
            var isNetworkEnabled = false
            while (!isGpsEnabled && !isNetworkEnabled) {
                isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                isNetworkEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                if (!!isGpsEnabled && !isNetworkEnabled) {
                    delay(3000L)
                }
            }
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                close()
            } else {
                client.lastLocation.addOnSuccessListener {
                    it?.let { location ->
                        /**
                         * Send the current value to the collectors that observe the flow.
                         */
                        trySend(location.toLocationWithAltitude())
                    }
                }
                val request = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    interval
                )
                    .build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        result.locations.lastOrNull()?.let { location ->
                            /**
                             * Send the current value to the collectors that observe the flow.
                             */
                            trySend(location.toLocationWithAltitude())
                        }
                    }
                }
                client.requestLocationUpdates(request, locationCallback, Looper.getMainLooper())

                /**
                 * When ViewModel is closed (due to screenchange e.g., the flow will also be closed and then
                 * we want to make some cleanup first.
                 * We only want to listen to the user's location state as long we need it.
                 */
                awaitClose {
                    client.removeLocationUpdates(locationCallback)
                }
            }
        }
    }
}