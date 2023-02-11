package com.orels.jeruchess.android.presentation.auth.forgot_password

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.presentation.components.AuthenticationInput
import com.orels.jeruchess.android.presentation.components.Loading

@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    viewModel: ForgotPasswordViewModel = hiltViewModel(),
) {
    val state = viewModel.state

    var username by rememberSaveable { mutableStateOf("") }

    var code by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    AnimatedVisibility(
        visible = (state.state is State.ForgotPassword),
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> 2 * fullWidth },
            animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
        )
    ) {
        ForgotPasswordContent(
            isLoading = state.state.isLoading,
            username = username,
            onUsernameChange = { username = it },
            onForgotPassword = viewModel::onForgotPassword
        )
    }

    AnimatedVisibility(
        visible = (state.state is State.ResetPassword),
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> 2 * fullWidth },
            animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
        )
    ) {
        ResetPasswordContent(
            code = code,
            password = password,
            confirmPassword = confirmPassword,
            onCodeChange = { code = it },
            onPasswordChange = { password = it },
            onConfirmPasswordChange = { confirmPassword = it },
            isLoading = state.state.isLoading,
            onResetPassword = viewModel::onResetPassword
        )
    }

    AnimatedVisibility(
        visible = (state.state is State.Done),
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth -> 2 * fullWidth },
            animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
        )
    ) {
        DoneContent(
            onDone = {
                navController.popBackStack()
            }
        )
    }
}

@Composable
fun ForgotPasswordContent(
    isLoading: Boolean,
    username: String,
    onUsernameChange: (String) -> Unit,
    onForgotPassword: (String) -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = stringResource(R.string.forgot_the_password),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.forgot_password_message),
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Thin,
        )

        AuthenticationInput(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp),
            placeholder = stringResource(R.string.username),
            value = username,
            onValueChange = { if (!isLoading) onUsernameChange(it) },
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
            onClick = { if (!isLoading) onForgotPassword(username) },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.background,
            ),
        ) {
            if (isLoading) {
                Loading(size = 16.dp, color = MaterialTheme.colors.background)
            } else {
                Text(
                    text = stringResource(R.string.reset_password),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
fun ResetPasswordContent(
    code: String,
    password: String,
    confirmPassword: String,
    onCodeChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onConfirmPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    onResetPassword: ((code: String, password: String, confirmPassword: String) -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = stringResource(R.string.change_the_password),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = "Insert the code sent to your email and create a new password",
            style = MaterialTheme.typography.body2,
            fontWeight = FontWeight.Thin,
        )

        AuthenticationInput(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp),
            placeholder = stringResource(R.string.code),
            value = code,
            onValueChange = { if (!isLoading) onCodeChange(it) }
        )

        AuthenticationInput(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp),
            placeholder = stringResource(R.string.password),
            value = password,
            onValueChange = { if (!isLoading) onPasswordChange(it) },
            isPassword = true
        )
        AuthenticationInput(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 24.dp),
            placeholder = stringResource(R.string.confirm_password),
            value = confirmPassword,
            onValueChange = { if (!isLoading) onConfirmPasswordChange(it) },
            isPassword = true
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
            onClick = { onResetPassword?.invoke(code, password, confirmPassword) },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.background,
            ),
        ) {
            if (isLoading) {
                Loading(size = 16.dp, color = MaterialTheme.colors.background)
            } else {
                Text(
                    text = stringResource(R.string.reset_password),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.Normal,
                )
            }
        }
    }
}

@Composable
fun DoneContent(onDone: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 16.dp),
            text = stringResource(R.string.done_exclamation),
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold,
        )
        Text(
            modifier = Modifier.padding(horizontal = 16.dp),
            text = stringResource(R.string.your_password_has_been_changed),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Thin,
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
            onClick = { onDone() },
            shape = MaterialTheme.shapes.medium,
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.background,
            ),
        ) {
            Text(
                text = stringResource(R.string.lets_go),
                style = MaterialTheme.typography.body2,
                fontWeight = FontWeight.Normal,
            )
        }
    }
}