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
import com.orels.jeruchess.android.core.presentation.Screens
import com.orels.jeruchess.android.presentation.components.ActionButton
import com.orels.jeruchess.android.presentation.components.BackPressHandler
import com.orels.jeruchess.android.presentation.components.CustomKeyboardType
import com.orels.jeruchess.android.presentation.components.Input
import com.orels.jeruchess.android.presentation.util.isDateValid
import com.orels.jeruchess.android.presentation.util.registerString
import com.orels.jeruchess.android.presentation.util.toRegisterDate
import com.orels.jeruchess.android.presentation.util.toRegisterDateLong
import com.orels.jeruchess.main.domain.model.Gender
import com.orels.jeruchess.utils.Formatters
import com.orels.jeruchess.utils.Validators
import java.util.*

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    var backPressed by remember { mutableStateOf(false) }

    val state = viewModel.state

    BackPressHandler {
        backPressed = true
        if (state.stage != (registrationProcess.firstOrNull() ?: Stage.BASIC_INFORMATION)) {
            viewModel.onEvent(RegisterEvent.PreviousStage)
        } else {
            navController.popBackStack()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        AnimateContent(
            shouldShow = state.stage == Stage.BASIC_INFORMATION,
        ) {
            GetBasicInformation(
                gender = state.gender,
                firstName = state.firstName,
                lastName = state.lastName,
                dateOfBirth = Date(state.dateOfBirth).toString(),
                onNameEntered = { firstName, lastName, gender, dateOfBirth ->
                    viewModel.onEvent(RegisterEvent.SetFirstName(firstName))
                    viewModel.onEvent(RegisterEvent.SetLastName(lastName))
                    viewModel.onEvent(RegisterEvent.SetGender(gender))
                    viewModel.onEvent(RegisterEvent.SetDateOfBirth(dateOfBirth))
                    viewModel.onEvent(RegisterEvent.CompleteRegistration)
                },
                isLoading = state.isLoading,
            )
        }
        AnimateContent(
            shouldShow = state.stage == Stage.EMAIL_NUMBER,
        ) {
            GetLoginDetails(
                initialEmail = state.email,
                initialPhoneNumber = state.phoneNumber,
                isLoading = state.isLoading,
                onDetailsEntered = { mail, number, username, password, confirmPassword ->
                    viewModel.onEvent(RegisterEvent.SetEmail(mail))
                    viewModel.onEvent(RegisterEvent.SetPhoneNumber(number))
                    viewModel.onEvent(RegisterEvent.SetUsername(username))
                    viewModel.onEvent(RegisterEvent.SetPassword(password))
                    viewModel.onEvent(RegisterEvent.SetConfirmPassword(confirmPassword))
                    viewModel.onEvent(RegisterEvent.Register)
                },
                validateEmail = { Validators.isEmailValid(it) },
                validatePhoneNumber = { Validators.isPhoneNumberValid(it) }
            )
        }

        AnimateContent(shouldShow = state.stage == Stage.CONFIRMATION && state.isLoading.not()) {
            ConfirmationCodeDialog {
                viewModel.onEvent(RegisterEvent.ConfirmCode(it))
            }
        }

        AnimateContent(shouldShow = state.stage == Stage.DONE) {
            DoneContent(
                onDone = {
                    navController.navigate(Screens.Main.route) {
                        popUpTo(Screens.Register.route) { inclusive = true }
                    }
                },
                isLoading = state.isLoading
            )

//            if (state.error != null) {
//                Text(
//                    text = stringResource(id = state.error),
//                    color = MaterialTheme.colors.error,
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth()
//                        .wrapContentHeight()
//                        .background(MaterialTheme.colors.background)
//                )
//            }
        }
    }
}

@Composable
fun GetBasicInformation(
    onNameEntered: (firstName: String, lastName: String, gender: Gender, dateOfBirth: Long) -> Unit,
    gender: Gender? = null,
    dateOfBirth: String = "",
    firstName: String = "",
    lastName: String = "",
    isLoading: Boolean = false
) {
    val firstNameValue = remember { mutableStateOf(firstName) }
    val firstNameError = remember { mutableStateOf(false) }

    val lastNameValue = remember { mutableStateOf(lastName) }
    val lastNameError = remember { mutableStateOf(false) }

    val genderValue = remember { mutableStateOf(gender) }

    val dateOfBirthValue = remember {
        mutableStateOf(
            dateOfBirth.toRegisterDate()?.registerString ?: ""
        )
    }
    val dateOfBirthError = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        Text(
            text = stringResource(R.string.lets_get_to_know),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onBackground
        )
        Input(
            title = stringResource(R.string.first_name),
            placeholder = stringResource(R.string.placeholder_first_name),
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
            placeholder = stringResource(R.string.placeholder_last_name),
            minLines = 1,
            maxLines = 1,
            isError = lastNameError.value,
            initialText = lastNameValue.value,
            isPassword = false,
            onTextChange = {
                lastNameValue.value = it
            }
        )
        Input(
            title = stringResource(R.string.date_of_birth),
            minLines = 1,
            maxLines = 1,
            isError = dateOfBirthError.value,
            initialText = Formatters.toDateString(dateOfBirthValue.value),
            placeholder = Formatters.toDateString(stringResource(R.string.default_date_of_birth)),
            isPassword = false,
            keyboardType = CustomKeyboardType.Date,
            onTextChange = {
                dateOfBirthValue.value = it
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
                    selected = genderValue.value == gender,
                    onClick = {
                        genderValue.value = it
                    },
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                lastNameError.value = false
                firstNameError.value = false
                if (firstNameValue.value.isNotBlank() && lastNameValue.value.isNotBlank() && dateOfBirthValue.value.isDateValid() && genderValue.value != null) {
                    onNameEntered(
                        firstNameValue.value,
                        lastNameValue.value,
                        genderValue.value!!,
                        dateOfBirthValue.value.toRegisterDateLong()
                    )
                } else {
                    firstNameError.value = firstNameValue.value.isBlank()
                    lastNameError.value = lastNameValue.value.isBlank()
                    dateOfBirthError.value = dateOfBirthValue.value.isBlank()
                }
            }, text = stringResource(R.string.next),
            isLoading = isLoading
        )
    }
}


@Composable
fun GetLoginDetails(
    onDetailsEntered: (email: String, number: String, username: String, password: String, confirmPassword: String) -> Unit,
    validateEmail: (String) -> Boolean,
    isEmailDisabled: Boolean = false,
    isPhoneNumberDisabled: Boolean = false,
    validatePhoneNumber: (String) -> Boolean,
    isLoading: Boolean = false,
    initialEmail: String = "thischessapp@gmail.com",
    initialPhoneNumber: String = "0543056286",
    initialUsername: String = "orelz7",
    ) {

    val phoneNumber = remember { mutableStateOf(initialPhoneNumber) }
    val phoneNumberError = remember { mutableStateOf(false) }
    val phoneNumberFocusRequest = remember { FocusRequester() }

    val emailValue = remember { mutableStateOf(initialEmail) }
    val emailError = remember { mutableStateOf(false) }
    val emailFocusRequester = remember { FocusRequester() }

    val usernameValue = remember { mutableStateOf(initialUsername) }
    val usernameError = remember { mutableStateOf(false) }
    val usernameFocusRequester = remember { FocusRequester() }

    val passwordValue = remember { mutableStateOf("00220022") }
    val passwordError = remember { mutableStateOf(false) }
    val passwordFocusRequester = remember { FocusRequester() }

    val confirmPasswordValue = remember { mutableStateOf("00220022") }
    val confirmPasswordError = remember { mutableStateOf(false) }
    val confirmPasswordFocusRequester = remember { FocusRequester() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        Text(
            text = stringResource(R.string.some_login_details),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onBackground
        )
        Input(
            title = stringResource(R.string.email),
            minLines = 1,
            maxLines = 1,
            isError = emailError.value,
            initialText = "adsfkasdmfklasd",
            keyboardActions = KeyboardActions(
                onNext = {
                    phoneNumberFocusRequest.requestFocus()
                }
            ),
            isPassword = false,
            onTextChange = {
                emailValue.value = it
                emailError.value = false
            },
            isDisabled = isEmailDisabled,
            keyboardType = CustomKeyboardType.Email,
            focusRequester = emailFocusRequester,
        )
        Input(
            title = stringResource(R.string.phone_number),
            minLines = 1,
            maxLines = 1,
            isError = phoneNumberError.value,
            initialText = phoneNumber.value,
            keyboardActions = KeyboardActions(
                onNext = {
                    usernameFocusRequester.requestFocus()
                }
            ),
            isPassword = false,
            onTextChange = {
                phoneNumber.value = it
            },
            keyboardType = CustomKeyboardType.Phone,
            isDisabled = isPhoneNumberDisabled,
            focusRequester = phoneNumberFocusRequest,
        )
        Input(
            title = stringResource(R.string.username),
            minLines = 1,
            maxLines = 1,
            isError = usernameError.value,
            initialText = usernameValue.value,
            keyboardActions = KeyboardActions(
                onDone = {
                    passwordFocusRequester.requestFocus()
                }
            ),
            isPassword = false,
            onTextChange = {
                usernameValue.value = it
            },
            focusRequester = usernameFocusRequester,
        )
        Input(
            title = stringResource(R.string.password),
            minLines = 1,
            maxLines = 1,
            isError = passwordError.value,
            initialText = passwordValue.value,
            keyboardActions = KeyboardActions(
                onDone = {
                    confirmPasswordFocusRequester.requestFocus()
                }
            ),
            isPassword = true,
            onTextChange = {
                passwordValue.value = it
            },
            focusRequester = passwordFocusRequester,
        )
        Input(
            title = stringResource(R.string.confirm_password),
            minLines = 1,
            maxLines = 1,
            isError = confirmPasswordError.value,
            initialText = confirmPasswordValue.value,
            isPassword = true,
            onTextChange = {
                confirmPasswordValue.value = it
            },
            focusRequester = confirmPasswordFocusRequester,
        )

        Spacer(modifier = Modifier.height(32.dp))
        ActionButton(
            onClick = {
                emailError.value = false
                phoneNumberError.value = false
                if (!validateEmail(emailValue.value)) {
                    emailError.value = true
                }
                if (!validatePhoneNumber(phoneNumber.value)) {
                    phoneNumberError.value = true
                }
                if (!emailError.value &&
                    !phoneNumberError.value &&
                    !usernameError.value &&
                    !passwordError.value &&
                    !confirmPasswordError.value &&
                    usernameValue.value.isNotBlank() &&
                    passwordValue.value.isNotBlank() &&
                    confirmPasswordValue.value.isNotBlank() &&
                    emailValue.value.isNotBlank() &&
                    phoneNumber.value.isNotBlank()) {
                    onDetailsEntered(
                        emailValue.value,
                        phoneNumber.value,
                        usernameValue.value,
                        passwordValue.value,
                        confirmPasswordValue.value
                    )
                } else {
                    emailError.value = emailValue.value.isBlank()
                    phoneNumberError.value = phoneNumber.value.isBlank()
                    usernameError.value = usernameValue.value.isBlank()
                    passwordError.value = passwordValue.value.isBlank()
                    confirmPasswordError.value = confirmPasswordValue.value.isBlank()
                }
            }, text = stringResource(R.string.next),
            isLoading = isLoading
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

    Column(
        modifier = Modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.insert_email_code),
            style = MaterialTheme.typography.h4,
            color = MaterialTheme.colors.onBackground
        )
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
                        keyboardType = KeyboardType.Phone,
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


@Composable
fun DoneContent(onDone: () -> Unit, isLoading: Boolean) {
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
            text = stringResource(R.string.your_account_was_created_exclamation),
            style = MaterialTheme.typography.body1,
            fontWeight = FontWeight.Thin,
        )
        ActionButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 16.dp),
            onClick = { onDone() },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = MaterialTheme.colors.onBackground,
                contentColor = MaterialTheme.colors.background,
            ),
            text = stringResource(R.string.lets_go),
            isLoading = isLoading,
        )
    }
}