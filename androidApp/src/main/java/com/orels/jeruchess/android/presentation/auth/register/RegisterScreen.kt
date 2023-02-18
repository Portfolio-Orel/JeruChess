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
    preInsertedPhoneNumber: String,
    preInsertedEmail: String
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(preInsertedEmail) }
    var phoneNumber by remember { mutableStateOf(preInsertedPhoneNumber) }
    var backPressed by remember { mutableStateOf(false) }
    val stage = remember { mutableStateOf(Stage.NAME) }

    BackPressHandler {
        backPressed = true
        if (stage.value != (registrationProcess.firstOrNull() ?: Stage.NAME)) {
            stage.value = stage.value.previous()
        } else {
            navController.popBackStack()
        }
    }

    AnimateContent(
        shouldShow = stage.value == Stage.NAME,
    ) {
        GetName(
            firstName = firstName,
            lastName = lastName,
            onNameEntered = { first, last ->
                firstName = first
                lastName = last
                stage.value = stage.value.next()
            }
        )
    }
    AnimateContent(
        shouldShow = stage.value == Stage.EMAIL_NUMBER,
    ) {
        GetEmailAndPhoneNumber(
            email = email,
            phoneNumber = phoneNumber,
            isEmailDisabled = preInsertedEmail.isNotBlank(),
            isPhoneNumberDisabled = preInsertedPhoneNumber.isNotBlank(),
            onDetailsEntered = { mail, number ->
                email = mail
                phoneNumber = number
                stage.value = stage.value.next()
            },
            validateEmail = { Validators.isEmailValid(it) },
            validatePhoneNumber = { Validators.isPhoneNumberValid(it) }
        )
    }

    AnimateContent(shouldShow = stage.value == Stage.CONFIRMATION) {
        ConfirmationCodeDialog {
            stage.value = stage.value.next()
        }
    }
}

@Composable
fun GetName(
    onNameEntered: (firstName: String, lastName: String) -> Unit,
    firstName: String = "",
    lastName: String = ""
) {
    val firstNameValue = remember { mutableStateOf(firstName) }
    val firstNameError = remember { mutableStateOf(false) }
    val lastNameValue = remember { mutableStateOf(lastName) }
    val lastNameError = remember { mutableStateOf(false) }

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
            isError = firstNameError.value,
            initialText = firstNameValue.value,
            isPassword = false,
            onTextChange = {
                firstNameValue.value = it
            }
        )
        Input(
            title = stringResource(R.string.last_name),
            minLines = 1,
            maxLines = 1,
            isError = lastNameError.value,
            initialText = lastNameValue.value,
            isPassword = false,
            onTextChange = {
                lastNameValue.value = it
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                lastNameError.value = false
                firstNameError.value = false

                if (firstNameValue.value.isNotBlank() && lastNameValue.value.isNotBlank()) {
                    onNameEntered(firstNameValue.value, lastNameValue.value)
                } else {
                    firstNameError.value = firstNameValue.value.isBlank()
                    lastNameError.value = lastNameValue.value.isBlank()
                }
            }, text = stringResource(R.string.next)
        )
    }
}


@Composable
fun GetEmailAndPhoneNumber(
    onDetailsEntered: (email: String, number: String) -> Unit,
    validateEmail: (String) -> Boolean,
    isEmailDisabled: Boolean = false,
    isPhoneNumberDisabled: Boolean = false,
    validatePhoneNumber: (String) -> Boolean,
    email: String = "",
    phoneNumber: String = "",
) {
    var number by remember { mutableStateOf(phoneNumber) }
    var numberError by remember { mutableStateOf(false) }
    var emailValue by remember { mutableStateOf(email) }
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
            initialText = emailValue,
            isPassword = false,
            onTextChange = {
                emailValue = it
                emailError = false
            },
            isDisabled = isEmailDisabled
        )
        Input(
            title = stringResource(R.string.phone_number),
            minLines = 1,
            maxLines = 1,
            isError = numberError,
            initialText = number,
            isPassword = false,
            onTextChange = {
                number = it
            },
            isDisabled = isPhoneNumberDisabled
        )

        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                emailError = false
                numberError = false
                if (!validateEmail(emailValue)) {
                    emailError = true
                }
                if (!validatePhoneNumber(number)) {
                    numberError = true
                }
                if (!emailError && !numberError && emailValue.isNotBlank() && number.isNotBlank()) {
                    onDetailsEntered(emailValue, number)
                } else {
                    emailError = emailValue.isBlank()
                    numberError = number.isBlank()
                }
            }, text = stringResource(R.string.next)
        )
    }
}


@Composable
fun ConfirmationCodeDialog(
    onConfirm: (code: String) -> Unit,
) {
    val codeInputs = 6
    val code = remember { mutableStateOf(Array(codeInputs) { "" }) }
    val focusRequesters = remember { Array(codeInputs) { FocusRequester() } }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val spaceBetweenInputs = 4.dp
    val inputSize = (screenWidth - spaceBetweenInputs * (codeInputs * 2)) / codeInputs
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(cursorColor = Color.Transparent)

    Column {
        Row(horizontalArrangement = Arrangement.Center) {
            for (i in 0 until codeInputs) {
                OutlinedTextField(
                    value = code.value[i],
                    onValueChange = { newValue ->
                        if (code.value[i].isNotEmpty() && newValue.length > 1) {
                            code.value[i] =
                                newValue.replaceFirst(code.value[i], "").lastOrNull().toString()
                        } else {
                            code.value[i] = newValue
                        }
                        if (code.value[i].length == 1 && i < codeInputs - 1) {
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
                        imeAction = if (i < codeInputs - 1) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onNext = {
                        focusRequesters[i + 1].requestFocus()
                    },
                        onDone = {
                            val fullCode = code.value.joinToString(separator = "")
                            onConfirm(fullCode)
                        }
                    ),
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
                val fullCode = code.value.joinToString(separator = "")
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