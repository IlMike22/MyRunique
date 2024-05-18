@file:Suppress("OPT_IN_USAGE_FUTURE_ERROR")

package com.example.auth.presentation.register

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel(): ViewModel() {
    var state by mutableStateOf(RegisterState())
        private set

    fun onAction(action:RegisterAction) {

    }

}