@file:OptIn(ExperimentalCoroutinesApi::class)

package com.example.run.domain

import com.example.core.domain.Timer
import com.example.core.domain.location.LocationTimeStamp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.zip
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class RunningTracker(
    private val locationObserver: LocationObserver,
    private val applicationScope: CoroutineScope
) {
    /**
     * Normally using states in such singleton object is not good.
     * But here we need to keep the states even if the app is killed.
     * Since we use these states in our foreground service, we can have
     * access to these states even the user killed the app.
     */
    private val _runData = MutableStateFlow(RunData())
    val runData = _runData.asStateFlow()

    private val _isTracking = MutableStateFlow(false)
    val isTracking = _isTracking.asStateFlow()

    private val isObservingLocation = MutableStateFlow(false)

    private val _elapsedTime = MutableStateFlow(Duration.ZERO)
    val elapsedime = _elapsedTime.asStateFlow()


    /**
     * We are now listen to every change of isObservingLocation and with flatMapLatest we can
     * convert the outcome of the original flow (isObservingLocation which is a boolean) to a
     * different flow! Here we map the outcome of isObservingLocation to the flow which is returned
     * by our locationObserver (which is a LocationWithAltitude flow).
     * We are then converting it to a StateFlow again (via stateIn())
     * If we are not oberving the location with empty flow (flowOf()) we just do nothing in this
     * case.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val currentLocation = isObservingLocation
        .flatMapLatest { isObservingLocation ->
            if (isObservingLocation) {
                locationObserver.observeLocation(1000L)
            } else flowOf()
        }
        .stateIn(
            applicationScope,
            SharingStarted.Lazily,
            null
        )

    init {
        _isTracking
            .onEach { isTracking ->
                if (!isTracking) {
                    val newList = buildList {
                        addAll(runData.value.locations)
                        add(emptyList<LocationTimeStamp>())
                    }.toList()
                    _runData.update {
                        it.copy(locations = newList)
                    }
                }
            }
            .flatMapLatest { isTracking ->
                if (isTracking) {
                    Timer.timeAndEmit()
                } else flowOf()
            }
            .onEach {
                _elapsedTime.value += it
            }
            .launchIn(applicationScope)

        /**
         * When tracking is true and we have a non null location, we emit the location.
         * FilterNotNull -> we emit only if the value is not null.
         * CombinTransform: Combine two flows and put them into another value, transform means
         * we get the emit collector here instead.
         */
        currentLocation
            .filterNotNull() // emit only if location is not null
            .combineTransform(_isTracking) { location, isTracking ->
                if (isTracking) {
                    emit(location)
                }
            }.zip(_elapsedTime) { location, elapsedTime ->
                // is triggered if there is an emission for both values, not only for one of them.
                // So either elapsedTime and location should be emitted to come into this code here.
                LocationTimeStamp(
                    location = location,
                    durationTimestamp = elapsedTime
                )
            }
            .onEach { location ->
                val currentLocations = runData.value.locations
                val lastLocations = if (currentLocations.isNotEmpty()) {
                    currentLocations.last() + location
                } else listOf(location)

                val newLocations = currentLocations.replaceLast(lastLocations)
                val distanceMeters = LocationDataCalculator.getTotalDistanceMeters(
                    locations = newLocations
                )
                val distanceKm = distanceMeters / 1000.0
                val currentDuration = location.durationTimestamp
                val averageSecondsKm = if (distanceKm == 0.0) {
                    0
                } else {
                    (currentDuration.inWholeSeconds / distanceKm).roundToInt()
                }

                _runData.update {
                    RunData(
                        distanceMeters = distanceMeters,
                        pace = averageSecondsKm.seconds,
                        locations = newLocations
                    )
                }
            }
            .launchIn(applicationScope)
    }

    fun setIsTracking(isTracking: Boolean) {
        this._isTracking.value = isTracking
    }

    fun startObservingLocation() {
        isObservingLocation.value = true
    }

    fun stopObservingLocation() {
        isObservingLocation.value = false
    }

    fun finishRun() {
        stopObservingLocation()
        setIsTracking(false)
        _elapsedTime.value = Duration.ZERO
        _runData.value = RunData()
    }
}

private fun <T> List<List<T>>.replaceLast(replacement: List<T>): List<List<T>> {
    if (this.isEmpty()) {
        return listOf(replacement)
    }
    return this.dropLast(1) + listOf(replacement)
}