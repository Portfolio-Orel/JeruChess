package com.orels.jeruchess.android.presentation.auth.register

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.presentation.components.ActionButton
import com.orels.jeruchess.android.presentation.components.BackPressHandler
import com.orels.jeruchess.android.presentation.components.CustomKeyboardType
import com.orels.jeruchess.android.presentation.components.Input
import com.orels.jeruchess.main.domain.model.Gender
import com.orels.jeruchess.utils.Formatters
import com.orels.jeruchess.utils.Validators
import java.util.*

private enum class Stage {
    BASIC_INFORMATION, EMAIL_NUMBER, CONFIRMATION, DONE, ERROR
}

// this will show the stages order, not as a string
private val registrationProcess: List<Stage> = listOf(
    Stage.BASIC_INFORMATION,
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
    preInsertedPhoneNumber: String = "",
    preInsertedEmail: String = "",
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var backPressed by remember { mutableStateOf(false) }

    val state = viewModel.state
    val stage = remember { mutableStateOf(Stage.BASIC_INFORMATION) }

    LaunchedEffect(key1 = preInsertedEmail) {
        viewModel.onEvent(RegisterEvent.SetEmail(preInsertedEmail))
    }

    LaunchedEffect(key1 = preInsertedPhoneNumber) {
        viewModel.onEvent(RegisterEvent.SetPhoneNumber(preInsertedPhoneNumber))
    }

    BackPressHandler {
        backPressed = true
        if (stage.value != (registrationProcess.firstOrNull() ?: Stage.BASIC_INFORMATION)) {
            stage.value = stage.value.previous()
        } else {
            navController.popBackStack()
        }
    }

    AnimateContent(
        shouldShow = stage.value == Stage.BASIC_INFORMATION,
    ) {
        GetBasicInformation(
            gender = state.gender,
            firstName = state.firstName,
            lastName = state.lastName,
            dateOfBirth = state.dateOfBirth,
            onNameEntered = { firstName, lastName, gender, dateOfBirth ->
                viewModel.onEvent(RegisterEvent.SetFirstName(firstName))
                viewModel.onEvent(RegisterEvent.SetLastName(lastName))
                viewModel.onEvent(RegisterEvent.SetGender(gender))
                viewModel.onEvent(RegisterEvent.SetDateOfBirth(dateOfBirth))
                stage.value = stage.value.next()
            }
        )
    }
    AnimateContent(
        shouldShow = stage.value == Stage.EMAIL_NUMBER,
    ) {
        GetEmailAndPhoneNumber(
            email = state.email,
            phoneNumber = state.phoneNumber,
            isEmailDisabled = preInsertedEmail.isNotBlank(),
            isPhoneNumberDisabled = preInsertedPhoneNumber.isNotBlank(),
            onDetailsEntered = { mail, number ->
                viewModel.onEvent(RegisterEvent.SetEmail(mail))
                viewModel.onEvent(RegisterEvent.SetPhoneNumber(number))
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
fun GetBasicInformation(
    onNameEntered: (firstName: String, lastName: String, gender: Gender, dateOfBirth: Long) -> Unit,
    gender: Gender? = null,
    dateOfBirth: Long = 0L,
    firstName: String = "",
    lastName: String = "",
) {
    var firstNameValue by remember { mutableStateOf(firstName) }
    var firstNameError by remember { mutableStateOf(false) }

    var lastNameValue by remember { mutableStateOf(lastName) }
    var lastNameError by remember { mutableStateOf(false) }

    var genderValue by remember { mutableStateOf(gender) }

    var dateOfBirthValue by remember { mutableStateOf(dateOfBirth) }
    var dateOfBirthError by remember { mutableStateOf(false) }

    var formattedDateOfBirth = if (dateOfBirthValue != 0L) Date(dateOfBirthValue).toString() else ""
    formattedDateOfBirth = Formatters.toDateString(formattedDateOfBirth)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        Text(
            text = stringResource(R.string.lets_get_to_know_better),
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground
        )
        Input(
            title = stringResource(R.string.first_name),
            placeholder = stringResource(R.string.placeholder_first_name),
            minLines = 1,
            maxLines = 1,
            isError = firstNameError,
            initialText = firstNameValue,
            isPassword = false,
            onTextChange = {
                firstNameValue = it
            }
        )
        Input(
            title = stringResource(R.string.last_name),
            placeholder = stringResource(R.string.placeholder_last_name),
            minLines = 1,
            maxLines = 1,
            isError = lastNameError,
            initialText = lastNameValue,
            isPassword = false,
            onTextChange = {
                lastNameValue = it
            }
        )
        Input(
            title = stringResource(R.string.date_of_birth),
            minLines = 1,
            maxLines = 1,
            isError = dateOfBirthError,
            initialText = if (dateOfBirthValue != 0L) formattedDateOfBirth else stringResource(R.string.empty_string),
            placeholder = Formatters.toDateString(stringResource(R.string.default_date_of_birth)),
            isPassword = false,
            keyboardType = CustomKeyboardType.Date,
            onTextChange = {
                lastNameValue = it
            },
            formatter = { Formatters.toDateString(it) },
        )
        SubTitle(text = stringResource(R.string.gender))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Gender.values().forEach { gender ->
                GenderContainer(
                    gender = gender,
                    selected = genderValue == gender,
                    onClick = {
                        genderValue = it
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                lastNameError = false
                firstNameError = false

                if (firstNameValue.isNotBlank() && lastNameValue.isNotBlank() && dateOfBirthValue != 0L && genderValue != null) {
                    onNameEntered(firstNameValue, lastNameValue, genderValue!!, dateOfBirthValue)
                } else {
                    firstNameError = firstNameValue.isBlank()
                    lastNameError = lastNameValue.isBlank()
                    dateOfBirthError = dateOfBirthValue == 0L
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
            text = stringResource(R.string.lets_get_to_know_better),
            style = MaterialTheme.typography.h4,
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

@Composable
fun SubTitle(
    text: String,
) {
    Text(
        modifier = Modifier.fillMaxWidth(),
        text = text,
        style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
        color = MaterialTheme.colors.onBackground,
        textAlign = TextAlign.Start
    )
}

@Composable
fun GenderContainer(
    gender: Gender,
    selected: Boolean,
    onClick: (Gender) -> Unit,
) {
    val drawable = when (gender) {
        Gender.Male -> R.drawable.male
        Gender.Female -> R.drawable.female
        Gender.None -> R.drawable.sex_none
    }
    val contentDescription = when (gender) {
        Gender.Male -> R.string.male
        Gender.Female -> R.string.female
        Gender.None -> R.string.none
    }

    val selectedColors = listOf(
        Color(MaterialTheme.colors.primary.copy(alpha = 0.9f).value),
        Color(MaterialTheme.colors.primary.copy(alpha = 0.85f).value),
        Color(MaterialTheme.colors.primary.copy(alpha = 0.8f).value),
    )
    val unselectedColors = listOf(
        Color(MaterialTheme.colors.onBackground.copy(alpha = 0.3f).value),
        Color(MaterialTheme.colors.onBackground.copy(alpha = 0.3f).value),
        Color(MaterialTheme.colors.onBackground.copy(alpha = 0.3f).value),
    )
    val colors = if (selected) selectedColors else unselectedColors

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = colors,
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY,
                    ),
                    shape = RoundedCornerShape(15.dp)
                )
                .padding(6.dp)
                .clickable { onClick(gender) }
        ) {
            Image(
                modifier = Modifier.size(64.dp),
                painter = painterResource(id = drawable),
                contentDescription = stringResource(contentDescription),
            )
        }
        Text(
            text = if (selected) stringResource(id = contentDescription) else stringResource(id = R.string.empty_string),
            style = MaterialTheme.typography.body1,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}