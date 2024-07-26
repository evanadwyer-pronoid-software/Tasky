package com.pronoidsoftware.tasky

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.pronoidsoftware.agenda.domain.model.AgendaItemType
import com.pronoidsoftware.agenda.presentation.detail.AgendaDetailScreenRoot
import com.pronoidsoftware.agenda.presentation.overview.AgendaOverviewScreenRoot
import com.pronoidsoftware.auth.presentation.login.LoginScreenRoot
import com.pronoidsoftware.auth.presentation.register.RegisterScreenRoot
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(navController: NavHostController, isLoggedIn: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) AgendaFeature else AuthFeature,
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
            AgendaOverviewScreenRoot(
                onCreateAgendaItem = { type, isEditing ->
                    navController.navigate(
                        DetailScreen(
                            type = type.toString(),
                            isEditing = isEditing,
                        ),
                    )
                },
            )
        }
        composable<DetailScreen> {
            val args = it.toRoute<DetailScreen>()
            AgendaDetailScreenRoot(
                type = AgendaItemType.from(args.type),
                isEditing = args.isEditing,
                onCloseClick = {
                    navController.navigateUp()
                },
            )
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

@Serializable
data class DetailScreen(
    val type: String,
    val isEditing: Boolean,
)
