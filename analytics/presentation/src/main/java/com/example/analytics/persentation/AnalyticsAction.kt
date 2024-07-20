package com.example.analytics.persentation

sealed interface AnalyticsAction {
    data object OnBackClick : AnalyticsAction
}
