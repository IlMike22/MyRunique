package com.example.myrunique

import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.auth.presentation.intro.IntroScreenRoot
import com.example.auth.presentation.login.LoginScreenRoot
import com.example.auth.presentation.register.RegisterScreenRoot
import com.example.run.presentation.active_run.ActiveRunScreenRoot
import com.example.run.presentation.overview.RunOverviewScreen
import com.example.run.presentation.overview.RunOverviewScreenRoot


@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "run" else "auth"
    ) {
        authGraph(navController)
        runGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation(
        route = "auth",
        startDestination = "intro"
    ) {
        composable(route = "intro") {
            IntroScreenRoot(
                onSignInClick = {
                    navController.navigate("login")
                },
                onSignUpClick = {
                    navController.navigate("register")
                }
            )
        }
        composable(route = "register") {
            RegisterScreenRoot(
                onSignInClick = {
                    navController.navigate("login") {
                        popUpTo("register") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = { navController.navigate("login") }
            )
        }
        composable(route = "login") {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate("run") {
                        popUpTo("auth") {
                            inclusive = true
                        }
                    }
                },
                onSignUpClick = {
                    navController.navigate("register") {
                        popUpTo("login") {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                }
            )
        }
    }
}

private fun NavGraphBuilder.runGraph(navController: NavController) {
    navigation(
        route = "run",
        startDestination = "run_overview"
    ) {
        composable(route = "run_overview") {
            RunOverviewScreenRoot(
                onStartRunClick = {
                    navController.navigate("active_run")
                }
            )
        }
        composable(route = "active_run") {
            ActiveRunScreenRoot()
        }
    }
}
