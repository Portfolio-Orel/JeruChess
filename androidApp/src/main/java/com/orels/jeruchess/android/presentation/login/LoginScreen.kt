package com.orels.jeruchess.android.presentation.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.orels.jeruchess.android.R
import com.orels.jeruchess.android.presentation.components.ActionButton
import com.orels.jeruchess.android.presentation.components.Input
import com.orels.jeruchess.android.presentation.login.components.forgot_password.ForgotPasswordComponent

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    if (state.isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .height(48.dp)
                    .width(48.dp),
                strokeWidth = 2.dp,
                color = MaterialTheme.colors.onBackground
            )
        }
    } else {
        ContentView(viewModel = viewModel)
    }
}

@Composable
private fun ContentView(viewModel: LoginViewModel) {
    val state = viewModel.state.value
    val context = LocalContext.current

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
                title = stringResource(R.string.username),
                placeholder = stringResource(R.string.username),
                initialText = "",
                isPassword = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        stringResource(R.string.username)
                    )
                },
                onTextChange = { viewModel.onUsernameChange(it) }
            )
            Input(
                title = stringResource(R.string.password),
                placeholder = stringResource(R.string.password),
                initialText = "",
                isPassword = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Lock,
                        stringResource(R.string.password_icon)
                    )
                },
                onTextChange = { viewModel.onPasswordChange(it) })
            ForgotPasswordComponent()
            Spacer(Modifier.weight(1f))
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 16.dp, top = 8.dp),
                onClick = { },
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = MaterialTheme.colors.onBackground,
                    contentColor = MaterialTheme.colors.background,
                ),
            ) {
                Text(
                    text = stringResource(R.string.login),
                    style = MaterialTheme.typography.body1,
                    fontWeight = FontWeight.SemiBold,
                )
            }
            Text(
                text = stringResource(state.error ?: R.string.empty_string),
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.error
            )
        }
    }
}
//
//@Composable
//fun GoogleButton(onClick: () -> Unit) {
//    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center)
//    {
//        Button(
//            onClick = onClick,
//            modifier = Modifier.size(75.dp),
//            shape = CircleShape,
//            contentPadding = PaddingValues(),
//            colors = ButtonDefaults.buttonColors(
//                contentColor = MaterialTheme.colors.background,
//                backgroundColor = MaterialTheme.colors.onBackground
//            )
//        ) {
//            Image(
//                painter = painterResource(id = R.drawable.ic_logo_google),
//                contentDescription = ""
//            )
//        }
//    }
//}

@Composable
fun RegistrationScreen(
    onRegister: (firstName: String, lastName: String) -> Unit
) {
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
                if (firstName.isNotBlank() && lastName.isNotBlank()) {
                    onRegister(firstName, lastName)
                } else {
                    firstNameError = firstName.isBlank()
                    lastNameError = lastName.isBlank()
                }
            }, text = stringResource(R.string.finish)
        )
    }
}