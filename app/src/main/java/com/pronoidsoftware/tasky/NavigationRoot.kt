package com.pronoidsoftware.tasky

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.pronoidsoftware.agenda.presentation.edittext.EditTextScreenRoot
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
        startDestination = AgendaScreen(
            text = null,
        ),
    ) {
        composable<AgendaScreen> {
            val args = it.toRoute<AgendaScreen>()
            AgendaOverviewScreenRoot(
                fabAction = {
                    navController.navigate(
                        EditTextScreen(
                            editType = EditType.Title.title,
                            value = args.text,
                        ),
                    )
                },
                text = args.text,
            )
        }
        composable<EditTextScreen> {
            val args = it.toRoute<EditTextScreen>()
            EditTextScreenRoot(
                title = args.editType,
                actionTitle = "Save",
                onBackClick = {
                    navController.navigateUp()
                },
                onSaveClick = { updatedText ->
                    navController.navigate(
                        AgendaScreen(
                            text = updatedText,
                        ),
                    ) {
                        popUpTo(EditTextScreen) {
                            inclusive = true
                        }
                    }
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
data class AgendaScreen(
    val text: String?,
)

@Serializable
data class EditTextScreen(
    val editType: String,
    val value: String?,
)

sealed class EditType(val title: String) {
    data object Title : EditType("EDIT TITLE")
    data object Description : EditType("EDIT DESCRIPTION")
}
