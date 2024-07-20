package com.example.myrunique

data class MainState(
    val isLoggedIn:Boolean = false,
    val isCheckingAuth:Boolean = false,
    val showAnalyticsInstallDialog:Boolean = false
)
