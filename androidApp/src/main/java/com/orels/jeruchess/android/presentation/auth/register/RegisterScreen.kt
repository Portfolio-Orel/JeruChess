package com.orels.jeruchess.android.presentation.auth.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.presentation.components.ActionButton
import com.orels.jeruchess.android.presentation.components.BackPressHandler
import com.orels.jeruchess.android.presentation.components.Input
import com.orels.jeruchess.utils.Validators

private enum class Stage {
    NAME, EMAIL_NUMBER, CONFIRMATION, DONE, ERROR
}

// this will show the stages order, not as a string
private val registrationProcess: List<Stage> = listOf(
    Stage.NAME,
    Stage.EMAIL_NUMBER,
    Stage.CONFIRMATION,
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
    var phoneNumber by remember { mutableStateOf("") }
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

//    AnimateContent(
//        shouldShow = stage == Stage.NAME,
//    ) {
//        GetName(
//            onNameEntered = { first, last ->
//                firstName = first
//                lastName = last
//                stage = stage.next()
//            }
//        )
//    }
    AnimateContent(
        shouldShow = stage == Stage.EMAIL_NUMBER,
    ) {
        GetEmailAndPhoneNumber(
            onDetailsEntered = { mail, number ->
                email = mail
                phoneNumber = number
                stage = stage.next()
            },
            validateEmail = { Validators.isEmailValid(it) },
            validatePhoneNumber = { Validators.isPhoneNumberValid(it) }
        )
    }

    AnimateContent(shouldShow = stage == Stage.NAME) {
        ConfirmationCodeDialog(onConfirm = {}) {
            stage = stage.next()
        }
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
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                lastNameError = false
                firstNameError = false

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
fun GetEmailAndPhoneNumber(
    onDetailsEntered: (email: String, number: String) -> Unit,
    validateEmail: (String) -> Boolean,
    validatePhoneNumber: (String) -> Boolean
) {
    var number by remember { mutableStateOf("") }
    var numberError by remember { mutableStateOf(false) }
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
        Input(
            title = stringResource(R.string.phone_number),
            minLines = 1,
            maxLines = 1,
            isError = numberError,
            isPassword = false,
            onTextChange = {
                number = it
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                emailError = false
                numberError = false
                if (!validateEmail(email)) {
                    emailError = true
                }
                if (!validatePhoneNumber(number)) {
                    numberError = true
                }
                if (!emailError && !numberError && email.isNotBlank() && number.isNotBlank()) {
                    onDetailsEntered(email, number)
                } else {
                    emailError = email.isBlank()
                    numberError = number.isBlank()
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
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                passwordError = false
                confirmPasswordError = false

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
fun ConfirmationCodeDialog(
    onConfirm: (code: String) -> Unit,
    onDismiss: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val code = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { Array(6) { FocusRequester() } }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val spaceBetweenInputs = 4.dp
    val inputSize = (screenWidth - spaceBetweenInputs * 12) / 6
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(cursorColor = Color.Transparent)

    Column {
        Row(horizontalArrangement = Arrangement.Center) {
            for (i in 0 until 6) {
                OutlinedTextField(
                    value = code[i],
                    onValueChange = { newValue ->
                        if (code[i].isNotEmpty() && newValue.length > 1) {
                            code[i] = newValue.replaceFirst(code[i], "").lastOrNull().toString()
                        } else {
                            code[i] = newValue
                        }
                        if (code[i].length == 1 && i < 5) {
                            focusRequesters[i + 1].requestFocus()
                        }
                    },
                    modifier = Modifier
                        .padding(horizontal = spaceBetweenInputs)
                        .size(inputSize)
                        .focusRequester(focusRequesters[i]),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusRequesters[i + 1].requestFocus()
                    }),
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    ),
                    colors = textFieldColors
                )
            }
        }
        ActionButton(
            onClick = {
                val fullCode = code.joinToString(separator = "")
                onConfirm(fullCode)
            },
            modifier = Modifier.padding(vertical = 16.dp),
            text = stringResource(id = R.string.confirm)
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
            targetOffsetX = { fullWidth -> -fullWidth },
            animationSpec = tween(durationMillis = 150, easing = FastOutLinearInEasing)
        )
    ) {
        content()
    }
}