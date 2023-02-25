package com.orels.jeruchess.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.orels.jeruchess.android.core.presentation.Routes
import com.orels.jeruchess.android.core.presentation.RoutesArguments.PRE_INSERTED_EMAIL
import com.orels.jeruchess.android.core.presentation.RoutesArguments.PRE_INSERTED_PHONE_NUMBER
import com.orels.jeruchess.android.presentation.auth.forgot_password.ForgotPasswordScreen
import com.orels.jeruchess.android.presentation.auth.login.LoginScreen
import com.orels.jeruchess.android.presentation.auth.register.RegisterScreen
import com.orels.jeruchess.android.presentation.main.AndroidMainViewModel
import com.orels.jeruchess.android.presentation.main.MainScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JeruChessTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navHostController = rememberNavController()
                    val navController = navHostController as NavController
                    NavHost(
                        navController = navHostController,
                        startDestination = Routes.LOGIN
                    ) {
                        composable(
                            route = Routes.LOGIN,
                        ) {
                            LoginScreen(
                                navController = navController,
                            )
                        }
                        composable(
                            route = Routes.FORGOT_PASSWORD,
                        ) {
                            ForgotPasswordScreen(navController = navController)
                        }
                        composable(
                            route = Routes.REGISTER + "/{$PRE_INSERTED_PHONE_NUMBER}/{$PRE_INSERTED_EMAIL}",
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
                        composable(
                            route = Routes.MAIN,
                        ) {
                            val viewModel = hiltViewModel<AndroidMainViewModel>()
                            val state by viewModel.state.collectAsState()
                            MainScreen(
                                navController = navController,
                                state = state,
                                viewModel = viewModel
                            )
                        }

                    }
                }
            }
        }
    }
}