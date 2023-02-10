package com.orels.jeruchess.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orels.jeruchess.android.core.presentation.Routes
import com.orels.jeruchess.android.presentation.forgot_password.ForgotPasswordScreen
import com.orels.jeruchess.android.presentation.login.LoginScreen
import com.orels.jeruchess.android.presentation.register.RegisterScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
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
                            LoginScreen(navController = navController)
                        }
                        composable(
                            route = Routes.FORGOT_PASSWORD,
                        ) {
                            ForgotPasswordScreen(navController = navController)
                        }
                        composable(
                            route = Routes.REGISTER,
                        ) {
                            RegisterScreen(navController = navController)
                        }

                    }
                }
            }
        }
    }
}