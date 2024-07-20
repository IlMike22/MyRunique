package com.example.analytics.analytics_feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.analytics.di.analyticsModule
import com.example.analytics.persentation.AnalyticsDashboardScreenRoot
import com.example.analytics.persentation.di.analyticsPresentationModule
import com.example.core.presentation.designsystem.MyRuniqueTheme
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.core.context.loadKoinModules

class AnalyticsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(
            listOf(analyticsModule, analyticsPresentationModule)
        )

        SplitCompat.installActivity(this)

        setContent {
            MyRuniqueTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "analytics_dashboard"
                ) {
                    composable("analytics_dashboard") {
                        AnalyticsDashboardScreenRoot(
                            onBack = { finish() }
                        )
                    }
                }
            }
        }
    }
}