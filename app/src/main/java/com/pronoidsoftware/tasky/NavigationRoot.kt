package com.pronoidsoftware.tasky

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.pronoidsoftware.auth.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AuthFeature,
    ) {
        authGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AuthFeature>(
        startDestination = RegisterScreen,
    ) {
        composable<RegisterScreen> {
            RegisterScreenRoot(
                onLogInClick = {
                    navController.navigate(LoginScreen) {
                        popUpTo(RegisterScreen) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
                onSuccessfulRegistration = {
                    navController.navigate(LoginScreen)
                },
            )
        }
        composable<LoginScreen> {
            Text(text = "LoginScreen")
        }
    }
}

@Serializable
object AuthFeature

@Serializable
object RegisterScreen

@Serializable
object LoginScreen
