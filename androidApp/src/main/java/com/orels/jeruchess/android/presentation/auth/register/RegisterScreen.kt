package com.orels.jeruchess.android.presentation.auth.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.presentation.components.ActionButton
import com.orels.jeruchess.android.presentation.components.BackPressHandler
import com.orels.jeruchess.android.presentation.components.Input

private enum class Stage {
    NAME, EMAIL, PASSWORD, DONE, ERROR
}

// this will show the stages order, not as a string
private val registrationProcess: List<Stage> = listOf(
    Stage.NAME,
    Stage.EMAIL,
    Stage.PASSWORD,
    Stage.DONE
)

private fun Stage.next(): Stage {
    val nextIndex = registrationProcess.indexOf(this) + 1
    return if (nextIndex < registrationProcess.size) {
        registrationProcess[nextIndex]
    } else {
        this
    }
}

private fun Stage.previous(): Stage {
    val previousIndex = registrationProcess.indexOf(this) - 1
    return if (previousIndex >= 0) {
        registrationProcess[previousIndex]
    } else {
        this
    }
}

@Composable
fun RegisterScreen(
    navController: NavController,
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordConfirm by remember { mutableStateOf("") }
    var stage by remember { mutableStateOf(Stage.NAME) }
    var backPressed by remember { mutableStateOf(false) }

    BackPressHandler {
        backPressed = true
        if (stage != (registrationProcess.firstOrNull() ?: Stage.NAME)) {
            stage = stage.previous()
        } else {
            navController.popBackStack()
        }
    }

    AnimateContent(
        shouldShow = stage == Stage.NAME,
    ) {
        GetName(
            onNameEntered = { first, last ->
                firstName = first
                lastName = last
                stage = stage.next()
            }
        )
    }
    AnimateContent(
        shouldShow = stage == Stage.EMAIL,
    ) {
        GetEmail(
            onEmailEntered = { mail ->
                email = mail
                stage = stage.next()
            }
        )
    }

    AnimateContent(
        shouldShow = stage == Stage.PASSWORD,
    ) {
        GetPassword(
            onPasswordEntered = { pass, passConfirm ->
                password = pass
                passwordConfirm = passConfirm
                stage = stage.next()
            }
        )
    }
}

@Composable
fun GetName(onNameEntered: (firstName: String, lastName: String) -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var firstNameError by remember { mutableStateOf(false) }
    var lastName by remember { mutableStateOf("") }
    var lastNameError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        Text(
            text = stringResource(R.string.what_is_your_name),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground
        )
        Input(
            title = stringResource(R.string.first_name),
            minLines = 1,
            maxLines = 1,
            isError = firstNameError,
            isPassword = false,
            onTextChange = {
                firstName = it
                firstNameError = false
            }
        )
        Input(
            title = stringResource(R.string.last_name),
            minLines = 1,
            maxLines = 1,
            isError = lastNameError,
            isPassword = false,
            onTextChange = {
                lastName = it
                lastNameError = false
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                onNameEntered(firstName, lastName)
                if (firstName.isNotBlank() && lastName.isNotBlank()) {
                    onNameEntered(firstName, lastName)
                } else {
                    firstNameError = firstName.isBlank()
                    lastNameError = lastName.isBlank()
                }
            }, text = stringResource(R.string.next)
        )
    }
}


@Composable
fun GetEmail(onEmailEntered: (email: String) -> Unit) {
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        Text(
            text = stringResource(R.string.what_is_your_email),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground
        )
        Input(
            title = stringResource(R.string.email),
            minLines = 1,
            maxLines = 1,
            isError = emailError,
            isPassword = false,
            onTextChange = {
                email = it
                emailError = false
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                if (email.isNotBlank()) {
                    onEmailEntered(email)
                } else {
                    emailError = email.isBlank()
                }
            }, text = stringResource(R.string.next)
        )
    }
}

@Composable
fun GetPassword(onPasswordEntered: (password: String, confirmPassword: String) -> Unit) {
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf(false) }
    var confirmPassword by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        Text(
            text = stringResource(R.string.what_is_your_name),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground
        )
        Input(
            title = stringResource(R.string.password),
            minLines = 1,
            maxLines = 1,
            isError = passwordError,
            isPassword = false,
            onTextChange = {
                password = it
                passwordError = false
            }
        )
        Input(
            title = stringResource(R.string.confirm_password),
            minLines = 1,
            maxLines = 1,
            isError = confirmPasswordError,
            isPassword = false,
            onTextChange = {
                confirmPassword = it
                confirmPasswordError = false
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                if (password.isNotBlank() && confirmPassword.isNotBlank()) {
                    onPasswordEntered(password, confirmPassword)
                } else {
                    passwordError = password.isBlank()
                    confirmPasswordError = confirmPassword.isBlank()
                }
            }, text = stringResource(R.string.next)
        )
    }
}

@Composable
fun AnimateContent(
    shouldShow: Boolean,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = shouldShow,
        enter = slideInHorizontally(
            initialOffsetX = { fullWidth -> 2 * fullWidth },
            animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { fullWidth ->  -fullWidth },
            animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
        )
    ) {
        content()
    }
}