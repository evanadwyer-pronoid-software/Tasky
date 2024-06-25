package com.pronoidsoftware.tasky

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.pronoidsoftware.auth.presentation.login.LoginScreenRoot
import com.pronoidsoftware.auth.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = AuthFeature,
    ) {
        authGraph(navController)
        agendaGraph(navController)
    }
}

private fun NavGraphBuilder.authGraph(navController: NavHostController) {
    navigation<AuthFeature>(
        startDestination = LoginScreen,
    ) {
        composable<LoginScreen> {
            LoginScreenRoot(
                onLoginSuccess = {
                    navController.navigate(AgendaFeature) {
                        popUpTo(AuthFeature) {
                            inclusive = true
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate(RegisterScreen) {
                        popUpTo(LoginScreen) {
                            inclusive = true
                            saveState = true
                        }
                        restoreState = true
                    }
                },
            )
        }
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
    }
}

private fun NavGraphBuilder.agendaGraph(navController: NavHostController) {
    navigation<AgendaFeature>(
        startDestination = AgendaScreen,
    ) {
        composable<AgendaScreen> {
            Text(text = "AgendaScreen")
        }
    }
}

// Auth
@Serializable
object AuthFeature

@Serializable
object RegisterScreen

@Serializable
object LoginScreen

// Agenda
@Serializable
object AgendaFeature

@Serializable
object AgendaScreen
