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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.orels.jeruchess.android.core.presentation.RoutesArguments.PRE_INSERTED_EMAIL
import com.orels.jeruchess.android.core.presentation.RoutesArguments.PRE_INSERTED_PHONE_NUMBER
import com.orels.jeruchess.android.core.presentation.Screens
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
    val context = LocalContext.current

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Loading(size = 8.dp, color = MaterialTheme.colors.onBackground)
        }
    } else {
        if (!state.isAuthenticated) {
            LoginScreen(navController = navController)
        } else {
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
                    startDestination = Screens.Main.route
                ) {
                    composable(route = Screens.Login.route) {
                        LoginScreen(navController = navController)
                    }
                    composable(route = Screens.ForgotPassword.route) {
                        ForgotPasswordScreen(navController = navController)
                    }
                    composable(
                        route = Screens.Register.withArgs(
                            PRE_INSERTED_PHONE_NUMBER,
                            PRE_INSERTED_EMAIL
                        ),
                        arguments = listOf(
                            navArgument(PRE_INSERTED_PHONE_NUMBER) {
                                defaultValue = ""
                            },
                            navArgument(PRE_INSERTED_EMAIL) {
                                defaultValue = ""
                            },
                        )
                    ) {
                        val preInsertedPhoneNumber =
                            it.arguments?.getString(
                                PRE_INSERTED_PHONE_NUMBER
                            ) ?: ""
                        val preInsertedEmail =
                            it.arguments?.getString(
                                PRE_INSERTED_EMAIL
                            ) ?: ""
                        RegisterScreen(
                            navController = navController,
                            preInsertedPhoneNumber = preInsertedPhoneNumber,
                            preInsertedEmail = preInsertedEmail
                        )
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
    }
}