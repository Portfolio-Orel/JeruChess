package com.orels.jeruchess.android.presentation.auth.login

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.core.presentation.Routes
import com.orels.jeruchess.android.presentation.components.Input
import com.orels.jeruchess.android.presentation.components.Loading
import com.orels.jeruchess.android.presentation.components.noRippleClickable

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state = viewModel.state
    val context = LocalContext.current

    navController.graph
    val phoneNumber = remember { mutableStateOf("") }

    LaunchedEffect(key1 = state.authState) {
        if (state.authState == AuthState.AUTHENTICATED) {
            navController.navigate(Routes.MAIN) {
                popUpTo(Routes.LOGIN) { inclusive = true }
            }
        } else if (state.authState == AuthState.REGISTRATION_REQUIRED) {
            navController.navigate(
                Routes.withArgs(
                    Routes.REGISTER,
                    state.user.phoneNumber,
                    state.user.email
                )
            )
        }
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Loading(
                modifier = Modifier
                    .size(48.dp)
                    .zIndex(1f),
                color = MaterialTheme.colors.onBackground,
                strokeWidth = 2.dp,
            )
        }
    } else {
        Column(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxSize()
                .padding(20.dp)
                .background(MaterialTheme.colors.background.copy(alpha = 0.3f)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.zIndex(1f),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Input(
                    title = stringResource(R.string.phone_number),
                    placeholder = stringResource(R.string.phone_number),
                    initialText = "",
                    keyboardType = KeyboardType.Phone,
                    isPassword = false,
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Filled.Phone,
                            stringResource(R.string.phone_number)
                        )
                    },
                    onTextChange = {
                        phoneNumber.value = it
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                AppLogo()
                Spacer(modifier = Modifier.weight(1f))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.dont_have_an_account),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Light,
                    )
                    Text(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .noRippleClickable {
                                // navigate to register with phoneNumber and email
                                navController.navigate(
                                    Routes.withArgs(
                                        Routes.REGISTER,
                                        state.user.phoneNumber,
                                        state.user.email
                                    )
                                )
                            },
                        text = stringResource(R.string.sign_up),
                        style = MaterialTheme.typography.body2,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
                    onClick = {
                        (context as? Activity)?.let {
                            viewModel.onEvent(
                                LoginEvent.Login(
                                    phoneNumber = phoneNumber.value,
                                ),

                                )
                        }
                    },
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = MaterialTheme.colors.onBackground,
                        contentColor = MaterialTheme.colors.background,
                    ),
                ) {
                    if (state.isLoadingLogin) {
                        Loading(
                            modifier = Modifier
                                .height(16.dp)
                                .width(16.dp),
                            color = MaterialTheme.colors.background
                        )
                    } else {
                        Text(
                            text = stringResource(R.string.login),
                            style = MaterialTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
                GoogleButton(onClick = {
                    (context as? Activity)?.let {
                        viewModel.onEvent(
                            LoginEvent.LoginWithGoogle(
                                activity = it,
                            )
                        )
                    }
                })
                Text(
                    text = state.error ?: "",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.error
                )
            }
        }
    }
}

@Composable
fun AppLogo() {
    if (MaterialTheme.colors.isLight) {
        Image(
            painter = painterResource(id = R.drawable.jeruchess_light),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    } else {
        Image(
            painter = painterResource(id = R.drawable.jeruchess_dark),
            contentDescription = stringResource(R.string.app_name),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )
    }
}

//
@Composable
fun GoogleButton(onClick: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
    {
        Button(
            onClick = onClick,
            modifier = Modifier.size(75.dp),
            shape = CircleShape,
            contentPadding = PaddingValues(),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colors.background,
                backgroundColor = MaterialTheme.colors.onBackground
            )
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_logo_google),
                contentDescription = ""
            )
        }
    }
}