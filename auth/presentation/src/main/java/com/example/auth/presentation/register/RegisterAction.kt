package com.example.auth.presentation.register

sealed interface RegisterAction {
    data object OnSignInClick : RegisterAction
    data object OnRegisterClick: RegisterAction
    data class OnEmailChange(val email: String) : RegisterAction
    data class OnPasswordChange(val password: String) : RegisterAction
    data object OnTogglePasswordVisibilityClick : RegisterAction
}