package com.orels.jeruchess.android.presentation.jeruchess

import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orels.jeruchess.android.core.presentation.Screens
import com.orels.jeruchess.android.domain.AuthState
import com.orels.jeruchess.android.presentation.auth.forgot_password.ForgotPasswordScreen
import com.orels.jeruchess.android.presentation.auth.login.LoginScreen
import com.orels.jeruchess.android.presentation.auth.register.RegisterScreen
import com.orels.jeruchess.android.presentation.components.CustomScaffold
import com.orels.jeruchess.android.presentation.components.Loading
import com.orels.jeruchess.android.presentation.main.AndroidMainViewModel
import com.orels.jeruchess.android.presentation.main.MainScreen
import com.orels.jeruchess.android.presentation.main.components.BottomBar

@Composable
fun JeruChess(
    viewModel: JeruChessViewModel = hiltViewModel()
) {
    val navHostController = rememberNavController()
    val navController = navHostController as NavController
    val state = viewModel.state

    if (state.isLoading || state.authState == AuthState.Loading) {
        LoadingContent()
    } else {
        if (state.authState is AuthState.LoggedOut || state.authState is AuthState.RegistrationRequired) {
            UnAuthenticatedContent(
                navController = navController,
                navHostController = navHostController
            )
        } else {
            AuthenticatedContent(
                navController = navController,
                navHostController = navHostController,
                viewModel = viewModel
            )
        }
    }
}

@Composable
fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Loading(size = 8.dp, color = MaterialTheme.colors.onBackground)
    }
}

@Composable
fun AuthenticatedContent(
    navController: NavController,
    navHostController: NavHostController,
    viewModel: JeruChessViewModel,
) {
    val context = LocalContext.current

    CustomScaffold(
        navHostController = navHostController,
        topBar = { /*TODO*/ },
        bottomBar = {
            BottomBar(navHostController = navHostController,
                onNavigationClick = { viewModel.navigateToClub(context = context) })
        }
    ) {
        NavHost(
            modifier = Modifier.padding(
                top = it.calculateTopPadding(),
                bottom = it.calculateBottomPadding(),
                end = it.calculateEndPadding(LayoutDirection.Rtl),
                start = it.calculateStartPadding(LayoutDirection.Rtl)
            ),
            navController = navHostController,
            startDestination = if (viewModel.state.authState.name == AuthState.RegistrationRequired().name) {
                Screens.Register.route

            } else Screens.Main.route
        ) {
            composable(route = Screens.Login.route) {
                LoginScreen(navController = navController)
            }
            composable(route = Screens.Register.route) {
                RegisterScreen(navController = navController)
            }
            composable(route = Screens.Main.route) {
                val mainViewModel = hiltViewModel<AndroidMainViewModel>()
                val mainState by mainViewModel.state.collectAsState()
                MainScreen(
                    navController = navController,
                    state = mainState,
                    viewModel = mainViewModel
                )
            }

        }
    }
}

@Composable
fun UnAuthenticatedContent(
    navController: NavController,
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screens.Login.route
    ) {
        composable(route = Screens.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screens.Register.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screens.ForgotPassword.route) {
            ForgotPasswordScreen(navController = navController)
        }
    }
}